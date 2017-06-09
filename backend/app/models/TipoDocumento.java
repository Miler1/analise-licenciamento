package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
@Table(schema = "analise", name = "tipo_documento")
public class TipoDocumento extends Model {


	@Required
	public String nome;
	
	@Column(name="caminho_modelo")
	public String caminhoModelo;
	
	@Required
	@Column(name="caminho_pasta")
	public String caminhoPasta;
	
	@Required
	@Column(name="prefixo_nome_arquivo")
	public String prefixoNomeArquivo;
	
}
