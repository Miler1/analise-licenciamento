
# --- !Ups

UPDATE analise.tipo_documento
SET caminho_pasta='notificacao_analise_tecnica'
WHERE id=6;


CREATE TABLE analise.rel_documento_notificacao_tecnica
(
    id_documento   INTEGER NOT NULL,
    id_notificacao INTEGER NOT NULL,

    CONSTRAINT pk_rel_documento_notificacao_tecnica
        PRIMARY KEY (id_documento, id_notificacao),
    CONSTRAINT fk_rdnt_documento FOREIGN KEY (id_documento)
        REFERENCES analise.documento (id),
    CONSTRAINT fk_rdnt_notificacao FOREIGN KEY (id_notificacao)
        REFERENCES analise.notificacao (id)
);
ALTER TABLE analise.rel_documento_notificacao_tecnica OWNER TO postgres;
GRANT INSERT, SELECT,UPDATE,DELETE ON TABLE analise.rel_documento_notificacao_tecnica TO licenciamento_am;
COMMENT ON TABLE analise.rel_documento_notificacao_tecnica is 'Entidade responsável por armazenar a relação entre as entidades documento e notificação referentes aos documentos gerados na notificação do técnico.';
COMMENT ON COLUMN analise.rel_documento_notificacao_tecnica.id_documento is 'Identificador da tabela documento, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.rel_documento_notificacao_tecnica.id_notificacao is 'Identificador da tabela notificacao, responsável pelo relacionamento entre as duas tabelas referentes aos documentos gerados na notificação do técnico.';


# --- !Downs

UPDATE analise.tipo_documento
SET caminho_pasta='notificacao_analise'
WHERE id=6;


DROP TABLE analise.rel_documento_notificacao_tecnica;

