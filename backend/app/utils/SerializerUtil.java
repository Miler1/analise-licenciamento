package utils;

import java.util.Date;

import play.Play;
import play.Play.Mode;
import serializers.DateSerializer;
import flexjson.JSONSerializer;

public class SerializerUtil {

	public static JSONSerializer create(String ... includes) {
		
		boolean prettyPrint = Play.mode == Mode.DEV;
	
		return new JSONSerializer()
			.include(includes)
			.exclude("*")
			.prettyPrint(prettyPrint)
			.transform(DateSerializer.getTransformer(), Date.class);
	}

	public static JSONSerializer createWithDateTime(String ... includes) {

		boolean prettyPrint = Play.mode == Mode.DEV;

		return new JSONSerializer()
				.include(includes)
				.exclude("*")
				.prettyPrint(prettyPrint)
				.transform(DateSerializer.getTransformerWithDateTime(), Date.class);
	}
}