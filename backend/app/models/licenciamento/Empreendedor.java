package models.licenciamento;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Filter;

import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.ParamDef;
import play.data.validation.Required;
import play.db.jpa.GenericModel;

@Entity
@Table(schema = "licenciamento", name = "empreendedor")
@FilterDefs(value = {
		@FilterDef( name = "empreendedorAtivo", parameters = @ParamDef(name = "ativo", type = "boolean"), defaultCondition = "ativo = TRUE" ),
})
public class Empreendedor extends GenericModel {

	private static final String SEQ = "licenciamento.empreendedor_id_seq";
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;
	
	@Required
	@OneToOne
	@JoinColumn(name = "id_pessoa", referencedColumnName = "id")
	public Pessoa pessoa;
	
	@OneToMany(cascade = CascadeType.ALL, targetEntity = RepresentanteLegal.class, orphanRemoval = true)
	@JoinColumn(name = "id_empreendedor", referencedColumnName = "id", nullable = false)
	public List<RepresentanteLegal> representantesLegais;

	@Column(name="tipo_esfera")
	@Enumerated(EnumType.ORDINAL)
	public Esfera esfera;
	
	@Column(name = "data_cadastro")
	public Date dataCadastro;
	
	public boolean ativo;
}
