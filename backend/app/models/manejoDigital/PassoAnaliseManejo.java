package models.manejoDigital;

public enum PassoAnaliseManejo {

	DADOS_IMOVEL(0,"Dados do imóvel"),
	BASE_VETORIAL(1,"Metadados da base vetorial oficial utilizada"),
	ANALISE_VETORIAL(2,"Análise vetorial sobre a base de dados oficiais"),
	ANALISE_TEMPORAL(3,"Análise temporal de imagens de satélite"),
	INSUMOS_UTILIZADOS(4,"Insumos utilizados para análise temporal"),
	CALCULO_NDFI(5,"Cálculo do NDFI"),
	CALCULO_AREA_EFETIVA(6,"Cálculo da área efetiva de manejo"),
	DETALHAMENTO_AREA_EFETIVA(7,"Detalhamento da área efetiva de manejo"),
	CONSIDERACOES(8,"Considerações"),
	DOCUMENTOS_COMPLEMENTARES(9, "Documentos complementares"),
	EMBASAMENTOS_LEGAIS(10,"Embasamentos legais"),
	CONCLUSAO(11,"Conclusão");


	public int id;
	public String nome;

	PassoAnaliseManejo(int id, String nome) {

		this.id = id;
		this.nome = nome;
	}
}
