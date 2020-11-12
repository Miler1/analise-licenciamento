package models.licenciamento;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import br.ufla.lemaf.beans.EmpreendimentoFiltroResult;
import br.ufla.lemaf.beans.FiltroEmpreendimento;
import models.Comunicado;
import models.Processo;
import org.geotools.feature.SchemaException;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import play.libs.Files;
import security.cadastrounificado.CadastroUnificadoWS;
import serializers.ComunicadoSerializer;
import services.IntegracaoEntradaUnicaService;
import utils.Configuracoes;
import utils.GeoJsonUtils;
import utils.Identificavel;
import utils.WriteShapefile;

import javax.persistence.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Entity
@Table(schema = "licenciamento", name = "caracterizacao")
public class Caracterizacao extends GenericModel implements Identificavel {

	private static final String SEQ = "licenciamento.caracterizacao_id_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;

	@Column(name = "numero")
	public String numero;

	@Column(name = "data_cadastro")
	public Date dataCadastro;

	@Column(name = "data_finalizacao")
	public Date dataFinalizacao;

	@ManyToOne
	@JoinColumn(name="id_empreendimento")
	public Empreendimento empreendimento;

	@ManyToOne
	@JoinColumn(name="id_status")
	public StatusCaracterizacao status;

	@ManyToOne
	@JoinColumn(name="id_tipo_licenca")
	public TipoLicenca tipoLicenca;

	@ManyToOne
	@JoinColumn(name="id_tipo")
	public TipoCaracterizacao tipo;
	
	@OneToOne(mappedBy = "caracterizacao")
	public DispensaLicenciamento dispensa;

	@OneToMany(mappedBy = "caracterizacao", cascade = CascadeType.ALL)
	public List<AtividadeCaracterizacao> atividadesCaracterizacao;

	@OneToMany(mappedBy = "caracterizacao", cascade = CascadeType.ALL)
	public List<SobreposicaoCaracterizacaoEmpreendimento> sobreposicoesCaracterizacaoEmpreendimento;

	@OneToMany(mappedBy = "caracterizacao", cascade = CascadeType.ALL)
	public List<SobreposicaoCaracterizacaoComplexo> sobreposicoesCaracterizacaoComplexo;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(schema = "licenciamento", name = "rel_caracterizacao_resposta",
			joinColumns = @JoinColumn(name = "id_caracterizacao"),
			inverseJoinColumns = @JoinColumn(name = "id_resposta"))
	public List<Resposta> respostas;

	@ManyToMany
	@JoinTable(schema = "licenciamento", name = "rel_tipo_licenca_caracterizacao_andamento",
			joinColumns = @JoinColumn(name = "id_caracterizacao"),
			inverseJoinColumns = @JoinColumn(name = "id_tipo_licenca"))
	public List<TipoLicenca> tiposLicencaEmAndamento;

	@OneToMany(mappedBy="caracterizacao", cascade = CascadeType.ALL)
	public List<SolicitacaoDocumentoCaracterizacao> solicitacoesDocumento;

	@OneToMany(mappedBy="caracterizacao", cascade = CascadeType.ALL)
	public List<SolicitacaoGrupoDocumento> documentosSolicitacaoGrupo;
	
	@Column(name="declaracao_veracidade_informacoes")
	public Boolean declaracaoVeracidadeInformacoes;

	@OneToOne(mappedBy="caracterizacao", targetEntity = Processo.class, orphanRemoval = true)
	public Processo processo;
	
