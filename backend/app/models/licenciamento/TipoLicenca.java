package models.licenciamento;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.db.jpa.GenericModel;

@Entity
@Table(schema = "licenciamento", name = "tipo_licenca")
public class TipoLicenca extends GenericModel {

	private static Map<Long, Long> TIPOS_DOCUMENTO = new HashMap<>();
	
	public static final long DLA = 1l;
	public static final long LICENCA_PREVIA = 2l;
	public static final long LICENCA_INSTALACAO = 3l;
	public static final long LICENCA_OPERACAO = 4l;
	public static final long LICENCA_AMBIENTAL_RURAL = 5l;
	
	static {
		
		TIPOS_DOCUMENTO.put(DLA, TipoDocumentoLicenciamento.DISPENSA_LICENCIAMENTO);
		TIPOS_DOCUMENTO.put(LICENCA_PREVIA, TipoDocumentoLicenciamento.LICENCA_PREVIA);
		TIPOS_DOCUMENTO.put(LICENCA_INSTALACAO, TipoDocumentoLicenciamento.LICENCA_INSTALACAO);
		TIPOS_DOCUMENTO.put(LICENCA_OPERACAO, TipoDocumentoLicenciamento.LICENCA_OPERACAO);
		TIPOS_DOCUMENTO.put(LICENCA_AMBIENTAL_RURAL, TipoDocumentoLicenciamento.LICENCA_AMBIENTAL_RURAL);
	}
	
	@Id
	public Long id;

	public String nome;

	@Column(name = "codigo_receita_dae")
	public Integer codigoReceitaDae;
	
	@Column(name = "validade_em_anos")
	public Integer validadeEmAnos;
	
	@Transient
	public Double taxaAdministrativa;
	
	@Transient
	public Double taxaLicenca;
	
	@Transient
	public Double valorDae;
}
