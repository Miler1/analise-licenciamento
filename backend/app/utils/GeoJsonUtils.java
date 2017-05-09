package utils;

import java.io.IOException;

import org.geotools.geojson.geom.GeometryJSON;

import com.vividsolutions.jts.geom.Geometry;

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
	
	public static String toGeoJson(Geometry geometry){
		
		GeometryJSON gjson = new GeometryJSON(PRECISION);
		
		return gjson.toString(geometry);
		
	}
}
