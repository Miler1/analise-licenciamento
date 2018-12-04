package models.manejoDigital.analise.analiseTecnica;

import models.Documento;
import models.TipoDocumento;
import models.manejoDigital.DocumentoManejo;
import models.manejoDigital.DocumentoShape;
import models.manejoDigital.PassoAnaliseManejo;
import models.manejoDigital.ProcessoManejo;
import models.pdf.PDFGenerator;
import models.portalSeguranca.Setor;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;
import org.apache.tika.Tika;
import play.data.Upload;
import play.data.validation.Max;
import play.data.validation.Min;
import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.io.IOException;
import java.util.*;

import static models.TipoDocumento.*;

@Entity
@Table(schema = "analise", name = "analise_tecnica_manejo")
public class AnaliseTecnicaManejo extends GenericModel {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="analise.analise_tecnica_manejo_id_seq")
    @SequenceGenerator(name="analise.analise_tecnica_manejo_id_seq", sequenceName="analise.analise_tecnica_manejo_id_seq", allocationSize=1)
    public Long id;

    @Required
    @Column(name="data")
    public Date dataAnalise;

    @Required
    @Column(name="dias_analise")
    public Integer diasAnalise;

    @Column(name="area_manejo_florestal_solicitada")
    public Double areaManejoFlorestalSolicitada;

    @Column(name="area_preservacao_permanente")
    public Double areaPreservacaoPermanente;

    @Column(name="area_servidao")
    public Double areaServidao;

    @Column(name="area_antropizada_nao_consolidada")
    public Double areaAntropizadaNaoConsolidada;

    @Column(name="area_consolidada")
    public Double areaConsolidada;

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

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="id_documento")
    public Documento documentoAnalise;

    @OneToMany(mappedBy = "analiseTecnicaManejo")
    public List<Observacao> observacoes;

    @OrderBy("dataAnalise DESC")
    @OneToMany(mappedBy = "analiseTecnicaManejo", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<AnaliseNdfi> analisesNdfi;

    @OrderBy("id")
    @OneToMany(mappedBy = "analiseTecnicaManejo", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<AnaliseVetorial> analisesVetorial;

    @OrderBy("ultimaAtualizacao DESC")
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(schema = "analise", name = "rel_base_vetorial_analise_manejo",
            joinColumns = @JoinColumn(name = "id_analise_tecnica_manejo"),
            inverseJoinColumns = @JoinColumn(name = "id_base_vetorial"))
    public List<BaseVetorial> basesVetorial;

    @Column(name = "object_id")
    public String objectId;

    @Required
    @Min(2)
    @Max(3)
    @OneToMany(mappedBy = "analiseTecnicaManejo", orphanRemoval = true)
    public List<DocumentoShape> documentosShape;

    @Required
    @OneToOne(mappedBy = "analiseTecnicaManejo", cascade = CascadeType.ALL, orphanRemoval =  true)
    public AnalistaTecnicoManejo analistaTecnico;

    @Required
    @ManyToOne
    @JoinColumn(name = "id_processo_manejo")
    public ProcessoManejo processoManejo;

    @Max(2)
    @OneToMany(mappedBy = "analiseTecnicaManejo", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<DocumentoManejo> documentosManejo;

    @OneToMany(mappedBy = "analiseTecnicaManejo", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<VinculoAnaliseTecnicaManejoInsumo> vinculoInsumos;

    @Override
    public AnaliseTecnicaManejo save() {

        this.dataAnalise = new Date();

        this.diasAnalise = 0;

        this._save();

        return this.refresh();
    }

	public AnaliseTecnicaManejo updateExibirPdf(AnaliseTecnicaManejo novaAnalise, PassoAnaliseManejo passo) {

    	switch (passo) {
			case CALCULO_NDFI:

				this.updateExibirPdfCalculoNDFI(novaAnalise.analisesNdfi);
				break;

			case ANALISE_VETORIAL:

				this.updateExibirPdfAnalisesVetorial(novaAnalise.analisesVetorial);
				break;

			case INSUMOS_UTILIZADOS:

				this.updateExibirPdfInsumos(novaAnalise.vinculoInsumos);
				break;

		}
		return this.refresh();
	}


	private void updateExibirPdfCalculoNDFI(List<AnaliseNdfi> novasAnalisesNdfi) {

		Map<Long, Boolean> exibirPdfMap = new HashMap<Long, Boolean>();

		for (AnaliseNdfi novaAnaliseNdfi : novasAnalisesNdfi) {

			exibirPdfMap.put(novaAnaliseNdfi.id, novaAnaliseNdfi.exibirPDF);
		}

		for (AnaliseNdfi analiseNdfi : this.analisesNdfi) {

			analiseNdfi.exibirPDF = exibirPdfMap.get(analiseNdfi.id);
		}

		this._save();
	}

	private void updateExibirPdfAnalisesVetorial(List<AnaliseVetorial> novasAnalisesVetorial) {

		Map<Long, Boolean> exibirPdfMap = new HashMap<Long, Boolean>();

		for (AnaliseVetorial novaAnaliseVetorial : novasAnalisesVetorial) {

			exibirPdfMap.put(novaAnaliseVetorial.id, novaAnaliseVetorial.exibirPDF);
		}

		for (AnaliseVetorial analiseVetorial : this.analisesVetorial) {

			analiseVetorial.exibirPDF = exibirPdfMap.get(analiseVetorial.id);
		}

		this._save();
	}

	private void updateExibirPdfInsumos(List<VinculoAnaliseTecnicaManejoInsumo> novosInsumos) {

		Map<Long, Boolean> exibirPdfMap = new HashMap<Long, Boolean>();

		for (VinculoAnaliseTecnicaManejoInsumo novoInsumo : novosInsumos) {

			exibirPdfMap.put(novoInsumo.id, novoInsumo.exibirPDF);
		}

		for (VinculoAnaliseTecnicaManejoInsumo vinculoInsumo : this.vinculoInsumos) {

			vinculoInsumo.exibirPDF = exibirPdfMap.get(vinculoInsumo.id);
		}

		this._save();
	}

    public AnaliseTecnicaManejo gerarAnalise() {

        this.areaManejoFlorestalSolicitada = Math.random();

        this.areaPreservacaoPermanente = Math.random();

        this.areaServidao = Math.random();

        this.areaConsolidada = Math.random();

        this.areaAntropizadaNaoConsolidada = Math.random();

        this.areaUsoRestrito = Math.random();

        this.areaSemPotencial = Math.random();

        this.areaCorposAgua = Math.random();

        this.areaEmbargadaIbama = Math.random();

        this.areaEfetivoNdfi = Math.random();

        this.areaEmbargadaLdi = Math.random();

        this.areaSeletivaNdfi = Math.random();

        this.areaExploracaoNdfiBaixo = Math.random();

        this.areaExploracaoNdfiMedio = Math.random();

        this.areaSemPreviaExploracao = Math.random();

        this.analisesNdfi.addAll(AnaliseNdfi.gerarAnaliseNfid(this));

        this.basesVetorial.addAll(BaseVetorial.gerarBaseVetorial(this));

        this.analisesVetorial.addAll(AnaliseVetorial.gerarAnalisesVetoriais(this));

        VinculoAnaliseTecnicaManejoInsumo.gerarVinculos(this);

        this._save();

        return this.refresh();
    }

    public DocumentoManejo saveDocumentoImovel(Upload file, Long idTipoDocumento) throws IOException {

        DocumentoManejo documento = new DocumentoManejo();
        documento.arquivo = file.asFile();
        documento.tipo = TipoDocumento.findById(idTipoDocumento);
        documento.analiseTecnicaManejo = this;

        return (DocumentoManejo) documento.save();
    }

    public DocumentoManejo saveDocumentoComplementar(Upload file) throws IOException {

        DocumentoManejo documento = new DocumentoManejo();
        documento.arquivo = file.asFile();
        documento.tipo = TipoDocumento.findById(DOCUMENTO_COMPLEMENTAR_MANEJO);
        documento.analiseTecnicaManejo = this;

        return (DocumentoManejo) documento.save();
    }

    public List<Observacao> getObservacoesDadosImovel() {

        return Observacao.find("analiseTecnicaManejo.id = :x AND passoAnalise = 0 ORDER BY id")
                .setParameter("x", this.id)
                .fetch();
    }

    public List<Observacao> getObservacoesBaseVetorial() {

        return Observacao.find("analiseTecnicaManejo.id = :x AND passoAnalise = 1 ORDER BY id")
                .setParameter("x", this.id)
                .fetch();
    }

    public List<Observacao> getObservacoesAnaliseVetorial() {

        return Observacao.find("analiseTecnicaManejo.id = :x AND passoAnalise = 2 ORDER BY id")
                .setParameter("x", this.id)
                .fetch();
    }

    public List<Observacao> getObservacoesAnaliseTemporal() {

        return Observacao.find("analiseTecnicaManejo.id = :x AND passoAnalise = 3 ORDER BY id")
                .setParameter("x", this.id)
                .fetch();
    }

    public List<Observacao> getObservacoesInsumosUtilizados() {

        return Observacao.find("analiseTecnicaManejo.id = :x AND passoAnalise = 4 ORDER BY id")
                .setParameter("x", this.id)
                .fetch();
    }

    public List<Observacao> getObservacoesCalculoNDFI() {

        return Observacao.find("analiseTecnicaManejo.id = :x AND passoAnalise = 5 ORDER BY id")
                .setParameter("x", this.id)
                .fetch();
    }

    public List<Observacao> getObservacoesCalculoAreaEfetiva() {

        return Observacao.find("analiseTecnicaManejo.id = :x AND passoAnalise = 6 ORDER BY id")
                .setParameter("x", this.id)
                .fetch();
    }

    public List<Observacao> getObservacoesDetalhamentoAreaEfetiva() {

        return Observacao.find("analiseTecnicaManejo.id = :x AND passoAnalise = 7 ORDER BY id")
                .setParameter("x", this.id)
                .fetch();
    }

    public List<Observacao> getObservacoesConsideracoes() {

        return Observacao.find("analiseTecnicaManejo.id = :x AND passoAnalise = 8 ORDER BY id")
                .setParameter("x", this.id)
                .fetch();
    }

    public List<Observacao> getObservacoesDocumentosComplementares() {

        return Observacao.find("analiseTecnicaManejo.id = :x AND passoAnalise = 9 ORDER BY id")
                .setParameter("x", this.id)
                .fetch();
    }

	public List<Observacao> getObservacoesConclusao() {

		return Observacao.find("analiseTecnicaManejo.id = :x AND passoAnalise = 10 ORDER BY id")
				.setParameter("x", this.id)
				.fetch();
	}


    public void finalizar() {

        this.processoManejo.tramitacao.tramitar(this.processoManejo, AcaoTramitacao.DEFERIR_PROCESSO_MANEJO, this.analistaTecnico.usuario);
        Setor.setHistoricoTramitacao(HistoricoTramitacao.getUltimaTramitacao(this.processoManejo.idObjetoTramitavel), this.analistaTecnico.usuario);
    }

    public Documento gerarPDFAnalise() throws Exception {

        TipoDocumento tipoDocumento = TipoDocumento.findById(TipoDocumento.DOCUMENTO_ANALISE_MANEJO);

        Double totalAnaliseNDFI = Double.valueOf(0);

        String nomeAnexo = null;

//        if(this.pathAnexo != null){
//
//            nomeAnexo = this.pathAnexo.substring(this.pathAnexo.lastIndexOf(System.getProperty("file.separator"))+1,this.pathAnexo.length());
//        }

        for(AnaliseNdfi analiseNdfi : this.analisesNdfi) {

            totalAnaliseNDFI += analiseNdfi.area;
        }


        PDFGenerator pdf = new PDFGenerator()
                .setTemplate(tipoDocumento.getPdfTemplate())
                .addParam("nomeAnexo", nomeAnexo)
                .addParam("totalAnaliseNDFI", totalAnaliseNDFI)
                .addParam("analiseTecnicaManejo", this)
                .addParam("processoManejo", this.processoManejo)
                .addParam("arquivosComplementares", this.getArquivosComplementaresImagens())
                .setPageSize(21.0D, 30.0D, 1.0D, 1.0D, 1.5D, 3.5D);

        pdf.generate();

        Documento documento = new Documento(tipoDocumento, pdf.getFile());

        return documento;

    }

//    public void setAnalisesVetoriais(List<FeatureQuerySobreposicao> features) {
//
//        this.analisesVetorial = new ArrayList<>();
//
//        for (FeatureQuerySobreposicao feature : features) {
//
//            feature.attributes.analiseTecnicaManejo = this;
//            this.analisesVetorial.add(feature.attributes);
//        }
//    }
//
//    public void setInsumos(List<FeatureQueryInsumo> features) {
//
//        this.insumos = new ArrayList<>();
//
//        for (FeatureQueryInsumo feature : features) {
//
//            this.insumos.add(feature.attributes);
//        }
//    }
//
//    public void setAnalisesNdfi(List<FeatureQueryResumoNDFI> features) {
//
//        this.analisesNdfi = new ArrayList<>();
//
//        for (FeatureQueryResumoNDFI feature : features) {
//
//            feature.attributes.analiseTecnicaManejo = this;
//            this.analisesNdfi.add(feature.attributes);
//        }
//    }

    public List<DocumentoManejo> getDocumentosImovel() {

        return DocumentoManejo.find("(tipo.id = :x OR tipo.id = :z) AND analiseTecnicaManejo.id = :y")
                .setParameter("x", TERMO_DELIMITACAO_AREA_RESERVA_LEGAL_APROVADA)
                .setParameter("z", TERMO_AJUSTAMENTO_CONDUTA)
                .setParameter("y", this.id)
                .fetch();
    }

    public List<DocumentoManejo> getDocumentosComplementares() {

        return DocumentoManejo.find("tipo.id = :x AND analiseTecnicaManejo.id = :y")
                .setParameter("x", DOCUMENTO_COMPLEMENTAR_MANEJO)
                .setParameter("y", this.id)
                .fetch();
    }

    public List<VinculoAnaliseTecnicaManejoInsumo> getVinculos() {

        return VinculoAnaliseTecnicaManejoInsumo.find("analiseTecnicaManejo.id = :x ORDER BY insumo.data ASC")
                .setParameter("x", this.id)
                .fetch();
    }

    public boolean isDocumentosShapeValidos() {

        boolean hasAMF = false;
        boolean hasAPP = false;

        for (DocumentoShape documento : this.documentosShape) {

            if (documento.tipo.id.equals(TipoDocumento.AREA_DE_MANEJO_FLORESTAL_SOLICITADA)) {

                hasAMF = true;

            } else if (documento.tipo.id.equals(TipoDocumento.AREA_DE_PRESERVACAO_PERMANENTE)) {

                hasAPP = true;
            }
        }

        return  hasAMF && hasAPP;
    }

    public List<DocumentoManejo> getArquivosComplementaresImagens() throws IOException {

        List<DocumentoManejo> documentos = new ArrayList<>();

        Tika tika = new Tika();

        for (DocumentoManejo documento : this.getDocumentosComplementares()) {

            String realType = tika.detect(documento.getFile());

            if(realType.contains("image/jpeg") ||
                    realType.contains("image/jpg") ||
                    realType.contains("image/png") ||
                    realType.contains("bmp")) {

                documentos.add(documento);
            }
        }

        return documentos;
    }
}