package models;

import exceptions.AppException;
import exceptions.PortalSegurancaException;
import br.ufla.lemaf.beans.pessoa.Endereco;
import br.ufla.lemaf.enums.TipoEndereco;
import models.ReenvioEmail.TipoEmail;
import models.licenciamento.Caracterizacao;
import models.licenciamento.TipoAnalise;
import notifiers.Emails;
import org.apache.commons.lang.StringUtils;
import services.IntegracaoEntradaUnicaService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EmailNotificacaoAnaliseTecnica extends EmailNotificacao {
	
	private AnaliseTecnica analiseTecnica;
	private ParecerAnalistaTecnico parecerAnalistaTecnico;
	private List<Documento> pdfsNotificacao;
	
	public EmailNotificacaoAnaliseTecnica(AnaliseTecnica analiseTecnica, ParecerAnalistaTecnico parecerAnalistaTecnico, List<String> emailsDestinatarios) throws Exception {

		super(emailsDestinatarios);
		this.analiseTecnica = analiseTecnica;
		this.parecerAnalistaTecnico = parecerAnalistaTecnico;

		this.pdfsNotificacao = analiseTecnica.gerarPDFNotificacao(analiseTecnica);
				
	}

	public List<Documento> getPdfsNotificacao() {

		return pdfsNotificacao;

	}

	@Override
	public void enviar() {
		
		try {

			List<String> tiposlicenca = new ArrayList<String>();
			tiposlicenca.add(this.analiseTecnica.analise.processo.caracterizacao.tipoLicenca.nome);

			String licencas = StringUtils.join(tiposlicenca, ",");

			IntegracaoEntradaUnicaService integracaoEntradaUnica = new IntegracaoEntradaUnicaService();
			br.ufla.lemaf.beans.Empreendimento empreendimentoEU = integracaoEntradaUnica.findEmpreendimentosByCpfCnpj(this.analiseTecnica.analise.processo.empreendimento.getCpfCnpj());

			final Endereco enderecoCompleto = empreendimentoEU.enderecos.stream().filter(endereco -> endereco.tipo.id.equals(TipoEndereco.ID_PRINCIPAL)).findAny().orElseThrow(PortalSegurancaException::new);

			if(!Emails.notificarRequerenteAnaliseTecnica(this.emailsDestinatarios, licencas, this.analiseTecnica, this.parecerAnalistaTecnico, enderecoCompleto, this.pdfsNotificacao).get()) {

				throw new AppException();

			}

		} catch (InterruptedException | ExecutionException | AppException e) {

			ReenvioEmail reenvioEmail = new ReenvioEmail(this.analiseTecnica.id, TipoEmail.NOTIFICACAO_ANALISE_TECNICA, e.getMessage(), this.emailsDestinatarios);
			reenvioEmail.save();

			e.printStackTrace();

		}
	}

}
