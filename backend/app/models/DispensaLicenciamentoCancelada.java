package models;

import br.ufla.lemaf.beans.pessoa.Contato;
import exceptions.AppException;
import models.licenciamento.*;
import models.tramitacao.AcaoTramitacao;
import play.Logger;
import play.db.jpa.GenericModel;
import security.cadastrounificado.CadastroUnificadoWS;
import utils.ListUtil;
import utils.Mensagem;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(schema="analise", name="dispensa_licenciamento_cancelada")
public class DispensaLicenciamentoCancelada extends GenericModel{

    public static final String SEQ = "analise.licenca_cancelada_id_seq";

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
    @SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
    public Long id;

    @OneToOne
    @JoinColumn(name="id_dispensa_licenciamento")
    public DispensaLicenciamento dispensaLicenciamento;

    @ManyToOne
    @JoinColumn(name="id_usuario_executor")
    public UsuarioAnalise usuario;

    @Column(name="data_cancelamento")
    public Date dataCancelada;

    @Column(name="justificativa")
    public String justificativa;


    public DispensaLicenciamentoCancelada() {

    }

    public void cancelarDispensa(UsuarioAnalise usuarioExecutor) {

        Calendar c = Calendar.getInstance();
        Date dataAtual = c.getTime();

        DispensaLicenciamento dispensaLicenciamento = DispensaLicenciamento.findById(this.dispensaLicenciamento.id);
        dispensaLicenciamento.ativo = false;

        this.dispensaLicenciamento = dispensaLicenciamento;
        this.usuario = usuarioExecutor;
        this.dataCancelada = dataAtual;

        try {

            dispensaLicenciamento.save();
            this.save();

            Caracterizacao.setStatusCaracterizacao(ListUtil.createList(this.dispensaLicenciamento.caracterizacao.id), StatusCaracterizacao.CANCELADO);

            this.dispensaLicenciamento.caracterizacao.status = StatusCaracterizacao.findById(StatusCaracterizacaoEnum.CANCELADO.id);

            enviarEmailStatusDispensa(this.dispensaLicenciamento.caracterizacao);

        } catch (Exception e) {

            Logger.error(e, e.getMessage());
            throw new AppException(Mensagem.ERRO_ENVIAR_EMAIL, e.getMessage());

        }

    }

    public void enviarEmailStatusDispensa(Caracterizacao caracterizacao) throws Exception {

        Empreendimento empreendimento = Empreendimento.findById(caracterizacao.empreendimento.id);

        Contato emailCadastrante =  CadastroUnificadoWS.ws.getPessoa(empreendimento.cpfCnpjCadastrante).contatos.stream()
                .filter(contato -> contato.principal == true && contato.tipo.descricao.equals("Email")).findFirst().orElseThrow(null);

        List<String> interessados = new ArrayList<>(Collections.singleton(emailCadastrante.valor));

        EmailNotificacaoStatusDispensa emailNotificacaoStatusDispensa = new EmailNotificacaoStatusDispensa(caracterizacao, interessados);
        emailNotificacaoStatusDispensa.enviar();

    }



}
