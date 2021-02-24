package controllers;

import builders.LicencaEmitidaBuilder.FiltroLicenca;
import models.licenciamento.DispensaLicenciamento;
import models.licenciamento.DocumentoLicenciamento;
import models.licenciamento.Licenca;
import models.licenciamento.LicencaEmitida;
import security.Acao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class LicencasEmitidas extends InternalController {
	
	/**
	 * Utilizado para a pesquisa rápida, ou seja, um único campo de pesquisa
	 * @param filtro
	 */
	public static void listWithFilter(FiltroLicenca filtro){
		
		verificarPermissao(Acao.CONSULTAR_LICENCAS_EMITIDAS);

		List licencasList = LicencaEmitida.listWithFilter(filtro);
		
		renderJSON(licencasList);
	}
	
	/**
	 * Utilizado para a pesquisa rápida, ou seja, um único campo de pesquisa
	 * @param filtro
	 */	
	public static void countWithFilter(FiltroLicenca filtro){
		
		verificarPermissao(Acao.CONSULTAR_LICENCAS_EMITIDAS);
		
		renderJSON(LicencaEmitida.countWithFilter(filtro));
	}
	
	/**
	 * Utilizado para a pesquisa avançada, ou seja, vários campos de pesquisa
	 * @param filtro
	 */
	public static void listWithFilters(FiltroLicenca filtro){
		
		verificarPermissao(Acao.CONSULTAR_LICENCAS_EMITIDAS);

		List licencasList = LicencaEmitida.listWithFilters(filtro);

		renderJSON(licencasList);
	}
	
	/**
	 * Utilizado para a pesquisa avançada, ou seja, vários campos de pesquisa
	 * @param filtro
	 */
	public static void countWithFilters(FiltroLicenca filtro){
		
		verificarPermissao(Acao.CONSULTAR_LICENCAS_EMITIDAS);
		
		renderJSON(LicencaEmitida.countWithFilters(filtro));
	}
	
	public static void downloadLicencas(Long id) throws FileNotFoundException {
		
		verificarPermissao(Acao.CONSULTAR_LICENCAS_EMITIDAS);
		
		Licenca licenca = Licenca.findById(id);

		DocumentoLicenciamento documento = DocumentoLicenciamento.findById(licenca.documento.id);

		if(documento != null) {
			File documentoBinary = documento.getFile();
			renderBinary(new FileInputStream(documentoBinary), documentoBinary.getName(), true);
		}

	}
	
	public static void downloadDla(Long id) throws FileNotFoundException {

		verificarPermissao(Acao.CONSULTAR_LICENCAS_EMITIDAS);
		
		DispensaLicenciamento dla = DispensaLicenciamento.findById(id);		
		
		DocumentoLicenciamento documento = DocumentoLicenciamento.findById(dla.documento.id);

		if(documento != null) {
			File documentoBinary = documento.getFile();
			renderBinary(new FileInputStream(documentoBinary), documentoBinary.getName(), true);
		}
		
	}
	
}