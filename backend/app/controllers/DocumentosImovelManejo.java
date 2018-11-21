package controllers;

import org.apache.tika.Tika;
import play.data.Upload;
import play.libs.IO;
import play.mvc.Http;
import security.Acao;
import utils.Configuracoes;
import utils.FileManager;
import utils.Mensagem;

import java.io.File;
import java.io.IOException;

public class DocumentosImovelManejo extends InternalController {

	public static void upload(Upload file) throws IOException {

		verificarPermissao(Acao.ANALISAR_PROCESSO_MANEJO);

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
			String key = FileManager.getInstance().createFile(Configuracoes.ARQUIVOS_IMOVEL_MANEJO, file.getFileName(), data, extension);

			renderText(key);

		} else {

			response.status = Http.StatusCode.INTERNAL_ERROR;
			renderMensagem(Mensagem.UPLOAD_ERRO);

		}

	}

	public static void downloadTmp(String key) {

		verificarPermissao(Acao.ANALISAR_PROCESSO_MANEJO);

		returnIfNull(key, "String");

		File file = FileManager.getInstance().getFile(key, Configuracoes.ARQUIVOS_IMOVEL_MANEJO);

		if(file != null && file.exists()) {

			renderBinary(file, file.getName());
		}

		renderMensagem(Mensagem.DOCUMENTO_NAO_ENCONTRADO);

	}

	public static void deleteTmp(String key) {

		verificarPermissao(Acao.ANALISAR_PROCESSO_MANEJO);

		File file = FileManager.getInstance().getFile(key, Configuracoes.ARQUIVOS_IMOVEL_MANEJO);

		if(file == null || !file.exists()) {

			renderMensagem(Mensagem.DOCUMENTO_NAO_ENCONTRADO);
		}

		file.delete();

		renderMensagem(Mensagem.DOCUMENTO_DELETADO_COM_SUCESSO);

	}
}
