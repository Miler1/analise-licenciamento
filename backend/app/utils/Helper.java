package utils;

import br.com.caelum.stella.format.CNPJFormatter;
import br.com.caelum.stella.format.CPFFormatter;
import br.com.caelum.stella.format.Formatter;
import br.com.caelum.stella.validation.CNPJValidator;
import com.vividsolutions.jts.geom.Coordinate;
import java.awt.*;
import org.apache.commons.lang.StringUtils;
import play.Play;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Helper {

	/**
	 *  ==================== Tratamento CPF e CNPJ ==============================
	 * 
	 * Parte do codigo:  
	 * 	Creditos à 
	 * 		@author Jaime Daniel Corrêa Mendes [a.k.a ~lordshark]
	 * 		@since 22/08/2014
	 * 
	 */
	
	public static enum Identificacao {CPF, CNPJ};
	
	private static final int[] pesosCPF = { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 };
	private static final int[] pesosCNPJ = { 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };

	
	/**
	 * Método para desformatar um cpf ou cnpj.
	 * O método irá remover os pontos (.), traços (-) e barras (/)
	 * de um cpf ou cnpj
	 *
	 * @param cpfCnpj
	 *			o cpj ou cnpj que deseja-se desformatar
	 *
	 * @return o cpf ou cnpj desformatado
	 */
	public static String desformatarCpfCnpj(String cpfCnpj) {

		if(StringUtils.isBlank(cpfCnpj) || (cpfCnpj.length() != 11 && cpfCnpj.length() != 14 && cpfCnpj.length() != 18))
			throw new IllegalArgumentException("Parametro não é um cpf nem cnpj.") ;

		cpfCnpj =  cpfCnpj.replaceAll("([.-])", "").replace("/","");

		if(cpfCnpj.length() != 11 && cpfCnpj.length() != 14) {

			throw new IllegalArgumentException("Parametro não é um cpf nem cnpj.") ;

		}

		return  cpfCnpj;

	}

	public static String formatarCoordenada(Coordinate coordinate) {

		return "Coordenadas [" + CoordenadaUtil.formataLatitudeString(coordinate.y) + ", " + CoordenadaUtil.formataLongitudeString(coordinate.x) + "]";

	}

	/**
	 * Método para formatar um cpf ou cnpj.
	 * O método irá colocar a máscara de um cpf ou cnpj
	 *
	 * @param cpfCnpj
	 *			o cpj ou cnpj que deseja-se desformatar
	 *
	 * @return o cpf ou cnpj formatado com sua máscara
	 */
	public static String formatarCpfCnpj(String cpfCnpj) {

		cpfCnpj = desformatarCpfCnpj(cpfCnpj);

		Formatter formatter = cpfCnpj.length() == 11 ? new CPFFormatter() : new CNPJFormatter();

		return formatter.format(cpfCnpj);

	}
	
	
	/**
	 * Método para validar um cpf.
	 * O método verifica se o cpf desejado é valido
	 *
	 * @param cpf
	 *			cpf que deseja-se validar
	 * 
	 * @return true para cpf válidao; false para cpf inválido
	 */
	public static boolean eValidoCPF(String cpf) {
		if (cpf == null) {
			return false;
		}
		// remove "." e "-"
		cpf = cpf.replace(".", "");
		cpf = cpf.replace("-", "");
		if (cpf.length() != 11 || cpf.equals("00000000000")) {
			return false; // sai se não tem o tamanho esperado
		}

		// passo 1 - calcula somente para a string sem o digito verificador
		Integer digito1 = calcularDigitoVerificador(cpf.substring(0, 9),
				pesosCPF);
		// passo 2 - calculo novamente com o dígito obtido no passo 1
		Integer digito2 = calcularDigitoVerificador(cpf.substring(0, 9)
				+ digito1, pesosCPF);
		// retorna indicando se o CPF fornecido é igual o CPF com os
		// digitos verificadores calculados
		return cpf.equals(cpf.substring(0, 9) + digito1.toString()
				+ digito2.toString());
	}

	/**
	 * Método privado usado na validação de cpf 
	 *
	 */
	private static int calcularDigitoVerificador(String str, int[] peso) {
		int soma = 0;
		for (int indice = str.length() - 1, digito; indice >= 0; indice--) {
			digito = Integer.parseInt(str.substring(indice, indice + 1));
			soma += digito * peso[peso.length - str.length() + indice];
		}
		soma = 11 - soma % 11;
		return soma > 9 ? 0 : soma;
	}
	
	/**
	 * Método para validar um cnpj.
	 * O método verifica se o cnpj desejado é valido
	 *
	 * @param cnpj
	 *			cnpj que deseja-se validar
	 * @param isFormatted
	 *			valor que indica se o cnpj é formatado. true para cnpj formatado; false para cnpj não formatado
	 *
	 *
	 * @return true para cnpj válidao; false para cnpj inválido
	 */
	public static boolean eValidoCNPJ(String cnpj, boolean isFormatted) {
		return new CNPJValidator(isFormatted).invalidMessagesFor(cnpj)
				.isEmpty();
	}
	
	/**
	 * 
	 * Método para validar um cnpj.
	 * O método verifica se o cnpj desejado é valido
	 *
	 * @param cnpj
	 *			cnpj que deseja-se validar
	 *
	 * @return true para cnpj válidao; false para cnpj inválido
	 */
	
	public static boolean eValidoCNPJ(String cnpj) {
		if (cnpj == null) {
			return false;
		}
		// remove "." e "-"
		cnpj = Helper.desformatarCpfCnpj(cnpj);
		if (cnpj.length() != 14) {
			return false;
		}
		// passo 1 - calcula somente para a string sem o digito verificador
		Integer digito1 = calcularDigitoVerificador(cnpj.substring(0, 12),
				pesosCNPJ);
		// passo 2 - calculo novamente com o dígito obtido no passo 1
		Integer digito2 = calcularDigitoVerificador(cnpj.substring(0, 12)
				+ digito1, pesosCNPJ);
		return cnpj.equals(cnpj.substring(0, 12) + digito1.toString()
				+ digito2.toString());
	}
	
	/**
	 * Verifica o tipo do documento
	 * @return CPF ou CNPJ
	 */
	public static Identificacao isCpfCnpj (String cpfCnpj) {
		
		if (Helper.eValidoCPF(cpfCnpj)) {
			return Identificacao.CPF;
		} else if (Helper.eValidoCNPJ(cpfCnpj)) {
			return Identificacao.CNPJ;
		} else {
			return null;
		}
		
	}
		
	//=====================================================================================
	
	/**
	 * Método para desformatar o código de protocolo do CAR. 
	 * O método irá validar o código do Car e retirar os pontos (.)
	 *
	 * @param codigoCar 
	 *			o código que deseja-se desformatar
	 *
	 * @return o codigoDesformatado
	 */
	public static String desformatarCodigoCarProtocolo(String codigoCar) {

		if(StringUtils.isBlank(codigoCar))
			throw new IllegalArgumentException("Número do car inválido.") ;

		codigoCar =  codigoCar.replace(".", "");

		return  codigoCar;

	}
	
	/**
	 * Método para formatar cep.
	 * O método irá válidar o cep recebido como parametro 
	 * e colocar a máscara de cep
	 *
	 * @param valor
	 *			o valor do cep que deseja-se formatar
	 * 
	 * @return o cep formatado com sua máscara
	 */
	public static String formatarCep(String valor) {
		// Se a string for vazia
		if (StringUtils.isBlank(valor)) {
			return "";
		}

		// Se possuir tamanho inválido, retorna a própria string
		if (valor.length() != 8) {
			return valor;
		}

		return valor.substring(0, 5) + "-" + valor.substring(5);
	}

	/**
	 * Método para formatar número decimal.
	 * O método irá formatar um número decimal para 
	 * imprimir um número de casas decimais limitado
	 *
	 * @param number
	 *			o número decimal que deseja-se formatar
	 *
	 * @param fraction
	 *			o número de casas decimais que deseja-se imprimir do número
	 * 
	 * @return número formatado com a quantidade de casas decimais desejadas
	 *
	 * @return Caso lance exceção, retorna null
	 */
	public static String formatBrDecimal(Object number, Integer fraction){

		try {

			number = Double.parseDouble(number.toString());

			NumberFormat numberText = NumberFormat.getNumberInstance(new Locale("pt", "BR"));

			numberText.setRoundingMode(RoundingMode.DOWN);

			numberText.setMinimumFractionDigits(fraction);
			numberText.setMaximumFractionDigits(fraction);

			return numberText.format(number);

		}catch (Exception e){

			e.printStackTrace();

			return null;

		}
	}


	/**
	 * Método para formatar um número decimal com 4 casas decimais.,
	 *
	 * @param number
	 *			número que deseja-se formatar
	 * 
	 * @return O número formatado com 4 casas decimais
	 */
	public static String formatBrDecimal(Object number){

		return  formatBrDecimal(number, 4);

	}


	/**
	 * Método para formatar a data.
	 * Irá formatar a data de acordo com o 
	 * formato desejado na chamada do método
	 *
	 * @param data
	 * 			data que deseja-se formatar
	 * 
	 * @param formato
	 *			o formato no qual deseja-se formatar a data
	 * 
	 * @return a data formatada de acordo com o formato desejado
	 */
	public static String formatarData(Date data, String formato) {
		
		if(data == null){
			return "";
		}

		SimpleDateFormat sdf = new SimpleDateFormat(formato);

		return sdf.format(data);

	}

	/**
	 * Método para formatar a data no formato definido no application.conf
	 *
	 * @param date
	 *			data que deseja-se formatar 
	 * 
	 * @return a data formatada de acordo com o formato do application.conf
	 */
	public static String formatSimpleDate(Date date) {

		return formatarData(date, (String) Play.configuration.get("date.format"));

	}
	
	/**
	 * Método de retorno do status do CAR. 
	 * O método irá verificar qual a sigla do status do CAR
	 * e retornar o nome do status correspondente
	 *
	 * @param abreviacaoStatus
	 *			a abreviacao do status que deseja-se ter o nome por extenso
	 * 
	 * @return o nome por extenso da abreviação desejada
	 */
	public static String getNomeStatusCAR(String abreviacaoStatus) {
		
		String nomeStatus = "";
		
		switch (abreviacaoStatus) {
			case "AT":
				nomeStatus = "Ativo";
				break;
				
			case "CA":
				nomeStatus = "Cancelado";
				break;
			
			case "PE":
				nomeStatus = "Pendente";
				break;
	
		}

		return nomeStatus;
	}
	
	/**
	 * Método de retorno do nome do tipo do imóvel. 
	 * O método irá verificar qual a sigla do tipo de imovel
	 * e retornar o nome correspondente à sigla
	 *
	 * @param codigoTipoImovel
	 *			o codigo do tipo do imóvel que deseja-se ter o nome por extenso
	 * 
	 * @return o nome por extenso da sigla desejada 
	 */	
	public static String getNomeTipoImovel(String codigoTipoImovel) {
		
		String descricaoTipoImovel = "";
		
		switch (codigoTipoImovel) {
			
			case "IRU":
				descricaoTipoImovel = "Imóvel Rural";
				break;

			case "PCT":
				descricaoTipoImovel = "Imóvel Rural de Povos e Comunidades Tradicionais";
				break;
			
			case "AST":
				descricaoTipoImovel = "Imóvel Rural de Assentamentos da Reforma Agrária";
				break;
		}
		
		return descricaoTipoImovel;
	}

	/**
	 * Método para remover os acentos da string desejada
	 *
	 * @param str
	 *			string que deseja-se remover os acentos
	 * 
	 * @return String sem acentos
	 */
	public static String removerAcentos(String str) {
		return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}

	
	/**
	 * Método para compactar arquivos em um zip.
	 * O método irá compactar todos os arquivos de um diretório
	 * passado por parametro em um arquivo zip. Com a opção
	 * de zipar ou não um possível arquivo que contenha o mesmo
	 * nome do arquivo zip que está sendo criado
	 *
	 * @param zipFile
	 *			o nome que será dado ao arquivo zip criado
	 *
	 * @param sourceDirectory
	 *			a rota da pasta que deseja-se compactar em um zip
	 * @param importFileWithSameName
	 *			se vai importar um possível arquivo que tenha o mesmo nome
	 *			do arquivo zip que está sendo criado
	 */	
	public static void compactarParaZip(File zipFile, String sourceDirectory, boolean importFileWithSameName) {
		
		try	{			
			//create byte buffer
			byte[] buffer = new byte[1024];

			//create object of FileOutputStream
			FileOutputStream fout = new FileOutputStream(zipFile);

			//create object of ZipOutputStream from FileOutputStream
			ZipOutputStream zout = new ZipOutputStream(fout);

			//create File object from directory name
			File dir = new File(sourceDirectory);

			//check to see if this directory exists
			if(!dir.isDirectory()) {
				System.out.println(sourceDirectory + " is not a directory");
			}else{
				File[] files = dir.listFiles();

				for(int i=0; i < files.length ; i++) {
					
					if (!importFileWithSameName && zipFile.getName().equals(files[i].getName())) {
						
						continue;
					}
					
					//create object of FileInputStream for source file
					FileInputStream fin = new FileInputStream(files[i]);

					/*
					 * To begin writing ZipEntry in the zip file, use
					 *
					 * void putNextEntry(ZipEntry entry)
					 * method of ZipOutputStream class.
					 *
					 * This method begins writing a new Zip entry to
					 * the zip file and positions the stream to the start
					 * of the entry data.
					 */
					zout.putNextEntry(new ZipEntry(files[i].getName()));

					/*
					 * After creating entry in the zip file, actually
					 * write the file.
					 */
					int length;

					while((length = fin.read(buffer)) > 0) {
						zout.write(buffer, 0, length);
					}

					/*
					 * After writing the file to ZipOutputStream, use
					 *
					 * void closeEntry() method of ZipOutputStream class to
					 * close the current entry and position the stream to
					 * write the next entry.
					 */
					zout.closeEntry();

					//close the InputStream
					fin.close();
				}
			}

			//close the ZipOutputStream
			zout.close();


		}
		catch(IOException ioe) {
			System.out.println("IOException :" + ioe);
		}		
	}

	/**
	 * Método para compactar arquivos em um zip.
	 * O método irá compactar todos os arquivos de um diretório
	 * passado por parametro em um arquivo zip 
	 *
	 * @param zipFile
	 *			o nome que será dado ao arquivo zip criado
	 *
	 * @param sourceDirectory
	 *			a rota da pasta que deseja-se compactar em um zip
	 */
	public static void compactarParaZip(String zipFile, String sourceDirectory){
		
		compactarParaZip(new File(zipFile), sourceDirectory, true);
	}
	

	/**
	 * Método que retorna a diferença entre datas em milissegundos
	 * O método calcula a diferença entre duas datas em milissegundos
	 *
	 * @param date1
	 *			data inicial que deseja calcular a diferença
	 * @param date2
	 *			data final que deseja calcular a diferença
	 * @param timeUnit
	 *			formato em que as datas se encontram.
	 *			TimeUnit.NANOSECONDS, TimeUnit.MICROSECONDS, TimeUnit.MILLISECONDS,
	 *			TimeUnit.SECONDS, TimeUnit.MINUTES, TimeUnit.HOURS, TimeUnit.DAYS;
	 * @return
	 * 			retorna a diferença entre as datas passadas em milissegundos
	 */
	public static Long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
		long diffInMillies = date2.getTime() - date1.getTime();
		return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}

	/**
	 * Método que converte data e hora do tipo String para o tipo Date
	 * O método faz a conversão de data e hora do tipo string para data do tipo Date
	 *
	 * @param dataString
	 * 			data do tipo string no formato ("yyyy/MM/dd")
	 * @param horaString
	 * 			hora do tipo string no formato ("00:00:00")
	 * @return
	 * 			retorn a data do tipo Date
	 */
	public static Date toDate(String dataString, String horaString) {

		String[] data = dataString.split("/");
		String[] hora = horaString.split(":");

		GregorianCalendar dateGC = new GregorianCalendar(
				Integer.parseInt(data[2]),
				Integer.parseInt(data[1])-1,
				Integer.parseInt(data[0]),
				Integer.parseInt(hora[0]),
				Integer.parseInt(hora[1]),
				Integer.parseInt(hora[2]));

		return dateGC.getTime();
	}
	
	public static String getNomeMes(String mes) {
		
		return getNomeMes(Integer.parseInt(mes)-1);
	}
	
	
	
	public static String getNomeMes(Integer mes) {

		String[] nomesMes = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};

		return nomesMes[mes];
	}
	
	public static String getMesAno(Date data) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);

		String mes = getNomeMes(calendar.get(Calendar.MONTH));
		String ano = String.valueOf(calendar.get(Calendar.YEAR));

		return mes + "/" + ano;
	}

	public static String getDataPorExtenso(Date data) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);

		String dia =  String.valueOf(calendar.get(Calendar.DATE));
		String mes = getNomeMes(calendar.get(Calendar.MONTH));
		String ano = String.valueOf(calendar.get(Calendar.YEAR));

		return dia + " de " + mes + " de " + ano;
	}
	public static String getAno(Date data) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);

		return String.valueOf(calendar.get(Calendar.YEAR));

	}

	/**
	 * Método que remove a hora da data
	 * O método remove a hora da data passada como parâmetro
	 *
	 * @param dataComHora
	 * 			data do tipo date com a hora
	 * @return
	 * 			retorna a data sem hora do tipo Date
	 */	
	public static Date tirarHoraData(Date dataComHora) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dataComHora);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new Date(cal.getTimeInMillis());
	}

	/**
	 * Método que insere 23:59:59 na hora da data
	 * O método altera a hora da data passada como parâmetro
	 *
	 * @param dataComHora
	 * 			data do tipo date com a hora
	 * @return
	 * 			retorna a data sem hora do tipo Date
	 */
	public static Date colocarDataFinalDoDia(Date dataComHora) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dataComHora);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return new Date(cal.getTimeInMillis());
	}
	
	/**
	 * Método que adiciona um dia na data
	 * O método adiciona um dia na data passada no parâmetro
	 *
	 * @param dataComHora
	 * 			data do tipo date com a hora
	 * @return
	 * 			retorna a data do tipo Date
	 */
	public static Date somarUmDiaNaDataComHora(Date dataComHora) {
		Calendar c = Calendar.getInstance();
		c.setTime(dataComHora); 
		c.add(Calendar.DATE, 1);
		return new Date(c.getTimeInMillis());
	}
	
	/**
	 * Método que adiciona dias na data
	 * O método adiciona a quantidade de dias passada no parâmetro na data passada no parâmetro
	 *
	 * @param date
	 * 			data do tipo date
	 * @param dias
	 * 			quantidade de dias do tipo int a serem somados
	 * @return
	 * 			retorna a data do tipo Date
	 */	
	public static Date somarDias(Date date, int dias) {

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, dias);
		return new Date(c.getTimeInMillis());
	}

	/**
	 * Método que adiciona um dia na data
	 * O método adiciona um dia na data passada no parâmetro
	 *
	 * @param data
	 * 			data do tipo date
	 * @return
	 * 			retorna a data do tipo Date
	 */
	public static Date somarUmDia(Date data) {
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		c.add(Calendar.DATE, 1);
		return c.getTime();

	}

	/**
	 * Método que adiciona milisegundos na data
	 * O método adiciona a quantidade de milisegundos passado no parâmetro na data passada no parâmetro
	 *
	 * @param data
	 * 			data do tipo date
	 * @param milliseconds
	 * 			quantidade de milisegundos do tipo int a serem somados
	 * @return
	 * 			retorna a data do tipo Date
	 */
	public static Date somarMilissegundos(Date data, int milliseconds) {
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		c.add(Calendar.MILLISECOND, milliseconds);
		return c.getTime();

	}

	/**
	 * Método que subtrai milisegundos na data
	 * O método subtrai a quantidade de milisegundos passado no parâmetro na data passada no parâmetro
	 *
	 * @param data
	 * 			data do tipo date
	 * @param milliseconds
	 * 			quantidade de milisegundos do tipo int a serem subtraídos
	 * @return
	 * 			retorna a data do tipo Date
	 */
	public static Date subtrairMilissegundos(Date data, int milliseconds) {
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		c.add(Calendar.MILLISECOND, -milliseconds);
		return c.getTime();

	}

	/**
	 * Método que verifica se a data de hoje é posterior a data passada
	 * O método verifica se a data de hoje é posterior a data passada como parâmetro da função
	 *
	 * @param data
	 * 			data do tipo date
	 * @return
	 * 			retorna true se a data de hoje é posterior ou false se não for posterior
	 */
	public static boolean hojePosteriorA(Date data) throws NullPointerException {

		Date hoje = new Date();

		return hoje.after(data);
	}

	/**
	 * Método que remove um dia na data
	 * O método remove um dia na data passada no parâmetro
	 *
	 * @param dataComHora
	 * 			data do tipo date com a hora
	 * @return
	 * 			retorna a data do tipo Date
	 */
	public static Date removerUmDia(Date dataComHora) {
		Calendar c = Calendar.getInstance();
		c.setTime(dataComHora);
		c.add(Calendar.DATE, -1);
		return new Date(c.getTimeInMillis());
	}

	/**
	 * Método que remove horas na data
	 * O método remove a quantidade de horas passada no parâmetro na data passada no parâmetro
	 *
	 * @param data
	 * 			data do tipo date
	 * @param quantidadeHorasRemover
	 * 			quantidade de horas do tipo Integer a serem removidas
	 * @return
	 * 			retorna a data do tipo Date
	 */
	public static Date removeHoras(Date data, Integer quantidadeHorasRemover){
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		c.add(Calendar.HOUR, -quantidadeHorasRemover);
		return new Date(c.getTimeInMillis());
	}


	/**
	 * Método que adiciona minutos na data
	 * O método adiciona a quantidade de minutos passado no parâmetro na data passada no parâmetro
	 *
	 * @param data
	 * 			data do tipo Date
	 * @param minutosParaSomar
	 * 			quantidade de minutos do tipo Integer a serem somados
	 * @return
	 * 			retorna a data do tipo Date
	 */
	public static Date somaMinutos(Date data, Integer minutosParaSomar){
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		c.add(Calendar.MINUTE, minutosParaSomar);
		return new Date(c.getTimeInMillis());
	}

	/**
	 * Método que retorna a diferença entre datas em dias
	 * O método calcula a diferença entre duas datas em dias
	 *
	 * @param data
	 *			data final que deseja calcular a diferença
	 * @param data2
	 *			data inicial que deseja calcular a diferença
	 * @return
	 * 			retorna a diferença entre as datas passadas em dias
	 */
	public static Long getDiferencaDias(Date data, Date data2) {

		Long diffInMillies = data.getTime() - data2.getTime();

		return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

	}

	/**
	 * Método que retorna a diferença entre datas em horas
	 * O método calcula a diferença entre duas datas em horas
	 *
	 * @param data
	 *			data final que deseja calcular a diferença
	 * @param data2
	 *			data inicial que deseja calcular a diferença
	 * @return
	 * 			retorna a diferença entre as datas passadas em horas
	 */
	public static Long getDiferencaHoras(Date data, Date data2) {

		Long diffInMillies = data.getTime() - data2.getTime();

		return TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);

	}

	/**
	 * Método que retorna a diferença entre datas em segundos
	 * O método calcula a diferença entre duas datas em segundos
	 *
	 * @param d1
	 *			data inicial que deseja calcular a diferença
	 * @param d2
	 *			data final que deseja calcular a diferença
	 * @return
	 * 			retorna a diferença entre as datas passadas em segundos
	 */
	public static Long getDiferencaSegundos(Date d1, Date d2) {

		return (d2.getTime() - d1.getTime()) / 1000;
	}


	public static Boolean dataMaiorQue(Date data, Date dataComparacao) {

		if(data.compareTo(dataComparacao) == 1) {
			return true;
		}

		return false;

	}

	public static Boolean checkValuePositive(Integer value) {

		return value > 0 ;
	}
	
	public static String getStringOrHifen(String str){
		if(StringUtils.isNotEmpty(str)){
			return str;
		}
		return "-";
	}

	public static String capitalize(String texto) {

		String capitalize = texto.toLowerCase();

		return Character.toUpperCase(capitalize.charAt(0)) + capitalize.substring(1);

	}

	public static String removeLastCharacter(String texto) {

		String result = texto;

		return result.substring(0, result.length() - 1);

	}

	/**
	 * Método para fazer o cast de uma data no formato String para Date.
	 * @param dataString
	 *			data no formato dd/MM/yyyy
	 *
	 * @return a data no formato Date
	 */
	public static Date stringToDate(String dataString) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		Date data = sdf.parse(dataString);

		return data;

	}
}
