package models;

import models.licenciamento.Caracterizacao;
import models.licenciamento.DocumentoLicenciamento;
import models.licenciamento.StatusCaracterizacao;
import models.licenciamento.TipoDocumentoLicenciamento;
import models.pdf.PDFGenerator;
import models.tramitacao.HistoricoTramitacao;
import play.db.jpa.GenericModel;
import play.libs.Crypto;
import utils.Configuracoes;
import utils.DateUtil;
import utils.Helper;
import utils.QRCode;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(schema="analise", name="notificacao")
public class Notificacao extends GenericModel {
	
	private final static String SEQ = "analise.notificacao_id_seq";
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;

	@ManyToOne
	@JoinColumn(name="id_analise_juridica", nullable=true)
	public AnaliseJuridica analiseJuridica;
	
	@ManyToOne
	@JoinColumn(name="id_analise_tecnica", nullable=true)
	public AnaliseTecnica analiseTecnica;

	@ManyToOne
	@JoinColumn(name="id_analise_geo", nullable=true)
	public AnaliseGeo analiseGeo;

	@OneToOne
	@JoinColumn(name="id_parecer_analista_geo")
	public ParecerAnalistaGeo parecerAnalistaGeo;

	@OneToOne
	@JoinColumn(name="id_parecer_analista_tecnico")
	public ParecerAnalistaTecnico parecerAnalistaTecnico;
	
	@ManyToOne
	@JoinColumn(name="id_tipo_documento", referencedColumnName="id")
	public TipoDocumentoLicenciamento tipoDocumento;
	
	@ManyToOne
	@JoinColumn(name="id_documento_corrigido", nullable=true)
	public DocumentoLicenciamento documentoCorrigido;
	
	@ManyToOne
	@JoinColumn(name="id_analise_documento")
	public AnaliseDocumento analiseDocumento;
	
	public Boolean resolvido;
	
	public Boolean ativo;

	@Column(name="codigo_sequencia")
	public Long codigoSequencia;

	@Column(name="codigo_ano")
	public Integer codigoAno;
	
	@Column(name="data_notificacao")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataNotificacao;

	@OneToOne
	@JoinColumn(name = "id_historico_tramitacao")
	public HistoricoTramitacao historicoTramitacao;

	@Column(name="data_final_notificacao")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataFinalNotificacao;

	@Column(name="documentacao")
	public Boolean documentacao;

	@Column(name="retificacao_empreendimento")
	public Boolean retificacaoEmpreendimento;

	@Column(name="retificacao_solicitacao")
	public Boolean retificacaoSolicitacao;

	@Column(name="retificacao_solicitacao_com_geo")
	public Boolean retificacaoSolicitacaoComGeo;

	@ManyToMany
	@JoinTable(schema="analise", name="rel_documento_notificacao",
			joinColumns=@JoinColumn(name="id_notificacao"),
			inverseJoinColumns=@JoinColumn(name="id_documento"))
	public List<Documento> documentos;

	@ManyToMany
	@JoinTable(schema="analise", name="rel_documento_notificacao_tecnica",
			joinColumns=@JoinColumn(name="id_notificacao"),
			inverseJoinColumns=@JoinColumn(name="id_documento"))
	public List<Documento> documentosNotificacaoTecnica;

	@Column(name="prazo_notificacao")
	public Integer prazoNotificacao;

	@Column(name="justificativa_documentacao")
	public String justificativaDocumentacao;

	@Column(name="justificativa_retificacao_empreendimento")
	public String justificativaRetificacaoEmpreendimento;

	@Column(name="justificativa_retificacao_solicitacao")
	public String justificativaRetificacaoSolicitacao;

	@Column(name="segundo_email_enviado")
	public Boolean segundoEmailEnviado;

	@Column(name = "data_conclusao")
	public Date dataConclusao;

	@Transient
	public String justificativa;

	@Transient
	public List<Documento> documentosParecer;

	@Transient
	public Integer diasConclusao;

