package models;

import exceptions.AppException;
import models.ReenvioEmail.TipoEmail;
import models.licenciamento.Caracterizacao;
import models.licenciamento.TipoAnalise;
import notifiers.Emails;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EmailNotificacaoAnaliseGeo extends EmailNotificacao {

    private AnaliseGeo analiseGeo;

    public EmailNotificacaoAnaliseGeo(AnaliseGeo analiseGeo, List<String> emailsDestinatarios) {

        super(emailsDestinatarios);
        this.analiseGeo = analiseGeo;

    }

    @Override
    public void enviar() {

        try {

            List<String> tiposlicenca = new ArrayList<String>();
            for(Caracterizacao caracterizacao : this.analiseGeo.analise.processo.caracterizacoes) {

                tiposlicenca.add(caracterizacao.tipoLicenca.nome);
            }
            String licencas = StringUtils.join(tiposlicenca, ",");

            List<AnaliseDocumento> documentosInvalidados = new ArrayList<AnaliseDocumento>();
            for(AnaliseDocumento analiseDocumento : this.analiseGeo.analisesDocumentos) {

                if(analiseDocumento.documento.tipo.tipoAnalise.equals(TipoAnalise.GEO) && !analiseDocumento.validado) {
                    documentosInvalidados.add(analiseDocumento);
                }
            }

            Notificacao notificacao = Notificacao.find("id_analise_geo", this.analiseGeo.id).first();

            if(!Emails.notificarRequerenteAnaliseGeo(this.emailsDestinatarios, licencas, documentosInvalidados, this.analiseGeo, notificacao).get()) {

                throw new AppException();

            }

        } catch (InterruptedException | ExecutionException | AppException e) {

            ReenvioEmail reenvioEmail = new ReenvioEmail(this.analiseGeo.id, TipoEmail.NOTIFICACAO_ANALISE_GEO, e.getMessage(), this.emailsDestinatarios);
            reenvioEmail.save();

            e.printStackTrace();

        }
    }

}
