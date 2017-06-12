package utils;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.operation.distance.DistanceOp;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.GeodeticCalculator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.operation.TransformException;

import java.awt.geom.Point2D;

public class GeoHelper {

	public static Double converterBufferMetrosGraus(Geometry geo, Double raioMetros){

		Point centro = geo.getCentroid();

		GeodeticCalculator calc = new  GeodeticCalculator(Configuracoes.CRS_DEFAULT);

		calc.setStartingGeographicPoint(centro.getX(), centro.getY());

		calc.setDirection(0.0, raioMetros);

		Point2D p2 = calc.getDestinationGeographicPoint();

		calc.setDirection(90.0, raioMetros);

		Point2D p3 = calc.getDestinationGeographicPoint();

		double dy = p2.getY() - centro.getY();

		double dx = p3.getX() - centro.getX();


		return (dy + dx) / 2;
	}

	public static Double calcularDistancia(SimpleFeature feature, Geometry geometryFrom) throws TransformException {

		Geometry featurePolygon = (Geometry) feature.getDefaultGeometry();

		GeodeticCalculator geoCalc = new GeodeticCalculator(Configuracoes.CRS_DEFAULT);

		Coordinate pontoMaisProximoA = DistanceOp.nearestPoints(featurePolygon, geometryFrom)[0];

		Coordinate pontoMaisProximoB = DistanceOp.nearestPoints(geometryFrom, featurePolygon)[0];

		geoCalc.setStartingPosition(JTS.toDirectPosition(pontoMaisProximoA, Configuracoes.CRS_DEFAULT));

		geoCalc.setDestinationPosition( JTS.toDirectPosition(pontoMaisProximoB , Configuracoes.CRS_DEFAULT ));

		return geoCalc.getOrthodromicDistance();
	}
}
