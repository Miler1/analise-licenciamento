package models;

import com.itextpdf.text.DocumentException;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import deserializers.GeometryDeserializer;
import enums.CamadaGeoEnum;
import exceptions.ValidacaoException;
import main.java.br.ufla.lemaf.beans.pessoa.Endereco;
import main.java.br.ufla.lemaf.beans.pessoa.Municipio;
import main.java.br.ufla.lemaf.enums.TipoEndereco;
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
import play.db.jpa.GenericModel;
import services.IntegracaoEntradaUnicaService;
import utils.*;
import static models.licenciamento.Caracterizacao.OrigemSobreposicao.COMPLEXO;
import static models.licenciamento.Caracterizacao.OrigemSobreposicao.EMPREENDIMENTO;
import static models.licenciamento.Caracterizacao.OrigemSobreposicao.ATIVIDADE;
import javax.persistence.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import static security.Auth.getUsuarioSessao;

@Entity
@Table(schema = "analise", name = "analise_geo")
public class AnaliseGeo extends GenericModel implements Analisavel {

    public static final String SEQ = "analise.analise_geo_id_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    @Column(name = "id")
    public Long id;

    @ManyToOne
    @JoinColumn(name = "id_analise")
    public Analise analise;

    public String parecer;

    @Required
    @Column(name = "data_vencimento_prazo")
    public Date dataVencimentoPrazo;

    @Required
    @Column(name = "revisao_solicitada")
    public Boolean revisaoSolicitada;

    @Required
    @Column(name = "notificacao_atendida")
    public Boolean notificacaoAtendida;

    public Boolean ativo;

    @OneToOne
    @JoinColumn(name = "id_analise_geo_revisada", referencedColumnName = "id")
    public AnaliseGeo analiseGeoRevisada;

    @Column(name = "data_inicio")
    @Temporal(TemporalType.TIMESTAMP)
    public Date dataInicio;

    @Column(name = "data_fim")
    @Temporal(TemporalType.TIMESTAMP)
    public Date dataFim;

    @ManyToOne
    @JoinColumn(name = "id_tipo_resultado_analise")
    public TipoResultadoAnalise tipoResultadoAnalise;

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

    @Column(name = "parecer_validacao")
    public String parecerValidacao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario_validacao", referencedColumnName = "id")
    public UsuarioAnalise usuarioValidacao;

    @OneToMany(mappedBy = "analiseGeo", orphanRemoval = true)
    public List<LicencaAnalise> licencasAnalise;

    @OneToMany(mappedBy = "analiseGeo", orphanRemoval = true)
    public List<ParecerGeoRestricao> pareceresGeoRestricoes;

    @Column(name = "justificativa_coordenador")
    public String justificativaCoordenador;

    @ManyToOne
    @JoinColumn(name = "id_tipo_resultado_validacao_gerente")
    public TipoResultadoAnalise tipoResultadoValidacaoGerente;

    @Column(name = "parecer_validacao_gerente")
    public String parecerValidacaoGerente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario_validacao_gerente", referencedColumnName = "id")
    public UsuarioAnalise usuarioValidacaoGerente;

    @OneToMany(mappedBy = "analiseGeo", cascade = CascadeType.ALL)
    public List<Gerente> gerentes;

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

    @Column(name = "situacao_fundiaria")
    public String situacaoFundiaria;

    @Column(name = "analise_temporal")
    public String analiseTemporal;

    @Column(name = "despacho_analista")
    public String despacho;

    @OneToMany(mappedBy = "analiseGeo", cascade = CascadeType.ALL)
    public List<Inconsistencia> inconsistencias;

    @OneToMany(mappedBy = "analiseGeo", fetch = FetchType.LAZY)
    public List<Desvinculo> desvinculos;

