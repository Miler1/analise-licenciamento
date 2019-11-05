package models.licenciamento;

import models.Processo;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import utils.Identificavel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(schema = "licenciamento", name = "caracterizacao")
public class Caracterizacao extends GenericModel implements Identificavel {

	private static final String SEQ = "licenciamento.caracterizacao_id_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;

	@Column(name = "numero_processo")
	public String numeroProcesso;

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

	@ManyToOne
	@JoinColumn(name="id_grupo")
	public GrupoCaracterizacao grupo;

	@ManyToMany
	@JoinTable(schema = "licenciamento", name = "rel_tipo_licenca_caracterizacao_andamento",
			joinColumns = @JoinColumn(name = "id_caracterizacao"),
			inverseJoinColumns = @JoinColumn(name = "id_tipo_licenca"))
	public List<TipoLicenca> tiposLicencaEmAndamento;

	@OneToMany(mappedBy="caracterizacao", cascade = CascadeType.ALL)
	public List<SolicitacaoDocumentoCaracterizacao> solicitacoesDocumento;
	
	@Column(name="declaracao_veracidade_informacoes")
	public Boolean declaracaoVeracidadeInformacoes;

	@OneToOne(mappedBy="caracterizacao")
	public Processo processo;
	
	@OneToMany(mappedBy="caracterizacao")
	public List<Licenca> licencas;

	@Column
	public boolean renovacao;

	@Column(name = "numero_processo_antigo")
	public String numeroProcessoAntigo;

	public enum OrigemSobreposicao {

		EMPREENDIMENTO,
		ATIVIDADE,
		COMPLEXO,
		SEM_SOBREPOSICAO;

	}

	@Enumerated(EnumType.STRING)
	@Column(name="origem_sobreposicao")
	public OrigemSobreposicao origemSobreposicao;

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
}