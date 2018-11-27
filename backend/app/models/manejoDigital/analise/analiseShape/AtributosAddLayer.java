package models.manejoDigital.analise.analiseShape;

public class AtributosAddLayer {

	public String protocolo;
	public String amf;
	public Integer status;
	public String responsavel;

	public AtributosAddLayer(String protocolo, String amf, Integer status, String responsavel) {

		this.protocolo = protocolo;
		this.amf = amf.length() > 50 ? amf.substring(0, 50) : amf;
		this.status = status;
		this.responsavel = responsavel;
	}
}
