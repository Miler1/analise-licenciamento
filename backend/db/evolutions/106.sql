
# --- !Ups

ALTER TABLE analise.inconsistencia ADD COLUMN id_atividade_caracterizacao INTEGER;
COMMENT ON COLUMN analise.inconsistencia IS 'Campo responsável por armazenar o id da caracterizacao da inconsistencia.';


# --- !Downs

ALTER TABLE analise.inconsistencia DROP COLUMN id_atividade_caracterizacao;
