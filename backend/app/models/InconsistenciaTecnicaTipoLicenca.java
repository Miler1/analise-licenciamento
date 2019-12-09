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
@Table(schema="analise", name="inconsistencia_tecnica_tipo_licenca")
public class InconsistenciaTecnicaTipoLicenca extends GenericModel{

	public static final String SEQ = "analise.inconsistencia_tecnica_tipo_licenca_id_seq";

	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;

	@OneToOne
	@JoinColumn(name="id_tipo_licenca")
	public TipoLicenca tipoLicenca;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="id_inconsistencia_tecnica")
	public InconsistenciaTecnica inconsistenciaTecnica;


	public InconsistenciaTecnicaTipoLicenca (TipoLicenca tipoLicenca, InconsistenciaTecnica inconsistenciaTecnica){

		this.tipoLicenca = tipoLicenca;
		this.inconsistenciaTecnica = inconsistenciaTecnica;

	}
}
