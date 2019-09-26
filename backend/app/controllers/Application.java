package controllers;

import models.Notificacao;
import models.UsuarioAnalise;
import org.apache.tika.Tika;
import play.Play;
import play.data.Upload;
import play.libs.Crypto;
import play.libs.IO;
import play.mvc.Http;
import security.Auth;
import serializers.ApplicationSerializer;
import utils.Configuracoes;
import utils.FileManager;
import utils.Mensagem;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Application extends GenericController {

	public static void index() {

		UsuarioAnalise usuarioSessao = Auth.getAuthenticatedUser(session.current());
		
		if (usuarioSessao != null)
			redirect(Configuracoes.INDEX_URL);
			Auth.logout(session.current());
			redirect(Configuracoes.LOGIN_URL);

	}

	public static void findInfo() {
		
		DadosApp dados = new DadosApp();
		dados.usuarioSessao = Auth.getAuthenticatedUser(session.current());

		String jsonConfig = "";

		if (dados.usuarioSessao == null) {
			//redirect(Configuracoes.LOGIN_URL);
			jsonConfig = ApplicationSerializer.findInfoSemUsuario.serialize(dados);
		} else {

			if(Play.mode == Play.Mode.DEV)
				dados.usuarioSessao.usuarioEntradaUnica.autenticadoViaToken = true;

			jsonConfig = ApplicationSerializer.findInfo.serialize(dados);
		}

		render(jsonConfig);
	}
	
	public static void versao() {
		
		render();
	}
	
	public static class DadosApp {
		
		public UsuarioAnalise usuarioSessao;
		public ConfiguracoesApp configuracoes = new ConfiguracoesApp();
	}
	
	public static class ConfiguracoesApp {
		
		public String baseURL = Configuracoes.HTTP_PATH;
		public String baseUrlGeoServer = Configuracoes.GEOSERVER_BASE_URL;
	}


	public static void qrCodeView(String idQrCode) throws UnsupportedEncodingException {

		Map<String, Object> args = generateArgs(idQrCode);

		renderTemplate(Configuracoes.PDF_TEMPLATES_FOLDER_PATH + "/qrcode/informacoes.html", args);
	}

	public static void qrCodeDownload(String idTramitacao) throws Exception {

		Notificacoes.downloadPDF(Integer.parseInt(Crypto.decryptAES(idTramitacao)));
	}

	private static Map<String, Object> generateArgs(String idQrCode) {

		idQrCode = Crypto.decryptAES(idQrCode);
		String[] params = idQrCode.split("/");

		List<Notificacao> notificacoes = Notificacao.find("codigoSequencia = :x AND codigoAno = :y")
				.setParameter("x", Long.valueOf(params[0]))
				.setParameter("y", Integer.valueOf(params[1]))
				.fetch();

		Notificacao notificacao = notificacoes.get(0);
		String url = Configuracoes.APP_URL + "notificacoes/" + Crypto.encryptAES(String.valueOf(notificacao.historicoTramitacao.idHistorico))
				+ "/download";

		String urlImageLogo = Configuracoes.APP_URL + "app/images/brasao_pa.png";

		Map<String, Object> args = new HashMap<>(7);
		args.put("notificacoes", notificacoes);
		args.put("urlDownload", url);
		args.put("urlImageLogo", urlImageLogo);

		if (notificacao.analiseJuridica != null) {

			args.put("analiseEspecifica", notificacao.analiseJuridica);
			args.put("analiseArea", "ANALISE_JURIDICA");

		} else {

			args.put("analiseEspecifica", notificacao.analiseTecnica);
			args.put("analiseArea", "ANALISE_TECNICA");
		}

		return args;
	}

	public static void upload(Upload file) throws IOException {

		returnIfNull(file, "Upload");

		String realType = null;

		// Detecta o tipo de arquivo pela assinatura (Magic)
		Tika tika = new Tika();
		realType = tika.detect(file.asFile());

		if(realType == null){
			response.status = Http.StatusCode.INTERNAL_ERROR;
			renderMensagem(Mensagem.UPLOAD_EXTENSAO_NAO_SUPORTADA);
		}

		// Verifica se a extensão do arquivo é compatível com o tipo detectado,
		// com exceção de arquivos BMP
		if(!realType.contains("bmp")){
			if(!realType.contentEquals(file.getContentType())){
				response.status = Http.StatusCode.INTERNAL_ERROR;
				renderMensagem(Mensagem.UPLOAD_EXTENSAO_NAO_SUPORTADA);
			}
		}

		if(realType.contains("application/pdf") ||
				realType.contains("application/zip") ||
				realType.contains("image/jpeg") ||
				realType.contains("image/jpg") ||
				realType.contains("image/png") ||
				realType.contains("bmp")) {

			byte[] data = IO.readContent(file.asFile());
			String extension = FileManager.getInstance().getFileExtention(file.getFileName());
			String key = FileManager.getInstance().createFile(data, extension);

			renderText(key);
		}

		else {

			response.status = Http.StatusCode.INTERNAL_ERROR;
			renderMensagem(Mensagem.UPLOAD_ERRO);
		}
	}
}
