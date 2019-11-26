package enums;

public enum GeometriaFocoEnum {
    EMPREENDIMENTO(0,"Empreendimento", "EMPREENDIMENTO"),
    ATIVIDADE(1,"Atividade", "ATIVIDADE"),
    COMPLEXO(2,"Complexo", "COMPLEXO");

    public int id;
    public String nome;
    public String codigo;

    GeometriaFocoEnum(int id, String nome, String codigo) {

        this.id = id;
        this.nome = nome;
        this.codigo = codigo;
    }
}
