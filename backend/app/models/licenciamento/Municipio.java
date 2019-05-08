package models.licenciamento;

import com.vividsolutions.jts.geom.Geometry;
import org.hibernate.SQLQuery;
import org.hibernate.spatial.GeometryType;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.List;

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
	
	@ManyToMany
	@JoinTable(schema="licenciamento", name="rel_municipio_atividade_nao_apta", 
		joinColumns = @JoinColumn(name = "id_municipio"), 
		inverseJoinColumns = @JoinColumn(name="id_atividade"))
	public List<Atividade> atividadesNaoAptas;
	
	public Geometry getLimite() {

		// TODO PROCURAR NO BANCO
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