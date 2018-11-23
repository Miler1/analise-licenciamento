package models.manejoDigital;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Entity
@Table(schema = "analise", name = "analise_ndfi")
public class AnaliseNdfi extends GenericModel {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="analise.analise_ndfi_id_seq")
    @SequenceGenerator(name="analise.analise_ndfi_id_seq", sequenceName="analise.analise_ndfi_id_seq", allocationSize=1)
    public Long id;

    @Required
    @Column(name="data")
    public Date dataAnalise;

    @Required
    @Column(name="orbita")
    public Integer orbita;

    @Required
    @Column(name="ponto")
    public Integer ponto;

    @Required
    @Column(name="satelite")
    public String satelite;

    @Required
    @Column(name="nivel_exploracao")
    public String nivelExploracao;

    @Required
    @Column(name="valor_ndfi")
    public Double valor;

    @Required
    @Column(name="area")
    public Double area;

    @Required
    @ManyToOne
    @JoinColumn(name="id_analise_tecnica_manejo")
    public AnaliseTecnicaManejo analiseTecnicaManejo;

    @Column(name = "exibir_pdf")
    public boolean exibirPDF;

    public static List<AnaliseNdfi> gerarAnaliseNfid(AnaliseTecnicaManejo analise) {

        Random rand = new Random();
        int numeroRandomico = rand.nextInt(20) + 1;

        List<AnaliseNdfi> listaAnalise = new ArrayList<>();

        for(int i = 0; i < numeroRandomico; i++) {

            AnaliseNdfi analiseNdfi = new AnaliseNdfi();

            analiseNdfi.dataAnalise = new Date();

            analiseNdfi.orbita = rand.nextInt(2000 ) + 1;

            analiseNdfi.ponto = rand.nextInt(2000 ) + 1;

            analiseNdfi.satelite = UUID.randomUUID().toString().replace('-', ' ');

            analiseNdfi.nivelExploracao = UUID.randomUUID().toString().replace('-', ' ');

            analiseNdfi.valor = Math.random();

            analiseNdfi.area = Math.random();

            analiseNdfi.analiseTecnicaManejo = analise;

            analiseNdfi.exibirPDF = true;

            listaAnalise.add(analiseNdfi);
        }

        return listaAnalise;
    }
}
