package models;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import models.licenciamento.Caracterizacao;
import models.licenciamento.DocumentoLicenciamento;
import models.licenciamento.StatusCaracterizacao;
import models.licenciamento.TipoDocumentoLicenciamento;
import models.pdf.PDFGenerator;
import models.tramitacao.HistoricoTramitacao;
import models.tramitacao.ObjetoTramitavel;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.libs.Crypto;
import utils.Configuracoes;
import utils.QRCode;

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

	public String justificativa;
	
	@Column(name="data_cadastro")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataCadastro;

	@OneToOne
	@JoinColumn(name = "id_historico_tramitacao")
	public HistoricoTramitacao historicoTramitacao;

	@Column(name="data_leitura")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataLeitura;

	public static void criarNotificacoesAnaliseJuridica(AnaliseJuridica analiseJuridica) {
		
		for(AnaliseDocumento analiseDocumento : analiseJuridica.analisesDocumentos) {
			
			if(analiseDocumento.validado == null || analiseDocumento.validado == true) {
				continue;
			}

			analiseDocumento.documento = DocumentoLicenciamento.findById(analiseDocumento.documento.id);
				
			Notificacao notificacao = new Notificacao();
			notificacao.analiseJuridica = analiseJuridica;
			notificacao.tipoDocumento = analiseDocumento.documento.tipo;
			notificacao.analiseDocumento = analiseDocumento;
			notificacao.resolvido = false;
			notificacao.ativo = true;
			notificacao.dataCadastro = new Date();

			Calendar calendario = new GregorianCalendar();
			calendario.setTime(notificacao.dataCadastro);
			int anoDataCadastro = calendario.get(Calendar.YEAR);

			notificacao.codigoSequencia = getProximaSequenciaCodigo(anoDataCadastro, analiseJuridica);
			notificacao.codigoAno = anoDataCadastro;
			notificacao.save();
			
		}
		
		Analise analise = Analise.findById(analiseJuridica.analise.id);
		DiasAnalise verificaDiasAnalise = DiasAnalise.find("analise.id", analise.id).first();
		verificaDiasAnalise.qtdeDiasNotificacao = 0;
		verificaDiasAnalise.save();

		Caracterizacao caracterizacao = analise.processo.caracterizacoes.get(0);
		caracterizacao.status = StatusCaracterizacao.findById(StatusCaracterizacao.NOTIFICADO);
		caracterizacao._save();

		analise.temNotificacaoAberta = true;
		analise._save();
		
	}
	
	public static void criarNotificacoesAnaliseTecnica(AnaliseTecnica analiseTecnica) {
		
		for(AnaliseDocumento analiseDocumento : analiseTecnica.analisesDocumentos) {
			
			if(analiseDocumento.validado == null || analiseDocumento.validado == true) {
				continue;
			}

			analiseDocumento.documento = DocumentoLicenciamento.findById(analiseDocumento.documento.id);
				
			Notificacao notificacao = new Notificacao();
			notificacao.analiseTecnica = analiseTecnica;
			notificacao.tipoDocumento = analiseDocumento.documento.tipo;
			notificacao.analiseDocumento = analiseDocumento;
			notificacao.resolvido = false;
			notificacao.ativo = true;
			notificacao.dataCadastro = new Date();

			Calendar calendario = new GregorianCalendar();
			calendario.setTime(notificacao.dataCadastro);
			int anoDataCadastro = calendario.get(Calendar.YEAR);

			notificacao.codigoSequencia = getProximaSequenciaCodigo(anoDataCadastro, analiseTecnica);
			notificacao.codigoAno = anoDataCadastro;
			notificacao.save();
			
		}
		
		Analise analise = Analise.findById(analiseTecnica.analise.id);
		DiasAnalise verificaDiasAnalise = DiasAnalise.find("analise.id", analise.id).first();
		verificaDiasAnalise.qtdeDiasNotificacao = 0;
		verificaDiasAnalise.save();

		Caracterizacao caracterizacao = analise.processo.caracterizacoes.get(0);
		caracterizacao.status = StatusCaracterizacao.findById(StatusCaracterizacao.NOTIFICADO);
		caracterizacao._save();

		analise.temNotificacaoAberta = true;
		analise._save();
		
	}
	
	public static List<Notificacao> getByAnalise(Analise analise) {
		
		List<Notificacao> notificacoes;
		
		if(analise.getAnaliseTecnica() != null) {
			
			notificacoes = Notificacao.find("analiseTecnica.id = :idAnaliseTecnica AND ativo = true")
					.setParameter("idAnaliseTecnica", analise.getAnaliseTecnica().id)
					.fetch();
			
		} else {
			
			notificacoes = Notificacao.find("analiseJuridica.id = :idAnaliseJuridica AND ativo = true")
					.setParameter("idAnaliseJuridica", analise.getAnaliseJuridica().id)
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

	public Documento gerarPDF() throws Exception {

		Long analiseId;
		TipoDocumento tipoDocumento;
		List<Notificacao> notificacoes;

		if (this.analiseJuridica != null) {

			analiseId = this.analiseJuridica.id;
			tipoDocumento = TipoDocumento.findById(TipoDocumento.NOTIFICACAO_ANALISE_JURIDICA);
			notificacoes = Notificacao.find("id_analise_juridica", analiseId).fetch();

		} else {

			analiseId = this.analiseTecnica.id;
			tipoDocumento = TipoDocumento.findById(TipoDocumento.NOTIFICACAO_ANALISE_TECNICA);
			notificacoes = Notificacao.find("id_analise_tecnica", analiseId).fetch();
		}

		String idQrCode = String.valueOf(notificacoes.get(0).codigoSequencia) + '/' + notificacoes.get(0).codigoAno;
		String url = Configuracoes.APP_URL + "notificacoes/" + Crypto.encryptAES(idQrCode) + "/view";

		PDFGenerator pdf = new PDFGenerator()
				.setTemplate(tipoDocumento.getPdfTemplate())
				.addParam("notificacoes", notificacoes)
				.addParam("qrcode", new QRCode(url).getBase64())
				.addParam("qrcodeLink", url)
				.setPageSize(21.0D, 30.0D, 1.0D, 1.0D, 1.5D, 1.5D);

		if (this.analiseJuridica != null) {

			pdf.addParam("analiseEspecifica", this.analiseJuridica);
			pdf.addParam("analiseArea", "ANALISE_JURIDICA");

		} else {

			pdf.addParam("analiseEspecifica", this.analiseTecnica);
			pdf.addParam("analiseArea", "ANALISE_TECNICA");
		}

		pdf.generate();

		Documento documento = new Documento(tipoDocumento, pdf.getFile());

		return documento;
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
}
