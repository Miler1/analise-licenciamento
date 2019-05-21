package security;

import play.Logger;
import play.Play;
import play.mvc.Http.Request;
import play.mvc.results.Unauthorized;
import sun.net.InetAddressCachePolicy;
import utils.InetAddressComparator;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ExternalServiceSecurity {

	protected static String ADRESS_LIST = "authentication.external.httpAddress";
	
	public static void validateAdress(Request request) {
		
		String validateAdress = Play.configuration.getProperty("authentication.external.validateAddress");
		
		if (validateAdress.isEmpty())
			return;
		
		String remoteAddress = null;

		if(request.headers.get("x-real-ip") != null) {
			remoteAddress = request.headers.get("x-real-ip").value();
		} else {
			remoteAddress = request.remoteAddress;
		}
		
		String adressList = Play.configuration.getProperty(ADRESS_LIST);
		
		// Se nao estiver configurada a seguranca por IP
		if (adressList == null || adressList.trim().isEmpty() || remoteAddress == null)
			throw new Unauthorized("Unauthorized");
		
		String[] list = adressList.split(",");

		InetAddress addr = null;
		InetAddress remoteAddr = null;
		
		Boolean isPermitted = false;
		
		try {
			
			remoteAddr = InetAddress.getByName(remoteAddress);
			
			for (int i = 0; i < list.length; i++) {
				
				InetAddressComparator addressComparator = new InetAddressComparator();

				addr = InetAddress.getByName(list[i]);
				
				if (addressComparator.compare(remoteAddr, addr) == 0)
					isPermitted = true;
			}
			

		} catch (UnknownHostException e) {
			throw new Unauthorized("Unauthorized");
		}
		
		if (!isPermitted) {
			Logger.error("UNAUTHORIZED IP: " + remoteAddress);
			throw new Unauthorized("Unauthorized");
		}
	}
}
