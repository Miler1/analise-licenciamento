package models;

import exceptions.ValidacaoException;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import utils.DateUtil;
import utils.Mensagem;

import javax.persistence.*;
import javax.validation.ValidationException;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static models.tramitacao.AcaoTramitacao.SOLICITAR_DESVINCULO;

@Entity
@Table(schema="analise", name="desvinculo_analise_geo")
public class DesvinculoAnaliseGeo extends GenericModel {

    public static final String SEQ = "analise.desvinculo_id_seq";

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator=SEQ)
    @SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
    public Long id;

    @ManyToOne
    @JoinColumn(name="id_analise_geo", nullable=true)
    public AnaliseGeo analiseGeo;

    @Required
    @Column(name="justificativa")
    public String justificativa;


    @Column(name="resposta_gerente")
    public String respostaGerente;


    @Column(name="aprovada")
    public Boolean aprovada;

    @OneToOne
    @JoinColumn(name="id_gerente", referencedColumnName = "id")
    public UsuarioAnalise gerente;

    @Required
    @Column(name="data_solicitacao")
    public Date dataSolicitacao;


    @Column(name="data_resposta")
    public Date dataResposta;

    @ManyToOne
    @JoinColumn(name="id_usuario")
    public UsuarioAnalise analistaGeo;

    @ManyToOne
    @JoinColumn(name="id_usuario_destino")
    public UsuarioAnalise analistaGeoDestino;

    public Date getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void update(DesvinculoAnaliseGeo novoDesvinculo) {

        this.respostaGerente = novoDesvinculo.respostaGerente;
        this.aprovada = novoDesvinculo.aprovada;
        this.dataResposta = novoDesvinculo.dataResposta;
        this.analistaGeoDestino = novoDesvinculo.analistaGeoDestino;

        this._save();
    }

    public void solicitaDesvinculoAnaliseGeo (UsuarioAnalise analistaGeo){

        if(this.justificativa == null || this.justificativa.equals("")){

            throw new ValidacaoException(Mensagem.CAMPOS_OBRIGATORIOS);

        }
        this.dataSolicitacao = new Date();

        String siglaSetor = analistaGeo.usuarioEntradaUnica.setorSelecionado.sigla;

        Gerente gerente = Gerente.distribuicaoAutomaticaGerente(siglaSetor, this.analiseGeo);

        gerente.save();

        this.gerente = UsuarioAnalise.findByGerente(gerente);

        this.analistaGeo =  analistaGeo;

        this.save();

        this.analiseGeo = AnaliseGeo.findById(this.analiseGeo.id);
        this.analiseGeo.analise.processo.tramitacao.tramitar(this.analiseGeo.analise.processo, SOLICITAR_DESVINCULO, analistaGeo, this.gerente);
        HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(this.analiseGeo.analise.processo.objetoTramitavel.id), analistaGeo);

    }

    public void respondeSolicitacaoDesvinculoAnaliseGeo(UsuarioAnalise analistaGeoDestino){

        if(this.justificativa == null ||
                this.justificativa.equals("") ||
                this.respostaGerente== null  ||
                this.respostaGerente.equals("") ||
                this.aprovada == null){

            throw new ValidacaoException(Mensagem.CAMPOS_OBRIGATORIOS);

        }

        this.dataResposta = new Date();

        DesvinculoAnaliseGeo desvinculoAlterar = DesvinculoAnaliseGeo.findById(this.id);
        desvinculoAlterar.update(this);

        this.analiseGeo = AnaliseGeo.findById(this.analiseGeo.id);

        if(this.aprovada) {

            this.analistaGeoDestino = UsuarioAnalise.findById(this.analistaGeoDestino.id);
            AnalistaGeo analistaGeo = AnalistaGeo.find("id_analise_geo = :id_analise_geo")
                    .setParameter("id_analise_geo", this.analiseGeo.id).first();
            analistaGeo.usuario = this.analistaGeoDestino;
            analistaGeo._save();

            this.analiseGeo.analise.processo.tramitacao.tramitar(this.analiseGeo.analise.processo, AcaoTramitacao.APROVAR_SOLICITACAO_DESVINCULO, analistaGeoDestino, this.analistaGeoDestino);

        }else {

            this.analiseGeo.analise.processo.tramitacao.tramitar(this.analiseGeo.analise.processo, AcaoTramitacao.NEGAR_SOLICITACAO_DESVINCULO, analistaGeoDestino, this.analistaGeo);

        }
        HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(this.analiseGeo.analise.processo.objetoTramitavel.id), analistaGeoDestino);

    }

    private static int prazoCongelamentoDesvinculo(DesvinculoAnaliseGeo desvinculoAnaliseGeo) {

        List<HistoricoTramitacao> historicoTramitacao = desvinculoAnaliseGeo.analiseGeo.analise.processo.getHistoricoTramitacao().stream().sorted(Comparator.comparing(HistoricoTramitacao::getDataInicial).reversed()).collect(Collectors.toList());

        if(!historicoTramitacao.isEmpty()) {

            HistoricoTramitacao historicoInicialGerente = historicoTramitacao.stream().filter(tramitacao -> tramitacao.idAcao.equals(SOLICITAR_DESVINCULO))
                    .findFirst().orElseThrow(ValidationException::new);

            String prazo = DateUtil.getDiferencaEmDiasHorasMinutos(historicoInicialGerente.dataInicial, new Date());

            return Integer.parseInt(prazo.split(",")[0].split(" ")[0]);

        }

        return 0;

    }

    private static Date somaDataEmDias(Date data, Integer prazo) {

        Calendar c = Calendar.getInstance();
        c.setTime(data);
        c.add(Calendar.DAY_OF_MONTH, prazo);

        return c.getTime();

    }

}