	public Notificacao(AnaliseGeo analiseGeo, Notificacao notificacao, List<Documento> documentos, ParecerAnalistaGeo parecerAnalistaGeo){
		
		this.analiseGeo = analiseGeo;
		this.parecerAnalistaGeo = parecerAnalistaGeo;
		this.resolvido = false;
		this.ativo = true;
		this.dataNotificacao = new Date();
		this.dataFinalNotificacao = Helper.somarDias(dataNotificacao, notificacao.prazoNotificacao);
		this.documentacao = notificacao.documentacao;
		this.retificacaoEmpreendimento = notificacao.retificacaoEmpreendimento;
		this.retificacaoSolicitacao = notificacao.retificacaoSolicitacao;
		this.retificacaoSolicitacaoComGeo = notificacao.retificacaoSolicitacaoComGeo;
		this.prazoNotificacao = notificacao.prazoNotificacao;
		this.documentos = new ArrayList<>();
		this.documentosNotificacaoTecnica = new ArrayList<>();
		this.segundoEmailEnviado = notificacao.segundoEmailEnviado;

	}

	public Notificacao(AnaliseTecnica analiseTecnica, Notificacao notificacao, ParecerAnalistaTecnico parecerAnalistaTecnico){

		this.analiseTecnica = analiseTecnica;
		this.parecerAnalistaTecnico = parecerAnalistaTecnico;
		this.resolvido = false;
		this.ativo = true;
		this.dataNotificacao = new Date();
		this.dataFinalNotificacao = Helper.somarDias(dataNotificacao, notificacao.prazoNotificacao);
		this.documentacao = notificacao.documentacao;
		this.retificacaoEmpreendimento = notificacao.retificacaoEmpreendimento;
		this.retificacaoSolicitacao = notificacao.retificacaoSolicitacao;
		this.retificacaoSolicitacaoComGeo = notificacao.retificacaoSolicitacaoComGeo;
		this.prazoNotificacao = notificacao.prazoNotificacao;
		this.documentos = new ArrayList<>();
		this.documentosNotificacaoTecnica = new ArrayList<>();
		this.segundoEmailEnviado = notificacao.segundoEmailEnviado;

	}

	public Notificacao(){

	}

	public static void criarNotificacoesAnaliseJuridica(AnaliseJuridica analiseJuridica) {

		for(AnaliseDocumento analiseDocumento : analiseJuridica.analisesDocumentos) {

			if(analiseDocumento.validado == null || analiseDocumento.validado) {
				continue;
			}

			analiseDocumento.documento = DocumentoLicenciamento.findById(analiseDocumento.documento.id);

			Notificacao notificacao = new Notificacao();
			notificacao.analiseJuridica = analiseJuridica;
			notificacao.tipoDocumento = analiseDocumento.documento.tipo;
			notificacao.analiseDocumento = analiseDocumento;
			notificacao.resolvido = false;
			notificacao.ativo = true;
			notificacao.dataNotificacao = new Date();

			Calendar calendario = new GregorianCalendar();
			calendario.setTime(notificacao.dataNotificacao);
			int anoDataCadastro = calendario.get(Calendar.YEAR);

			notificacao.codigoSequencia = getProximaSequenciaCodigo(anoDataCadastro, analiseJuridica);
			notificacao.codigoAno = anoDataCadastro;
			notificacao.save();

		}

		Analise analise = Analise.findById(analiseJuridica.analise.id);
		DiasAnalise verificaDiasAnalise = DiasAnalise.find("analise.id", analise.id).first();
		verificaDiasAnalise.qtdeDiasNotificacao = 0;
		verificaDiasAnalise.save();

		for (Caracterizacao caracterizacao : analise.processo.empreendimento.caracterizacoes) {

			caracterizacao.status = StatusCaracterizacao.findById(StatusCaracterizacao.NOTIFICADO);
			caracterizacao._save();
		}

		analise.temNotificacaoAberta = true;
		analise._save();

	}

