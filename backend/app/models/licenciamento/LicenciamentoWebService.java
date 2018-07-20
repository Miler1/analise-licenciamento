package models.licenciamento;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.ws.WebServiceException;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import deserializers.DateDeserializer;
import play.libs.WS.HttpResponse;
import utils.Configuracoes;
import utils.WebService;

public class LicenciamentoWebService {
	
	public List<Caracterizacao> getCaracterizacoesEmAndamento() {
		
		Type type = new TypeToken<List<Caracterizacao>>(){}.getType();
		List<Caracterizacao> caracterizacoesRetorno = new WebService().getJson(Configuracoes.URL_LICENCIAMENTO_CARACTERIZACOES_EM_ANDAMENTO, type);
		
		return caracterizacoesRetorno;
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

	public void reemitirPDFDla(Long idDla) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idDla", idDla);

		HttpResponse response = new WebService().post(Configuracoes.URL_LICENCIAMENTO_REEMITIR_PDFS_DLA, params);

		if(!response.success()) {
			throw new WebServiceException("Erro ao reemitir DLA.");
		}
	}

}
