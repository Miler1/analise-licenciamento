package models.manejoDigital;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Entity
@Table(schema = "analise", name = "base_vetorial")
public class BaseVetorial extends GenericModel {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="analise.base_vetorial_id_seq")
    @SequenceGenerator(name="analise.base_vetorial_id_seq", sequenceName="analise.base_vetorial_id_seq", allocationSize=1)
    public Long id;

    @Required
    @Column(name="nome")
    public String nome;

    @Required
    @Column(name="fonte")
    public String fonte;

    @Required
    @Column(name="ultima_atualizacao")
    public Date ultimaAtualizacao;

    @Required
    @Column(name="escala")
    public String escala;

    @Required
    @Column(name="observacao")
    public String observacao;

    public static List<BaseVetorial> gerarBaseVetorial(AnaliseManejo analise) {

        Random rand = new Random();
        int numeroRandomico = rand.nextInt(20) + 1;

        List<BaseVetorial> lista = new ArrayList<>();

        for(int i = 0; i < numeroRandomico; i++) {

            BaseVetorial baseVetorial = new BaseVetorial();

            baseVetorial.nome = UUID.randomUUID().toString();

            baseVetorial.fonte = UUID.randomUUID().toString();

            baseVetorial.ultimaAtualizacao = new Date();

            baseVetorial.escala = UUID.randomUUID().toString();

            baseVetorial.observacao = UUID.randomUUID().toString();

            lista.add(baseVetorial);
        }

        return lista;
    }
}
