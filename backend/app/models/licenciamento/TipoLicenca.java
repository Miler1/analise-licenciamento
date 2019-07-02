package models.licenciamento;

import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(schema = "licenciamento", name = "tipo_licenca")
public class TipoLicenca extends GenericModel {

	private static Map<Long, Long> TIPOS_DOCUMENTO = new HashMap<>();

	public static final long DISPENSA_INEXIGIBILIDADE = 1l;
	public static final long LICENCA_PREVIA = 2l;
	public static final long LICENCA_INSTALACAO = 3l;
	public static final long LICENCA_OPERACAO = 4l;
	public static final long LICENCA_AMBIENTAL_RURAL = 5l;
	public static final long RENOVACAO_LICENCA_DE_INSTALACAO = 6l;
	public static final long RENOVACAO_LICENCA_DE_OPERACAO = 7l;

	static {

		TIPOS_DOCUMENTO.put(DISPENSA_INEXIGIBILIDADE, TipoDocumentoLicenciamento.DISPENSA_LICENCIAMENTO);
		TIPOS_DOCUMENTO.put(LICENCA_PREVIA, TipoDocumentoLicenciamento.LICENCA_PREVIA);
		TIPOS_DOCUMENTO.put(LICENCA_INSTALACAO, TipoDocumentoLicenciamento.LICENCA_INSTALACAO);
		TIPOS_DOCUMENTO.put(LICENCA_OPERACAO, TipoDocumentoLicenciamento.LICENCA_OPERACAO);
		TIPOS_DOCUMENTO.put(LICENCA_AMBIENTAL_RURAL, TipoDocumentoLicenciamento.LICENCA_AMBIENTAL_RURAL);
		TIPOS_DOCUMENTO.put(RENOVACAO_LICENCA_DE_INSTALACAO,TipoDocumentoLicenciamento.RENOVACAO_LICENCA_DE_INSTALACAO);
		TIPOS_DOCUMENTO.put(RENOVACAO_LICENCA_DE_OPERACAO,TipoDocumentoLicenciamento.RENOVACAO_LICENCA_DE_OPERACAO);
	}

	public enum Finalidades {

		DISPENSA("DISPENSA"),
		SOLICITACAO("SOLICITACAO"),
		RENOVACAO("RENOVACAO");

		public String codigo;

		Finalidades(String codigo) {
			this.codigo = codigo;
		}

	}
	
	@Id
	public Long id;

	@Column
	public String nome;

	@Column
	public String sigla;

	@Column(name = "validade_em_anos")
	public Integer validadeEmAnos;

	@Column(name = "finalidade")
	public String finalidade;
	
	@Transient
	public Double taxaAdministrativa;
	
	@Transient
	public Double taxaLicenca;
	
	@Transient
	public Double valorDae;
}
