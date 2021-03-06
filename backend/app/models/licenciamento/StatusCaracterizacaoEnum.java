package models.licenciamento;

public enum StatusCaracterizacaoEnum {

    DEFERIDO ( 1L, "DEFERIDO"),
    EM_ANDAMENTO ( 2L, "EM_ANDAMENTO"),
    AGUARDANDO_EMISSAO_DAE ( 3L, "AGUARDANDO_EMISSAO_DAE"),
    AGUARDANDO_QUITACAO_DAE ( 4L, "AGUARDANDO_QUITACAO_DAE"),
    EM_ANALISE ( 5L, "EM_ANALISE"),
    ARQUIVADO ( 6L, "ARQUIVADO"),
    SUSPENSO ( 7L, "SUSPENSO"),
    CANCELADO ( 8L, "CANCELADO"),
    NOTIFICADO ( 9L, "NOTIFICADO"),
    EM_RENOVACAO_SEM_ALTERACAO ( 10L, "EM_RENOVACAO_SEM_ALTERACAO"),
    EM_RENOVACAO_COM_ALTERACAO ( 11L, "EM_RENOVACAO_COM_ALTERACAO"),
    EXPIRADA ( 12L, "EXPIRADA"),
    VENCIDO_AGUARDANDO_PAGAMENTO ( 13L, "VENCIDO_AGUARDANDO_PAGAMENTO"),
    VENCIDO_AGUARDANDO_EMISSAO ( 14L, "VENCIDO_AGUARDANDO_EMISSAO"),
    ANALISE_APROVADA ( 15L, "ANALISE_APROVADA"),
    AGUARDANDO_EMISSAO_TAXA_LICENCIAMENTO ( 16L, "AGUARDANDO_EMISSAO_TAXA_LICENCIAMENTO"),
    SOLICITACAO_DE_DESVINCULO ( 17L, "SOLICITACAO_DE_DESVINCULO"),
    ANALISE_REJEITADA(20L,"ANALISE_REJEITADA");

    public Long id;

    public String codigo;

    StatusCaracterizacaoEnum(Long id, String codigo) {
        this.id = id;
        this.codigo = codigo;
    }
}
