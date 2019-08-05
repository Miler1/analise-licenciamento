package enums;


public enum CamadaGeoEnum {

	PROPRIEDADE("PRP", "Propriedade", "PROPRIEDADE"),
	HIDROGRADIA("HID","Hidrografia", "HIDROGRAFIA"),
	APP("APP","Área de Preservação Permanente", "APP"),
	AREA_ATROPIZADA("AA","Área Antropizada", "AREA_ANTROPIZADA"),
	AREA_PROJETO("ARP","Área do Projeto", "AREA_PROJETO");

	public String codigo;
	public String nome;
	public String tipo;

	CamadaGeoEnum(String codigo, String nome, String tipo) {

		this.codigo = codigo;
		this.nome = nome;
		this.tipo = tipo;

	}

	public static String tipoFromCodigo(String codigo){

		for(CamadaGeoEnum camada :  CamadaGeoEnum.values()){

			if (camada.codigo.equals(codigo)){

				return camada.tipo;

			}
		}
		return "";
	}
}
