# --- !Ups

ALTER TABLE analise.processo_manejo ADD COLUMN justificativa_indeferimento TEXT;
COMMENT ON COLUMN analise.processo_manejo.justificativa_indeferimento IS 'Justificativa dada pelo análista técnico para o indeferimento do processo.';

# --- !Downs

ALTER TABLE analise.processo_manejo DROP COLUMN justificativa_indeferimento;