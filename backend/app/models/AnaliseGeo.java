package models;

import exceptions.ValidacaoException;
import models.licenciamento.Caracterizacao;
import models.licenciamento.TipoAnalise;
import models.pdf.PDFGenerator;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;
import models.validacaoParecer.*;
import org.apache.commons.lang.StringUtils;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import utils.Configuracoes;
import utils.ListUtil;
import utils.Mensagem;
import utils.ModelUtil;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(schema="analise", name="analise_geo")
public class AnaliseGeo extends GenericModel implements Analisavel {

    public static final String SEQ = "analise.analise_geo_id_seq";

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
    @SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
    public Long id;

    @ManyToOne
    @JoinColumn(name="id_analise")
    public Analise analise;

    public String parecer;

    @Required
    @Column(name="data_vencimento_prazo")
    public Date dataVencimentoPrazo;

    @Required
    @Column(name="revisao_solicitada")
    public Boolean revisaoSolicitada;

    @Required
    @Column(name="notificacao_atendida")
    public Boolean notificacaoAtendida;

    public Boolean ativo;

    @OneToOne
    @JoinColumn(name="id_analise_geo_revisada", referencedColumnName="id")
    public AnaliseGeo analiseGeoRevisada;

    @Column(name="data_inicio")
    @Temporal(TemporalType.TIMESTAMP)
    public Date dataInicio;

    @Column(name="data_fim")
    @Temporal(TemporalType.TIMESTAMP)
    public Date dataFim;

    @ManyToOne
    @JoinColumn(name="id_tipo_resultado_analise")
    public TipoResultadoAnalise tipoResultadoAnalise;

    @ManyToOne
    @JoinColumn(name="id_tipo_resultado_validacao")
    public TipoResultadoAnalise tipoResultadoValidacao;

    @ManyToMany
    @JoinTable(schema="analise", name="rel_documento_analise_geo",
            joinColumns=@JoinColumn(name="id_analise_geo"),
            inverseJoinColumns=@JoinColumn(name="id_documento"))
    public List<Documento> documentos;

    @OneToMany(mappedBy="analiseGeo", cascade=CascadeType.ALL)
    public List<AnaliseDocumento> analisesDocumentos;

    @OneToMany(mappedBy="analiseGeo", cascade=CascadeType.ALL)
    public List<AnalistaGeo> analistasGeo;

