package models.licenciamento;

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
	
	@Override
	public Long getId() {
		
		return this.id;
	}
	
	
}
