package models.manejoDigital;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "analise", name = "tipo_licenca_manejo")
public class TipoLicencaManejo {

	public static final long APAT = 1l;
	public static final long AUTEF = 2l;
	public static final long LAR = 3l;

	@Id
	public Long id;

	@Column
	public String nome;

	@Column
	public Integer codigo;
}