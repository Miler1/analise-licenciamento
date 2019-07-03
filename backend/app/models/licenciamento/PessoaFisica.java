package models.licenciamento;

import play.data.validation.CheckWith;
import play.data.validation.Required;
import utils.validacao.CpfCheck;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = "licenciamento", name = "pessoa_fisica")
@PrimaryKeyJoinColumn(name = "id_pessoa", referencedColumnName = "id")
public class PessoaFisica extends Pessoa {

	public enum Sexo { M, F }

	@Required
	public String nome;
	
	@Required
	@CheckWith(value = CpfCheck.class)
	public String cpf;
	
	@Required
	public String rg;
	
	@Column(name = "titulo_eleitor")
	public String tituloEleitor;
	
	@Required
	@Column(name = "nome_mae")
	public String nomeMae;
	
	@Required
	@Column(name = "data_nascimento")
	public Date dataNascimento;
	
	@Required
	@Enumerated(EnumType.STRING)
	public Sexo sexo;
	
	@Required
	@ManyToOne
	@JoinColumn(name="id_estado_civil", referencedColumnName="id")
	public EstadoCivil estadoCivil;

	@Column
	public String naturalidade;

}
