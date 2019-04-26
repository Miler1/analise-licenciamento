package controllers;

import builders.LicencaEmitidaBuilder.FiltroLicenca;
import exceptions.AppException;
import models.licenciamento.DispensaLicenciamento;
import models.licenciamento.DocumentoLicenciamento;
import models.licenciamento.Licenca;
import models.licenciamento.LicencaEmitida;
import security.Acao;
import utils.Mensagem;

import java.io.File;
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
	
	public static void downloadLicencas(Long id) {
		
		verificarPermissao(Acao.CONSULTAR_LICENCAS_EMITIDAS);
		
		Licenca licenca = Licenca.findById(id);

		if(licenca.isSuspensa())
			throw new AppException(Mensagem.LICENCA_CANCELADA_OU_SUSPENSA);
		
		DocumentoLicenciamento documento = DocumentoLicenciamento.findById(licenca.documento.id);
		
		File file = documento.getFile();
		
		renderBinary(file, file.getName());
	}
	
	public static void downloadDla(Long id) {
		
		verificarPermissao(Acao.CONSULTAR_LICENCAS_EMITIDAS);
		
		DispensaLicenciamento dla = DispensaLicenciamento.findById(id);		
		
		DocumentoLicenciamento documento = DocumentoLicenciamento.findById(dla.documento.id);
		
		File file = documento.getFile();
		
		renderBinary(file, file.getName());
	}
	
}
