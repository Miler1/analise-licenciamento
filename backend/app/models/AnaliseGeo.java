package models;

import br.ufla.lemaf.beans.pessoa.Contato;
import br.ufla.lemaf.beans.pessoa.Pessoa;
import com.itextpdf.text.DocumentException;
import com.vividsolutions.jts.geom.Coordinate;
import deserializers.GeometryDeserializer;
import enums.CamadaGeoEnum;
import exceptions.PortalSegurancaException;
import exceptions.ValidacaoException;
import br.ufla.lemaf.beans.pessoa.Endereco;
import br.ufla.lemaf.beans.pessoa.Municipio;
import br.ufla.lemaf.enums.TipoEndereco;
import models.licenciamento.*;
import models.pdf.PDFGenerator;
import models.tmsmap.LayerType;
import models.tmsmap.MapaImagem;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;
import models.validacaoParecer.*;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import play.data.validation.Required;
import security.cadastrounificado.CadastroUnificadoWS;
import services.IntegracaoEntradaUnicaService;
import utils.*;

import javax.persistence.*;
import javax.validation.ValidationException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static security.Auth.getUsuarioSessao;

@Entity
@Table(schema = "analise", name = "analise_geo")
public class AnaliseGeo extends Analisavel {

    public static final String SEQ = "analise.analise_geo_id_seq";
    private static final String NOME_PERFIL = "Analista Geo";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    @Column(name = "id")
    public Long id;

    @OneToOne
    @JoinColumn(name = "id_analise_geo_revisada", referencedColumnName = "id")
    public AnaliseGeo analiseGeoRevisada;

    @ManyToOne
    @JoinColumn(name = "id_tipo_resultado_validacao")
    public TipoResultadoAnalise tipoResultadoValidacao;

    @ManyToMany
    @JoinTable(schema = "analise", name = "rel_documento_analise_geo",
            joinColumns = @JoinColumn(name = "id_analise_geo"),
            inverseJoinColumns = @JoinColumn(name = "id_documento"))
    public List<Documento> documentos;

    @OneToMany(mappedBy = "analiseGeo", cascade = CascadeType.ALL)
    public List<AnaliseDocumento> analisesDocumentos;

    @OneToMany(mappedBy = "analiseGeo", cascade = CascadeType.ALL)
    public List<AnalistaGeo> analistasGeo;

    @OneToMany(mappedBy = "analiseGeo", orphanRemoval = true)
    public List<LicencaAnalise> licencasAnalise;

    @OneToMany(mappedBy = "analiseGeo", orphanRemoval = true)
    public List<ParecerGeoRestricao> pareceresGeoRestricoes;

    @Column(name = "justificativa_coordenador")
    public String justificativaCoordenador;

    @OneToMany(mappedBy = "analiseGeo", cascade = CascadeType.ALL)
    public List<Coordenador> coordenadores;

    @Required
    @Column(name = "data_cadastro")
    @Temporal(TemporalType.TIMESTAMP)
    public Date dataCadastro;

    @ManyToOne
    @JoinColumn(name = "id_tipo_resultado_validacao_aprovador")
    public TipoResultadoAnalise tipoResultadoValidacaoAprovador;

    @Column(name = "parecer_validacao_aprovador")
    public String parecerValidacaoAprovador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_validacao_aprovador", referencedColumnName = "id")
    public UsuarioAnalise usuarioValidacaoAprovador;

    @Column(name = "data_fim_validacao_aprovador")
    @Temporal(TemporalType.TIMESTAMP)
    public Date dataFimValidacaoAprovador;

    @OneToMany(mappedBy = "analiseGeo", cascade = CascadeType.ALL)
    public List<Inconsistencia> inconsistencias;

    @OneToMany(mappedBy = "analiseGeo", fetch = FetchType.LAZY)
    public List<DesvinculoAnaliseGeo> desvinculos;

    @OneToMany(mappedBy = "analiseGeo", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    public List<Notificacao> notificacoes;

    @OneToMany(mappedBy = "analiseGeo")
    @Fetch(FetchMode.SUBSELECT)
    public List<ParecerCoordenadorAnaliseGeo> pareceresCoordenadorAnaliseGeo;

    @OneToMany(mappedBy = "analiseGeo")
    @Fetch(FetchMode.SUBSELECT)
    public List<ParecerAnalistaGeo> pareceresAnalistaGeo;

    @Transient
    public String linkNotificacao;

    @Transient
    public Integer prazoNotificacao;

    @Transient
    public Long idAnalistaDestino;

    public Long getId() {
        return id;
    }

    public static AnaliseGeo findByProcessoAtivo(Processo processo) {
        return AnaliseGeo.find("analise.processo.id = :idProcesso AND ativo = true")
                .setParameter("idProcesso", processo.id)
                .first();
    }

    public Date getDataParecer(){

        ParecerAnalistaGeo parecerAnalistaGeo = this.pareceresAnalistaGeo.stream().max(Comparator.comparing(ParecerAnalistaGeo::getDataParecer)).orElseThrow(ValidationException::new);

        return parecerAnalistaGeo.dataParecer;

    }

    public AnaliseGeo save() {

        if (this.dataCadastro == null) {

            this.dataCadastro = new Date();
        }

        Calendar c = Calendar.getInstance();
        c.setTime(this.dataCadastro);
        c.add(Calendar.DAY_OF_MONTH, Configuracoes.PRAZO_ANALISE_GEO);
        this.dataVencimentoPrazo = c.getTime();

        this.ativo = true;

        return super.save();
    }

    public AnaliseGeo save(AnaliseGeo analiseGeoAnterior) {

        this.dataCadastro = analiseGeoAnterior.dataCadastro;
        this.dataVencimentoPrazo = analiseGeoAnterior.dataVencimentoPrazo;

        this.ativo = true;

        return super.save();
    }

    public void iniciar(UsuarioAnalise usuarioExecutor) {

        verificarDataInicio();

        this.analise.processo.tramitacao.tramitar(this.analise.processo, AcaoTramitacao.INICIAR_ANALISE_GEO, usuarioExecutor);
        HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(this.analise.processo.objetoTramitavel.id), usuarioExecutor);
    }

