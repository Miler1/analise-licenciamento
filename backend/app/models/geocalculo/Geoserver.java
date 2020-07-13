package models.geocalculo;

import com.vividsolutions.jts.geom.Geometry;
import groovy.transform.Immutable;
import models.licenciamento.ImovelEmpreendimento;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureImpl;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.operation.TransformException;
import play.db.jpa.GenericModel;
import utils.Configuracoes;
import utils.GeoHelper;
import utils.WFSGeoserver;

import javax.persistence.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Entity
@Table(schema="analise", name="geoserver")
@Immutable
public class Geoserver extends GenericModel {

	@Id
	@Column(name = "id")
	public Long id;

	@Column(name = "url_getcapabilities")
	public String urlGetcapabilities;

	@OneToMany
	@JoinColumn(name="id_geoserver", referencedColumnName="id")
	public List<ConfiguracaoLayer> configuracaoLayers;


	@Transient
	private static final SimpleFeatureType featureType;

	static {
		SimpleFeatureTypeBuilder builderType = new SimpleFeatureTypeBuilder();

		builderType.setName("retorno");
		builderType.setCRS(Configuracoes.CRS_DEFAULT);
		builderType.add("geometria", Geometry.class);
		builderType.add("descricaoLayer", String.class);
		builderType.add("descricao", String.class);
		builderType.add("restricao", Double.class);
		builderType.setDefaultGeometry("geometria");

		featureType = builderType.buildFeatureType();
	}


	public static File verificarRestricoes(Geometry geoEmpreendimento, ImovelEmpreendimento imovel, String nomeArquivo) throws Exception {

		String pathDir = Configuracoes.GEOJSON_INCONFORMIDADES_PATH;
		String caminhoFile = pathDir + File.separator + nomeArquivo + ".geojson";

		File filePathDir = new File(pathDir);
		
		if(!filePathDir.exists())
			filePathDir.mkdirs();
		
		File retorno = new File(caminhoFile);

		if(!retorno.exists()){
			
			List<Geoserver> geoservers = Geoserver.findAll();

			DefaultFeatureCollection featureCollection = new DefaultFeatureCollection(nomeArquivo, featureType);

			for(Geoserver geoserver : geoservers){

				String getCapabilities = geoserver.urlGetcapabilities;

				WFSGeoserver wfs = new WFSGeoserver(getCapabilities);

				for(ConfiguracaoLayer configuracao : geoserver.configuracaoLayers){

					String layerName = configuracao.nomeLayer;

					FeatureCollection<SimpleFeatureType, SimpleFeature> features;

					//se tiver buffer adiciona graus
					if(configuracao.buffer != null && configuracao.buffer > 0){

						Double raioGraus = GeoHelper.converterBufferMetrosGraus(geoEmpreendimento, configuracao.buffer);

						features =  wfs.intersects(layerName, geoEmpreendimento.buffer(raioGraus));

					}else{

						features =  wfs.intersects(layerName, geoEmpreendimento);

					}

					FeatureIterator<SimpleFeature> interator = features.features();

					while(interator.hasNext()){

						SimpleFeature feature = interator.next();

						featureCollection.add(criarFeatureGeoserver(feature, configuracao, GeoHelper.calcularDistancia(feature, geoEmpreendimento)));

					}

				}

			}

			if(imovel != null){

				processarImovel(geoEmpreendimento, imovel, featureCollection);

			}

			featureCollection.add(getGeoserverFeature(
					"meu-empreendimento",
					"Limite do Emmpreendimento",
					"Empreendimento",
					0.0,
					geoEmpreendimento
			));

			FeatureJSON io = new FeatureJSON();

			io.writeFeatureCollection(featureCollection, new FileOutputStream(caminhoFile));

			retorno = new File(caminhoFile);
		}

		return retorno;

	}

	private static void processarImovel(Geometry geoEmpreendimento, ImovelEmpreendimento imovel, DefaultFeatureCollection featureCollection) throws IOException, TransformException {

		WFSGeoserver wfsSicar = new WFSGeoserver(Configuracoes.GETCAPABILITIES_GEOSERVER_SICAR);

		Geometry geoImovel = imovel.limite;

		FeatureIterator<SimpleFeature> interatorImoveisSobrepostos = wfsSicar.intersects(Configuracoes.GEOSERVER_SICAR_IMOVEL_LAYER, geoImovel).features();

		while(interatorImoveisSobrepostos.hasNext()){

			SimpleFeature feature = interatorImoveisSobrepostos.next();
			Geometry featurePolygon = (Geometry) feature.getDefaultGeometry();
			String codigoImovel = String.valueOf(feature.getAttribute("cod_imovel"));

			if(!imovel.codigo.equals(codigoImovel)){

				featureCollection.add(getGeoserverFeature(
						feature.getID(),
						codigoImovel,
						"Imóvel Sobreposto",
						featurePolygon.intersection(geoImovel).getArea()*100/geoImovel.getArea(),
						featurePolygon
						));

			}
				
		}
		
		featureCollection.add(getGeoserverFeature(
				"meu-imovel",
				imovel.codigo,
				"Imóvel Análisado",
				0.0,
				geoImovel
		));


	}


	private static SimpleFeature criarFeatureGeoserver(SimpleFeature featureProcessada, ConfiguracaoLayer configuracaoLayer, Double restricao){

		Geometry featurePolygon = (Geometry) featureProcessada.getDefaultGeometry();

		String nome;
		
		if(featureProcessada.getAttribute(configuracaoLayer.atributoDescricao) == null)
			nome = "Nome não informado";
		else
			nome = featureProcessada.getAttribute(configuracaoLayer.atributoDescricao).toString();
		
		return getGeoserverFeature(
				featureProcessada.getID(),
				nome,
				configuracaoLayer.descricao,
				restricao,
				featurePolygon

		);

	}

	private static SimpleFeature getGeoserverFeature(String featureId, String descricaoLayer, String descricao, Double restricao, Geometry featurePolygon) {

		SimpleFeatureBuilder builder = new SimpleFeatureBuilder(featureType);

		builder.set("geometria", featurePolygon);

		builder.set("descricaoLayer", descricaoLayer);

		builder.set("descricao", descricao);

		builder.set("restricao", restricao);

		return builder.buildFeature(featureId);
	}


}
