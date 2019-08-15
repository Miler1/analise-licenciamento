package models.licenciamento;

import com.vividsolutions.jts.geom.Geometry;
import play.db.jpa.GenericModel;

import javax.persistence.*;
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

    public SobreposicaoCaracterizacaoAtividade(TipoSobreposicao tipoSobreposicao, AtividadeCaracterizacao atividadeCaracterizacao, Geometry geometria) {
        this.tipoSobreposicao = tipoSobreposicao;
        this.atividadeCaracterizacao = atividadeCaracterizacao;
        this.geometria = geometria;
    }
}