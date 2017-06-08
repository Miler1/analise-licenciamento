package utils;


import com.vividsolutions.jts.geom.Geometry;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.feature.FeatureCollection;
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

		connectionParameters.put("WFSDataStoreFactory:GET_CAPABILITIES_URL", urlGetCapabilities );
		connectionParameters.put("WFSDataStoreFactory:PROTOCOL", true );
		connectionParameters.put("WFSDataStoreFactory:LENIENT", true );
		connectionParameters.put("WFSDataStoreFactory:WFS_STRATEGY", "geoserver" );

		data = DataStoreFinder.getDataStore( connectionParameters );

	}


	public FeatureCollection<SimpleFeatureType, SimpleFeature> intersects(String nomeLayer, Geometry geometry) throws IOException {

		SimpleFeatureType schema = data.getSchema( nomeLayer );

		FeatureSource<SimpleFeatureType, SimpleFeature> source = data.getFeatureSource( nomeLayer );

		String geomName = schema.getGeometryDescriptor().getLocalName();

		FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2( GeoTools.getDefaultHints());

		Intersects filter = ff.intersects( ff.property( geomName ), ff.literal( geometry ) );

		return source.getFeatures( filter );
	}
}
