package models.licenciamento;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import models.Documento;
import play.db.jpa.GenericModel;

@Entity
@Table(schema = "licenciamento", name = "dae")
public class Dae extends GenericModel {

	public static enum Status { NAO_EMITIDO, EMITIDO, ERRO_EMISSAO, PAGO, VENCIDO }
	
	private static final String SEQ = "licenciamento.dae_id_seq";
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;

	@OneToOne
	@JoinColumn(name = "id_documento")
	public Documento documento;
	
	public Double valor;
	
	@OneToOne
	@JoinColumn(name = "id_caracterizacao", referencedColumnName = "id", nullable = false)
	public Caracterizacao caracterizacao;
	
	@Column(name = "codigo_receita")
	public Integer codigoReceita;
	
	public String numero;

	public Date competencia;
	
	@Column(name = "data_cadastro")
	public Date dataCadastro;
	
	@Column(name = "data_emissao")
	public Date dataEmissao;
	
	@Column(name = "data_vencimento")
	public Date dataVencimento;
	
	@Column(name = "cpf_cnpj_contribuinte")
	public String cpfCnpjContribuinte;

	@Enumerated(EnumType.ORDINAL)
	public Status status;
	
	@Column(name = "erro_emissao")
	public String erroEmissao;
}