    @Column(name="parecer_validacao")
    public String parecerValidacao;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "id_usuario_validacao", referencedColumnName = "id")
    public UsuarioAnalise usuarioValidacao;

    @OneToMany(mappedBy="analiseGeo", orphanRemoval = true)
    public List<LicencaAnalise> licencasAnalise;

    @OneToMany(mappedBy = "analiseGeo", orphanRemoval = true)
    public List<ParecerGeoRestricao> pareceresGeoRestricoes;

    @Column(name="justificativa_coordenador")
    public String justificativaCoordenador;

    @ManyToOne
    @JoinColumn(name="id_tipo_resultado_validacao_gerente")
    public TipoResultadoAnalise tipoResultadoValidacaoGerente;

    @Column(name="parecer_validacao_gerente")
    public String parecerValidacaoGerente;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "id_usuario_validacao_gerente", referencedColumnName = "id")
    public UsuarioAnalise usuarioValidacaoGerente;

    @OneToMany(mappedBy="analiseGeo", cascade=CascadeType.ALL)
    public List<Gerente> gerentes;

    @Required
    @Column(name="data_cadastro")
    @Temporal(TemporalType.TIMESTAMP)
    public Date dataCadastro;

    @ManyToOne
    @JoinColumn(name="id_tipo_resultado_validacao_aprovador")
    public TipoResultadoAnalise tipoResultadoValidacaoAprovador;

    @Column(name="parecer_validacao_aprovador")
    public String parecerValidacaoAprovador;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "id_usuario_validacao_aprovador", referencedColumnName = "id")
    public UsuarioAnalise usuarioValidacaoAprovador;

    @Column(name="data_fim_validacao_aprovador")
    @Temporal(TemporalType.TIMESTAMP)
    public Date dataFimValidacaoAprovador;

    private void validarParecer() {

        if(StringUtils.isBlank(this.parecer))
            throw new ValidacaoException(Mensagem.ANALISE_PARECER_NAO_PREENCHIDO);
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

        if(this.dataInicio == null) {

            Calendar c = Calendar.getInstance();
            c.setTime(new Date());

            this.dataInicio = c.getTime();

            this._save();

            iniciarLicencas();
        }

        this.analise.processo.tramitacao.tramitar(this.analise.processo, AcaoTramitacao.INICIAR_ANALISE_GEO, usuarioExecutor);
        HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(this.analise.processo.objetoTramitavel.id), usuarioExecutor);
    }

    public Boolean validarEmissaoLicencas(List<LicencaAnalise> licencas) {
        for(int i = 0; i < licencas.size() ; i++ ) {
            LicencaAnalise licencaVerificar = licencas.get(i);
            if (licencaVerificar.emitir) {
                return true;
            }
        }
        throw new ValidacaoException(Mensagem.ERRO_NENHUMA_LICENCA_EMITIDA);
    }

    private void iniciarLicencas() {

        List<LicencaAnalise> novasLicencasAnalise = new ArrayList<>();

        for (Caracterizacao caracterizacao : this.analise.processo.caracterizacoes) {

            LicencaAnalise novaLicencaAnalise = new LicencaAnalise();
            novaLicencaAnalise.caracterizacao = caracterizacao;
            novaLicencaAnalise.validade = caracterizacao.tipoLicenca.validadeEmAnos;

            novasLicencasAnalise.add(novaLicencaAnalise);
        }

        updateLicencasAnalise(novasLicencasAnalise);
    }

    public void update(AnaliseGeo novaAnalise) {

        if(this.dataFim != null) {
            throw new ValidacaoException(Mensagem.ANALISE_GEO_CONCLUIDA);
        }

        this.parecer = novaAnalise.parecer;

        if(novaAnalise.tipoResultadoAnalise != null &&
                novaAnalise.tipoResultadoAnalise.id != null) {

            this.tipoResultadoAnalise = novaAnalise.tipoResultadoAnalise;
        }

        updateDocumentos(novaAnalise.documentos);

        if (this.analisesDocumentos == null) {

            this.analisesDocumentos = new ArrayList<>();
        }

        for(AnaliseDocumento novaAnaliseDocumento : novaAnalise.analisesDocumentos) {

            AnaliseDocumento analiseDocumento = ListUtil.getById(novaAnaliseDocumento.id, this.analisesDocumentos);

            if(analiseDocumento != null) {

                analiseDocumento.update(novaAnaliseDocumento);

            } else {

                novaAnaliseDocumento.analiseGeo = this;
                this.analisesDocumentos.add(novaAnaliseDocumento);
            }
        }

        this._save();

        updateLicencasAnalise(novaAnalise.licencasAnalise);
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

        for(LicencaAnalise novaLicencaAnalise : novasLicencasAnalise) {

            LicencaAnalise licencaAnalise = ListUtil.getById(novaLicencaAnalise.id, this.licencasAnalise);

            if(licencaAnalise != null) {

                licencaAnalise.update(novaLicencaAnalise);

            } else {

                novaLicencaAnalise.analiseGeo = this;
                novaLicencaAnalise.save();

                this.licencasAnalise.add(novaLicencaAnalise);
            }
        }
    }

    private void updateDocumentos(List<Documento> novosDocumentos) {

        TipoDocumento tipo = TipoDocumento.findById(TipoDocumento.PARECER_ANALISE_GEO);

        if (this.documentos == null)
            this.documentos = new ArrayList<>();

        Iterator<Documento> docsCadastrados = documentos.iterator();
        List<Documento> documentosDeletar = new ArrayList<>();

        while (docsCadastrados.hasNext()) {

            Documento docCadastrado = docsCadastrados.next();

            if (ListUtil.getById(docCadastrado.id, novosDocumentos) == null) {

                docsCadastrados.remove();
                // remove o documeto do banco apenas se ele não estiver relacionado
                // com outra análises
                List<AnaliseGeo> analisesGeoRelacionadas = docCadastrado.getAnaliseGeoRelacionadas();
                if(analisesGeoRelacionadas.size() == 0) {

                    documentosDeletar.add(docCadastrado);
                }
            }
        }

        for (Documento novoDocumento : novosDocumentos) {

            if (novoDocumento.id == null) {

                novoDocumento.tipo = tipo;
                novoDocumento.save();
                this.documentos.add(novoDocumento);
            }
        }

        ModelUtil.deleteAll(documentosDeletar);
    }

    public static AnaliseGeo findByNumeroProcesso(String numeroProcesso) {

        return AnaliseGeo.find("analise.processo.numero = :numeroProcesso AND ativo = true")
                .setParameter("numeroProcesso", numeroProcesso)
                .first();
    }

    public void finalizar(AnaliseGeo analise, UsuarioAnalise usuarioExecutor) {

        this.update(analise);

        validarLicencasAnalise();
        validarParecer();
        validarAnaliseDocumentos();
        validarResultado();

        if(this.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.DEFERIDO))
            validarEmissaoLicencas(this.licencasAnalise);

        if(this.analise.diasAnalise.qtdeDiasAprovador == null) {

            this.analise.diasAnalise.qtdeDiasAprovador = 0;
            this.analise.diasAnalise.save();
        }

        this._save();



        if(this.tipoResultadoAnalise.id == TipoResultadoAnalise.DEFERIDO) {

            if(this.usuarioValidacaoGerente != null) {

                this.analise.processo.tramitacao.tramitar(this.analise.processo, AcaoTramitacao.DEFERIR_ANALISE_GEO_VIA_GERENTE, usuarioExecutor);
                HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(this.analise.processo.objetoTramitavel.id), usuarioExecutor);
            }

        } else if(this.tipoResultadoAnalise.id == TipoResultadoAnalise.INDEFERIDO) {

            if(this.usuarioValidacaoGerente != null) {

                this.analise.processo.tramitacao.tramitar(this.analise.processo, AcaoTramitacao.INDEFERIR_ANALISE_GEO_VIA_GERENTE, usuarioExecutor);
                HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(this.analise.processo.objetoTramitavel.id), usuarioExecutor);
            }

        } else {

            Notificacao.criarNotificacoesAnaliseGeo(analise);

            this.analise.processo.tramitacao.tramitar(this.analise.processo, AcaoTramitacao.NOTIFICAR, usuarioExecutor);
            HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(this.analise.processo.objetoTramitavel.id), usuarioExecutor);

            HistoricoTramitacao historicoTramitacao = HistoricoTramitacao.getUltimaTramitacao(this.analise.processo.objetoTramitavel.id);

            List<Notificacao> notificacoes = Notificacao.find("analiseGeo.id", this.id).fetch();
            Notificacao.setHistoricoAlteracoes(notificacoes, historicoTramitacao);

            HistoricoTramitacao.setSetor(historicoTramitacao, usuarioExecutor);

            enviarEmailNotificacao();
        }
    }

    public void enviarEmailNotificacao() {

        List<String> destinatarios = new ArrayList<String>();
        destinatarios.addAll(this.analise.processo.empreendimento.emailsProprietarios());
        destinatarios.addAll(this.analise.processo.empreendimento.emailsResponsaveis());

        EmailNotificacaoAnaliseGeo notificacao = new EmailNotificacaoAnaliseGeo(this, destinatarios);
        notificacao.enviar();
    }

    public void validaParecer(AnaliseGeo analiseGeo, UsuarioAnalise usuarioExecutor) {

        TipoResultadoAnaliseChain<AnaliseGeo> tiposResultadosAnalise = new ParecerValidadoGeo();;
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

        if(this.tipoResultadoAnalise.id == TipoResultadoAnalise.DEFERIDO) {

            for (LicencaAnalise licencaAnalise : this.licencasAnalise) {

                if (licencaAnalise.emitir == null) {

                    throw new ValidacaoException(Mensagem.ANALISE_GEO_LICENCA_SEM_VALIDACAO);
                }
            }
        }
    }

    private void validarResultado() {

        if(this.tipoResultadoAnalise == null)
            throw new ValidacaoException(Mensagem.ANALISE_SEM_RESULTADO);

        boolean todosDocumentosValidados = true;
        for(AnaliseDocumento analise : this.analisesDocumentos) {

            if(analise.documento.tipo.tipoAnalise.equals(TipoAnalise.GEO)) {

                if(analise.validado == null || (!analise.validado && StringUtils.isBlank(analise.parecer))) {

                    throw new ValidacaoException(Mensagem.ANALISE_DOCUMENTO_NAO_AVALIADO);
                }
                todosDocumentosValidados &= analise.validado;
            }
        }

        if(this.tipoResultadoAnalise.id == TipoResultadoAnalise.DEFERIDO && !todosDocumentosValidados) {

            throw new ValidacaoException(Mensagem.TODOS_OS_DOCUMENTOS_VALIDOS);
        }
    }

    private void validarAnaliseDocumentos() {

        if(this.analisesDocumentos == null || this.analisesDocumentos.size() == 0)
            throw new ValidacaoException(Mensagem.ANALISE_DOCUMENTO_NAO_AVALIADO);

        for(AnaliseDocumento analise : this.analisesDocumentos) {

            if(analise.documento.tipo.tipoAnalise.equals(TipoAnalise.GEO) &&
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

        for (AnaliseDocumento analiseDocumento: this.analisesDocumentos) {

            AnaliseDocumento copiaAnaliseDoc = analiseDocumento.gerarCopia();
            copiaAnaliseDoc.analiseGeo = copia;
            copia.analisesDocumentos.add(copiaAnaliseDoc);
        }

        copia.analistasGeo = new ArrayList<>();

        for (AnalistaGeo analistaGeo: this.analistasGeo) {

            AnalistaGeo copiaAnalistaGeo = analistaGeo.gerarCopia();

            copiaAnalistaGeo.analiseGeo = copia;
            copia.analistasGeo.add(copiaAnalistaGeo);
        }

        copia.gerentes = new ArrayList<>();

        for (Gerente gerente: this.gerentes) {

            Gerente copiaGerenteGeo = gerente.gerarCopia();

            copiaGerenteGeo.analiseGeo= copia;
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

    public Documento gerarPDFParecer() throws Exception {

        TipoDocumento tipoDocumento = TipoDocumento.findById(TipoDocumento.PARECER_ANALISE_GEO);

        PDFGenerator pdf = new PDFGenerator()
                .setTemplate(tipoDocumento.getPdfTemplate())
                .addParam("analiseEspecifica", this)
                .addParam("analiseArea", "ANALISE_GEO")
                .setPageSize(21.0D, 30.0D, 1.0D, 1.0D, 1.5D, 1.5D);

        pdf.generate();

        Documento documento = new Documento(tipoDocumento, pdf.getFile());

        return documento;

    }
}