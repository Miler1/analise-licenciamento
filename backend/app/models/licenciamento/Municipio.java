package models.licenciamento;

import com.vividsolutions.jts.geom.Geometry;
import play.db.jpa.GenericModel;

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

	@Column(name = "the_geom")
	public Geometry limite;
	
	public Geometry getLimite() {

		String jpql = "SELECT m.limite FROM " + Municipio.class.getSimpleName() + " m " + " WHERE id = :idMunicipio";

		Geometry geometry = Municipio.find(jpql)
				.setParameter("idMunicipio", this.id)
				.first();
		return geometry;
	}
	
	public static List<Municipio> findByEstado(String uf) {
		return Municipio.find("estado.codigo = :uf order by nome")
				.setParameter("uf", uf.toUpperCase()).fetch();
	}	
}