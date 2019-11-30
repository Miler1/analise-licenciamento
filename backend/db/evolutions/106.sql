
# --- !Ups

ALTER TABLE analise.inconsistencia ADD COLUMN id_atividade_caracterizacao INTEGER;
COMMENT ON COLUMN analise.inconsistencia.id_atividade_caracterizacao IS 'Campo responsável por armazenar o id da atividade de caracterização quando a categoria é ATIVIDADE.';


# --- !Downs

ALTER TABLE analise.inconsistencia DROP COLUMN id_atividade_caracterizacao;
