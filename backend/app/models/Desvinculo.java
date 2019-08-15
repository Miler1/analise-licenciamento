package models;

import models.EntradaUnica.CodigoPerfil;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;

import javax.persistence.*;
import javax.xml.ws.WebServiceException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(schema="analise", name="desvinculo")
public class Desvinculo extends GenericModel {

    public static final String SEQ = "analise.desvinculo_id_seq";

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator=SEQ)
    @SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
    public Long id;

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
    @JoinColumn(name="id_analise_geo", nullable=true)
    public AnaliseGeo analiseGeo;

    @ManyToOne
    @JoinColumn(name="id_analise_tecnica", nullable=true)
    public AnaliseTecnica analiseTecnica;

    @ManyToOne
    @JoinColumn(name="id_analise_juridica", nullable=true)
    public AnaliseJuridica analiseJuridica;
}
