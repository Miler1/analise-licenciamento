package models.analiseShape;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import java.util.List;

public class GeometryAddLayer {

	public List<GeometryRing> rings;


	public GeometryAddLayer(Geometry geometry) {

		int numGeometrias = geometry.getNumGeometries();

		for (int i = 0; i <  numGeometrias; i++) {

			Coordinate[] coordinates = geometry.getGeometryN(i).getCoordinates();
			GeometryRing ring = new GeometryRing(coordinates);
			this.rings.add(ring);
		}
	}
}


class GeometryRing {

	List<GeometryPoint> points;

	public GeometryRing(Coordinate[] coordinates) {


		//this.points() ;
	}
}

class GeometryPoint {

	String coordenadaX;
	String coordenadaY;

	public GeometryPoint(String x, String y) {

		coordenadaX = x;
		coordenadaY = y;
	}
}