package models.licenciamento;

import play.db.jpa.GenericModel;

import javax.persistence.*;

@Entity
@Table(schema = "licenciamento", name = "solicitacao_grupo_documento")
public class SolicitacaoGrupoDocumento extends GenericModel {

	private static final String SEQ = "licenciamento.solicitacao_grupo_documento_id_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;

	@ManyToOne
	@JoinColumn(name="id_tipo_documento", referencedColumnName="id")
	public TipoDocumentoLicenciamento tipoDocumento;

	public Boolean obrigatorio;

	@OneToOne
	@JoinColumn(name = "id_documento", referencedColumnName="id")
	public DocumentoLicenciamento documento;

	@ManyToOne
	@JoinColumn(name="id_caracterizacao", referencedColumnName="id")
	public Caracterizacao caracterizacao;

	public static SolicitacaoGrupoDocumento findByTipoAndCaracterizacao(TipoDocumentoLicenciamento tipo, Caracterizacao caracterizacao) {

		return SolicitacaoGrupoDocumento.find("byTipoDocumentoAndCaracterizacao", tipo, caracterizacao).first();

	}

}
