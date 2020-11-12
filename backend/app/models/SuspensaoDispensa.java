package models;

import br.ufla.lemaf.beans.pessoa.Contato;
import exceptions.AppException;
import exceptions.ValidacaoException;
import models.licenciamento.*;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;
import play.Logger;
import play.db.jpa.GenericModel;
import security.cadastrounificado.CadastroUnificadoWS;
import utils.DateUtil;
import utils.ListUtil;
import utils.Mensagem;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(schema="analise", name="dispensa_licenciamento_suspensa")
public class SuspensaoDispensa extends GenericModel {

    public static final String SEQ = "analise.suspensao_id_seq";

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

    @Column(name="quantidade_dias_suspensao")
    public Integer qtdeDiasSuspensao;

    @Column(name="data_suspensao")
    public Date dataSuspensao;

    @Column(name="justificativa")
    public String justificativa;

    @Column(name="ativo")
    public Boolean ativo;

    public SuspensaoDispensa() {
    }

    public static List<Suspensao> findAtivas() {
        return Suspensao.find("byAtivo", true).fetch();
    }

    public void suspenderDispensa(UsuarioAnalise usuarioExecutor) {

        Calendar c = Calendar.getInstance();
        Date dataAtual = c.getTime();

        DispensaLicenciamento dispensaLicenciamento = DispensaLicenciamento.findById(this.dispensaLicenciamento.id);
        this.dispensaLicenciamento = dispensaLicenciamento;
        this.ativo = true;

        this.dataSuspensao = dataAtual;
        this.usuario = usuarioExecutor;

        dispensaLicenciamento.ativo = false;

        try {

            dispensaLicenciamento.save();
            this.save();

            Caracterizacao.setStatusCaracterizacao(ListUtil.createList(this.dispensaLicenciamento.caracterizacao.id), StatusCaracterizacao.SUSPENSO);

            this.dispensaLicenciamento.caracterizacao.status = StatusCaracterizacao.findById(StatusCaracterizacaoEnum.SUSPENSO.id);

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
