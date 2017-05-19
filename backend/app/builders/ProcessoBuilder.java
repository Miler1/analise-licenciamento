package builders;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Property;

import models.Processo;

public class ProcessoBuilder extends CriteriaBuilder<Processo> {
	
	private static final String EMPREENDIMENTO_ALIAS = "emp";
	private static final String PESSOA_EMPREENDIMENTO_ALIAS = "pes";
	private static final String MUNICIPIO_EMPREENDIMENTO_ALIAS = "mun";
	private static final String ESTADO_EMPREENDIMENTO_ALIAS = "est";
	private static final String ANALISE_EMPREENDIMENTO_ALIAS = "ana";
	private static final String ANALISE_JURIDICA_EMPREENDIMENTO_ALIAS = "anj";
	
	public void createEmpreendimentoAlias() {
		
		addAlias("empreendimento", EMPREENDIMENTO_ALIAS);
	}
	
	public void createPessoaEmpreendimentoAlias() {
		
		createEmpreendimentoAlias();
		
		addAlias(EMPREENDIMENTO_ALIAS+".pessoa", PESSOA_EMPREENDIMENTO_ALIAS);
	}
	
	public void createMunicipioEmpreendimentoAlias() {
		
		createEmpreendimentoAlias();
		
		addAlias(EMPREENDIMENTO_ALIAS+".municipio", MUNICIPIO_EMPREENDIMENTO_ALIAS);
	}
	
	public void createEstadoEmpreendimentoAlias() {
		
		createMunicipioEmpreendimentoAlias();
		
		addAlias(MUNICIPIO_EMPREENDIMENTO_ALIAS+".estado", ESTADO_EMPREENDIMENTO_ALIAS);
	}
	
	public void createAnaliseAlias() {
		
		addAlias("analises", ANALISE_EMPREENDIMENTO_ALIAS);
	}
	
	public void createAnaliseJuridicaAlias() {
		
		addAlias(ANALISE_EMPREENDIMENTO_ALIAS+".analisesJuridica", ANALISE_JURIDICA_EMPREENDIMENTO_ALIAS);
	}
	
	public ProcessoBuilder comNumeroProcesso(){
		
		addProjection(Property.forName("numero").as("numero"));
		
		return this;
	}
	
	public ProcessoBuilder comCpfCnpjEmpreendimento(){
		
		createPessoaEmpreendimentoAlias();

		addProjection(Property.forName(PESSOA_EMPREENDIMENTO_ALIAS+".cpf").as("cpfEmpreendimento"));
		addProjection(Property.forName(PESSOA_EMPREENDIMENTO_ALIAS+".cnpj").as("cnpjEmpreendimento"));
		
		return this;
	}	
	
	public ProcessoBuilder comDenominacaoEmpreendimento(){
		
		createEmpreendimentoAlias();

		addProjection(Property.forName(EMPREENDIMENTO_ALIAS+".denominacao").as("denominacaoEmpreendimento"));
		
		return this;
	}
	
	public ProcessoBuilder comMunicipioEmpreendimento(){
		
		createMunicipioEmpreendimentoAlias();		
		addProjection(Property.forName(MUNICIPIO_EMPREENDIMENTO_ALIAS+".nome").as("municipioEmpreendimento"));
		
		createEstadoEmpreendimentoAlias();
		addProjection(Property.forName(ESTADO_EMPREENDIMENTO_ALIAS+".id").as("siglaEstadoEmpreendimento"));
		
		return this;
	}
	
	public ProcessoBuilder comDataVencimentoPrazoAnalise(){
		
		createAnaliseAlias();
		addProjection(Property.forName(ANALISE_EMPREENDIMENTO_ALIAS+".dataVencimentoPrazo").as("dataVencimentoPrazoAnalise"));
				
		return this;
	}
	
	public ProcessoBuilder comDataVencimentoPrazoAnaliseJuridica(){
		
		createAnaliseJuridicaAlias();
		addProjection(Property.forName(ANALISE_JURIDICA_EMPREENDIMENTO_ALIAS+".dataVencimentoPrazo").as("dataVencimentoPrazoAnaliseJuridica"));
		
		return this;
	}	
	
	@Override
	public Long count() {

		return null;
	}
}
