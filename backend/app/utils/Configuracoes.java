package utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import play.Play;

public class Configuracoes {

	public static String APP_URL = getConfig("application.baseURL", null);
	public static String HTTP_PATH = getConfig("http.path", null);
	
	private static String DEFAULT_LOGIN_URL = "/login";

	public static String AUTH_SERVICE = getConfig("auth.service", null);
	public static String LOGIN_URL = getConfig("auth.login.url", DEFAULT_LOGIN_URL);
	public static Boolean EXTERNAL_LOGIN = !LOGIN_URL.equals(DEFAULT_LOGIN_URL);
	public static String INDEX_URL = "app/index.html";

	public static String APPLICATION_TEMP_FOLDER = getConfig("application.tempFolder", Play.applicationPath + "/tmp/");

	public static long TAMANHO_MAXIMO_ARQUIVO = getLongConfig("sistema.tamanhoMaximoArquivoUpload");

	public static String ARQUIVOS_PATH_ANALISE = getConfig("arquivos.path.analise", null);
	public static String ARQUIVOS_PATH_LICENCIAMENTO = getConfig("arquivos.path.licenciamento", null);
	public static String ARQUIVOS_DOCUMENTOS_PATH_ANALISE = ARQUIVOS_PATH_ANALISE + getConfig("arquivos.documentos.path", null);
	public static String ARQUIVOS_DOCUMENTOS_PATH_LICENCIAMENTO = ARQUIVOS_PATH_LICENCIAMENTO + getConfig("arquivos.documentos.path", null);
	
	public static String ARQUIVOS_PATH_LICENCIAMENTO = getConfig("arquivos.pathLicenciamento", null);
	public static String ARQUIVOS_DOCUMENTOS_LICENCIAMENTO_PATH = ARQUIVOS_PATH_LICENCIAMENTO + getConfig("arquivos.documentosLicenciamento.path", null);
	
	public static String ESTADO = "PA";

	public static boolean JOBS_ENABLED = getBooleanConfig("jobs.enabled");
	
	public static final String DATABASE_SCHEMA = getConfig("database.schema", null);
	
	public static Integer PRAZO_ANALISE = getIntConfig("analise.prazo");
	public static Integer PRAZO_ANALISE_JURIDICA = getIntConfig("analise.juridica.prazo");
	public static Integer PRAZO_ANALISE_TECNICA = getIntConfig("analise.tecnica.prazo");
	
	public static String URL_LICENCIAMENTO = getConfig("licenciamento.url", null);	
	public static String URL_LICENCIAMENTO_CARACTERIZACOES_EM_ANDAMENTO = URL_LICENCIAMENTO + getConfig("licenciamento.caracterizacoes.andamento.url", null);
	public static String URL_LICENCIAMENTO_CARACTERIZACAO_ADICIONAR_ANALISE = URL_LICENCIAMENTO + getConfig("licenciamento.caracterizacao.adicionar.analise", null);
	
	/*
	 * Métodos utilitários
	 */
	
	private static String getConfig(String configKey, Object defaultValue) {
		
		String defaultTextValue = defaultValue != null ? defaultValue.toString() : null;
		
		String configValue = Play.configuration.getProperty(configKey, defaultTextValue);
		
		/* Fix para configurações deixadas em branco no application.conf */
		return configValue.isEmpty() ? defaultValue.toString() : configValue;

	}
	
	
	private static Integer getIntConfig(String configKey) {
		
		String config = Play.configuration.getProperty(configKey);
		
		return config != null ? Integer.parseInt(config) : null;
	}
	
	private static Double getDoubleConfig(String configKey) {
		
		String config = Play.configuration.getProperty(configKey);
		
		return config != null ? Double.parseDouble(config) : null;
	}
	
	private static boolean getBooleanConfig(String configKey) {
		
		String config = Play.configuration.getProperty(configKey);
		
		return config != null ? Boolean.parseBoolean(config) : null;
	}
	
	private static List<String> getStringListConfig(String configKey, String separator, String defaultValue) {
		
		String config = Play.configuration.getProperty(configKey);
		
		if (config == null || config.isEmpty())
			config = defaultValue;
		
		if (config != null && !config.isEmpty()) {
			
			String [] values = config.split(separator);
			
			return Arrays.asList(values);
		}
		
		return null;
		
	}
	
	private static File getFileConfig(String property, String defaultPath) {
		
		String path = getConfig(property, null);
		
		if (path != null)
			return new File(path);
		
		if (defaultPath != null)
			return new File(defaultPath);
		
		return null;
	}
	
	private static Long getLongConfig(String configKey) {
		
		String config = Play.configuration.getProperty(configKey);
		
		if (config != null && !config.isEmpty())
			return Long.parseLong(config);
		else
			return null;

	}

}
