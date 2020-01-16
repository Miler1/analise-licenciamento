package models;

import play.data.validation.Required;
import play.db.jpa.GenericModel;
import utils.Mensagem;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(schema="analise", name="restricao")
public class Restricao extends GenericModel {

	public static final String SEQ = "analise.restricao_id_seq";

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;

	@ManyToOne
	@JoinColumn(name="id_parecer_analista_tecnico")
	public ParecerAnalistaTecnico parecerAnalistaTecnico;

	@Required
	@Column(name = "texto")
	public String texto;

	public void update(Condicionante novaCondicionante) {

		this.texto = novaCondicionante.texto;

		this.save();

	}

	public Restricao salvar() {

		return this.save();

	}

	public String excluir() {

		this._delete();

		return Mensagem.RESTRICAO_EXCLUIDA_SUCESSO.getTexto();

	}

	public static List<Restricao> findByIdParecer(Long parecerAnalistaTecnicoId){

		List<Restricao> restricoes = Condicionante.find("id_parecer_analista_tecnico", parecerAnalistaTecnicoId).fetch();

		return restricoes;

	}

}
