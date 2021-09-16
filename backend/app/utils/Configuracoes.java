package utils;

import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import play.Play;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Configuracoes {

	public static String HTTP_PATH = getConfig("http.path", null);
	public static String APP_URL = getConfig("application.baseURL", null) + (HTTP_PATH.compareTo("/") == 0 ? HTTP_PATH : HTTP_PATH + "/");

	private static String DEFAULT_LOGIN_URL = "/login";

	public static String SIGLA_MODULO = getConfig("sigla.modulo", "MAL");
	public static String AUTH_SERVICE = getConfig("auth.service", null);
	public static String LOGIN_URL = getConfig("auth.login.url", DEFAULT_LOGIN_URL);
	public static Boolean EXTERNAL_LOGIN = !LOGIN_URL.equals(DEFAULT_LOGIN_URL);
	public static String INDEX_URL = getConfig("index.path","app/index.html");
	public static String JOBS_PACKAGE = "jobs.";

	public static Integer CODIGO_TIPO_PESSOA_JURIDICA = 1;

	public static String APPLICATION_TEMP_FOLDER = getConfig("application.tempFolder", Play.applicationPath + "/tmp/");
	public static String APPLICATION_ANEXO_MANEJO_FOLDER = getConfig("application.anexoManejoFolder", "/home/licenciamento-pa/documentos/anexo_manejo");

	public static long TAMANHO_MAXIMO_ARQUIVO = getLongConfig("sistema.tamanhoMaximoArquivoUpload");

	public static String ARQUIVOS_ANALISE_PATH = getConfig("arquivos.path.analise", null);
	public static String ARQUIVOS_LICENCIAMENTO_PATH = getConfig("arquivos.path.licenciamento", null);
	public static String ARQUIVOS_DOCUMENTOS_ANALISE_PATH = ARQUIVOS_ANALISE_PATH + getConfig("arquivos.documentos.path", null);
	public static String ARQUIVOS_DOCUMENTOS_SHAPEFILE_PATH = ARQUIVOS_DOCUMENTOS_ANALISE_PATH + getConfig("arquivos.shapefile.path", null);
	public static String ARQUIVOS_DOCUMENTOS_LICENCIAMENTO_PATH = ARQUIVOS_LICENCIAMENTO_PATH + getConfig("arquivos.documentos.path", null);
	public static String GEOJSON_INCONFORMIDADES_PATH = ARQUIVOS_ANALISE_PATH + getConfig("arquivos.geojson.inconformidades.path", null);
	public static String ESTADO = "AM";
	public static String ARQUIVOS_SHAPE_MANEJO = ARQUIVOS_ANALISE_PATH + getConfig("arquivos.shape.manejo", null);
	public static String ARQUIVOS_IMOVEL_MANEJO = ARQUIVOS_ANALISE_PATH + getConfig("arquivos.imovel.manejo", null);

	public static boolean JOBS_ENABLED = getBooleanConfig("jobs.enabled");

	public static final String DATABASE_SCHEMA = getConfig("database.schema", null);

	public static Integer PRAZO_ANALISE = getIntConfig("analise.prazo");
	public static Integer PRAZO_ANALISE_JURIDICA = getIntConfig("analise.juridica.prazo");
	public static Integer PRAZO_ANALISE_GEO = getIntConfig("analise.geo.prazo");
	public static Integer PRAZO_ANALISE_TECNICA = getIntConfig("analise.tecnica.prazo");

	public static String DESTINATARIO_JURIDICO = getConfig("email.destinatario.juridico", null);
	public static String DESTINATARIO_JURIDICO2 = getConfig("email.destinatario.juridico2", null);
	public static String URL_LICENCIAMENTO = getConfig("licenciamento.url", null);
	public static String URL_LICENCIAMENTO_CARACTERIZACOES_EM_ANDAMENTO = URL_LICENCIAMENTO + getConfig("licenciamento.caracterizacoes.andamento.url", null);
	public static String URL_LICENCIAMENTO_CARACTERIZACAO_ADICIONAR_ANALISE = URL_LICENCIAMENTO + getConfig("licenciamento.caracterizacao.adicionar.analise", null);
	public static String URL_LICENCIAMENTO_GERAR_PDFS_LICENCA = URL_LICENCIAMENTO + getConfig("licenciamento.licenca.gerar.pdf", null);
	public static String URL_LICENCIAMENTO_CANCELAR_DLA = URL_LICENCIAMENTO + getConfig("licenciamento.licenca.cancelar.dla", null);
	public static String URL_LICENCIAMENTO_PRORROGAR_LICENCA = URL_LICENCIAMENTO + getConfig("licenciamento.prorrogar.licenca", null);
	public static String URL_LICENCIAMENTO_FINALIZAR_PRORROGACAO_LICENCAS = URL_LICENCIAMENTO + getConfig("licenciamento.finalizar.prorrogacao.licencas", null);
	public static String URL_LICENCIAMENTO_UPDATE_STATUS = URL_LICENCIAMENTO + getConfig("licenciamento.caracterizacao.update.status", null);

	public static String PDF_TEMPLATES_FOLDER_PATH = "templates" + File.separator + "pdf";
	public static String PDF_TEMPLATES_FOLDER_ABSOLUTE = Play.applicationPath.getAbsolutePath() + File.separator + "app" + File.separator + "views" + File.separator;

	public static final String URL_SICAR_SITE = getConfig("sicar.site.url", null);

	public static CoordinateReferenceSystem CRS_DEFAULT = null;
	public static Integer SRID_DEFAULT = null;
	public static String GETCAPABILITIES_GEOSERVER_SICAR = getConfig("sistema.url.getcapabilities.geoserver.sicar", null);
	public static String GEOSERVER_SICAR_IMOVEL_LAYER = getConfig("sistema.url.getcapabilities.geoserver.sicar.layer.name", null);
	public static String GEOSERVER_BASE_URL = getConfig("sistema.base.url.geoserver", null);

	//Prazo da condicionante deverá ser menor ou igual a 1900 (5 anos) conforme regra.
	public static Integer PRAZO_MAXIMO_CONDICIONANTE = 1900;

	public static long DIAS_PRORROGACAO = getLongConfig("dias.prorrogacao");
	public static Long TRAMITACAO_ETAPA_MANEJO = getLongConfig("tramitacao.etapa.manejo");
	public static String URL_SICAR = getConfig("sicar.url", null);
	public static String URL_SICAR_IMOVEIS_COMPLETOS = URL_SICAR + getConfig("sicar.imoveisCompletos.url", null);
	public static String URL_SICAR_IMOVEL_FICHA = URL_SICAR + getConfig("url.sicar.imovel.ficha", null);
	public static String URL_SICAR_IMOVEIS_SIMPLIFICADOS = URL_SICAR + getConfig("url.sicar.imoveisSimplificados", null);

	public static String ANALISE_SHAPE_URL = getConfig("analise.shape.url", null);
	public static String ANALISE_SHAPE_TOKEN_URL = getConfig("analise.shape.token.url", null);
	public static String ANALISE_SHAPE_TOKEN_USERNAME = getConfig("analise.shape.token.username", null);
	public static String ANALISE_SHAPE_TOKEN_PASSWORD = getConfig("analise.shape.token.password", null);
	public static String ANALISE_SHAPE_TOKEN_REFERER = getConfig("analise.shape.token.referer", null);
	public static String ANALISE_SHAPE_TOKEN_EXPIRATION = getConfig("analise.shape.token.expiration", null);

	public static String ANALISE_SHAPE_ADD_FEATURES_PROPRIEDADE_URL = ANALISE_SHAPE_URL + getConfig("analise.shape.add.features.propriedade.url", null);
	public static String ANALISE_SHAPE_ADD_FEATURES_AREA_SEM_POTENCIAL_URL = ANALISE_SHAPE_URL + getConfig("analise.shape.add.features.area.sem.potencial.url", null);
	public static String ANALISE_SHAPE_ADD_FEATURES_PROCESSOS_URL = ANALISE_SHAPE_URL + getConfig("analise.shape.add.features.processos.url", null);
	public static String ANALISE_SHAPE_QUERY_PROCESSOS_URL = ANALISE_SHAPE_URL + getConfig("analise.shape.query.processos.url", null);
	public static String ANALISE_SHAPE_QUERY_SOBREPOSICOES_URL = ANALISE_SHAPE_URL + getConfig("analise.shape.query.sobreposicoes.url", null);
	public static String ANALISE_SHAPE_QUERY_INSUMOS_URL = ANALISE_SHAPE_URL + getConfig("analise.shape.query.insumos.url", null);
	public static String ANALISE_SHAPE_QUERY_RESUMO_NDFI_URL = ANALISE_SHAPE_URL + getConfig("analise.shape.query.resumo.ndfi.url", null);
	public static String ANALISE_SHAPE_QUERY_AMF_MANEJO_URL = ANALISE_SHAPE_URL + getConfig("analise.shape.query.amf.manejo.url", null);
	public static String ANALISE_SHAPE_QUERY_AMF_METADADOS_URL = ANALISE_SHAPE_URL + getConfig("analise.shape.query.amf.metadados.url", null);

	public static String MANEJO_NIVEL_EXPLORACAO_ALTO = getConfig("manejo.nivel.exploracao.alto", null);
	public static String MANEJO_NIVEL_EXPLORACAO_MEDIO = getConfig("manejo.nivel.exploracao.medio", null);
	public static String MANEJO_NIVEL_EXPLORACAO_BAIXO = getConfig("manejo.nivel.exploracao.baixo", null);

	// Integração com o Entrada Unica
	public static final String ENTRADA_UNICA_CLIENTE_ID = Play.configuration.getProperty("entrada.unica.cliente.id");
	public static final String ENTRADA_UNICA_CLIENTE_SECRET = Play.configuration.getProperty("entrada.unica.cliente.secret");
	public static final String ENTRADA_UNICA_URL_PORTAL_SEGURANCA = Play.configuration.getProperty("entrada.unica.url.portal.seguranca");
	public static final String ENTRADA_UNICA_URL_CADASTRO_UNIFICADO = Play.configuration.getProperty("entrada.unica.url.cadastro.unificado");

	// Código

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
