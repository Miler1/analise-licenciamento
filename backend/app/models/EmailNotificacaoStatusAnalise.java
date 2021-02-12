package models;

import exceptions.AppException;
import br.ufla.lemaf.beans.pessoa.Endereco;
import br.ufla.lemaf.beans.pessoa.Municipio;
import br.ufla.lemaf.enums.TipoEndereco;
import notifiers.Emails;
import services.IntegracaoEntradaUnicaService;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class EmailNotificacaoStatusAnalise extends EmailNotificacao {

    private Analise analise;
    private ParecerSecretario parecerSecretario;

    public EmailNotificacaoStatusAnalise(Analise analise, ParecerSecretario parecerSecretario, List<String> emailsDestinatarios) throws Exception {

        super(emailsDestinatarios);
        this.analise = analise;
        this.parecerSecretario = parecerSecretario;

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

            if(!Emails.notificarRequerenteStatusAnalise(this.emailsDestinatarios, analise, parecerSecretario).get()) {

                throw new AppException();

            }

        } catch (InterruptedException | ExecutionException | AppException e) {

            ReenvioEmail reenvioEmail = new ReenvioEmail(parecerSecretario.id, ReenvioEmail.TipoEmail.NOTIFICACAO_SECRETARIO, e.getMessage(), this.emailsDestinatarios);
            reenvioEmail.save();

            e.printStackTrace();

        }
    }

}
