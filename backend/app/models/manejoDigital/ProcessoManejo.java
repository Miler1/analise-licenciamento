package models.manejoDigital;

import builders.ProcessoManejoBuilder;
import com.google.gson.GsonBuilder;
import deserializers.AnaliseNDFIDeserializer;
import deserializers.AnaliseVetorialDeserializer;
import models.analiseShape.AtributosAddLayer;
import models.analiseShape.FeatureAddLayer;
import models.analiseShape.ResponseAddLayer;
import models.analiseShape.ResponseQueryInsumo;
import models.analiseShape.ResponseQueryProcesso;
import models.analiseShape.ResponseQueryResumoNDFI;
import models.analiseShape.ResponseQuerySobreposicao;
import models.portalSeguranca.Setor;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;
import models.tramitacao.ObjetoTramitavel;
import models.tramitacao.AcaoDisponivelObjetoTramitavel;
import models.tramitacao.Tramitacao;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.GenericModel;
import security.InterfaceTramitavel;
import utils.Configuracoes;
import utils.WebService;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(schema = "analise", name = "processo_manejo")
public class ProcessoManejo extends GenericModel implements InterfaceTramitavel {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="analise.processo_manejo_id_seq")
    @SequenceGenerator(name="analise.processo_manejo_id_seq", sequenceName="analise.processo_manejo_id_seq", allocationSize=1)
    public Long id;

    @Required
    @Unique
    @Column(name="num_processo")
    public String numeroProcesso;

    @Required
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_empreendimento")
    public EmpreendimentoManejo empreendimento;

    @Required
    @ManyToOne
    @JoinColumn(name = "id_tipo_licenca")
    public TipoLicencaManejo tipoLicenca;

    @Required
    @ManyToOne
    @JoinColumn(name = "id_atividade_manejo")
    public AtividadeManejo atividadeManejo;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_analise_manejo")
    public AnaliseManejo analiseManejo;

    @Column(name = "id_objeto_tramitavel")
    public Long idObjetoTramitavel;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_objeto_tramitavel", referencedColumnName = "id_objeto_tramitavel", insertable=false, updatable=false)
    public ObjetoTramitavel objetoTramitavel;

    @Transient
    public transient Tramitacao tramitacao = new Tramitacao();


    @Override
    public ProcessoManejo save() {

        this.tipoLicenca = TipoLicencaManejo.findById(this.tipoLicenca.id);
        this.atividadeManejo = AtividadeManejo.findById(this.atividadeManejo.id);

        tramitacao.iniciar(this, null, Tramitacao.MANEJO_DIGITAL);

        return this;
    }

    @Override
    public Long getIdObjetoTramitavel() {

        return this.idObjetoTramitavel;
    }

    @Override
    public void setIdObjetoTramitavel(Long idObjetoTramitavel) {

        this.idObjetoTramitavel = idObjetoTramitavel;
    }

    @Override
    public List<AcaoDisponivelObjetoTramitavel> getAcoesDisponiveisTramitacao() {

        if (this.idObjetoTramitavel == null)
            return null;

        ObjetoTramitavel objetoTramitavel = ObjetoTramitavel.findById(this.idObjetoTramitavel);
        return objetoTramitavel.acoesDisponiveis;
    }

    @Override
    public void salvaObjetoTramitavel() {

        super.save();
    }

    public ProcessoManejo iniciarAnaliseShape(ProcessoManejo processo) {

        this.analiseManejo = processo.analiseManejo;

        this._save();

        this.enviarProcessoAnaliseShape();

        tramitacao.tramitar(this, AcaoTramitacao.INICIAR_ANALISE_SHAPE, this.analiseManejo.usuario);
        Setor.setHistoricoTramitacao(HistoricoTramitacao.getUltimaTramitacao(this.idObjetoTramitavel), this.analiseManejo.usuario);

        return this.refresh();
    }

    public ProcessoManejo iniciarAnaliseTecnica() {

        tramitacao.tramitar(this, AcaoTramitacao.INICIAR_ANALISE_TECNICA_MANEJO, this.analiseManejo.usuario);
        Setor.setHistoricoTramitacao(HistoricoTramitacao.getUltimaTramitacao(this.idObjetoTramitavel), this.analiseManejo.usuario);

        return this.refresh();
    }

    public String getNomeCondicao() {

        ObjetoTramitavel objetoTramitavel = ObjetoTramitavel.findById(this.idObjetoTramitavel);

        return objetoTramitavel.condicao.nomeCondicao;
    }

    public static List listWithFilter(ProcessoManejoBuilder.FiltroProcessoManejo filtro) {

        ProcessoManejoBuilder processoBuilder = commonFilterProcesso(filtro)
                .groupByIdProcesso()
                .groupByNumeroProcesso()
                .groupByDenominacaoEmpreendimento()
                .groupByMunicipioEmpreendimento()
                .groupByTipoLicencaManejo()
                .groupByCpfCnpjEmpreendimento()
                .groupByCondicao();

        return processoBuilder
                .fetch(filtro.paginaAtual.intValue(), filtro.itensPorPagina.intValue())
                .list();
    }

    public static Long countWithFilter(ProcessoManejoBuilder.FiltroProcessoManejo filtro) {

        ProcessoManejoBuilder processoBuilder = commonFilterProcesso(filtro)
                .addEstadoEmpreendimentoAlias()
                .addTipologiaAtividadeAlias()
                .addObjetoTramitavelAlias()
                .addTipoLicencaAlias()
                .count();

        Object qtdeTotalItens = processoBuilder.unique();

        return ((Map<String, Long>) qtdeTotalItens).get("total");
    }

    private static ProcessoManejoBuilder commonFilterProcesso(ProcessoManejoBuilder.FiltroProcessoManejo filtro) {

        ProcessoManejoBuilder processoBuilder = new ProcessoManejoBuilder()
                .filtrarPorNumeroProcesso(filtro.numeroProcesso)
                .filtrarPorIdMunicipio(filtro.idMunicipioEmpreendimento)
                .filtrarPorCpfCnpjEmpreendimento(filtro.cpfCnpjEmpreendimento)
                .filtrarPorIdTipologia(filtro.idTipologia)
                .filtrarPorIdAtividade(filtro.idAtividade)
                .filtrarPorIdCondicao(filtro.idStatusLicenca)
                .filtrarPorIdTipoLicenca(filtro.idManejoDigital);

        return processoBuilder;
    }

    private void enviarProcessoAnaliseShape() {

        FeatureAddLayer feature = new FeatureAddLayer(
                new AtributosAddLayer(this.numeroProcesso, this.empreendimento.imovel.nome, 0, this.analiseManejo.usuario.nome),
                this.analiseManejo.geometria
        );

        WebService webService = new WebService();

        Map<String, Object> params = new HashMap<>();

        params.put("attributes", feature.attributes);
        params.put("geometry", feature.geometry);

        ResponseAddLayer response = webService.post(Configuracoes.ANALISE_SHAPE_ADD_FEATURES_URL, params, ResponseAddLayer.class);

        //TODO RECUPERAR RESPONTA E SALVAR O objectID;

        this._save();
    }

    public void verificarAnaliseShape() {

        WebService webService = new WebService(
                new GsonBuilder().setDateFormat("dd/MM/yyyy")
                        .registerTypeAdapter(AnaliseNdfi.class, new AnaliseNDFIDeserializer())
                        .registerTypeAdapter(AnaliseVetorial.class, new AnaliseVetorialDeserializer())
                        .create()
        );

        Map<String, Object> params = new HashMap<>();
        params.put("objectIds", this.analiseManejo.objectId);
        params.put("outFields", "*");

        ResponseQueryProcesso response = webService.post(Configuracoes.ANALISE_SHAPE_QUERY_PROCESSOS_URL, params, ResponseQueryProcesso.class);

        if (response.features.get(0).attributes.status == 3) {

            params.clear();
            params.put("where", "protocolo = '" + this.numeroProcesso + "'");
            params.put("outFields", "*");

            ResponseQuerySobreposicao responseSobreposicao =  webService.post(Configuracoes.ANALISE_SHAPE_QUERY_SOBREPOSICOES_URL, params, ResponseQuerySobreposicao.class);
            ResponseQueryInsumo responseInsumo =  webService.post(Configuracoes.ANALISE_SHAPE_QUERY_INSUMOS_URL, params, ResponseQueryInsumo.class);
            ResponseQueryResumoNDFI responseResumoNDFI =  webService.post(Configuracoes.ANALISE_SHAPE_QUERY_INSUMOS_URL, params, ResponseQueryResumoNDFI.class);

            this.analiseManejo.setAnalisesVetoriais(responseSobreposicao.features);
            this.analiseManejo.setInsumos(responseInsumo.features);
            this.analiseManejo.setAnalisesNdfi(responseResumoNDFI.features);

            this.analiseManejo._save();

            tramitacao.tramitar(this, AcaoTramitacao.FINALIZAR_ANALISE_SHAPE, null);
        }
    }

    public static boolean verificaNumeroProcesso(String numeroProcesso) {

        Boolean existe = false;

        ProcessoManejo processo = ProcessoManejo.find("num_processo", numeroProcesso).first();

        if(processo != null){

            if(processo.numeroProcesso.equals(numeroProcesso)) {

                existe = true;
            }
        }
        return existe;
    }
}
