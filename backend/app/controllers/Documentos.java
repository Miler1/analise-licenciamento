package controllers;

import exceptions.ValidacaoException;
import models.*;
import org.apache.commons.io.FileUtils;
import org.apache.tika.Tika;
import play.data.Upload;
import play.libs.IO;
import play.mvc.Http;
import security.Acao;
import utils.FileManager;
import utils.Mensagem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Documentos extends InternalController {

   public static void download(Long id) throws FileNotFoundException {

		verificarPermissao(Acao.BAIXAR_DOCUMENTO);

		returnIfNull(id, "Long");

		Documento documento = Documento.findById(id);
	   
		if(documento != null) {
		   File documentoBinary = documento.getFile();
		   renderBinary(new FileInputStream(documentoBinary), documentoBinary.getName(), true);
	   }

		renderMensagem(Mensagem.DOCUMENTO_NAO_ENCONTRADO);
	   
   }

	public static void downloadParecerTecnico(Long idAnalisetecnica) throws FileNotFoundException {

		verificarPermissao(Acao.BAIXAR_DOCUMENTO);

		AnaliseTecnica analiseTecnica = AnaliseTecnica.findById(idAnalisetecnica);

		ParecerAnalistaTecnico parecerAnalistaTecnico = ParecerAnalistaTecnico.getUltimoParecer(analiseTecnica.pareceresAnalistaTecnico);

		returnIfNull(parecerAnalistaTecnico.documentoParecer.id, "Long");

		Documento documento = Documento.findById(parecerAnalistaTecnico.documentoParecer.id);

		if(documento != null) {
			File documentoBinary = documento.getFile();
			renderBinary(new FileInputStream(documentoBinary), documentoBinary.getName(), true);
		}

		renderMensagem(Mensagem.DOCUMENTO_NAO_ENCONTRADO);

	}

	public static void downloadMinutaByIdAnaliseTecnica(Long idAnalisetecnica) throws FileNotFoundException {

		verificarPermissao(Acao.BAIXAR_DOCUMENTO_MINUTA);

		AnaliseTecnica analiseTecnica = AnaliseTecnica.findById(idAnalisetecnica);

		ParecerAnalistaTecnico parecerAnalistaTecnico = ParecerAnalistaTecnico.getUltimoParecer(analiseTecnica.pareceresAnalistaTecnico);

		download(parecerAnalistaTecnico.documentoMinuta.id);

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

	public static void downloadTmp(String key) throws FileNotFoundException {

		returnIfNull(key, "String");

		File file = FileManager.getInstance().getFile(key);

		if(file != null && file.exists()) {

			renderBinary(new FileInputStream(file), file.getName(), true);

		}

		throw new ValidacaoException(Mensagem.DOCUMENTO_NAO_ENCONTRADO);

	}

	public static void deleteTmp(String key) throws IOException {

		File file = FileManager.getInstance().getFile(key);

		if(file == null || !file.exists()) {

			throw new ValidacaoException(Mensagem.DOCUMENTO_NAO_ENCONTRADO);

		}

		FileUtils.deleteDirectory(file.getParentFile());

		renderMensagem(Mensagem.DOCUMENTO_DELETADO_COM_SUCESSO);

	}

	public static void downloadRTVByIdAnaliseTecnica(Long idAnalisetecnica) throws FileNotFoundException {

		verificarPermissao(Acao.BAIXAR_DOCUMENTO_RELATORIO_TECNICO_VISTORIA);

		AnaliseTecnica analiseTecnica = AnaliseTecnica.findById(idAnalisetecnica);

		ParecerAnalistaTecnico parecerAnalistaTecnico = ParecerAnalistaTecnico.getUltimoParecer(analiseTecnica.pareceresAnalistaTecnico);

		download(parecerAnalistaTecnico.vistoria.documentoRelatorioTecnicoVistoria.id);

	}

	public static void downloadPDFNotificacao(AnaliseTecnica analiseTecnica) throws Exception {

		analiseTecnica.analise = Analise.findById(analiseTecnica.analise.id);

		List<Notificacao> notificacoes = Notificacao.gerarNotificacoesTemporarias(analiseTecnica);

		Documento pdfNotificacao = Notificacao.gerarPDF(notificacoes, analiseTecnica);

		String nome = pdfNotificacao.tipo.nome +  "_" + analiseTecnica.id + ".pdf";
		nome = nome.replace(' ', '_');
		response.setHeader("Content-Disposition", "attachment; filename=" + nome);
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Content-Type", "application/pdf");

		renderBinary(pdfNotificacao.arquivo, nome);

	}

}
