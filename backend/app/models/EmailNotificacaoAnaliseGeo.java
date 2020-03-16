package models;

import exceptions.AppException;
import exceptions.PortalSegurancaException;
import main.java.br.ufla.lemaf.beans.pessoa.Endereco;
import main.java.br.ufla.lemaf.beans.pessoa.Municipio;
import main.java.br.ufla.lemaf.enums.TipoEndereco;
import models.ReenvioEmail.TipoEmail;
import models.licenciamento.Caracterizacao;
import models.licenciamento.TipoAnalise;
import notifiers.Emails;
import org.apache.commons.lang.StringUtils;
import services.IntegracaoEntradaUnicaService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EmailNotificacaoAnaliseGeo extends EmailNotificacao {

    private AnaliseGeo analiseGeo;
    private ParecerAnalistaGeo parecerAnalistaGeo;
    private List<Documento> pdfsNotificacao;

    public EmailNotificacaoAnaliseGeo(AnaliseGeo analiseGeo, ParecerAnalistaGeo parecerAnalistaGeo, List<String> emailsDestinatarios) throws Exception {

        super(emailsDestinatarios);
        this.analiseGeo = analiseGeo;
        this.parecerAnalistaGeo = parecerAnalistaGeo;
        this.pdfsNotificacao = analiseGeo.gerarPDFNotificacao(analiseGeo);

    }

    public List<Documento> getPdfsNotificacao() {

        return pdfsNotificacao;

    }

    @Override
    public void enviar() {

        try {

            List<String> tiposlicenca = new ArrayList<String>();
            tiposlicenca.add(this.analiseGeo.analise.processo.caracterizacao.tipoLicenca.nome);

            String licencas = StringUtils.join(tiposlicenca, ",");

//            List<AnaliseDocumento> documentosInvalidados = new ArrayList<AnaliseDocumento>();
//            for(AnaliseDocumento analiseDocumento : this.analiseGeo.analisesDocumentos) {
//
//                if(analiseDocumento.documento.tipo.tipoAnalise.equals(TipoAnalise.GEO) && !analiseDocumento.validado) {
//                    documentosInvalidados.add(analiseDocumento);
//                }
//            }

            IntegracaoEntradaUnicaService integracaoEntradaUnica = new IntegracaoEntradaUnicaService();
            main.java.br.ufla.lemaf.beans.Empreendimento empreendimentoEU = integracaoEntradaUnica.findEmpreendimentosByCpfCnpj(this.analiseGeo.analise.processo.empreendimento.getCpfCnpj());

            final Endereco enderecoCompleto = empreendimentoEU.enderecos.stream().filter(endereco -> endereco.tipo.id.equals(TipoEndereco.ID_PRINCIPAL)).findAny().orElseThrow(PortalSegurancaException::new);



            if(!Emails.notificarRequerenteAnaliseGeo(this.emailsDestinatarios, licencas, this.analiseGeo, this.parecerAnalistaGeo, enderecoCompleto, this.pdfsNotificacao).get()) {

                throw new AppException();

            }

        } catch (InterruptedException | ExecutionException | AppException e) {

            ReenvioEmail reenvioEmail = new ReenvioEmail(this.parecerAnalistaGeo.id, TipoEmail.NOTIFICACAO_ANALISE_GEO, e.getMessage(), this.emailsDestinatarios);
            reenvioEmail.save();

            e.printStackTrace();

        }
    }

}
