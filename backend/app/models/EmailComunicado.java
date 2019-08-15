package models;

import java.util.List;

public abstract class EmailComunicado {

    protected List<String> emailsDestinatarios;

    public EmailComunicado(List<String> emailsDestinatarios) {
        this.emailsDestinatarios = emailsDestinatarios;
    }

    abstract void enviar();


}
