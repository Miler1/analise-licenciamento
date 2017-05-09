package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(schema = "licenciamento", name = "tipo_documento")
public class TipoDocumento extends Model {

	public static Long DOCUMENTO_REPRESENTACAO = 1l;
	public static Long DISPENSA_LICENCIAMENTO = 2l;
	public static Long DOCUMENTO_ARRECADACAO_ESTADUAL = 3l;
	
	public static Long LICENCA_PREVIA = 66l;
	public static Long LICENCA_INSTALACAO = 67l;
	public static Long LICENCA_OPERACAO = 68l;
	public static Long LICENCA_AMBIENTAL_RURAL = 69l;
	
	public String nome;
	
	@Column(name="caminho_modelo")
	public String caminhoModelo;

	@Column(name="caminho_pasta")
	public String caminhoPasta;
	
	@Column(name="prefixo_nome_arquivo")
	public String prefixoNomeArquivo;
	
}
