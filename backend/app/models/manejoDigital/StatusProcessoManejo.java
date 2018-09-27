package models.manejoDigital;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "analise", name = "status_processo_manejo")
public class StatusProcessoManejo {

	public static final long AGUARDANDO_ANALISE = 1l;
	public static final long EM_ANALISE = 2l;
	public static final long DEFERIDO = 3l;
	public static final long INDEFERIDO = 4l;

	@Id
	public Long id;

	@Column
	public String nome;

	@Column
	public Integer codigo;
}
