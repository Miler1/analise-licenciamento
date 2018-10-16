package models.pdf;

import java.io.File;

import models.TipoDocumento;
import utils.Configuracoes;

public enum PDFTemplate {

    PARECER_ANALISE_JURIDICA (TipoDocumento.PARECER_ANALISE_JURIDICA),
    PARECER_ANALISE_TECNICA (TipoDocumento.PARECER_ANALISE_TECNICA),
    NOTIFICACAO_ANALISE_TECNICA (TipoDocumento.NOTIFICACAO_ANALISE_TECNICA),
    NOTIFICACAO_ANALISE_JURIDICA (TipoDocumento.NOTIFICACAO_ANALISE_JURIDICA),
    ANALISE_MANEJO (TipoDocumento.DOCUMENTO_ANALISE_MANEJO);

    private static final File TEMPLATES_FOLDER = new File(Configuracoes.PDF_TEMPLATES_FOLDER_PATH);
    private static final String LIBS_PATH = new File(TEMPLATES_FOLDER, "libs").getPath();
    private static final String BODY_TEMPLATE_NAME = "corpo.html";
    private static final String HEADER_TEMPLATE_NAME = "cabecalho.html";
    private static final String FOOTER_TEMPLATE_NAME = "rodape.html";

    private Long idTipoDocumento;
    private File folder;

    PDFTemplate() {

    }

    PDFTemplate(Long idTipoDocumento) {

        this.idTipoDocumento = idTipoDocumento;
        TipoDocumento tipoDocumento = TipoDocumento.findById(idTipoDocumento);

        this.folder = new File(Configuracoes.PDF_TEMPLATES_FOLDER_PATH, tipoDocumento.caminhoPasta);
    }

    public String getRootFolderPath() {

        return TEMPLATES_FOLDER.getPath();
    }

    public String getFolderPath() {

        return this.folder.getPath();
    }

    public String getFolderAbsolutePath() {

        return Configuracoes.PDF_TEMPLATES_FOLDER_ABSOLUTE + this.folder;
    }

    public String getBodyTemplatePath() {

        return getFilePathIfExists(BODY_TEMPLATE_NAME);
    }

    public String getHeaderTemplatePath() {

        return getFilePathIfExists(HEADER_TEMPLATE_NAME);
    }

    public String getFooterTemplatePath() {

        return getFilePathIfExists(FOOTER_TEMPLATE_NAME);
    }

    public String getLibsPath() {

        return LIBS_PATH;
    }

    private String getFilePathIfExists(String fileName) {

        File file = new File(Configuracoes.PDF_TEMPLATES_FOLDER_ABSOLUTE + this.folder, fileName);

        return file.exists() ? getFolderPath() + File.separator + fileName : null;
    }

    public static PDFTemplate getByTipoDocumento(Long idTipoDocumento) {

        for (PDFTemplate template : values()) {

            if (template.idTipoDocumento != null && template.idTipoDocumento.equals(idTipoDocumento))
                return template;
        }

        return null;
    }
}
