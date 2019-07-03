package models.licenciamento;

import com.vividsolutions.jts.geom.Geometry;
import play.db.jpa.GenericModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "licenciamento", name="estado")
public class Estado extends GenericModel {
	
	@Id
	@Column(name="cod_estado")
	public String codigo;

	public String nome;

	@Column(name = "the_geom")
	public Geometry limite;

	public Geometry getLimite() {

		String jpql = "SELECT est.limite FROM " + Estado.class.getSimpleName() + " est " + " WHERE codigo = :codEstado";

		Geometry geometry = Estado.find(jpql)
				.setParameter("codEstado", this.codigo)
				.first();
		return geometry;

	}
	
}
