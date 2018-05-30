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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import models.licenciamento.DocumentoLicenciamento;
import models.licenciamento.TipoDocumentoLicenciamento;
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

	@Column(name="codigo_sequencia")
	public Long codigoSequencia;

	@Column(name="codigo_ano")
	public Integer codigoAno;

	public String justificativa;
	
	@Column(name="data_cadastro")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataCadastro;
	
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

			Calendar calendario = new GregorianCalendar();
			calendario.setTime(notificacao.dataCadastro);
			int anoDataCadastro = calendario.get(Calendar.YEAR);

			notificacao.codigoSequencia = notificacao.getProximaSequenciaCodigo(anoDataCadastro);
			notificacao.codigoAno = anoDataCadastro;
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

			Calendar calendario = new GregorianCalendar();
			calendario.setTime(notificacao.dataCadastro);
			int anoDataCadastro = calendario.get(Calendar.YEAR);

			notificacao.codigoSequencia = notificacao.getProximaSequenciaCodigo(anoDataCadastro);
			notificacao.codigoAno = anoDataCadastro;
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

	public static long getProximaSequenciaCodigo(int anoDataCadastro) {

		List<Notificacao> notificacoes = Notificacao.find("order by id desc").fetch();

		if (notificacoes.size() == 0 || notificacoes.get(0).codigoSequencia == null) {

			return 1;
		}

		if (anoDataCadastro > notificacoes.get(0).codigoAno) {

			return 1;
		}

		return notificacoes.get(0).codigoSequencia + 1;
	}
}
