package models.manejoDigital;

import builders.ProcessoManejoBuilder;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import deserializers.GeometriaArcgisDeserializer;
import exceptions.WebServiceException;
import models.TipoDocumento;
import models.manejoDigital.analise.analiseShape.AtributosAddLayer;
import models.manejoDigital.analise.analiseShape.GeometriaArcgis;
import models.manejoDigital.analise.analiseShape.ResponseAddLayer;
import models.manejoDigital.analise.analiseTecnica.AnaliseTecnicaManejo;
import models.manejoDigital.analise.analiseTecnica.AnalistaTecnicoManejo;
import models.portalSeguranca.Setor;
import models.portalSeguranca.Usuario;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;
import models.tramitacao.ObjetoTramitavel;
import models.tramitacao.AcaoDisponivelObjetoTramitavel;
import models.tramitacao.Tramitacao;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.GenericModel;
import security.Auth;
import security.InterfaceTramitavel;
import utils.Configuracoes;
import utils.WebService;

import javax.persistence.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
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

    @OneToMany(mappedBy = "processoManejo", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<AnaliseTecnicaManejo> analisesTecnicaManejo;

    @Column(name = "id_objeto_tramitavel")
    public Long idObjetoTramitavel;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_objeto_tramitavel", referencedColumnName = "id_objeto_tramitavel", insertable=false, updatable=false)
    public ObjetoTramitavel objetoTramitavel;

    @Column(name = "revisao_solicitada")
    public boolean revisaoSolicitada;

    @Column(name = "justificativa_indeferimento")
    public String justificativaIndeferimento;

    @Transient
    public transient Tramitacao tramitacao = new Tramitacao();


    @Override
    public ProcessoManejo save() {

        this.tipoLicenca = TipoLicencaManejo.findById(this.tipoLicenca.id);
        this.atividadeManejo = AtividadeManejo.findById(this.atividadeManejo.id);
        this.revisaoSolicitada = false;

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

    public ProcessoManejo iniciarAnaliseShape(ProcessoManejo processo, Usuario usuario) {

        this.analisesTecnicaManejo.add(processo.getAnaliseTecnica());
        this.getAnaliseTecnica().dataAnalise = new Date();
        this.getAnaliseTecnica().diasAnalise = 0;
        this.getAnaliseTecnica().analistaTecnico = new AnalistaTecnicoManejo(processo.getAnaliseTecnica(),
                (Usuario) Usuario.findById(Auth.getUsuarioSessao().id));
        this.getAnaliseTecnica().processoManejo = this;
        this.getAnaliseTecnica().analistaTecnico = new AnalistaTecnicoManejo(this.getAnaliseTecnica(), usuario);

        this.getAnaliseTecnica()._save();

        for(DocumentoShape documento : this.getAnaliseTecnica().documentosShape) {

            documento.analiseTecnicaManejo = this.getAnaliseTecnica();
            documento.tipo = TipoDocumento.findById(documento.tipo.id);
            documento.save();
        }

        //TODO Integração com o serviço VEGA
        //this.enviarProcessoAnaliseShape();

        tramitacao.tramitar(this, AcaoTramitacao.INICIAR_ANALISE_SHAPE, this.getAnaliseTecnica().analistaTecnico.usuario);
        Setor.setHistoricoTramitacao(HistoricoTramitacao.getUltimaTramitacao(this.idObjetoTramitavel), this.getAnaliseTecnica().analistaTecnico.usuario);

        this.revisaoSolicitada = false;
        this._save();

        return this.refresh();
    }

    public ProcessoManejo iniciarAnaliseTecnica() {

        tramitacao.tramitar(this, AcaoTramitacao.INICIAR_ANALISE_TECNICA_MANEJO, this.getAnaliseTecnica().analistaTecnico.usuario);
        Setor.setHistoricoTramitacao(HistoricoTramitacao.getUltimaTramitacao(this.idObjetoTramitavel), this.getAnaliseTecnica().analistaTecnico.usuario);

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
                .groupByCondicao()
                .groupByRevisaoSolicitada();

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

        Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy")
                .registerTypeAdapter(GeometriaArcgis.class, new GeometriaArcgisDeserializer())
                .create();

        // TODO Alterar envio de features para refletir os diferentes tipos de documento shape. É NECESSÁRIO QUE O SERVIÇO DE INTEGRAÇÃO DA VEGA SEJA ALTERADO.
        List<GeometriaArcgis> features = new ArrayList<>();
        Type listType = new TypeToken<ArrayList<GeometriaArcgis>>(){}.getType();

        for (DocumentoShape documento : this.getAnaliseTecnica().documentosShape) {

           features.addAll((ArrayList<GeometriaArcgis>) gson.fromJson(documento.geoJsonArcgis, listType));
        }

        for (GeometriaArcgis feature : features) {

            feature.attributes = new AtributosAddLayer(this.numeroProcesso,
                    this.empreendimento.imovel.nome, 0, this.getAnaliseTecnica().analistaTecnico.usuario.nome);
        }

        WebService webService = new WebService();

        Map<String, Object> params = new HashMap<>();

        params.put("features", gson.toJson(features));
        params.put("f", "json");

        ResponseAddLayer response = webService.post(Configuracoes.ANALISE_SHAPE_ADD_FEATURES_URL, params, ResponseAddLayer.class);

        if (response.error != null) {

            throw new WebServiceException("Erro ao enviar processo para análise: " + response.error.message);

        } else {

            this.getAnaliseTecnica().objectId = response.addResults.get(response.addResults.size() - 1).objectId;
        }

        this._save();
    }

    public void verificarAnaliseShape() {

        //TODO Integração com o serviço VEGA
//        WebService webService = new WebService(
//                new GsonBuilder().setDateFormat("dd/MM/yyyy")
//                        .registerTypeAdapter(AnaliseNdfi.class, new AnaliseNDFIDeserializer())
//                        .registerTypeAdapter(AnaliseVetorial.class, new AnaliseVetorialDeserializer())
//                        .registerTypeAdapter(AtributosQueryAMFManejo.class, new AtributosQueryAMFManejoDeserializer())
//                        .create()
//        );
//
//        Map<String, Object> params = new HashMap<>();
//        params.put("objectIds", this.getAnaliseTecnica().objectId);
//        params.put("outFields", "*");
//        params.put("f", "json");
//
//        ResponseQueryProcesso response = webService.post(Configuracoes.ANALISE_SHAPE_QUERY_PROCESSOS_URL, params, ResponseQueryProcesso.class);
//
//        if (response.features.get(0).attributes.status == 3) {
//
//            params.clear();
//            params.put("where", "protocolo = '" + this.numeroProcesso + "'");
//            params.put("outFields", "*");
//            params.put("f", "json");
//
//            ResponseQuerySobreposicao responseSobreposicao =  webService.post(Configuracoes.ANALISE_SHAPE_QUERY_SOBREPOSICOES_URL, params, ResponseQuerySobreposicao.class);
//            ResponseQueryAMFManejo responseAMFManejo = webService.post(Configuracoes.ANALISE_SHAPE_QUERY_AMF_MANEJO_URL, params, ResponseQueryAMFManejo.class);
//
//            params.remove("where");
//            params.put("where", "processo = '" + this.numeroProcesso + "'");
//            ResponseQueryInsumo responseInsumo = webService.post(Configuracoes.ANALISE_SHAPE_QUERY_INSUMOS_URL, params, ResponseQueryInsumo.class);
//
//            params.remove("where");
//            params.put("where", "processo_amf = '" + this.numeroProcesso + "'");
//            ResponseQueryResumoNDFI responseResumoNDFI = webService.post(Configuracoes.ANALISE_SHAPE_QUERY_RESUMO_NDFI_URL, params, ResponseQueryResumoNDFI.class);
//
//            this.getAnaliseTecnica().setAnalisesVetoriais(responseSobreposicao.features);
//            this.getAnaliseTecnica().setInsumos(responseInsumo.features);
//            this.getAnaliseTecnica().setAnalisesNdfi(responseResumoNDFI.features);
//            this.getAnaliseTecnica().areaEfetivoNdfi = responseAMFManejo.features.get(0).attributes.area;
//
//            this.getAnaliseTecnica()._save();
//
//            tramitacao.tramitar(this, AcaoTramitacao.FINALIZAR_ANALISE_SHAPE, null);
//        }

        if (this.analisesTecnicaManejo.size() == 0) {

            this.analisesTecnicaManejo = new ArrayList<>();
        }

        // Simulação de falha da análise
        if (this.numeroProcesso.startsWith("FALHA") && !this.revisaoSolicitada) {

            for (DocumentoShape documento : this.getAnaliseTecnica().documentosShape) {

                documento.delete();
            }

            this.revisaoSolicitada = true;
            this._save();

            tramitacao.tramitar(this, AcaoTramitacao.SOLICITAR_REVISAO_SHAPE, null);

        } else {

            this.getAnaliseTecnica().gerarAnalise();

            tramitacao.tramitar(this, AcaoTramitacao.FINALIZAR_ANALISE_SHAPE, null);
        }

    }

    public static boolean findByNumeroProcesso(String numeroProcesso) {

        Boolean existe = false;

        ProcessoManejo processo = ProcessoManejo.find("num_processo", numeroProcesso).first();

        if(processo != null){

            if(processo.numeroProcesso.equals(numeroProcesso)) {

                existe = true;
            }
        }
        return existe;
    }

    public AnaliseTecnicaManejo getAnaliseTecnica() {

        if (this.analisesTecnicaManejo.size() == 0) {

            return null;
        }

        return this.analisesTecnicaManejo.get(this.analisesTecnicaManejo.size() - 1);
    }

    public void indeferir(ProcessoManejo processoManejo, Usuario usuario) {

        this.justificativaIndeferimento = processoManejo.justificativaIndeferimento;

        tramitacao.tramitar(this, AcaoTramitacao.INDEFERIR_PROCESSO_MANEJO, usuario);

        this._save();
    }
}
