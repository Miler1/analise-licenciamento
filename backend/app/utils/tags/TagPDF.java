package utils.tags;

import groovy.lang.Closure;
import play.templates.FastTags;
import play.templates.GroovyTemplate;
import play.templates.JavaExtensions;

import java.io.PrintWriter;
import java.util.Locale;
import java.util.Map;

public class TagPDF extends FastTags {

	public static void _cpf(Map<?, ?> args, Closure body, PrintWriter out,
	                           GroovyTemplate.ExecutableTemplate template, int fromLine) {

		String cpf = JavaExtensions.toString(body);
		cpf = cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9);

		out.println(cpf);
	}
}
