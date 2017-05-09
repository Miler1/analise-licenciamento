package models;

import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.junit.Test;

import exceptions.ValidacaoException;
import net.sf.oval.constraint.ValidateWithMethod;
import net.sf.oval.internal.util.ReflectionUtils;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import utils.Identificavel;
import utils.ListUtil;
import utils.validacao.CustomValidation;
import utils.validacao.ICustomValidation;
import utils.validacao.Validacao;


@Entity
@Table(schema = "licenciamento", name = "endereco")
@CustomValidation
public class Endereco extends GenericModel implements ICustomValidation, Identificavel {

	private static final String SEQ = "licenciamento.endereco_id_seq";
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;
	
	@Column(name="tipo_endereco")
	@Enumerated(EnumType.ORDINAL)
	public TipoLocalizacao tipo;
	
	public Integer cep;
	
	public String logradouro;

	public String numero;
	
	public String complemento;
	
	public Boolean correspondencia;
	
	public String bairro;
	
	@Column(name = "caixa_postal")
	public String caixaPostal;
	
	@Column(name = "roteiro_acesso")
	public String roteiroAcesso;
	
	@ManyToOne
	@JoinColumn(name="id_municipio", referencedColumnName="id_municipio")
	public Municipio municipio;
	

	@Override
	public Long getId() {
		
		return this.id;
	}
	
	public boolean isValid() {
		
		if (tipo == null)
			return false;
		
		if (tipo == TipoLocalizacao.ZONA_RURAL) {
			
			return !correspondencia && Validacao.checkRequired(municipio, roteiroAcesso);
			
		} else {
			
			return Validacao.checkRequired(cep, logradouro, municipio);
		}
	}
	
	@PrePersist
	@PreUpdate
	private void preSave() {
		
		if (tipo == TipoLocalizacao.ZONA_RURAL) {
			
			this.logradouro = null;
			this.cep = null;
			this.complemento = null;
			this.bairro = null;
			this.numero = null;
			
		} else {
			
			this.roteiroAcesso = null;
		}
		
		if (this.correspondencia == null || !this.correspondencia) {
			
			this.correspondencia = false;
			this.caixaPostal = null;
		}
	}
	
	public void update(Endereco dados) {
		
		this.tipo = dados.tipo;
		this.cep = dados.cep;
		this.logradouro = dados.logradouro;
		this.numero = dados.numero;
		this.complemento = dados.complemento;
		this.correspondencia = dados.correspondencia;
		this.bairro = dados.bairro;
		this.caixaPostal = dados.caixaPostal;
		this.roteiroAcesso = dados.roteiroAcesso;
		this.municipio = dados.municipio;
		
		Validacao.validar(this);
		super.save();
	}
	
	public static Endereco getByCorrespondencia(Collection<Endereco> enderecos, boolean correspondencia) {
		
		if (enderecos == null)
			return null;
		
		for (Endereco endereco : enderecos) {
			
			if (endereco.correspondencia.equals(correspondencia))
				return endereco;
		}

		return null;
	}

	public static int countByCorrespondencia(Collection<Endereco> enderecos, boolean correspondencia) {
		
		int count = 0;
		
		if (enderecos == null)
			return 0;
		
		for (Endereco endereco : enderecos) {
			
			if (endereco.correspondencia.equals(correspondencia))
				count++;
		}

		return count;
	}
	
	public String getDescricao() {
		
		return getDescricao(Integer.MAX_VALUE);
	}
	
	public String getDescricao(int max) {
		
		if (this.tipo == TipoLocalizacao.ZONA_RURAL) {
			
			return this.roteiroAcesso.length() > max 
					? this.roteiroAcesso.substring(0, max) : this.roteiroAcesso;
			
		} else {
			
			StringBuilder sb = new StringBuilder(this.logradouro);
			
			if (sb.length() > max)
				return sb.substring(0, max).toString();
			
			String separador = ", ";
			
			boolean temp = 
				concatenarDescricao(sb, numero, separador, max) &&
				concatenarDescricao(sb, complemento, separador, max) &&
				concatenarDescricao(sb, bairro, separador, max) &&
				concatenarDescricao(sb, "CEP " + this.cep, separador, max);
		
			return sb.toString();
		}
	}
	
	private boolean concatenarDescricao(StringBuilder sb, String texto, String prefixo, int max) {
			
		if (texto == null)
			return true;
			
		if ((sb.length() + texto.length() + prefixo.length()) <= max) {
			
			sb.append(prefixo).append(texto);
			return true;
		
		} else {
			
			return false;
		}
	}
	
	public boolean isZonaRural() {
		
		return (tipo == TipoLocalizacao.ZONA_RURAL);
		
	}

}
