package models.licenciamento;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Filter;

import models.DiasAnalise;
import models.LicencaAnalise;
import models.Suspensao;
import models.licenciamento.StatusCaracterizacao;
import play.db.jpa.GenericModel;
import utils.Identificavel;

@Entity
@Table(schema = "licenciamento", name = "licenca")
public class Licenca extends GenericModel implements Identificavel {

	private static final String SEQ = "licenciamento.licenca_id_seq";
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_caracterizacao", referencedColumnName = "id", nullable = false)
	public Caracterizacao caracterizacao;
	
	@Column(name = "data_cadastro")
	public Date dataCadastro;
	
	@OneToOne
	@JoinColumn(name = "id_documento")
	public DocumentoLicenciamento documento;
	
	public String numero;
	
	@Column(name = "data_validade")
	public Date dataValidade;
	
	@ManyToOne
	@JoinColumn(name="id_licenca_analise")
	public LicencaAnalise licencaAnalise;
	
	@OneToOne
	@JoinColumn(name="id_licenca_anterior")
	public Licenca licencaAnterior;
	
	@Column(name = "data_validade_prorrogada")
	public Date dataValidadeProrrogada;
	
	@OneToOne(mappedBy="licenca")
	public Suspensao suspensao;
	
	public Boolean ativo;

	public Licenca(Caracterizacao caracterizacao) {
		
		this.caracterizacao = caracterizacao;
		
	}
	
	public void gerar(LicencaAnalise licencaAnalise) {
		
		this.dataCadastro = new Date();
		
		Calendar c = Calendar.getInstance();
		c.setTime(this.dataCadastro);
		
		c.add(Calendar.YEAR, licencaAnalise.validade);
		this.dataValidade = c.getTime();
		this.licencaAnalise = licencaAnalise;
		this.ativo = true;
		this.save();
		
		this.gerarNumero();
		
		this.save();
		
	}
	
	private void gerarNumero() {
		
		if (this.id == null)
			throw new IllegalStateException("Licença não salva.");
		
		this.numero = Calendar.getInstance().get(Calendar.YEAR) + "/" +
				String.format("%06d", this.id);
	}

	@Override
	public Long getId() {
		
		return this.id;
	}
	
	public boolean isSuspensa() {
		return this.caracterizacao.status.equals(StatusCaracterizacao.SUSPENSO);
	}
	
	public boolean isCancelada() {
		return this.caracterizacao.status.equals(StatusCaracterizacao.CANCELADO);
	}

	public LicencaAnalise getLicencaAnalise() {
		return this.licencaAnalise;
	}
}
