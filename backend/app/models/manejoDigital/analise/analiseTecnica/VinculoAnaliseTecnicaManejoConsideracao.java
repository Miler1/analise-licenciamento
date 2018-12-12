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

@Entity
@Table(schema = "analise", name = "vinculo_analise_tecnica_manejo_consideracao")
public class VinculoAnaliseTecnicaManejoConsideracao extends GenericModel {

	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="analise.vinculo_analise_tecnica_manejo_consideracao_id_seq")
	@SequenceGenerator(name="analise.vinculo_analise_tecnica_manejo_consideracao_id_seq", sequenceName="analise.vinculo_analise_tecnica_manejo_consideracao_id_seq", allocationSize=1)
	public Long id;

	@OneToOne
	@JoinColumn(name = "id_analise_tecnica_manejo")
	public AnaliseTecnicaManejo analiseTecnicaManejo;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_consideracao")
	public Consideracao consideracao;

	@Column(name = "exibir_pdf")
	public boolean exibirPDF;
}
