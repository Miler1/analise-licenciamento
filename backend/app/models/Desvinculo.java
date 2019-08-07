package models;

import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;
import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(schema="analise", name="desvinculo")
public class Desvinculo extends GenericModel {

    public static final String SEQ = "analise.desvinculo_id_seq";

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator=SEQ)
    @SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
    public Long id;

    @Required
    @OneToOne
    @JoinColumn(name="id_processo", referencedColumnName = "id")
    public Processo idProcesso;

    @Required
    @OneToOne
    @JoinColumn(name="id_analista", referencedColumnName = "id")
    public UsuarioAnalise idAnalistaGeo;

    @Required
    @Column(name="justificativa")
    public String justificativa;


    @Column(name="resposta_gerente")
    public String respostaGerente;


    @Column(name="aprovada")
    public Boolean aprovada;

    @OneToOne
    @JoinColumn(name="id_gerente", referencedColumnName = "id")
    public Gerente idGerente;

    @Required
    @Column(name="data_solicitacao")
    public Date dataSolicitacao;


    @Column(name="data_resposta")
    public Date dataResposta;




}
