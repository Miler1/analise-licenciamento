package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import models.portalSeguranca.Usuario;
import play.data.validation.Required;
import play.db.jpa.GenericModel;

@Entity
@Table(schema="analise", name="consultor_juridico")
public class ConsultorJuridico extends GenericModel {
	
	public static final String SEQ = "processo_id_seq";
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	public Long id;
	
	@Required
	@ManyToOne
	@JoinColumn(name="id_analise_juridica")
	public AnaliseJuridica analiseJuridica;
	
	@Required
	@ManyToOne
	@JoinColumn(name="id_usuario")
	public Usuario usuario;
	
	@Required
	@Column(name="data_vinculacao")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataVinculacao;
}
