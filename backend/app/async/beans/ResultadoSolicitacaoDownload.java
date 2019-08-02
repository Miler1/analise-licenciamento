package async.beans;

import java.io.Serializable;

public class ResultadoSolicitacaoDownload implements Serializable {

	public boolean processado;
	public boolean agendado;
	public boolean erro;
	public String mensagem;
	public String urlBaixarArquivo;
	public String emailAgendamento;
	public String pathResultado;
	public Integer numeroRegistrosResultado;
}
