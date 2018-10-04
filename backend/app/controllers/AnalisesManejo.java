package controllers;

import models.manejoDigital.AnaliseManejo;
import play.data.Upload;
import security.Acao;
import serializers.AnalisesManejoSerializer;
import utils.Configuracoes;
import utils.FileManager;
import utils.Mensagem;

import java.io.IOException;

public class AnalisesManejo extends InternalController {

	public static void uploadAnexo(Long id, Upload file) throws IOException {

		verificarPermissao(Acao.ANALISAR_PROCESSO_MANEJO);

		returnIfNull(id, "Long");
		returnIfNull(file, "Upload");

		AnaliseManejo analise = AnaliseManejo.findById(id);
		String path = analise.saveAnexo(file);

		renderText(path);
	}

	public static void deleteAnexo(String token) {

		verificarPermissao(Acao.ANALISAR_PROCESSO_MANEJO);

		returnIfNull(token, "String");

		FileManager.getInstance().deleteFile(Configuracoes.APPLICATION_ANEXO_MANEJO_FOLDER, token);

		renderMensagem(Mensagem.ANEXO_REMOVIDO_SUCESSO);
	}

	public static void findById(Long id) {

		verificarPermissao(Acao.ANALISAR_PROCESSO_MANEJO);

		returnIfNull(id, "Long");

		AnaliseManejo analise = AnaliseManejo.findById(id);

		renderJSON(analise, AnalisesManejoSerializer.findById);
	}
}
