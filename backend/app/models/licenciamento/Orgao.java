package models.licenciamento;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;

@Entity
@Table(schema="licenciamento", name="orgao")
public class Orgao  extends GenericModel {

    private static final String SEQ = "licenciamento.orgao_id_seq";

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator=SEQ)
    @SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
    public Long id;

    @Required
    @JoinColumn(name="sigla")
    public String sigla;

    @Required
    @JoinColumn(name="nome")
    public String nome;

    @Required
    @JoinColumn(name="email")
    public String email;
}
