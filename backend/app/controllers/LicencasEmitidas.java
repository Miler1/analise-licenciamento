package controllers;

import java.io.File;
import java.util.List;

import builders.LicencaEmitidaBuilder.FiltroLicenca;
import models.Documento;
import models.licenciamento.DispensaLicenciamento;
import models.licenciamento.DocumentoLicenciamento;
import models.licenciamento.Licenca;
import models.licenciamento.LicencaEmitida;
import play.db.jpa.JPABase;
import play.libs.Crypto;
import security.Acao;

public class LicencasEmitidas extends InternalController {
	
	/**
	 * Utilizado para a pesquisa rápida, ou seja, um único campo de pesquisa
	 * @param filtro
	 */
	public void listWithFilter(FiltroLicenca filtro){
		
		verificarPermissao(Acao.CONSULTAR_LICENCAS_EMITIDAS);
		
		List licencasList = LicencaEmitida.listWithFilter(filtro);
		
		renderJSON(licencasList);
	}
	
	/**
	 * Utilizado para a pesquisa rápida, ou seja, um único campo de pesquisa
	 * @param filtro
	 */	
	public void countWithFilter(FiltroLicenca filtro){
		
		verificarPermissao(Acao.CONSULTAR_LICENCAS_EMITIDAS);
		
		renderJSON(LicencaEmitida.countWithFilter(filtro));
	}
	
	/**
	 * Utilizado para a pesquisa avançada, ou seja, vários campos de pesquisa
	 * @param filtro
	 */
	public void listWithFilters(FiltroLicenca filtro){
		
		verificarPermissao(Acao.CONSULTAR_LICENCAS_EMITIDAS);
		
		List licencasList = LicencaEmitida.listWithFilters(filtro);
		
		renderJSON(licencasList);
	}
	
	/**
	 * Utilizado para a pesquisa avançada, ou seja, vários campos de pesquisa
	 * @param filtro
	 */
	public void countWithFilters(FiltroLicenca filtro){
		
		verificarPermissao(Acao.CONSULTAR_LICENCAS_EMITIDAS);
		
		renderJSON(LicencaEmitida.countWithFilters(filtro));
	}
	
	public static void downloadLicencas(Long id) {
		
		verificarPermissao(Acao.CONSULTAR_LICENCAS_EMITIDAS);
		
		Licenca licenca = Licenca.findById(id);

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
