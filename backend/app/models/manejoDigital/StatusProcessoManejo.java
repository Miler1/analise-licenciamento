package models.manejoDigital;

import play.db.jpa.GenericModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(schema = "analise", name = "status_processo_manejo")
public class StatusProcessoManejo extends GenericModel {

	public static final long AGUARDANDO_ANALISE = 1l;
	public static final long EM_ANALISE = 2l;
	public static final long DEFERIDO = 3l;
	public static final long INDEFERIDO = 4l;

	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="analise.status_processo_manejo_id_seq")
	@SequenceGenerator(name="analise.tipo_licenca_manejo_id_seq", sequenceName="analise.status_processo_manejo_id_seq", allocationSize=1)
	public Long id;

	@Column
	public String nome;

	@Column
	public Integer codigo;
}
