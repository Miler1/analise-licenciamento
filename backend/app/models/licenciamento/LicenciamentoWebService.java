package models.licenciamento;

import com.google.gson.reflect.TypeToken;
import models.DlaCancelada;
import play.libs.WS.HttpResponse;
import utils.Configuracoes;
import utils.WebService;

import javax.xml.ws.WebServiceException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LicenciamentoWebService {
	
	public List<Caracterizacao> getCaracterizacoesEmAndamento() {
		
		Type type = new TypeToken<List<Caracterizacao>>(){}.getType();

		List<Caracterizacao> caracterizacoesRetorno = new WebService().getJson(Configuracoes.URL_LICENCIAMENTO_CARACTERIZACOES_EM_ANDAMENTO, type);

		return caracterizacoesRetorno != null ? caracterizacoesRetorno : Collections.emptyList();
	}
	
	public void adicionarCaracterizacoesEmAnalise(Long...ids) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", ids);
		
		HttpResponse response = new WebService().post(Configuracoes.URL_LICENCIAMENTO_CARACTERIZACAO_ADICIONAR_ANALISE, params);
		
		if(!response.success()) {
			throw new WebServiceException("Erro ao definir o status da Caracterização.");
		}
	}

	public void gerarPDFLicencas(List<Long> idsLicencas) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idsLicencas", idsLicencas);

		HttpResponse response = new WebService().post(Configuracoes.URL_LICENCIAMENTO_GERAR_PDFS_LICENCA, params);

		if(!response.success()) {
			throw new WebServiceException("Erro ao gerar os PDFs.");
		}
	}

	public void cancelarDla(DlaCancelada dla) {

		new WebService().postJSON(Configuracoes.URL_LICENCIAMENTO_CANCELAR_DLA, dla);
	}

	public void prorrogarLicenca(Long id) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);

		HttpResponse response = new WebService().post(Configuracoes.URL_LICENCIAMENTO_PRORROGAR_LICENCA, params);

		if(!response.success()) {
			throw new WebServiceException("Erro ao prorrogar licenças.");
		}
	}

	public void finalizarProrrogacao(List<Long> idsLicencas) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", idsLicencas);

		HttpResponse response = new WebService().post(Configuracoes.URL_LICENCIAMENTO_FINALIZAR_PRORROGACAO_LICENCAS, params);

		if(!response.success()) {
			throw new WebServiceException("Erro ao finalizar prorrogações de licenças.");
		}
	}
}
