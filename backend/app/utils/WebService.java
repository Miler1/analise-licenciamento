package utils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import exceptions.WebServiceException;
import play.Logger;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.libs.WS.WSRequest;

public class WebService {

	private Gson gson;
	private Map<String, String> defaultHeaders;
	
	public WebService() {
		
		this.gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
	}
	
	public WebService(Gson gson) {
		
		this.gson = gson;
	}	
	
	public HttpResponse get(String url) {
		
		return createRequest(url).get();
	}
	
	public HttpResponse get(String url, String authenticate) {
		
		WSRequest request = createRequest(url);
		request.setHeader("www-authenticate", authenticate);
		
		return request.get();
	}
	
	public HttpResponse get(String url, Map<String, Object> params) {
		
		WSRequest request = createRequest(url);
		
		if (params != null && !params.isEmpty()) {
			request.params(params);
		}
		
		return request.get();
	}
	
	public <T> T getJson(String url, Class<T> clazz) {

		return getJson(url, null, clazz);
				
	}
	
	public <T> T getJson(String url, Map<String, Object> params, Class<T> clazz) {

		HttpResponse response = get(url, params);
		
		if (response.success()) {
			
			JsonElement responseJson = response.getJson();
			
			if (responseJson.isJsonNull()) 
				return null;

			return gson.fromJson(responseJson, clazz);
			
		} else {
			
			Logger.error("Não foi possível consumir o serviço: " + url + " - " + response.getStatusText() + ": " + response.getString());
			
			throw new WebServiceException("Houve um problema ao utilizar o serviço da base corporativa. Contate o administrador do sistema.");
			
		}
		
	}
	
	public <T> T getJson(String url, Type type) {

		return getJson(url, null, type);
				
	}
	
	public <T> T getJson(String url, Map<String, Object> params, Type type) {

		HttpResponse response = get(url, params);
		
		if (response.success()) {
			
			JsonElement responseJson = response.getJson();
			
			if (responseJson.isJsonNull()) 
				return null;

			return gson.fromJson(responseJson, type);
			
		} else {
			
			Logger.error("Não foi possível consumir o serviço: " + url + " - " + response.getStatusText() + ": " + response.getString());
			
			throw new WebServiceException("Houve um problema ao utilizar o serviço da base corporativa. Contate o administrador do sistema.");
			
		}
		
	}
	
	public HttpResponse post(String url, Map<String, Object> params) {
		
		WSRequest request = createRequest(url);
		
		if (params != null && !params.isEmpty()) {
			request.params(params);
		}
		
		return request.post();
	}
	
	public HttpResponse post(String url) {
		
		WSRequest request = createRequest(url);
		
		return request.post();
	}
	
	public HttpResponse post(String url, String data, String contentType) {
		
		WSRequest request = createRequest(url);
		request.setHeader("Content-Type",contentType);
		
		if (data != null)
			request.body(data);
		
		return request.post();
	}
	
	public HttpResponse post(String url, String data, String contentType, String authenticate) {
		
		WSRequest request = createRequest(url);
		request.setHeader("Content-Type",contentType);
		request.setHeader("www-authenticate", authenticate);
		
		if (data != null)
			request.body(data);
		
		return request.post();
	}
	
	public <T> T postJSON(String url, Object data, Class<T> responseType) {
		
		WSRequest request = createRequest(url);
		request.setHeader("Content-Type","application/json");
		
		if (data != null) {
			String jsonData = gson.toJson(data);
			request.body(jsonData);
		}
		
		HttpResponse response = request.post();
		
		if (!response.success())
			throw new WebServiceException(response);
			
		JsonElement responseJson = response.getJson();
		
		if (responseJson.isJsonObject()) {

			JsonObject json = (JsonObject) responseJson;
			return gson.fromJson(json, responseType);

		} else if (responseJson.isJsonArray()) {

			return gson.fromJson(responseJson.getAsJsonArray(), responseType);
		}
		
		return null;
	}
	
	public <T> T postJSON(String url, String jsonData, Class<T> responseType) {
		
		WSRequest request = createRequest(url);
		request.setHeader("Content-Type","application/json");
		
		if(jsonData == null)
			throw new IllegalArgumentException("Json está null ou não foi informado!");
		
		request.body(jsonData);
		
		HttpResponse response = request.post();
		
		if (!response.success())
			throw new WebServiceException(response);
			
		JsonElement responseJson = response.getJson();
		
		if (responseJson.isJsonObject()) {

			JsonObject json = (JsonObject) responseJson;
			return gson.fromJson(json, responseType);

		} else if (responseJson.isJsonArray()) {

			return gson.fromJson(responseJson.getAsJsonArray(), responseType);
		}
		
		return null;
	}
	
	public <T> T postJSON(String url, String jsonData, Class<T> responseType, String authenticate) {
		
		WSRequest request = createRequest(url);
		request.setHeader("Content-Type","application/json");
		request.setHeader("www-authenticate", authenticate);
		
		if(jsonData == null)
			throw new IllegalArgumentException("Json está null ou não foi informado!");
		
		request.body(jsonData);
		
		HttpResponse response = request.post();
		
		if (!response.success())
			throw new WebServiceException(response);
			
		JsonElement responseJson = response.getJson();
		
		if (responseJson.isJsonObject()) {

			JsonObject json = (JsonObject) responseJson;
			return gson.fromJson(json, responseType);

		} else if (responseJson.isJsonArray()) {

			return gson.fromJson(responseJson.getAsJsonArray(), responseType);
		}
		
		return null;
	}

	public void postJSON(String url, Object data) {

		WSRequest request = createRequest(url);
		request.setHeader("Content-Type","application/json");

		if (data != null) {
			String jsonData = gson.toJson(data);
			request.body(jsonData);
		}

		HttpResponse response = request.post();

		if (!response.success())
			throw new WebServiceException(response);
	}
	
	public void setDefaultHeader(String header, String value) {
		
		if (this.defaultHeaders == null)
			this.defaultHeaders = new HashMap<String, String>();
		
		this.defaultHeaders.put(header, value);
	}
	
	private WSRequest createRequest(String url) {
		
		WSRequest request = WS.url(url);
		
		if (this.defaultHeaders != null) {
			
			for (Entry<String, String> header : this.defaultHeaders.entrySet()) {
				
				request.setHeader(header.getKey(), header.getValue());
			}
		}
		
		return request;
	}
}
