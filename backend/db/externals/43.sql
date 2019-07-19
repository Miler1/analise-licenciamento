# --- !Ups

BEGIN;

ALTER TABLE licenciamento.empreendimento ADD COLUMN possui_anexo BOOLEAN;

COMMENT ON COLUMN licenciamento.empreendimento.possui_anexo IS 'Boooleano que indica se o empreendimento posui ou não anexos';

UPDATE licenciamento.empreendimento SET possui_anexo = false WHERE possui_anexo is null;

ALTER TABLE licenciamento.empreendimento ALTER COLUMN possui_anexo SET NOT NULL;
ALTER TABLE licenciamento.empreendimento ALTER COLUMN possui_anexo SET DEFAULT FALSE;

ALTER TABLE analise.analise_geo_anexo
    ADD CONSTRAINT fk_aga_empreendimento FOREIGN KEY (id_empreendimento)
        REFERENCES licenciamento.empreendimento (id);

GRANT USAGE ON SCHEMA analise TO tramitacao;
GRANT SELECT ON ALL TABLES IN SCHEMA analise TO tramitacao;

COMMIT;

# --- !Downs

BEGIN;

ALTER TABLE licenciamento.empreendimento DROP COLUMN possui_anexo;

ALTER TABLE analise.analise_geo_anexo DROP CONSTRAINT fk_aga_empreendimento;

REVOKE USAGE ON SCHEMA analise FROM tramitacao;
REVOKE SELECT ON ALL TABLES IN SCHEMA analise FROM tramitacao;

COMMIT;
