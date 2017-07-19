package models.portalSeguranca;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.GenericModel;

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
	
	@ManyToOne
	@JoinColumn(name = "id_perfil", nullable = true)
	public Perfil perfil;
	
	@Column(name="tipo_setor")
	@Enumerated(EnumType.ORDINAL)
	public TipoSetor tipoSetor;
	
	public List<Setor> getSetoresFilhos() {
		
		List<Setor> setoresFilhos = Setor.find("bySetorPai", this.id).fetch();
		
		return setoresFilhos;
	}

}