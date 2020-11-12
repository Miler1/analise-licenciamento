package models.tmsmap;

import com.vividsolutions.jts.geom.Coordinate;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import utils.CoordenadaUtil;
import utils.GeoCalc;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;

public class GridLayer implements CustomLayer {

	protected CoordinateReferenceSystem crs;
	protected int marginSize;
	protected int width;
	protected int height;
	protected Font font;

	public GridLayer(int marginSize, CoordinateReferenceSystem crs, Font font) {

		super();

		this.crs = crs;
		this.marginSize = marginSize;
		this.font = font;

	}

	public GridLayer(int marginSize, int width, int height, CoordinateReferenceSystem crs, Font font) {

		this(marginSize, crs, font);

		this.width = width;
		this.height = height;

	}

	private int ceil(double x, int size) {
		return (int)(Math.ceil(x / size) * size);
	}

	private int floor(double x, int size) {
		return (int)(Math.floor(x / size) * size);
	}

	@Override
	public void customDraw(Graphics2D graphics, MapContent mapContent, MapViewport mapViewport) {

		double gridSize = this.marginSize / 2;
		int doubleMarginSize = this.marginSize * 2;

		int fontSize = (int)(this.marginSize - (gridSize / 2)) - 4;

		Point2D worldResultPoint1 = new Point2D.Double();
		Point2D worldResultPoint2 = new Point2D.Double();
		mapViewport.getScreenToWorld().transform(new Point2D.Double(doubleMarginSize + 20, doubleMarginSize + 20), worldResultPoint1);
		mapViewport.getScreenToWorld().transform(new Point2D.Double(this.width - doubleMarginSize - 20, this.height - doubleMarginSize - 20), worldResultPoint2);

		Coordinate worldCoordinate1 = new Coordinate(worldResultPoint1.getX(), worldResultPoint1.getY());
		Coordinate worldUtmCoordinate1 = GeoCalc.transform(worldCoordinate1, DefaultGeographicCRS.WGS84, this.crs);
		Coordinate worldCoordinate2 = new Coordinate(worldResultPoint2.getX(), worldResultPoint2.getY());
		Coordinate worldUtmCoordinate2 = GeoCalc.transform(worldCoordinate2, DefaultGeographicCRS.WGS84, this.crs);

		int gridMeterRound = 10000;

		int x1 = ceil(worldUtmCoordinate1.x, gridMeterRound);
		int y1 = floor(worldUtmCoordinate1.y, gridMeterRound);
		int x2 = floor(worldUtmCoordinate2.x, gridMeterRound);
		int y2 = ceil(worldUtmCoordinate2.y, gridMeterRound);

		while((Math.ceil(x2 - x1) / gridMeterRound) < 1) {

			gridMeterRound = gridMeterRound / 10;
			x1 = ceil(worldUtmCoordinate1.x, gridMeterRound);
			x2 = floor(worldUtmCoordinate2.x, gridMeterRound);

		}

		while((Math.ceil(y1 - y2) / gridMeterRound) < 1) {

			gridMeterRound = gridMeterRound / 10;
			y1 = floor(worldUtmCoordinate1.y, gridMeterRound);
			y2 = ceil(worldUtmCoordinate2.y, gridMeterRound);

		}

		worldUtmCoordinate1 = new Coordinate(x1, y1);
		worldUtmCoordinate2 = new Coordinate(x2, y2);

		worldCoordinate1 = GeoCalc.transform(worldUtmCoordinate1, this.crs, DefaultGeographicCRS.WGS84);
		worldCoordinate2 = GeoCalc.transform(worldUtmCoordinate2, this.crs, DefaultGeographicCRS.WGS84);

		worldResultPoint1 = new Point2D.Double(worldCoordinate1.x, worldCoordinate1.y);
		worldResultPoint2 = new Point2D.Double(worldCoordinate2.x, worldCoordinate2.y);

		Point2D screenResultPoint1 = new Point2D.Double();
		Point2D screenResultPoint2 = new Point2D.Double();
		mapViewport.getWorldToScreen().transform(worldResultPoint1, screenResultPoint1);
		mapViewport.getWorldToScreen().transform(worldResultPoint2, screenResultPoint2);

		int difference = (int)(this.marginSize + (gridSize / 2));
		int doubleDifference = (int)(doubleMarginSize + gridSize);
		Rectangle internalGridRect = new Rectangle(difference, difference, this.width - doubleDifference, this.height - doubleDifference);

		Rectangle internalFrameRect = new Rectangle(this.marginSize, this.marginSize, this.width - doubleMarginSize, this.height - doubleMarginSize);

		Rectangle leftRect = new Rectangle(0, 0, this.marginSize, this.height);
		Rectangle rightRect = new Rectangle(this.width - this.marginSize, 0, this.marginSize, this.height);
		Rectangle topRect = new Rectangle(this.marginSize, 0 + this.height - this.marginSize, this.width - doubleMarginSize, this.marginSize);
		Rectangle bottomRect = new Rectangle(this.marginSize, 0, this.width - doubleMarginSize, this.marginSize);

		graphics.setColor(Color.WHITE);
		graphics.fill(leftRect);
		graphics.fill(topRect);
		graphics.fill(bottomRect);
		graphics.fill(rightRect);

		Line2D line1xt = new Line2D.Double(screenResultPoint1.getX(), internalGridRect.getMinY(), screenResultPoint1.getX(), internalGridRect.getMinY() - gridSize);
		Line2D line1xb = new Line2D.Double(screenResultPoint1.getX(), internalGridRect.getMaxY(), screenResultPoint1.getX(), internalGridRect.getMaxY() + gridSize);
		Line2D line2xt = new Line2D.Double(screenResultPoint2.getX(), internalGridRect.getMinY(), screenResultPoint2.getX(), internalGridRect.getMinY() - gridSize);
		Line2D line2xb = new Line2D.Double(screenResultPoint2.getX(), internalGridRect.getMaxY(), screenResultPoint2.getX(), internalGridRect.getMaxY() + gridSize);

		Line2D line1yl = new Line2D.Double(internalGridRect.getMinX(), screenResultPoint1.getY(), internalGridRect.getMinX() - gridSize, screenResultPoint1.getY());
		Line2D line1yr = new Line2D.Double(internalGridRect.getMaxX(), screenResultPoint1.getY(), internalGridRect.getMaxX() + gridSize, screenResultPoint1.getY());
		Line2D line2yl = new Line2D.Double(internalGridRect.getMinX(), screenResultPoint2.getY(), internalGridRect.getMinX() - gridSize, screenResultPoint2.getY());
		Line2D line2yr = new Line2D.Double(internalGridRect.getMaxX(), screenResultPoint2.getY(), internalGridRect.getMaxX() + gridSize, screenResultPoint2.getY());

		graphics.setStroke(new BasicStroke());

		graphics.setColor(Color.BLACK);
		graphics.draw(internalFrameRect);
		graphics.draw(line1xt);
		graphics.draw(line1xb);
		graphics.draw(line2xt);
		graphics.draw(line2xb);
		graphics.draw(line1yl);
		graphics.draw(line1yr);
		graphics.draw(line2yl);
		graphics.draw(line2yr);

		Font font = this.font.deriveFont(Font.PLAIN, fontSize);
		graphics.setFont(font);

		FontMetrics fm = graphics.getFontMetrics();

		int fontMargin = ((fm.getHeight() - fontSize));
		double remainingMargin = this.marginSize - (gridSize / 2) - fm.getHeight();

		double topLablePositionY = internalGridRect.getMinY() - (gridSize + fontMargin + (remainingMargin / 2));
		double bottomLablePositionY = internalGridRect.getMaxY() + gridSize + fontSize + (remainingMargin / 2);
		double leftLablePositionX = internalGridRect.getMinX() - (gridSize + fontMargin + (remainingMargin / 2));
		double rightLablePositionX = internalGridRect.getMaxX() + gridSize + fontSize + (remainingMargin / 2);

		DecimalFormat df = new DecimalFormat("#");

		String line1xLabel = CoordenadaUtil.formataLongitudeString(worldCoordinate1.x);
		String line1yLabel = CoordenadaUtil.formataLatitudeString(worldCoordinate1.y);
		String line2xLabel = CoordenadaUtil.formataLongitudeString(worldCoordinate2.x);
		String line2yLabel = CoordenadaUtil.formataLatitudeString(worldCoordinate2.y);

		int line1xLabelWidth = fm.stringWidth(line1xLabel);
		int line2xLabelWidth = fm.stringWidth(line2xLabel);

		graphics.drawString(line1xLabel, (float)screenResultPoint1.getX() - line1xLabelWidth / 2, (float)topLablePositionY);
		graphics.drawString(line1xLabel, (float)screenResultPoint1.getX() - line1xLabelWidth / 2, (float)bottomLablePositionY);
		graphics.drawString(line2xLabel, (float)screenResultPoint2.getX() - line2xLabelWidth / 2, (float)topLablePositionY);
		graphics.drawString(line2xLabel, (float)screenResultPoint2.getX() - line2xLabelWidth / 2, (float)bottomLablePositionY);

		AffineTransform orig = graphics.getTransform();

		AffineTransform at = new AffineTransform();
		at.setToRotation(Math.toRadians(-90), leftLablePositionX, screenResultPoint1.getY());
		graphics.setTransform(at);

		int line1yLabelWidth = fm.stringWidth(line1yLabel);
		graphics.drawString(line1yLabel, (float)(leftLablePositionX - line1yLabelWidth / 2), (float)screenResultPoint1.getY());

		graphics.setTransform(orig);

		at = new AffineTransform();
		at.setToRotation(Math.toRadians(-90), rightLablePositionX, screenResultPoint1.getY());
		graphics.setTransform(at);

		graphics.drawString(line1yLabel, (float)(rightLablePositionX - line1yLabelWidth / 2), (float)screenResultPoint1.getY());

		graphics.setTransform(orig);

		at = new AffineTransform();
		at.setToRotation(Math.toRadians(-90), leftLablePositionX, screenResultPoint2.getY());
		graphics.setTransform(at);

		int line2yLabelWidth = fm.stringWidth(line2yLabel);
		graphics.drawString(line2yLabel, (float)(leftLablePositionX - line2yLabelWidth / 2), (float)screenResultPoint2.getY());

		graphics.setTransform(orig);

		at = new AffineTransform();
		at.setToRotation(Math.toRadians(-90), rightLablePositionX, screenResultPoint2.getY());
		graphics.setTransform(at);

		graphics.drawString(line2yLabel, (float)(rightLablePositionX - line2yLabelWidth / 2), (float)screenResultPoint2.getY());

		graphics.setTransform(orig);

	};

}
