package models.licenciamento;

import com.vividsolutions.jts.geom.Geometry;
import models.InconsistenciaTecnicaQuestionario;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

	@OneToMany(mappedBy = "atividadeCaracterizacao", cascade = CascadeType.ALL)
	public List<AtividadeCaracterizacaoParametros> atividadeCaracterizacaoParametros;

	@OneToMany(mappedBy = "atividadeCaracterizacao", cascade = CascadeType.ALL)
	public List<SobreposicaoCaracterizacaoAtividade> sobreposicoesCaracterizacaoAtividade;

	public String getNomeAtividade(){
		return this.atividade != null ? this.atividade.nome : "-";
	}

	public static AtividadeCaracterizacao getAtividadeCaracterizacaoWithMaiorPotencialPoluidor(List<AtividadeCaracterizacao> atividadesCaracterizacao) {
		
		PotencialPoluidor potencialPoluidor = atividadesCaracterizacao.get(0).atividade.potencialPoluidor;
		AtividadeCaracterizacao maiorAC = atividadesCaracterizacao.get(0);
		
		for(AtividadeCaracterizacao atividadeCaracterizacao : atividadesCaracterizacao) {
			
			if(atividadeCaracterizacao.atividade.potencialPoluidor.compareTo(potencialPoluidor) == 1) {
				potencialPoluidor = atividadeCaracterizacao.atividade.potencialPoluidor;
				maiorAC = atividadeCaracterizacao;
			}
			
		}
		
		return maiorAC;
	}

	public Boolean isAtividadeDentroEmpreendimento() {
		return this.atividade.dentroEmpreendimento;
	}

	public String getAreaDeclaradaInteressado() {

		AtividadeCaracterizacaoParametros atividadeCaracterizacaoParametro =  this.atividadeCaracterizacaoParametros.stream().filter(valor -> valor.parametroAtividade.codigo.equals("AU")).findAny().orElse(null);

		return (atividadeCaracterizacaoParametro != null) ? atividadeCaracterizacaoParametro.valorParametro.toString() : "-";

	}
	
	public Stream<Geometry> getGeoms(){
		return this.geometriasAtividade.stream().map(ga -> ga.geometria);
	}
	
}
