package models;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;

@Entity
@Table(schema="analise", name="origem_notificacao")
public class OrigemNotificacao extends GenericModel {

    @Id
    public Long id;

    @Required
    public String codigo;

    @Required
    public String descricao;
}
