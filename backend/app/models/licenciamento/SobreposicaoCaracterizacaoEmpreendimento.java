package models.licenciamento;

import com.vividsolutions.jts.geom.Geometry;
import models.CamadaGeoRestricaoVO;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(schema = "licenciamento", name = "sobreposicao_caracterizacao_empreendimento")
public class SobreposicaoCaracterizacaoEmpreendimento extends GenericModel{

private static final String SEQ = "licenciamento.sobreposicao_caracterizacao_id_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    public Long id;

    @ManyToOne
    @JoinColumn(name = "id_tipo_sobreposicao", referencedColumnName="id")
    public TipoSobreposicao tipoSobreposicao;

    @ManyToOne
    @JoinColumn(name = "id_caracterizacao", referencedColumnName="id")
    public Caracterizacao caracterizacao;

    @Column(name = "geometria", columnDefinition="Geometry")
    public Geometry geometria;

    @Column(name = "distancia")
    public Double distancia;

    @Column(name = "nome_area_sobreposicao")
    public String nomeAreaSobreposicao;

    @Column(name = "data_area_sobreposicao")
    public String dataAreaSobreposicao;

    @Column(name = "cpf_cnpj_area_sobreposicao")
    public String cpfCnpjAreaSobreposicao;

//    public SobreposicaoCaracterizacaoEmpreendimento(TipoSobreposicao tipoSobreposicao, Caracterizacao caracterizacao, Geometry geometria) {
//        this.tipoSobreposicao = tipoSobreposicao;
//        this.caracterizacao = caracterizacao;
//        this.geometria = geometria;
//    }

    public CamadaGeoRestricaoVO convertToVO() {

        return new CamadaGeoRestricaoVO(this);

    }

}