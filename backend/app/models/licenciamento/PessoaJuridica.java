package models.licenciamento;

import play.data.validation.CheckWith;
import play.data.validation.Required;
import utils.validacao.CnpjCheck;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = "licenciamento", name = "pessoa_juridica")
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "id_pessoa")
public class PessoaJuridica extends Pessoa {

	@Required
	@CheckWith(value = CnpjCheck.class)
	public String cnpj;
	
	@Required
	@Column(name = "razao_social")
	public String razaoSocial;
	
	@Required
	@Column(name = "nome_fantasia")
	public String nomeFantasia;
	
	@Required
	@ManyToOne
	@JoinColumn(name="id_municipio", referencedColumnName="id_municipio")
	public Municipio municipio;	
	
	@Required
	@Column(name = "data_fundacao")
	public Date dataFundacao;
	
	@Column(name = "inscricao_estadual")
	public String inscricaoEstadual;
}
