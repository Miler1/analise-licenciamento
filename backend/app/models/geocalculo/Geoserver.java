package models.geocalculo;

import com.vividsolutions.jts.geom.Geometry;
import groovy.transform.Immutable;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
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
import java.io.*;
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
		builderType.add("distancia", Double.class);
		builderType.setDefaultGeometry("geometria");

		featureType = builderType.buildFeatureType();
	}


	public static File verificarRestricoes(Geometry geoEmpreendimento, Geometry geoImovel, String nomeArquivo) throws Exception {

		String caminhoFile = Configuracoes.GEOJSON_INCONFORMIDADES_PATH + File.separator + nomeArquivo + ".geojson";

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

			if(geoImovel != null){

				processarImovel(geoEmpreendimento, geoImovel, featureCollection);

			}

			featureCollection.add(getGeoserverFeature(
					"meuEmpreendimento",
					"empreendimento",
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

	private static void processarImovel(Geometry geoEmpreendimento, Geometry geoImovel, DefaultFeatureCollection featureCollection) throws IOException, TransformException {

			WFSGeoserver wfsSicar = new WFSGeoserver(Configuracoes.GETCAPABILITIES_GEOSERVER_SICAR);

		FeatureIterator<SimpleFeature> interatorImoveisSobrepostos = wfsSicar.intersects(Configuracoes.GEOSERVER_SICAR_IMOVEL_LAYER, geoImovel).features();

		while(interatorImoveisSobrepostos.hasNext()){

			SimpleFeature feature = interatorImoveisSobrepostos.next();
			Geometry featurePolygon = (Geometry) feature.getDefaultGeometry();
			featureCollection.add(getGeoserverFeature(
					feature.getID(),
					"imovel-sobreposto",
					"Imóvel Sobreposto",
					GeoHelper.calcularDistancia(feature, geoEmpreendimento),
					featurePolygon
					));

		}

		featureCollection.add(getGeoserverFeature(
				"meuGeoImovel",
				"imovel",
				"Imóvel",
				0.0,
				geoImovel
		));
	}


	private static SimpleFeature criarFeatureGeoserver(SimpleFeature featureProcessada, ConfiguracaoLayer configuracaoLayer, Double distancia){

		Geometry featurePolygon = (Geometry) featureProcessada.getDefaultGeometry();

		return getGeoserverFeature(
				featureProcessada.getID(),
				configuracaoLayer.descricao,
				featureProcessada.getAttribute(configuracaoLayer.atributoDescricao).toString(),
				distancia,
				featurePolygon

		);

	}

	private static SimpleFeature getGeoserverFeature(String featureId, String descricaoLayer, String descricao, Double distancia, Geometry featurePolygon) {

		SimpleFeatureBuilder builder = new SimpleFeatureBuilder(featureType);

		builder.set("geometria", featurePolygon);

		builder.set("descricaoLayer", descricaoLayer);

		builder.set("descricao", descricao);

		//distância em metros
		builder.set("distancia", distancia);

		return builder.buildFeature(featureId);
	}


}