    public void aguardarResposta(UsuarioAnalise usuarioExecutor){

        this.analise.processo.tramitacao.tramitar(this.analise.processo, AcaoTramitacao.AGUARDAR_RESPOSTA_COMUNICADO, usuarioExecutor);
        HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(this.analise.processo.objetoTramitavel.id), usuarioExecutor);
    }

    public void iniciarAnaliseCoordenador(UsuarioAnalise usuarioExecutor) {

        verificarDataInicio();

        this.analise.processo.tramitacao.tramitar(this.analise.processo, AcaoTramitacao.INICIAR_ANALISE_COORDENADOR, usuarioExecutor);
        HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(this.analise.processo.objetoTramitavel.id), usuarioExecutor);
    }

    public void verificarDataInicio() {
        if (this.dataInicio == null) {

            Calendar c = Calendar.getInstance();
            c.setTime(new Date());

            this.dataInicio = c.getTime();

            this._save();

            iniciarLicencas();
        }
    }

    public Boolean validarEmissaoLicencas(List<LicencaAnalise> licencas) {
        for (LicencaAnalise licencaVerificar : licencas) {
            if (licencaVerificar.emitir) {
                return true;
            }
        }
        throw new ValidacaoException(Mensagem.ERRO_NENHUMA_LICENCA_EMITIDA);
    }

    public static AnaliseGeo findByProcesso(Processo processo) {

        return AnaliseGeo.find("analise.processo.id = :idProcesso")
                .setParameter("idProcesso", processo.id)
                .first();

    }

    public static List<AnaliseGeo> findAnalisesByNumeroProcesso(String numeroProcesso) {

        List<AnaliseGeo> analisesGeo = AnaliseGeo.find("analise.processo.numero = :numeroProcesso")
                .setParameter("numeroProcesso", numeroProcesso)
                .fetch();

        return analisesGeo.stream().sorted(Comparator.comparing(AnaliseGeo::getDataParecer).reversed()).collect(Collectors.toList());

    }

    private void iniciarLicencas() {

        List<LicencaAnalise> novasLicencasAnalise = new ArrayList<>();
        LicencaAnalise novaLicencaAnalise = new LicencaAnalise();
        novaLicencaAnalise.caracterizacao = this.analise.processo.caracterizacao;
        novaLicencaAnalise.validade = this.analise.processo.caracterizacao.tipoLicenca.validadeEmAnos;
        novasLicencasAnalise.add(novaLicencaAnalise);

        updateLicencasAnalise(novasLicencasAnalise);
    }

    public void update(AnaliseGeo novaAnalise) {

        if (this.dataFim != null) {
            throw new ValidacaoException(Mensagem.ANALISE_GEO_CONCLUIDA);
        }

        updateDocumentos(novaAnalise.documentos);

        this._save();

        updatePareceresGeoRestricoes(novaAnalise.pareceresGeoRestricoes);
    }

    public void updatePareceresGeoRestricoes(List<ParecerGeoRestricao> pareceresSalvar) {

        if (this.pareceresGeoRestricoes == null)
            this.pareceresGeoRestricoes = new ArrayList<>();

        Iterator<ParecerGeoRestricao> pareceresGeoRestricoesIterator = this.pareceresGeoRestricoes.iterator();

        while (pareceresGeoRestricoesIterator.hasNext()) {

            ParecerGeoRestricao parecerGeoRestricao = pareceresGeoRestricoesIterator.next();

            if (ListUtil.getById(parecerGeoRestricao.id, pareceresSalvar) == null) {

                parecerGeoRestricao.delete();
                pareceresGeoRestricoesIterator.remove();
            }
        }

        for (ParecerGeoRestricao novoParecerGeoRestricao : pareceresSalvar) {

            ParecerGeoRestricao parecerGeoRestricao = ListUtil.getById(novoParecerGeoRestricao.id, this.pareceresGeoRestricoes);

            if (parecerGeoRestricao == null) { // novo parecer geo restrição

                novoParecerGeoRestricao.analiseGeo = this;
                novoParecerGeoRestricao.save();

                this.pareceresGeoRestricoes.add(novoParecerGeoRestricao);

            } else { // parecer geo já existente

                parecerGeoRestricao.update(novoParecerGeoRestricao);
            }
        }

    }

    public void updateLicencasAnalise(List<LicencaAnalise> novasLicencasAnalise) {

        if (this.licencasAnalise == null) {

            this.licencasAnalise = new ArrayList<>();
        }

        for (LicencaAnalise novaLicencaAnalise : novasLicencasAnalise) {

            LicencaAnalise licencaAnalise = ListUtil.getById(novaLicencaAnalise.id, this.licencasAnalise);

            if (licencaAnalise != null) {

                licencaAnalise.update(novaLicencaAnalise);

            } else {

                novaLicencaAnalise.analiseGeo = this;
                novaLicencaAnalise.save();

                this.licencasAnalise.add(novaLicencaAnalise);
            }
        }
    }

    private void updateDocumentos(List<Documento> novosDocumentos) {

        TipoDocumento tipoParecer = TipoDocumento.findById(TipoDocumento.PARECER_ANALISE_GEO);
        TipoDocumento tipoNotificacao = TipoDocumento.findById(TipoDocumento.DOCUMENTO_NOTIFICACAO_ANALISE_GEO);
        TipoDocumento tipoDocumentoAnaliseTemporal = TipoDocumento.findById(TipoDocumento.DOCUMENTO_ANALISE_TEMPORAL);

        if (this.documentos == null) {
            this.documentos = new ArrayList<>();
        }

        Iterator<Documento> docsCadastrados = documentos.iterator();
        List<Documento> documentosDeletar = new ArrayList<>();

        while (docsCadastrados.hasNext()) {

            Documento docCadastrado = docsCadastrados.next();

            if (ListUtil.getById(docCadastrado.id, novosDocumentos) == null) {

                docsCadastrados.remove();
                // remove o documeto do banco apenas se ele não estiver relacionado
                // com outra análises
                List<AnaliseGeo> analisesGeoRelacionadas = docCadastrado.getAnaliseGeoRelacionadas();
                if (analisesGeoRelacionadas.isEmpty()) {

                    documentosDeletar.add(docCadastrado);
                }
            }
        }

        List<Documento> documentosSalvos = new ArrayList<>();

        if(novosDocumentos != null && !novosDocumentos.isEmpty()) {
            for (Documento novoDocumento : novosDocumentos) {

                if (novoDocumento.id == null) {

                    if (novoDocumento.tipo.id.equals(tipoParecer.id)) {

                        novoDocumento.tipo = tipoParecer;

                    } else if (novoDocumento.tipo.id.equals(tipoNotificacao.id)) {

                        novoDocumento.tipo = tipoNotificacao;

                    } else if (novoDocumento.tipo.id.equals(tipoDocumentoAnaliseTemporal.id)) {

                        novoDocumento.tipo = tipoDocumentoAnaliseTemporal;

                    }

                    long quantidadeDocumentosComMesmoNome = documentosSalvos.stream().filter(documento -> documento.nomeDoArquivo.contains(novoDocumento.nomeDoArquivo.split("\\.")[0])).count();

                    if (quantidadeDocumentosComMesmoNome > 0) {
                        novoDocumento.nomeDoArquivo = novoDocumento.nomeDoArquivo.split("\\.")[0] + " (" + quantidadeDocumentosComMesmoNome + ")." + novoDocumento.nomeDoArquivo.split("\\.")[1];
                    }

                    this.documentos.add(novoDocumento);
                    documentosSalvos.add(novoDocumento.save());

                }
            }
        }

        ModelUtil.deleteAll(documentosDeletar);

    }

    public static AnaliseGeo findByNumeroProcesso(String numeroProcesso) {

        return AnaliseGeo.find("analise.processo.numero = :numeroProcesso AND ativo = true AND " +
                "tipoResultadoAnalise.id in (:idsTipoResultadoAnalise)")
                .setParameter("numeroProcesso", numeroProcesso)
                .setParameter("idsTipoResultadoAnalise", Arrays.asList(TipoResultadoAnalise.DEFERIDO, TipoResultadoAnalise.INDEFERIDO, TipoResultadoAnalise.EMITIR_NOTIFICACAO))
                .first();
    }

    public void enviarEmailNotificacao(Notificacao notificacao, ParecerAnalistaGeo parecerAnalistaGeo, List<Documento> documentos) throws Exception {

        Empreendimento empreendimento = Empreendimento.findById(this.analise.processo.empreendimento.id);

        Contato emailCadastrante =  CadastroUnificadoWS.ws.getPessoa(empreendimento.cpfCnpjCadastrante).contatos.stream()
                .filter(contato -> contato.principal && contato.tipo.descricao.equals("Email")).findFirst().orElseThrow(null);

        List<String> destinatarios = new ArrayList<>(Collections.singleton(emailCadastrante.valor));

        this.linkNotificacao = Configuracoes.URL_LICENCIAMENTO;

        if(notificacao.segundoEmailEnviado){

            notificacao._save();

            this.prazoNotificacao = notificacao.prazoNotificacao/3;

            EmailNotificacaoAnaliseGeo emailNotificacaoAnaliseGeo = new EmailNotificacaoAnaliseGeo(this, parecerAnalistaGeo, destinatarios);
            emailNotificacaoAnaliseGeo.enviar();

        } else {

            Notificacao notificacaoSave = new Notificacao(this, notificacao, documentos, parecerAnalistaGeo);
            notificacaoSave.save();
            this.prazoNotificacao = notificacaoSave.prazoNotificacao;

            EmailNotificacaoAnaliseGeo emailNotificacaoAnaliseGeo = new EmailNotificacaoAnaliseGeo(this, parecerAnalistaGeo, destinatarios);
            emailNotificacaoAnaliseGeo.enviar();

            notificacaoSave.documentosNotificacaoTecnica.addAll(emailNotificacaoAnaliseGeo.getPdfsNotificacao());
            notificacaoSave.save();

        }

    }

    public void enviarEmailComunicado(Caracterizacao caracterizacao, ParecerAnalistaGeo parecerAnalistaGeo, SobreposicaoCaracterizacaoEmpreendimento sobreposicaoCaracterizacaoEmpreendimento, Orgao orgaoResponsavel) throws Exception {

        List<String> destinatarios = new ArrayList<>();
        destinatarios.add(orgaoResponsavel.email);

        boolean aguardandoResposta = false;

        if (ParecerAnalistaGeo.verificaTipoSobreposicaoComunicado(sobreposicaoCaracterizacaoEmpreendimento)) {

            aguardandoResposta = true;
        }

        Comunicado comunicado = new Comunicado(this, caracterizacao, aguardandoResposta, sobreposicaoCaracterizacaoEmpreendimento, orgaoResponsavel);
        comunicado.save();
        comunicado.linkComunicado = Configuracoes.APP_URL + "app/index.html#!/parecer-orgao/" + comunicado.id;

        EmailComunicarOrgaoResponsavelAnaliseGeo emailComunicarOrgaoResponsavelAnaliseGeo = new EmailComunicarOrgaoResponsavelAnaliseGeo(this, parecerAnalistaGeo, comunicado, destinatarios);
        emailComunicarOrgaoResponsavelAnaliseGeo.enviar();

    }

    public void enviarEmailComunicado(Caracterizacao caracterizacao, ParecerAnalistaGeo parecerAnalistaGeo, SobreposicaoCaracterizacaoComplexo sobreposicaoCaracterizacaoComplexo, Orgao orgaoResponsavel) throws Exception {

        List<String> destinatarios = new ArrayList<String>();
        destinatarios.add(orgaoResponsavel.email);

        boolean aguardandoResposta = false;

        if (ParecerAnalistaGeo.verificaTipoSobreposicaoComunicado(sobreposicaoCaracterizacaoComplexo)) {

            aguardandoResposta = true;
        }

        Comunicado comunicado = new Comunicado(this, caracterizacao, aguardandoResposta, sobreposicaoCaracterizacaoComplexo, orgaoResponsavel);
        comunicado.save();
        comunicado.linkComunicado = Configuracoes.APP_URL + "app/index.html#!/parecer-orgao/" + comunicado.id;

        EmailComunicarOrgaoResponsavelAnaliseGeo emailComunicarOrgaoResponsavelAnaliseGeo = new EmailComunicarOrgaoResponsavelAnaliseGeo(this, parecerAnalistaGeo, comunicado, destinatarios);
        emailComunicarOrgaoResponsavelAnaliseGeo.enviar();

    }

    public void enviarEmailComunicado(Caracterizacao caracterizacao, ParecerAnalistaGeo parecerAnalistaGeo, SobreposicaoCaracterizacaoAtividade sobreposicaoCaracterizacaoAtividade, Orgao orgaoResponsavel) throws Exception {

        List<String> destinatarios = new ArrayList<String>();
        destinatarios.add(orgaoResponsavel.email);

        boolean aguardandoResposta = false;

        if (ParecerAnalistaGeo.verificaTipoSobreposicaoComunicado(sobreposicaoCaracterizacaoAtividade)) {

            aguardandoResposta = true;
        }

        Comunicado comunicado = new Comunicado(this, caracterizacao, aguardandoResposta, sobreposicaoCaracterizacaoAtividade, orgaoResponsavel);
        comunicado.save();
        comunicado.linkComunicado = Configuracoes.APP_URL + "app/index.html#!/parecer-orgao/" + comunicado.id;

        EmailComunicarOrgaoResponsavelAnaliseGeo emailComunicarOrgaoResponsavelAnaliseGeo = new EmailComunicarOrgaoResponsavelAnaliseGeo(this, parecerAnalistaGeo, comunicado, destinatarios);
        emailComunicarOrgaoResponsavelAnaliseGeo.enviar();

    }

    public void reenviarEmailComunicado(ParecerAnalistaGeo parecerAnalistaGeo, Comunicado comunicado, List<String> destinatarios) throws Exception {

        comunicado.linkComunicado = Configuracoes.APP_URL + "app/index.html#!/parecer-orgao/" + comunicado.id;

        EmailComunicarOrgaoResponsavelAnaliseGeo emailComunicarOrgaoResponsavelAnaliseGeo = new EmailComunicarOrgaoResponsavelAnaliseGeo( parecerAnalistaGeo, comunicado, destinatarios);
        emailComunicarOrgaoResponsavelAnaliseGeo.enviar();

    }

    public void enviarNotificacaoInteressado(ParecerAnalistaGeo parecerAnalistaGeo, List<String> destinatarios, Caracterizacao caracterizacao, Comunicado comunicado) throws Exception {

        EmailNotificacaoComunicado emailNotificacaoComunicado = new EmailNotificacaoComunicado(this, parecerAnalistaGeo, destinatarios, caracterizacao, comunicado);
        emailNotificacaoComunicado.enviar();

    }

    public void validaParecer(AnaliseGeo analiseGeo, UsuarioAnalise usuarioExecutor) {

        TipoResultadoAnaliseChain<AnaliseGeo> tiposResultadosAnalise = new ParecerValidadoGeo();

        tiposResultadosAnalise.setNext(new SolicitarAjustesGeo());
        tiposResultadosAnalise.setNext(new ParecerNaoValidadoGeo());

        tiposResultadosAnalise.validarParecer(this, analiseGeo, usuarioExecutor);
    }

    public void validaParecerCoordenador(AnaliseGeo analiseGeo, UsuarioAnalise usuarioExecutor) {

        TipoResultadoAnaliseChain<AnaliseGeo> tiposResultadosAnalise = new ParecerValidadoGeoCoordenador();
        tiposResultadosAnalise.setNext(new SolicitarAjustesGeoCoordenador());
        tiposResultadosAnalise.setNext(new ParecerNaoValidadoGeoCoordenador());

        tiposResultadosAnalise.validarParecer(this, analiseGeo, usuarioExecutor);
    }

    private void validarAnaliseDocumentos() {

        if (this.analisesDocumentos == null || this.analisesDocumentos.isEmpty())
            throw new ValidacaoException(Mensagem.ANALISE_DOCUMENTO_NAO_AVALIADO);

        for (AnaliseDocumento analise : this.analisesDocumentos) {

            if (analise.documento.tipo.tipoAnalise.equals(TipoAnalise.GEO) &&
                    (analise.validado == null || (!analise.validado && StringUtils.isBlank(analise.parecer)))) {

                throw new ValidacaoException(Mensagem.ANALISE_DOCUMENTO_NAO_AVALIADO);
            }
        }
    }

    @Override
    public TipoResultadoAnalise getTipoResultadoValidacao() {

        return this.tipoResultadoValidacao;
    }

    @Override
    public TipoAnalise getTipoAnalise() {
        return TipoAnalise.GEO;
    }

    @Override
    public List<Notificacao> getNotificacoes() {
        return this.notificacoes;
    }

    public void validarTipoResultadoValidacao() {

        if (tipoResultadoValidacao == null) {

            throw new ValidacaoException(Mensagem.ANALISE_SEM_RESULTADO_VALIDACAO);
        }
    }

    public void validarParecerValidacao() {

        if (StringUtils.isEmpty(parecerValidacao)) {

            throw new ValidacaoException(Mensagem.ANALISE_SEM_PARECER_VALIDACAO);
        }
    }

    public void validarTipoResultadoValidacaoCoordenador() {

        ParecerCoordenadorAnaliseGeo parecerCoordenadorAnaliseGeo = ParecerCoordenadorAnaliseGeo.find("analiseGeo", this).first();

        if (parecerCoordenadorAnaliseGeo == null) {

            throw new ValidacaoException(Mensagem.ANALISE_SEM_RESULTADO_VALIDACAO);
        }

    }

    public void validarParecerValidacaoCoordenador() {

        ParecerCoordenadorAnaliseGeo parecerCoordenadorAnaliseGeo = ParecerCoordenadorAnaliseGeo.find("analiseGeo", this).first();

        if (StringUtils.isEmpty(parecerCoordenadorAnaliseGeo.parecer)) {

            throw new ValidacaoException(Mensagem.ANALISE_SEM_PARECER_VALIDACAO);
        }
    }

    public void validarTipoResultadoValidacaoAprovador() {

        if (tipoResultadoValidacaoAprovador == null) {

            throw new ValidacaoException(Mensagem.ANALISE_SEM_RESULTADO_VALIDACAO);
        }
    }

    public void validarParecerValidacaoAprovador() {

        if (StringUtils.isEmpty(parecerValidacaoAprovador)) {

            throw new ValidacaoException(Mensagem.ANALISE_SEM_PARECER_VALIDACAO);
        }
    }

    public void validarParecerValidacaoAprovador(AnaliseGeo analiseGeo, UsuarioAnalise usuarioExecutor) {

        TipoResultadoAnaliseChain<AnaliseGeo> tiposResultadosAnalise = new SolicitarAjustesGeoAprovador();
        tiposResultadosAnalise.validarParecer(this, analiseGeo, usuarioExecutor);
    }


    public AnaliseGeo gerarCopia(boolean notificacao) {

        AnaliseGeo copia = new AnaliseGeo();

        copia.analise = this.analise;
        copia.dataCadastro = this.dataCadastro;
        copia.dataVencimentoPrazo = this.dataVencimentoPrazo;
        copia.revisaoSolicitada = !notificacao;
        copia.notificacaoAtendida = notificacao;
        copia.ativo = true;
        copia.analiseGeoRevisada = this;
        copia.dataInicio = this.dataInicio;
        copia.documentos = new ArrayList<>(this.documentos);
        copia.analisesDocumentos = new ArrayList<>();
        copia.usuarioValidacao = this.usuarioValidacao;

        for (AnaliseDocumento analiseDocumento : this.analisesDocumentos) {

            AnaliseDocumento copiaAnaliseDoc = analiseDocumento.gerarCopia();
            copiaAnaliseDoc.analiseGeo = copia;
            copia.analisesDocumentos.add(copiaAnaliseDoc);
        }

        copia.analistasGeo = new ArrayList<>();

        for (AnalistaGeo analistaGeo : this.analistasGeo) {

            AnalistaGeo copiaAnalistaGeo = analistaGeo.gerarCopia();

            copiaAnalistaGeo.analiseGeo = copia;
            copia.analistasGeo.add(copiaAnalistaGeo);
        }

        copia.coordenadores = new ArrayList<>();

        for (Coordenador coordenador : this.coordenadores) {

            Coordenador copiaCoordenadorGeo = coordenador.gerarCopia();

            copiaCoordenadorGeo.analiseGeo = copia;
            copia.coordenadores.add(copiaCoordenadorGeo);
        }

        copia.licencasAnalise = new ArrayList<>();
        for (LicencaAnalise licenca : this.licencasAnalise) {

            LicencaAnalise copiaLicencaAnalise = licenca.gerarCopia();

            copiaLicencaAnalise.analiseGeo = copia;
            copia.licencasAnalise.add(copiaLicencaAnalise);
        }

        copia.pareceresGeoRestricoes = new ArrayList<>();
        for (ParecerGeoRestricao parecerGeoRestricao : this.pareceresGeoRestricoes) {

            ParecerGeoRestricao copiaParecerGeoRestricao = parecerGeoRestricao.gerarCopia();

            copiaParecerGeoRestricao.analiseGeo = copia;
            copia.pareceresGeoRestricoes.add(copiaParecerGeoRestricao);
        }

        return copia;
    }

    public void setValidacaoCoordenador(AnaliseGeo analise) {

        this.tipoResultadoValidacao = analise.tipoResultadoValidacao;
        this.parecerValidacao = analise.parecerValidacao;
    }

    public AnalistaGeo getAnalistaGeo() {

        return this.analistasGeo.get(0);
    }

    public boolean hasCoordenadores() {

        return this.coordenadores != null && this.coordenadores.size() > 0;
    }

    public Coordenador getCoordenador() {

        return this.coordenadores.get(0);
    }

    public Documento gerarPDFParecer(ParecerAnalistaGeo parecerAnalistaGeo) throws IOException, DocumentException {

        TipoDocumento tipoDocumento = TipoDocumento.findById(TipoDocumento.PARECER_ANALISE_GEO);
        List<CamadaGeoAtividadeVO> empreendimento = Empreendimento.buscaDadosGeoEmpreendimento(this.analise.processo.empreendimento.getCpfCnpj());
        DadosProcessoVO dadosProcesso = this.analise.processo.getDadosProcesso();

        UsuarioAnalise usuarioExecutor = getUsuarioSessao();

        this.analise.processo.empreendimento.empreendimentoEU = new IntegracaoEntradaUnicaService().findEmpreendimentosByCpfCnpj(this.analise.processo.empreendimento.cpfCnpj);

        PDFGenerator pdf = new PDFGenerator()
                .setTemplate(tipoDocumento.getPdfTemplate())
                .addParam("analiseEspecifica", this)
                .addParam("parecer", parecerAnalistaGeo)
                .addParam("analiseArea", "ANALISE_GEO")
                .addParam("empreendimento", empreendimento)
                .addParam("atividades", dadosProcesso.atividades)
                .addParam("areasDeRestricoes", AreasDeRestricaoParaPDF(dadosProcesso.restricoes))
                .addParam("unidadesConservacao", UnidadesConservacaoParaPDF(dadosProcesso.restricoes))
                .addParam("complexo", dadosProcesso.complexo)
                .addParam("dataDoParecer", Helper.getDataPorExtenso(new Date()))
                .addParam("nomeAnalista", usuarioExecutor.pessoa.nome)
                .setPageSize(21.0D, 30.0D, 1.0D, 1.0D, 2.0D, 4.0D);

        pdf.generate();

        List<File> documentos = new ArrayList<>();
        documentos.add(pdf.getFile());

        parecerAnalistaGeo.documentos.stream()
                .filter(documento -> documento.tipo.id.equals(TipoDocumento.DOCUMENTO_ANALISE_TEMPORAL))
                .findAny().ifPresent(documentoAnaliseTemporal -> {
                    if(documentoAnaliseTemporal.caminho != null) {
                        documentos.add(documentoAnaliseTemporal.getFile());
                    } else {
                        File arquivoTmp = new File(documentoAnaliseTemporal.key);
                        documentos.add(arquivoTmp);
                    }
                });

        return new Documento(tipoDocumento, PDFGenerator.mergePDF(documentos), "parecer_analista_geo.pdf", parecerAnalistaGeo.usuario.pessoa.nome, new Date());

    }

    public Documento  gerarPDFCartaImagem(ParecerAnalistaGeo parecerAnalistaGeo) {

        TipoDocumento tipoDocumento = TipoDocumento.findById(TipoDocumento.CARTA_IMAGEM);
        Processo processo = Processo.findById(this.analise.processo.id);

        DadosProcessoVO dadosProcesso = processo.getDadosProcesso();

        Caracterizacao.OrigemSobreposicao geometriaFoco = ondeFocar(dadosProcesso.caracterizacao);

        List<CamadaGeoAtividadeVO> camadasGeoEmpreendimento = Empreendimento.buscaDadosGeoEmpreendimento(this.analise.processo.empreendimento.getCpfCnpj());

        CamadaGeoAtividadeVO camadaPropriedade = camadasGeoEmpreendimento.stream().filter(camada -> {
            return camada.geometrias.stream().anyMatch(c -> c.tipo.equals(CamadaGeoEnum.PROPRIEDADE.tipo));
        }).findAny().orElse(null);

        Map<LayerType, CamadaGeoAtividadeVO> geometriasAtividades = new HashMap<>();
        Map<LayerType, List<CamadaGeoRestricaoVO>> geometriasRestricoes = new HashMap<>();
        Map<LayerType, List<CamadaGeoAtividadeVO>> geometriasEmpreendimento = new HashMap<>();
        Map<LayerType, CamadaGeoComplexoVO> geometriasComplexo = new HashMap<>();

        for (CamadaGeoAtividadeVO camadaAtividade : dadosProcesso.atividades) {

            geometriasAtividades.put(new Tema(camadaAtividade.atividadeCaracterizacao.atividade.nome, MapaImagem.getColorTemaCiclo()), camadaAtividade);

        }

        if (!dadosProcesso.restricoes.isEmpty()) {

            geometriasRestricoes.put(new Tema("Áreas de restrições", MapaImagem.getColorTemaCiclo()), dadosProcesso.restricoes);

        }

        if (!camadasGeoEmpreendimento.isEmpty() && geometriaFoco.equals(Caracterizacao.OrigemSobreposicao.EMPREENDIMENTO)) {

            List<CamadaGeoAtividadeVO> camadasEmpreendimento = camadasGeoEmpreendimento.stream().filter(camada -> camada.geometrias.stream().allMatch(g -> g.geometria != null)).collect(Collectors.toList());
            geometriasEmpreendimento.put(new Tema("Dados do empreendimento", MapaImagem.getColorTemaCiclo()), camadasEmpreendimento);

        }

        if(dadosProcesso.complexo != null && !dadosProcesso.complexo.geometrias.isEmpty()){

            geometriasComplexo.put(new Tema("Complexo das atividades:", MapaImagem.getColorTemaCiclo()), dadosProcesso.complexo);
        }

        MapaImagem.GrupoDataLayerImagem grupoImagemCaracterizacao = new MapaImagem().createMapCaracterizacaoImovel(camadaPropriedade, geometriasAtividades, geometriasRestricoes, geometriasEmpreendimento, geometriasComplexo, geometriaFoco);

        Integer coodinatesAtividadeSize = grupoImagemCaracterizacao.coordinatesAtividade.length;

        this.analise.processo.empreendimento.empreendimentoEU = new IntegracaoEntradaUnicaService().findEmpreendimentosByCpfCnpj(this.analise.processo.empreendimento.cpfCnpj);

        PDFGenerator pdf = new PDFGenerator()
                .setTemplate(tipoDocumento.getPdfTemplate())
                .addParam("analiseEspecifica", this)
                .addParam("camadasGeoEmpreedimento", camadasGeoEmpreendimento)
                .addParam("dataCartaImagem", Helper.formatarData(new Date(), "dd/MM/YYYY"))
                .addParam("imagemCaracterizacao", grupoImagemCaracterizacao.imagem)
                .addParam("grupoDataLayers", grupoImagemCaracterizacao.grupoDataLayers)
                .addParam("coordinates", grupoImagemCaracterizacao.coordinates)
                .addParam("coordinatesAtividades", grupoImagemCaracterizacao.coordinatesAtividade)
                .addParam("coordinatesAtividadeSize", coodinatesAtividadeSize)
                .setPageSize(30.0D, 21.0D, 0.2D, 0D, 0D, 0.2D);

        pdf.generate();

        return new Documento(tipoDocumento, pdf.getFile(), "carta_imagem.pdf", parecerAnalistaGeo.usuario.pessoa.nome, new Date());

    }

    public Caracterizacao.OrigemSobreposicao ondeFocar(Caracterizacao caracterizacao) {

        if(caracterizacao.origemSobreposicao.equals(Caracterizacao.OrigemSobreposicao.SEM_SOBREPOSICAO)) {

            if(caracterizacao.atividadesCaracterizacao.get(0).atividade.dentroEmpreendimento) {

                return Caracterizacao.OrigemSobreposicao.EMPREENDIMENTO;

            } else {

                if(caracterizacao.geometriasComplexo != null && !caracterizacao.geometriasComplexo.isEmpty()) {

                    return Caracterizacao.OrigemSobreposicao.COMPLEXO;

                } else {

                    return Caracterizacao.OrigemSobreposicao.ATIVIDADE;

                }

            }

        }

        return caracterizacao.origemSobreposicao;

    }

    public List<Documento> gerarPDFNotificacao(AnaliseGeo analiseGeo) {

        TipoDocumento tipoDocumento = TipoDocumento.findById(TipoDocumento.NOTIFICACAO_ANALISE_GEO);

        IntegracaoEntradaUnicaService integracaoEntradaUnica = new IntegracaoEntradaUnicaService();

        br.ufla.lemaf.beans.Empreendimento empreendimentoEU = integracaoEntradaUnica.findEmpreendimentosByCpfCnpj(analiseGeo.analise.processo.empreendimento.cpfCnpj);
        analiseGeo.analise.processo.empreendimento.empreendimentoEU = empreendimentoEU;

        final Endereco enderecoCompleto = empreendimentoEU.enderecos.stream().filter(endereco -> endereco.tipo.id.equals(TipoEndereco.ID_PRINCIPAL)).findAny().orElseThrow(PortalSegurancaException::new);

        Pessoa cadastrante = integracaoEntradaUnica.findEmpreendimentosByCpfCnpj(analiseGeo.analise.processo.empreendimento.cpfCnpjCadastrante).pessoa;
        final  Contato contatoCadastrante = cadastrante.contatos.stream().filter(contato -> contato.principal).findAny().orElseThrow(PortalSegurancaException::new);

        UsuarioAnalise analista;
        AnalistaVO analistaVO;

        if(analiseGeo.id != null) {

            AnalistaGeo analistaGeo = AnalistaGeo.findByAnaliseGeo(analiseGeo.id);

            analista = UsuarioAnalise.findById(analistaGeo.usuario.id);

            analistaVO = new AnalistaVO(analista.pessoa.nome, NOME_PERFIL, analiseGeo.analise.processo.caracterizacao.atividadesCaracterizacao.get(0).atividade.siglaSetor);

        } else {

            analista = getUsuarioSessao();
            analistaVO = new AnalistaVO(analista.pessoa.nome, analista.usuarioEntradaUnica.perfilSelecionado.nome, analista.usuarioEntradaUnica.setorSelecionado.sigla);

        }

        List<Documento> documentosNotificacao = new ArrayList<>();

        AnalistaVO finalAnalistaVO = analistaVO;
        analiseGeo.inconsistencias.forEach(inconsistencia -> {

            List<String> localizacoes = new ArrayList<>();
            String itemRestricao = null;

            Inconsistencia.Categoria categoriaInconsistencia;

            if(inconsistencia.categoria.equals(Inconsistencia.Categoria.RESTRICAO)) {
                categoriaInconsistencia = Inconsistencia.Categoria.preencheCategoria(analiseGeo.analise.processo.caracterizacao);

                if(categoriaInconsistencia.equals(Inconsistencia.Categoria.ATIVIDADE)){

                    List<SobreposicaoCaracterizacaoAtividade> sobreposicaoCaracterizacaoAtividadeList = new ArrayList<>();

                    analiseGeo.analise.processo.caracterizacao.atividadesCaracterizacao.forEach(atividadeCaracterizacao ->
                            sobreposicaoCaracterizacaoAtividadeList.addAll(atividadeCaracterizacao.sobreposicoesCaracterizacaoAtividade)
                    );

                    itemRestricao = sobreposicaoCaracterizacaoAtividadeList.stream()
                            .filter( sobreposicaoCaracterizacaoAtividade -> sobreposicaoCaracterizacaoAtividade.id.equals(inconsistencia.sobreposicaoCaracterizacaoAtividade.id))
                            .findFirst().orElseThrow(ValidationException::new).tipoSobreposicao.nome;

                } else if(categoriaInconsistencia.equals(Inconsistencia.Categoria.COMPLEXO)) {

                    itemRestricao = inconsistencia.sobreposicaoCaracterizacaoComplexo.tipoSobreposicao.nome;

                } else if(categoriaInconsistencia.equals(Inconsistencia.Categoria.PROPRIEDADE)) {

                    itemRestricao = inconsistencia.sobreposicaoCaracterizacaoEmpreendimento.tipoSobreposicao.nome;

                }

            } else {

                categoriaInconsistencia = inconsistencia.categoria;

            }

            if(categoriaInconsistencia.equals(Inconsistencia.Categoria.PROPRIEDADE)){

                Coordinate coordenadasEmpreendimento = GeometryDeserializer.parseGeometry(empreendimentoEU.localizacao.geometria).getCentroid().getCoordinate();
                localizacoes.add("[" + CoordenadaUtil.formataLatitudeString(coordenadasEmpreendimento.y) + ", " + CoordenadaUtil.formataLongitudeString(coordenadasEmpreendimento.x) + "]");

            } else if(categoriaInconsistencia.equals(Inconsistencia.Categoria.COMPLEXO)){

                Coordinate coordenadasComplexo = analiseGeo.analise.processo.caracterizacao.geometriasComplexo.get(0).geometria.getCentroid().getCoordinate();
                localizacoes.add("[" + CoordenadaUtil.formataLatitudeString(coordenadasComplexo.y) + ", " + CoordenadaUtil.formataLongitudeString(coordenadasComplexo.x) + "]");

            } else if(categoriaInconsistencia.equals(Inconsistencia.Categoria.ATIVIDADE)){

                Coordinate coordenadasAtividade;

                inconsistencia.atividadeCaracterizacao = (inconsistencia.atividadeCaracterizacao != null) ?
                        inconsistencia.atividadeCaracterizacao :
                        inconsistencia.sobreposicaoCaracterizacaoAtividade.atividadeCaracterizacao;

                for (GeometriaAtividade geometriaAtividade : inconsistencia.atividadeCaracterizacao.geometriasAtividade) {

                    coordenadasAtividade = geometriaAtividade.geometria.getCentroid().getCoordinate();
                    localizacoes.add("[" + CoordenadaUtil.formataLatitudeString(coordenadasAtividade.y) + ", " + CoordenadaUtil.formataLongitudeString(coordenadasAtividade.x) + "]");

                }

            }

            PDFGenerator pdf = new PDFGenerator()
                    .setTemplate(tipoDocumento.getPdfTemplate())
                    .addParam("analiseGeo", analiseGeo)
                    .addParam("inconsistencia", inconsistencia)
                    .addParam("enderecoCompleto", enderecoCompleto)
                    .addParam("analista", finalAnalistaVO)
                    .addParam("localizacoes", localizacoes)
                    .addParam("contatoCadastrante", contatoCadastrante.valor)
                    .addParam("dataDoParecer", Helper.getDataPorExtenso(new Date()))
                    .addParam("categoriaInconsistencia", categoriaInconsistencia)
                    .addParam("itemRestricao", itemRestricao)
                    .setPageSize(21.0D, 30.0D, 1.0D, 1.0D, 4.0D, 4.0D);

            pdf.generate();

            documentosNotificacao.add(new Documento(tipoDocumento, pdf.getFile(), "notificacao_geo.pdf", analistaVO.nomeAnalista, new Date()));
        });

        return documentosNotificacao;

    }

    public Documento gerarPDFOficioOrgao(Comunicado comunicado) {

        TipoDocumento tipoDocumento = TipoDocumento.findById(TipoDocumento.DOCUMENTO_OFICIO_ORGAO);

        IntegracaoEntradaUnicaService integracaoEntradaUnica = new IntegracaoEntradaUnicaService();
        Municipio municipio = null;
        String distancia = null;

        if(comunicado.analiseGeo.analise.processo.caracterizacao.origemSobreposicao.equals(Caracterizacao.OrigemSobreposicao.EMPREENDIMENTO)) {

            distancia = comunicado.getDistancia(comunicado.sobreposicaoCaracterizacaoEmpreendimento.distancia, comunicado.sobreposicaoCaracterizacaoEmpreendimento.geometria, comunicado.sobreposicaoCaracterizacaoEmpreendimento.caracterizacao);

        } else if(comunicado.analiseGeo.analise.processo.caracterizacao.origemSobreposicao.equals(Caracterizacao.OrigemSobreposicao.ATIVIDADE)){

            distancia = comunicado.getDistancia(comunicado.sobreposicaoCaracterizacaoAtividade.distancia, comunicado.sobreposicaoCaracterizacaoAtividade.geometria, comunicado.sobreposicaoCaracterizacaoAtividade.atividadeCaracterizacao.caracterizacao);

        } else if(comunicado.analiseGeo.analise.processo.caracterizacao.origemSobreposicao.equals(Caracterizacao.OrigemSobreposicao.COMPLEXO)){

            distancia = comunicado.getDistancia(comunicado.sobreposicaoCaracterizacaoComplexo.distancia, comunicado.sobreposicaoCaracterizacaoComplexo.geometria, comunicado.sobreposicaoCaracterizacaoComplexo.caracterizacao);

        }

        br.ufla.lemaf.beans.Empreendimento empreendimentoEU = integracaoEntradaUnica.findEmpreendimentosByCpfCnpj(comunicado.analiseGeo.analise.processo.empreendimento.getCpfCnpj());
        for (Endereco endereco : empreendimentoEU.enderecos) {
            if (endereco.tipo.id.equals(TipoEndereco.ID_PRINCIPAL)) {
                municipio = endereco.municipio;
            }
        }

        PDFGenerator pdf = new PDFGenerator()
                .setTemplate(tipoDocumento.getPdfTemplate())
                .addParam("analiseGeo", comunicado.analiseGeo)
                .addParam("comunicado", comunicado)
                .addParam("distancia", distancia)
                .addParam("municipio", municipio)
                .setPageSize(21.0D, 30.0D, 1.0D, 1.0D, 4.0D, 4.0D);

        pdf.generate();

        return new Documento(tipoDocumento, pdf.getFile());
    }

    public AnaliseTecnica geraAnaliseTecnica() {

        AnaliseTecnica analiseTecnica = new AnaliseTecnica();
        analiseTecnica.analise = this.analise;
        analiseTecnica.ativo = true;
        analiseTecnica.dataCadastro = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(analiseTecnica.dataCadastro);
        calendar.add(Calendar.DAY_OF_MONTH, Configuracoes.PRAZO_ANALISE_TECNICA);
        analiseTecnica.dataVencimentoPrazo = calendar.getTime();

        return analiseTecnica;

    }

    public List<CamadaGeoRestricaoVO> AreasDeRestricaoParaPDF(List<CamadaGeoRestricaoVO> restricoes) {

        return restricoes.stream().filter(restricao ->
                restricao.orgao.sigla.equals(OrgaoEnum.IPHAN.codigo) || restricao.orgao.sigla.equals(OrgaoEnum.IBAMA.codigo))
                .collect(Collectors.toList());

    }

    public List<CamadaGeoRestricaoVO> UnidadesConservacaoParaPDF(List<CamadaGeoRestricaoVO> restricoes) {

        return restricoes.stream().filter(restricao ->
                !restricao.orgao.sigla.equals(OrgaoEnum.IPHAN.codigo) && !restricao.orgao.sigla.equals(OrgaoEnum.IBAMA.codigo))
                .collect(Collectors.toList());

    }

    public String getJustificativaUltimoParecer() {

        ParecerCoordenadorAnaliseGeo parecerCoordenadorAnaliseGeo = this.pareceresCoordenadorAnaliseGeo.stream()
                .filter(parecer -> parecer.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.SOLICITAR_AJUSTES))
                .max(Comparator.comparing(ParecerCoordenadorAnaliseGeo::getDataParecer))
                .orElseThrow(() -> new ValidacaoException(Mensagem.PARECER_NAO_ENCONTRADO));

        return parecerCoordenadorAnaliseGeo.parecer;

    }

    public static AnaliseGeo findUltimaByAnalise(Analise analise){
        return AnaliseGeo.find("analise.processo.numero = :numero ORDER BY id DESC")
                .setParameter("numero", analise.processo.numero)
                .first();
    }

    public static List<AnaliseGeo> findAllByAnalise(Analise analise){
        return AnaliseGeo.find("analise.id = :idAnalise ORDER BY id")
                .setParameter("idAnalise", analise.id)
                .fetch();
    }


    public static List<AnaliseGeo> findAllByProcesso(String numero){
        return AnaliseGeo.find("analise.processo.numero = :numero ORDER BY analise.id DESC")
                .setParameter("numero", numero)
                .fetch();
    }

}