	public static List<Notificacao> gerarNotificacoesTemporarias(AnaliseJuridica analiseJuridica) {

		List<Notificacao> notificacoes = new ArrayList<>();

		for(AnaliseDocumento analiseDocumento : analiseJuridica.analisesDocumentos) {

			if(analiseDocumento.validado == null || analiseDocumento.validado) {
				continue;
			}

			analiseDocumento.documento = DocumentoLicenciamento.findById(analiseDocumento.documento.id);

			Notificacao notificacao = new Notificacao();
			notificacao.analiseJuridica = analiseJuridica;
			notificacao.tipoDocumento = analiseDocumento.documento.tipo;
			notificacao.analiseDocumento = analiseDocumento;
			notificacao.resolvido = false;
			notificacao.ativo = true;
			notificacao.dataNotificacao = new Date();

			Calendar calendario = new GregorianCalendar();
			calendario.setTime(notificacao.dataNotificacao);
			int anoDataCadastro = calendario.get(Calendar.YEAR);

			notificacao.codigoSequencia = getProximaSequenciaCodigo(anoDataCadastro, analiseJuridica);
			notificacao.codigoAno = anoDataCadastro;

			notificacoes.add(notificacao);

		}

		return notificacoes;

	}

	public static void criarNotificacoesAnaliseTecnica(AnaliseTecnica analiseTecnica) {

		for(AnaliseDocumento analiseDocumento : analiseTecnica.analisesDocumentos) {

			if(analiseDocumento.validado == null || analiseDocumento.validado) {
				continue;
			}

			analiseDocumento.documento = DocumentoLicenciamento.findById(analiseDocumento.documento.id);

			Notificacao notificacao = new Notificacao();
			notificacao.analiseTecnica = analiseTecnica;
			notificacao.tipoDocumento = analiseDocumento.documento.tipo;
			notificacao.analiseDocumento = analiseDocumento;
			notificacao.resolvido = false;
			notificacao.ativo = true;
			notificacao.dataNotificacao = new Date();

			Calendar calendario = new GregorianCalendar();
			calendario.setTime(notificacao.dataNotificacao);
			int anoDataCadastro = calendario.get(Calendar.YEAR);

			notificacao.codigoSequencia = getProximaSequenciaCodigo(anoDataCadastro, analiseTecnica);
			notificacao.codigoAno = anoDataCadastro;
			notificacao.save();

		}

		Analise analise = Analise.findById(analiseTecnica.analise.id);
		DiasAnalise verificaDiasAnalise = DiasAnalise.find("analise.id", analise.id).first();
		verificaDiasAnalise.qtdeDiasNotificacao = 0;
		verificaDiasAnalise.save();

		for (Caracterizacao caracterizacao : analise.processo.empreendimento.caracterizacoes) {

			caracterizacao.status = StatusCaracterizacao.findById(StatusCaracterizacao.NOTIFICADO);
			caracterizacao._save();
		}

		analise.temNotificacaoAberta = true;
		analise._save();

	}

	public static void criarNotificacoesAnaliseGeo(AnaliseGeo analiseGeo) {

		for(AnaliseDocumento analiseDocumento : analiseGeo.analisesDocumentos) {

			if(analiseDocumento.validado == null || analiseDocumento.validado) {
				continue;
			}

			analiseDocumento.documento = DocumentoLicenciamento.findById(analiseDocumento.documento.id);

			Notificacao notificacao = new Notificacao();
			notificacao.analiseGeo = analiseGeo;
			notificacao.tipoDocumento = analiseDocumento.documento.tipo;
			notificacao.analiseDocumento = analiseDocumento;
			notificacao.resolvido = false;
			notificacao.ativo = true;
			notificacao.dataNotificacao = new Date();

			Calendar calendario = new GregorianCalendar();
			calendario.setTime(notificacao.dataNotificacao);
			int anoDataCadastro = calendario.get(Calendar.YEAR);

			notificacao.codigoSequencia = getProximaSequenciaCodigo(anoDataCadastro, analiseGeo);
			notificacao.codigoAno = anoDataCadastro;
			notificacao.save();

		}

		Analise analise = Analise.findById(analiseGeo.analise.id);
		DiasAnalise verificaDiasAnalise = DiasAnalise.find("analise.id", analise.id).first();
		verificaDiasAnalise.qtdeDiasNotificacao = 0;
		verificaDiasAnalise.save();

		for (Caracterizacao caracterizacao : analise.processo.empreendimento.caracterizacoes) {

			caracterizacao.status = StatusCaracterizacao.findById(StatusCaracterizacao.NOTIFICADO);
			caracterizacao._save();
		}

		analise.temNotificacaoAberta = true;
		analise._save();

	}



