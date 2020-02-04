package models;

import models.licenciamento.Caracterizacao;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import play.db.jpa.JPABase;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

@Entity
@Table(schema = "licenciamento", name = "processo")
public class ProcessoLicenciamento extends GenericModel {

	private static final String SEQ = "licenciamento.processo_id_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;

	@Column(name = "numero")
	public String numero;

	@OneToMany(mappedBy = "processoLicenciamento")
	public List<Caracterizacao> caracterizacoes;

	public <T extends JPABase> T save(){
		super.save();
		JPA.em().refresh(this);
		gerarNumero();
		return super.save();
	}

	private void gerarNumero(){
		this.numero = Calendar.getInstance().get(Calendar.YEAR) + "/" + String.format("%07d", this.id);
	}

	public Processo gerar(){
		return this.save();
	}
}
