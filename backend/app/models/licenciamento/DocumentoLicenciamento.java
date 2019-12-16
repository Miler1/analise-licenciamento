package models.licenciamento;

import play.db.jpa.GenericModel;
import utils.Configuracoes;
import utils.Identificavel;

import javax.persistence.*;
import java.io.File;
import java.util.Date;

@Entity
@Table(schema = "licenciamento", name = "documento")
public class DocumentoLicenciamento extends GenericModel implements Identificavel {

	private static final String SEQ = "licenciamento.documento_id_seq";
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;
	
	public String caminho;
	
	@ManyToOne
	@JoinColumn(name="id_tipo_documento", referencedColumnName="id")
	public TipoDocumentoLicenciamento tipo;
	
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
	
	@Override
	public Long getId() {
		
		return this.id;
	}
	
	private String getCaminhoCompleto() {
		
		return Configuracoes.ARQUIVOS_DOCUMENTOS_LICENCIAMENTO_PATH + File.separator + this.caminho;
	}
	
	public File getFile() {
		
		return new File(getCaminhoCompleto());
	}	
	
	public String getNome() {
		
		if (this.caminho.isEmpty())
			return "";
		
		return this.caminho.substring(this.caminho.lastIndexOf("/") + 1);
	}

}
