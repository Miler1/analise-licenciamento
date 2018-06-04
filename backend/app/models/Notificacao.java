package models;

import java.util.Date;
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

import models.licenciamento.DocumentoLicenciamento;
import models.licenciamento.TipoDocumentoLicenciamento;
import models.pdf.PDFGenerator;
import models.tramitacao.HistoricoTramitacao;
import models.tramitacao.ObjetoTramitavel;
import play.data.validation.Required;
import play.db.jpa.GenericModel;

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
	
	public String justificativa;
	
	@Column(name="data_cadastro")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataCadastro;

	@OneToOne
	@JoinColumn(name = "id_historico_tramitacao")
	public HistoricoTramitacao historicoTramitacao;
	
	public static void criarNotificacoesAnaliseJuridica(AnaliseJuridica analiseJuridica) {
		
		for(AnaliseDocumento analiseDocumento : analiseJuridica.analisesDocumentos) {
			
			if(analiseDocumento.validado == null || analiseDocumento.validado == true) {
				continue;
			}
			
			TipoDocumentoLicenciamento tipoDoc = TipoDocumentoLicenciamento.findById(analiseDocumento.documento.tipo.id);
				
			Notificacao notificacao = new Notificacao();
			notificacao.analiseJuridica = analiseJuridica;
			notificacao.tipoDocumento = tipoDoc;
			notificacao.analiseDocumento = analiseDocumento;
			notificacao.resolvido = false;
			notificacao.ativo = true;
			notificacao.dataCadastro = new Date();
			notificacao.save();
			
		}
		
		Analise analise = Analise.findById(analiseJuridica.analise.id);
		DiasAnalise verificaDiasAnalise = DiasAnalise.find("analise.id", analise.id).first();
		verificaDiasAnalise.qtdeDiasNotificacao = 1;
		verificaDiasAnalise.save();

		analise.temNotificacaoAberta = true;
		analise._save();
		
	}
	
	public static void criarNotificacoesAnaliseTecnica(AnaliseTecnica analiseTecnica) {
		
		for(AnaliseDocumento analiseDocumento : analiseTecnica.analisesDocumentos) {
			
			if(analiseDocumento.validado == null || analiseDocumento.validado == true) {
				continue;
			}
			
			TipoDocumentoLicenciamento tipoDoc = TipoDocumentoLicenciamento.findById(analiseDocumento.documento.tipo.id);
				
			Notificacao notificacao = new Notificacao();
			notificacao.analiseTecnica = analiseTecnica;
			notificacao.tipoDocumento = tipoDoc;
			notificacao.analiseDocumento = analiseDocumento;
			notificacao.resolvido = false;
			notificacao.ativo = true;
			notificacao.dataCadastro = new Date();
			notificacao.save();
			
		}
		
		Analise analise = Analise.findById(analiseTecnica.analise.id);
		DiasAnalise verificaDiasAnalise = DiasAnalise.find("analise.id", analise.id).first();
		verificaDiasAnalise.qtdeDiasNotificacao = 1;
		verificaDiasAnalise.save();

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

	public Documento gerarPDFNotificacaoAnaliseJuridica() throws Exception {

		TipoDocumento tipoDocumento = TipoDocumento.findById(TipoDocumento.NOTIFICACAO_ANALISE_JURIDICA);

		PDFGenerator pdf = new PDFGenerator()
				.setTemplate(tipoDocumento.getPdfTemplate())
				.addParam("analise", this)
				.setPageSize(21.0D, 30.0D, 0.5D, 0.5D, 1.5D, 1.5D);

		pdf.generate();

		Documento documento = new Documento(tipoDocumento, pdf.getFile());

		return documento;

	}
    
}
