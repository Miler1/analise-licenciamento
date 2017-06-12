package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

@Entity
@Table(schema="analise", name="tipo_resultado_analise")
public class TipoResultadoAnalise extends GenericModel {
	
	public static Long DEFERIDO = 1l;
	public static Long INDEFERIDO = 2l;
	public static Long EMITIR_NOTIFICACAO = 3l;
	
	public static final String SEQ = "analise.tipo_resultado_analise_id_seq";
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;
	
	@Required
	public String nome;
}
