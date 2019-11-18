package models.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import org.allcolor.yahp.converter.IHtmlToPdfTransformer;
import org.allcolor.yahp.converter.IHtmlToPdfTransformer.PageSize;
import org.apache.commons.collections.FastHashMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import play.Play;
import play.exceptions.TemplateNotFoundException;
import play.exceptions.UnexpectedException;
import play.modules.pdf.PDF;
import play.modules.pdf.PDF.Options;
import play.templates.Template;
import play.templates.TemplateLoader;
import utils.Configuracoes;
import utils.FileManager;
import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Classe utilizada para gerar PDF atravém de template HTML.
 * Para gerar um PDF, é necessário já haver o template configurado.
 * No enum {@link PDFTemplate} são configurados os templates existentes,
 * que devem seguir estrutura abaixo. Considere como exemplo o template
 * PDFTemplate.LICENCA_PREVIA:
 *
 * 	/templates/pdf/licenca_previa	-> pasta raiz do template
 * 		- corpo.html 		-> template do corpo do PDF
 * 		- cabecalho.html	-> template do cabeçalho do PDF
 * 		- rodape.html		-> template do cabeçalho do PDF
 *
 * A pasta pode conter mais arquivos a serem utilizados, (css, imagens, etc.)
 * Além dos parâmetros informados utilizando o método "addParam", os seguintes
 * parâmetros padrão são disponibilizados para o template:
 *
 * - rootPath: "/templates/pdf" -> caminho raiz da estrutura de templates
 * - templatePath: "/templates/pdf/licenca_previa" -> caminho da pasta do template
 * - libsPath: ""/templates/pdf/libs" caminho da pasta libs dos templates
 *
 */
public class PDFGenerator {

    private static final Class<IHtmlToPdfTransformer> transformerClass;

    private static final IHtmlToPdfTransformer.PageSize DEFAULT_PAGE_SIZE =
            new IHtmlToPdfTransformer.PageSize(21.0D, 30.0D, 0.5D, 0.5D, 2.0D, 5.9D);

    private File file;
    private Map<String, Object> parameters = new HashMap<>();
    private PDFTemplate template;
    private String bodyTemplatePath;
    private String headerTemplatePath;
    private String footerTemplatePath;
    private IHtmlToPdfTransformer.PageSize pageSize = DEFAULT_PAGE_SIZE;

    static {

        try {

            transformerClass = (Class<IHtmlToPdfTransformer>)
                    Class.forName(IHtmlToPdfTransformer.DEFAULT_PDF_RENDERER);

        } catch (ClassNotFoundException e) {

            throw new RuntimeException(e);
        }
    }

    public PDFGenerator() {

    }

    public PDFGenerator(File file) {

        this.file = file;
    }

    public PDFGenerator addParam(String key, Object value) {

        this.parameters.put(key, value);
        return this;
    }

    public PDFGenerator setTemplate(PDFTemplate template) {

        this.bodyTemplatePath = template.getBodyTemplatePath();
        this.headerTemplatePath = template.getHeaderTemplatePath();
        this.footerTemplatePath = template.getFooterTemplatePath();
        this.template = template;

        return this;
    }

    public PDFGenerator setBodyTemplate(String path) {

        this.bodyTemplatePath = path;
        return this;
    }

    public PDFGenerator setHeaderTemplate(String path) {

        this.headerTemplatePath = path;
        return this;
    }

    public PDFGenerator setFooterTemplate(String path) {

        this.footerTemplatePath = path;
        return this;
    }

    public PDFGenerator setPageSize(
            double width,
            double height,
            double leftMargin,
            double rightMargin,
            double bottomMargin,
            double topMargin) {

        this.pageSize = new IHtmlToPdfTransformer.PageSize(
                width, height, leftMargin, rightMargin, bottomMargin, topMargin);

        return this;
    }

    public File getFile() {

        return this.file;
    }

    public void generate() {

        try {

            configureFile();
            configureDefaultParameters();

            OutputStream os = new FileOutputStream(this.file);
            writePDF(os);
            os.flush();
            os.close();

        } catch (IOException e) {

            throw new UnexpectedException(e);
        }
    }


    private void configureFile() throws IOException {

        if (this.file != null)
            return;

        String folderName = FileManager.getInstance().getFolderName();
        String fileName = FileManager.getInstance().generateFileName("pdf");

        this.file = new File(Configuracoes.APPLICATION_TEMP_FOLDER + folderName, fileName);
    }


    private void configureDefaultParameters() {

        if (this.template != null) {

            this.addParam("rootPath", this.template.getRootFolderPath());
            this.addParam("templatePath", this.template.getFolderAbsolutePath());
            this.addParam("libsPath", this.template.getFolderPath());
        }
    }

