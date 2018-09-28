package models.manejoDigital;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;

public class AnaliseManejo  extends GenericModel {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="analise_manejo_id_seq")
    @SequenceGenerator(name="analise_manejo_id_seq", sequenceName="analise_manejo_id_seq", allocationSize=1)
    public Long id;

    @Required
    @Column(name="prazo_analise")
    public Integer prazoAnalise;

    @Required
    @Column(name="caminho_shape")
    public String caminhoShape;

    @JoinColumn(name="id_observacao")
    public Observacao observacao;

    @Required
    @Column(name="registro_car")
    public String registroCar;

    @Required
    @Column(name="area_total_imovel")
    public Double areaTotalImovel;

    @Required
    @Column(name="area_liquida_imovel")
    public Double areaLiquidaImovel;

    @Required
    @Column(name="reserva_legal")
    public Double reservaLegal;

    @Required
    @Column(name="area_preservacao_permanente")
    public Double areaPreservacaoPermanente;

    @Required
    @Column(name="remanescente_da_vegetacao_nativa")
    public Double remanescenteDaVegetacaoNativa;

    @Required
    @Column(name="corpos_dagua")
    public Double corposDagua;

    @Required
    @Column(name="area_de_uso_consolidado")
    public Double areaUsoConsolidado;

    @Required
    @Column(name="caminho_anexo")
    public String caminhoAnexo;

    @Required
    @JoinColumn(name="id_metadados_base_vetorial")
    public MetadadosBaseVetorial metadadosBaseVetorial;

    @Required
    @JoinColumn(name="dados_analisados_amf")
    public DadosAmf dadosAmf;

    @Required
    @Column(name="analise_temporal_de_satelites")
    public String analiseTemporalSatelites;

    @Required
    @JoinColumn(name="calculo_ndfi")
    public CalculoNdfi calculoNdfi;

    @Required
    @Column(name="area_manejo_florestal_solicitada")
    public Double areaManejoFlorestalSolicitada;

    @Required
    @Column(name="area_servidao")
    public Double areaServidao;

    @Required
    @Column(name="area_antropizada")
    public Double areaAntropizada;

    @Required
    @Column(name="area_consolidada")
    public Double areaConsolidada;

    @Required
    @Column(name="uso_restrito")
    public Double usoRestrito;

    @Required
    @Column(name="area_sem_potencial")
    public Double areaSemPotencial;

    @Required
    @Column(name="corpo_agua")
    public Double corpoAgua;

    @Required
    @Column(name="area_embargada_ibama")
    public Double areaEmbargadaIbama;

    @Required
    @Column(name="area_embargada_ldi")
    public Double areaEmbargadaLdi;

    @Required
    @Column(name="exploracao_seletiva_ndfi_alto")
    public Double exploracaoSeletivaNdfiAlto;

    @Required
    @Column(name="area_de_efetivo_manejo")
    public Double areaEfetivoManejo;

    @Required
    @Column(name="area_exploracao_ndfi_baixo")
    public Double areaExploracaoNdfiBaixo;

    @Required
    @Column(name="area_exploracao_ndfi_medio")
    public Double areaExploracaoNdfiMedio;

    @Required
    @Column(name="area_sem_previa_exploracao")
    public Double areaSemPreviaExploracao;

    @Required
    @Column(name="consideracoes")
    public String consideracoes;

    @Required
    @Column(name="conclusao")
    public String conclusao;

}
