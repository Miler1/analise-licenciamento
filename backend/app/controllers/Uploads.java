package controllers;

import org.apache.tika.Tika;
import play.data.Upload;
import play.libs.IO;
import play.mvc.Http;
import utils.FileManager;
import utils.Mensagem;

import java.io.IOException;

public class Uploads extends InternalController {

	/**
	 * Upload de arquivos, copia o arquivo enviado para uma área temporária
	 * do sistema e gera uma chave de retorno para que seja possível obter o arquivo
	 * quando necessário.
	 * @param file
	 * @throws IOException
	 */
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