    @OneToMany(mappedBy = "analiseGeo", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    public List<Notificacao> notificacoes;

    @Transient
    public String linkNotificacao;

    @Transient
    public Integer prazoNotificacao;

    @Transient
    public Long idAnalistaDestino;

    private void validarParecer(AnaliseGeo analise) {

        if (StringUtils.isBlank(this.parecer))
            throw new ValidacaoException(Mensagem.ANALISE_PARECER_NAO_PREENCHIDO);
    }

    private void validarTipoResultadoAnalise() {

        if (this.tipoResultadoAnalise == null) {
            throw new ValidacaoException(Mensagem.ANALISE_FINAL_PROCESSO_NAO_PREENCHIDA);
        }

        if (this.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.DEFERIDO) && this.despacho.equals("")) {
            throw new ValidacaoException(Mensagem.ANALISE_DESPACHO_NAO_PREENCHIDO);
        }

        if (this.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.INDEFERIDO) && this.despacho.equals("")) {
            throw new ValidacaoException(Mensagem.ANALISE_JUSTIFICATIVA_NAO_PREENCHIDA);
        }

        //TODO PUMA-SQ1 Adicionar validacao para o Tipo Resultado Análise "EMITIR NOTIFICACAO"

    }

    public static AnaliseGeo findByProcesso(Processo processo) {
        return AnaliseGeo.find("analise.processo.id = :idProcesso AND ativo = true")
                .setParameter("idProcesso", processo.id)
                .first();
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

    public void iniciar(UsuarioAnalise usuarioExecutor) {

        verificarDataInicio();

        this.analise.processo.tramitacao.tramitar(this.analise.processo, AcaoTramitacao.INICIAR_ANALISE_GEO, usuarioExecutor);
        HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(this.analise.processo.objetoTramitavel.id), usuarioExecutor);
    }

    public void iniciarAnaliseGerente(UsuarioAnalise usuarioExecutor) {

        verificarDataInicio();

        this.analise.processo.tramitacao.tramitar(this.analise.processo, AcaoTramitacao.INICIAR_ANALISE_GERENTE, usuarioExecutor);
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
        for (int i = 0; i < licencas.size(); i++) {
            LicencaAnalise licencaVerificar = licencas.get(i);
            if (licencaVerificar.emitir) {
                return true;
            }
        }
        throw new ValidacaoException(Mensagem.ERRO_NENHUMA_LICENCA_EMITIDA);
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

        this.parecer = novaAnalise.parecer;
        this.situacaoFundiaria = novaAnalise.situacaoFundiaria;
        this.analiseTemporal = novaAnalise.analiseTemporal;
        this.despacho = novaAnalise.despacho;

        if (novaAnalise.tipoResultadoAnalise != null &&
                novaAnalise.tipoResultadoAnalise.id != null) {

            this.tipoResultadoAnalise = novaAnalise.tipoResultadoAnalise;
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
                if (analisesGeoRelacionadas.size() == 0) {

                    documentosDeletar.add(docCadastrado);
                }
            }
        }

        for (Documento novoDocumento : novosDocumentos) {

            if (novoDocumento.id == null) {

                if (novoDocumento.tipo.id.equals(tipoParecer.id)) {

                    novoDocumento.tipo = tipoParecer;
                    this.documentos.add(novoDocumento);

                } else if (novoDocumento.tipo.id.equals(tipoNotificacao.id)) {

                    novoDocumento.tipo = tipoNotificacao;

                } else if (novoDocumento.tipo.id.equals(tipoDocumentoAnaliseTemporal.id)) {

                    novoDocumento.tipo = tipoDocumentoAnaliseTemporal;

                }

                novoDocumento.save();
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

    public void finalizar(AnaliseGeo analise, UsuarioAnalise usuarioExecutor) throws Exception {

        this.update(analise);
        validarParecer(analise);
        validarTipoResultadoAnalise();

        this._save();

        Gerente gerente = Gerente.distribuicaoAutomaticaGerente(usuarioExecutor.usuarioEntradaUnica.setorSelecionado.sigla, this);

        this.usuarioValidacaoGerente = UsuarioAnalise.findByGerente(gerente);

        if (this.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.DEFERIDO)) {

            if(this.analise.processo.caracterizacao.origemSobreposicao.equals(EMPREENDIMENTO)){

                List<SobreposicaoCaracterizacaoEmpreendimento> sobreposicoesCaracterizacaoEmpreendimento = this.analise.processo.caracterizacao.sobreposicoesCaracterizacaoEmpreendimento.stream().distinct()
                        .filter(distinctByKey(sobreposicaoCaracterizacaoEmpreendimento -> sobreposicaoCaracterizacaoEmpreendimento.tipoSobreposicao.codigo)).collect(Collectors.toList());

                for (SobreposicaoCaracterizacaoEmpreendimento sobreposicaoCaracterizacaoEmpreendimento : sobreposicoesCaracterizacaoEmpreendimento) {

                    if (sobreposicaoCaracterizacaoEmpreendimento != null){
                        enviarEmailComunicado(this.analise.processo.caracterizacao, sobreposicaoCaracterizacaoEmpreendimento);
                    }
                    
                }

            }else if (this.analise.processo.caracterizacao.origemSobreposicao.equals(ATIVIDADE)){

                List<SobreposicaoCaracterizacaoAtividade> sobreposicoesCaracterizacaoAtividade =  this.analise.processo.caracterizacao.atividadesCaracterizacao.stream().map(atividadeCaracterizacao -> atividadeCaracterizacao.sobreposicaoCaracterizacaoAtividade).collect(Collectors.toList());

                for (SobreposicaoCaracterizacaoAtividade sobreposicaoCaracterizacaoAtividade : sobreposicoesCaracterizacaoAtividade) {

                    if(sobreposicaoCaracterizacaoAtividade != null){
                        enviarEmailComunicado(this.analise.processo.caracterizacao, sobreposicaoCaracterizacaoAtividade);
                    }

                }

            } else if (this.analise.processo.caracterizacao.origemSobreposicao.equals(COMPLEXO)) {

                List<SobreposicaoCaracterizacaoComplexo> sobreposicoesCaracterizacaoComplexo = this.analise.processo.caracterizacao.sobreposicoesCaracterizacaoComplexo.stream().distinct()
                        .filter(distinctByKey(sobreposicaoCaracterizacaoComplexo -> sobreposicaoCaracterizacaoComplexo.tipoSobreposicao.codigo)).collect(Collectors.toList());

                for (SobreposicaoCaracterizacaoComplexo sobreposicaoCaracterizacaoComplexo : sobreposicoesCaracterizacaoComplexo) {

                    if(sobreposicaoCaracterizacaoComplexo != null){
                        enviarEmailComunicado(this.analise.processo.caracterizacao, sobreposicaoCaracterizacaoComplexo);
                    }

                }

            }

            if (this.usuarioValidacaoGerente != null) {

                gerente.save();

                this.analise.processo.tramitacao.tramitar(this.analise.processo, AcaoTramitacao.DEFERIR_ANALISE_GEO_VIA_GERENTE, usuarioExecutor, this.usuarioValidacaoGerente);
                HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(this.analise.processo.objetoTramitavel.id), usuarioExecutor);
            }

        } else if (this.tipoResultadoAnalise.id == TipoResultadoAnalise.INDEFERIDO) {

            if (this.usuarioValidacaoGerente != null) {

                gerente.save();

                this.analise.processo.tramitacao.tramitar(this.analise.processo, AcaoTramitacao.INDEFERIR_ANALISE_GEO_VIA_GERENTE, usuarioExecutor, this.usuarioValidacaoGerente);
                HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(this.analise.processo.objetoTramitavel.id), usuarioExecutor);
            }

        } else if (this.tipoResultadoAnalise.id == TipoResultadoAnalise.EMITIR_NOTIFICACAO) {

//            Notificacao.criarNotificacoesAnaliseGeo(analise);

            List<Notificacao> notificacoes = analise.notificacoes;
            notificacoes = notificacoes.stream().filter(notificacao -> notificacao.id == null).collect(Collectors.toList());

            if (notificacoes.size() != 1) {

                throw new ValidacaoException(Mensagem.ERRO_SALVAMENTO_NOTIFICACAO);
            }

            Notificacao novaNotificacao = notificacoes.get(0);

            enviarEmailNotificacao(novaNotificacao, analise.documentos);

            alterarStatusLicenca(StatusCaracterizacaoEnum.NOTIFICADO.codigo, analise.analise.processo.numero);

            this.analise.processo.tramitacao.tramitar(this.analise.processo, AcaoTramitacao.NOTIFICAR, usuarioExecutor, this.usuarioValidacaoGerente);
            HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(this.analise.processo.objetoTramitavel.id), usuarioExecutor);
