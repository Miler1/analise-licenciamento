package models.geocalculo;

import groovy.transform.Immutable;
import play.db.jpa.GenericModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema="analise", name="configuracao_layer")
@Immutable
public class ConfiguracaoLayer extends GenericModel {

	@Id
	@Column(name = "id")
	public Long id;

	@Column(name = "atributo_descricao")
	public String atributoDescricao;

	@Column(name = "nome_layer")
	public String nomeLayer;

	@Column(name = "descricao")
	public String descricao;

	@Column(name = "buffer")
	public Double buffer;

}
