package models;

import exceptions.AppException;
import br.ufla.lemaf.beans.pessoa.Endereco;
import br.ufla.lemaf.beans.pessoa.Municipio;
import br.ufla.lemaf.enums.TipoEndereco;
import models.licenciamento.Caracterizacao;
import notifiers.Emails;
import services.IntegracaoEntradaUnicaService;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class EmailNotificacaoStatusDispensa extends EmailNotificacao {

    private Caracterizacao caracterizacao;

    public EmailNotificacaoStatusDispensa(Caracterizacao caracterizacao, List<String> emailsDestinatarios) throws Exception {

        super(emailsDestinatarios);
        this.caracterizacao = caracterizacao;

    }

    @Override
    public void enviar() {

        try {

            IntegracaoEntradaUnicaService integracaoEntradaUnica = new IntegracaoEntradaUnicaService();
            br.ufla.lemaf.beans.Empreendimento empreendimentoEU = integracaoEntradaUnica.findEmpreendimentosByCpfCnpj(caracterizacao.empreendimento.getCpfCnpj());
            Municipio municipio = null;
            for(Endereco endereco : empreendimentoEU.enderecos){
                if(endereco.tipo.id == TipoEndereco.ID_PRINCIPAL){
                    municipio = endereco.municipio;
                }
            }

            if(!Emails.notificarRequerenteStatusDispensa(this.emailsDestinatarios, this.caracterizacao).get()) {

                throw new AppException();

            }

        } catch (InterruptedException | ExecutionException | AppException e) {

            ReenvioEmail reenvioEmail = new ReenvioEmail(this.caracterizacao.id, ReenvioEmail.TipoEmail.NOTIFICACAO_SECRETARIO_DISPENSA, e.getMessage(), this.emailsDestinatarios);
            reenvioEmail.save();

            e.printStackTrace();

        }
    }

}
