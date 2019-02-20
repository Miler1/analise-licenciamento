package models.licenciamento;

import play.db.jpa.GenericModel;
import utils.Identificavel;
import utils.validacao.CustomValidation;
import utils.validacao.ICustomValidation;
import utils.validacao.Validacao;

import javax.persistence.*;


@Entity
@Table(schema = "licenciamento", name = "endereco")
@CustomValidation
public class Endereco extends GenericModel implements ICustomValidation, Identificavel {

	private static final String SEQ = "licenciamento.endereco_id_seq";
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;
	
	@Column(name="tipo_endereco")
	@Enumerated(EnumType.ORDINAL)
	public TipoLocalizacao tipo;
	
	public Integer cep;
	
	public String logradouro;

	public String numero;
	
	public String complemento;
	
	public Boolean correspondencia;
	
	public String bairro;
	
	@Column(name = "caixa_postal")
	public String caixaPostal;
	
	@Column(name = "roteiro_acesso")
	public String roteiroAcesso;
	
	@ManyToOne
	@JoinColumn(name="id_municipio", referencedColumnName="id_municipio")
	public Municipio municipio;
	

	@Override
	public Long getId() {
		
		return this.id;
	}
	
	public boolean isValid() {
		
		if (tipo == null)
			return false;
		
		if (tipo == TipoLocalizacao.ZONA_RURAL) {
			
			return !correspondencia && Validacao.checkRequired(municipio, roteiroAcesso);
			
		} else {
			
			return Validacao.checkRequired(cep, logradouro, municipio);
		}
	}
}
