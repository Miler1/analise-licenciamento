package models;

import java.util.Date;

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

import exceptions.PermissaoNegadaException;
import models.licenciamento.Licenca;
import models.portalSeguranca.Perfil;
import models.portalSeguranca.Usuario;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.db.jpa.JPABase;
import utils.Identificavel;
import utils.Mensagem;
import utils.validacao.Validacao;

@Entity
@Table(schema="analise", name="recomendacao")
public class Recomendacao extends GenericModel implements Identificavel {
	
	public static final String SEQ = "analise.recomendacao_id_seq";
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;
	
	@Required
	@ManyToOne
	@JoinColumn(name="id_licenca_analise")
	public LicencaAnalise licencaAnalise;
	
	@Required
	public String texto;
	
	@Required
	public Integer ordem;

	@Override
	public Long getId() {
		
		return this.id;
	}
	
	@Override
	public Recomendacao save() {

		Validacao.validar(this);
		
		return super.save();
	}

	public void update(Recomendacao novaRecomendacao) {

		this.texto = novaRecomendacao.texto;
		this.ordem = novaRecomendacao.ordem;
		
		this.save();
	}
}