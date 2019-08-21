# --- !Ups

ALTER TABLE analise.inconsistencia
    ADD COLUMN id_atividade_caracterizacao INTEGER,
    ADD COLUMN id_geometria_atividade INTEGER;

COMMENT ON COLUMN analise.inconsistencia.id_atividade_caracterizacao IS 'Campo responsável por armazenar o id da atividade_caracterizacao da inconsistencia.';
COMMENT ON COLUMN analise.inconsistencia.id_geometria_atividade IS 'Campo responsável por armazenar o id da geometria_atividade da inconsistencia.';

# --- !Downs

ALTER TABLE analise.inconsistencia
    DROP COLUMN id_atividade_caracterizacao,
    DROP COLUMN id_geometria_atividade;