package models;

import models.licenciamento.*;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import utils.*;

import javax.persistence.*;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

@Entity
@Table(schema="analise", name="inconsistencia_tecnica_documento")
public class InconsistenciaTecnicaDocumento extends GenericModel{

	public static final String SEQ = "analise.inconsistencia_tecnica_documento_id_seq";

	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="id_inconsistencia_tecnica")
	public InconsistenciaTecnica inconsistenciaTecnica;

	@OneToMany
	@JoinColumn(name="id_documento_administrativo")
	public List<SolicitacaoDocumentoCaracterizacao> documentosAdministrativo;

	@ManyToMany
	@JoinColumn(name="id_documento_tecnicos")
	public List<SolicitacaoGrupoDocumento> documentosTecnicos;





}
