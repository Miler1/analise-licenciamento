package models;

import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

import builders.ProcessoBuilder;
import builders.ProcessoBuilder.FiltroProcesso;
import models.licenciamento.Caracterizacao;
import models.licenciamento.Empreendimento;
import models.portalSeguranca.Usuario;
import models.tramitacao.AcaoDisponivelObjetoTramitavel;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.Condicao;
import models.tramitacao.ObjetoTramitavel;
import models.tramitacao.Tramitacao;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import security.Auth;
import security.InterfaceTramitavel;
import security.UsuarioSessao;
import utils.Configuracoes;

@Entity
@Table(schema="analise", name="processo")
public class Processo extends GenericModel implements InterfaceTramitavel{
	
	private static final String SEQ = "analise.processo_id_seq";
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;
	
	@Required
	public String numero;
	
	@Required
	@ManyToOne
	@JoinColumn(name="id_empreendimento")
	public Empreendimento empreendimento;
	
	@Column(name = "id_objeto_tramitavel")
	public Long idObjetoTramitavel;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_objeto_tramitavel", referencedColumnName = "id_objeto_tramitavel", insertable=false, updatable=false)
	public ObjetoTramitavel objetoTramitavel;
	
	@ManyToMany
	@JoinTable(schema="analise", name="rel_processo_caracterizacao", 
		joinColumns= @JoinColumn(name="id_processo"),
		inverseJoinColumns = @JoinColumn(name="id_caracterizacao"))
	public List<Caracterizacao> caracterizacoes;
	
	@OneToMany(mappedBy="processo")
	public List<Analise> analises;

	@Transient
	public transient Tramitacao tramitacao = new Tramitacao();
	
	@Override
	public Processo save() {

		// Inicia a tramitacao e chama o metodo salvaObjetoTramitavel() que cria o Processo ja com o objeto tramitavel setado que eh obrigatorio.
		tramitacao.iniciar(this, null, Tramitacao.LICENCIAMENTO_AMBIENTAL);
			
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
	
	public void vincularConsultor(Usuario consultor, Usuario usuarioExecutor) {
		
		ConsultorJuridico.vincularAnalise(consultor, AnaliseJuridica.findByProcesso(this));
		
		tramitacao.tramitar(this, AcaoTramitacao.VINCULAR, usuarioExecutor);
		
	}

	private static ProcessoBuilder commonFilterProcesso(FiltroProcesso filtro, Long idUsuarioLogado ) {
		
		return new ProcessoBuilder()
			.filtrarPorNumeroProcesso(filtro.numeroProcesso)
			.filtrarPorIdMunicipio(filtro.idMunicipioEmpreendimento)
			.filtrarPorCpfCnpjEmpreendimento(filtro.cpfCnpjEmpreendimento)
			.filtrarPorIdTipologia(filtro.idTipologiaEmpreendimento)
			.filtrarPorIdAtividade(filtro.idAtividadeEmpreendimento)
			.filtrarPorIdCondicao(filtro.idCondicaoTramitacao)
			.filtrarPorIdConsultorJuridico(getIdConsultorJuridico(filtro.idCondicaoTramitacao, idUsuarioLogado))
			.filtrarAnaliseJuridicaAtiva();
	}
	
	private static Long getIdConsultorJuridico(Long idCondicaoTramitacao, Long idUsuarioLogado) {
		
		if (idCondicaoTramitacao.equals(Condicao.AGUARDANDO_ANALISE_JURIDICA) || idCondicaoTramitacao.equals(Condicao.EM_ANALISE_JURIDICA)) {
			
			return idUsuarioLogado;
		}
		
		return null;
	}

	public static List listWithFilter(FiltroProcesso filtro, UsuarioSessao usuarioSessao) {
		
		return commonFilterProcesso(filtro, usuarioSessao.id)
			.comTiposLicencas()
			.groupByIdProcesso()
			.groupByNumeroProcesso()
			.groupByCpfCnpjEmpreendimento()
			.groupByDenominacaoEmpreendimento()
			.groupByMunicipioEmpreendimento()			
			.groupByDataVencimentoPrazoAnalise()
			.groupByDataVencimentoPrazoAnaliseJuridica()
			.groupByRevisaoSolicitadaAnaliseJuridica()
			.orderByDataVencimentoPrazoAnaliseJuridica()
			.fetch(filtro.paginaAtual.intValue(), filtro.itensPorPagina.intValue())
			.list();	
	}
	
	public static Long countWithFilter(FiltroProcesso filtro, UsuarioSessao usuarioSessao) {
		
		Object qtdeTotalItens = commonFilterProcesso(filtro, usuarioSessao.id)
			.addPessoaEmpreendimentoAlias()
			.addEstadoEmpreendimentoAlias()
			.addAnaliseAlias()
			.addAnaliseJuridicaAlias()
			.count()
			.unique();
		
		return ((Map<String, Long>) qtdeTotalItens).get("total"); 
	}

}
