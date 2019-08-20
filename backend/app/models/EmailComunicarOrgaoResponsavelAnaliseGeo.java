package models;

import exceptions.AppException;
import main.java.br.ufla.lemaf.beans.Empreendimento;
import main.java.br.ufla.lemaf.beans.pessoa.Endereco;
import main.java.br.ufla.lemaf.beans.pessoa.Municipio;
import main.java.br.ufla.lemaf.enums.TipoEndereco;
import models.licenciamento.Caracterizacao;
import models.licenciamento.TipoAnalise;
import notifiers.Emails;
import org.apache.commons.lang.StringUtils;
import services.IntegracaoEntradaUnicaService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EmailComunicarOrgaoResponsavelAnaliseGeo extends EmailComunicado {

    private AnaliseGeo analiseGeo;
    private Comunicado comunicado;

    public EmailComunicarOrgaoResponsavelAnaliseGeo(AnaliseGeo analiseGeo, Comunicado comunicado, List<String> emailsDestinatarios) {

        super(emailsDestinatarios);
        this.analiseGeo = analiseGeo;
        this.comunicado = comunicado;

    }

    @Override
    public void enviar() {

        try {

//            List<String> tiposlicenca = new ArrayList<String>();
//            for(Caracterizacao caracterizacao : this.analiseGeo.analise.processo.caracterizacoes) {
//
//                tiposlicenca.add(caracterizacao.tipoLicenca.nome);
//            }
//            String licencas = StringUtils.join(tiposlicenca, ",");
//
//            List<AnaliseDocumento> documentosInvalidados = new ArrayList<AnaliseDocumento>();
//            for(AnaliseDocumento analiseDocumento : this.analiseGeo.analisesDocumentos) {
//
//                if(analiseDocumento.documento.tipo.tipoAnalise.equals(TipoAnalise.GEO) && !analiseDocumento.validado) {
//                    documentosInvalidados.add(analiseDocumento);
//                }
//            }

            IntegracaoEntradaUnicaService integracaoEntradaUnica = new IntegracaoEntradaUnicaService();
            main.java.br.ufla.lemaf.beans.Empreendimento empreendimentoEU = integracaoEntradaUnica.findEmpreendimentosByCpfCnpj(this.analiseGeo.analise.processo.empreendimento.getCpfCnpj());
            Municipio municipio = null;
            for(Endereco endereco : empreendimentoEU.enderecos){
                if(endereco.tipo.id == TipoEndereco.ID_PRINCIPAL){
                    municipio = endereco.municipio;
                }
            }

            if(!Emails.comunicarOrgaoResponsavelAnaliseGeo(this.emailsDestinatarios, this.analiseGeo, this.comunicado, municipio).get()) {

                throw new AppException();

            }

        } catch (InterruptedException | ExecutionException | AppException e) {

            ReenvioEmail reenvioEmail = new ReenvioEmail(this.analiseGeo.id, ReenvioEmail.TipoEmail.NOTIFICACAO_ANALISE_GEO, e.getMessage(), this.emailsDestinatarios);
            reenvioEmail.save();

            e.printStackTrace();

        }
    }

}
