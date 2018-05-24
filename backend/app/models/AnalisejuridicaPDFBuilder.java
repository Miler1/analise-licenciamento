package models;

import play.Play;
import play.classloading.enhancers.LocalvariablesNamesEnhancer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;

import static play.modules.pdf.PDF.writePDF;

public class AnalisejuridicaPDFBuilder implements Callable<InputStream>, LocalvariablesNamesEnhancer.LocalVariablesSupport {

    private AnaliseJuridica analiseJuridica;
    private String data;
    private String rota;

    public AnalisejuridicaPDFBuilder(AnaliseJuridica analiseJuridica) {

        Date data = new Date();
        SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy - HH:mm");

        this.data = formatador.format(data);
        this.analiseJuridica = analiseJuridica;

    }

    @Override
    public InputStream call() throws Exception {

        return createPDF(this.analiseJuridica, this.data);

    }

    private InputStream createPDF(AnaliseJuridica analiseJuridica, String data) throws IOException {

        ByteArrayOutputStream pdf = new ByteArrayOutputStream();

        writePDF(pdf, analiseJuridica, data);

        InputStream bis = new ByteArrayInputStream(pdf.toByteArray());

        return bis;
    }
}
