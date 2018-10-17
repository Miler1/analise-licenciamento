package models.licenciamento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import models.Processo;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import utils.Identificavel;

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
	
	@Column(name="quantidade_daes_vencidos")
	public int quantidadeDaesVencidos;
	
	@ManyToMany(mappedBy="caracterizacoes")
	public List<Processo> processos;	
	
	@OneToMany(mappedBy="caracterizacao")
	public List<Licenca> licencas;

	@Column
	public boolean renovacao;

	@Column(name = "numero_processo_antigo")
	public String numeroProcessoAntigo;

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

	public static void setLicencaAnteriorInativa(Caracterizacao caracterizacao) {

		List<Licenca> licencasAntigas = Licenca.find("caracterizacao.id = ? AND ativo = TRUE ORDER BY dataCadastro", caracterizacao.id).fetch();

		if (licencasAntigas.size() > 0) {

			licencasAntigas.remove(licencasAntigas.size() - 1);

			for (Licenca licenca : licencasAntigas) {

				licenca.ativo = false;
				licenca._save();
			}
		}
	}

	public static void setLicencaFimProrrogacao(Caracterizacao caracterizacao) {

		List<Licenca> licencasAntigas = Licenca.find("caracterizacao.id = ? AND ativo = TRUE ORDER BY dataCadastro", caracterizacao.id).fetch();

		if (licencasAntigas.size() > 0) {

			licencasAntigas.remove(licencasAntigas.size() - 1);

			for (Licenca licenca : licencasAntigas) {

				licenca.prorrogacao = false;
				licenca._save();
			}
		}
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