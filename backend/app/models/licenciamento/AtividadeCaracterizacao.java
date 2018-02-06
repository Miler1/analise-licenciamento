package models.licenciamento;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import models.licenciamento.AtividadeCnae;
import models.portalSeguranca.Setor;
import play.db.jpa.GenericModel;

@Entity
@Table(schema = "licenciamento", name = "atividade_caracterizacao")
public class AtividadeCaracterizacao extends GenericModel {

	private static final String SEQ = "licenciamento.atividade_caracterizacao_id_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;

	@ManyToOne
	@JoinColumn(name="id_atividade")
	public Atividade atividade;

	@ManyToMany
	@JoinTable(schema = "licenciamento", name = "rel_atividade_caracterizacao_cnae",
		joinColumns = @JoinColumn(name = "id_atividade_caracterizacao"),
		inverseJoinColumns = @JoinColumn(name = "id_atividade_cnae"))
	public List<AtividadeCnae> atividadesCnae;

	@OneToMany(mappedBy = "atividadeCaracterizacao", cascade = CascadeType.ALL)
	public List<GeometriaAtividade> geometriasAtividade;

	@ManyToOne
	@JoinColumn(name = "id_caracterizacao", referencedColumnName = "id")
	public Caracterizacao caracterizacao;

	@ManyToOne
	@JoinColumn(name="id_porte_empreendimento")
	public PorteEmpreendimento porteEmpreendimento;

	@Column(name = "valor_parametro")
	public Double valorParametro;
	
}
