# --- !Ups

ALTER TABLE analise.inconsistencia
    DROP CONSTRAINT fk_i_atividade_caracterizacao;

UPDATE analise.inconsistencia SET id_atividade_caracterizacao = ac.id_caracterizacao
FROM (
    SELECT a.id_caracterizacao, a.id FROM licenciamento.atividade_caracterizacao a
        INNER JOIN analise.inconsistencia i ON a.id = i.id_atividade_caracterizacao
    ) AS ac
WHERE id_atividade_caracterizacao = ac.id;

ALTER TABLE analise.inconsistencia RENAME COLUMN id_atividade_caracterizacao TO id_caracterizacao;
COMMENT ON COLUMN analise.inconsistencia.id_caracterizacao IS 'Campo responsável por armazenar o id da caracterizacao da inconsistencia.';

ALTER TABLE analise.inconsistencia
    ADD CONSTRAINT fk_i_caracterizacao FOREIGN KEY (id_caracterizacao)
        REFERENCES licenciamento.caracterizacao (id);

ALTER TABLE analise.comunicado DROP COLUMN id_atividade_caracterizacao;

CREATE TABLE analise.rel_comunicado_atividade_caracterizacao
(
    id_comunicado               INTEGER NOT NULL,
    id_atividade_caracterizacao INTEGER NOT NULL,

    CONSTRAINT fk_rcac_comunicado FOREIGN KEY (id_comunicado)
        REFERENCES analise.comunicado (id)
);

ALTER TABLE analise.rel_comunicado_atividade_caracterizacao OWNER TO postgres;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE analise.rel_comunicado_atividade_caracterizacao TO licenciamento_am;

COMMENT ON TABLE analise.rel_comunicado_atividade_caracterizacao IS 'Entidade responsável pelo armazenamento das atividades do comunicado';
COMMENT ON COLUMN analise.rel_comunicado_atividade_caracterizacao.id_comunicado IS 'Coluna que armazena o id do comunicado';
COMMENT ON COLUMN analise.rel_comunicado_atividade_caracterizacao.id_atividade_caracterizacao IS 'Coluna que armazena o id da atividade de caracterização';

ALTER TABLE analise.rel_comunicado_atividade_caracterizacao
    ADD CONSTRAINT fk_rcac_atividade_caracterizacao FOREIGN KEY (id_atividade_caracterizacao)
        REFERENCES licenciamento.atividade_caracterizacao (id);

# --- !Downs

DROP TABLE analise.rel_comunicado_atividade_caracterizacao;

ALTER TABLE analise.comunicado
    ADD COLUMN id_atividade_caracterizacao INTEGER,
    ADD CONSTRAINT fk_c_atividade_caracterizacao FOREIGN KEY (id_atividade_caracterizacao)
        REFERENCES licenciamento.atividade_caracterizacao;

COMMENT ON COLUMN analise.comunicado.id_atividade_caracterizacao IS 'Identificador da tabela atividade_caracterizacao, responsável pelo relacionamento entre as duas tabelas.';

ALTER TABLE analise.inconsistencia RENAME COLUMN id_caracterizacao TO id_atividade_caracterizacao;
COMMENT ON COLUMN analise.inconsistencia.id_atividade_caracterizacao IS 'Campo responsável por armazenar o id da atividade_caracterizacao da inconsistencia.';

ALTER TABLE analise.inconsistencia
    DROP CONSTRAINT fk_i_caracterizacao,
    ADD CONSTRAINT fk_i_atividade_caracterizacao FOREIGN KEY (id_atividade_caracterizacao)
        REFERENCES licenciamento.atividade_caracterizacao (id);