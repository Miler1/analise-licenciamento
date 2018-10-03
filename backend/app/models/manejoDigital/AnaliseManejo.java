package models.manejoDigital;

import models.portalSeguranca.Usuario;
import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Entity
@Table(schema = "analise", name = "analise_manejo")
public class AnaliseManejo  extends GenericModel {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="analise.analise_manejo_id_seq")
    @SequenceGenerator(name="analise.analise_manejo_id_seq", sequenceName="analise.analise_manejo_id_seq", allocationSize=1)
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

    @Column(name="path_anexo")
    public String pathAnexo;

    @Required
    @Column(name="analise_temporal")
    public String analiseTemporal;

    @Required
    @Column(name="area_manejo_florestal_solicitada")
    public Double areaManejoFlorestalSolicitada;

    @Column(name="area_preservacao_permanente")
    public Double areaPreservacaoPermanente;

    @Column(name="area_servidao")
    public Double areaServidao;

    @Column(name="area_antropizada_nao_consolidada")
    public Double areaAntropizadaNaoConsolidada;

    @Column(name="area_uso_restrito")
    public Double areaUsoRestrito;

    @Column(name="area_sem_potencial")
    public Double areaSemPotencial;

    @Column(name="area_corpos_agua")
    public Double areaCorposAgua;

    @Column(name="area_embargada_ibama")
    public Double areaEmbargadaIbama;

    @Column(name="area_embargada_ldi")
    public Double areaEmbargadaLdi;

    @Column(name="area_seletiva_ndfi")
    public Double areaSeletivaNdfi;

    @Column(name="area_efetivo_manejo")
    public Double areaEfetivoNdfi;

    @Column(name="area_com_exploraca_ndfi_baixo")
    public Double areaExploracaoNdfiBaixo;

    @Column(name="area_com_exploraca_ndfi_medio")
    public Double areaExploracaoNdfiMedio;

    @Column(name="area_sem_previa_exploracao")
    public Double areaSemPreviaExploracao;

    @Required
    @Column
    public String consideracoes;

    @Required
    @Column
    public String conclusao;

    @Required
    @OneToOne
    @JoinColumn(name="id_usuario")
    public Usuario usuario;

    @Required
    @OneToMany(mappedBy = "analiseManejo")
    public List<Observacao> observacoes;

    @Required
    @OneToOne(mappedBy = "analiseManejo")
    public ProcessoManejo processoManejo;

    @Required
    @OneToMany(mappedBy = "analiseManejo")
    public List<AnaliseNdfi> analisesNdfi;

    @Required
    @OneToMany(mappedBy = "analiseManejo")
    public List<AnaliseVetorial> analisesVetorial;

    @ManyToMany
    @JoinTable(schema = "analise", name = "rel_base_vetorial_analise_manejo",
            joinColumns = @JoinColumn(name = "id_analise_manejo"),
            inverseJoinColumns = @JoinColumn(name = "id_base_vetorial"))
    public List<BaseVetorial> basesVetorial;

    public static AnaliseManejo	gerarAnalise(ProcessoManejo processo) {

        AnaliseManejo analiseManejo = new AnaliseManejo();

        analiseManejo.dataAnalise = new Date();

        analiseManejo.diasAnalise = 0;

        analiseManejo.pathShape = processo.analiseManejo.pathShape;

        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        analiseManejo.analiseTemporal = new String(array, Charset.forName("UTF-8"));

        analiseManejo.areaManejoFlorestalSolicitada = Math.random();

        analiseManejo.areaPreservacaoPermanente = Math.random();

        analiseManejo.areaServidao = Math.random();

        analiseManejo.areaAntropizadaNaoConsolidada = Math.random();

        analiseManejo.areaUsoRestrito = Math.random();

        analiseManejo.areaSemPotencial = Math.random();

        analiseManejo.areaCorposAgua = Math.random();

        analiseManejo.areaEmbargadaIbama = Math.random();

        analiseManejo.areaEfetivoNdfi = Math.random();

        analiseManejo.areaEmbargadaLdi = Math.random();

        analiseManejo.areaSeletivaNdfi = Math.random();

        analiseManejo.areaExploracaoNdfiBaixo = Math.random();

        analiseManejo.areaExploracaoNdfiMedio = Math.random();

        analiseManejo.areaSemPreviaExploracao = Math.random();

        array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        analiseManejo.consideracoes =  new String(array, Charset.forName("UTF-8"));

        array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        analiseManejo.conclusao =  new String(array, Charset.forName("UTF-8"));

        analiseManejo.usuario = Usuario.findById(22);

        analiseManejo.analisesNdfi = AnaliseNdfi.gerarAnaliseNfid(analiseManejo);

        analiseManejo.analisesVetorial = AnaliseVetorial.gerarAnalisesVetoriais(analiseManejo);
    }
}
