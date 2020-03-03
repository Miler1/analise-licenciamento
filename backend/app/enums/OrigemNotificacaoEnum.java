package enums;

public enum OrigemNotificacaoEnum {

    ANALISE_GEO(1L,"ANALISE_GEO" ),
    ANALISE_TECNICA(2L,"ANALISE_TECNICA" );

    public Long id;
    public String codigo;

    OrigemNotificacaoEnum(Long id, String codigo) {
        this.id = id;
        this.codigo = codigo;
    }
}
