package controllers;

import models.Notificacao;
import models.portalSeguranca.UsuarioLicenciamento;
import play.libs.Crypto;
import security.Auth;
import serializers.ApplicationSerializer;
import utils.Configuracoes;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Application extends GenericController {

	public static void index() {

		UsuarioLicenciamento usuarioSessao = Auth.getAuthenticatedUser(session.current());
		
		if (usuarioSessao != null)
			redirect(Configuracoes.INDEX_URL);
		else
			redirect(Configuracoes.LOGIN_URL);

	}

	public static void findInfo() {
		
		DadosApp dados = new DadosApp();
		dados.usuarioSessao = Auth.getAuthenticatedUser(session.current());
		
//		if(Play.mode == Play.Mode.DEV)
//			dados.usuarioSessao.autenticadoViaToken = true;
		
		String jsonConfig = ApplicationSerializer.findInfo.serialize(dados);
		
		render(jsonConfig);
	}
	
	public static void versao() {
		
		render();
	}
	
	public static class DadosApp {
		
		public UsuarioLicenciamento usuarioSessao;
		public ConfiguracoesApp configuracoes = new ConfiguracoesApp();
	}
	
	public static class ConfiguracoesApp {
		
		public String baseURL = Configuracoes.HTTP_PATH;
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
}
