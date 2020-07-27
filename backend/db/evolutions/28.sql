# --- !Ups

ALTER TABLE analise.analise_tecnica ADD COLUMN data_fim_validacao_aprovador timestamp without time zone;

COMMENT ON COLUMN analise.analise_tecnica.data_fim_validacao_aprovador IS 'Data final da análise do aprovador.';

# --- !Downs

ALTER TABLE analise.analise_tecnica DROP COLUMN data_fim_validacao_aprovador;