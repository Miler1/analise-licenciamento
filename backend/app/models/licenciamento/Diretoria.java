package models.licenciamento;

import play.db.jpa.GenericModel;

import javax.persistence.*;

@Entity
@Table(schema = "licenciamento", name = "diretoria")
public class Diretoria extends GenericModel {
	
	public static final String DGFLOR = "DGFLOR";
	public static final String DLA = "DLA";
	
	
	private static final String SEQ = "licenciamento.diretoria_id_seq";
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;	
	
	
	@Column(name = "nome", length = 200)
	public String nome;

	@Column(name = "sigla", length = 20)
	public String sigla;

}
