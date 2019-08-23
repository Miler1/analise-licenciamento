package models;

import models.licenciamento.*;
import play.db.jpa.GenericModel;
import utils.Helper;

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

    @Column(name="data_cadastro")
    @Temporal(TemporalType.TIMESTAMP)
    public Date dataCadastro;

    @Column(name="data_vencimento")
    @Temporal(TemporalType.TIMESTAMP)
    public Date dataVencimento;

    @Column(name="data_resposta")
    @Temporal(TemporalType.TIMESTAMP)
    public Date dataResposta;

    @Column(name="parecer_orgao")
    public String parecerOrgao;

    @Column(name="resolvido")
    public Boolean resolvido;

    @Column(name="ativo")
    public Boolean ativo;

    @OneToOne
    @JoinColumn(name="id_atividade_caracterizacao", referencedColumnName="id")
    public AtividadeCaracterizacao atividadeCaracterizacao;

    @OneToOne
    @JoinColumn(name="id_tipo_sobreposicao", referencedColumnName="id")
    public TipoSobreposicao tipoSobreposicao;

    @OneToOne
    @JoinColumn(name="id_orgao", referencedColumnName="id")
    public Orgao orgao;

    @Transient
    public String linkComunicado;

    @Transient
    public boolean valido;

    public Comunicado(AnaliseGeo analiseGeo, AtividadeCaracterizacao atividadeCaracterizacao, SobreposicaoCaracterizacaoAtividade sobreposicaoCaracterizacaoAtividade, Orgao orgao){
        this.tipoSobreposicao = sobreposicaoCaracterizacaoAtividade.tipoSobreposicao;
        this.dataCadastro = new Date();
        this.dataVencimento = Helper.somarDias(new Date(), 30);
        this.atividadeCaracterizacao = atividadeCaracterizacao;
        this.ativo = true;
        this.analiseGeo = analiseGeo;
        this.resolvido = false;
        this.orgao = orgao;
    }

    public boolean isValido() {

        return this.dataVencimento.after(new Date());
    }


}
