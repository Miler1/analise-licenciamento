package models.manejoDigital;

import builders.ProcessoManejoBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import deserializers.*;
import exceptions.AppException;
import exceptions.ValidacaoException;
import exceptions.WebServiceException;
import models.TipoDocumento;
import models.manejoDigital.analise.analiseShape.*;
import models.manejoDigital.analise.analiseTecnica.*;
import models.UsuarioAnalise;
import models.tramitacao.*;
import org.apache.commons.io.IOUtils;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.GenericModel;
import play.libs.WS.HttpResponse;
import security.Auth;
import security.InterfaceTramitavel;
import utils.Configuracoes;
import utils.Mensagem;
import utils.WebService;

import javax.persistence.*;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

import static models.TipoDocumento.*;

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

    public ProcessoManejo iniciarAnaliseShape(ProcessoManejo processo, String token) {

        this.analisesTecnicaManejo.add(processo.getAnaliseTecnica());
        this.getAnaliseTecnica().dataAnalise = new Date();
        this.getAnaliseTecnica().diasAnalise = 0;
        this.getAnaliseTecnica().analistaTecnico = new AnalistaTecnicoManejo(processo.getAnaliseTecnica(),
                (UsuarioAnalise) UsuarioAnalise.findById(Auth.getUsuarioSessao().id));
        this.getAnaliseTecnica().processoManejo = this;

        this.getAnaliseTecnica()._save();

        if (this.getAnaliseTecnica().documentosShape.size() > 3) {

            throw new ValidacaoException(Mensagem.DOCUMENTO_SHAPE_MANEJO_TAMANHO_MAXIMO_LISTA_EXCEDIDO);
        }

        if (!this.getAnaliseTecnica().isDocumentosShapeValidos()) {

            throw new ValidacaoException(Mensagem.DOCUMENTO_SHAPE_MANEJO_DOCUMENTOS_OBRIGATORIOS);
        }

        for(DocumentoShape documento : this.getAnaliseTecnica().documentosShape) {

            documento.analiseTecnicaManejo = this.getAnaliseTecnica();
            documento.tipo = TipoDocumento.findById(documento.tipo.id);
            documento.save();
        }

        this.enviarProcessoAnaliseShape(token);

        tramitacao.tramitar(this, AcaoTramitacao.INICIAR_ANALISE_SHAPE, this.getAnaliseTecnica().analistaTecnico.usuario);
        HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(this.idObjetoTramitavel), this.getAnaliseTecnica().analistaTecnico.usuario);

        this.revisaoSolicitada = false;
        this._save();

        return this.refresh();
    }

    public ProcessoManejo iniciarAnaliseTecnica() {

        tramitacao.tramitar(this, AcaoTramitacao.INICIAR_ANALISE_TECNICA_MANEJO, this.getAnaliseTecnica().analistaTecnico.usuario);
        HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(this.idObjetoTramitavel), this.getAnaliseTecnica().analistaTecnico.usuario);

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

        return new ProcessoManejoBuilder()
                .filtrarPorNumeroProcesso(filtro.numeroProcesso)
                .filtrarPorIdMunicipio(filtro.idMunicipioEmpreendimento)
                .filtrarPorCpfCnpjEmpreendimento(filtro.cpfCnpjEmpreendimento)
                .filtrarPorIdAtividade(filtro.idAtividade)
                .filtrarPorIdCondicao(filtro.idStatusLicenca)
                .filtrarPorIdTipoLicenca(filtro.idManejoDigital);
    }

    private void enviarProcessoAnaliseShape(String token) {

        Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy")
                .registerTypeAdapter(GeometriaArcgis.class, new GeometriaArcgisDeserializer())
                .create();

        List<GeometriaArcgis> featuresProcesso = this.montarGeometrias(this.getDocumentoManejoByTipo(AREA_DE_MANEJO_FLORESTAL_SOLICITADA), gson);
        List<GeometriaArcgis> featuresPropriedade = this.montarGeometrias(this.getDocumentoManejoByTipo(AREA_DA_PROPRIEDADE), gson);
        List<GeometriaArcgis> featuresAreaSemPotencial = this.montarGeometrias(this.getDocumentoManejoByTipo(AREA_SEM_POTENCIAL), gson);

        this.getAnaliseTecnica().objectId = this.enviarFeatures(featuresProcesso, Configuracoes.ANALISE_SHAPE_ADD_FEATURES_PROCESSOS_URL, token, gson);
        this.enviarFeatures(featuresPropriedade, Configuracoes.ANALISE_SHAPE_ADD_FEATURES_PROPRIEDADE_URL, token, gson);
        this.enviarFeatures(featuresAreaSemPotencial, Configuracoes.ANALISE_SHAPE_ADD_FEATURES_AREA_SEM_POTENCIAL_URL, token, gson);

        this._save();
    }

    private List<GeometriaArcgis> montarGeometrias(DocumentoShape documento, Gson gson) {

        if (documento == null) {

            return new ArrayList<>();
        }

        List<GeometriaArcgis> features = new ArrayList<>();
        Type listType = new TypeToken<ArrayList<GeometriaArcgis>>(){}.getType();

        features.addAll((ArrayList<GeometriaArcgis>) gson.fromJson(documento.geoJsonArcgis, listType));

        for (GeometriaArcgis feature : features) {

            feature.attributes = new AtributosAddLayer(this.numeroProcesso,
                    this.empreendimento.imovel.nome, 0, this.getAnaliseTecnica().analistaTecnico.usuario.login);
        }

        return features;
    }

    private DocumentoShape getDocumentoManejoByTipo(Long idTipo) {

        for (DocumentoShape documento : this.getAnaliseTecnica().documentosShape) {

            if (documento.tipo.id.equals(idTipo)) {

                return documento;
            }
        }

        return null;
    }

    private String enviarFeatures(List<GeometriaArcgis> features, String rota, String token, Gson gson) {

        if (features.size() == 0) {

            return null;
        }

        WebService webService = new WebService();

        Map<String, Object> params = new HashMap<>();

        params.put("token", token);
        params.put("features", gson.toJson(features).replace("rings\":\"", "rings\":").replace("]\",\"s", "],\"s"));
        params.put("f", "json");

        ResponseAddLayer response = webService.post(rota, params, ResponseAddLayer.class);

        if (response.error != null) {

            throw new WebServiceException("Erro ao enviar processo para análise: " + response.error.message);

        } else {

            return response.addResults.get(response.addResults.size() - 1).objectId;
        }
    }



    public void verificarAnaliseShape(String token) {

        WebService webService = new WebService(
                new GsonBuilder().setDateFormat("dd/MM/yyyy")
                        .registerTypeAdapter(AnaliseNdfi.class, new AnaliseNDFIDeserializer())
                        .registerTypeAdapter(AnaliseVetorial.class, new AnaliseVetorialDeserializer())
                        .registerTypeAdapter(Insumo.class, new InsumoDeserializer())
                        .registerTypeAdapter(BaseVetorial.class, new BaseVetorialDeserializer())
                        .registerTypeAdapter(FeatureAddLayer.class, new FeatureAddLayerDeserializer())
                        .registerTypeAdapter(AtributosQueryAMFManejo.class, new AtributosQueryAMFManejoDeserializer())
                        .serializeNulls()
                        .create()
        );

        Map<String, Object> params = new HashMap<>();
        params.put("token", token);
        params.put("objectIds", this.getAnaliseTecnica().objectId);
        params.put("outFields", "*");
        params.put("f", "json");

        ResponseQueryProcesso response = webService.post(Configuracoes.ANALISE_SHAPE_QUERY_PROCESSOS_URL, params, ResponseQueryProcesso.class);

        if (response.features.isEmpty()) {

            return;
        }

        if (response.features.get(0).attributes.status == 2) {

            params.clear();
            params.put("token", token);
            params.put("where", "protocolo = '" + this.numeroProcesso + "'");
            params.put("outFields", "*");
            params.put("f", "json");

            ResponseQuerySobreposicao responseSobreposicao =  webService.post(Configuracoes.ANALISE_SHAPE_QUERY_SOBREPOSICOES_URL, params, ResponseQuerySobreposicao.class);

            //ResponseQueryAMFManejo responseAMFManejo = webService.post(Configuracoes.ANALISE_SHAPE_QUERY_AMF_MANEJO_URL, params, ResponseQueryAMFManejo.class);

            params.remove("where");
            params.put("where", "processo = '" + this.numeroProcesso + "'");
            ResponseQueryInsumo responseInsumo = webService.post(Configuracoes.ANALISE_SHAPE_QUERY_INSUMOS_URL, params, ResponseQueryInsumo.class);

            params.remove("where");
            params.put("where", "processo = '" + this.numeroProcesso + "'");
            ResponseQueryResumoNDFI responseResumoNDFI = webService.post(Configuracoes.ANALISE_SHAPE_QUERY_RESUMO_NDFI_URL, params, ResponseQueryResumoNDFI.class);

            params.remove("where");
            params.put("where", "1 = 1");
            ResponseQueryMetadados responseQueryMetadados = webService.post(Configuracoes.ANALISE_SHAPE_QUERY_AMF_METADADOS_URL, params, ResponseQueryMetadados.class);

            this.getAnaliseTecnica().gerarCalculoAreaEfetiva();
            this.getAnaliseTecnica().setAnalisesVetoriais(responseSobreposicao.features);
            this.getAnaliseTecnica().setInsumos(responseInsumo.features);
            this.getAnaliseTecnica().setAnalisesNdfi(responseResumoNDFI.features);
            //this.getAnaliseTecnica().areaEfetivoNdfi = responseAMFManejo.features.get(0).attributes.area;
            this.getAnaliseTecnica().setDetalhamentoNdfi();
            this.getAnaliseTecnica().setBasesVetoriais(responseQueryMetadados.features);
            this.getAnaliseTecnica().setConsideracoes();
            this.getAnaliseTecnica().setEmbasamentos();

            this.getAnaliseTecnica()._save();

            params.clear();
            params.put("token", token);
            params.put("f", "json");

            String urlAnexos = Configuracoes.ANALISE_SHAPE_URL + "/1/"
                    + response.features.get(0).attributes.objectid + "/attachments";
            ResponseAnexoProcesso responseAnexoProcesso = webService.post(urlAnexos, params, ResponseAnexoProcesso.class);

            if(responseAnexoProcesso.attachmentInfos != null) {

                for(AttachmentInfo anexo : responseAnexoProcesso.attachmentInfos) {

                    DocumentoManejo documento = new DocumentoManejo();

                    HttpResponse responseAnexo = webService.post(urlAnexos + "/" + anexo.id + "?gdbVersion=1", params);
                    InputStream inputStream = responseAnexo.getStream();

                    // Por algum motivo o filename está dentro do header content-disposition
                    // TODO Alterar obtenção do nome do arquivo quando os headers forem separados
                    String contentDisposition = responseAnexo.getHeader("Content-Disposition");
                    String fileName = contentDisposition.substring(contentDisposition.indexOf("\"") + 1, contentDisposition.lastIndexOf("\""));

                    File tmp = new File(Configuracoes.APPLICATION_TEMP_FOLDER + "/" + fileName);
                    OutputStream outputStream = null;

                    try {

                        outputStream = new FileOutputStream(tmp);
                        IOUtils.copy(inputStream, outputStream);
                        outputStream.close();
                        documento.arquivo = tmp;

                    } catch (IOException e) {

                        e.printStackTrace();
                        throw new AppException(Mensagem.ERRO_PROCESSAR_ANEXO_PROCESSO_MANEJO);
                    }

                    documento.tipo = TipoDocumento.findById(TipoDocumento.ANEXO_PROCESSO_MANEJO_DIGITAL);
                    documento.analiseTecnicaManejo = this.getAnaliseTecnica();
                    documento.save();
                }
            }

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

    public void indeferir(ProcessoManejo processoManejo, UsuarioAnalise usuario) {

        if (!this.revisaoSolicitada) {

            throw new ValidacaoException(Mensagem.ERRO_PADRAO);
        }

        this.justificativaIndeferimento = processoManejo.justificativaIndeferimento;
        this.revisaoSolicitada = false;

        tramitacao.tramitar(this, AcaoTramitacao.INDEFERIR_PROCESSO_MANEJO_ANALISE_SHAPE, usuario);

        this._save();
    }
}
