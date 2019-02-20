package models.portalSeguranca;

import play.Play;
import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(schema = "portal_seguranca", name = "modulo")
public class Modulo extends GenericModel {

	public enum Alvo {
		MESMA_JANELA,
		OUTRA_JANELA
	}
	
	public static final String caminhoTemp = Play.applicationPath + "/tmp/";
	public static final String pathModulos = Play.configuration.getProperty("application.diretorioGravarImagens.modulos") + "/";
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_modulo")
	@SequenceGenerator(name = "sq_modulo", sequenceName = "portal_seguranca.sq_modulo", allocationSize = 1)
	public Integer id;

	@Required(message = "modulos.validacao.nome.obrigatorio")
	@MinSize(message = "modulos.validacao.nome.min", value = 3)
	@MaxSize(message = "modulos.validacao.nome.max", value = 200)
	public String nome;

	@Required(message = "modulos.validacao.sigla.obrigatorio")
	@MinSize(message = "modulos.validacao.sigla.min", value = 3)
	@MaxSize(message = "modulos.validacao.sigla.max", value = 3)
	public String sigla;

	private String logotipo;

	@Required(message = "modulos.validacao.url.obrigatorio")
	public String url;

	@Required(message = "modulos.validacao.descricao.obrigatorio")
	public String descricao;

	@Column(name = "data_cadastro")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataCadastro;

	@Required(message = "modulos.validacao.ativo.obrigatorio")
	public Boolean ativo;
	
	@Transient
	public String key;

	@Required(message = "modulos.validacao.chave.obrigatorio")
	public String chave;

	@Enumerated(EnumType.ORDINAL)
	public Alvo alvo;

	public Boolean fixo;
	
	@Transient
	private List<Integer> permittedActionsIds;

}
