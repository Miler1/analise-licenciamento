package async.beans;

import java.io.Serializable;

public class ResultadoValidacaoCadastroPublicacaoAreas implements Serializable {

    public enum Status {
        SUCESSO,
        ERRO
    }

    public Status status;
    public String mensagem;
}
