package models.sicar;

import models.licenciamento.Municipio;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ImovelSicar {
	
	public Long id;
	
	public String nome;
	
	public String codigo;
	
	public String protocolo;
	
//	TODO: Usar geometry
//	public Geometry geo;
	public String geo;
	
	public Municipio municipio;
	
	//TODO usar Date
	public String dataCriacao;
	
	public Double areaImovel;
	
	public Double areaAPP;
	
	public Double areaReservaLegal;
	
	public Double areaUsoAlternativoSolo;

	public ImovelSicar() { }
	
	public ImovelSicar(Long id, String nome, String codigo, String geo, 
			Municipio municipio, Date dataCriacao, Double areaImovel,
			Double areaAPP, Double areaReservaLegal, Double areaUsoAlternativoSolo) {
		
		this.id = id;
		this.nome = nome;
		this.codigo = codigo;
		this.geo = geo;
		this.municipio = municipio;
		this.dataCriacao = new SimpleDateFormat("dd/MM/yyyy").format(dataCriacao);
		this.areaImovel = areaImovel;
		this.areaAPP = areaAPP;
		this.areaReservaLegal = areaReservaLegal;
		this.areaUsoAlternativoSolo = areaUsoAlternativoSolo;
	}
	
	public static ImovelSicar getByCodigo(String codigo, List<ImovelSicar> imoveis) {
		
		if (imoveis == null || imoveis.isEmpty())
			return null;
		
		for (ImovelSicar imovel : imoveis) {
			
			if (imovel.codigo != null && imovel.codigo.equals(codigo))
				return imovel;
		}
		
		return null;
	}
}
