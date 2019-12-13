package utils;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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
	
	public static String formatarMesMinusculo(Date data) {

		SimpleDateFormat DATE_FORMAT;

		DATE_FORMAT = new SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy");
		DateFormatSymbols symbols = new DateFormatSymbols(new Locale("pt", "BR"));
		symbols.setMonths(new String[] {"janeiro","fevereiro","março","abril","maio","junho","julho","agosto","setembro","outubro","novembro","dezembro"});
		DATE_FORMAT.setDateFormatSymbols(symbols);

		String st;
		synchronized (DATE_FORMAT) {
			st = DATE_FORMAT.format(data);
		}
       
       return st;
				       

	}

	public static Date somaDiasEmData(Date data, Integer prazo) {

		Calendar c = Calendar.getInstance();
		c.setTime(data);
		c.add(Calendar.DAY_OF_MONTH, prazo);

		return c.getTime();

	}

}
