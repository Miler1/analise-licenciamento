package models.licenciamento;

import models.DispensaLicenciamentoCancelada;
import models.LicencaCancelada;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = "licenciamento", name = "dispensa_licenciamento")
public class DispensaLicenciamento extends GenericModel {

	private static final String SEQ = "licenciamento.dispensa_licenciamento_id_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;

	@Column(name = "data_cadastro")
	public Date dataCadastro;

	@Column(name = "informacao_adicional")
	public String informacaoAdicional;

	@OneToOne
	@JoinColumn(name = "id_documento")
	public DocumentoLicenciamento documento;

	@ManyToOne
	@JoinColumn(name = "id_caracterizacao", referencedColumnName = "id", nullable = false)
	public Caracterizacao caracterizacao;
	
	public String numero;
	
	public Boolean ativo;

	@OneToOne(mappedBy = "dispensaLicenciamento")
	public DispensaLicenciamentoCancelada dispensaCancelada;
}
