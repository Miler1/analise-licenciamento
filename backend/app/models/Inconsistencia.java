package models;

import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import utils.ListUtil;
import utils.ModelUtil;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

@Entity
@Table(schema="analise", name="inconsistencia")
public class Inconsistencia extends GenericModel{

    public static final String SEQ = "analise.inconsistencia_id_seq";

    public enum Categoria { PROPRIEDADE, AREA_PROJETO, RESTRICAO }

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator=SEQ)
    @SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
    public Long id;

    @Required
    @Column(name="descricao_inconsistencia")
    public String descricaoInconsistencia;

    @Required
    @Column(name="tipo_inconsistencia")
    public String tipoInconsistencia;

    @ManyToOne
    @JoinColumn(name="id_analise_geo")
    public AnaliseGeo analiseGeo;

    @ManyToMany
    @JoinTable(schema="analise", name="rel_documento_inconsistencia",
            joinColumns=@JoinColumn(name="id_inconsistencia"),
            inverseJoinColumns=@JoinColumn(name="id_documento"))
    public List<Documento> anexos;


    @Required
    @Enumerated(EnumType.STRING)
    @Column(name="categoria")
    public Categoria categoria;

    public Inconsistencia(AnaliseGeo analiseGeo) {

        if(this.analiseGeo == null){

            this.analiseGeo = analiseGeo;
            this.descricaoInconsistencia = "";
            this.tipoInconsistencia = "";
            this.anexos = null;
        }

    }

    public Inconsistencia() {

    }

    public Inconsistencia(String descricaoInconsistencia, String tipoInconsistencia, Categoria categoria, AnaliseGeo analiseGeo) {

        this.analiseGeo = analiseGeo;
        this.descricaoInconsistencia = descricaoInconsistencia;
        this.tipoInconsistencia = tipoInconsistencia;
        this.categoria = categoria;
    }

    public Inconsistencia(String descricaoInconsistencia, String tipoInconsistencia, Categoria categoria, AnaliseGeo analiseGeo, List<Documento> anexos) {

        this.analiseGeo = analiseGeo;
        this.descricaoInconsistencia = descricaoInconsistencia;
        this.tipoInconsistencia = tipoInconsistencia;
        this.categoria = categoria;
        this.anexos = anexos;
    }

    public void saveAnexos(List<Documento> novosAnexos) {

        TipoDocumento tipo = TipoDocumento.findById(TipoDocumento.DOCUMENTO_INCONSISTENCIA);

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
                List<AnaliseGeo> analiseGeoRelacionadas = docCadastrado.getAnaliseGeoRelacionadas();
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
}
