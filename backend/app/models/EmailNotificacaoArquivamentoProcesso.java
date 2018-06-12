package models;

import exceptions.AppException;
import notifiers.Emails;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class EmailNotificacaoArquivamentoProcesso extends EmailNotificacao {

	private Processo processo;

	public EmailNotificacaoArquivamentoProcesso(Processo processo, List<String> emailsDestinatarios) {

		super(emailsDestinatarios);
		this.processo = processo;
	}

	@Override
	public void enviar() {

		try {

			if(!Emails.notificarRequerenteArquivamentoProcesso(this.emailsDestinatarios, this.processo).get()) {

				throw new AppException();

			}

		} catch (InterruptedException | ExecutionException | AppException e) {

			ReenvioEmail reenvioEmail = new ReenvioEmail(this.processo.id, ReenvioEmail.TipoEmail.ARQUIVAMENTO_PROCESSO,
					e.getMessage(), this.emailsDestinatarios);
			reenvioEmail.save();

			e.printStackTrace();
		}
	}
}
