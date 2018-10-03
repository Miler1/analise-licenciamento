package models.manejoDigital;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

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

            byte[] array = new byte[7]; // length is bounded by 7
            new Random().nextBytes(array);
            baseVetorial.nome = new String(array, Charset.forName("UTF-8"));

            new Random().nextBytes(array);
            baseVetorial.fonte = new String(array, Charset.forName("UTF-8"));

            baseVetorial.ultimaAtualizacao = new Date();

            new Random().nextBytes(array);
            baseVetorial.escala = new String(array, Charset.forName("UTF-8"));

            new Random().nextBytes(array);
            baseVetorial.observacao = new String(array, Charset.forName("UTF-8"));

            lista.add(baseVetorial);
        }

        return lista;
    }
}
