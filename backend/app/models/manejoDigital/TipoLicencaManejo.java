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
@Table(schema = "analise", name = "tipo_licenca_manejo")
public class TipoLicencaManejo extends GenericModel {

	public static final long APAT = 1l;
	public static final long AUTEF = 2l;
	public static final long LAR = 3l;

	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="tipo_licenca_manejo_id_seq")
	@SequenceGenerator(name="tipo_licenca_manejo_id_seq", sequenceName="analise.tipo_licenca_manejo_id_seq", allocationSize=1)
	public Long id;

	@Column
	public String nome;

	@Column
	public Integer codigo;
}