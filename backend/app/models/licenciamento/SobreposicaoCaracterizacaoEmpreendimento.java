package models.licenciamento;

import com.vividsolutions.jts.geom.Geometry;
import play.db.jpa.GenericModel;

import javax.persistence.*;
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

    public SobreposicaoCaracterizacaoEmpreendimento(TipoSobreposicao tipoSobreposicao, Caracterizacao caracterizacao, Geometry geometria) {
        this.tipoSobreposicao = tipoSobreposicao;
        this.caracterizacao = caracterizacao;
        this.geometria = geometria;
    }
}