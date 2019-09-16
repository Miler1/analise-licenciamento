package models.tmsmap;

import br.ufla.tmsmap.*;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import models.CamadaGeoAtividadeVO;
import models.CamadaGeoRestricaoVO;
import models.DadosProcessoVO;
import models.GeometriaAtividadeVO;
import org.apache.commons.codec.binary.Base64;
import org.geotools.graph.util.geom.GeometryUtil;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import play.Play;
import utils.GeoCalc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;

public class MapaImagem {

	public class DataLayer {

		public String name;
		public Geometry geometry;
		public Color color;
		public Color fillColor;
		public Stroke stroke;
		String colorCode;

		public DataLayer(String name, Geometry geometry, Color color, String colorCode) {

			super();
			this.name = name;
			this.geometry = geometry;
			this.color = color;
			this.stroke = new BasicStroke();
			this.colorCode = colorCode;

		}

		public DataLayer(String name, Geometry geometry, Color color) {

			super();
			this.name = name;
			this.geometry = geometry;
			this.color = color;
			this.stroke = new BasicStroke();

		}

		public DataLayer stroke(Stroke stroke) {
			this.stroke = stroke;
			return this;
		}

		public DataLayer fillColor(Color color) {
			this.fillColor = color;
			return this;
		}

	}

	public class GrupoDataLayer {

		public String titulo;
		public  List<DataLayer> dataLayers;

		public GrupoDataLayer(String titulo, List<DataLayer> dataLayers) {
			this.titulo = titulo;
			this.dataLayers = dataLayers;
		}

	}

	public class GrupoDataLayerImagem {

		public String imagem;
		public  List<GrupoDataLayer> grupoDataLayers;

		public GrupoDataLayerImagem(String imagem, List<GrupoDataLayer> grupoDataLayers) {
			this.imagem = imagem;
			this.grupoDataLayers = grupoDataLayers;
		}

	}

	private static final String URL_MOSAICOS = Play.configuration.getProperty("mapa.mosaicos", "http://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}.jpg");

	// External Frame
	private static final int WIDTH = 312 * 2 + 40 * 3;
	private static final int HEIGHT = 312 * 2 + 40 * 3;
	//private static final int HEIGHT = 312 * 2 + 340;
	private static final int HORIZONTAL_MARGIN_SIZE = 15 * 2;
	private static final int VERTICAL_MARGIN_SIZE = 10 * 2;

	// Map
	private static final int MAP_WIDTH = WIDTH - HORIZONTAL_MARGIN_SIZE * 2;
	private static final int MAP_HEIGHT = MAP_WIDTH;
	private static final int MAP_MARGIN_SIZE = 14 * 2;
	private static final int MAP_PADDING_SIZE = MAP_MARGIN_SIZE * 8;
	private static final int MAP_COMPONENTS_MARGIN_SIZE = (int)(MAP_MARGIN_SIZE * 1.5);

	// Brand
	private static final int BRAND_WIDTH = 46;
	private static final int BRAND_HEIGHT = Math.round(BRAND_WIDTH * 1.415f);
	private static final int BRAND_X = BRAND_HEIGHT / 10;

	private Font brandPrimaryFont;
	private Font brandPrimaryFont1;
	private Font brandSecondaryFont;
	private BufferedImage brandBufferedImage;

	public MapaImagem() {

		super();

		File brand = new File(Play.applicationPath + "/app/assets/images/brasao.png");

		try {

			this.brandBufferedImage = ImageIO.read(brand);

			this.brandPrimaryFont = Font.createFont(Font.TRUETYPE_FONT, new File(Play.applicationPath + "/app/assets/fonts/trade-gothic-lt-std-bold-condensed-no-20.otf"));
			this.brandPrimaryFont1 = Font.createFont(Font.TRUETYPE_FONT, new File(Play.applicationPath + "/app/assets/fonts/din-medium.otf"));
			this.brandSecondaryFont = Font.createFont(Font.TRUETYPE_FONT, new File(Play.applicationPath + "/app/assets/fonts/ronnia.otf"));

		} catch(FontFormatException | IOException e) {
			throw new RuntimeException(e);
		}

	}

	public String createMapAreaImovel(Geometry geometryAreaImovel) {

		BufferedImage newImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = newImage.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setColor(Color.WHITE);
		graphics.fill(new Rectangle(0, 0, WIDTH, HEIGHT));
		graphics.setColor(Color.BLACK);

		graphics.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);

		int topY = VERTICAL_MARGIN_SIZE - 10;

		// Inicio Header

		//topY = createHeader(graphics, 0, topY, 0);

		// Fim Header

		// Inicio Mapa

		CoordinateReferenceSystem crs = GeoCalc.detecteCRS(geometryAreaImovel)[0];

		TMSMap map = createMap(crs);
		map.zoomTo(geometryAreaImovel.getEnvelopeInternal(), MAP_WIDTH, MAP_HEIGHT, 0, 16, 256, 256);

		Style polygonStyle = new PolygonStyle().fillOpacity(0f).color(Color.YELLOW).width(2).dashArray(2f).opacity(1f);
		map.addLayer(JTSLayer.from(DefaultGeographicCRS.WGS84, polygonStyle, geometryAreaImovel));

		Collection<Coordinate> mainCoordinates = createMainCoordinates(map, geometryAreaImovel, crs);

