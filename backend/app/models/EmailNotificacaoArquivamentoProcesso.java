package models;

import exceptions.AppException;
import notifiers.Emails;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EmailNotificacaoArquivamentoProcesso extends EmailNotificacao {

	private Processo processo;
	private Date arquivamento;
	private List<Notificacao> notificacoes;
	private String siglaSetor;

	public EmailNotificacaoArquivamentoProcesso(Processo processo, List<String> emailsDestinatarios, Date arquivamento,
	                                            List<Notificacao> notificacoes, String siglaSetor) {

		super(emailsDestinatarios);
		this.processo = processo;
		this.arquivamento = arquivamento;
		this.notificacoes = notificacoes;
		this.siglaSetor = siglaSetor;
	}

	@Override
	public void enviar() {

		try {

			if(!Emails.notificarRequerenteArquivamentoProcesso(this.emailsDestinatarios, this.processo, this.arquivamento,
					this.notificacoes, this.siglaSetor).get()) {

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
