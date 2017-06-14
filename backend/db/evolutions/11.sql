# --- !Ups

ALTER TABLE analise.analise_juridica ADD COLUMN parecer_validacao TEXT;
COMMENT ON COLUMN analise.analise_juridica.parecer_validacao IS 'Parecer da validação da análise jurdica';

# --- !Downs

ALTER TABLE analise.analise_juridica DROP COLUMN parecer_validacao;