		BufferedImage mapa = new BufferedImage(MAP_WIDTH, MAP_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		map.render(MAP_WIDTH, MAP_HEIGHT, Format.PNG, mapa);

		graphics.drawRect(HORIZONTAL_MARGIN_SIZE, topY, MAP_WIDTH + 1, MAP_HEIGHT + 1);
		graphics.drawImage(mapa, HORIZONTAL_MARGIN_SIZE + 1, topY + 1, null);

		// Fim Mapa

		// Início rodapé

		createMainCoordinatesTable(mainCoordinates, graphics, topY);

		// Fim rodapé

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {

			ImageIO.write(newImage, Format.PNG.formatName, out);

		} catch(IOException e) {
			throw new RuntimeException(e);
		}

		return "data:image/png;base64," + Base64.encodeBase64String(out.toByteArray());

	}

	public GrupoDataLayerImagem createMapCaracterizacaoImovel(CamadaGeoAtividadeVO geometryAreaImovel, Map<LayerType, List<CamadaGeoAtividadeVO>> geometriasEmpreendimento, Map<LayerType, CamadaGeoAtividadeVO> geometriasAtividades, Map<LayerType, CamadaGeoRestricaoVO> geometriasRestricoes) {

		LinkedList<GrupoDataLayer> grupoDataLayers = new LinkedList<>();

		for(Entry<LayerType, CamadaGeoAtividadeVO> entry : geometriasAtividades.entrySet()) {

			CamadaGeoAtividadeVO atividade = entry.getValue();
			LayerType layerType = entry.getKey();
			LinkedList<DataLayer> dataLayers = new LinkedList<>();

			if(atividade.geometrias.isEmpty()) {
				continue;
			}

			for(GeometriaAtividadeVO geometriaAtividade : atividade.geometrias) {

				String colorCode = getColorTemaCiclo();
				Color color = Color.decode(colorCode);
				Color fillColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 127);
				dataLayers.add(new DataLayer(geometriaAtividade.item, geometriaAtividade.geometria, color, colorCode).fillColor(fillColor));

			}

			dataLayers.sort((o1, o2) -> o1.name.compareTo(o2.name));

			grupoDataLayers.add(new GrupoDataLayer(layerType.getName(), dataLayers));

		}

		for(Entry<LayerType, CamadaGeoRestricaoVO> entry : geometriasRestricoes.entrySet()) {

			CamadaGeoRestricaoVO restricao = entry.getValue();
			LayerType layerType = entry.getKey();
			LinkedList<DataLayer> dataLayers = new LinkedList<>();

			if(restricao.geometria == null) {
				continue;
			}

			String colorCode = getColorTemaCiclo();
			Color color = Color.decode(colorCode);
			Color fillColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 127);
			dataLayers.add(new DataLayer(restricao.item, restricao.geometria, color, colorCode).fillColor(fillColor));

			dataLayers.sort((o1, o2) -> o1.name.compareTo(o2.name));

