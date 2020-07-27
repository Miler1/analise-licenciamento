# --- !Ups

ALTER TABLE analise.suspensao ADD COLUMN justificativa text;
COMMENT ON COLUMN analise.suspensao.justificativa IS 'justificativa da suspensão de licença realizada pelo Aprovador.';

# --- !Downs

ALTER TABLE analise.suspensao DROP COLUMN justificativa;
