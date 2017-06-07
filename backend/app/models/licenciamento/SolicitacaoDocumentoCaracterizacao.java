package models.licenciamento;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import models.Documento;
import models.TipoDocumento;
import play.db.jpa.GenericModel;

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
	public TipoDocumento tipoDocumento;
	
	public Boolean obrigatorio;
	
	@OneToOne
	@JoinColumn(name = "id_documento", referencedColumnName="id")
	public DocumentoLicenciamento documento;
	
	@ManyToOne
	@JoinColumn(name="id_caracterizacao", referencedColumnName="id")
	public Caracterizacao caracterizacao;
}