//
//            HistoricoTramitacao historicoTramitacao = HistoricoTramitacao.getUltimaTramitacao(this.analise.processo.objetoTramitavel.id);
//
//            List<Notificacao> notificacoes = Notificacao.find("analiseGeo.id", this.id).fetch();
//            Notificacao.setHistoricoAlteracoes(notificacoes, historicoTramitacao);
//
//            HistoricoTramitacao.setSetor(historicoTramitacao, usuarioExecutor);
//

        }
    }

    public void enviarEmailNotificacao(Notificacao notificacao, List<Documento> documentos) throws Exception {

        List<String> destinatarios = new ArrayList<String>();
        Empreendimento empreendimento = Empreendimento.findById(this.analise.processo.empreendimento.id);
        destinatarios.addAll(Collections.singleton(empreendimento.cadastrante.contato.email));

        this.linkNotificacao = Configuracoes.URL_LICENCIAMENTO;
        Notificacao notificacaoSave = new Notificacao(this, notificacao, documentos);
        notificacaoSave.save();
        this.prazoNotificacao = notificacao.prazoNotificacao;

        EmailNotificacaoAnaliseGeo emailNotificacaoAnaliseGeo = new EmailNotificacaoAnaliseGeo(this, destinatarios, notificacaoSave);
        emailNotificacaoAnaliseGeo.enviar();
    }

    public void enviarEmailComunicado(Caracterizacao caracterizacao, SobreposicaoCaracterizacaoEmpreendimento sobreposicaoCaracterizacaoEmpreendimento) throws Exception {

        for (Orgao orgaoResponsavel : sobreposicaoCaracterizacaoEmpreendimento.tipoSobreposicao.orgaosResponsaveis) {

            if (!orgaoResponsavel.sigla.equals(OrgaoEnum.IPHAN.codigo) && !orgaoResponsavel.sigla.equals(OrgaoEnum.IBAMA.codigo)) {

                List<String> destinatarios = new ArrayList<String>();
                destinatarios.add(orgaoResponsavel.email);

                Comunicado comunicado = new Comunicado(this, caracterizacao, sobreposicaoCaracterizacaoEmpreendimento, orgaoResponsavel);
                comunicado.save();
                comunicado.linkComunicado = Configuracoes.APP_URL + "app/index.html#!/parecer-orgao/" + comunicado.id;

                EmailComunicarOrgaoResponsavelAnaliseGeo emailComunicarOrgaoResponsavelAnaliseGeo = new EmailComunicarOrgaoResponsavelAnaliseGeo(this, comunicado, destinatarios);
                emailComunicarOrgaoResponsavelAnaliseGeo.enviar();
            }
        }
    }

    public void enviarEmailComunicado(Caracterizacao caracterizacao, SobreposicaoCaracterizacaoComplexo sobreposicaoCaracterizacaoComplexo) throws Exception {

        for (Orgao orgaoResponsavel : sobreposicaoCaracterizacaoComplexo.tipoSobreposicao.orgaosResponsaveis) {

            if (!orgaoResponsavel.sigla.equals(OrgaoEnum.IPHAN.codigo) && !orgaoResponsavel.sigla.equals(OrgaoEnum.IBAMA.codigo)) {

                List<String> destinatarios = new ArrayList<String>();
                destinatarios.add(orgaoResponsavel.email);

                Comunicado comunicado = new Comunicado(this, caracterizacao, sobreposicaoCaracterizacaoComplexo, orgaoResponsavel);
                comunicado.save();
                comunicado.linkComunicado = Configuracoes.APP_URL + "app/index.html#!/parecer-orgao/" + comunicado.id;

                EmailComunicarOrgaoResponsavelAnaliseGeo emailComunicarOrgaoResponsavelAnaliseGeo = new EmailComunicarOrgaoResponsavelAnaliseGeo(this, comunicado, destinatarios);
                emailComunicarOrgaoResponsavelAnaliseGeo.enviar();
            }
        }
    }

    public void enviarEmailComunicado(Caracterizacao caracterizacao, SobreposicaoCaracterizacaoAtividade sobreposicaoCaracterizacaoAtividade) throws Exception {

        for (Orgao orgaoResponsavel : sobreposicaoCaracterizacaoAtividade.tipoSobreposicao.orgaosResponsaveis) {

            if (!orgaoResponsavel.sigla.equals(OrgaoEnum.IPHAN.codigo) && !orgaoResponsavel.sigla.equals(OrgaoEnum.IBAMA.codigo)) {

                List<String> destinatarios = new ArrayList<String>();
                destinatarios.add(orgaoResponsavel.email);

                Comunicado comunicado = new Comunicado(this, caracterizacao, sobreposicaoCaracterizacaoAtividade, orgaoResponsavel);
                comunicado.save();
                comunicado.linkComunicado = Configuracoes.APP_URL + "app/index.html#!/parecer-orgao/" + comunicado.id;

                EmailComunicarOrgaoResponsavelAnaliseGeo emailComunicarOrgaoResponsavelAnaliseGeo = new EmailComunicarOrgaoResponsavelAnaliseGeo(this, comunicado, destinatarios);
                emailComunicarOrgaoResponsavelAnaliseGeo.enviar();
            }
        }
    }

    public void validaParecer(AnaliseGeo analiseGeo, UsuarioAnalise usuarioExecutor) {

        TipoResultadoAnaliseChain<AnaliseGeo> tiposResultadosAnalise = new ParecerValidadoGeo();
        ;
        tiposResultadosAnalise.setNext(new SolicitarAjustesGeo());
        tiposResultadosAnalise.setNext(new ParecerNaoValidadoGeo());

        tiposResultadosAnalise.validarParecer(this, analiseGeo, usuarioExecutor);
    }

    public void validaParecerGerente(AnaliseGeo analiseGeo, UsuarioAnalise usuarioExecutor) {

        TipoResultadoAnaliseChain<AnaliseGeo> tiposResultadosAnalise = new ParecerValidadoGeoGerente();
        tiposResultadosAnalise.setNext(new SolicitarAjustesGeoGerente());
        tiposResultadosAnalise.setNext(new ParecerNaoValidadoGeoGerente());

        tiposResultadosAnalise.validarParecer(this, analiseGeo, usuarioExecutor);
    }

    private void validarLicencasAnalise() {

        if (this.tipoResultadoAnalise.id == TipoResultadoAnalise.DEFERIDO) {

            for (LicencaAnalise licencaAnalise : this.licencasAnalise) {

                if (licencaAnalise.emitir == null) {

                    throw new ValidacaoException(Mensagem.ANALISE_GEO_LICENCA_SEM_VALIDACAO);
                }
            }
        }
    }

    private void validarResultado() {

        if (this.tipoResultadoAnalise == null)
            throw new ValidacaoException(Mensagem.ANALISE_SEM_RESULTADO);

        boolean todosDocumentosValidados = true;
        for (AnaliseDocumento analise : this.analisesDocumentos) {

            if (analise.documento.tipo.tipoAnalise.equals(TipoAnalise.GEO)) {

                if (analise.validado == null || (!analise.validado && StringUtils.isBlank(analise.parecer))) {

                    throw new ValidacaoException(Mensagem.ANALISE_DOCUMENTO_NAO_AVALIADO);
                }
                todosDocumentosValidados &= analise.validado;
            }
        }

        if (this.tipoResultadoAnalise.id == TipoResultadoAnalise.DEFERIDO && !todosDocumentosValidados) {

            throw new ValidacaoException(Mensagem.TODOS_OS_DOCUMENTOS_VALIDOS);
        }
    }

    private void validarAnaliseDocumentos() {

        if (this.analisesDocumentos == null || this.analisesDocumentos.size() == 0)
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

        return this.tipoResultadoValidacao != null ? this.tipoResultadoValidacao : this.tipoResultadoValidacaoGerente;
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

    public void validarTipoResultadoValidacaoGerente() {

        if (tipoResultadoValidacaoGerente == null) {

            throw new ValidacaoException(Mensagem.ANALISE_SEM_RESULTADO_VALIDACAO);
        }
    }

    public void validarParecerValidacaoGerente() {

        if (StringUtils.isEmpty(parecerValidacaoGerente)) {

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
        copia.parecer = this.parecer;
        copia.dataCadastro = this.dataCadastro;
        copia.dataVencimentoPrazo = this.dataVencimentoPrazo;
        copia.revisaoSolicitada = !notificacao;
        copia.notificacaoAtendida = notificacao;
        copia.ativo = true;
        copia.analiseGeoRevisada = this;
        copia.dataInicio = this.dataInicio;
        copia.tipoResultadoAnalise = this.tipoResultadoAnalise;
        copia.documentos = new ArrayList<>(this.documentos);
        copia.analisesDocumentos = new ArrayList<>();
        copia.usuarioValidacao = this.usuarioValidacao;
        copia.usuarioValidacaoGerente = this.usuarioValidacaoGerente;

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

        copia.gerentes = new ArrayList<>();

        for (Gerente gerente : this.gerentes) {

            Gerente copiaGerenteGeo = gerente.gerarCopia();

            copiaGerenteGeo.analiseGeo = copia;
            copia.gerentes.add(copiaGerenteGeo);
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

    public void setValidacaoGerente(AnaliseGeo analise) {

        this.tipoResultadoValidacaoGerente = analise.tipoResultadoValidacaoGerente;
        this.parecerValidacaoGerente = analise.parecerValidacaoGerente;
    }

    public void setValidacaoCoordenador(AnaliseGeo analise) {

        this.tipoResultadoValidacao = analise.tipoResultadoValidacao;
        this.parecerValidacao = analise.parecerValidacao;
    }

    public AnalistaGeo getAnalistaGeo() {

        return this.analistasGeo.get(0);
    }

    public boolean hasGerentes() {

        return this.gerentes != null && this.gerentes.size() > 0;
    }

    public Gerente getGerente() {

        return this.gerentes.get(0);
    }

    public Documento gerarPDFParecer() throws IOException, DocumentException {

        TipoDocumento tipoDocumento = TipoDocumento.findById(TipoDocumento.PARECER_ANALISE_GEO);
        List<CamadaGeoAtividadeVO> empreendimento = Empreendimento.buscaDadosGeoEmpreendimento(this.analise.processo.empreendimento.getCpfCnpj());
        Processo processo = Processo.findById(this.analise.processo.id);
        DadosProcessoVO dadosProcesso = processo.getDadosProcesso();

        PDFGenerator pdf = new PDFGenerator()
                .setTemplate(tipoDocumento.getPdfTemplate())
                .addParam("analiseEspecifica", this)
                .addParam("analiseArea", "ANALISE_GEO")
                .addParam("empreendimento", empreendimento)
                .addParam("atividades", dadosProcesso.atividades)
                .addParam("dataDoParecer", Helper.getDataPorExtenso(new Date()))
                .setPageSize(21.0D, 30.0D, 1.0D, 1.0D, 2.0D, 4.0D);

        pdf.generate();

        List<File> documentos = new ArrayList<>();
        documentos.add(pdf.getFile());
        List<Documento> documentosAnaliseTemporal = this.documentos.stream().filter(documento -> documento.tipo.id.equals(TipoDocumento.DOCUMENTO_ANALISE_TEMPORAL)).collect(Collectors.toList());
        documentos.addAll(documentosAnaliseTemporal.stream().map(Documento::getFile).collect(Collectors.toList()));
        FileOutputStream fos = PDFGenerator.mergePDF(documentos, pdf.getFile().getName());

        Documento documento = new Documento(tipoDocumento, pdf.getFile());

        return documento;

    }

    public Documento gerarPDFCartaImagem() {

        TipoDocumento tipoDocumento = TipoDocumento.findById(TipoDocumento.CARTA_IMAGEM);
        Processo processo = Processo.findById(this.analise.processo.id);

        DadosProcessoVO dadosProcesso = processo.getDadosProcesso();
        List<CamadaGeoAtividadeVO> camadasGeoEmpreendimento = Empreendimento.buscaDadosGeoEmpreendimento(this.analise.processo.empreendimento.getCpfCnpj());

        CamadaGeoAtividadeVO camadaPropriedade = camadasGeoEmpreendimento.stream().filter(camada -> {
            return camada.geometrias.stream().anyMatch(c -> c.tipo.equals(CamadaGeoEnum.PROPRIEDADE.tipo));
        }).findAny().orElse(null);

        Map<LayerType, CamadaGeoAtividadeVO> geometriasAtividades = new HashMap<>();
        Map<LayerType, List<CamadaGeoRestricaoVO>> geometriasRestricoes = new HashMap<>();
        Map<LayerType, List<CamadaGeoAtividadeVO>> geometriasEmpreendimento = new HashMap<>();

        for (CamadaGeoAtividadeVO camadaAtividade : dadosProcesso.atividades) {

            geometriasAtividades.put(new Tema(camadaAtividade.atividadeCaracterizacao.atividade.nome, MapaImagem.getColorTemaCiclo()), camadaAtividade);

        }

        if (!dadosProcesso.restricoes.isEmpty()) {

            geometriasRestricoes.put(new Tema("Áreas restrições", MapaImagem.getColorTemaCiclo()), dadosProcesso.restricoes);

        }

        if (!camadasGeoEmpreendimento.isEmpty()) {

            List<CamadaGeoAtividadeVO> camadasEmpreendimento = camadasGeoEmpreendimento.stream().filter(camada -> camada.geometrias.stream().allMatch(g -> g.geometria != null)).collect(Collectors.toList());
            geometriasEmpreendimento.put(new Tema("Dados do empreendimento", MapaImagem.getColorTemaCiclo()), camadasEmpreendimento);

        }

        MapaImagem.GrupoDataLayerImagem grupoImagemCaracterizacao = new MapaImagem().createMapCaracterizacaoImovel(camadaPropriedade, geometriasAtividades, geometriasRestricoes, geometriasEmpreendimento);

        PDFGenerator pdf = new PDFGenerator()
                .setTemplate(tipoDocumento.getPdfTemplate())
                .addParam("analiseEspecifica", this)
                .addParam("camadasGeoEmpreedimento", camadasGeoEmpreendimento)
                .addParam("dataCartaImagem", Helper.getDataPorExtenso(new Date()))
                .addParam("imagemCaracterizacao", grupoImagemCaracterizacao.imagem)
                .addParam("grupoDataLayers", grupoImagemCaracterizacao.grupoDataLayers)
                .setPageSize(30.0D, 21.0D, 0.5D, 0.5D, 2.0D, 3.9D);

        pdf.generate();

        Documento documento = new Documento(tipoDocumento, pdf.getFile());

        return documento;

    }

    public Documento gerarPDFNotificacao(AnaliseGeo analiseGeo, Notificacao notificacao) {

        TipoDocumento tipoDocumento = TipoDocumento.findById(TipoDocumento.NOTIFICACAO_ANALISE_GEO);

        IntegracaoEntradaUnicaService integracaoEntradaUnica = new IntegracaoEntradaUnicaService();
        main.java.br.ufla.lemaf.beans.Empreendimento empreendimentoEU = integracaoEntradaUnica.findEmpreendimentosByCpfCnpj(analiseGeo.analise.processo.empreendimento.getCpfCnpj());
        Endereco enderecoCompleto = null;
        for (Endereco endereco : empreendimentoEU.enderecos) {
            if (endereco.tipo.id == TipoEndereco.ID_PRINCIPAL) {
                enderecoCompleto = endereco;
            }
        }

        Geometry geometriaEmpreendimento = GeometryDeserializer.parseGeometry(empreendimentoEU.localizacao.geometria);
        Coordinate coordenadasEmpreendimento = geometriaEmpreendimento.getCentroid().getCoordinate();
        UsuarioAnalise analista = getUsuarioSessao();
        String localizacaoEmpreendimento = "[" + coordenadasEmpreendimento.x + ", " + coordenadasEmpreendimento.y + "]";

        PDFGenerator pdf = new PDFGenerator()
                .setTemplate(tipoDocumento.getPdfTemplate())
                .addParam("analiseGeo", analiseGeo)
                .addParam("enderecoCompleto", enderecoCompleto)
                .addParam("analista", analista)
                .addParam("localizacaoEmpreendimento", localizacaoEmpreendimento)
                .addParam("dataDoParecer", Helper.getDataPorExtenso(new Date()))
                .setPageSize(21.0D, 30.0D, 1.0D, 1.0D, 4.0D, 4.0D);

        pdf.generate();

        Documento documento = new Documento(tipoDocumento, pdf.getFile());

        return documento;

    }

    public Documento gerarPDFOficioOrgao(Comunicado comunicado) {

        TipoDocumento tipoDocumento = TipoDocumento.findById(TipoDocumento.DOCUMENTO_OFICIO_ORGAO);
        Documento documento = null;

        IntegracaoEntradaUnicaService integracaoEntradaUnica = new IntegracaoEntradaUnicaService();
        Municipio municipio = null;

        main.java.br.ufla.lemaf.beans.Empreendimento empreendimentoEU = integracaoEntradaUnica.findEmpreendimentosByCpfCnpj(comunicado.analiseGeo.analise.processo.empreendimento.getCpfCnpj());
        for (Endereco endereco : empreendimentoEU.enderecos) {
            if (endereco.tipo.id == TipoEndereco.ID_PRINCIPAL) {
                municipio = endereco.municipio;
            }
        }

        PDFGenerator pdf = new PDFGenerator()
                .setTemplate(tipoDocumento.getPdfTemplate())
                .addParam("analiseGeo", comunicado.analiseGeo)
                .addParam("comunicado", comunicado)
                .addParam("municipio", municipio)
                .setPageSize(21.0D, 30.0D, 1.0D, 1.0D, 4.0D, 4.0D);

        pdf.generate();

        documento = new Documento(tipoDocumento, pdf.getFile());

        return documento;
    }


    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public void finalizarAnaliseGerente(AnaliseGeo analiseGeo, UsuarioAnalise gerente) throws Exception {

        UsuarioAnalise analistaTecnico = UsuarioAnalise.findByAnalistaTecnico(AnalistaTecnico.distribuicaoAutomaticaAnalistaTecnico(gerente.usuarioEntradaUnica.setorSelecionado.sigla, this));

        if (analistaTecnico != null) {

            if (analiseGeo.tipoResultadoValidacaoGerente.id.equals(TipoResultadoAnalise.PARECER_VALIDADO)) {

                AnaliseGeo analiseGeoBanco = AnaliseGeo.findById(analiseGeo.id);

                this.analise.processo.tramitacao.tramitar(this.analise.processo, AcaoTramitacao.VALIDAR_PARECER_GEO_GERENTE, getUsuarioSessao(), analistaTecnico);
                HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(this.analise.processo.objetoTramitavel.id), getUsuarioSessao());

                new AnalistaTecnico(analiseGeoBanco.analise.analiseTecnica, analistaTecnico);

            } else if (analiseGeo.tipoResultadoValidacaoGerente.id.equals(TipoResultadoAnalise.SOLICITAR_AJUSTES)) {

                AnalistaGeo analista = AnalistaGeo.findByAnaliseGeo(this.id);
                UsuarioAnalise analistaGeo = UsuarioAnalise.findById(analista.usuario.id);

                this.analise.processo.tramitacao.tramitar(this.analise.processo, AcaoTramitacao.SOLICITAR_AJUSTES_PARECER_GEO_PELO_GERENTE, getUsuarioSessao(), analistaGeo);
                HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(this.analise.processo.objetoTramitavel.id), this.analise.processo.objetoTramitavel.usuarioResponsavel);

            } else if (analiseGeo.tipoResultadoValidacaoGerente.id.equals(TipoResultadoAnalise.PARECER_NAO_VALIDADO)) {

                UsuarioAnalise analistaGeoDestino = UsuarioAnalise.findById(analiseGeo.idAnalistaDestino);

                AnalistaGeo analistaGeo = AnalistaGeo.find("id_analise_geo = :id_analise_geo")
                        .setParameter("id_analise_geo", analise.id).first();

                analistaGeo.usuario = analistaGeoDestino;

                analistaGeo._save();

                this.analise.processo.tramitacao.tramitar(this.analise.processo, AcaoTramitacao.INVALIDAR_PARECER_GEO_ENCAMINHANDO_GEO, getUsuarioSessao(), analistaGeoDestino);
                HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(this.analise.processo.objetoTramitavel.id), getUsuarioSessao());

            }
            this.tipoResultadoValidacaoGerente = analiseGeo.tipoResultadoValidacaoGerente;
            this.parecerValidacaoGerente = analiseGeo.parecerValidacaoGerente;
            this.usuarioValidacaoGerente = gerente;
            this.save();

        }
    }

    public void alterarStatusLicenca(String codigoStatus, String numeroLicenca) {

        CaracterizacaoStatusVO caracterizacaoStatusVO = new CaracterizacaoStatusVO(codigoStatus, numeroLicenca);

        new WebService().postJSON(Configuracoes.URL_LICENCIAMENTO + "/caracterizacoes/update/status", caracterizacaoStatusVO);
    }

}
