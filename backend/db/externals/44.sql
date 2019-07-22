# --- !Ups

BEGIN;

ALTER TABLE licenciamento.empreendimento ALTER COLUMN possui_anexo DROP NOT NULL;
ALTER TABLE licenciamento.empreendimento ALTER COLUMN possui_anexo SET DEFAULT NULL;
ALTER TABLE licenciamento.empreendimento RENAME COLUMN possui_anexo TO possui_sahpe;
COMMENT ON COLUMN licenciamento.empreendimento.possui_sahpe IS 'Boooleano que indica se o empreendimento posui ou não upload de shapes, se é nulo o empreendimento nunca cadastrou shapes';

COMMIT;

# --- !Downs

BEGIN;

ALTER TABLE licenciamento.empreendimento ALTER COLUMN possui_sahpe SET DEFAULT false;
ALTER TABLE licenciamento.empreendimento RENAME COLUMN possui_sahpe TO possui_anexo;
COMMENT ON COLUMN licenciamento.empreendimento.possui_anexo IS 'Boooleano que indica se o empreendimento posui ou não anexos';

COMMIT;
