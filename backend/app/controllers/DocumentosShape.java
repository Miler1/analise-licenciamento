package controllers;

import org.apache.tika.Tika;
import play.data.Upload;
import play.libs.IO;
import play.mvc.Http;
import security.Acao;
import utils.Configuracoes;
import utils.FileManager;
import utils.Mensagem;

import java.io.IOException;

public class DocumentosShape extends InternalController {

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

		if(realType.contains("application/zip")) {

			byte[] data = IO.readContent(file.asFile());
			String extension = FileManager.getInstance().getFileExtention(file.getFileName());
			String key = FileManager.getInstance().createFile(Configuracoes.ARQUIVOS_SHAPE_MANEJO, file.getFileName(), data, extension);

			renderText(key);

		} else {

			response.status = Http.StatusCode.INTERNAL_ERROR;
			renderMensagem(Mensagem.UPLOAD_ERRO);

		}

	}

	
}
