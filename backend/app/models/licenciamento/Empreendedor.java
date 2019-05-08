package models.licenciamento;

import org.hibernate.annotations.Filter;
import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(schema = "licenciamento", name = "empreendedor")
@Filter(name="empreendedorAtivo")
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
