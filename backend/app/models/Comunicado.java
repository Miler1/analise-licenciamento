package models;

import java.util.List;
import models.licenciamento.*;
import play.db.jpa.GenericModel;
import utils.Helper;
import utils.ListUtil;
import utils.ModelUtil;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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

    @OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name="rel_comunicado_atividade_caracterizacao",
			joinColumns={@JoinColumn(name="id_comunicado", referencedColumnName="id")},
			inverseJoinColumns={@JoinColumn(name="id_atividade_caracterizacao", referencedColumnName="id")})
    public List<AtividadeCaracterizacao> atividadesCaracterizacao;

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


    @Transient
    public String linkComunicado;

    @Transient
    public boolean valido;

    public Comunicado(AnaliseGeo analiseGeo, List<AtividadeCaracterizacao> atividadesCaracterizacao, SobreposicaoCaracterizacao sobreposicaoCaracterizacao, Orgao orgao){

        this.tipoSobreposicao = sobreposicaoCaracterizacao.tipoSobreposicao;
        this.dataCadastro = new Date();
        this.dataVencimento = Helper.somarDias(new Date(), 30);
        this.atividadesCaracterizacao = atividadesCaracterizacao;
        this.ativo = true;
        this.analiseGeo = analiseGeo;
        this.resolvido = false;
        this.orgao = orgao;

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
