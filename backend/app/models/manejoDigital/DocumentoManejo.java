package models.manejoDigital;

import models.Documento;
import org.apache.commons.io.FileUtils;
import play.data.validation.Required;
import utils.FileManager;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@Entity
@Table(schema = "analise", name = "documento_manejo")
@PrimaryKeyJoinColumn(name = "id_documento", referencedColumnName = "id")
public class DocumentoManejo extends Documento {

	@Required
	@ManyToOne
	@JoinColumn(name = "id_analise_tecnica_manejo")
	public AnaliseTecnicaManejo analiseTecnicaManejo;

	@Required
	@Column
	public String nome;

	@Override
	public Documento save() {

		if (this.id != null) // evitando sobrescrever algum documento.
			throw new IllegalStateException("Documento já salvo");

		this.caminho = "temp";

		this.dataCadastro = new Date();

		this._save();

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

		this._save();

		return this;
	}

	@Override
	protected void saveArquivo(File file) {

		if (file == null || !file.exists())
			throw new IllegalStateException("Arquivo não existente.");

		try {
			FileManager fm = FileManager.getInstance();
			this.key = null;
			this.arquivo = null;
			this.extensao = fm.getFileExtention(file.getName());
			this.nome = file.getName();

			configurarCaminho();

			FileUtils.copyFile(file, new File(this.getCaminhoCompleto()));

		} catch (IOException e) {

			throw new RuntimeException(e);
		}
	}

	@Override
	protected void configurarCaminho() {

		this.nome = this.nome.substring(0, this.nome.lastIndexOf('.')) + "_" + this.id;

		this.caminho = File.separator + tipo.caminhoPasta
				+ File.separator + this.nome;

		if (this.extensao != null) {

			this.caminho += "." + this.extensao;
			this.nome += "." + this.extensao;
		}
	}
}