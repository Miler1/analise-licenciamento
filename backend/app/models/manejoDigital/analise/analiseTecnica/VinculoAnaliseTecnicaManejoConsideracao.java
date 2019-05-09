package models.manejoDigital.analise.analiseTecnica;

import play.db.jpa.GenericModel;

import javax.persistence.*;

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
