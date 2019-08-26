# --- !Ups

BEGIN;

ALTER TABLE licenciamento.empreendimento ALTER COLUMN possui_anexo DROP NOT NULL;
ALTER TABLE licenciamento.empreendimento ALTER COLUMN possui_anexo SET DEFAULT NULL;
ALTER TABLE licenciamento.empreendimento RENAME COLUMN possui_anexo TO possui_shape;
COMMENT ON COLUMN licenciamento.empreendimento.possui_shape IS 'Boooleano que indica se o empreendimento posui ou não upload de shapes, se é nulo o empreendimento nunca cadastrou shapes';

COMMIT;

# --- !Downs

BEGIN;

ALTER TABLE licenciamento.empreendimento ALTER COLUMN possui_shape SET DEFAULT false;
ALTER TABLE licenciamento.empreendimento RENAME COLUMN possui_shape TO possui_anexo;
ALTER TABLE analise.empreendimento_camada_geo RENAME CONSTRAINT fk_ecg_empreendimento TO fk_aga_empreendimento;
COMMENT ON COLUMN licenciamento.empreendimento.possui_anexo IS 'Boooleano que indica se o empreendimento posui ou não anexos';

COMMIT;
