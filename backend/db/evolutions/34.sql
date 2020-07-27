# --- !Ups

ALTER TABLE analise.licenca_cancelada
DROP COLUMN data_cancelamento;

ALTER TABLE analise.dispensa_licencamento_cancelada
DROP COLUMN data_cancelamento;

ALTER TABLE analise.licenca_cancelada
ADD COLUMN data_cancelamento TIMESTAMP WITHOUT TIME ZONE NOT NULL;

ALTER TABLE analise.dispensa_licencamento_cancelada
ADD COLUMN data_cancelamento TIMESTAMP WITHOUT TIME ZONE NOT NULL;

COMMENT ON COLUMN analise.licenca_cancelada.data_cancelamento IS 'Data do cancelamento da licença.';
COMMENT ON COLUMN analise.dispensa_licencamento_cancelada.data_cancelamento IS 'Data do cancelamento da Dispensa de licenciamento Ambiental.';

ALTER TABLE analise.licenca_cancelada
ALTER COLUMN justificativa TYPE TEXT;

ALTER TABLE analise.dispensa_licencamento_cancelada
ALTER COLUMN justificativa TYPE TEXT;

# --- !Downs

ALTER TABLE analise.licenca_cancelada
ALTER COLUMN justificativa TYPE INTEGER USING justificativa::INTEGER;

ALTER TABLE analise.dispensa_licencamento_cancelada
ALTER COLUMN justificativa TYPE INTEGER USING justificativa::INTEGER;

ALTER TABLE analise.licenca_cancelada
DROP COLUMN data_cancelamento;

ALTER TABLE analise.dispensa_licencamento_cancelada
DROP COLUMN data_cancelamento;

ALTER TABLE analise.licenca_cancelada
ADD COLUMN data_cancelamento INTEGER NOT NULL;

ALTER TABLE analise.dispensa_licencamento_cancelada
ADD COLUMN data_cancelamento INTEGER NOT NULL;

COMMENT ON COLUMN analise.licenca_cancelada.data_cancelamento IS 'Data do cancelamento da licença.';
COMMENT ON COLUMN analise.dispensa_licencamento_cancelada.data_cancelamento IS 'Data do cancelamento da Dispensa de licenciamento Ambiental.';