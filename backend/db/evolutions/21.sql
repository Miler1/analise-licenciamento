# --- !Ups

ALTER TABLE analise.analise_tecnica ADD justificativa_coordenador TEXT;

COMMENT ON COLUMN analise.analise_tecnica.parecer_validacao IS 'Campo responsável por armazenar a justificativa do coordenador quando o mesmo vincular diretamente um analista técnico.';

# --- !Downs

ALTER TABLE analise.analise_tecnica DROP COLUMN justificativa_coordenador;
