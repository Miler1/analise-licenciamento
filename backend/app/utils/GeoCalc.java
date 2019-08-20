package utils;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.index.strtree.STRtree;
import com.vividsolutions.jts.operation.valid.IsValidOp;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import play.Play;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import static java.lang.Double.parseDouble;

/**
 * Created by rthoth on 21/05/15.
 */
public class GeoCalc {

	public static final double BUFFER = 0.00000001;
	protected static final STRtree index = new STRtree(2);
	public static final String ILLEGAL_GEOMETRY_TYPE = "Resultado inapropiado para a categoria!";
	protected static CoordinateReferenceSystem CRS_DEFAUL = null;

	static {
		String key, value;
		int indexOf;

		try {
			CRS_DEFAUL = CRS.parseWKT(Play.configuration.getProperty("CRS:DEFAULT"));
		} catch (FactoryException e) {
			throw new RuntimeException(e);
		}

		for (Entry<Object, Object> entry : Play.configuration.entrySet()) {
			key = (String) entry.getKey();
			if (key.startsWith("crs.")) {
				value = (String) entry.getValue();
				indexOf = value.indexOf(' ');
				String[] envelopeStrings = value.substring(0, indexOf).split(",");

				if (envelopeStrings.length == 4) {
					String wkt = value.substring(indexOf);
					double minx, miny, maxx, maxy;
					minx = parseDouble(envelopeStrings[0]);
					miny = parseDouble(envelopeStrings[1]);
					maxx = parseDouble(envelopeStrings[2]);
					maxy = parseDouble(envelopeStrings[3]);

					Envelope envelope = new Envelope(minx, maxx, miny, maxy);
					try {
						addCRS(envelope, wkt);
					} catch (Exception e) {

					}
				}
			}
		}
	}

	private GeoCalc() {

	}

	public static void addCRS(Envelope envelope, String wkt) {
		if (envelope == null)
			throw new IllegalArgumentException("Envelope nulo para " + wkt);

		CoordinateReferenceSystem crs;
		try {
			crs = CRS.parseWKT(wkt);
		} catch (FactoryException e) {
			throw new IllegalArgumentException(wkt, e);
		}

		index.insert(envelope, crs);
	}

	public static double area(Geometry geometry) {

		if(geometry.isEmpty()) {
			return 0D;
		}

		CoordinateReferenceSystem[] crs = detecteCRS(geometry);

		return area(geometry, crs[0]);
	}

	public static double areaHectare(Geometry geometry) {

		if(geometry.isEmpty()) {
			return 0D;
		}

		CoordinateReferenceSystem[] crs = detecteCRS(geometry);

		return area(geometry, crs[0])/10000;
	}

	public static double area(Geometry geometry, CoordinateReferenceSystem crs) {

		MathTransform mathTransform;
		try {
			mathTransform = CRS.findMathTransform(CRS_DEFAUL, crs, true);
		} catch (FactoryException e) {
			throw new RuntimeException(e);
		}

		try {
			return JTS.transform(geometry, mathTransform).getArea();
		} catch (TransformException e) {
			throw new RuntimeException(e);
		}
	}

	public static CoordinateReferenceSystem[] detecteCRS(Geometry geometry) {
		List objects = index.query(geometry.getEnvelopeInternal());
		CoordinateReferenceSystem[] crs = new CoordinateReferenceSystem[objects.size()];

		for (int i = 0; i < crs.length; i++)
			crs[i] = (CoordinateReferenceSystem) objects.get(i);

		return crs;
	}

	public static double length(Geometry geometry) {
		CoordinateReferenceSystem[] crs = detecteCRS(geometry);

		if (crs.length != 1)
			throw new UnsupportedOperationException();

		return length(geometry, crs[0]);
	}

	public static double length(Geometry geometry, CoordinateReferenceSystem crs) {
		MathTransform mathTransform = null;
		try {
			mathTransform = CRS.findMathTransform(CRS_DEFAUL, crs);
		} catch (FactoryException e) {
			throw new RuntimeException(e);
		}

		try {
			return JTS.transform(geometry, mathTransform).getLength();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Geometry difference(Geometry g1, Geometry g2) {
		Geometry difference = null;
		try {
			difference = g1.difference(g2);
		} catch (TopologyException topoEx) {
			try {
				difference = g1.buffer(BUFFER).difference(g2.buffer(BUFFER));
			} catch (TopologyException e) {
				throw topoEx;
			}
		}

		if (difference instanceof GeometryCollection) {
			return fixWithBuffer(filter(difference));
		} else {
			return fixWithBuffer(multi(difference));
		}
	}

	public static Geometry intersection(Geometry g1, Geometry g2) {
		Geometry intersection = null;
		try {
			intersection = g1.intersection(g2);
		} catch (TopologyException topEx) {
			try {
				intersection = g1.buffer(BUFFER).intersection(g2.buffer(BUFFER));
			} catch (TopologyException e) {
				throw topEx;
			}
		}

		if (intersection instanceof GeometryCollection) {
			return fixWithBuffer(filter(intersection));
		} else
			return fixWithBuffer(multi(intersection));
	}

	private static Geometry fixWithBuffer(Geometry geometry) {
		if (geometry.isValid())
			return  geometry;

		geometry = geometry.buffer(BUFFER);

		IsValidOp validOp = new IsValidOp(geometry);

		if (validOp.isValid())
			return geometry;

		throw new TopologyException("A geometria não passará pelo sicar - " + validOp.getValidationError().getMessage());
	}

	private static Geometry multi(Geometry geometry) {
		if (geometry instanceof Polygon)
			return new MultiPolygon(new Polygon[]{(Polygon) geometry}, geometry.getFactory());

		if (geometry instanceof LineString)
			return new MultiLineString(new LineString[]{(LineString) geometry}, geometry.getFactory());

		return geometry;
	}

	private static Geometry filter(Geometry geometry) {
		List<Polygon> polygons = new LinkedList<>();

		for (int i = 0, l = geometry.getNumGeometries(); i < l; i++) {
			Geometry item = geometry.getGeometryN(i);
			if (item instanceof Polygon)
				polygons.add((Polygon) item);
		}

		return new MultiPolygon(polygons.toArray(new Polygon[polygons.size()]), geometry.getFactory());
	}

	public static List<Geometry> getGeometries(Geometry gc) {

		List<Geometry> geometries = new ArrayList<>();

		gc.apply(new GeometryFilter() {
			public void filter (Geometry geom) {
				if (geom instanceof Point || geom instanceof LineString || geom instanceof Polygon) {
					geometries.add(geom);
				}
			}
		});

		return geometries;
	}

	public static Geometry transform(Geometry geometry, CoordinateReferenceSystem crs) {

		MathTransform mathTransform;
		try {
			mathTransform = CRS.findMathTransform(CRS_DEFAUL, crs, true);
		} catch(FactoryException e) {
			throw new RuntimeException(e);
		}

		try {
			return JTS.transform(geometry, mathTransform);
		} catch(TransformException e) {
			throw new RuntimeException(e);
		}

	}

	public static Coordinate transform(Coordinate coordinate, CoordinateReferenceSystem originCrs, CoordinateReferenceSystem destCrs) {

		MathTransform mathTransform;
		try {
			mathTransform = CRS.findMathTransform(originCrs, destCrs, true);
		} catch(FactoryException e) {
			throw new RuntimeException(e);
		}

		try {

			Coordinate resultCoordinate = new Coordinate();

			return JTS.transform(coordinate, resultCoordinate, mathTransform);

		} catch(TransformException e) {
			throw new RuntimeException(e);
		}

	}
}