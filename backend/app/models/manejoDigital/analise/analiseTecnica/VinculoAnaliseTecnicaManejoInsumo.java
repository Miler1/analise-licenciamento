package models.manejoDigital.analise.analiseTecnica;

import play.db.jpa.GenericModel;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
@Table(schema = "analise", name = "vinculo_analise_tecnica_manejo_insumo")
public class VinculoAnaliseTecnicaManejoInsumo extends GenericModel {

	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="analise.vinculo_analise_tecnica_manejo_insumo_id_seq")
	@SequenceGenerator(name="analise.vinculo_analise_tecnica_manejo_insumo_id_seq", sequenceName="analise.vinculo_analise_tecnica_manejo_insumo_id_seq", allocationSize=1)
	public Long id;

	@OneToOne
	@JoinColumn(name = "id_analise_tecnica_manejo")
	public AnaliseTecnicaManejo analiseTecnicaManejo;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_insumo")
	public Insumo insumo;

	@Column(name = "exibir_pdf")
	public boolean exibirPDF;

	public static List<VinculoAnaliseTecnicaManejoInsumo> gerarVinculos(AnaliseTecnicaManejo analise) {

		Random rand = new Random();
		int numeroRandomico = rand.nextInt(20) + 1;

		List<VinculoAnaliseTecnicaManejoInsumo> listaVinculo = new ArrayList<>();

		for (int i = 0; i < numeroRandomico; i++) {

			VinculoAnaliseTecnicaManejoInsumo vinculo = new VinculoAnaliseTecnicaManejoInsumo();

			vinculo.analiseTecnicaManejo = analise;

			if(rand.nextBoolean()) {

				List<Insumo> insumos = Insumo.findAll();

				if (insumos.size() > 0) {

					vinculo.insumo = insumos.get(rand.nextInt(insumos.size()));

				} else {

					vinculo.insumo = Insumo.gerarInsumo();
				}

			} else {

				vinculo.insumo = Insumo.gerarInsumo();
			}

			vinculo.exibirPDF = true;

			listaVinculo.add(vinculo);
		}

		return listaVinculo;
	}
}
