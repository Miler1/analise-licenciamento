package models;

import exceptions.AppException;
import exceptions.PortalSegurancaException;
import br.ufla.lemaf.beans.pessoa.Endereco;
import br.ufla.lemaf.beans.pessoa.Municipio;
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

public class EmailNotificacaoComunicado extends EmailNotificacao {

    private AnaliseGeo analiseGeo;
    private ParecerAnalistaGeo parecerAnalistaGeo;
    private List<Documento> pdfsNotificacao;
    private Caracterizacao caracterizacao;
    private Comunicado comunicado;

    public EmailNotificacaoComunicado(AnaliseGeo analiseGeo, ParecerAnalistaGeo parecerAnalistaGeo, List<String> emailsDestinatarios, Caracterizacao caracterizacao, Comunicado comunicado) throws Exception {

        super(emailsDestinatarios);
        this.analiseGeo = analiseGeo;
        this.parecerAnalistaGeo = parecerAnalistaGeo;
        this.caracterizacao = caracterizacao;
        this.comunicado = comunicado;

    }

    @Override
    public void enviar() {

        try {

            List<String> tiposlicenca = new ArrayList<String>();
            tiposlicenca.add(this.analiseGeo.analise.processo.caracterizacao.tipoLicenca.nome);

            String licencas = StringUtils.join(tiposlicenca, ",");


            IntegracaoEntradaUnicaService integracaoEntradaUnica = new IntegracaoEntradaUnicaService();
            br.ufla.lemaf.beans.Empreendimento empreendimentoEU = integracaoEntradaUnica.findEmpreendimentosByCpfCnpj(this.analiseGeo.analise.processo.empreendimento.getCpfCnpj());

            final Endereco enderecoCompleto = empreendimentoEU.enderecos.stream().filter(endereco -> endereco.tipo.id.equals(TipoEndereco.ID_PRINCIPAL)).findAny().orElseThrow(PortalSegurancaException::new);

            if(!Emails.notificarInteressadoComunicado(this.emailsDestinatarios, licencas, this.analiseGeo, this.parecerAnalistaGeo, enderecoCompleto, this.caracterizacao, comunicado).get()) {

                throw new AppException();

            }

        } catch (InterruptedException | ExecutionException | AppException e) {

            ReenvioEmail reenvioEmail = new ReenvioEmail(this.analiseGeo.id, TipoEmail.NOTIFICACAO_COMUNICADO, e.getMessage(), this.emailsDestinatarios);
            reenvioEmail.save();

            e.printStackTrace();

        }
    }

}
