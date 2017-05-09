package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import play.data.validation.CheckWith;
import play.data.validation.Required;
import utils.validacao.CpfCheck;

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
	
	@Required
	@ManyToOne
	@JoinColumn(name="id_municipio_nascimento", referencedColumnName="id_municipio")
	public Municipio municipioNascimento;
	
	public boolean isMesmaPessoa(PessoaFisica pessoa) {
		
		return (this.id != null && pessoa.id != null && this.id.equals(pessoa.id)) ||
				(this.cpf != null && pessoa.cpf != null && this.cpf.equals(pessoa.cpf));
	}
	
	public PessoaFisica() {
		
	}
	
	public PessoaFisica(Long id) {
		
		this.id = id;
	}
}
