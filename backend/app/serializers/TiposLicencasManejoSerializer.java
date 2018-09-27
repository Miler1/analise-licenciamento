package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class TiposLicencasManejoSerializer {

	public static JSONSerializer findAll = SerializerUtil.create(
			"id",
			"nome",
			"codigo");
}
