package async.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResultadoProcessamentoShapeFile implements Serializable {

	public enum Status {
		SUCESSO,
		ERRO
	}

	public Status status;
	public List<String> mensagens;
	public Dados dados;

	public ResultadoProcessamentoShapeFile() {

		this.dados = new Dados();
	}

	public class Dados {

		public List<AtributoShape> atributos;
		public List<List<AtributoShape>> registros;
		public String keyTemp;

		public Dados() {

			this.atributos = new ArrayList<AtributoShape>();
			this.registros = new ArrayList<List<AtributoShape>>();
		}
	}
}
