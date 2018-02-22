package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import models.licenciamento.DocumentoLicenciamento;
import models.licenciamento.TipoDocumentoLicenciamento;
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
	
	public static void criarNotificacoesAnaliseJuridica(AnaliseJuridica analiseJuridica) {
		
		for(AnaliseDocumento analiseDocumento : analiseJuridica.analisesDocumentos) {
			
			if(analiseDocumento.validado == null || analiseDocumento.validado == true) {
				continue;
			}
				
			Notificacao notificacao = new Notificacao();
			notificacao.analiseJuridica = analiseJuridica;
			notificacao.tipoDocumento = analiseDocumento.documento.tipo;
			notificacao.analiseDocumento = analiseDocumento;
			notificacao.resolvido = false;
			notificacao.ativo = true;
			notificacao.save();
			
		}
		
		Analise analise = Analise.findById(analiseJuridica.analise.id);

		analise.temNotificacaoAberta = true;
		analise._save();
		
	}
	
	public static void criarNotificacoesAnaliseTecnica(AnaliseTecnica analiseTecnica) {
		
		for(AnaliseDocumento analiseDocumento : analiseTecnica.analisesDocumentos) {
			
			if(analiseDocumento.validado == null || analiseDocumento.validado == true) {
				continue;
			}
				
			Notificacao notificacao = new Notificacao();
			notificacao.analiseTecnica = analiseTecnica;
			notificacao.tipoDocumento = analiseDocumento.documento.tipo;
			notificacao.analiseDocumento = analiseDocumento;
			notificacao.resolvido = false;
			notificacao.ativo = true;
			notificacao.save();
			
		}
		
		Analise analise = Analise.findById(analiseTecnica.analise.id);

		analise.temNotificacaoAberta = true;
		analise._save();
		
	}
	
	public static List<Notificacao> getByAnalise(Analise analise) {
		
		List<Notificacao> notificacoes = Notificacao.find("(analiseJuridica.id = :idAnaliseJuridica OR analiseTecnica = :idAnaliseTecnica) AND ativo = true")
				.setParameter("idAnaliseJuridica", analise.getAnaliseJuridica().id)
				.setParameter("idAnaliseTecnica", analise.getAnaliseTecnica().id)
				.fetch();
		
		return notificacoes;
		
	}
    
}
