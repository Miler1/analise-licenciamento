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

import models.Documento;
import play.db.jpa.GenericModel;
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

	@OneToOne(mappedBy = "caracterizacao", cascade = CascadeType.ALL)
	public AtividadeCaracterizacao atividadeCaracterizacao;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(schema = "licenciamento", name = "rel_caracterizacao_resposta",
			joinColumns = @JoinColumn(name = "id_caracterizacao"),
			inverseJoinColumns = @JoinColumn(name = "id_resposta"))
	public List<Resposta> respostas;

	@ManyToOne
	@JoinColumn(name="id_grupo")
	public GrupoCaracterizacao grupo;
	
	@OneToMany(mappedBy="caracterizacao", cascade = CascadeType.ALL, orphanRemoval=true)
	public List<TipoLicencaCaracterizacaoEmAndamento> tiposLicencaEmAndamento;

	@OneToMany(mappedBy="caracterizacao", cascade = CascadeType.ALL)
	public List<SolicitacaoDocumentoCaracterizacao> solicitacoesDocumento;
	
	@Column(name="declaracao_veracidade_informacoes")
	public Boolean declaracaoVeracidadeInformacoes;
	
	@Column(name="quantidade_daes_vencidos")
	public int quantidadeDaesVencidos;
	
	@Transient
	public Dae dae;
	
	@Transient
	public String linkTaxasLicenciamento;
	
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
}