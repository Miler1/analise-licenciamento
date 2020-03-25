package models;

import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import enums.TipoSobreposicaoDistanciaEnum;
import models.licenciamento.Caracterizacao.OrigemSobreposicao;
import models.licenciamento.*;
import org.omg.CORBA.PRIVATE_MEMBER;
import play.db.jpa.GenericModel;
import utils.GeoCalc;
import utils.Helper;
import utils.ListUtil;
import utils.ModelUtil;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

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

    @Column(name="aguardando_resposta")
    public Boolean aguardandoResposta;

    @Column(name="segundo_email_enviado")
    public Boolean segundoEmailEnviado;

    @Column(name="interessado_notificado")
    public Boolean interessadoNotificado;

    @OneToOne
    @JoinColumn(name="id_caracterizacao", referencedColumnName="id")
    public Caracterizacao caracterizacao;

    @OneToOne
    @JoinColumn(name="id_tipo_sobreposicao", referencedColumnName="id")
    public TipoSobreposicao tipoSobreposicao;

    @OneToOne
    @JoinColumn(name="id_orgao", referencedColumnName="id")
    public Orgao orgao;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(schema="analise", name="rel_documento_comunicado",
            joinColumns=@JoinColumn(name="id_comunicado"),
            inverseJoinColumns=@JoinColumn(name="id_documento"))
    public List<Documento> anexos;

    @OneToOne
    @JoinColumn(name="id_sobreposicao_atividade")
    public SobreposicaoCaracterizacaoAtividade sobreposicaoCaracterizacaoAtividade;

    @OneToOne
    @JoinColumn(name="id_sobreposicao_empreendimento")
    public SobreposicaoCaracterizacaoEmpreendimento sobreposicaoCaracterizacaoEmpreendimento;

    @OneToOne
    @JoinColumn(name="id_sobreposicao_complexo")
    public SobreposicaoCaracterizacaoComplexo sobreposicaoCaracterizacaoComplexo;

    @Transient
    public String linkComunicado;

    @Transient
    public boolean valido;

    @Transient
    public String distancia;

    @Transient
    public ParecerAnalistaGeo parecerAnalistaGeo;

    public static boolean verificaTipoSobreposicaoComunicado(SobreposicaoCaracterizacaoEmpreendimento sobreposicaoCaracterizacaoEmpreendimento) {

        if (sobreposicaoCaracterizacaoEmpreendimento != null) {

            if (sobreposicaoCaracterizacaoEmpreendimento.tipoSobreposicao.id == TipoSobreposicao.UC_ESTADUAL_PI_FORA || sobreposicaoCaracterizacaoEmpreendimento.tipoSobreposicao.id == TipoSobreposicao.UC_MUNICIPAL) {

                return true;

            }

        }

        return false;
    }

    public static boolean verificaTipoSobreposicaoComunicado(SobreposicaoCaracterizacaoComplexo sobreposicaoCaracterizacaoComplexo) {

        if (sobreposicaoCaracterizacaoComplexo != null) {

            if (sobreposicaoCaracterizacaoComplexo.tipoSobreposicao.id == TipoSobreposicao.UC_ESTADUAL_PI_FORA || sobreposicaoCaracterizacaoComplexo.tipoSobreposicao.id == TipoSobreposicao.UC_MUNICIPAL) {

                return true;

            }

        }

        return false;
    }

    public static boolean verificaTipoSobreposicaoComunicado(SobreposicaoCaracterizacaoAtividade sobreposicaoCaracterizacaoAtividade) {

        if (sobreposicaoCaracterizacaoAtividade != null) {

            if (sobreposicaoCaracterizacaoAtividade.tipoSobreposicao.id == TipoSobreposicao.UC_ESTADUAL_PI_FORA || sobreposicaoCaracterizacaoAtividade.tipoSobreposicao.id == TipoSobreposicao.UC_MUNICIPAL) {

                return true;

            }

        }

        return false;
    }


    public Comunicado(AnaliseGeo analiseGeo, Caracterizacao caracterizacao, Boolean aguardandoResposta, SobreposicaoCaracterizacaoEmpreendimento sobreposicaoCaracterizacaoEmpreendimento, Orgao orgao){

        this.tipoSobreposicao = sobreposicaoCaracterizacaoEmpreendimento.tipoSobreposicao;
        this.dataCadastro = new Date();
        this.dataVencimento = Helper.somarDias(new Date(), 15);
        this.caracterizacao = caracterizacao;
        this.aguardandoResposta = aguardandoResposta;
        this.analiseGeo = analiseGeo;
        this.orgao = orgao;
        this.segundoEmailEnviado = false;
        this.sobreposicaoCaracterizacaoEmpreendimento = sobreposicaoCaracterizacaoEmpreendimento;
        this.distancia = getDistancia(sobreposicaoCaracterizacaoEmpreendimento.distancia, sobreposicaoCaracterizacaoEmpreendimento.geometria, sobreposicaoCaracterizacaoEmpreendimento.caracterizacao);
        this.interessadoNotificado = false;

        if (aguardandoResposta) {

            this.ativo = true;
            this.resolvido = false;

        } else {

            this.ativo = false;
            this.resolvido = true;

        }

    }

    public Comunicado(AnaliseGeo analiseGeo, Caracterizacao caracterizacao, Boolean aguardandoResposta, SobreposicaoCaracterizacaoAtividade sobreposicaoCaracterizacaoAtividade, Orgao orgao){

        this.tipoSobreposicao = sobreposicaoCaracterizacaoAtividade.tipoSobreposicao;
        this.dataCadastro = new Date();
        this.dataVencimento = Helper.somarDias(new Date(), 15);
        this.caracterizacao = caracterizacao;
        this.aguardandoResposta = aguardandoResposta;
        this.analiseGeo = analiseGeo;
        this.orgao = orgao;
        this.segundoEmailEnviado = false;
        this.sobreposicaoCaracterizacaoAtividade = sobreposicaoCaracterizacaoAtividade;
        this.distancia = getDistancia(sobreposicaoCaracterizacaoAtividade.distancia, sobreposicaoCaracterizacaoAtividade.geometria, sobreposicaoCaracterizacaoAtividade.atividadeCaracterizacao.caracterizacao);
        this.interessadoNotificado = false;

        if (aguardandoResposta) {

            this.ativo = true;
            this.resolvido = false;

        } else {

            this.ativo = false;
            this.resolvido = true;

        }

    }

    public Comunicado(AnaliseGeo analiseGeo, Caracterizacao caracterizacao, Boolean aguardandoResposta, SobreposicaoCaracterizacaoComplexo sobreposicaoCaracterizacaoComplexo, Orgao orgao){

        this.tipoSobreposicao = sobreposicaoCaracterizacaoComplexo.tipoSobreposicao;
        this.dataCadastro = new Date();
        this.dataVencimento = Helper.somarDias(new Date(), 15);
        this.caracterizacao = caracterizacao;
        this.aguardandoResposta = aguardandoResposta;
        this.analiseGeo = analiseGeo;
        this.orgao = orgao;
        this.segundoEmailEnviado = false;
        this.sobreposicaoCaracterizacaoComplexo = sobreposicaoCaracterizacaoComplexo;
        this.distancia = getDistancia(sobreposicaoCaracterizacaoComplexo.distancia, sobreposicaoCaracterizacaoComplexo.geometria, sobreposicaoCaracterizacaoComplexo.caracterizacao);
        this.interessadoNotificado = false;

        if (aguardandoResposta) {

            this.ativo = true;
            this.resolvido = false;

        } else {

            this.ativo = false;
            this.resolvido = true;

        }

    }

    public String getDistancia(Double distancia, Geometry geometria, Caracterizacao caracterizacao) {

        if(TipoSobreposicaoDistanciaEnum.getList().contains(this.tipoSobreposicao.codigo) && caracterizacao.origemSobreposicao.equals(OrigemSobreposicao.EMPREENDIMENTO)) {

            return "o empreendimento dista " + Helper.formatBrDecimal(distancia / 1000, 2) + " km";

        } else if(TipoSobreposicaoDistanciaEnum.getList().contains(this.tipoSobreposicao.codigo) && !caracterizacao.origemSobreposicao.equals(OrigemSobreposicao.EMPREENDIMENTO)) {

            return "a(s) atividade(s) dista(m) " + Helper.formatBrDecimal(distancia / 1000, 2) + " km";

        } else if(caracterizacao.origemSobreposicao.equals(OrigemSobreposicao.EMPREENDIMENTO)) {

            return "o empreendimento sobrepõe " + getDescricao(geometria);

        } else {

            return "a(s) atividade(s) sobrepõe(m) " + getDescricao(geometria);
        }

    }

    public static String getDescricao(Geometry geometry) {

        switch (geometry.getGeometryType().toUpperCase()) {

            case "POINT" :

                return Helper.formatarCoordenada(geometry.getCoordinate());

            case "LINESTRING":

                return Helper.formatBrDecimal(GeoCalc.length(geometry)/1000, 2) + " km";

            default:

                return Helper.formatBrDecimal(GeoCalc.areaHectare(geometry),2) + " ha";

        }

    }

    public static List<Comunicado> findByAnaliseGeo(Long idAnaliseGeo) {
        List<Comunicado> listaComunicados = null;
        return listaComunicados = Comunicado.find("id_analise_geo = :analiseGeo")
                .setParameter("analiseGeo", idAnaliseGeo).fetch();
    }


    public void saveAnexos(List<Documento> novosAnexos) {

        TipoDocumento tipo = TipoDocumento.findById(TipoDocumento.DOCUMENTO_COMUNICADO);

        if (this.anexos == null)
            this.anexos = new ArrayList<>();

        Iterator<Documento> docsCadastrados = anexos.iterator();
        List<Documento> documentosDeletar = new ArrayList<>();

        while (docsCadastrados.hasNext()) {

            Documento docCadastrado = docsCadastrados.next();

            if (ListUtil.getById(docCadastrado.id, novosAnexos) == null) {

                docsCadastrados.remove();

                // remove o documeto do banco apenas se ele não estiver relacionado
                // com outra análises
                List<Comunicado> analiseGeoRelacionadas = docCadastrado.getComunicadosRelacionados();
                if(analiseGeoRelacionadas.size() == 0) {

                    documentosDeletar.add(docCadastrado);
                }

            }
        }
        for (Documento novoAnexo : novosAnexos) {

            if (novoAnexo.id == null) {

                novoAnexo.tipo = tipo;

                novoAnexo.save();
                this.anexos.add(novoAnexo);
            }
        }

        ModelUtil.deleteAll(documentosDeletar);
    }


    public boolean isValido() {

        return this.dataVencimento.after(new Date());

    }

}
