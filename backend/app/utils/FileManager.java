package utils;

import exceptions.AppException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import play.Logger;
import play.libs.Files;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class FileManager {

    private static final String FILE_KEY_SEPARATOR = "_";
    private static final String TEMP_FILES_FOLDER_PATH = Configuracoes.APPLICATION_TEMP_FOLDER;
    private static final long TAMANHO_MAX_ARQUIVO = Configuracoes.TAMANHO_MAXIMO_ARQUIVO;

    private static FileManager instance;

    public static FileManager getInstance() {

        if (instance == null) {
            instance = new FileManager();
        }

        return instance;
    }

    private FileManager() {

        if (TEMP_FILES_FOLDER_PATH == null || TEMP_FILES_FOLDER_PATH.trim().equals("")) {

            throw new IllegalStateException("Erro ao inicializar o FileManager. A pasta temporária não foi especificada");
        }

        File tempFileFolder = new File(TEMP_FILES_FOLDER_PATH);

        if (!tempFileFolder.exists()) {
            throw new IllegalStateException("Erro ao inicializar o FileManager. A pasta temporária não foi encontrada:  " + TEMP_FILES_FOLDER_PATH);
        }

        if (!tempFileFolder.isDirectory()) {
            throw new IllegalStateException("Erro ao inicializar o FileManager. A pasta temporária não é um diretório: " + TEMP_FILES_FOLDER_PATH);
        }

        if (!tempFileFolder.canWrite()) {
            throw new IllegalStateException("Erro ao inicializar o FileManager. Sem permissão de escrita para a pasta temporária: " + TEMP_FILES_FOLDER_PATH);
        }

    }

    public File getTempFilesFolder() {

    	return new File(TEMP_FILES_FOLDER_PATH);
    }

    public String createFile(String pathFileGenerated, String name, byte [] fileBytes, String extension) throws IOException {

        String fileName = generateFileName(name, extension);

        File file = new File(pathFileGenerated, fileName);

        writeFile(file, fileBytes);

        return fileName;
    }

    public String createFile(byte [] fileBytes, String extension) throws IOException {

        String fileName = generateFileName(extension);
        File file = new File(TEMP_FILES_FOLDER_PATH, fileName);

        writeFile(file, fileBytes);

        return file.getAbsolutePath();

    }

    public String createKey(byte [] fileBytes, String nomeDoArquivo) throws IOException {

	    String key = generateKey();

	    File diretorio = new File(TEMP_FILES_FOLDER_PATH + key);

	    if(!diretorio.exists()) {

	    	diretorio.mkdir();

	    }

	    File file = new File(TEMP_FILES_FOLDER_PATH + key, nomeDoArquivo);

	    writeFile(file, fileBytes);

	    return key;

    }

    private void writeFile(File file, byte[] fileBytes) throws IOException {

    	BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
    	stream.write(fileBytes);
    	stream.close();
    }

    public File createFile(String filename) {

    	return new File(TEMP_FILES_FOLDER_PATH + filename);
    }

    public String removerExtension(String fileName) {

    	return fileName.substring(0, fileName.lastIndexOf("."));
    }

    public File getFile(String fileKey) {

    	return getFile(fileKey, TEMP_FILES_FOLDER_PATH);

    }

    public File getFile(String fileKey, String diretorio) {

        if (fileKey == null || diretorio == null) {
            return null;
        }

	    Path path = Paths.get(diretorio + fileKey + "/");

        if(!path.toFile().exists() || !path.toFile().isDirectory()) {

        	return null;

        }

        return Objects.requireNonNull(path.toFile().listFiles())[0];

    }

	public File getFile(String fileKey, String folder, String extension) {

		if (fileKey == null || folder == null) {
			return null;
		}

		File file = new File (folder, fileKey + '.' + extension);

		if (file.exists()) {
			return file;
		}

		return null;
	}

	public byte [] getFileBytes(String fileKey) {

      try {

    	String diretorio = Configuracoes.APPLICATION_TEMP_FOLDER;

        File file = getFile(fileKey,diretorio);

        if (file != null) {
            return getFileBytes(file);
        } else {
            return null;
        }

        } catch (IOException ex) {
          throw new RuntimeException (ex);
        }
    }

    public void deleteFile(String fileKey) {

    	String diretorio = Configuracoes.APPLICATION_TEMP_FOLDER;

        File file = getFile(fileKey,diretorio);

        if (file != null && file.exists()) {
            file.delete();
        }
    }

	public void deleteFile(String path, String token) {

		File file = new File (path + token);

		if (file != null && file.exists()) {

			file.delete();
		}
	}

	public void deleteFileFromPath(String path) {

		File file = getFile(path);

		if (file != null && file.exists()) {
			file.delete();
		}
	}

    public Map<String,File> createEmptyFiles(int numberOfFiles, String extension) throws IOException {

        if (numberOfFiles > 0) {

            String folderName = getFolderName();

            Map<String, File> files = new HashMap<String, File>();

            for (int i = 0; i < numberOfFiles; i++) {

                String fileName = generateFileName(extension);
                File file = new File(TEMP_FILES_FOLDER_PATH + folderName, fileName);

                files.put(generateFileKey(fileName, folderName), file);
            }

            return files;

        }

        return null;
    }

    public String generateFileKey(String fileName, String folderName) {
        return folderName + fileName;
    }

    public String getFileNameByKey(String key) {

        try {
            return key.split(TEMP_FILES_FOLDER_PATH)[1];

        } catch (Exception e) {
            return null;
        }
    }

    private String getFolderNameByKey(String key, String diretorio) {

        try {
            return diretorio + key.substring(0, key.indexOf(FILE_KEY_SEPARATOR));
        } catch (Exception e) {
            return null;
        }
    }

    public File getFolderByKey(String key) {

        try {
            return new File(TEMP_FILES_FOLDER_PATH + key.substring(0, key.indexOf(FILE_KEY_SEPARATOR)));
        } catch (Exception e) {
            return null;
        }
    }

    public String generateFileName(String extension) {

    	return UUID.randomUUID().toString() + ((extension != null) ? "." + extension : "");
    }

	public String generateKey() {

		return UUID.randomUUID().toString();

	}

	public String generateFileName(String name, String extension) {

    	name = name.substring(0, name.lastIndexOf("."));
		return name + '_' + UUID.randomUUID().toString() + ((extension != null) ? "." + extension : "");
	}

    public String getFolderName() throws IOException {

        File folder = new File(TEMP_FILES_FOLDER_PATH);
        this.createFolderIfNotExists(folder);

        return folder.getName();
    }

    private void createFolderIfNotExists(File folder) throws IOException {

        if (!folder.exists() && !folder.mkdir()) {
            throw new IOException("Não foi possível criar a pasta temporária.");
        }
    }

    public void cleanUpTemporaryFiles(String folderPath, long prazo) throws IOException {

        File tempFolder = new File(folderPath);

        Logger.info("Apagando arquivos da pasta : " + folderPath);
        if (tempFolder != null && tempFolder.exists() && tempFolder.isDirectory()) {

        	Date current = new Date();
        	Date lastModified = null;

            for (File file : tempFolder.listFiles()) {

            	lastModified = new Date(file.lastModified());
            	long diffInDays = ((current.getTime() - lastModified.getTime()) / (1000 * 60 * 60 * 24));

            	if (diffInDays >= prazo) {

	                if (file.isDirectory()) {
	                    Files.deleteDirectory(file);
	                } else {
	                	Files.delete(file);
	                }
            	}
            }
        }
    }

    public void copyFile(File origem, File destino) throws IOException  {

        if (!origem.exists()) {
            throw new FileNotFoundException("Arquivo de origem não existe: " + origem);
        }

        InputStream in = new FileInputStream(origem);

        OutputStream out = new FileOutputStream(destino);

        byte[] buf = new byte[1024];
        int len;

        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }

        in.close();
        out.close();
    }

    public byte [] getFileBytes(File file) throws IOException {

        ByteArrayOutputStream ous = null;
        InputStream ios = null;

        try {

            byte [] buffer = new byte[4096];
            ous = new ByteArrayOutputStream();
            ios = new FileInputStream(file);
            int read = 0;

            while ( (read = ios.read(buffer)) != -1 ) {
                ous.write(buffer, 0, read);
            }

        } finally {

            try {

                if (ous != null) {
                  ous.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {

                if (ios != null) {
                  ios.close();
                }
            } catch ( IOException e) {
                e.printStackTrace();
            }
        }

        return ous.toByteArray();
    }

    public String getFileExtention(String completeName) {

    	int dotIndex = completeName.lastIndexOf(".");

    	if (dotIndex > 0) {

    		return completeName.substring(dotIndex + 1);

    	} else {

    		return "";
    	}
    }

    public boolean validateExtention(String completeName, String ... validFormats) {

    	String extention = getFileExtention(completeName);

    	if (extention != null && !extention.isEmpty()) {

    		for (String validFormat : validFormats) {

	    		if (extention.equalsIgnoreCase(validFormat))
	    			return true;
	    	}
    	}

    	return false;
    }

    public InputStream byteArrayToInputStream(byte[] dados){

    	if (dados == null)
    		throw new AppException(Mensagem.ERRO_PADRAO);

    	InputStream is = new ByteArrayInputStream(dados);

    	return is;
    }

	public static File moveFileToDir(String pathArquivo, String diretorioMover) throws IOException {

		File filePath = new File(pathArquivo);

		String nomeArquivo = Calendar.getInstance().getTimeInMillis() + "." + FilenameUtils.getExtension(filePath.getPath());

		File diretorioFinal = new File(diretorioMover);

		if(!diretorioFinal.exists())
			diretorioFinal.mkdirs();

		File fileFinal = new File(diretorioFinal.getPath() + File.separator + nomeArquivo);

		FileUtils.moveFile(filePath, fileFinal);

		return fileFinal;

	}

	public static boolean validaArquivo(File file){
		ArrayList<String> extensoes = new ArrayList<>();
		extensoes.add("png");
		extensoes.add("jpg");
		extensoes.add("jpeg");
		extensoes.add("pdf");

		if(file.length() < TAMANHO_MAX_ARQUIVO && extensoes.contains(FilenameUtils.getExtension(file.getPath()))){
			return true;
		}
		return false;
	}

}
