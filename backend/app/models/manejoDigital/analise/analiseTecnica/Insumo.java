package models.manejoDigital.analise.analiseTecnica;

import org.joda.time.LocalDate;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Entity
@Table(schema = "analise", name = "insumo")
public class Insumo extends GenericModel {

	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="analise.insumo_id_seq")
	@SequenceGenerator(name="analise.insumo_id_seq", sequenceName="analise.insumo_id_seq", allocationSize=1)
	public Long id;

	@Column
	public Date data;

	@Column
	public String satelite;

	@Column(name = "orb_ponto")
	public String orbPonto;

	public static Insumo gerarInsumo() {

		Random rand = new Random();
		Insumo insumo = new Insumo();

		insumo.data = new Date(-946771200000L + (Math.abs(rand.nextLong()) % (70L * 365 * 24 * 60 * 60 * 1000)));
		insumo.satelite = UUID.randomUUID().toString().replace('-', ' ');
		insumo.orbPonto = rand.nextInt(200) + "/" + rand.nextInt(2000);

		return insumo;
	}

	public int getAno() {

		LocalDate localDate = new LocalDate(this.data);
		return localDate.getYear();
	}
}
