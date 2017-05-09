package models;

import groovy.transform.BaseScript;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.annotations.Type;
import org.hibernate.spatial.GeometryType;

import com.vividsolutions.jts.geom.Geometry;

import play.db.jpa.GenericModel;
import play.db.jpa.JPA;

@Entity
@Table(schema = "licenciamento", name = "municipio")
public class Municipio extends GenericModel {

	@Id
	@Column(name="id_municipio")
	public Long id;
	
	public String nome;
	
	@ManyToOne
	@JoinColumn(name = "cod_estado")
	public Estado estado;
	
	@Column(name="apto_licenciamento")
	public Boolean aptoLicenciamento;

	@Column(name="cod_tse")
	public Long codigoTse;
	
	public Municipio(long id) {
		this.id = id;
	}
	
	public Geometry getLimite() {
		
		String sql = "SELECT the_geom FROM licenciamento.municipio WHERE id_municipio = :id";
		
		return (Geometry) JPA.em().createNativeQuery(sql)
			.unwrap(SQLQuery.class)
			.addScalar("the_geom", GeometryType.INSTANCE)
			.setParameter("id", this.id)
			.uniqueResult();
	}
	
	public static List<Municipio> findByEstado(String uf) {
		return Municipio.find("estado.codigo = :uf order by nome")
				.setParameter("uf", uf.toUpperCase()).fetch();
	}
}