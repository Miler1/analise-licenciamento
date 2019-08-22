package enums;


import models.tmsmap.LayerType;

public enum CamadaGeoEnum implements LayerType{

	PROPRIEDADE("PRP", "Propriedade", "PROPRIEDADE", "#DF5A53") {

		@Override
		public String getName() {
			return nome;
		}

		@Override
		public String getColor() {
			return color;
		}
	},
	HIDROGRADIA("HID","Hidrografia", "HIDROGRAFIA", "#2196F3") {
		@Override
		public String getName() {
			return nome;
		}

		@Override
		public String getColor() {
			return color;
		}
	},
	APP("APP","Área de Preservação Permanente", "APP", "#8BC34A") {
		@Override
		public String getName() {
			return nome;
		}

		@Override
		public String getColor() {
			return color;
		}
	},
	AREA_ATROPIZADA("AA","Área Antropizada", "AREA_ANTROPIZADA", "#CDDC39") {
		@Override
		public String getName() {
			return nome;
		}

		@Override
		public String getColor() {
			return color;
		}
	},
	ATIVIDADE("ATV","Geometria", "ATIVIDADE", "#A52A2A") {
		@Override
		public String getName() {
			return nome;
		}

		@Override
		public String getColor() {
			return color;
		}
	};

	public String codigo;
	public String nome;
	public String tipo;
	public String color;

	CamadaGeoEnum(String codigo, String nome, String tipo, String color) {

		this.codigo = codigo;
		this.nome = nome;
		this.tipo = tipo;
		this.color = color;

	}

	public static String tipoFromCodigo(String codigo){

		for(CamadaGeoEnum camada :  CamadaGeoEnum.values()){

			if (camada.codigo.equals(codigo)){

				return camada.tipo;

			}
		}
		return "";
	}

	public static CamadaGeoEnum fromTipo(String tipo){

		for(CamadaGeoEnum camada :  CamadaGeoEnum.values()){

			if (camada.tipo.equals(tipo)){

				return camada;

			}
		}
		return CamadaGeoEnum.ATIVIDADE;
	}
}
