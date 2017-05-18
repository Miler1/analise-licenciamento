package models.licenciamento;

import javax.persistence.CascadeType;
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
@Table(schema = "licenciamento", name = "tipo_licenca_caracterizacao_andamento")
public class TipoLicencaCaracterizacaoEmAndamento extends GenericModel {
	
	private static final String SEQ = "licenciamento.tipo_licenca_caracterizacao_andamento_id_seq";
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_tipo_licenca")
	public TipoLicenca tipoLicenca;
	
	@ManyToOne
	@JoinColumn(name = "id_caracterizacao", nullable=false)
	public Caracterizacao caracterizacao;
	
	@Column(name = "validade_em_anos")
	public Integer validadeEmAnos;

	
	public static TipoLicencaCaracterizacaoEmAndamento findByTipoLicencaCaracterizacao(TipoLicenca tipoLicenca, Caracterizacao caracterizacao) {
		return TipoLicencaCaracterizacaoEmAndamento.find("byTipoLicencaAndCaracterizacao", tipoLicenca, caracterizacao).first();
	}
	
}
