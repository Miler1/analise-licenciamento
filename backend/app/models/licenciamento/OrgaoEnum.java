package models.licenciamento;

public enum OrgaoEnum {

    FUNAI ("Fundação Nacional do Índio", "FUNAI"),
    DEMUC ("Departamento Mudanças Climáticas e gestão de UC", "DEMUC"),
    ICMBIO ("Instituto Chico Mendes de conservação da Biodiversidade", "ICMBIO"),
    INCRA ("Instituto Nacional da Colonização e Reforma Agrária", "INCRA"),
    SPF ("Secretaria do Estado de Política Fundiária", "SPF"),
    IPHAN ("Instituto do Patrimônio Histórico e Artístico Nacional", "IPHAN"),
    IBAMA ("Instituto Brasileiro do Meio Ambiente e dos Recursos Naturais Renováveis", "IBAMA");

    public String nome;

    public String codigo;

    OrgaoEnum(String nome, String codigo) {

        this.nome = nome;
        this.codigo = codigo;

    }

}
