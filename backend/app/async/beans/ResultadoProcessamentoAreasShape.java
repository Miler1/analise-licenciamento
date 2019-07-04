package async.beans;

//import models.desmatamento.LoteArea;

import java.io.Serializable;

public class ResultadoProcessamentoAreasShape implements Serializable {

    public enum Status {
        SUCESSO,
        ERRO
    }

    public Status status;
    public String mensagem;
//    public LoteArea loteArea;
}
