package models.manejoDigital;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Date;

public class AnaliseManejo  extends GenericModel {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="analise_manejo_id_seq")
    @SequenceGenerator(name="analise_manejo_id_seq", sequenceName="analise_manejo_id_seq", allocationSize=1)
    public Long id;

    @Required
    @Column(name="data")
    public Date dataAnalise;

    @Required
    @Column(name="dias_analise")
    public Integer diasAnalise;

    @Required
    @Column(name="path_arquivo_shape")
    public String pathShape;

    @Required
    @Column(name="path_anexo")
    public String pathAnexo;

    @Required
    @Column(name="analise_temporal")
    public String analiseTemporal;

    @Required
    @Column(name="area_manejo_florestal_solicitada")
    public Double areaManejoFlorestalSolicitada;

    @Required
    @Column(name="area_preservacao_permanente")
    public Double areaPreservacaoPermanente;

    @Required
    @Column(name="area_servidao")
    public Double areaServidao;

    @Required
    @Column(name="area_antropizada_nao_consolidada")
    public Double areaAntropizadaNaoConsolidada;

    @Required
    @Column(name="area_uso_restrito")
    public Double areaUsoRestrito;

    @Required
    @Column(name="area_sem_potencial")
    public Double areaSemPotencial;

    @Required
    @Column(name="area_corpos_agua")
    public Double areaCorposAgua;

    @Required
    @Column(name="area_embargada_ibama")
    public Double areaEmbargadaIbama;

    @Required
    @Column(name="area_embargada_ldi")
    public Double areaEmbargadaLdi;

    @Required
    @Column(name="prazo_analise")
    public Integer prazoAnalise;

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
    @Column(name="remanescente_da_vegetacao_nativa")
    public Double remanescenteDaVegetacaoNativa;

    @Required
    @Column(name="area_de_uso_consolidado")
    public Double areaUsoConsolidado;

    @Required
    @JoinColumn(name="area_seletiva_ndfi")
    public AnaliseNdfi areaSeletivaNdfi;

    @Required
    @Column(name="area_efetivo_manejo")
    public Double areaEfetivoManejo;

    @Required
    @Column(name="area_com_exploraca_ndfi_baixo")
    public Double areaExploracaoNdfiBaixo;

    @Required
    @Column(name="area_com_exploraca_ndfi_medio")
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

    @Required
    @Column(name="id_usuario")
    public Integer idusuario;

    @Required
    @Column(name="id_imovel_manejo")
    public Integer idImovelManjeo;

    @Required
    @Column(name="area_consolidada")
    public Double areaConsolidada;


}
