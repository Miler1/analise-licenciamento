package controllers;

import models.manejoDigital.AnaliseTecnicaManejo;
import models.manejoDigital.DocumentoImovelManejo;
import org.apache.tika.Tika;
import play.data.Upload;
import play.libs.IO;
import play.mvc.Http;
import security.Acao;
import serializers.DocumentosImovelManejoSerializer;
import utils.Configuracoes;
import utils.FileManager;
import utils.Mensagem;

import java.io.File;
import java.io.IOException;

public class DocumentosImovelManejo extends InternalController {

	public static void upload(Upload file, Long idAnaliseTecnica) throws IOException {

		verificarPermissao(Acao.ANALISAR_PROCESSO_MANEJO);

		returnIfNull(file, "Upload");
		returnIfNull(idAnaliseTecnica, "Long");

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

			AnaliseTecnicaManejo analiseTecnica = AnaliseTecnicaManejo.findById(idAnaliseTecnica);
			notFoundIfNull(analiseTecnica);

			DocumentoImovelManejo documento = analiseTecnica.saveDocumentoImovel(file);

			renderJSON(documento, DocumentosImovelManejoSerializer.upload);

		} else {

			response.status = Http.StatusCode.INTERNAL_ERROR;
			renderMensagem(Mensagem.UPLOAD_ERRO);

		}

	}

	public static void download(Long id) {

		verificarPermissao(Acao.ANALISAR_PROCESSO_MANEJO);

		returnIfNull(id, "Long");
		DocumentoImovelManejo documento = DocumentoImovelManejo.findById(id);
		returnIfNull(documento, "DocumentoImovelManejo");

		File file = documento.getFile();

		if(file != null && file.exists()) {

			renderBinary(file, file.getName());
		}

		renderMensagem(Mensagem.DOCUMENTO_NAO_ENCONTRADO);

	}

	public static void delete(Long id) {

		verificarPermissao(Acao.ANALISAR_PROCESSO_MANEJO);

		returnIfNull(id, "Long");
		DocumentoImovelManejo documento = DocumentoImovelManejo.findById(id);
		returnIfNull(documento, "DocumentoImovelManejo");

		documento.delete();

		renderMensagem(Mensagem.DOCUMENTO_DELETADO_COM_SUCESSO);

	}
}