	public static List<Notificacao> gerarNotificacoesTemporarias(AnaliseGeo analiseGeo) {

		List<Notificacao> notificacoes = new ArrayList<>();

		for(AnaliseDocumento analiseDocumento : analiseGeo.analisesDocumentos) {

			if(analiseDocumento.validado == null || analiseDocumento.validado) {
				continue;
			}

			analiseDocumento.documento = DocumentoLicenciamento.findById(analiseDocumento.documento.id);

			Notificacao notificacao = new Notificacao();
			notificacao.analiseGeo = analiseGeo;
			notificacao.tipoDocumento = analiseDocumento.documento.tipo;
			notificacao.analiseDocumento = analiseDocumento;
			notificacao.resolvido = false;
			notificacao.ativo = true;
			notificacao.dataNotificacao = new Date();

			Calendar calendario = new GregorianCalendar();
			calendario.setTime(notificacao.dataNotificacao);
			int anoDataCadastro = calendario.get(Calendar.YEAR);

			notificacao.codigoSequencia = getProximaSequenciaCodigo(anoDataCadastro, analiseGeo);
			notificacao.codigoAno = anoDataCadastro;

			notificacoes.add(notificacao);

		}

		return notificacoes;

	}


	public static List<Notificacao> gerarNotificacoesTemporarias(AnaliseTecnica analiseTecnica) {

		List<Notificacao> notificacoes = new ArrayList<>();

		for(AnaliseDocumento analiseDocumento : analiseTecnica.analisesDocumentos) {

			if(analiseDocumento.validado == null || analiseDocumento.validado) {
				continue;
			}

			analiseDocumento.documento = DocumentoLicenciamento.findById(analiseDocumento.documento.id);

			Notificacao notificacao = new Notificacao();
			notificacao.analiseTecnica = analiseTecnica;
			notificacao.tipoDocumento = analiseDocumento.documento.tipo;
			notificacao.analiseDocumento = analiseDocumento;
			notificacao.resolvido = false;
			notificacao.ativo = true;
			notificacao.dataNotificacao = new Date();

			Calendar calendario = new GregorianCalendar();
			calendario.setTime(notificacao.dataNotificacao);
			int anoDataCadastro = calendario.get(Calendar.YEAR);

			notificacao.codigoSequencia = getProximaSequenciaCodigo(anoDataCadastro, analiseTecnica);
			notificacao.codigoAno = anoDataCadastro;

			notificacoes.add(notificacao);

		}

		return notificacoes;

	}

	public static List<Notificacao> getByAnalise(Analise analise) {

		List<Notificacao> notificacoes;

		if(analise.getAnaliseTecnica() != null) {

			notificacoes = Notificacao.find("analiseTecnica.id = :idAnaliseTecnica AND ativo = true")
					.setParameter("idAnaliseTecnica", analise.getAnaliseTecnica().id)
					.fetch();

		} else{
			notificacoes = Notificacao.find("analiseGeo.id = :idAnaliseGeo AND ativo = true")
					.setParameter("idAnaliseGeo", analise.getAnaliseGeo().id)
					.fetch();
		}

		return notificacoes;

	}

