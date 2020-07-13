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

public class EmailNotificacaoStatusAnalise extends EmailNotificacao {

    private Analise analise;
    private ParecerPresidente parecerPresidente;

    public EmailNotificacaoStatusAnalise(Analise analise, ParecerPresidente parecerPresidente, List<String> emailsDestinatarios) throws Exception {

        super(emailsDestinatarios);
        this.analise = analise;
        this.parecerPresidente = parecerPresidente;

    }

    @Override
    public void enviar() {

        try {

            IntegracaoEntradaUnicaService integracaoEntradaUnica = new IntegracaoEntradaUnicaService();
            br.ufla.lemaf.beans.Empreendimento empreendimentoEU = integracaoEntradaUnica.findEmpreendimentosByCpfCnpj(analise.processo.empreendimento.getCpfCnpj());

            this.analise.processo.empreendimento.empreendimentoEU = empreendimentoEU;

            Municipio municipio = null;
            for(Endereco endereco : empreendimentoEU.enderecos){
                if(endereco.tipo.id == TipoEndereco.ID_PRINCIPAL){
                    municipio = endereco.municipio;
                }
            }

            if(!Emails.notificarRequerenteStatusAnalise(this.emailsDestinatarios, analise, parecerPresidente).get()) {

                throw new AppException();

            }

        } catch (InterruptedException | ExecutionException | AppException e) {

            ReenvioEmail reenvioEmail = new ReenvioEmail(parecerPresidente.id, ReenvioEmail.TipoEmail.NOTIFICACAO_PRESIDENTE, e.getMessage(), this.emailsDestinatarios);
            reenvioEmail.save();

            e.printStackTrace();

        }
    }

}
