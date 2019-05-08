package models.manejoDigital;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;

@Entity
@Table(schema = "analise", name = "tipo_licenca_manejo")
public class TipoLicencaManejo extends GenericModel {

	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="analise.tipo_licenca_manejo_id_seq")
	@SequenceGenerator(name="analise.tipo_licenca_manejo_id_seq", sequenceName="analise.tipo_licenca_manejo_id_seq", allocationSize=1)
	public Long id;

	@Required
	@Column
	public String nome;

	@Required
	@Column
	public String codigo;
}
