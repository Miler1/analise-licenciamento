package models.manejoDigital;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;

public class Observacao  extends GenericModel {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="observacao_id_seq")
    @SequenceGenerator(name="observacao_id_seq", sequenceName="observacao_id_seq", allocationSize=1)
    public Long id;

    @Required
    @Column(name="descricao")
    public String descricao;

    @Required
    @Column(name="etapa")
    public Integer etapa;



}
