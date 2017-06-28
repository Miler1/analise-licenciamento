package utils;


import com.vividsolutions.jts.geom.Geometry;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.spatial.Intersects;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WFSGeoserver {

	private DataStore data;

	public WFSGeoserver(String urlGetCapabilities) throws IOException {

		Map<String, Object> connectionParameters = new HashMap<String, Object>();

		//parametros de configuração do WFS
		connectionParameters.put("WFSDataStoreFactory:GET_CAPABILITIES_URL", urlGetCapabilities );
		connectionParameters.put("WFSDataStoreFactory:PROTOCOL", true );
		connectionParameters.put("WFSDataStoreFactory:LENIENT", true );
		connectionParameters.put("WFSDataStoreFactory:WFS_STRATEGY", "geoserver" );
		connectionParameters.put("WFSDataStoreFactory:TIMEOUT", 1000000000 );
		connectionParameters.put("WFSDataStoreFactory:TRY_GZIP", false );

		//pegar a conexão do geoserver
		//aqui posso olhar quais as camadas tem o geoserver
		data = DataStoreFinder.getDataStore( connectionParameters );

	}

	public FeatureCollection<SimpleFeatureType, SimpleFeature> intersects(String nomeLayer, Geometry geometry) throws IOException {

		//pegar a camada com o nome nomeLayer
		SimpleFeatureType schema = data.getSchema( nomeLayer );

		//pegar o nome da geomteria default

		FeatureSource<SimpleFeatureType, SimpleFeature> source = data.getFeatureSource( nomeLayer );

		//montar filtro
		FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2( GeoTools.getDefaultHints());
		//uso boundary box para aumentar o desempenho quando consulto o banco
		Intersects filter = ff.intersects(
				ff.property( schema.getGeometryDescriptor().getLocalName() ),
				ff.literal( geometry.getEnvelope() )
		);

		DefaultFeatureCollection featureCollectionRetorno = new DefaultFeatureCollection(nomeLayer, source.getSchema());

		FeatureIterator<SimpleFeature> iterator = source.getFeatures(filter).features();

		while (iterator.hasNext()){

			SimpleFeature featureTestada = iterator.next();

			if(geometry.intersects((Geometry) featureTestada.getDefaultGeometry())){

				featureCollectionRetorno.add(featureTestada);

			}

		}

		return featureCollectionRetorno;
	}
}
