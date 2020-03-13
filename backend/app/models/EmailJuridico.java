package models;

import java.util.List;

public abstract class EmailJuridico {

    protected List<String> emailsDestinatarios;

    public EmailJuridico(List<String> emailsDestinatarios) {
        this.emailsDestinatarios = emailsDestinatarios;
    }

    abstract void enviar();


}