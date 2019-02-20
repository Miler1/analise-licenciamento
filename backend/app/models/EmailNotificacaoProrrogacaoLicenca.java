package models;

import exceptions.AppException;
import models.ReenvioEmail.TipoEmail;
import models.licenciamento.Licenca;
import notifiers.Emails;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class EmailNotificacaoProrrogacaoLicenca extends EmailNotificacao {

    private Licenca licenca;

    public EmailNotificacaoProrrogacaoLicenca(Licenca licenca, List<String> emailsDestinatarios) {

        super(emailsDestinatarios);
        this.licenca = licenca;
    }

    @Override
    public void enviar() {

        try {

            if(!Emails.notificarRequerenteProrrogacaoLicenca(this.emailsDestinatarios, this.licenca).get()) {

                throw new AppException();

            }

        } catch (InterruptedException | ExecutionException | AppException e) {

            ReenvioEmail reenvioEmail = new ReenvioEmail(this.licenca.id, TipoEmail.PRORROGACAO_LICENCA, e.getMessage(), this.emailsDestinatarios);
            reenvioEmail.save();

            e.printStackTrace();

        }
    }
}
