package models.manejoDigital;

import models.licenciamento.Municipio;
import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;

@Entity
@Table(schema = "analise", name = "empreendimento_manejo")
public class EmpreendimentoManejo extends GenericModel {

	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="analise.empreendimento_manejo_id_seq")
	@SequenceGenerator(name="analise.empreendimento_manejo_id_seq", sequenceName="analise.empreendimento_manejo_id_seq", allocationSize=1)
	public Long id;

	@Required
	@Column(name="denominacao")
	public String denominacao;

	@Required
	@Column(name="cpf_cnpj")
	public String cpfCnpj;

	@Required
	@ManyToOne
	@JoinColumn(name="id_imovel", referencedColumnName="id")
	public ImovelManejo imovel;

	@Required
	@ManyToOne
	@JoinColumn(name="id_municipio", referencedColumnName="id_municipio")
	public Municipio municipio;

}
