package utils;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;

import java.io.IOException;

public class GeoJsonUtils {
	
	public static final Integer SRID = 4674;

	public static final Integer PRECISION = 15;
	
	public static Geometry toGeometry(String geoJson) {
		
		try {
			
			GeometryJSON gjson = new GeometryJSON();
			Geometry geometry;
			geometry = gjson.read(geoJson);
			geometry.setSRID(SRID);
			
			return geometry;
			
		} catch (IOException e) {
			
			throw new RuntimeException(e);
		}
	}

	public static GeometryCollection toGeometryCollection(String geoJson) {

		try {

			GeometryJSON gjson = new GeometryJSON();
			GeometryCollection geometryCollection;
			geometryCollection = gjson.readGeometryCollection(geoJson);
			geometryCollection.setSRID(SRID);

			for(int i = 0; i < geometryCollection.getNumGeometries(); i++) {

				geometryCollection.getGeometryN(i).setSRID(SRID);
			}

			return geometryCollection;

		} catch (IOException e) {

			throw new RuntimeException(e);
		}
	}

	public static DefaultFeatureCollection toDefaultFeatureCollection(String geoJson) {

		try {

			FeatureJSON gjson = new FeatureJSON();

			return (DefaultFeatureCollection) gjson.readFeatureCollection(geoJson);

		} catch (IOException e) {

			throw new RuntimeException(e);
		}
	}
	
	public static String toGeoJson(Geometry geometry){
		
		GeometryJSON gjson = new GeometryJSON(PRECISION);
		
		return gjson.toString(geometry);
	}
}