	@OneToMany(mappedBy="caracterizacao")
	public List<Licenca> licencas;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "caracterizacao", orphanRemoval = true)
	public Questionario3 questionario3;

	@Column
	public Boolean complexo = false;

	@Column(name = "ativo")
	public Boolean ativo;

	@Column(name = "vigencia_solicitada")
	public Integer vigenciaSolicitada;

	@Column
	public boolean renovacao;

	@Column
	public boolean retificacao;

	@Column(name = "id_origem")
	public Long idCaracterizacaoOrigem;

	@ManyToOne
	@JoinColumn(name = "id_processo")
	public models.ProcessoLicenciamento processoLicenciamento;

	public enum OrigemSobreposicao {

		EMPREENDIMENTO,
		ATIVIDADE,
		COMPLEXO,
		SEM_SOBREPOSICAO

	}

	@Enumerated(EnumType.STRING)
	@Column(name="origem_sobreposicao")
	public OrigemSobreposicao origemSobreposicao;

	@OneToMany(mappedBy = "caracterizacao", cascade = CascadeType.ALL)
	public List<GeometriaComplexo> geometriasComplexo;

	@Column(name="valor_taxa_licenciamento")
	public Double valorTaxaLicenciamento;

	@Transient
	public Dae dae;
	
	@Transient
	public String linkTaxasLicenciamento;
	
	@Transient
	public Licenca licenca;
	
	@Override
	public Long getId() {
		
		return this.id;
	}
	
	public List<DocumentoLicenciamento> getDocumentosEnviados() {
		
		List<DocumentoLicenciamento> documentos = new ArrayList<>();
		
		for(SolicitacaoDocumentoCaracterizacao solicitacao : this.solicitacoesDocumento) {
			if(solicitacao.documento != null)
				documentos.add(solicitacao.documento);
		}
		
		return documentos;
		
	}
	
	public static void setStatusCaracterizacao(List<Long> idsCaracterizacoes, Long idNewStatusCaracterizacao) {
		
		StatusCaracterizacao newStatus = StatusCaracterizacao.findById(idNewStatusCaracterizacao);
		
		JPA.em().createQuery("UPDATE Caracterizacao SET status = :status WHERE id IN :idsCaracterizacoes")
			.setParameter("status", newStatus)
			.setParameter("idsCaracterizacoes", idsCaracterizacoes)
			.executeUpdate();
	}

	public static void setCaracterizacaoEmAnalise(List<Long> idsCaracterizacoes, boolean status) {

		JPA.em().createQuery("UPDATE Caracterizacao SET analise = :status WHERE id IN :idsCaracterizacoes")
			.setParameter("status", status)
			.setParameter("idsCaracterizacoes", idsCaracterizacoes)
			.executeUpdate();
	}

	public static void setCaracterizacaoEmRenovacao(List<Long> idsCaracterizacoes, boolean status) {

		JPA.em().createQuery("UPDATE Caracterizacao SET renovacao = :status WHERE id IN :idsCaracterizacoes")
			.setParameter("status", status)
			.setParameter("idsCaracterizacoes", idsCaracterizacoes)
			.executeUpdate();
	}
	
	public Licenca getLicenca() {
		
		if(this.licenca != null)
			return this.licenca;

		if(this.licenca == null && !this.licencas.isEmpty())
			for(Licenca licenca : this.licencas)
				if(licenca.ativo)
					this.licenca = licenca;

		return this.licenca;
	}

	public Licenca getLicencaAnterior() {

		if (this.licencas != null) {

			if (this.licencas.size() > 1) {

				return this.licencas.get(this.licencas.size() - 2);
			}

			return this.licencas.get(0);
		}

		return null;
	}
	
	public boolean isSuspensa() {
		return this.status.id.equals(StatusCaracterizacao.SUSPENSO);
	}
	
	public Boolean isCancelado() {
		return this.status.id.equals(StatusCaracterizacao.CANCELADO);
	}
	
	public Boolean isArquivada() {
		return this.status.id.equals(StatusCaracterizacao.ARQUIVADO);
	}

	public AtividadeCaracterizacao getAtividadeCaracterizacaoMaiorPotencialPoluidorEPorte() {

		AtividadeCaracterizacao atividadeCaracterizacaoMaiorPPDPorte = null;

		for (AtividadeCaracterizacao atividadeCaracterizacao : this.atividadesCaracterizacao) {

			if (atividadeCaracterizacaoMaiorPPDPorte == null) {
				atividadeCaracterizacaoMaiorPPDPorte = atividadeCaracterizacao;
			} else if (atividadeCaracterizacao.atividade.potencialPoluidor.compareTo(atividadeCaracterizacaoMaiorPPDPorte.atividade.potencialPoluidor) == 1) {
				atividadeCaracterizacaoMaiorPPDPorte = atividadeCaracterizacao;
			} else if (atividadeCaracterizacao.atividade.potencialPoluidor.compareTo(atividadeCaracterizacaoMaiorPPDPorte.atividade.potencialPoluidor) == 0) {
				// Caso PPD seja igual, comparar os portes.
				if (atividadeCaracterizacao.porteEmpreendimento.compareTo(atividadeCaracterizacaoMaiorPPDPorte.porteEmpreendimento) == 0
						|| atividadeCaracterizacao.porteEmpreendimento.compareTo(atividadeCaracterizacaoMaiorPPDPorte.porteEmpreendimento) == 1) {
					atividadeCaracterizacaoMaiorPPDPorte = atividadeCaracterizacao;
				}
			}

		}

		return atividadeCaracterizacaoMaiorPPDPorte;

	}

	public Boolean isComplexo() {

		return this.complexo;
		
	}

	private Boolean caracterizacaoAnteriorAtiva() {
		return this.idCaracterizacaoOrigem != null && ((Caracterizacao)findById(this.idCaracterizacaoOrigem)).ativo;
	}

	public Boolean isRenovacao() {
		return this.renovacao && this.caracterizacaoAnteriorAtiva();
	}

	public Boolean isRetificacao() {
		return this.retificacao && (this.idCaracterizacaoOrigem == null || !this.caracterizacaoAnteriorAtiva());
	}

	public File gerarShapeAtividade() throws IOException {

		if(this.isComplexo()){

			return this.gerarShapeComplexo();

		}

		return this.gerarShapeAtividades();

	}

	public File gerarShapeComplexo() throws IOException {

		return gerarShapeAtividade(this.geometriasComplexo.stream().map(gc -> gc.geometria).collect(toList()));

	}

	public File gerarShapeAtividades() throws IOException {

		return gerarShapeAtividade(this.atividadesCaracterizacao.stream().flatMap(ac -> ac.getGeoms()).collect(toList()));

	}

	public File gerarShapeEmpreendimento() throws IOException {

		IntegracaoEntradaUnicaService entradaUnicaService = new IntegracaoEntradaUnicaService();
		br.ufla.lemaf.beans.Empreendimento empreendimentosByCpfCnpj = entradaUnicaService.findEmpreendimentosByCpfCnpj(this.empreendimento.getCpfCnpj());
		Geometry geom = GeoJsonUtils.toGeometry(empreendimentosByCpfCnpj.localizacao.geometria);
		return gerarShape(Collections.singletonList(geom));

	}

	private File gerarShapeAtividade(List<Geometry> geoms) throws IOException {

		String basePath = Configuracoes.ARQUIVOS_DOCUMENTOS_ANALISE_PATH;
		String shapefilePAth = Configuracoes.ARQUIVOS_DOCUMENTOS_SHAPEFILE_PATH + File.separator + this.id;

		File shapeFile = new File(shapefilePAth + "/");
		File file = new File(shapefilePAth + "/geom_ativ.shp");
		File fileZip = new File(basePath + "/geom_ativ.zip");

		if(!shapeFile.exists()){
			shapeFile.mkdirs();
		}

		WriteShapefile.write(file, geoms, MultiPolygon.class);
		Files.zip(shapeFile, fileZip);

		Files.deleteDirectory(new File(Configuracoes.ARQUIVOS_DOCUMENTOS_SHAPEFILE_PATH));

		return fileZip;

	}

	private File gerarShape(List<Geometry> geoms) throws IOException {

		String basePath = Configuracoes.ARQUIVOS_DOCUMENTOS_ANALISE_PATH;
		String shapefilePAth = Configuracoes.ARQUIVOS_DOCUMENTOS_SHAPEFILE_PATH + File.separator + this.id;

		File shapeFile = new File(shapefilePAth + "/");
		File file = new File(shapefilePAth + "/geom_emp.shp");
		File fileZip = new File(basePath + "/geom_emp.zip");

		if(!shapeFile.exists()){
			shapeFile.mkdirs();
		}

		WriteShapefile.write(file, geoms, MultiPolygon.class);
		Files.zip(shapeFile, fileZip);

		Files.deleteDirectory(new File(Configuracoes.ARQUIVOS_DOCUMENTOS_SHAPEFILE_PATH));

		return fileZip;

	}
}