	public static Notificacao getByAnaliseDocumento(AnaliseDocumento analiseDocumento) {

		return Notificacao.find("byAnaliseDocumento", analiseDocumento).first();

	}

	public static void setHistoricoAlteracoes(List<Notificacao> notificacoes, HistoricoTramitacao historicoTramitacao) {

		for (Notificacao notificacao : notificacoes) {

			notificacao.historicoTramitacao = historicoTramitacao;
			notificacao._save();
		}
	}

	public void setJustificativa() {
		this.justificativa = this.getParecerAnalista().parecer;
	}

	public void setDocumentosParecer() {
		this.documentosParecer = this.getParecerAnalista().getDocumentosParecer();
	}

	public Date getDataNotificacao(){

		return this.dataNotificacao;

	}

	public Documento gerarPDF() throws Exception {

		Long analiseId;
		TipoDocumento tipoDocumento;
		List<Notificacao> notificacoes;

		if (this.analiseJuridica != null) {

			analiseId = this.analiseJuridica.id;
			tipoDocumento = TipoDocumento.findById(TipoDocumento.NOTIFICACAO_ANALISE_JURIDICA);
			notificacoes = Notificacao.find("id_analise_juridica", analiseId).fetch();

		} else if (this.analiseTecnica != null){

			analiseId = this.analiseTecnica.id;
			tipoDocumento = TipoDocumento.findById(TipoDocumento.NOTIFICACAO_ANALISE_TECNICA);
			notificacoes = Notificacao.find("id_analise_tecnica", analiseId).fetch();

		} else {

			analiseId = this.analiseGeo.id;
			tipoDocumento = TipoDocumento.findById(TipoDocumento.NOTIFICACAO_ANALISE_GEO);
			notificacoes = Notificacao.find("id_analise_geo", analiseId).fetch();
		}

		String idQrCode = String.valueOf(notificacoes.get(0).codigoSequencia) + '/' + notificacoes.get(0).codigoAno;
		String url = Configuracoes.APP_URL + "notificacoes/" + Crypto.encryptAES(idQrCode) + "/view";

		PDFGenerator pdf = new PDFGenerator()
				.setTemplate(tipoDocumento.getPdfTemplate())
				.addParam("notificacoes", notificacoes)
				.addParam("qrcode", new QRCode(url).getBase64())
				.addParam("qrcodeLink", url)
				.setPageSize(21.0D, 30.0D, 1.0D, 1.0D, 1.5D, 1.5D);

		if (this.analiseTecnica != null) {

			pdf.addParam("analiseEspecifica", this.analiseTecnica);
			pdf.addParam("analiseArea", "ANALISE_TECNICA");

		} else{
			pdf.addParam("analiseEspecifica", this.analiseGeo);
			pdf.addParam("analiseArea", "ANALISE_GEO");
		}

		pdf.generate();

		Documento documento = new Documento(tipoDocumento, pdf.getFile());

		return documento;
	}

	public static Documento gerarPDF(List<Notificacao> notificacoes, AnaliseJuridica analiseJuridica) throws Exception {

		TipoDocumento tipoDocumento = TipoDocumento.findById(TipoDocumento.NOTIFICACAO_ANALISE_JURIDICA);

		PDFGenerator pdf = new PDFGenerator()
				.setTemplate(tipoDocumento.getPdfTemplate())
				.addParam("notificacoes", notificacoes)
				.addParam("analiseEspecifica", analiseJuridica)
				.addParam("analiseArea", "ANALISE_JURIDICA")
				.setPageSize(21.0D, 30.0D, 1.0D, 1.0D, 1.5D, 1.5D);

		pdf.generate();

		return new Documento(tipoDocumento, pdf.getFile());
	}


