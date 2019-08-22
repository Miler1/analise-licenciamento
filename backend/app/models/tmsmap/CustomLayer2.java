package models.tmsmap;

import br.ufla.tmsmap.Layer;
import com.vividsolutions.jts.geom.Envelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DirectLayer;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.geotools.referencing.crs.DefaultGeographicCRS;

import java.awt.*;
import java.awt.image.ColorModel;

public class CustomLayer2 implements Layer {

	private int width = 50;
	private int height = 50;

	public CustomLayer2() {

		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public org.geotools.map.Layer createMapLayer(MapViewport viewport, int zoom, ColorModel colorSpace) {

		Rectangle screenArea = viewport.getScreenArea();
		screenArea.grow(this.width, this.height);

		ReferencedEnvelope originalBounds = viewport.getBounds();
		double x1 = originalBounds.getMinX() - 10;
		double x2 = originalBounds.getMaxX() + 10;
		double y1 = originalBounds.getMinY() - 10;
		double y2 = originalBounds.getMaxY() + 10;
		Envelope envelope = new Envelope(x1, x2, y1, y2);
		ReferencedEnvelope newBounds = new ReferencedEnvelope(envelope, DefaultGeographicCRS.WGS84);
		viewport.setBounds(newBounds);

		final ReferencedEnvelope bounds = viewport.getBounds();

		return new DirectLayer() {

			@Override
			public ReferencedEnvelope getBounds() {
				return bounds;
			}

			@Override
			public void draw(Graphics2D graphics, MapContent mapContent, MapViewport mapViewPort) {

				Rectangle externalFrame = mapViewPort.getScreenArea();

				graphics.setStroke(new BasicStroke(1));
				graphics.setColor(Color.BLACK);

//				graphics.draw(internalFrame);
				graphics.draw(externalFrame);

			}

		};

	}

}
