package models.portalSeguranca;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.GenericModel;
import utils.Identificavel;

@Entity
@Table(schema = "portal_seguranca", name = "setor")
public class Setor extends GenericModel {
	
	@Id
	@Column(name="id")
	public Integer id;
	
	public String nome;
	
	public String sigla;
	
	@ManyToOne
	@JoinColumn(name="id_setor_pai")
	public Setor setorPai;
	
	@ManyToMany
	@JoinTable(schema = "portal_seguranca", name = "perfil_setor",
			joinColumns = @JoinColumn(name = "id_setor"),
			inverseJoinColumns = @JoinColumn(name = "id_perfil"))
	public List<Perfil> perfis;
	
	@Column(name="tipo_setor")
	@Enumerated(EnumType.ORDINAL)
	public TipoSetor tipoSetor;
	
	public List<Setor> getSetoresFilhos() {
		
		List<Setor> setoresFilhos = Setor.find("bySetorPai", this).fetch();
		
		return setoresFilhos;
	}
	
	public List<Integer> getIdsSetoresFilhos() {
		
		List<Setor> setoresFilhos = getSetoresFilhos();
		
		ArrayList<Integer> idsSetoresFilhos = new ArrayList<>();
		
		for (Setor setor : setoresFilhos) {
			
			idsSetoresFilhos.add(setor.id);
		}
		
		return idsSetoresFilhos;
	}	

}