	public static Documento gerarPDF(List<Notificacao> notificacoes, AnaliseGeo analiseGeo) throws Exception {

		TipoDocumento tipoDocumento = TipoDocumento.findById(TipoDocumento.NOTIFICACAO_ANALISE_GEO);

		PDFGenerator pdf = new PDFGenerator()
				.setTemplate(tipoDocumento.getPdfTemplate())
				.addParam("notificacoes", notificacoes)
				.addParam("analiseEspecifica", analiseGeo)
				.addParam("analiseArea", "ANALISE_GEO")
				.setPageSize(21.0D, 30.0D, 1.0D, 1.0D, 1.5D, 1.5D);

		pdf.generate();

		return new Documento(tipoDocumento, pdf.getFile());
	}


	public static Documento gerarPDF(List<Notificacao> notificacoes, AnaliseTecnica analiseTecnica) throws Exception {

		TipoDocumento tipoDocumento = TipoDocumento.findById(TipoDocumento.NOTIFICACAO_ANALISE_TECNICA);

		PDFGenerator pdf = new PDFGenerator()
				.setTemplate(tipoDocumento.getPdfTemplate())
				.addParam("notificacoes", notificacoes)
				.addParam("analiseEspecifica", analiseTecnica)
				.addParam("analiseArea", "ANALISE_TECNICA")
				.setPageSize(21.0D, 30.0D, 1.0D, 1.0D, 1.5D, 1.5D);

		pdf.generate();

		return new Documento(tipoDocumento, pdf.getFile());
	}

	public static long getProximaSequenciaCodigo(int anoDataCadastro, AnaliseJuridica analiseJuridica) {

		List<Notificacao> notificacoes = Notificacao.find("codigoAno = :x order by codigoSequencia desc")
				.setParameter("x", anoDataCadastro)
				.fetch();

		if (notificacoes.size() == 0) {

			return 1;
		}

		Notificacao notificacao = notificacoes.get(0);
		AnaliseJuridica analiseNotificacao = notificacao.analiseJuridica;

		if (notificacao.codigoSequencia == null) {

			return 1;
		}

		if (analiseNotificacao != null && analiseJuridica.id.equals(analiseNotificacao.id)) {

			return notificacao.codigoSequencia;
		}

		return notificacao.codigoSequencia + 1;
	}

	public static long getProximaSequenciaCodigo(int anoDataCadastro, AnaliseTecnica analiseTecnica) {

		List<Notificacao> notificacoes = Notificacao.find("codigoAno = :x order by codigoSequencia desc")
				.setParameter("x", anoDataCadastro)
				.fetch();

		if (notificacoes.size() == 0) {

			return 1;
		}

		Notificacao notificacao = notificacoes.get(0);
		AnaliseTecnica analiseNotificacao = notificacao.analiseTecnica;

		if (notificacao.codigoSequencia == null) {

			return 1;
		}

		if (analiseNotificacao != null && analiseTecnica.id.equals(analiseNotificacao.id)) {

			return notificacao.codigoSequencia;
		}

		return notificacao.codigoSequencia + 1;
	}

	public static long getProximaSequenciaCodigo(int anoDataCadastro, AnaliseGeo analiseGeo) {

		List<Notificacao> notificacoes = Notificacao.find("codigoAno = :x order by codigoSequencia desc")
				.setParameter("x", anoDataCadastro)
				.fetch();

		if (notificacoes.size() == 0) {

			return 1;
		}

		Notificacao notificacao = notificacoes.get(0);
		AnaliseGeo analiseNotificacao = notificacao.analiseGeo;

		if (notificacao.codigoSequencia == null) {

			return 1;
		}

		if (analiseNotificacao != null && analiseGeo.id.equals(analiseNotificacao.id)) {

			return notificacao.codigoSequencia;
		}

		return notificacao.codigoSequencia + 1;
	}

	public ParecerAnalista getParecerAnalista() {
		 return this.parecerAnalistaGeo != null ? this.parecerAnalistaGeo : this.parecerAnalistaTecnico;
	}

	public void setDiasConclusao(){
		if(this.dataNotificacao != null && this.dataConclusao != null) {
			this.diasConclusao = DateUtil.getDiferencaEmDias(this.dataNotificacao, this.dataConclusao);
		}
	}

}
