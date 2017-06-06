# --- !Ups

ALTER TABLE analise.processo ADD COLUMN data_cadastro TIMESTAMP WITHOUT TIME ZONE NOT NULL; -- Data de cadastro do processo.
COMMENT ON COLUMN analise.processo.data_cadastro IS 'Data de cadastro do processo.';

# --- !Downs

ALTER TABLE analise.processo DROP COLUMN data_cadastro; -- Data de cadastro do processo.
