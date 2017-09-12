package controllers;

import java.util.List;

import builders.LicencaBuilder.FiltroLicenca;
import models.Processo;
import models.licenciamento.Licenca;
import security.Acao;
import security.Auth;

public class Licencas extends InternalController {
	
	/**
	 * Utilizado para a pesquisa rápida, ou seja, um único campo de pesquisa
	 * @param filtro
	 */
	public void listWithFilter(FiltroLicenca filtro){
		
		verificarPermissao(Acao.CONSULTAR_LICENCAS_EMITIDAS);
		
		List licencasList = Licenca.listWithFilter(filtro);
		
		renderJSON(licencasList);
	}
	
	/**
	 * Utilizado para a pesquisa rápida, ou seja, um único campo de pesquisa
	 * @param filtro
	 */	
	public void countWithFilter(FiltroLicenca filtro){
		
		verificarPermissao(Acao.CONSULTAR_LICENCAS_EMITIDAS);
		
		renderJSON(Licenca.countWithFilter(filtro));
	}
	
	/**
	 * Utilizado para a pesquisa avançada, ou seja, vários campos de pesquisa
	 * @param filtro
	 */
	public void listWithFilters(FiltroLicenca filtro){
		
		verificarPermissao(Acao.CONSULTAR_LICENCAS_EMITIDAS);
		
		List licencasList = Licenca.listWithFilters(filtro);
		
		renderJSON(licencasList);
	}
	
	/**
	 * Utilizado para a pesquisa avançada, ou seja, vários campos de pesquisa
	 * @param filtro
	 */
	public void countWithFilters(FiltroLicenca filtro){
		
		verificarPermissao(Acao.CONSULTAR_LICENCAS_EMITIDAS);
		
		renderJSON(Licenca.countWithFilters(filtro));
	}
}
