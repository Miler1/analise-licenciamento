package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import models.licenciamento.Licenca;
import models.portalSeguranca.Usuario;
import play.db.jpa.GenericModel;
import utils.Configuracoes;

@Entity
@Table(schema="analise", name="suspensao")
public class Suspensao extends GenericModel {
	
	public static final String SEQ = "analise.suspensao_id_seq";
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;	
	
	@OneToOne
	@JoinColumn(name="id_licenca")
	public Licenca licenca;
	
	@OneToOne
	@JoinColumn(name="id_usuario_suspensao")
	public Usuario usuario;
	
	@Column(name="quantidade_dias_suspensao")
	public Integer qtdeDiasSuspensao;
	
	@Column(name="data_suspensao")
	public Date dataSuspensao;
	
	@Column(name="data_validade_licenca")
	public Date dataValidadeLicenca;
	
	@Column(name="data_validade_prorrogada")
	public Date dataValidadeProrrogada;
	
	@Column(name="ativo")
	public Boolean ativo;
	
	
	public Suspensao() {
		
	}
	
	public static List<Suspensao> findAtivas() {
		return Suspensao.find("byAtivo", true).fetch();
	}
}