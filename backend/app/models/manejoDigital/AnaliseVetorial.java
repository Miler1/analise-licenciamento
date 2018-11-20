package models.manejoDigital;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Entity
@Table(schema = "analise", name = "analise_vetorial")
public class AnaliseVetorial extends GenericModel {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="analise.analise_vetorial_id_seq")
    @SequenceGenerator(name="analise.analise_vetorial_id_seq", sequenceName="analise.analise_vetorial_id_seq", allocationSize=1)
    public Long id;

    @Required
    @Column(name="tipo")
    public String tipo;

    @Required
    @Column(name="nome")
    public String nome;

    @Required
    @Column(name="distancia_propriedade")
    public Double distanciaPropriedade;

    @Required
    @Column(name="sobreposicao_propriedade")
    public Double sobreposicaoPropriedade;

    @Required
    @Column(name="distancia_amf")
    public Double distanciaAmf;

    @Required
    @Column(name="sobreposicao_amf")
    public Double sobreposicaoAmf;

    @Required
    @Column(name="observacao")
    public String observacao;

    @Required
    @ManyToOne
    @JoinColumn(name="id_analise_tecnica_manejo")
    public AnaliseTecnicaManejo analiseTecnicaManejo;

    public static List<AnaliseVetorial> gerarAnalisesVetoriais(AnaliseTecnicaManejo analise) {

        Random rand = new Random();
        int numeroRandomico = rand.nextInt(20) + 1;

        List<AnaliseVetorial> listaAnalise = new ArrayList<>();

        for(int i = 0; i < numeroRandomico; i++) {

            AnaliseVetorial analiseVetorial = new AnaliseVetorial();

            analiseVetorial.tipo = UUID.randomUUID().toString().replace('-', ' ');

            analiseVetorial.nome = UUID.randomUUID().toString().replace('-', ' ');

            analiseVetorial.distanciaPropriedade = Math.random();

            analiseVetorial.sobreposicaoPropriedade = Math.random();

            analiseVetorial.distanciaAmf = Math.random();

            analiseVetorial.sobreposicaoAmf = Math.random();

            analiseVetorial.observacao = UUID.randomUUID().toString().replace('-', ' ');

            analiseVetorial.analiseTecnicaManejo = analise;

            listaAnalise.add(analiseVetorial);
        }

        return listaAnalise;
    }
}
