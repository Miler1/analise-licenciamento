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
@Table(schema="analise", name="parecer_juridico")
public class ParecerJuridico extends GenericModel {

    private final static String SEQ = "analise.parecer_juridico_id_seq";

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator=SEQ)
    @SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
    public Long id;

    @ManyToOne
    @JoinColumn(name="id_analise_geo", nullable=true)
    public AnaliseGeo analiseGeo;

    @OneToOne
    @JoinColumn(name = "id_parecer_analista_geo", referencedColumnName = "id")
    public ParecerAnalistaGeo parecerAnalistaGeo;

    @ManyToOne
    @JoinColumn(name = "id_tipo_resultado_validacao_juridica")
    public TipoResultadoAnalise tipoResultadoAnalise;

    @Column(name="data_cadastro")
    @Temporal(TemporalType.TIMESTAMP)
    public Date dataCadastro;

    @Column(name="data_resposta")
    @Temporal(TemporalType.TIMESTAMP)
    public Date dataResposta;

    @Column(name="parecer")
    public String parecer;

    @Column(name="resolvido")
    public Boolean resolvido;

    @Column(name="ativo")
    public Boolean ativo;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(schema="analise", name="rel_documento_juridico",
            joinColumns=@JoinColumn(name="id_parecer_juridico"),
            inverseJoinColumns=@JoinColumn(name="id_documento"))
    public List<Documento> anexos;

    @Transient
    public String linkParecerJuridico;

    public ParecerJuridico(AnaliseGeo analiseGeo, ParecerAnalistaGeo parecerAnalistaGeo){

        this.dataCadastro = new Date();
        this.analiseGeo = analiseGeo;
        this.ativo = true;
        this.resolvido = false;
        this.parecerAnalistaGeo = parecerAnalistaGeo;

    }

    public static List<Comunicado> findByAnaliseGeo(Long idAnaliseGeo) {
        List<Comunicado> listaComunicados = null;
        return listaComunicados = Comunicado.find("id_analise_geo = :analiseGeo")
                .setParameter("analiseGeo", idAnaliseGeo).fetch();
    }


    public void saveAnexos(List<Documento> novosAnexos) {

        TipoDocumento tipo = TipoDocumento.findById(TipoDocumento.DOCUMENTO_JURIDICO);

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
                List<ParecerJuridico> pareceresJuridicosRelacionados = docCadastrado.getPareceresJuridicosRelacionados();
                if(pareceresJuridicosRelacionados.size() == 0) {

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

}

