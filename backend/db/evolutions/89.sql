# --- !Ups

BEGIN;

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

ALTER TABLE analise.comunicado DROP CONSTRAINT fk_c_atividade_caracterizacao;

UPDATE analise.comunicado SET id_atividade_caracterizacao = ac.id_caracterizacao
FROM (
    SELECT a.id_caracterizacao, a.id FROM licenciamento.atividade_caracterizacao a
        INNER JOIN analise.comunicado c ON a.id = c.id_atividade_caracterizacao
    ) AS ac
WHERE id_atividade_caracterizacao = ac.id;

ALTER TABLE analise.comunicado RENAME COLUMN id_atividade_caracterizacao TO id_caracterizacao;
ALTER TABLE analise.comunicado
    ADD CONSTRAINT fk_c_caracterizacao FOREIGN KEY (id_caracterizacao)
        REFERENCES licenciamento.caracterizacao (id);

COMMENT ON COLUMN analise.comunicado.id_caracterizacao IS 'Coluna que relaciona um comunicado a uma caracterização';

COMMIT;

# --- !Downs

BEGIN;

UPDATE analise.comunicado SET id_caracterizacao = NULL
WHERE id_caracterizacao IS NOT NULL;

ALTER TABLE analise.comunicado RENAME COLUMN id_caracterizacao TO id_atividade_caracterizacao;
ALTER TABLE analise.comunicado
    DROP CONSTRAINT fk_c_caracterizacao,
    ADD CONSTRAINT fk_c_atividade_caracterizacao FOREIGN KEY (id_atividade_caracterizacao)
        REFERENCES licenciamento.atividade_caracterizacao (id);

COMMENT ON COLUMN analise.comunicado.id_atividade_caracterizacao IS 'Identificador da tabela atividade_caracterizacao, responsável pelo relacionamento entre as duas tabelas.';


UPDATE analise.inconsistencia SET id_caracterizacao = NULL
WHERE id_caracterizacao IS NOT NULL;

ALTER TABLE analise.inconsistencia RENAME COLUMN id_caracterizacao TO id_atividade_caracterizacao;
ALTER TABLE analise.inconsistencia
    DROP CONSTRAINT fk_i_caracterizacao,
    ADD CONSTRAINT fk_i_atividade_caracterizacao FOREIGN KEY (id_atividade_caracterizacao)
        REFERENCES licenciamento.atividade_caracterizacao (id);

COMMENT ON COLUMN analise.inconsistencia.id_atividade_caracterizacao IS 'Campo responsável por armazenar o id da atividade_caracterizacao da inconsistencia.';

COMMIT;