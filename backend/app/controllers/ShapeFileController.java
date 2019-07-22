package controllers;

import async.beans.ResultadoProcessamentoShapeFile;
import async.callable.ProcessamentoShapeFile;
import enums.InformacoesNecessariasShapeEnum;
import models.*;
import models.licenciamento.Empreendimento;
import org.apache.tika.Tika;
import play.data.Upload;
import play.i18n.Messages;
import play.libs.IO;
import serializers.ProcessamentoShapeSerializer;
import utils.FileManager;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ShapeFileController extends GenericController {

	public static void enviar(Upload file, Long idMunicipio) throws IOException {

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
			ProcessamentoShapeFile processamentoShapeFile = new ProcessamentoShapeFile(file.asFile(), key, true, InformacoesNecessariasShapeEnum.APENAS_GEOMETRIA, idMunicipio);

			// Executa o processamento
			ResultadoProcessamentoShapeFile resultadoProcessamentoShapeFile = async(processamentoShapeFile);

			renderJSON(new Message<ResultadoProcessamentoShapeFile>(Messages.get("list.sucesso"), resultadoProcessamentoShapeFile), ProcessamentoShapeSerializer.listResultado);

		}else {

			renderError(Messages.get(Messages.get("erro.upload.documento")));
		}

	}

	public static void salvarGeometrias(ListShapeContentVO geometrias) {

		if(geometrias.naoTemShapes) {
			Empreendimento empreendimento = Empreendimento.buscaEmpreendimentoByCpfCnpj(geometrias.cpfCnpjEmpreendimento);
			empreendimento.possui_shape = false;
			empreendimento.save();
		} else {


			geometrias.listaGeometrias.forEach(g -> {

				List<TipoAreaGeometria> tiposArea = TipoAreaGeometria.findAll();

				TipoAreaGeometria tipoAreaGeometria = null;
				tiposArea = tiposArea.stream().filter(ta -> {
					return ta.codigo.equals(g.type);
				}).collect(Collectors.toList());

				/* Atualmente, um shape só pode ter um tipo de área */
				if(tiposArea.size() > 0){
					tipoAreaGeometria = tiposArea.get(0);
				}

				if(tipoAreaGeometria != null){

					Empreendimento empreendimento = Empreendimento.buscaEmpreendimentoByCpfCnpj(geometrias.cpfCnpjEmpreendimento);
					empreendimento.possui_shape = true;
					empreendimento.save();

					AnalistaGeoAnexo novoAnexo = new AnalistaGeoAnexo(empreendimento, tipoAreaGeometria, g.geometry);
					novoAnexo.save();

				}

			});
		}

	}

}
