package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import play.data.validation.CheckWith;
import play.data.validation.Required;
import utils.validacao.CnpjCheck;
import utils.validacao.Validacao;

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
	
	@Required
	public boolean isento;

	
	@Override
	protected void validar() {
		
		super.validar();
		
		if (!this.isento)
			Validacao.validateRequired(this.inscricaoEstadual);
	}
	
	@PrePersist
	@PreUpdate
	private void preSave() {
		
		if (this.isento)
			this.inscricaoEstadual = null;
	}

}
