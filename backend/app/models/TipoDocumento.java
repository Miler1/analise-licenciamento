package models;

import models.pdf.PDFTemplate;
import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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
	public static Long AREA_DE_MANEJO_FLORESTAL_SOLICITADA = 8l;
	public static Long AREA_DA_PROPRIEDADE = 9l;
	public static Long AREA_SEM_POTENCIAL = 10l;
	public static Long DOCUMENTO_COMPLEMENTAR_MANEJO = 12l;
	public static Long TERMO_DELIMITACAO_AREA_RESERVA_LEGAL_APROVADA = 13l;
	public static Long TERMO_AJUSTAMENTO_CONDUTA = 14l;
	public static Long ANEXO_PROCESSO_MANEJO_DIGITAL = 15l;
	public static Long PARECER_ANALISE_GEO = 16l;
	public static Long NOTIFICACAO_ANALISE_GEO = 17l;
	public static Long DOCUMENTO_INCONSISTENCIA = 18l;
	public static Long CARTA_IMAGEM = 19l;
	public static Long DOCUMENTO_COMUNICADO = 20l;
	public static Long DOCUMENTO_OFICIO_ORGAO = 21l;
	public static Long DOCUMENTO_NOTIFICACAO_ANALISE_GEO = 22l;
	public static Long DOCUMENTO_ANALISE_TEMPORAL = 23l;
	public static Long DOCUMENTO_INCONSISTENCIA_TECNICA = 26l;
	public static Long DOCUMENTO_RIT = 24L;
	public static Long DOCUMENTO_VISTORIA = 25L;
	public static Long INCONSISTENCIA_VISTORIA = 27L;
	public static Long AUTO_INFRACAO = 29L;
	public static Long DOCUMENTO_RELATORIO_TECNICO_VISTORIA = 30L;

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
