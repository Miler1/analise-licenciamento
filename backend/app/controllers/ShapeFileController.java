package controllers;

import async.beans.ResultadoProcessamentoShapeFile;
import async.callable.ProcessamentoShapeFile;
import enums.InformacoesNecessariasShapeEnum;
import models.Message;
import org.apache.tika.Tika;
import play.data.Upload;
import play.i18n.Messages;
import play.libs.IO;
import serializers.ProcessamentoShapeSerializer;
import utils.FileManager;

import java.io.IOException;

public class ShapeFileController extends InternalController{

	public static void enviar(Upload file) throws IOException {

		String realType = null;

		// Detecta o tipo de arquivo pela assinatura (Magic)
		Tika tika = new Tika();
		realType = tika.detect(file.asFile());

		returnIfNotFound(realType);

		if(realType.contains("application/zip") || realType.contains("application/x-rar-compressed")) {

			byte[] data = IO.readContent(file.asFile());
			String extension = FileManager.getInstance().getFileExtention(file.getFileName());
			String key = FileManager.getInstance().createFile(data, extension);

			// Processamento do arquivo zip
			ProcessamentoShapeFile processamentoShapeFile = new ProcessamentoShapeFile(file.asFile(), key, true, InformacoesNecessariasShapeEnum.TABELA_REGIAO_DESMATADA, null);

			// Executa o processamento
			ResultadoProcessamentoShapeFile resultadoProcessamentoShapeFile = async(processamentoShapeFile);

			renderJSON(new Message<ResultadoProcessamentoShapeFile>(Messages.get("list.sucesso"), resultadoProcessamentoShapeFile), ProcessamentoShapeSerializer.listResultado);

		}else {

			renderError(Messages.get(Messages.get("erro.upload.documento")));
		}

	}
}
