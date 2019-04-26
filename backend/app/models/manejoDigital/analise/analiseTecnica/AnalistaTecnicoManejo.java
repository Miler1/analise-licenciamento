package models.manejoDigital.analise.analiseTecnica;

import models.portalSeguranca.UsuarioLicenciamento;
import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema="analise", name="analista_tecnico_manejo")
public class AnalistaTecnicoManejo extends GenericModel {

	public static final String SEQ = "analise.analista_tecnico_manejo_id_seq";

	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;

	@Required
	@OneToOne
	@JoinColumn(name="id_analise_tecnica_manejo")
	public AnaliseTecnicaManejo analiseTecnicaManejo;

	@Required
	@ManyToOne
	@JoinColumn(name="id_usuario")
	public UsuarioLicenciamento usuario;

	@Required
	@Column(name="data_vinculacao")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataVinculacao;

	public AnalistaTecnicoManejo(AnaliseTecnicaManejo analiseTecnica, UsuarioLicenciamento usuario) {

		this.analiseTecnicaManejo = analiseTecnica;
		this.usuario = usuario;
		this.dataVinculacao = new Date();
	}
}
