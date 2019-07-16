package models;

import org.hibernate.annotations.Immutable;
import play.db.jpa.GenericModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Immutable
@Table(schema="analise", name="tipo_area_geometria")
public class TipoAreaGeometria extends GenericModel {

	@Id
	public Long id;

	@Column(name="codigo")
	public String codigo;

	@Column(name="nome")
	public String nome;

}
