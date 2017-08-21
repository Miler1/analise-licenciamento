package utils;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {

	public static int getDiferencaEmDias(Date a, Date b) {
		
	    int tempDifference = 0;
	    int difference = 0;
	    
	    Calendar earlier = Calendar.getInstance();
	    Calendar later = Calendar.getInstance();

	    if (a.compareTo(b) < 0)
	    {
	        earlier.setTime(a);
	        later.setTime(b);
	    }
	    else
	    {
	        earlier.setTime(b);
	        later.setTime(a);
	    }

	    while (earlier.get(Calendar.YEAR) != later.get(Calendar.YEAR))
	    {
	        tempDifference = 365 * (later.get(Calendar.YEAR) - earlier.get(Calendar.YEAR));
	        difference += tempDifference;

	        earlier.add(Calendar.DAY_OF_YEAR, tempDifference);
	    }

	    difference += later.get(Calendar.DAY_OF_YEAR) - earlier.get(Calendar.DAY_OF_YEAR);
	    
	    return difference;
	}
	
	public static Long getDiferencaEmHoras(Date inicial,Date fim) {
		
		Long diferenca = getDiferencaEmMilisegundos(inicial, fim);
		
		return diferenca / (60 * 60 * 1000);// DIFERENCA EM HORAS            
	}
	
	public static Long getDiferencaEmMinutos(Date inicial,Date fim) {
		
		Long diferenca = getDiferencaEmMilisegundos(inicial, fim);
		
		return diferenca / (60 * 1000);// DIFERENCA EM MINUTOS            
	}

	public static Long getDiferencaEmMilisegundos(Date inicial,Date fim) {
		
		Calendar dataA = Calendar.getInstance();
		dataA.setTime(inicial);
		
		Calendar dataB = Calendar.getInstance();
		dataB.setTime(fim);
		
		return dataB.getTimeInMillis() - dataA.getTimeInMillis();            
	}
	
	public static String getDiferencaEmDiasHorasMinutos(Date inicial, Date fim) {
	
		Long diferenca = getDiferencaEmMilisegundos(inicial, fim);
		Long diasDiferenca = TimeUnit.MILLISECONDS.toDays(diferenca);
		
		//Transforma a diferença de dias em positivo
		if(diasDiferenca < 0)
			diasDiferenca = diasDiferenca * -1;
		
		StringBuilder sb = new StringBuilder();
		sb.append(diasDiferenca + " dias, ");
		sb.append(String.format("%02d", TimeUnit.MILLISECONDS.toHours(diferenca) % TimeUnit.DAYS.toHours(1)) + ":");
		sb.append(String.format("%02d", TimeUnit.MILLISECONDS.toMinutes(diferenca) % TimeUnit.HOURS.toMinutes(1)) + "h");
		
		return sb.toString();
	}
	
	public static String formatarMesMinusculo(String strData) {
		
		String regexMes = "de\\s[a-zA-Z]{1,}\\sde";
		Pattern padraoMes = Pattern.compile(regexMes);
		
		Matcher matcher = padraoMes.matcher(strData);
		
		matcher.find();
		
		String nomeMes = matcher.group(0).toLowerCase();
		
		return strData.replaceAll(regexMes, nomeMes);
	}	

}
