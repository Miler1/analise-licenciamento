package models.manejoDigital;

import models.StatusImovelManejo;
import models.licenciamento.Municipio;
import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;

@Entity
@Table(schema = "analise", name = "imovel_manejo")
public class ImovelManejo extends GenericModel {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="analise.imovel_manejo_id_seq")
    @SequenceGenerator(name="analise.imovel_manejo_id_seq", sequenceName="analise.imovel_manejo_id_seq", allocationSize=1)
    public Long id;

    @Required
    @Column(name="registro_car")
    public String registroCar;

    @Column
    public String nome;

    @Column(name="area_total_imovel_documentado")
    public Double areaTotalImovelDocumentado;

    @Column(name="area_liquida_imovel")
    public Double areaLiquidaImovel;

    @Column(name="area_reserva_legal")
    public Double areaReservaLegal;

    @Column(name="area_preservacao_permanente")
    public Double areaPreservacaoPermanente;

    @Column(name="area_remanescente_vegetacao_nativa")
    public Double areaRemanescenteVegetacaoNativa;

    @Column(name="area_corpos_agua")
    public Double areaCorposAgua;

    @Column(name="area_uso_consolidado")
    public Double areaUsoConsolidado;

    @Column(name="descricao_acesso")
    public String descricaoAcesso;

    @Column
    @Enumerated(EnumType.STRING)
    public StatusImovelManejo status;

	@Required
	@ManyToOne
	@JoinColumn(name="id_municipio", referencedColumnName="id_municipio")
	public Municipio municipio;

    @Override
    public ImovelManejo save() {

        this.municipio = Municipio.findById(this.municipio.id);

        return this;
    }
}
