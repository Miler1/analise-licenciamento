package controllers;

import models.manejoDigital.AnaliseManejo;
import play.data.Upload;
import utils.Configuracoes;
import utils.FileManager;
import utils.Mensagem;

import java.io.IOException;

public class AnalisesManejo extends InternalController {

	public static void uploadAnexo(Long id, Upload file) throws IOException {

		returnIfNull(id, "Long");
		returnIfNull(file, "Upload");

		AnaliseManejo analise = AnaliseManejo.findById(id);
		String path = analise.saveAnexo(file);

		renderText(path);
	}

	public static void deleteAnexo(String token) {

		returnIfNull(token, "String");

		FileManager.getInstance().deleteFile(Configuracoes.APPLICATION_ANEXO_MANEJO_FOLDER, token);

		renderMensagem(Mensagem.ANEXO_REMOVIDO_SUCESSO);
	}
}
