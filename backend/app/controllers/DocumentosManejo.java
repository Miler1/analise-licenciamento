package controllers;

import models.manejoDigital.AnaliseTecnicaManejo;
import models.manejoDigital.DocumentoManejo;
import org.apache.tika.Tika;
import play.data.Upload;
import play.mvc.Http;
import security.Acao;
import serializers.DocumentosManejoSerializer;
import utils.Mensagem;

import java.io.File;
import java.io.IOException;

public class DocumentosManejo extends InternalController {

	public static void uploadFileImovel(Upload file, Long idAnaliseTecnica) throws IOException {

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

			DocumentoManejo documento = analiseTecnica.saveDocumentoImovel(file);

			renderJSON(documento, DocumentosManejoSerializer.upload);

		} else {

			response.status = Http.StatusCode.INTERNAL_ERROR;
			renderMensagem(Mensagem.UPLOAD_ERRO);

		}

	}

	public static void uploadFileComplementar(Upload file, Long idAnaliseTecnica) throws IOException {

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

			DocumentoManejo documento = analiseTecnica.saveDocumentoComplementar(file);

			renderJSON(documento, DocumentosManejoSerializer.upload);

		} else {

			response.status = Http.StatusCode.INTERNAL_ERROR;
			renderMensagem(Mensagem.UPLOAD_ERRO);

		}

	}

	public static void download(Long id) {

		verificarPermissao(Acao.ANALISAR_PROCESSO_MANEJO);

		returnIfNull(id, "Long");
		DocumentoManejo documento = DocumentoManejo.findById(id);
		returnIfNull(documento, "DocumentoManejo");

		File file = documento.getFile();

		if(file != null && file.exists()) {

			renderBinary(file, file.getName());
		}

		renderMensagem(Mensagem.DOCUMENTO_NAO_ENCONTRADO);

	}

	public static void delete(Long id) {

		verificarPermissao(Acao.ANALISAR_PROCESSO_MANEJO);

		returnIfNull(id, "Long");
		DocumentoManejo documento = DocumentoManejo.findById(id);
		returnIfNull(documento, "DocumentoManejo");

		documento.delete();

		renderMensagem(Mensagem.DOCUMENTO_DELETADO_COM_SUCESSO);

	}
}
