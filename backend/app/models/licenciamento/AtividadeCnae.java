package models.licenciamento;

import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(schema = "licenciamento", name = "atividade_cnae")
public class AtividadeCnae extends GenericModel {

	@Id
	public Long id;

	public String nome;

	public String codigo;
	
	@OneToMany(mappedBy="atividadeCnae" ,fetch=FetchType.LAZY)
	public List<TipoCaracterizacaoAtividade> tiposCaracterizacaoAtividades;
	
}
