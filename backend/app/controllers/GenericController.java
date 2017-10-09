package controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import models.Documento;

import org.hibernate.Session;

import play.Logger;
import play.db.jpa.JPA;
import play.mvc.Before;
import play.mvc.Catch;
import play.mvc.Controller;
import play.mvc.Http.StatusCode;
import utils.Configuracoes;
import utils.Mensagem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import exceptions.AppException;
import flexjson.JSONSerializer;

public class GenericController extends Controller {

	protected static Gson gson;
	
	static {

		GsonBuilder builder = new GsonBuilder()
				.setDateFormat("dd/MM/yyyy HH:mm:ss");
		gson = builder.create();		
	}
	
	
	/**
	 * Recupera um parametro da requisição e converte para Long
	 */
	protected static Long getParamAsLong(String key) {
		String value = request.params.get(key);
		return (value!= null) ? Long.valueOf(value) : null;
	}
	
	
	/**
	 * Recupera um parametro da requisição e converte para Integer
	 */
	protected static Integer getParamAsInteger(String key) {
		String value = request.params.get(key);
		return (value!= null) ? Integer.valueOf(value) : Integer.valueOf(0);
	}
	
	
	/**
	 * Recupera um parametro da requisição e converte para String
	 */
	protected static String getParamAsString(String key) {
		return request.params.get(key);
	}

	
	/**
	 * Recupera um parametro da requisição e converte para Boolean
	 */
	protected static Boolean getParamAsBoolean(String key) {
		String value = request.params.get(key);
		return (value != null) ? "true".equalsIgnoreCase(value) : null;
	}
	
	
	/**
	 * Método recupera uma parametro da requisição no formado nomeDoParametro=1,3,5,6
	 * e efetua a conversão, retornando uma lista de Long
	 */
	protected static List<Long> getParamAsLongList(String key) {
		
		String value = request.params.get(key);
	
		if (value == null || value.isEmpty())
			return null;
		
		String [] textLongs = value.split(",");
		
		if (textLongs.length == 0)
			return null;
		
		List<Long> longs = new ArrayList<Long>();
		
		for (String s : textLongs) {
			longs.add(Long.parseLong(s));
		}
	
		return longs;
	}
	

	/**
	 * Renderiza um JSON utilizando o serializador informado.
	 */
	protected static void renderJSON(Object model, JSONSerializer js) {
		
		String json = js.serialize(model);
		renderJSON(json);
	}
	
	
	protected static void renderJSON(Object model) {
		
		renderJSON(gson.toJson(model));
	}
	

	/*
	 * Renderiza um JSON utilizando o serializador informado.
	 */
	protected static void renderJSON(Collection<Object> models, JSONSerializer js) {
	
		String json = js.serialize(models);
		renderJSON(json);
	}
	
	protected static void renderMensagem(Mensagem msg) {
		
		renderMensagem(new MensagemResponse(msg.getTexto(), null), null);
	}
	
	protected static void renderMensagem(Mensagem msg, Object argumentos) {

		renderMensagem(new MensagemResponse(msg.getTexto(argumentos), null), null);
	}
	
	protected static void renderMensagem(Mensagem msg, Object dados, JSONSerializer js) {

		renderMensagem(new MensagemResponse(msg.getTexto(), dados), js);
	}
	
	
	private static void renderMensagem(MensagemResponse response, JSONSerializer js) {
		
		if (js != null)
			renderJSON(response, js);
		else
			renderJSON(response);
	}
   
   
	/**
	 * Caso algum dos objetos passados como parâmetro forem null, retorna 404.
	 */
	protected static void returnIfNull(Object model, String tipo, Object ... models) {

		if (model == null) {
			
			response.status = 404;

			renderText("É necessário informar um objeto do tipo " + tipo);

		}
		
		if (models != null && models.length > 0) {
		 
			for (int i = 0; i < models.length; i++) {
			
				if (models[i] == null) {
					renderText("O objeto informado não foi encontrado.");
					return;
				}
			}
		}

	}
	
	/**
	 * Tratamento de exceções: as exceções não tratadas que chegam à controller são tratadas por
	 * este método. É retornado o status 500 e o texto de mensagem. Se a exceção for do 
	 * tipo AnaliseException, a mensagem estará contida na exceção, caso contrário será utilizada
	 * uma mensagem padrão.
	 */
	@Catch(value = Exception.class, priority = 2)
	protected static void returnIfExceptionRaised(Throwable throwable) {
		
		Logger.error(throwable, throwable.getMessage());
		
		if (JPA.isEnabled())
			JPA.setRollbackOnly();
		
		response.status = StatusCode.INTERNAL_ERROR;
		
		if (throwable instanceof AppException){
			
			AppException appException = ((AppException) throwable);	
			
			renderMensagem(new MensagemResponse(appException.getTextoMensagem(), null), null);
		}

		renderMensagem(new MensagemResponse(Mensagem.ERRO_PADRAO.getTexto(), null), null);
	}
	
	
	protected static void renderBinary(Documento documento) {
		
		File file = documento.getFile();
		
		response.setHeader("Content-Type", "application/x-download");
		response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());

		renderBinary(file);
	}
	
	
	public static class MensagemResponse {
		
		public String texto;
		public Object dados;
		
		public MensagemResponse(String texto, Object dados) {
			
			this.texto = texto;
			this.dados = dados;
		}
		
		public MensagemResponse(Mensagem msg, Object dados) {
			
			this.texto = msg.getTexto();
			this.dados = dados;
		}
	}

}
