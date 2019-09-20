package models.licenciamento;

import com.vividsolutions.jts.geom.Geometry;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import play.db.jpa.GenericModel;

@Entity
@Table(schema = "licenciamento", name = "sobreposicao_complexo")
public class SobreposicaoCaracterizacaoComplexo extends GenericModel {

	private static final String SEQ = "licenciamento.sobreposicao_complexo_id_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;

	@ManyToOne
	@JoinColumn(name = "id_tipo_sobreposicao", referencedColumnName="id")
	public TipoSobreposicao tipoSobreposicao;

	@ManyToOne
	@JoinColumn(name = "id_caracterizacao", referencedColumnName="id")
	public Caracterizacao caracterizacao;

	@Column(name = "geometria", columnDefinition="Geometry")
	public Geometry geometria;

	public SobreposicaoCaracterizacaoComplexo(TipoSobreposicao tipoSobreposicao, Caracterizacao caracterizacao, Geometry geometria) {
		this.tipoSobreposicao = tipoSobreposicao;
		this.caracterizacao = caracterizacao;
		this.geometria = geometria;
	}
}