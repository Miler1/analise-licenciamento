package controllers;

import async.beans.ResultadoProcessamentoShapeFile;
import async.callable.ProcessamentoShapeFile;
import enums.InformacoesNecessariasShapeEnum;
import main.java.br.ufla.lemaf.beans.pessoa.Tipo;
import models.*;
import models.licenciamento.Empreendimento;
import models.licenciamento.Municipio;
import org.apache.tika.Tika;
import play.Logger;
import play.data.Upload;
import play.i18n.Messages;
import play.libs.IO;
import serializers.ProcessamentoShapeSerializer;
import sun.net.www.content.text.Generic;
import utils.FileManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import utils.GeoCalc;

public class ShapeFileController extends InternalController {

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
			/** TODO oisouothiago - Após o merge trazer o Municipio pra validação no construtor **/
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
			empreendimento.possuiAnexo = false;
			empreendimento.save();
		} else {

			List<TipoAreaGeometria> tiposArea = TipoAreaGeometria.findAll();

			geometrias.listaGeometrias.forEach(g -> {

				TipoAreaGeometria tipoAreaGeometria = tiposArea.stream()
						.filter(ta -> ta.codigo.equals(g.type))
						.findAny()
						.orElse(null);

				if(tipoAreaGeometria != null){

					Empreendimento empreendimento = Empreendimento.buscaEmpreendimentoByCpfCnpj(geometrias.cpfCnpjEmpreendimento);
					empreendimento.possuiAnexo = true;
					empreendimento.save();

					AnaliseGeoAnexo novoAnexo = new AnaliseGeoAnexo(empreendimento, tipoAreaGeometria, g.geometry, GeoCalc.area(g.geometry)/10000);
					novoAnexo.save();

				}

			});
		}

	}

}
