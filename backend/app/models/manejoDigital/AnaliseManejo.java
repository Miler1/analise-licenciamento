package models.manejoDigital;

import models.portalSeguranca.Usuario;
import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(schema = "analise", name = "analise_manejo")
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

    @Column(name="area_efetivo_ndfi")
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
    public List<AnaliseNdfi> analiseNdfi;

    @Required
    @OneToMany(mappedBy = "analiseManejo")
    public List<AnaliseVetorial> analiseVetorial;

    @ManyToMany
    @JoinTable(schema = "analise", name = "rel_base_vetorial_analise_manejo",
            joinColumns = @JoinColumn(name = "id_analise_manejo"),
            inverseJoinColumns = @JoinColumn(name = "id_base_vetorial"))
    public List<BaseVetorial> baseVetorial;
}
