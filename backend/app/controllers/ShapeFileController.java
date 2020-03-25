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
import java.util.concurrent.atomic.AtomicReference;

import utils.GeoCalc;

public class ShapeFileController extends InternalController {

	public static void enviar(Upload file, Long idMunicipio, Long idEmpreendimento) throws IOException {

		String realType = null;

		// Detecta o tipo de arquivo pela assinatura (Magic)
		Tika tika = new Tika();
		realType = tika.detect(file.asFile());

		returnIfNotFound(realType);

		if(realType.contains("application/zip") || realType.contains("application/x-rar-compressed")) {

			byte[] data = IO.readContent(file.asFile());
			String key = FileManager.getInstance().createKey(data, file.getFileName());

			// Processamento do arquivo zip
			ProcessamentoShapeFile processamentoShapeFile = new ProcessamentoShapeFile(file.asFile(), key, true, InformacoesNecessariasShapeEnum.APENAS_GEOMETRIA, idMunicipio, idEmpreendimento);

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
			empreendimento.possuiShape = false;
			empreendimento.save();

			List<EmpreendimentoCamandaGeo> empreendimentoCamandasGeo =  EmpreendimentoCamandaGeo.find("id_empreendimento = :id_empreendimento")
					.setParameter("id_empreendimento", empreendimento.id).fetch();

			empreendimentoCamandasGeo.forEach( empreendimentoCamadaGeo -> empreendimentoCamadaGeo._delete());

		} else {

			List<TipoAreaGeometria> tiposArea = TipoAreaGeometria.findAll();

			Empreendimento empreendimento = Empreendimento.buscaEmpreendimentoByCpfCnpj(geometrias.cpfCnpjEmpreendimento);

			List<EmpreendimentoCamandaGeo> empreendimentoCamandasGeo =  EmpreendimentoCamandaGeo.find("id_empreendimento = :id_empreendimento")
					.setParameter("id_empreendimento", empreendimento.id).fetch();

			AtomicReference<Boolean> removida = new AtomicReference<>(true);

			empreendimentoCamandasGeo.forEach( empreendimentoCamadaGeo -> {

				geometrias.listaGeometrias.forEach(geometria -> {
					if(empreendimentoCamadaGeo.id == geometria.id) {
						removida.set(false);
					}
				});

				if(removida.get()) {

					empreendimentoCamadaGeo._delete();
				}

				removida.set(true);

			});

			geometrias.listaGeometrias.forEach(g -> {

				if(g.id == null) {

					TipoAreaGeometria tipoAreaGeometria = tiposArea.stream()
							.filter(ta -> ta.codigo.equals(g.type))
							.findAny()
							.orElse(null);

					if(tipoAreaGeometria != null){


						empreendimento.possuiShape = true;
						empreendimento.save();

						EmpreendimentoCamandaGeo novoAnexo = new EmpreendimentoCamandaGeo(empreendimento, tipoAreaGeometria, g.geometry, GeoCalc.area(g.geometry) / 10000);
						novoAnexo.save();

					}
				}

			});
		}

	}

}
