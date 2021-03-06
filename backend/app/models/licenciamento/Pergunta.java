package models.licenciamento;

import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(schema = "licenciamento", name = "pergunta")
public class Pergunta extends GenericModel {

	@Id
	public Long id;

	public String texto;

	public String codigo;

	public Integer ordem;

	@OneToMany(mappedBy="pergunta")
	public List<Resposta> respostas;

	@Column(name="tipo_localizacao_empreendimento")
	@Enumerated(EnumType.ORDINAL)
	public TipoLocalizacao localizacaoEmpreendimento;
	
	@Column(name="tipo_pergunta")
	@Enumerated(EnumType.STRING)
	public TipoPergunta tipoPergunta;

}
