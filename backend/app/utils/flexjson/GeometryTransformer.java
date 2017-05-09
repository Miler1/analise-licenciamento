package utils.flexjson;

import java.io.IOException;
import java.lang.reflect.Type;

import com.vividsolutions.jts.geom.Geometry;

import utils.GeoJsonUtils;

import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import flexjson.transformer.AbstractTransformer;

public class GeometryTransformer extends AbstractTransformer implements ObjectFactory {
	
	private Boolean writeQuoted;

	public GeometryTransformer() {
		
		writeQuoted = true;
	}
	
	public GeometryTransformer(Boolean writeQuoted) {

		this.writeQuoted = writeQuoted;
	}
		
	@Override
	public void transform(Object geometria) {

		if (geometria == null)
			return;
		
		if (this.writeQuoted) 
			getContext().writeQuoted(GeoJsonUtils.toGeoJson((Geometry) geometria));
		else 
			getContext().write(GeoJsonUtils.toGeoJson((Geometry) geometria));
	}

	@Override
	public Object instantiate(ObjectBinder context, Object geoJson, Type targetType, Class targetClass) {

		return GeoJsonUtils.toGeometry((String) geoJson);
	}
}
