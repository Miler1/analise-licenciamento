package models;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import play.db.jpa.GenericModel;
import utils.Configuracoes;
import utils.FileManager;
import utils.Identificavel;

@Entity
@Table(schema = "licenciamento", name = "documento")
public class Documento extends GenericModel implements Identificavel {

	private static final String SEQ = "licenciamento.documento_id_seq";
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;
	
	public String caminho;
	
	@ManyToOne
	@JoinColumn(name="id_tipo_documento", referencedColumnName="id")
	public TipoDocumento tipo;
	
	@Transient
	public String key;
	
	@Transient
	public String base64;
	
	@Transient
	public File arquivo;
	
	@Transient
	public String extensao;
	
	@Column(name="data_cadastro")
	public Date dataCadastro;
	
	
	public Documento() {
		
	}
	
	
	public Documento(TipoDocumento tipo, File arquivo) {
		
		this.tipo = tipo;
		this.arquivo = arquivo;
	}
	
	public String getNome() {
		
		if (this.caminho.isEmpty())
			return "";
		
		return this.caminho.substring(this.caminho.lastIndexOf("/") + 1);
	}	
	
	@Override
	public Documento save() {
		
		if (this.id != null) // evitando sobrescrever algum documento.
			throw new IllegalStateException("Documento já salvo");
		
		this.caminho = "temp";
		
		this.dataCadastro = new Date();
		
		super.save();
		
		if (this.tipo == null)
			throw new IllegalStateException("Tipo do documento não preenchido.");
		
		criarPasta();
		
		if (this.key != null) {
			
			saveArquivoTemp();
			
		} else if (this.arquivo != null) {
			
			saveArquivo(this.arquivo);
			
		} else if (this.base64 != null) {
			
			saveArquivoBase64();
		
		} else {
			
			throw new RuntimeException("Não é possível identificar o arquivo a ser salvo para o documento.");
		}
		
		super.save();
		
		return this;
	}

	private void saveArquivoTemp() {
		
		if (this.key == null)
			throw new IllegalStateException("Key do documento não preenchido.");
		
		saveArquivo(FileManager.getInstance().getFile(this.key));
	}

	private void saveArquivo(File file) {
		
		if (file == null || !file.exists())
			throw new IllegalStateException("Arquivo não existente.");
		
		try {
			FileManager fm = FileManager.getInstance();
			this.key = null;
			this.arquivo = null;
			this.extensao = fm.getFileExtention(file.getName());
			
			configurarCaminho();
					
			FileUtils.copyFile(file, new File(this.getCaminhoCompleto()));

		} catch (IOException e) {
			
			throw new RuntimeException(e);
		}
	}

	private void saveArquivoBase64() {
		
		if (this.base64 == null)
			throw new IllegalStateException("Base64 do documento não preenchido.");
		
		configurarCaminho();
		
		FileOutputStream fos;
		
		try {
			
			File file = new File(this.getCaminhoCompleto());
			
			if (!file.exists())
				file.createNewFile();
			
			fos = new FileOutputStream(file);
			fos.write(Base64.decodeBase64(this.base64));
			fos.close();
			
		} catch (IOException e) {
			
			throw new RuntimeException(e);
		}
	}
	
	private void configurarCaminho() {
		
		this.caminho = File.separator + tipo.caminhoPasta
				+ File.separator + tipo.prefixoNomeArquivo + "_"
				+ this.id;
		
		if (this.extensao != null)
			this.caminho += "." + this.extensao;
	}
	
	private void criarPasta() {
		
		String caminho = Configuracoes.ARQUIVOS_DOCUMENTOS_PATH + File.separator + tipo.caminhoPasta;
		
		File pasta = new File(caminho);
		
		if (!pasta.exists())
			pasta.mkdirs();
	}
	
	
	private String getCaminhoCompleto() {
		
		return Configuracoes.ARQUIVOS_DOCUMENTOS_PATH + File.separator + this.caminho;
	}
	
	public File getFile() {
		
		return new File(getCaminhoCompleto());
	}

	public File getFileByKey(){
		
		if (this.key == null)
			throw new IllegalStateException("key do documento não preenchidos.");
		
		return FileManager.getInstance().getFile(this.key);
	}

	@Override
	public Documento delete() {
		
		super.delete();
		getFile().delete();
		return this;
	}


	@Override
	public Long getId() {
		
		return this.id;
	}
	
	
}
