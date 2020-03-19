package models.licenciamento;

import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(schema = "licenciamento", name = "tipo_sobreposicao")
public class TipoSobreposicao extends GenericModel {

	public static final long TERRA_INDIGENA = 1l;
	public static final long UC_FEDERAL = 2l;
	public static final long UC_ESTADUAL = 3l;
	public static final long UC_MUNICIPAL = 4l;
	public static final long TERRA_INDIGENA_ZA = 5l;
	public static final long TERRA_INDIGENA_ESTUDO = 6l;
	public static final long UC_FEDERAL_APA_DENTRO = 7l;
	public static final long UC_FEDERAL_APA_FORA = 8l;
	public static final long UC_FEDERAL_ZA = 9l;
	public static final long UC_ESTADUAL_PI_DENTRO = 10l;
	public static final long UC_ESTADUAL_PI_FORA = 11l;
	public static final long UC_ESTADUAL_ZA = 12l;
	public static final long TOMBAMENTO_ENCONTRO_AGUAS = 13l;
	public static final long TOMBAMENTO_ENCONTRO_AGUAS_ZA = 14l;
	public static final long AREAS_EMBARGADAS_IBAMA = 15l;
	public static final long AUTO_DE_INFRACAO_IBAMA = 16l;
	public static final long SAUIM_DE_COLEIRA = 17l;
	public static final long SITIOS_ARQUEOLOGICOS = 18l;
	public static final long UC_ESTADUAL_ZA_PI_FORA = 19l;
	public static final long BENS_ACAUTELADOS_IPHAN_PT = 20l;



	@Id
	public Long id;

	@Column
	public String codigo;

	@Column
	public String nome;

	@ManyToMany
	@JoinTable(schema = "licenciamento", name = "rel_tipo_sobreposicao_orgao",
			joinColumns = @JoinColumn(name = "id_tipo_sobreposicao"),
			inverseJoinColumns = @JoinColumn(name = "id_orgao"))
	public List<Orgao> orgaosResponsaveis;

}