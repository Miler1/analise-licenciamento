package models.licenciamento;

import java.lang.reflect.Type;
import java.util.List;

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
	
	public void adicionarCaracterizacaoEmAnalise(Caracterizacao caracterizacao) {
		
		String url = Configuracoes.URL_LICENCIAMENTO_CARACTERIZACAO_ADICIONAR_ANALISE.replace("{id}", caracterizacao.id.toString());
		
		HttpResponse response = new WebService().post(url);
		
		if(!response.success()) {
			throw new WebServiceException("Erro ao definir o status da Caracterização.");
		}
		
	}

}
