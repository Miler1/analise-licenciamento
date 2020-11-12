package async.callable;

import async.beans.AtributoShape;
import async.beans.ResultadoProcessamentoShapeFile;
import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
import com.vividsolutions.jts.geom.Geometry;
import enums.InformacoesNecessariasShapeEnum;
import models.licenciamento.Empreendimento;
import models.licenciamento.Municipio;
import org.apache.commons.io.FileUtils;
import org.apache.tika.Tika;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import play.Logger;
import play.Play;
import play.i18n.Messages;
import services.IntegracaoEntradaUnicaService;
import utils.Configuracoes;
import utils.GeoCalc;
import utils.GeoJsonUtils;
import utils.StringUtils;

import java.io.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ProcessamentoShapeFile implements Callable<ResultadoProcessamentoShapeFile> {

	private File arquivoZip;
	private File arquivoRar;
	private String keyTemp;
	private ZipEntry zipShp;
	private ZipEntry zipDbf;
	private ZipEntry zipShx;
	private ZipEntry zipPrj;
	private FileHeader rarShp;
	private FileHeader rarDbf;
	private FileHeader rarShx;
	private FileHeader rarPrj;
	private ResultadoProcessamentoShapeFile resultado;
	private boolean incluiGeometriaNoResultado;
	private InformacoesNecessariasShapeEnum informacoesNecessarias;
	private Long idMunicipio;
	private Long idEmpreendimento;

	private static final int BUFFER_SIZE = Integer.valueOf(Play.configuration.getProperty("sistema.constTamanhoMaximoArquivoUpload"));

	public ProcessamentoShapeFile(File arquivo, String keyTemp, boolean incluiGeometriaNoResultado, InformacoesNecessariasShapeEnum informacoesNecessarias, Long idMunicipio, Long idEmpreendimento) throws IOException {

		String realType = null;

		// Detecta o tipo de arquivo pela assinatura (Magic)
		Tika tika = new Tika();
		realType = tika.detect(arquivo);

		if (realType.contains("application/zip")) {

			this.arquivoZip = arquivo;
		} else {
			this.arquivoRar = arquivo;
		}
		this.keyTemp = keyTemp;
		this.incluiGeometriaNoResultado = incluiGeometriaNoResultado;
		this.resultado = new ResultadoProcessamentoShapeFile();
		this.resultado.dados.keyTemp = keyTemp;
		this.resultado.mensagens = new ArrayList<String>();
		this.informacoesNecessarias = informacoesNecessarias;
		this.idMunicipio = idMunicipio;
		this.idEmpreendimento = idEmpreendimento;
	}

	@Override
	public ResultadoProcessamentoShapeFile call() throws Exception {

		boolean isZip = this.arquivoZip != null;

		boolean arquivoExtraido;

		if (isZip) {
			arquivoExtraido = this.arquivoShapeExtraido(this.arquivoZip);
		} else {
			arquivoExtraido = this.arquivoShapeExtraido(this.arquivoRar);
		}

		if (!arquivoExtraido) {
			if (isZip) {

				this.extrairArquivosShapeZip(this.arquivoZip);
			} else {
				this.extrairArquivosShapeRar(this.arquivoRar);
			}
		}

		if (this.resultado.status != ResultadoProcessamentoShapeFile.Status.ERRO) {
			this.processarArquivosShape();
		}
		if (this.resultado.status != ResultadoProcessamentoShapeFile.Status.ERRO) {
			this.validarAtributosProcessados();
		}
		if (this.resultado.status != ResultadoProcessamentoShapeFile.Status.ERRO) {
			this.validarGeometriasSobrepostas();
		}
		if (this.resultado.status != ResultadoProcessamentoShapeFile.Status.ERRO) {
			this.validarGeometriaDentroEmpreendimento();
		}
		if (this.informacoesNecessarias == InformacoesNecessariasShapeEnum.APENAS_GEOMETRIA) {
			if (this.resultado.status != ResultadoProcessamentoShapeFile.Status.ERRO) {
				//O Analista não precisa de um shape vinculado ao usuário logado
				//Mas é preciso validar em função do
				this.validarGeometriaMunicipioEmpreendimento();
			}
		}

		if (this.resultado.status == null) {

			this.resultado.status = ResultadoProcessamentoShapeFile.Status.SUCESSO;
			this.resultado.mensagens.add(Messages.get("success.shapefile"));
		} else {

			this.resultado.dados = null;
		}

		this.removeArquivosTemporarios();

		return this.resultado;
	}

	private void validarGeometriaDentroEmpreendimento() {

		boolean isForaEmpreendimento = false;

		if (this.idEmpreendimento != null) {

			Empreendimento empreendimentoBanco = Empreendimento.findById(this.idEmpreendimento);
			IntegracaoEntradaUnicaService integracaoEntradaUnicaService = new IntegracaoEntradaUnicaService();
			br.ufla.lemaf.beans.Empreendimento empreendimentoEU = integracaoEntradaUnicaService.findEmpreendimentosByCpfCnpj(empreendimentoBanco.cpfCnpj);
			Geometry geometriaEmpreendimento = GeoJsonUtils.toGeometry(empreendimentoEU.localizacao.geometria);

			isForaEmpreendimento = getTodasAsGeometriasDoShape()
					.stream()
					.anyMatch(geometriaShape -> !geometriaEmpreendimento.contains(geometriaShape));
		}

		if (isForaEmpreendimento) {

			this.fireError(Messages.get("error.shapefile.fora.empreendimento"), null);
			return;
		}
	}

	/**
	 * Valida se existem geometrias sobrepostas entre si no shape
	 */
	private void validarGeometriasSobrepostas() {

		boolean isAreasSobrepostas = false;

		List<Geometry> geometriasDoShapeFile = getTodasAsGeometriasDoShape();

		String detalhesSobreposicao = "";

		for (int x = 0; x < geometriasDoShapeFile.size(); x++) {

			int countSobreposicao = 0;

			for (int y = 0; y < geometriasDoShapeFile.size(); y++) {

				if (x == y) {

					countSobreposicao++;
				} else if (geometriasDoShapeFile.get(x).intersects(geometriasDoShapeFile.get(y))) {

					Geometry interseccoes = geometriasDoShapeFile.get(x).intersection(geometriasDoShapeFile.get(y));
					int quantidadeInterseccoes = interseccoes.getNumGeometries();
					double somaAreaInterseccoes = 0;
					for (int i = 0; i < quantidadeInterseccoes; i++) {
						somaAreaInterseccoes += (GeoCalc.area(interseccoes.getGeometryN(i))) / 10000;
					}

					if (somaAreaInterseccoes > 0.1) {
						countSobreposicao++;
					}
				}

				if (countSobreposicao > 1) {

					Logger.info("Sobreposição detectada entre os registros: [ " + this.resultado.dados.registros.get(x).get(1).valor + " ] e [ " + this.resultado.dados.registros.get(y).get(1).valor + " ]");

					detalhesSobreposicao = "[ " + this.resultado.dados.registros.get(x).get(1).valor + " ] e [ " + this.resultado.dados.registros.get(y).get(1).valor + " ]";

					isAreasSobrepostas = true;
					break;
				}
			}

			if (isAreasSobrepostas) {

				break;
			}
		}

		if (isAreasSobrepostas) {

			this.fireError(Messages.get("error.shapefile.attributes.sobreposicao") + " " + detalhesSobreposicao, null);
			return;
		}
	}

	/**
	 * Obtem todas as áreas (geometrias) processadas do shape
	 *
	 * @return
	 */
	private List<Geometry> getTodasAsGeometriasDoShape() {

		List<Geometry> geometriasDoShapeFile = new ArrayList<Geometry>();

		if (this.resultado.dados == null || this.resultado.dados.registros == null || this.resultado.dados.registros.isEmpty()) {

			return geometriasDoShapeFile;
		}

		for (List<AtributoShape> registro : this.resultado.dados.registros) {

			for (AtributoShape atributo : registro) {

				if (atributo.valor != null && atributo.valor instanceof Geometry) {

					Geometry geometria = (Geometry) atributo.valor;
					geometria.setSRID(Configuracoes.SRID_DEFAULT);
					geometriasDoShapeFile.add(geometria);
				}
			}
		}

		return geometriasDoShapeFile;
	}

	/**
	 * Valida os atributos processados com base no contador dos tipos encontrados.
	 * - Compara o contador dos tipos de atributos encontrados na tabela RegiaoDesmatada (Atributos Obrigatórios)
	 * com o contador dos tipos de atributos encontrados no shapefile.
	 */
	private void validarAtributosProcessados() {

		TipoContadorAtributoShape contadorTiposAtributosShape = this.contaTiposAtributosShape();

		if (contadorTiposAtributosShape.atributosVazios()) {
			this.fireError(Messages.get("error.shapefile.attributes"), null);
		} else {
			return;
		}

	}

	private void validarGeometriaMunicipioEmpreendimento() {

		if (this.idMunicipio != null) {

			Municipio municipio = Municipio.findById(this.idMunicipio);
			List<Geometry> geometriasDoShapeFile = getTodasAsGeometriasDoShape();

			geometriasDoShapeFile = geometriasDoShapeFile.stream().map(gds -> {
				if (!municipio.limite.contains(gds)) {
					Geometry interseccao = municipio.limite.intersection(gds);
					return interseccao;
				}
				else {
					return gds;
				}
			}).collect(Collectors.toList());

			if (geometriasDoShapeFile.stream().anyMatch(gds -> gds.isEmpty())) {
				this.fireError(Messages.get("error.shapefile.attributes.foraMunicipio"), null);
			}else {
				/** TODO oisouothiago - Encontrar o conteúdo alterado da lista e salvar nos registros! **/
				List<List<AtributoShape>> registrosAlterados = new ArrayList<List<AtributoShape>>();

				for (List<AtributoShape> registro : this.resultado.dados.registros) {

					for (AtributoShape atributo : registro) {

						if (atributo.valor != null && atributo.valor instanceof Geometry) {

							Geometry geometria = (Geometry) atributo.valor;
							geometria.setSRID(Configuracoes.SRID_DEFAULT);
							geometriasDoShapeFile.add(geometria);
						}
					}

					registrosAlterados.add(registro);
				}

				this.resultado.dados.registros = registrosAlterados;
			}
		}
	}

	/**
	 * Retorna o contador para cada tipo de atributo encontrado
	 * na tabela (atributos obrigatórios).
	 * @return
	 */
//	private TipoContadorAtributoShape contaTiposAtributosTabelaRegiaoDesmatada() {
//
//		TipoContadorAtributoShape tipoContadorAtributoTabela = new TipoContadorAtributoShape();
//
//		/**
//		 * Considera somente os atributos com anotação Required para o contador
//		 * de tipos.
//		 */
//		for(Field field : RegiaoDesmatada.class.getFields()) {
//
//			if (field.getName().equals("id")) {
//
//				continue;
//			}
//
//			for (Annotation annotation : field.getAnnotations()) {
//
//				if (annotation instanceof ColumnShapeFile) {
//
//					if (field.getType().equals(Geometry.class)) {
//
//						tipoContadorAtributoTabela.cGeometry++;
//
//					} else if (field.getType().equals(Long.class)) {
//
//						tipoContadorAtributoTabela.cLong++;
//
//					} else if (field.getType().equals(Double.class)) {
//
//						tipoContadorAtributoTabela.cDouble++;
//
//					} else if (field.getType().equals(String.class)) {
//
//						tipoContadorAtributoTabela.cString++;
//
//					} else if(field.getType().equals(Date.class)) {
//
//						tipoContadorAtributoTabela.cDate++;
//					}
//
//				}
//			}
//		}
//
//		return tipoContadorAtributoTabela;
//	}

	/**
	 * Retorna o contador para cada tipo de atributo encontrado
	 * na tabela (atributos obrigatórios).
	 *
	 * @return
	 */
	private TipoContadorAtributoShape contaTiposAtributosApenasGeometria() {

		TipoContadorAtributoShape tipoContadorAtributoTabela = new TipoContadorAtributoShape();

		tipoContadorAtributoTabela.cGeometry = 1;

		return tipoContadorAtributoTabela;
	}

	/**
	 * Retorna o contador para cada tipo de atributo encontrado
	 * no shapefile.
	 *
	 * @return
	 */
	private TipoContadorAtributoShape contaTiposAtributosShape() {

		TipoContadorAtributoShape tipoContadorAtributoShape = new TipoContadorAtributoShape();

		if (this.resultado.dados == null || this.resultado.dados.atributos == null || this.resultado.dados.atributos.isEmpty()) {

			return tipoContadorAtributoShape;
		}

		for (AtributoShape atributoShape : this.resultado.dados.atributos) {

			if (atributoShape.tipo.equals(Geometry.class.getSimpleName())) {

				tipoContadorAtributoShape.cGeometry++;
			} else if (atributoShape.tipo.equals(Long.class.getSimpleName())) {

				tipoContadorAtributoShape.cLong++;
			} else if (atributoShape.tipo.equals(Double.class.getSimpleName())) {

				tipoContadorAtributoShape.cDouble++;
			} else if (atributoShape.tipo.equals(String.class.getSimpleName())) {

				tipoContadorAtributoShape.cString++;
			} else if (atributoShape.tipo.equals(Date.class.getSimpleName())) {

				tipoContadorAtributoShape.cDate++;
			}
		}

		return tipoContadorAtributoShape;
	}


	/**
	 * Retorna se o arquivo shapefile .sh existe
	 *
	 * @return
	 */
	private boolean arquivoShapeExtraido(File arquivo) {

		boolean todosArquivosExistem = true;

		String arquivoShp = arquivo.getParent() + "/" + this.keyTemp + ".shp";

		String arquivoDbf = arquivo.getParent() + "/" + this.keyTemp + ".dbf";

		String arquivoShx = arquivo.getParent() + "/" + this.keyTemp + ".shx";

		String arquivoPrj = arquivo.getParent() + "/" + this.keyTemp + ".prj";

		if (!new File(arquivoShp).exists()) {

			return false;
		} else if (!new File(arquivoDbf).exists()) {

			return false;
		} else if (!new File(arquivoShx).exists()) {

			return false;
		} else if (!new File(arquivoPrj).exists()) {

			return false;
		}


		return true;
	}

	private void extrairArquivosShapeZip(File arquivoZip) {

		ZipFile zipFile = null;
		InputStream ioShp = null;
		InputStream ioShx = null;
		InputStream ioDbf = null;
		InputStream ioPrj = null;

		try {

			zipFile = new ZipFile(arquivoZip);

			/**
			 * Valida o arquivo zip e obtem os arquivos
			 * necessários na estrutura do shapefile (shp,dbf,shx)
			 */
			if (!arquivoZipValido(zipFile)) {

				return;
			}

			/**
			 * Tudo ok com o arquivo zip, então vamos processar o shape
			 */
			ioShp = zipFile.getInputStream(this.zipShp);
			ioDbf = zipFile.getInputStream(this.zipDbf);
			ioShx = zipFile.getInputStream(this.zipShx);
			ioPrj = zipFile.getInputStream(this.zipPrj);

			/**
			 * Grava os arquivos shp/shx/dbf na área temporária
			 */
			String pathBaseDestino = this.arquivoZip.getParent() + "/" + this.keyTemp;

			this.writeFileFromInputStream(ioShp, pathBaseDestino + ".shp");
			this.writeFileFromInputStream(ioDbf, pathBaseDestino + ".dbf");
			this.writeFileFromInputStream(ioShx, pathBaseDestino + ".shx");
			this.writeFileFromInputStream(ioPrj, pathBaseDestino + ".prj");

			/**
			 * Finaliza conexão com o arquivo zip
			 */
			ioShp.close();
			ioDbf.close();
			ioShx.close();
			ioPrj.close();
			zipFile.close();

		} catch (Exception ex) {

			this.fireError(Messages.get("error.shapefile.invalid"), ex.getMessage());
			return;
		}
	}

	private void extrairArquivosShapeRar(File arquivoRar) throws IOException, RarException {

		/**
		 * Grava os arquivos shp/shx/dbf na área temporária
		 */
		String pathBaseDestino = arquivoRar.getParent() + "/" + this.keyTemp;


		Archive archive = new Archive(arquivoRar);


		try {

			/**
			 * Valida o arquivo zip e obtem os arquivos
			 * necessários na estrutura do shapefile (shp,dbf,shx)
			 */
			if (!arquivoRarValido(archive)) {

				return;
			}

			/**
			 * Tudo ok com o arquivo zip, então vamos processar o shape
			 */
			FileOutputStream osShp = new FileOutputStream(new File(pathBaseDestino + ".shp"));
			FileOutputStream osDbf = new FileOutputStream(new File(pathBaseDestino + ".dbf"));
			FileOutputStream osShx = new FileOutputStream(new File(pathBaseDestino + ".shx"));
			FileOutputStream osPrj = new FileOutputStream(new File(pathBaseDestino + ".prj"));
			archive.extractFile(this.rarShp, osShp);
			archive.extractFile(this.rarDbf, osDbf);
			archive.extractFile(this.rarShx, osShx);
			archive.extractFile(this.rarPrj, osPrj);

		} catch (Exception ex) {

			this.fireError(Messages.get("error.shapefile.invalid"), ex.getMessage());
			return;
		}

	}

	/**
	 * Realiza o processamento dos atributos do shapefile
	 */
	private void processarArquivosShape() {

		boolean isZip = this.arquivoZip != null;
		try {
			String pathBaseDestino;
			if (isZip) {

				pathBaseDestino = this.arquivoZip.getParent() + "/" + this.keyTemp;
			} else {
				pathBaseDestino = this.arquivoRar.getParent() + "/" + this.keyTemp;
			}


			if (this.verificarSistemasDeCoordenadas(pathBaseDestino + ".prj")) {

				/**
				 * Obtem as propriedades do shape (atributos do cabeçalho)
				 */
				this.processarAtributosShape(pathBaseDestino + ".shp");

			} else {

				this.fireError(Messages.get("error.shapefile.not.sirgas2000"), "O arquivo não segue a configuração de projeção indicada");
			}

		} catch (Exception ex) {

			ex.printStackTrace();

			Logger.error(ex.getMessage());

			if (this.resultado.mensagens.isEmpty()) {

				this.fireError(Messages.get("error.shapefile.generic"), ex.getMessage());
			}
		}
	}

	/**
	 * @param rarFile
	 * @return
	 */
	private boolean arquivoRarValido(Archive rarFile) {

		if (rarFile.getFileHeaders().size() == 0) {

			this.fireError(Messages.get("error.shapefile.empty"), "Arquivo inválido, size=0");
			return false;
		}

		List<FileHeader> headers = rarFile.getFileHeaders();

		FileHeader fileHeader;

		Iterator<FileHeader> iterator = headers.iterator();

		while (iterator.hasNext()) {

			fileHeader = iterator.next();

			Logger.info(fileHeader.getFileNameString());


			if (fileHeader.getFileNameString().toLowerCase().endsWith(".shp")) {

				this.rarShp = fileHeader;
			}

			if (fileHeader.getFileNameString().toLowerCase().endsWith(".dbf")) {

				this.rarDbf = fileHeader;
			}

			if (fileHeader.getFileNameString().toLowerCase().endsWith(".shx")) {

				this.rarShx = fileHeader;
			}

			if (fileHeader.getFileNameString().toLowerCase().endsWith(".prj")) {

				this.rarPrj = fileHeader;
			}

		}

		/**
		 * Valida o conteúdo do shapefile, conforme definição do ArcGis
		 * http://help.arcgis.com/en/arcgisdesktop/10.0/help/index.html#/Shapefile_file_extensions/005600000003000000/
		 * Esses quatro arquivos (shp,shx,dbf, prj) são requeridos na estrutura do shapefile.
		 */
		if (this.rarShp == null || this.rarDbf == null || this.rarShx == null || this.rarPrj == null) {

			this.fireError(Messages.get("error.shapefile.structure"), "O shapefile não contém todos os arquivos requeridos (shp,shx,dbf,prj)");

			return false;
		}

		return true;

	}

	/**
	 * @param zipFile
	 * @return
	 */
	private boolean arquivoZipValido(ZipFile zipFile) {

		if (zipFile.size() == 0) {

			this.fireError(Messages.get("error.shapefile.empty"), "Arquivo inválido, size=0");

			return false;
		}

		Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
		while (zipEntries.hasMoreElements()) {

			ZipEntry element = zipEntries.nextElement();

			if (element.getName().toLowerCase().endsWith(".shp")) {

				this.zipShp = element;
			}

			if (element.getName().toLowerCase().endsWith(".dbf")) {

				this.zipDbf = element;
			}

			if (element.getName().toLowerCase().endsWith(".shx")) {

				this.zipShx = element;
			}

			if (element.getName().toLowerCase().endsWith(".prj")) {

				this.zipPrj = element;
			}
		}

		/**
		 * Valida o conteúdo do shapefile, conforme definição do ArcGis
		 * http://help.arcgis.com/en/arcgisdesktop/10.0/help/index.html#/Shapefile_file_extensions/005600000003000000/
		 * Esses três arquivos (shp,shx,dbf) são requeridos na estrutura do shapefile.
		 */

		if (this.zipShp == null || this.zipDbf == null || this.zipShx == null || this.zipPrj == null) {

			this.fireError(Messages.get("error.shapefile.structure"), "O shapefile não contém todos os arquivos requeridos (shp,shx,dbf,prj)");

			return false;
		}


		return true;
	}

	/**
	 * Processa os atributos do cabeçalho a serem armazenados no resultado
	 * do processamento.
	 *
	 * @param pathShpFile - Caminho do arquivo .shp do shapefile
	 */
	private boolean processarAtributosShape(String pathShpFile) {

		try {

			File file = new File(pathShpFile);

			Map<String, Object> map = new HashMap<>();
			map.put("url", file.toURI().toURL());

			DataStore dataStore = DataStoreFinder.getDataStore(map);
			String typeName = dataStore.getTypeNames()[0];

			FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore
					.getFeatureSource(typeName);
			Filter filter = Filter.INCLUDE; // ECQL.toFilter("BBOX(THE_GEOM, 10,20,30,40)")

			FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures(filter);

			FeatureIterator<SimpleFeature> features = collection.features();

			/**
			 * Processa as features
			 */
			while (features.hasNext()) {

				SimpleFeature feature = features.next();

				if (this.resultado.dados.atributos.isEmpty()) {

					this.preencheAtributosResultado(feature);
				}

				List<AtributoShape> registro = this.preencheNovoRegistroResultado(feature);

				if (!registro.isEmpty()) {

					this.resultado.dados.registros.add(registro);
				}
			}

			features.close();

			dataStore.dispose();

			if (this.resultado.dados.atributos.isEmpty() || this.resultado.dados.registros.isEmpty()) {

				this.fireError(Messages.get("error.shapefile.attributes.empty"), "Lista de atributos/registros não contém registros");

				return false;
			}
//			else if((this.informacoesNecessarias == InformacoesNecessariasShapeEnum.APENAS_GEOMETRIA) && this.resultado.dados.registros.size() > 1){
//
//				this.fireError(Messages.get("error.poligonos.analise.upload.shapefile.multipleAttributes"), "Lista de registros contém mais de um registro no shape");
//
//				return false;
//			}

			return true;

		} catch (IOException ex) {

			this.fireError(Messages.get("error.shapefile.shp"), ex.getMessage());

			return false;
		}
	}

	/**
	 * Verifica se o sistema de coordenadas do shapefile é o SIRGAS 2000 Geometry
	 *
	 * @return
	 */
	private boolean verificarSistemasDeCoordenadas(String pathPjrFile) throws IOException {

		File file = new File(pathPjrFile);
		FileInputStream fi = new FileInputStream(file);

		byte[] bytesFile = new byte[(int) file.length()];

		fi.read(bytesFile);

		String conteudo = new String(bytesFile, "UTF-8");

		fi.close();

		DefaultGeographicCRS CRSshape = null;
		try {
			CRSshape = (DefaultGeographicCRS) CRS.parseWKT(conteudo);

			if (CRSshape == null) {

				return false;
			}

			if (CRSshape.getName() == null) {

				return false;
			}

			// A projeção deve estar em Sirgas2000 Geográfico
			String name = CRSshape.getName().toString().toLowerCase();

			if (!name.contains("sirgas 2000") && !name.contains("sirgas_2000")) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	/**
	 * Preenche os atributos a serem retornados como resultado
	 *
	 * @param feature
	 */
	private void preencheAtributosResultado(SimpleFeature feature) {

		for (Property attribute : feature.getProperties()) {

			String name = attribute.getName() != null ? attribute.getName().toString() : null;

			if (name != null) {

				AtributoShape atributo = new AtributoShape();
				atributo.nome = attribute.getName().toString();

				if (attribute.getValue() instanceof Geometry) {

					atributo.tipo = Geometry.class.getSimpleName();
				} else if (attribute.getValue() instanceof Integer) {

					atributo.tipo = Long.class.getSimpleName();
				} else {

					atributo.tipo = attribute.getType().getBinding().getSimpleName();
				}

				this.resultado.dados.atributos.add(atributo);
			}
		}
	}

	/**
	 * Preenche um registro a ser retornado como resultado
	 *
	 * @param feature
	 */
	private List<AtributoShape> preencheNovoRegistroResultado(SimpleFeature feature) {

		List<AtributoShape> registro = new ArrayList<AtributoShape>();

		for (Property attribute : feature.getProperties()) {

			String name = attribute.getName() != null ? attribute.getName().toString() : null;

			if (name != null) {

				AtributoShape atributo = new AtributoShape();

				atributo.nome = attribute.getName().toString();

				if (attribute.getValue() instanceof Geometry) {

					Geometry geometria = (Geometry) attribute.getValue();

					// Valida geometria

					if (!(geometria.getGeometryType().equals("Polygon") || geometria.getGeometryType().equals("MultiPolygon"))) {

						this.fireError(Messages.get("error.shapefile.geometry.invalid.notPolygon"), "Polígono gerado não é do tipo Polygon");
					}

					if (!geometria.isValid()) {

						//Converte geometria para o padrão OGC (altera-se a ordem dos vértices)
						Geometry geometriaPadraoOGC = geometria.buffer(0.0);

						if (geometriaPadraoOGC.isValid()) {
							//Ao alterar ordem dos vértices, pode-se ter gerado polígonos menores, com arestas novas
							if (geometria.contains(geometriaPadraoOGC) && geometriaPadraoOGC.contains(geometria)) {

								attribute.setValue(geometriaPadraoOGC);
							} else {

								this.fireError(Messages.get("error.shapefile.geometry.invalid.arestaCruzada"), "Geometria inválida.");
							}
						} else {

							this.fireError(Messages.get("error.shapefile.geometry.invalid.arestaCruzada"), "Geometria inválida.");
						}
					}

					if (geometria.getGeometryType().toLowerCase().equals("polygon") || geometria.getGeometryType().toLowerCase().equals("multipolygon")) {

						atributo.tipo = attribute.getType().getBinding().getSimpleName();

						if (this.incluiGeometriaNoResultado) {

							atributo.valor = attribute.getValue();
						} else {

							atributo.valor = null;
						}
					}
				} else if (attribute.getValue() instanceof Integer) {

					Long valor = ((Integer) attribute.getValue()).longValue();

					atributo.tipo = Long.class.getSimpleName();
					atributo.valor = valor;
				} else if (attribute.getValue() instanceof String) {

					atributo.tipo = attribute.getType().getBinding().getSimpleName();

					String str = (String) attribute.getValue();
					String strConvert = StringUtils.convertCharsetString(str);

					atributo.valor = strConvert;
				} else {

					atributo.tipo = attribute.getType().getBinding().getSimpleName();
					atributo.valor = attribute.getValue();
				}

				registro.add(atributo);
			}
		}

		return registro;
	}

	/**
	 * Grava o conteúdo mapeado pelo Inputstream no path informado
	 *
	 * @param in
	 * @param filePath
	 * @throws IOException
	 */
	private void writeFileFromInputStream(InputStream in, String filePath) throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
		byte[] bytesIn = new byte[BUFFER_SIZE];
		int read = 0;
		while ((read = in.read(bytesIn)) != -1) {
			bos.write(bytesIn, 0, read);
		}
		bos.close();
	}

	/**
	 * Dispara inserção de mensagem de erro.
	 *
	 * @param message
	 */
	private void fireError(String message, String ex) {

		Logger.error(ex != null ? message + " - " + ex : message);

		this.resultado.status = ResultadoProcessamentoShapeFile.Status.ERRO;

		for (String msg : this.resultado.mensagens) {

			if (msg.equals(message)) {

				return;
			}
		}

		this.resultado.mensagens.add(message);
//        this.resultado.dados = null;
	}

	private void removeArquivosTemporarios() {

		boolean isZip = this.arquivoZip != null;

		File arquivoShape;

		if (isZip) {

			arquivoShape = this.arquivoZip;
		} else {

			arquivoShape = this.arquivoRar;
		}

		String arquivoShp = arquivoShape.getParent() + "/" + this.keyTemp + ".shp";
		String arquivoDbf = arquivoShape.getParent() + "/" + this.keyTemp + ".dbf";
		String arquivoShx = arquivoShape.getParent() + "/" + this.keyTemp + ".shx";
		String arquivoPrj = arquivoShape.getParent() + "/" + this.keyTemp + ".prj";

		File arquivoRemover;

		arquivoRemover = new File(arquivoShp);
		if (arquivoRemover.exists()) {

			try {
				FileUtils.forceDelete(arquivoRemover);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		arquivoRemover = new File(arquivoDbf);
		if (arquivoRemover.exists()) {

			try {
				FileUtils.forceDelete(arquivoRemover);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		arquivoRemover = new File(arquivoShx);
		if (arquivoRemover.exists()) {

			try {
				FileUtils.forceDelete(arquivoRemover);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		arquivoRemover = new File(arquivoPrj);
		if (arquivoRemover.exists()) {

			try {
				FileUtils.forceDelete(arquivoRemover);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private class TipoContadorAtributoShape {

		int cGeometry = 0;
		int cLong = 0;
		int cDouble = 0;
		int cString = 0;
		int cDate = 0;

		public boolean atributosVazios() {
			if (this.cGeometry == 0 && this.cGeometry == 0 &&
					this.cDouble == 0 && this.cString == 0 && this.cDate == 0) {
				return true;
			}

			return false;
		}
	}

}
