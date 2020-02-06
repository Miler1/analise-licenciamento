package models.licenciamento;

import com.vividsolutions.jts.geom.Geometry;
import models.CamadaGeoRestricaoVO;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(schema = "licenciamento", name = "sobreposicao_caracterizacao_atividade")
public class SobreposicaoCaracterizacaoAtividade extends GenericModel{

    private static final String SEQ = "licenciamento.sobreposicao_caracterizacao_atividade_id_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    public Long id;

    @ManyToOne
    @JoinColumn(name = "id_tipo_sobreposicao", referencedColumnName="id")
    public TipoSobreposicao tipoSobreposicao;

    @ManyToOne
    @JoinColumn(name = "id_atividade_caracterizacao", referencedColumnName="id")
    public AtividadeCaracterizacao atividadeCaracterizacao;

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

    public SobreposicaoCaracterizacaoAtividade(TipoSobreposicao tipoSobreposicao, AtividadeCaracterizacao atividadeCaracterizacao, Geometry geometria) {
        this.tipoSobreposicao = tipoSobreposicao;
        this.atividadeCaracterizacao = atividadeCaracterizacao;
        this.geometria = geometria;
    }

    public CamadaGeoRestricaoVO convertToVO() {

        return new CamadaGeoRestricaoVO(this);

    }

}
