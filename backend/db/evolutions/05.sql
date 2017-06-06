# --- !Ups

ALTER TABLE analise.analise ADD COLUMN ativo Boolean DEFAULT true;

COMMENT ON COLUMN analise.analise.ativo IS 'Indica se a analise está ativa ou não.';

# --- !Downs

ALTER TABLE analise.analise DROP COLUMN ativo;
