package models.licenciamento;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.db.jpa.GenericModel;

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
