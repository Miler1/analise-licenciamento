# --- !Ups

ALTER TABLE analise.desvinculo
    ADD COLUMN id_analise_geo INTEGER,
    ADD COLUMN id_analise_tecnica INTEGER,
    ADD COLUMN id_analise_juridica INTEGER,

    ADD CONSTRAINT fk_d_analise_geo FOREIGN KEY (id_analise_geo)
        REFERENCES analise.analise_geo (id),
    ADD CONSTRAINT fk_d_analise_tecnica FOREIGN KEY (id_analise_tecnica)
        REFERENCES analise.analise_tecnica (id),
    ADD CONSTRAINT fk_d_analise_juridica FOREIGN KEY (id_analise_juridica)
        REFERENCES analise.analise_juridica (id),

    DROP COLUMN id_processo,
    DROP COLUMN id_analista;

COMMENT ON COLUMN analise.desvinculo.id_analise_geo IS 'Identificador da tabela id_analise_geo, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.desvinculo.id_analise_tecnica IS 'Identificador da tabela id_analise_tecnica, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.desvinculo.id_analise_juridica IS 'Identificador da tabela analise_juridica, responsável pelo relacionamento entre as duas tabelas.';

CREATE TABLE analise.comunicado
(
    id                          SERIAL                      NOT NULL,
    id_analise_geo              INTEGER,
    id_atividade_caracterizacao INTEGER                     NOT NULL,
    justificativa               TEXT,
    data_cadastro               TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    data_leitura                TIMESTAMP WITHOUT TIME ZONE,
    data_resposta               TIMESTAMP WITHOUT TIME ZONE,
    id_tipo_sobreposicao        INTEGER                     NOT NULL,
    parecer_orgao               TEXT,
    resolvido                   BOOLEAN                     NOT NULL DEFAULT FALSE,
    ativo                       BOOLEAN                     NOT NULL DEFAULT TRUE,

    CONSTRAINT pk_comunicado PRIMARY KEY (id),

    CONSTRAINT fk_c_analise_geo FOREIGN KEY (id_analise_geo)
        REFERENCES analise.analise_geo (id)
);

ALTER TABLE analise.comunicado OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.comunicado TO licenciamento_am;
GRANT SELECT, USAGE ON SEQUENCE analise.comunicado_id_seq TO licenciamento_am;

COMMENT ON TABLE analise.comunicado IS 'Entidade responsável por armazenar os dados referentes ao comunicado que será enviado ao órgão responsável para resolver a restrição encontrada na análise.';
COMMENT ON COLUMN analise.comunicado.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.comunicado.id_analise_geo IS 'Identificador da tabela analise_geo, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.comunicado.id_atividade_caracterizacao IS 'Identificador da tabela atividade_caracterizacao, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.comunicado.justificativa IS 'Identificador da tabela tipo_documento do schema licenciamento, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.comunicado.data_cadastro IS 'Identificador da tabela documento do schema licenciamento, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.comunicado.data_leitura IS 'Identificador da tabela analise_documento, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.comunicado.data_resposta IS 'Campo responsável por armazenar a data em que a notificação foi criada.';
COMMENT ON COLUMN analise.comunicado.id_tipo_sobreposicao IS 'Campo responsável por armazenar o id do tipo de sobreposicao.';
COMMENT ON COLUMN analise.comunicado.parecer_orgao IS 'Campo responsável por armazenar a resposta do órgão.';
COMMENT ON COLUMN analise.comunicado.resolvido IS 'Flag que indica se o comunicado foi resolvido.';
COMMENT ON COLUMN analise.comunicado.ativo IS ' Flag que indica se o comunicado está ativo.';

# --- !Downs

DROP TABLE analise.comunicado;

ALTER TABLE analise.desvinculo
    ADD COLUMN id_processo INTEGER,
    ADD COLUMN id_analista INTEGER,

	ADD CONSTRAINT fk_d_processo
		FOREIGN KEY (id_processo) REFERENCES analise.processo (id),

	ADD CONSTRAINT fk_d_analista_usuario_analise
		FOREIGN KEY (id_analista) REFERENCES analise.usuario_analise (id),

    DROP COLUMN id_analise_geo,
    DROP COLUMN id_analise_tecnica,
    DROP COLUMN id_analise_juridica;