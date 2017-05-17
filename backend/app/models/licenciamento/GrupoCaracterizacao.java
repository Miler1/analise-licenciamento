package models.licenciamento;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import play.db.jpa.GenericModel;
import utils.Configuracoes;

@Entity
@Table(schema = "licenciamento", name = "grupo_caracterizacao")
public class GrupoCaracterizacao extends GenericModel {

	private static final String SEQ = "licenciamento.grupo_caracterizacao_id_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;

}
