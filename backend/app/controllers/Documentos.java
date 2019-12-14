package controllers;

import exceptions.ValidacaoException;
import models.Documento;
import org.apache.tika.Tika;
import play.data.Upload;
import play.libs.IO;
import play.mvc.Http;
import security.Acao;
import utils.FileManager;
import utils.Mensagem;

import java.io.File;
import java.io.IOException;

public class Documentos extends InternalController {

   public static void download(Long id) {

		verificarPermissao(Acao.BAIXAR_DOCUMENTO);

		returnIfNull(id, "Long");

		Documento documento = Documento.findById(id);
	   
		if(documento != null) {
		   File documentoBinary = documento.getFile();
		   renderBinary(documentoBinary, documentoBinary.getName());
		}

		renderMensagem(Mensagem.DOCUMENTO_NAO_ENCONTRADO);
	   
   }

	public static void upload(Upload file) throws IOException {

		verificarPermissao(Acao.ANALISAR_PROCESSO_MANEJO);

		returnIfNull(file, "Upload");

		String realType = null;

		// Detecta o tipo de arquivo pela assinatura (Magic)
		Tika tika = new Tika();
		realType = tika.detect(file.asFile());

		if(realType == null){
			response.status = Http.StatusCode.INTERNAL_ERROR;
			renderMensagem(Mensagem.UPLOAD_EXTENSAO_NAO_SUPORTADA_SHAPE);
		}

		if(realType.contains("application/zip")) {

			byte[] data = IO.readContent(file.asFile());
			String extension = FileManager.getInstance().getFileExtention(file.getFileName());
			String key = FileManager.getInstance().createFile(data, extension);

			renderText(key);

		} else {

			response.status = Http.StatusCode.INTERNAL_ERROR;
			renderMensagem(Mensagem.UPLOAD_ERRO);
		}
	}

	public static void downloadTmp(String key, String nome) {

		returnIfNull(key, "String");

		File file = FileManager.getInstance().getFile(key);

		if(file != null && file.exists()) {

			renderBinary(file, nome);
		}

		throw new ValidacaoException(Mensagem.DOCUMENTO_NAO_ENCONTRADO);
	}

	public static void deleteTmp(String key) {

		File file = FileManager.getInstance().getFile(key);

		if(file == null || !file.exists()) {

			throw new ValidacaoException(Mensagem.DOCUMENTO_NAO_ENCONTRADO);
		}

		file.delete();

		renderMensagem(Mensagem.DOCUMENTO_DELETADO_COM_SUCESSO);
	}

}
