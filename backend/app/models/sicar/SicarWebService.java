package models.sicar;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import deserializers.DateDeserializer;
import models.licenciamento.Municipio;
import play.libs.WS.HttpResponse;
import utils.Configuracoes;
import utils.WebService;

import javax.xml.ws.WebServiceException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SicarWebService {
	
	private GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat(DateDeserializer.DATE_FORMAT);
	
	public ImovelSicar getImovelByCodigo(String codigoImovel) {
		
		String url = Configuracoes.URL_SICAR_IMOVEIS_COMPLETOS.replace("{codigoImovel}", codigoImovel);

		HttpResponse response = new WebService().get(url);
		
		if(!response.success()){
			throw new WebServiceException("Erro ao consultar imóveis no CAR/PA");
		}
		
		Type type = new TypeToken<MensagemSicar<ImovelSicar>>(){}.getType();

		MensagemSicar<ImovelSicar> retorno = gsonBuilder.create().fromJson(response.getJson(), type);
		
		if(!retorno.status.equals(StatusSiCAR.SUCESSO.sigla)){
			throw new WebServiceException(String.format("Erro ao consultar imóveis no CAR/PA: %s", retorno.mensagem));
		}
		
		return retorno.dados;
		
	}

	public FichaSicar getImovelById(String idImovel) {

		String url = Configuracoes.URL_SICAR_IMOVEL_FICHA.replace("{idImove}", idImovel);

		HttpResponse response = new WebService().get(url);

		if(!response.success()){
			throw new WebServiceException("Erro ao consultar imóveis no CAR/PA");
		}

		Type type = new TypeToken<MensagemSicar<FichaSicar>>(){}.getType();

		MensagemSicar<FichaSicar> retorno = gsonBuilder.create().fromJson(response.getJson(), type);

		if(!retorno.status.equals(StatusSiCAR.SUCESSO.sigla)){
			throw new WebServiceException(String.format("Erro ao consultar imóveis no CAR/PA: %s", retorno.mensagem));
		}

		return retorno.dados;
	}

	public List<ImovelSicar> getImoveisSimplificadosPorCpfCnpj(String cpfCnpj, Municipio municipio) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cpfCnpj", cpfCnpj);
		params.put("codMunicipioIbge", municipio.id);

		HttpResponse response = new WebService().get(Configuracoes.URL_SICAR_IMOVEIS_SIMPLIFICADOS, params);

		if(!response.success()){
			throw new WebServiceException("Erro ao consultar imóveis no CAR/PA");
		}

		Type type = new TypeToken<MensagemSicar<List<ImovelSicar>>>(){}.getType();

		MensagemSicar<List<ImovelSicar>> retorno = gsonBuilder.create().fromJson(response.getJson(), type);

		if(!retorno.status.equals(StatusSiCAR.SUCESSO.sigla)){
			throw new WebServiceException(String.format("Erro ao consultar imóveis no CAR/PA: %s", retorno.mensagem));
		}

		return retorno.dados;

	}

	public enum StatusSiCAR {
		
		SUCESSO("s"),
		ERROR("e"),
		ALERTA("a");
		
		public String sigla;

		private StatusSiCAR(String sigla) {
			
			this.sigla = sigla;
		}

	}
	
	public static class MensagemSicar<T> {
		
		public static String SUCESSO = "s";
		
		public String status;
		public String mensagem;
		public T dados;

	}

}
