package utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Classe contendo utilitários para Strings
 */
public class StringUtils {

    public static String formatarCEP(String str) {

        String digits = str.replaceAll("[^0-9]", "");
        if(digits.length() == 8){

            return digits.substring(0, 1) + "." + digits.substring(2, 4) + "-" + digits.substring(5) ;
        }
        return str;
    }

    public static String formatarEspacamentoHtml(String str){

        return str.replace("\u0040", "\u0020");
    }

    public static String addHtmlStyles(String text, List<String> styles){

        String estilos = "";
        for(String style : styles){
            estilos += style + ";";
        }
        String str = "style='" + estilos + "'";
        return text.replace("<p", "<p " + str);
    }

    public static String cleanHtmlOldStyles(String text){

        return text.replaceAll(" style='[^']*'","");
    }

    public static String convertCharsetString(String value) {

        String charsets[] = new String[]{"WINDOWS-1250","US-ASCII","ISO-8859-1"};

        String chSet = charset(value, charsets);

        return convertCharsetString(value, chSet, StandardCharsets.UTF_8.name());
    }

    private static String convertCharsetString(String value, String fromEncoding, String toEncoding) {

        try {
            return new String(value.getBytes(fromEncoding), toEncoding);
        }
        catch (UnsupportedEncodingException e) {
            return value;
        }
    }

    private static String charset(String value, String[] charsets) {
        String probe = StandardCharsets.UTF_8.name();
        for(String c : charsets) {
            Charset charset = Charset.forName(c);
            if(charset != null) {
                if (value.equals(convertCharsetString(convertCharsetString(value, charset.name(), probe), probe, charset.name()))) {
                    return c;
                }
            }
        }
        return StandardCharsets.UTF_8.name();
    }

}