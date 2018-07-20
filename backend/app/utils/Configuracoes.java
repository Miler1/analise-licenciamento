package utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import play.Play;

public class Configuracoes {

	public static String HTTP_PATH = getConfig("http.path", null);
	public static String APP_URL = getConfig("application.baseURL", null) + (HTTP_PATH.compareTo("/") == 0 ? HTTP_PATH : HTTP_PATH + "/");
	
	private static String DEFAULT_LOGIN_URL = "/login";

	public static String AUTH_SERVICE = getConfig("auth.service", null);
	public static String LOGIN_URL = getConfig("auth.login.url", DEFAULT_LOGIN_URL);
	public static Boolean EXTERNAL_LOGIN = !LOGIN_URL.equals(DEFAULT_LOGIN_URL);
	public static String INDEX_URL = "app/index.html";

	public static String APPLICATION_TEMP_FOLDER = getConfig("application.tempFolder", Play.applicationPath + "/tmp/");

	public static long TAMANHO_MAXIMO_ARQUIVO = getLongConfig("sistema.tamanhoMaximoArquivoUpload");

	public static String ARQUIVOS_ANALISE_PATH = getConfig("arquivos.path.analise", null);
	public static String ARQUIVOS_LICENCIAMENTO_PATH = getConfig("arquivos.path.licenciamento", null);
	public static String ARQUIVOS_DOCUMENTOS_ANALISE_PATH = ARQUIVOS_ANALISE_PATH + getConfig("arquivos.documentos.path", null);
	public static String ARQUIVOS_DOCUMENTOS_LICENCIAMENTO_PATH = ARQUIVOS_LICENCIAMENTO_PATH + getConfig("arquivos.documentos.path", null);
	public static String GEOJSON_INCONFORMIDADES_PATH = ARQUIVOS_ANALISE_PATH + getConfig("arquivos.geojson.inconformidades.path", null);
	public static String ESTADO = "PA";

	public static boolean JOBS_ENABLED = getBooleanConfig("jobs.enabled");
	
	public static final String DATABASE_SCHEMA = getConfig("database.schema", null);
	
	public static Integer PRAZO_ANALISE = getIntConfig("analise.prazo");
	public static Integer PRAZO_ANALISE_JURIDICA = getIntConfig("analise.juridica.prazo");
	public static Integer PRAZO_ANALISE_TECNICA = getIntConfig("analise.tecnica.prazo");
	
	public static String URL_LICENCIAMENTO = getConfig("licenciamento.url", null);	
	public static String URL_LICENCIAMENTO_CARACTERIZACOES_EM_ANDAMENTO = URL_LICENCIAMENTO + getConfig("licenciamento.caracterizacoes.andamento.url", null);
	public static String URL_LICENCIAMENTO_CARACTERIZACAO_ADICIONAR_ANALISE = URL_LICENCIAMENTO + getConfig("licenciamento.caracterizacao.adicionar.analise", null);
	public static String URL_LICENCIAMENTO_GERAR_PDFS_LICENCA = URL_LICENCIAMENTO + getConfig("licenciamento.licenca.gerar.pdf", null);
	public static String URL_LICENCIAMENTO_REEMITIR_PDFS_DLA = URL_LICENCIAMENTO + getConfig("licenciamento.licenca.reemitir.dla", null);

	public static String PDF_TEMPLATES_FOLDER_PATH = "templates" + File.separator + "pdf";
	public static String PDF_TEMPLATES_FOLDER_ABSOLUTE = Play.applicationPath.getAbsolutePath() + File.separator + "app" + File.separator + "views" + File.separator;
	
	public static final String URL_SICAR_SITE = getConfig("sicar.site.url", null);

	public static CoordinateReferenceSystem CRS_DEFAULT = null;
	public static Integer SRID_DEFAULT = null;
	public static String GETCAPABILITIES_GEOSERVER_SICAR = getConfig("sistema.url.getcapabilities.geoserver.sicar", null);
	public static String GEOSERVER_SICAR_IMOVEL_LAYER = getConfig("sistema.url.getcapabilities.geoserver.sicar.layer.name", null);

	//Prazo da condicionante deverá ser menor ou igual a 1900 (5 anos) conforme regra.
	public static Integer PRAZO_MAXIMO_CONDICIONANTE = 1900;

	static {

		try {

			CRS_DEFAULT = CRS.parseWKT(getConfig("sistema.crs.default", null));

			SRID_DEFAULT = Integer.parseInt(AbstractIdentifiedObject.getIdentifier(CRS_DEFAULT, Citations.EPSG).getCode());

		} catch (Exception e) {

			e.printStackTrace();
		}
	}



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
