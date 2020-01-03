
# --- !Ups

ALTER TABLE analise.inconsistencia_tecnica_parametro ADD COLUMN id_atividade_caracterizacao integer;
COMMENT ON COLUMN analise.inconsistencia_tecnica_parametro.id_atividade_caracterizacao IS 'Identificador da atividade caracterizacao a qual os parâmetros pertencem.';

# --- !Downs

ALTER TABLE analise.inconsistencia_tecnica_parametro DROP COLUMN id_atividade_caracterizacao;


