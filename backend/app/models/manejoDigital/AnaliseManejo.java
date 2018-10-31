package models.manejoDigital;

import models.Documento;
import models.TipoDocumento;
import models.pdf.PDFGenerator;
import models.portalSeguranca.Setor;
import models.portalSeguranca.UsuarioLicenciamento;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;
import play.data.Upload;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.libs.IO;
import utils.Configuracoes;
import utils.FileManager;

import javax.persistence.*;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

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

    @Required
    @Column
    public String consideracoes;

    @Required
    @Column
    public String conclusao;

    @Required
    @OneToOne
    @JoinColumn(name="id_usuario")
    public UsuarioLicenciamento usuario;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="id_documento")
    public Documento documentoAnalise;

    @Required
    @OneToMany(mappedBy = "analiseManejo")
    public List<Observacao> observacoes;

    @Required
    @OneToOne(mappedBy = "analiseManejo")
    public ProcessoManejo processoManejo;

    @Required
    @OneToMany(mappedBy = "analiseManejo", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<AnaliseNdfi> analisesNdfi;

    @Required
    @OneToMany(mappedBy = "analiseManejo", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<AnaliseVetorial> analisesVetorial;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(schema = "analise", name = "rel_base_vetorial_analise_manejo",
            joinColumns = @JoinColumn(name = "id_analise_manejo"),
            inverseJoinColumns = @JoinColumn(name = "id_base_vetorial"))
    public List<BaseVetorial> basesVetorial;

    public static AnaliseManejo gerarAnalise(ProcessoManejo processo, UsuarioLicenciamento usuario) {

        AnaliseManejo analiseManejo = new AnaliseManejo();

        analiseManejo.dataAnalise = new Date();

        analiseManejo.diasAnalise = 0;

        analiseManejo.pathShape = processo.analiseManejo.pathShape;

        analiseManejo.analiseTemporal = UUID.randomUUID().toString().replace('-', ' ');

        analiseManejo.areaManejoFlorestalSolicitada = Math.random();

        analiseManejo.areaPreservacaoPermanente = Math.random();

        analiseManejo.areaServidao = Math.random();

        analiseManejo.areaConsolidada = Math.random();

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

        analiseManejo.consideracoes =  UUID.randomUUID().toString().replace('-', ' ');

        analiseManejo.conclusao =  UUID.randomUUID().toString().replace('-', ' ');

        analiseManejo.usuario = usuario;

        analiseManejo.analisesNdfi = AnaliseNdfi.gerarAnaliseNfid(analiseManejo);

        analiseManejo.basesVetorial = BaseVetorial.gerarBaseVetorial(analiseManejo);

        analiseManejo.analisesVetorial = AnaliseVetorial.gerarAnalisesVetoriais(analiseManejo);

        return analiseManejo;
    }

    public String saveAnexo(Upload file) throws IOException {

        byte[] data = IO.readContent(file.asFile());
        String extension = FileManager.getInstance().getFileExtention(file.getFileName());
        String path = FileManager.getInstance().createFile(Configuracoes.APPLICATION_ANEXO_MANEJO_FOLDER, file.getFileName(),
                data, extension);

        this.pathAnexo = path;
        this._save();

        return path;
    }

    public void deleteAnexo() {

        FileManager.getInstance().deleteFileFromPath(this.pathAnexo);
        this.pathAnexo = null;
        this._save();
    }

    public List<Observacao> getObservacoesDadosImovel() {

        return Observacao.find("analiseManejo.id = :x AND passoAnalise = 0 ORDER BY id")
                .setParameter("x", this.id)
                .fetch();
    }

    public List<Observacao> getObservacoesBaseVetorial() {

        return Observacao.find("analiseManejo.id = :x AND passoAnalise = 1 ORDER BY id")
                .setParameter("x", this.id)
                .fetch();
    }

    public List<Observacao> getObservacoesAnaliseVetorial() {

        return Observacao.find("analiseManejo.id = :x AND passoAnalise = 2 ORDER BY id")
                .setParameter("x", this.id)
                .fetch();
    }

    public List<Observacao> getObservacoesAnaliseTemporal() {

        return Observacao.find("analiseManejo.id = :x AND passoAnalise = 3 ORDER BY id")
                .setParameter("x", this.id)
                .fetch();
    }

    public List<Observacao> getObservacoesInsumosUtilizados() {

        return Observacao.find("analiseManejo.id = :x AND passoAnalise = 4 ORDER BY id")
                .setParameter("x", this.id)
                .fetch();
    }

    public List<Observacao> getObservacoesCalculoNDFI() {

        return Observacao.find("analiseManejo.id = :x AND passoAnalise = 5 ORDER BY id")
                .setParameter("x", this.id)
                .fetch();
    }

    public List<Observacao> getObservacoesCalculoAreaEfetiva() {

        return Observacao.find("analiseManejo.id = :x AND passoAnalise = 6 ORDER BY id")
                .setParameter("x", this.id)
                .fetch();
    }

    public List<Observacao> getObservacoesDetalhamentoAreaEfetiva() {

        return Observacao.find("analiseManejo.id = :x AND passoAnalise = 7 ORDER BY id")
                .setParameter("x", this.id)
                .fetch();
    }

    public List<Observacao> getObservacoesConsideracoes() {

        return Observacao.find("analiseManejo.id = :x AND passoAnalise = 8 ORDER BY id")
                .setParameter("x", this.id)
                .fetch();
    }

    public List<Observacao> getObservacoesConclusao() {

        return Observacao.find("analiseManejo.id = :x AND passoAnalise = 9 ORDER BY id")
                .setParameter("x", this.id)
                .fetch();
    }

    public void finalizar() {

        Random random = new Random();

        // Simulação do resultado da análise feita pela Vega
        if (random.nextBoolean()) {

            this.processoManejo.tramitacao.tramitar(this.processoManejo, AcaoTramitacao.DEFERIR_ANALISE_TECNICA_MANEJO, this.usuario);
            Setor.setHistoricoTramitacao(HistoricoTramitacao.getUltimaTramitacao(this.processoManejo.idObjetoTramitavel), this.usuario);

        } else {

            this.processoManejo.tramitacao.tramitar(this.processoManejo, AcaoTramitacao.INDEFERIR_ANALISE_TECNICA_MANEJO, this.usuario);
            Setor.setHistoricoTramitacao(HistoricoTramitacao.getUltimaTramitacao(this.processoManejo.idObjetoTramitavel), this.usuario);
        }
    }

    public Documento gerarPDFAnalise() throws Exception {

        TipoDocumento tipoDocumento = TipoDocumento.findById(TipoDocumento.DOCUMENTO_ANALISE_MANEJO);

        Double totalAnaliseNDFI = Double.valueOf(0);

        String nomeAnexo = null;

        if(this.pathAnexo != null){

            nomeAnexo = this.pathAnexo.substring(this.pathAnexo.lastIndexOf(System.getProperty("file.separator"))+1,this.pathAnexo.length());
        }

        for(AnaliseNdfi analiseNdfi : this.analisesNdfi) {

            totalAnaliseNDFI += analiseNdfi.area;
        }


        PDFGenerator pdf = new PDFGenerator()
                .setTemplate(tipoDocumento.getPdfTemplate())
                .addParam("nomeAnexo", nomeAnexo)
                .addParam("totalAnaliseNDFI", totalAnaliseNDFI)
                .addParam("analiseManejo", this)
                .addParam("processoManejo", this.processoManejo)
                .setPageSize(21.0D, 30.0D, 1.0D, 1.0D, 1.5D, 3.5D);

        pdf.generate();

        Documento documento = new Documento(tipoDocumento, pdf.getFile());

        return documento;

    }
}