package models;

import exceptions.AppException;
import main.java.br.ufla.lemaf.beans.Empreendimento;
import main.java.br.ufla.lemaf.beans.pessoa.Endereco;
import main.java.br.ufla.lemaf.beans.pessoa.Municipio;
import main.java.br.ufla.lemaf.enums.TipoEndereco;
import models.licenciamento.Caracterizacao;
import models.licenciamento.TipoAnalise;
import notifiers.Emails;
import org.apache.commons.lang.StringUtils;
import services.IntegracaoEntradaUnicaService;

import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EmailParecerJuridico extends EmailJuridico {

    private AnaliseGeo analiseGeo;
    private ParecerJuridico parecerJuridico;
    private Documento pdfParecer;
    private Documento cartaImagem;


    public EmailParecerJuridico(AnaliseGeo analiseGeo, ParecerAnalistaGeo parecerAnalistaGeo, List<String> emailsDestinatarios, ParecerJuridico parecerJuridico) throws Exception {

        super(emailsDestinatarios);
//        this.pdfParecer = analiseGeo.gerarPDFParecer(parecerAnalistaGeo);
//        this.cartaImagem = analiseGeo.gerarPDFCartaImagem(parecerAnalistaGeo);
        this.analiseGeo = analiseGeo;
        this.parecerJuridico = parecerJuridico;

    }

    @Override
    public void enviar() {

        try {

            List<AnaliseDocumento> documentosInvalidados = new ArrayList<AnaliseDocumento>();
            for(AnaliseDocumento analiseDocumento : this.analiseGeo.analisesDocumentos) {

                if(analiseDocumento.documento.tipo.tipoAnalise.equals(TipoAnalise.GEO) && !analiseDocumento.validado) {
                    documentosInvalidados.add(analiseDocumento);
                }
            }

            IntegracaoEntradaUnicaService integracaoEntradaUnica = new IntegracaoEntradaUnicaService();
            main.java.br.ufla.lemaf.beans.Empreendimento empreendimentoEU = integracaoEntradaUnica.findEmpreendimentosByCpfCnpj(this.analiseGeo.analise.processo.empreendimento.getCpfCnpj());
            Municipio municipio = null;
            for(Endereco endereco : empreendimentoEU.enderecos){
                if(endereco.tipo.id == TipoEndereco.ID_PRINCIPAL){
                    municipio = endereco.municipio;
                }
            }

            if(!Emails.comunicarJuridicoAnalise(this.emailsDestinatarios, this.analiseGeo, municipio, this.parecerJuridico).get()) {

                throw new AppException();

            }

        } catch (InterruptedException | ExecutionException | AppException e) {

            ReenvioEmail reenvioEmail = new ReenvioEmail(this.analiseGeo.id, ReenvioEmail.TipoEmail.NOTIFICACAO_ANALISE_GEO, e.getMessage(), this.emailsDestinatarios);
            reenvioEmail.save();

            e.printStackTrace();

        }
    }

}
