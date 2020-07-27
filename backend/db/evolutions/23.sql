# --- !Ups

ALTER TABLE analise.analise_tecnica ADD COLUMN data_cadastro TIMESTAMP WITHOUT TIME ZONE;
COMMENT ON COLUMN analise.analise_tecnica.data_cadastro IS 'Data de cadastro da análise.';

UPDATE analise.analise_tecnica SET data_cadastro=(data_vencimento_prazo-10);

ALTER TABLE analise.analise_tecnica ALTER COLUMN data_cadastro SET NOT NULL;

# --- !Downs

ALTER TABLE analise.analise_tecnica DROP COLUMN data_cadastro;