package models.licenciamento;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import builders.ProcessoBuilder;
import builders.LicencaBuilder;
import builders.LicencaBuilder.FiltroLicenca;
import builders.ProcessoBuilder.FiltroProcesso;
import models.Documento;
import play.db.jpa.GenericModel;
import security.UsuarioSessao;

@Entity
@Table(schema = "licenciamento", name = "licenca")
public class Licenca extends GenericModel {

	private static final String SEQ = "licenciamento.licenca_id_seq";
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;
	
	@OneToOne
	@JoinColumn(name = "id_caracterizacao", referencedColumnName = "id", nullable = false)
	public Caracterizacao caracterizacao;
	
	@Column(name = "data_cadastro")
	public Date dataCadastro;
	
	@OneToOne
	@JoinColumn(name = "id_documento")
	public Documento documento;
	
	public String numero;
	
	@Column(name = "data_validade")
	public Date dataValidade;
	
	
	private static List groupByLicenca(LicencaBuilder licencaBuilder, FiltroLicenca filtro) {
		
		licencaBuilder.groupByIdLicenca()
		.groupByNumeroLicenca()
		.groupByNumeroProcesso()
		.groupByCpfCnpjEmpreendimento()
		.groupByDenominacaoEmpreendimento()
		.groupByMunicipioEmpreendimento()
		.groupByLicenca();
				
	return licencaBuilder
		.fetch(filtro.paginaAtual.intValue(), filtro.itensPorPagina.intValue())				
		.list();		
	}
	
	/**
	 * Filtro utilizado para a pesquisa avançada, ou seja, vários campos de pesquisa
	 * @param filtro
	 * @return
	 */
	private static LicencaBuilder commonFiltersLicenca(FiltroLicenca filtro) {
		
		return new LicencaBuilder()
				.filtrarPorNumeroLicenca(filtro.numeroLicenca)
				.filtrarPorNumeroProcesso(filtro.numeroProcesso)
				.filtrarPorCpfCnpjEmpreendimento(filtro.cpfCnpjEmpreendimento)
				.filtrarPorIdAtividade(filtro.idAtividadeEmpreendimento)
				.filtrarPorDenominacaoEmpreendimento(filtro.denominacaoEmpreendimento)
				.filtrarPorIdMunicipio(filtro.idMunicipioEmpreendimento)
				.filtrarPorIdTipoLicenca(filtro.idLicenca)
				.filtrarPorPeriodoVencimento(filtro.periodoInicial, filtro.periodoFinal);
	}
	
	public static List listWithFilters(FiltroLicenca filtro) {
		
		return groupByLicenca(commonFiltersLicenca(filtro), filtro);
	}
	
	public static Long countWithFilters(FiltroLicenca filtro) {
		
		LicencaBuilder licencaBuilder = commonFiltersLicenca(filtro)
			.addProcessosAlias()
			.addPessoaEmpreendimentoAlias()
			.addEstadoEmpreendimentoAlias()
			.addMunicipioEmpreendimentoAlias()
			.addTipoLicencaAlias()				
			.count();
			
		Object qtdeTotalItens = licencaBuilder.unique();
		
		return ((Map<String, Long>) qtdeTotalItens).get("total"); 
	}	
	
	/**
	 * Filtro utilizado para a pesquisa rápida, ou seja, um único campo de pesquisa
	 * @param pesquisa
	 * @return
	 */
	private static LicencaBuilder commonFilterLicenca(String pesquisa) {
		
		return new LicencaBuilder()
			.filtrarPorNumeroLicenca(pesquisa)
			.filtrarPorNumeroProcesso(pesquisa)
			.filtrarPorCpfCnpjEmpreendimento(pesquisa)
			.filtrarPorDenominacaoEmpreendimento(pesquisa)
			.filtrarPorMunicipio(pesquisa);
	}
	
	public static List listWithFilter(FiltroLicenca filtro) {
		
		return groupByLicenca(commonFilterLicenca(filtro.pesquisa), filtro);
	}
	
	public static Long countWithFilter(FiltroLicenca filtro) {
		
		LicencaBuilder licencaBuilder = commonFilterLicenca(filtro.pesquisa)
			.addProcessosAlias()
			.addPessoaEmpreendimentoAlias()
			.addEstadoEmpreendimentoAlias()
			.addMunicipioEmpreendimentoAlias()
			.addTipoLicencaAlias()
			.count();
			
		Object qtdeTotalItens = licencaBuilder.unique();
		
		return ((Map<String, Long>) qtdeTotalItens).get("total"); 
	}
}
