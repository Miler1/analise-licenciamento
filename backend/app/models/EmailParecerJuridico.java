package models;

import exceptions.AppException;
import br.ufla.lemaf.beans.Empreendimento;
import br.ufla.lemaf.beans.pessoa.Endereco;
import br.ufla.lemaf.beans.pessoa.Municipio;
import br.ufla.lemaf.enums.TipoEndereco;
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
    private ParecerAnalistaGeo parecerAnalistaGeo;
    private Documento pdfParecer;
    private Documento cartaImagem;


    public EmailParecerJuridico(AnaliseGeo analiseGeo, ParecerAnalistaGeo parecerAnalistaGeo, List<String> emailsDestinatarios, ParecerJuridico parecerJuridico) throws Exception {

        super(emailsDestinatarios);
        this.pdfParecer = parecerAnalistaGeo.documentoParecer;
        this.cartaImagem = parecerAnalistaGeo.cartaImagem;
        this.analiseGeo = analiseGeo;
        this.parecerJuridico = parecerJuridico;
        this.parecerAnalistaGeo = parecerAnalistaGeo;

    }

    @Override
    public void enviar() {

        try {

            IntegracaoEntradaUnicaService integracaoEntradaUnica = new IntegracaoEntradaUnicaService();
            br.ufla.lemaf.beans.Empreendimento empreendimentoEU = integracaoEntradaUnica.findEmpreendimentosByCpfCnpj(this.analiseGeo.analise.processo.empreendimento.getCpfCnpj());
            this.analiseGeo.analise.processo.empreendimento.empreendimentoEU = empreendimentoEU;

            Municipio municipio = empreendimentoEU.enderecos.stream().filter(e -> e.tipo.id == TipoEndereco.ID_PRINCIPAL).findFirst().orElse(null).municipio;

            if(!Emails.comunicarJuridicoAnalise(this.emailsDestinatarios, this.analiseGeo, municipio, this.parecerJuridico, this.parecerAnalistaGeo, this.pdfParecer.getFile(), this.cartaImagem.getFile()).get()) {

                throw new AppException();

            }

        } catch (InterruptedException | ExecutionException | AppException e) {

            ReenvioEmail reenvioEmail = new ReenvioEmail(this.parecerJuridico.id, ReenvioEmail.TipoEmail.COMUNICAR_JURIDICO, e.getMessage(), this.emailsDestinatarios);
            reenvioEmail.save();

            e.printStackTrace();

        }
    }

}