			grupoDataLayers.add(new GrupoDataLayer(layerType.getName(), dataLayers));
		}

		for(Entry<LayerType, List<CamadaGeoAtividadeVO>> entry : geometriasEmpreendimento.entrySet()) {

			List<CamadaGeoAtividadeVO> empreendimento = entry.getValue();
			LayerType layerType = entry.getKey();
			LinkedList<DataLayer> dataLayers = new LinkedList<>();

			for (CamadaGeoAtividadeVO e : empreendimento) {

				if(e.geometrias.isEmpty()) {
					continue;
				}

				e.geometrias.forEach(geometriaEmpreendimento -> {

					String colorCode = getColorTemaCiclo();
					Color color = Color.decode(colorCode);
					Color fillColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 127);
					dataLayers.add(new DataLayer(geometriaEmpreendimento.item, geometriaEmpreendimento.geometria, color, colorCode).fillColor(fillColor));

				});

			}

			dataLayers.sort((o1, o2) -> o1.name.compareTo(o2.name));

			grupoDataLayers.add(new GrupoDataLayer(layerType.getName(), dataLayers));
		}


		return createMapCaracterizacaoImovel(geometryAreaImovel, grupoDataLayers);

	}

	private String createMapCaracterizacaoImovel2(Geometry geometryAreaImovel, LinkedList<DataLayer> dataLayers) {

		BufferedImage newImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = newImage.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setColor(Color.WHITE);
		graphics.fill(new Rectangle(0, 0, WIDTH, HEIGHT));
		graphics.setColor(Color.BLACK);

		graphics.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);

		int topY = VERTICAL_MARGIN_SIZE - 10;

		// Inicio Header

		// topY = createHeader(graphics, 0, topY, 0);

		// Fim Header

		// Inicio Mapa

		CoordinateReferenceSystem crs = GeoCalc.detecteCRS(geometryAreaImovel)[0];

		TMSMap map = createMap(crs);
		map.zoomTo(geometryAreaImovel.getEnvelopeInternal(), MAP_WIDTH, MAP_HEIGHT, 0, 16, 256, 256);

		PolygonStyle polygonStyle = (PolygonStyle)new PolygonStyle().fillOpacity(0f).color(Color.YELLOW).width(2).dashArray(2f).opacity(1f);
		map.addLayer(JTSLayer.from(DefaultGeographicCRS.WGS84, polygonStyle, geometryAreaImovel));

		for(DataLayer dataLayer : dataLayers) {

			PolygonStyle polygonStyle1 = (PolygonStyle)new PolygonStyle().fillColor(dataLayer.fillColor).fillOpacity(0.5f).color(dataLayer.color).width(2).opacity(1f);
			map.addLayer(JTSLayer.from(DefaultGeographicCRS.WGS84, polygonStyle1, dataLayer.geometry));

		}

		dataLayers.addFirst(new DataLayer("Área total do município", geometryAreaImovel, Color.YELLOW).stroke(new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, new float[] {2,2}, 1 )));

		createMainCoordinates(map, geometryAreaImovel, crs);

		//Setando os pontos do poligono
		for(DataLayer dataLayer : dataLayers) {

			if (dataLayer.geometry instanceof Point){
				createPointCoordinates(map, dataLayer.geometry , dataLayer.name, crs);
			}
		}

		BufferedImage mapa = new BufferedImage(MAP_WIDTH, MAP_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		map.render(MAP_WIDTH, MAP_HEIGHT, Format.PNG, mapa);

		graphics.drawRect(HORIZONTAL_MARGIN_SIZE, topY, MAP_WIDTH + 1, MAP_HEIGHT + 1);
		graphics.drawImage(mapa, HORIZONTAL_MARGIN_SIZE + 1, topY + 1, null);

		topY += MAP_HEIGHT + 2 + VERTICAL_MARGIN_SIZE;
		// Fim Mapa

		// Início rodapé

		int frameWidth = (WIDTH - HORIZONTAL_MARGIN_SIZE * 2 - HORIZONTAL_MARGIN_SIZE / 2) / 2;
		createLegendTable2(graphics, HORIZONTAL_MARGIN_SIZE, topY, frameWidth, 120, dataLayers);
		createAreasTable2(graphics, WIDTH - HORIZONTAL_MARGIN_SIZE - frameWidth, topY, frameWidth, 120, crs, dataLayers);

		// Fim rodapé

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {
			ImageIO.write(newImage, Format.PNG.formatName, out);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}

		return "data:image/png;base64," + Base64.encodeBase64String(out.toByteArray());

	}

	private GrupoDataLayerImagem createMapCaracterizacaoImovel(CamadaGeoAtividadeVO geometriaImovel, LinkedList<GrupoDataLayer> grupoDataLayers) {

		BufferedImage newImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = newImage.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setColor(Color.WHITE);
		graphics.fill(new Rectangle(0, 0, WIDTH, HEIGHT));
		graphics.setColor(Color.BLACK);

		graphics.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);

		int topY = VERTICAL_MARGIN_SIZE - 10;

		// Inicio Header

		// topY = createHeader(graphics, 0, topY, 0);

		// Fim Header

		// Inicio Mapa

		Geometry geometryAreaImovel = new GeometryFactory().createGeometryCollection(geometriaImovel.geometrias.stream().map(geometria -> geometria.geometria).toArray(Geometry[]::new));

		CoordinateReferenceSystem crs = GeoCalc.detecteCRS(geometryAreaImovel)[0];

		TMSMap map = createMap(crs);
		map.zoomTo(geometryAreaImovel.getEnvelopeInternal(), MAP_WIDTH, MAP_HEIGHT, 0, 16, 256, 256);

		PolygonStyle polygonStyle = (PolygonStyle)new PolygonStyle().fillOpacity(0f).color(Color.YELLOW).width(2).dashArray(2f).opacity(1f);
		map.addLayer(JTSLayer.from(DefaultGeographicCRS.WGS84, polygonStyle, geometryAreaImovel));

		//Uni todas as dataLayer de todos os grupos dataLayer
		LinkedList<DataLayer> dataLayers = new LinkedList<>();

		for(GrupoDataLayer grupoDataLayer : grupoDataLayers) {
			dataLayers.addAll(grupoDataLayer.dataLayers);
		}

		for(DataLayer dataLayer : dataLayers) {

			PolygonStyle polygonStyle1 = (PolygonStyle)new PolygonStyle().fillColor(dataLayer.fillColor).fillOpacity(0.5f).color(dataLayer.color).width(2).opacity(1f);
			map.addLayer(JTSLayer.from(DefaultGeographicCRS.WGS84, polygonStyle1, dataLayer.geometry));

		}

		DataLayer newDataLayer = new DataLayer("Limite", geometryAreaImovel, Color.YELLOW, "#FFFF00").stroke(new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, new float[] {2,2}, 1 ));
		dataLayers.addFirst(newDataLayer);

		List<DataLayer> newDataLayers = new ArrayList<>();
		newDataLayers.add(newDataLayer);

		grupoDataLayers.add(new GrupoDataLayer("Área total da propriedade", newDataLayers));

		createMainCoordinates(map, geometryAreaImovel, crs);

		//Setando os pontos do poligono
		for(DataLayer dataLayer : dataLayers) {

			if (dataLayer.geometry instanceof Point){
				createPointCoordinates(map, dataLayer.geometry , dataLayer.name, crs);
			}
		}

		BufferedImage mapa = new BufferedImage(MAP_WIDTH, MAP_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		map.render(MAP_WIDTH, MAP_HEIGHT, Format.PNG, mapa);

		graphics.drawRect(HORIZONTAL_MARGIN_SIZE, topY, MAP_WIDTH + 1, MAP_HEIGHT + 1);
		graphics.drawImage(mapa, HORIZONTAL_MARGIN_SIZE + 1, topY + 1, null);

		topY += MAP_HEIGHT + 2 + VERTICAL_MARGIN_SIZE;
		// Fim Mapa

		// Início rodapé

		LinkedList<GrupoDataLayer> grupoDataLayersOrder = new LinkedList<>();
		grupoDataLayers.descendingIterator().forEachRemaining(grupoDataLayer -> {
			grupoDataLayersOrder.add(grupoDataLayer);
		});

		int height = 120 * ((dataLayers.size()/5) + 5);

		int frameWidth = (WIDTH - HORIZONTAL_MARGIN_SIZE * 2 - HORIZONTAL_MARGIN_SIZE / 2) / 2;
		//createLegendTable(graphics, HORIZONTAL_MARGIN_SIZE, topY, frameWidth, height, grupoDataLayersOrder);
		//createAreasTable(graphics, WIDTH - HORIZONTAL_MARGIN_SIZE - frameWidth, topY, frameWidth, height, crs, grupoDataLayersOrder);

		// Fim rodapé

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {

			ImageIO.write(newImage, Format.PNG.formatName, out);

		} catch(IOException e) {

			throw new RuntimeException(e);

		}

		return new GrupoDataLayerImagem("data:image/png;base64," + Base64.encodeBase64String(out.toByteArray()), grupoDataLayersOrder);

	}

	public String createMapPoligonosCicloRestauracaoImovel(Geometry geometryAreaImovel, List<Geometry> geometriesPoligonos, Map<LayerType, Geometry> geometriesCaracterizacao, int numeroCiclos) {

		LinkedList<DataLayer> dataLayers = new LinkedList<>();

		for(Entry<LayerType, Geometry> entry : geometriesCaracterizacao.entrySet()) {

			Geometry geometry = entry.getValue();
			LayerType layerType = entry.getKey();

			if(geometry == null) {
				continue;
			}

			Color color = Color.decode(layerType.getColor());
			Color fillColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 127);

			dataLayers.add(new DataLayer(layerType.getName(), geometry, color).fillColor(fillColor));

		}

		dataLayers.sort((o1, o2) -> o1.name.compareTo(o2.name));

		return createMapPoligonosCicloRestauracaoImovel(geometryAreaImovel, geometriesPoligonos, dataLayers, numeroCiclos);

	}
	private String createMapPoligonosCicloRestauracaoImovel(Geometry geometryAreaImovel, List<Geometry> geometriesPoligonos, LinkedList<DataLayer> dataLayers, int numeroCiclos) {

		BufferedImage newImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = newImage.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setColor(Color.WHITE);
		graphics.fill(new Rectangle(0, 0, WIDTH, HEIGHT));
		graphics.setColor(Color.BLACK);

		graphics.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);

		int topY = VERTICAL_MARGIN_SIZE - 10;

		// Inicio Header

		// topY = createHeader(graphics, 0, topY, 0);

		// Fim Header

		// Inicio Mapa

		CoordinateReferenceSystem crs = GeoCalc.detecteCRS(geometryAreaImovel)[0];

		TMSMap map = createMap(crs);
		map.zoomTo(geometryAreaImovel.getEnvelopeInternal(), MAP_WIDTH, MAP_HEIGHT, 0, 16, 256, 256);

		PolygonStyle polygonStyle = (PolygonStyle)new PolygonStyle().fillOpacity(0f).color(Color.YELLOW).width(2).dashArray(2f).opacity(1f);
		map.addLayer(JTSLayer.from(DefaultGeographicCRS.WGS84, polygonStyle, geometryAreaImovel));

		for(DataLayer dataLayer : dataLayers) {

			PolygonStyle polygonStyle1 = (PolygonStyle)new PolygonStyle().fillColor(dataLayer.fillColor).fillOpacity(0.5f).color(dataLayer.color).width(2).opacity(1f);
			map.addLayer(JTSLayer.from(DefaultGeographicCRS.WGS84, polygonStyle1, dataLayer.geometry));

		}

		dataLayers.addFirst(new DataLayer("Área total do imóvel", geometryAreaImovel, Color.YELLOW).stroke(new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, new float[] {2,2}, 1 )));

		//Setando os pontos do poligono
		int index = 1;
		for (Geometry geometriePoligono : geometriesPoligonos) {

			createPoligonosCoordinates(map, geometriePoligono, index, crs);

			index ++;
		}

		BufferedImage mapa = new BufferedImage(MAP_WIDTH, MAP_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		map.render(MAP_WIDTH, MAP_HEIGHT, Format.PNG, mapa);

		graphics.drawRect(HORIZONTAL_MARGIN_SIZE, topY, MAP_WIDTH + 1, MAP_HEIGHT + 1);
		graphics.drawImage(mapa, HORIZONTAL_MARGIN_SIZE + 1, topY + 1, null);

		topY += MAP_HEIGHT + 2 + VERTICAL_MARGIN_SIZE;
		// Fim Mapa

		// Início rodapé

		int frameWidth = (WIDTH - HORIZONTAL_MARGIN_SIZE * 2 - HORIZONTAL_MARGIN_SIZE / 2) / 2;
		int frameHeight = 60 + 30 * numeroCiclos;
		createLegendTable2(graphics, HORIZONTAL_MARGIN_SIZE, topY, frameWidth, frameHeight, dataLayers);
		createAreasTable2(graphics, WIDTH - HORIZONTAL_MARGIN_SIZE - frameWidth, topY, frameWidth, frameHeight, crs, dataLayers);

		// Fim rodapé

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {
			ImageIO.write(newImage, Format.PNG.formatName, out);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}

		return "data:image/png;base64," + Base64.encodeBase64String(out.toByteArray());

	}

	private void createLegendTable2(Graphics2D graphics, int x, int y, int width, int height, Collection<DataLayer> dataLayers) {

		float tableFontSize = BRAND_X * 2.3f;
		Font headerFont = this.brandPrimaryFont1.deriveFont(Font.BOLD, tableFontSize);
		graphics.setFont(headerFont);

		FontMetrics fm = graphics.getFontMetrics(headerFont);

		int hCellPadding = 10;
		int vCellPadding = 1;

		int textTopY = y + fm.getHeight();

		graphics.setColor(Color.BLACK);
		graphics.drawRect(x, y, width, height);

		String headerText = "Legenda";
		graphics.drawString(headerText, x + width / 2 - fm.stringWidth(headerText) / 2, textTopY + vCellPadding);

		Font contentFont = this.brandPrimaryFont1.deriveFont(tableFontSize);
		graphics.setFont(contentFont);

		int legendSize = fm.getHeight();
		int lineHeight = legendSize + hCellPadding;
		int lineNumber = 0;
		for(DataLayer dataLayer: dataLayers) {

			lineNumber++;

			graphics.setColor(dataLayer.color);
			graphics.setStroke(dataLayer.stroke);

			graphics.drawRect(x + hCellPadding, y + lineHeight * lineNumber, legendSize, legendSize);

			if(dataLayer.fillColor != null) {
				graphics.setColor(dataLayer.fillColor);
				graphics.fillRect(x + hCellPadding, y + lineHeight * lineNumber, legendSize, legendSize);
			}

			graphics.setStroke(new BasicStroke());
			graphics.setColor(Color.BLACK);

			String legend = dataLayer.name;

			graphics.drawString(legend, x + legendSize + hCellPadding * 2, y + lineHeight * lineNumber + legendSize - 1);

		}

	}

	private void createLegendTable(Graphics2D graphics, int x, int y, int width, int height, Collection<GrupoDataLayer> grupoDataLayers) {

		float tableFontSize = BRAND_X * 2.3f;
		Font headerFont = this.brandPrimaryFont1.deriveFont(Font.BOLD, tableFontSize);
		graphics.setFont(headerFont);

		FontMetrics fm = graphics.getFontMetrics(headerFont);

		int hCellPadding = 10;
		int vCellPadding = 1;

		int textTopY = y + fm.getHeight();

		graphics.setColor(Color.BLACK);
		graphics.drawRect(x, y, width, height);

		String headerText = "Legenda";
		graphics.drawString(headerText, x + width / 2 - fm.stringWidth(headerText) / 2, textTopY + vCellPadding);

		Font contentFont = this.brandPrimaryFont1.deriveFont(tableFontSize);

		int legendSize = fm.getHeight();
		int lineHeight = legendSize + hCellPadding;
		int lineNumber = 0;

		for (GrupoDataLayer grupoDataLayer: grupoDataLayers) {

			lineNumber++;
			graphics.setFont(headerFont);

			graphics.drawString(grupoDataLayer.titulo, x + legendSize + hCellPadding * 2, y + lineHeight * lineNumber + legendSize - 1);

			for (DataLayer dataLayer : grupoDataLayer.dataLayers) {

				graphics.setFont(contentFont);

				lineNumber++;

				graphics.setColor(dataLayer.color);
				graphics.setStroke(dataLayer.stroke);

				graphics.drawRect(x + hCellPadding, y + lineHeight * lineNumber, legendSize, legendSize);

				if (dataLayer.fillColor != null) {
					graphics.setColor(dataLayer.fillColor);
					graphics.fillRect(x + hCellPadding, y + lineHeight * lineNumber, legendSize, legendSize);
				}

				graphics.setStroke(new BasicStroke());
				graphics.setColor(Color.BLACK);

				String legend = dataLayer.name;

				graphics.drawString(legend, x + legendSize + hCellPadding * 2, y + lineHeight * lineNumber + legendSize - 1);

			}
		}

	}

	private void createAreasTable2(Graphics2D graphics, int x, int y, int width, int height, CoordinateReferenceSystem utmCrs, Collection<DataLayer> dataLayes) {

		float tableFontSize = BRAND_X * 2.3f;
		Font headerFont = this.brandPrimaryFont1.deriveFont(Font.BOLD, tableFontSize);
		graphics.setFont(headerFont);

		FontMetrics fm = graphics.getFontMetrics(headerFont);

		int hCellPadding = 10;
		int vCellPadding = 2;

		int textTopY = y + fm.getHeight();

		graphics.setColor(Color.BLACK);
		graphics.drawRect(x, y, width, height);

		String headerText = "Quadro de Áreas";
		graphics.drawString(headerText, x + width / 2 - fm.stringWidth(headerText) / 2, textTopY + vCellPadding);

		Font contentFont = this.brandPrimaryFont1.deriveFont(tableFontSize);
		graphics.setFont(contentFont);

		DecimalFormat df = new DecimalFormat("#,##0.0000");

		int legendSize = fm.getHeight();
		int lineHeight = legendSize + hCellPadding;
		int lineNumber = 0;
		for(DataLayer dataLayer: dataLayes) {

			lineNumber++;

			String legend = dataLayer.name;
			String area = df.format(GeoCalc.area(dataLayer.geometry, utmCrs) / 10000) + " ha";

			graphics.drawString(legend, x + hCellPadding, y + lineHeight * lineNumber + legendSize - 1);
			graphics.drawString(area, x + width - hCellPadding - fm.stringWidth(area), y + lineHeight * lineNumber + legendSize - 1);

		}

	}

	private void createAreasTable(Graphics2D graphics, int x, int y, int width, int height, CoordinateReferenceSystem utmCrs, Collection<GrupoDataLayer> grupoDataLayes) {

		float tableFontSize = BRAND_X * 2.3f;
		Font headerFont = this.brandPrimaryFont1.deriveFont(Font.BOLD, tableFontSize);
		graphics.setFont(headerFont);

		FontMetrics fm = graphics.getFontMetrics(headerFont);

		int hCellPadding = 10;
		int vCellPadding = 2;

		int textTopY = y + fm.getHeight();

		graphics.setColor(Color.BLACK);
		graphics.drawRect(x, y, width, height);

		String headerText = "Quadro de Áreas";
		graphics.drawString(headerText, x + width / 2 - fm.stringWidth(headerText) / 2, textTopY + vCellPadding);

		Font contentFont = this.brandPrimaryFont1.deriveFont(tableFontSize);


		DecimalFormat df = new DecimalFormat("#,##0.0000");

		int legendSize = fm.getHeight();
		int lineHeight = legendSize + hCellPadding;
		int lineNumber = 0;

		for (GrupoDataLayer grupoDataLayer: grupoDataLayes) {

			graphics.setFont(headerFont);

			lineNumber++;

			graphics.drawString(grupoDataLayer.titulo, x + hCellPadding, y + lineHeight * lineNumber + legendSize - 1);

			for (DataLayer dataLayer : grupoDataLayer.dataLayers) {

				graphics.setFont(contentFont);

				lineNumber++;

				String legend = dataLayer.name;
				String area = df.format(GeoCalc.area(dataLayer.geometry, utmCrs) / 10000) + " ha";

				graphics.drawString(legend, x + hCellPadding, y + lineHeight * lineNumber + legendSize - 1);
				graphics.drawString(area, x + width - hCellPadding - fm.stringWidth(area), y + lineHeight * lineNumber + legendSize - 1);

			}
		}

	}

	private TMSMap createMap(CoordinateReferenceSystem utmCrs) {

		TMSMap map = new TMSMap();

		try {
			map.addLayer(TMSLayer.from(new URL(URL_MOSAICOS), false));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		Font font = this.brandSecondaryFont.deriveFont(12f);

		map.padding(MAP_PADDING_SIZE, MAP_PADDING_SIZE);
		map.addLayer(new GridLayer(MAP_MARGIN_SIZE, MAP_WIDTH, MAP_HEIGHT, utmCrs, font));
		map.addLayer(ScaleBar.Simple.from(font, Color.WHITE).bottom(MAP_COMPONENTS_MARGIN_SIZE).right(MAP_COMPONENTS_MARGIN_SIZE).height(10));

		map.addLayer((CustomLayer)(graphics, mapContent, mapViewport) -> {

			File file = new File(Play.applicationPath + "/app/assets/images/rosa_s.png");

			BufferedImage bufferedImage = null;

			try {

				bufferedImage = ImageIO.read(file);

			} catch(IOException e) {
				throw new RuntimeException(file.getAbsolutePath(), e);
			}

			int size = MAP_MARGIN_SIZE * 4;
			System.out.println(MAP_PADDING_SIZE);
			System.out.println(MAP_MARGIN_SIZE);
			System.out.println(size);

			graphics.drawImage(bufferedImage, MAP_COMPONENTS_MARGIN_SIZE - 7, MAP_COMPONENTS_MARGIN_SIZE, size, size, null);

		});

		return map;

	}

	private Collection<Coordinate> createMainCoordinates(TMSMap map, Geometry geometryAreaImovel, CoordinateReferenceSystem crs) {

		List<Coordinate> mainCoordinatesResult = new ArrayList<>();

		Coordinate[] coordinates = geometryAreaImovel.getCoordinates();

		Map<String, Coordinate> mainCoordinates = new LinkedHashMap<>();
		mainCoordinates.put("S", coordinates[0]);
		mainCoordinates.put("E", coordinates[0]);
		mainCoordinates.put("N", coordinates[0]);
		mainCoordinates.put("W", coordinates[0]);

		for(Coordinate coordinate : coordinates) {

			if(coordinate.y > mainCoordinates.get("N").y) {
				mainCoordinates.put("N", coordinate);
			} else if(coordinate.y < mainCoordinates.get("S").y) {
				mainCoordinates.put("S", coordinate);
			}

			if(coordinate.x > mainCoordinates.get("W").x) {
				mainCoordinates.put("W", coordinate);
			} else if(coordinate.x < mainCoordinates.get("E").x) {
				mainCoordinates.put("E", coordinate);
			}

		}

		map.addLayer((CustomLayer)(graphics, mapContent, mapViewport) -> {

			Font font = new Font("Dialog", Font.PLAIN, 10);
			graphics.setFont(font);
			graphics.setStroke(new BasicStroke());
			graphics.setColor(Color.RED);

			int coordinateNumber = 1;
			LinkedHashSet<Coordinate> resultCoordinates = new LinkedHashSet<>(mainCoordinates.values());
			Iterator<Coordinate> iterator = resultCoordinates.iterator();

			while(iterator.hasNext()) {

				Coordinate coordinate = iterator.next();

				Point2D resultPoint = new Point2D.Double();
				mapViewport.getWorldToScreen().transform(new Point2D.Double(coordinate.x, coordinate.y), resultPoint);

				graphics.fill(new Ellipse2D.Double(resultPoint.getX() - 2, resultPoint.getY() - 2, 4, 4));
				graphics.drawString("P" + coordinateNumber, (float)resultPoint.getX() + 3, (float)resultPoint.getY() + 3);

				Coordinate worldUtmCoordinate = GeoCalc.transform(coordinate, DefaultGeographicCRS.WGS84, crs);
				mainCoordinatesResult.add(worldUtmCoordinate);

				coordinateNumber++;

			}

		});

		return mainCoordinatesResult;

	}

	private Collection<Coordinate> createPoligonosCoordinates(TMSMap map, Geometry geometriePoligono, int numeroPoligono, CoordinateReferenceSystem crs) {

		List<Coordinate> mainCoordinatesResult = new ArrayList<>();

		Point centerPoint = geometriePoligono.getInteriorPoint();

		Coordinate coordinate = centerPoint.getCoordinate();

		map.addLayer((CustomLayer)(graphics, mapContent, mapViewport) -> {

			Font font = new Font("Dialog", Font.PLAIN, 10);
			graphics.setFont(font);
			graphics.setStroke(new BasicStroke());
			graphics.setColor(Color.RED);

			Point2D resultPoint = new Point2D.Double();
			mapViewport.getWorldToScreen().transform(new Point2D.Double(coordinate.x, coordinate.y), resultPoint);

			graphics.fill(new Ellipse2D.Double(resultPoint.getX() - 2, resultPoint.getY() - 2, 4, 4));
			graphics.drawString(String.valueOf(numeroPoligono), (float)resultPoint.getX() + 3, (float)resultPoint.getY() + 3);

			Coordinate worldUtmCoordinate = GeoCalc.transform(coordinate, DefaultGeographicCRS.WGS84, crs);
			mainCoordinatesResult.add(worldUtmCoordinate);

		});

		return mainCoordinatesResult;

	}

	private Collection<Coordinate> createPointCoordinates(TMSMap map, Geometry geometriePoligono, String numeroPoligono, CoordinateReferenceSystem crs) {

		List<Coordinate> mainCoordinatesResult = new ArrayList<>();

		Point centerPoint = geometriePoligono.getInteriorPoint();

		Coordinate coordinate = centerPoint.getCoordinate();

		map.addLayer((CustomLayer)(graphics, mapContent, mapViewport) -> {

			Font font = new Font("Dialog", Font.PLAIN, 10);
			graphics.setFont(font);
			graphics.setStroke(new BasicStroke());
			graphics.setColor(Color.RED);

			Point2D resultPoint = new Point2D.Double();
			mapViewport.getWorldToScreen().transform(new Point2D.Double(coordinate.x, coordinate.y), resultPoint);

			graphics.fill(new Ellipse2D.Double(resultPoint.getX() - 2, resultPoint.getY() - 2, 4, 4));
			graphics.drawString(String.valueOf(numeroPoligono), (float)resultPoint.getX() + 3, (float)resultPoint.getY() + 3);

			Coordinate worldUtmCoordinate = GeoCalc.transform(coordinate, DefaultGeographicCRS.WGS84, crs);
			mainCoordinatesResult.add(worldUtmCoordinate);

		});

		return mainCoordinatesResult;

	}

	private void createMainCoordinatesTable(Collection<Coordinate> mainCoordinates, Graphics2D graphics, int topY) {

		topY +=  MAP_HEIGHT + VERTICAL_MARGIN_SIZE;

		String column1Header = "Vértices";
		String column2Header = "Coordenada X";
		String column3Header = "Coordenada Y";

		float tableFontSize = BRAND_X * 2.3f;
		Font headerFont = this.brandPrimaryFont1.deriveFont(Font.BOLD, tableFontSize);

		FontMetrics fm = graphics.getFontMetrics(headerFont);

		int hCellPadding = 3;
		int vCellPadding = 1;

		int column1Width = fm.stringWidth(column1Header) + hCellPadding * 2;
		int column2Width = fm.stringWidth(column2Header) + hCellPadding * 2;
		int column3Width = fm.stringWidth(column3Header) + hCellPadding * 2;
		int lineHeight = fm.getHeight() + vCellPadding * 2 + 2;

		int vMargin = (WIDTH - (column1Width + column2Width + column3Width) + 2) / 2;
		int minX = vMargin;
		int maxX = WIDTH - vMargin;
		int textTopY = topY + fm.getHeight();

		graphics.setColor(Color.LIGHT_GRAY);
		graphics.fillRect(minX, topY, column1Width + column2Width + column3Width, lineHeight);

		graphics.setColor(Color.BLACK);
		graphics.setFont(headerFont);

		graphics.drawLine(minX, topY, maxX, topY);
		graphics.drawString(column1Header, minX + hCellPadding, textTopY + vCellPadding);
		graphics.drawString(column2Header, minX + column1Width + hCellPadding, textTopY + vCellPadding);
		graphics.drawString(column3Header, minX + column1Width + column2Width + hCellPadding, textTopY + vCellPadding);

		graphics.drawLine(minX, topY + lineHeight, maxX, topY + lineHeight);

		Font contentFont = this.brandPrimaryFont1.deriveFont(tableFontSize);
		graphics.setFont(contentFont);

		DecimalFormat format = new DecimalFormat("#.0000");

		int polygonNumber = 0;
		int lineNumber = 1;
		for(Coordinate coordinate: mainCoordinates) {

			polygonNumber++;
			lineNumber++;

			String column1Text = "P" + polygonNumber;
			String column2Text = format.format(coordinate.x);
			String column3Text = format.format(coordinate.y);

			graphics.drawString(column1Text, minX + hCellPadding, textTopY + lineHeight * polygonNumber + vCellPadding);
			graphics.drawString(column2Text, minX + column1Width + hCellPadding, textTopY + lineHeight * polygonNumber + vCellPadding);
			graphics.drawString(column3Text, minX + column1Width + column2Width + hCellPadding, textTopY + lineHeight * polygonNumber + vCellPadding);

			graphics.drawLine(minX, topY + lineHeight * lineNumber, maxX, topY + lineHeight * lineNumber);

		}

		graphics.drawLine(minX, topY, minX, topY + lineHeight * lineNumber);
		graphics.drawLine(minX + column1Width, topY, minX + column1Width, topY + lineHeight * lineNumber);
		graphics.drawLine(minX + column1Width + column2Width, topY, minX + column1Width + column2Width, topY + lineHeight * lineNumber);
		graphics.drawLine(minX + column1Width + column2Width + column3Width, topY, minX + column1Width + column2Width + column3Width, topY + lineHeight * lineNumber);

		topY += lineHeight * lineNumber + VERTICAL_MARGIN_SIZE;

	}

	private int createHeader(Graphics2D graphics, int x, int y, int width) {

		graphics.drawImage(this.brandBufferedImage, (WIDTH / 2) - (BRAND_WIDTH / 2) , y, BRAND_WIDTH, BRAND_HEIGHT, null);

		String logoTextLine1 = "RONDÔNIA";
		String logoTextLine2 = "Governo do Estado";

		float logoTextLine1FontSize = BRAND_X * 3f;
		Font logoTextLine1Font = this.brandPrimaryFont.deriveFont(logoTextLine1FontSize);
		FontMetrics fm = graphics.getFontMetrics(logoTextLine1Font);

		int textTopY = y + BRAND_HEIGHT + fm.getHeight() - 5;

		graphics.setFont(logoTextLine1Font);
		graphics.drawString(logoTextLine1, (WIDTH / 2) - (fm.stringWidth(logoTextLine1) / 2), textTopY);

		float logoTextLine2FontSize = BRAND_X * 1.3f;
		Font logoTextLine2Font = this.brandPrimaryFont1.deriveFont(logoTextLine2FontSize);
		fm = graphics.getFontMetrics(logoTextLine2Font);

		textTopY += fm.getHeight();

		graphics.setFont(logoTextLine2Font);
		graphics.drawString(logoTextLine2, (WIDTH / 2) - (fm.stringWidth(logoTextLine2) / 2) + 1, textTopY);

		String textLine1 = "ESTADO DE RONDÔNIA";
		String textLine2 = "Secretaria de Estado do Desenvolvimento Ambiental - SEDAM";
		String textLine3 = "Coordenadoria de Monitoramento e Regularização Ambiental - COMRAR";

		float textLine1FontSize = BRAND_X * 2f;
		Font textLine1Font = this.brandSecondaryFont.deriveFont(textLine1FontSize);
		fm = graphics.getFontMetrics(textLine1Font);

		textTopY += fm.getHeight() + BRAND_X;

		graphics.setFont(textLine1Font);
		graphics.drawString(textLine1, (WIDTH / 2) - (fm.stringWidth(textLine1) / 2) + 2, textTopY);

		textTopY += fm.getHeight();
		graphics.drawString(textLine2, (WIDTH / 2) - (fm.stringWidth(textLine2) / 2) + 2, textTopY);

		textTopY += fm.getHeight();
		graphics.drawString(textLine3, (WIDTH / 2) - (fm.stringWidth(textLine3) / 2) + 2, textTopY);

		y += textTopY - fm.getHeight() + VERTICAL_MARGIN_SIZE;

		return y;

	}

	public static String getColorTemaCiclo() {

		Random random = new Random();
		int nextInt = random.nextInt(256*256*256);
		return String.format("#%06x", nextInt);
	}

}
