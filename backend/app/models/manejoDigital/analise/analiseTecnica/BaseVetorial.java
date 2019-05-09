package models.manejoDigital.analise.analiseTecnica;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.*;

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

    @Column(name="observacao")
    public String observacao;

    @Column(name = "exibir_pdf")
    public boolean exibirPDF;

    public static List<BaseVetorial> gerarBaseVetorial(AnaliseTecnicaManejo analise) {

        Random rand = new Random();
        int numeroRandomico = rand.nextInt(20) + 1;

        List<BaseVetorial> lista = new ArrayList<>();

        for(int i = 0; i < numeroRandomico; i++) {

            BaseVetorial baseVetorial = new BaseVetorial();

            baseVetorial.nome = UUID.randomUUID().toString().replace('-', ' ');

            baseVetorial.fonte = UUID.randomUUID().toString().replace('-', ' ');

            baseVetorial.ultimaAtualizacao = new Date();

            baseVetorial.observacao = UUID.randomUUID().toString().replace('-', ' ');

            baseVetorial.exibirPDF = true;

            lista.add(baseVetorial);
        }

        return lista;
    }
}
