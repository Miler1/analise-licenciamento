package models;

import models.licenciamento.OrgaoClasse;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema="analise", name="comunicado")
public class Comunicado extends GenericModel {

    private final static String SEQ = "analise.comunicado_id_seq";

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator=SEQ)
    @SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
    public Long id;

    @ManyToOne
    @JoinColumn(name="id_analise_geo", nullable=true)
    public AnaliseGeo analiseGeo;

    @ManyToOne
    @JoinColumn(name="id_analise_tecnica", nullable=true)
    public AnaliseTecnica analiseTecnica;

    @ManyToOne
    @JoinColumn(name="id_analise_juridica", nullable=true)
    public AnaliseJuridica analiseJuridica;

    @Column(name="justificativa")
    public String justificativa;

    @Column(name="data_cadastro")
    @Temporal(TemporalType.TIMESTAMP)
    public Date dataCadastro;

    @Column(name="data_leitura")
    @Temporal(TemporalType.TIMESTAMP)
    public Date dataLeitura;

    @Column(name="data_resposta")
    @Temporal(TemporalType.TIMESTAMP)
    public Date dataResposta;

    @Column(name="parecer_orgao")
    public String parecerOrgao;

    @OneToOne
    @JoinColumn(name="id_orgao", referencedColumnName="id")
    public Orgao orgao;







}
