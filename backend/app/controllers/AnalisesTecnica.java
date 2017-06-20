package controllers;

import com.vividsolutions.jts.geom.Geometry;
import models.Processo;
import models.geocalculo.Geoserver;
import models.licenciamento.ImovelEmpreendimento;
import org.apache.commons.io.FileUtils;
import org.geotools.geometry.jts.WKTReader2;

import java.io.File;
import java.nio.charset.Charset;

public class AnalisesTecnica extends GenericController {

	public void getRestricoesGeo(Long id) throws Exception {

		Geometry geometryEmpreendimento = new WKTReader2().read("POINT(-54.5526123046875 -4.088418298945898)");
		Geometry geometryImovel = new WKTReader2().read("POLYGON((-54.45648193359375 -4.165636821607836,-54.6844482421875 -4.198508047098707,-54.56634521484375 -4.332717735806342,-54.3658447265625 -4.280680030820496,-54.35211181640625 -4.198508047098707,-54.45648193359375 -4.165636821607836))");

		ImovelEmpreendimento imovel = new ImovelEmpreendimento();

		imovel.codigo = "PA-1505650-A37C2EB9275541D1BAB5D01CD8783D81";
		imovel.limite = geometryImovel;

		File file = Geoserver.verificarRestricoes(geometryEmpreendimento, imovel, "analise-geo-id-" + id);

		renderJSON(FileUtils.readFileToString(file, Charset.defaultCharset()));
	}

}
