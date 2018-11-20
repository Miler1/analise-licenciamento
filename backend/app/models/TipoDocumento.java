package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import models.pdf.PDFTemplate;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
@Table(schema = "analise", name = "tipo_documento")
public class TipoDocumento extends Model {

	public static Long DOCUMENTO_ANALISE_JURIDICA = 1l;
	public static Long DOCUMENTO_ANALISE_TECNICA = 2l;
	public static Long PARECER_ANALISE_JURIDICA = 3l;
	public static Long PARECER_ANALISE_TECNICA = 4l;
	public static Long NOTIFICACAO_ANALISE_JURIDICA = 5l;
	public static Long NOTIFICACAO_ANALISE_TECNICA = 6l;
	public static Long DOCUMENTO_ANALISE_MANEJO = 7l;
	public static Long SHAPE_PROPRIEDADE_MANEJO = 8l;
	public static Long SHAPE_AREA_MANEJO = 9l;
	public static Long SHAPE_MANEJO = 10l;

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

	public PDFTemplate getPdfTemplate() {

		return PDFTemplate.getByTipoDocumento(this.id);
	}
	
}
