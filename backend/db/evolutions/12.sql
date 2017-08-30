# --- !Ups

ALTER TABLE analise.analise_documento ALTER COLUMN id_analise_juridica DROP NOT NULL;

ALTER TABLE analise.analise_tecnica ADD parecer_validacao TEXT;

COMMENT ON COLUMN analise.analise_tecnica.parecer_validacao IS 'Parecer da validação da análise técnica.';

# --- !Downs

ALTER TABLE analise.analise_tecnica DROP COLUMN parecer_validacao; 

ALTER TABLE analise.analise_documento ALTER COLUMN id_analise_juridica SET NOT NULL;
