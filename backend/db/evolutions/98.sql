# --- !Ups

INSERT INTO analise.tipo_documento(
            id, nome, caminho_modelo, caminho_pasta, prefixo_nome_arquivo)
    VALUES (22, 'Documento notificaçao análise geo', null, 'documento_notificacao_analise_geo', 'documento_notificacao_analise_geo');

CREATE TABLE analise.rel_documento_notificacao
(
    id_documento   INTEGER NOT NULL,
    id_notificacao INTEGER NOT NULL,

    CONSTRAINT pk_rel_documento_notificacao
        PRIMARY KEY (id_documento, id_notificacao),

    CONSTRAINT fk_rdag_documento FOREIGN KEY (id_documento)
        REFERENCES analise.documento (id),

    CONSTRAINT fk_rdag_notificacao FOREIGN KEY (id_notificacao)
        REFERENCES analise.notificacao (id)
);

ALTER TABLE analise.rel_documento_notificacao OWNER TO postgres;
GRANT INSERT, SELECT,UPDATE,DELETE ON TABLE analise.rel_documento_notificacao TO licenciamento_am;

COMMENT ON TABLE analise.rel_documento_notificacao is 'Entidade responsável por armazenar a relação entre as entidades documento e notificação.';
COMMENT ON COLUMN analise.rel_documento_notificacao.id_documento is 'Identificador da tabela documento, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.rel_documento_notificacao.id_notificacao is 'Identificador da tabela notificacao, responsável pelo relacionamento entre as duas tabelas.';

COMMENT ON TABLE analise.rel_documento_analise_geo is 'Entidade responsável por armazenar a relação entre as entidades documento e análise geo.';
COMMENT ON COLUMN analise.rel_documento_analise_geo.id_documento is 'Identificador da tabela documento, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.rel_documento_analise_geo.id_analise_geo is 'Identificador da tabela analise_geo, responsável pelo relacionamento entre as duas tabelas.';

COMMENT ON TABLE analise.rel_documento_analise_juridica is 'Entidade responsável por armazenar a relação entre as entidades documento e análise juridica.';
COMMENT ON COLUMN analise.rel_documento_analise_juridica.id_documento is 'Identificador da tabela documento, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.rel_documento_analise_juridica.id_analise_juridica is 'Identificador da tabela analise_juridica, responsável pelo relacionamento entre as duas tabelas.';

ALTER TABLE analise.notificacao ADD COLUMN documentacao BOOLEAN;
COMMENT ON COLUMN analise.notificacao.documentacao IS 'Indica se a notificação necessita de documentação.';

ALTER TABLE analise.notificacao ADD COLUMN retificacao_empreendimento BOOLEAN;
COMMENT ON COLUMN analise.notificacao.retificacao_empreendimento IS 'Indica se a notificação necessita de retificação do empreendimento.';

ALTER TABLE analise.notificacao ADD COLUMN retificacao_solicitacao BOOLEAN;
COMMENT ON COLUMN analise.notificacao.retificacao_solicitacao IS 'Indica se a notificação necessita retificação da solicitação.';

ALTER TABLE analise.notificacao ADD COLUMN retificacao_solicitacao_com_geo BOOLEAN;
COMMENT ON COLUMN analise.notificacao.retificacao_solicitacao_com_geo IS 'Indica se a notificação possui retificacao da solicitação com geometria.';



# --- !Downs

ALTER TABLE analise.notificacao DROP COLUMN retificacao_solicitacao_com_geo;
ALTER TABLE analise.notificacao DROP COLUMN retificacao_solicitacao;
ALTER TABLE analise.notificacao DROP COLUMN retificacao_empreendimento;
ALTER TABLE analise.notificacao DROP COLUMN documentacao;

DROP TABLE analise.rel_documento_notificacao;

DELETE FROM analise.tipo_documento WHERE id=22; 


