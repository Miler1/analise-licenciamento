package models.licenciamento;

import play.db.jpa.GenericModel;

import javax.persistence.*;

@Entity
@Table(schema = "licenciamento", name = "solicitacao_documento_caracterizacao")
public class SolicitacaoDocumentoCaracterizacao extends GenericModel {

	private static final String SEQ = "licenciamento.solicitacao_documento_caracterizacao_id_seq";

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
	
	public static SolicitacaoDocumentoCaracterizacao findByTipoAndCaracterizacao(TipoDocumentoLicenciamento tipo, Caracterizacao caracterizacao) {
		
		return SolicitacaoDocumentoCaracterizacao.find("byTipoDocumentoAndCaracterizacao", tipo, caracterizacao).first();
		
	}

	public static SolicitacaoDocumentoCaracterizacao findByIdTipoDocumentoAndCaracterizacao(Long idTipoDocumento, Caracterizacao caracterizacao) {

		return SolicitacaoDocumentoCaracterizacao.find("id_tipo_documento = :idTipoDocumento AND id_caracterizacao = :idCaracterizacao")
								.setParameter("idTipoDocumento", idTipoDocumento)
								.setParameter("idCaracterizacao", caracterizacao.id)
								.first();

	}
	
}
