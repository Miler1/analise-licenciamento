package models.tmsmap;

import br.ufla.tmsmap.Layer;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DirectLayer;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;

import java.awt.*;
import java.awt.image.ColorModel;

public interface CustomLayer extends Layer {

	@Override
	public default org.geotools.map.Layer createMapLayer(MapViewport viewport, int zoom, ColorModel colorSpace) {

		final ReferencedEnvelope bounds = viewport.getBounds();

		return new DirectLayer() {

			@Override
			public ReferencedEnvelope getBounds() {
				return bounds;
			}

			@Override
			public void draw(Graphics2D arg0, MapContent arg1, MapViewport arg2) {
				customDraw(arg0, arg1, arg2);
			}

		};

	}

	public abstract void customDraw(Graphics2D arg0, MapContent arg1, MapViewport arg2);

}