    private PDF.Options configurePdfOptions() {

        PDF.Options options = new PDF.Options();

        Map<String, Object> args = new FastHashMap(2);
        args.putAll(this.parameters);

        options.filename = this.file.getName();
        options.pageSize = this.pageSize;

        if (this.headerTemplatePath != null) {

            Template cabecalho = TemplateLoader.load(this.headerTemplatePath);
            options.HEADER = cabecalho.render(args);
        }

        if (this.footerTemplatePath != null) {

            Template rodape = TemplateLoader.load(this.footerTemplatePath);
            options.FOOTER = rodape.render(args);
        }

        return options;
    }

    private void writePDF(OutputStream out) {

        PDFDocument doc = new PDFDocument();
        doc.options = configurePdfOptions();
        doc.template = this.bodyTemplatePath;

        Template template = TemplateLoader.load(doc.template);
        doc.args.putAll(this.parameters);
        doc.content = template.render(new HashMap<String, Object>(doc.args));
        loadHeaderAndFooter(doc, doc.args);

        renderPDF(out, doc);
    }


    private void loadHeaderAndFooter(PDFDocument doc, Map<String, Object> args) throws TemplateNotFoundException {

        Options options = doc.options;

        if (options == null)
            return;

        if (!StringUtils.isEmpty(options.HEADER_TEMPLATE)) {

            Template template = TemplateLoader.load(options.HEADER_TEMPLATE);
            options.HEADER = template.render(new HashMap<String, Object>(args));
        }

        if (!StringUtils.isEmpty(options.FOOTER_TEMPLATE)) {

            Template template = TemplateLoader.load(options.FOOTER_TEMPLATE);
            options.FOOTER = template.render(new HashMap<String, Object>(args));
        }

        loadHeaderAndFooter(doc, options.HEADER, IHtmlToPdfTransformer.CHeaderFooter.HEADER);
        loadHeaderAndFooter(doc, options.ALL_PAGES, IHtmlToPdfTransformer.CHeaderFooter.ALL_PAGES);
        loadHeaderAndFooter(doc, options.EVEN_PAGES, IHtmlToPdfTransformer.CHeaderFooter.EVEN_PAGES);
        loadHeaderAndFooter(doc, options.FOOTER, IHtmlToPdfTransformer.CHeaderFooter.FOOTER);
        loadHeaderAndFooter(doc, options.ODD_PAGES, IHtmlToPdfTransformer.CHeaderFooter.ODD_PAGES);
    }

    private void loadHeaderAndFooter(PDFDocument doc, String optionHeaderFooter, String cHeaderFooter) {

        if (!StringUtils.isEmpty(optionHeaderFooter))
            doc.headerFooterList.add(new IHtmlToPdfTransformer.CHeaderFooter(optionHeaderFooter, cHeaderFooter));
    }

    private void renderPDF(OutputStream out, PDFDocument doc) {

        try {
            Map<?,?> properties = Play.configuration;

            PageSize pageSize = doc.options != null ? doc.options.pageSize : IHtmlToPdfTransformer.A4P;

            transformerClass.newInstance().transform(
                    new ByteArrayInputStream(doc.content.getBytes("UTF-8")),
                    null, pageSize, doc.headerFooterList, properties, out);

        } catch(Exception e) {

            throw new RuntimeException(e);
        }
    }


    public static class PDFDocument {

        public String template;
        public Options options;
        public Map<String, Object> args = new HashMap<String, Object>();
        List<IHtmlToPdfTransformer.CHeaderFooter> headerFooterList = new LinkedList<IHtmlToPdfTransformer.CHeaderFooter>();
        String content;
    }

    public static File mergePDF(List<File> filesToMerge) throws DocumentException, IOException {

        byte[] mergedDocument;

        try (ByteArrayOutputStream memoryStream = new ByteArrayOutputStream()) {

            Document document = new Document();
            PdfSmartCopy pdfSmartCopy = new PdfSmartCopy(document, memoryStream);
            document.open();

            for (File docPath : filesToMerge) {

                PdfReader reader = new PdfReader(docPath.toString());

                try {

                    reader.consolidateNamedDestinations();

                    int numberOfPages = reader.getNumberOfPages();

                    for (int page = 0; page < numberOfPages;) {

                        PdfImportedPage pdfImportedPage = pdfSmartCopy.getImportedPage(reader, ++page);
                        pdfSmartCopy.addPage(pdfImportedPage);

                    }

                } finally {
                    reader.close();
                }

            }

            document.close();
            mergedDocument = memoryStream.toByteArray();

        }

        File documentosMergeados = new File("documento-parecer.pdf");
        FileUtils.writeByteArrayToFile(documentosMergeados, mergedDocument);

        return documentosMergeados;

    }

}
