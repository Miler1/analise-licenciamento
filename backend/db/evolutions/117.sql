
# --- !Ups

ALTER TABLE analise.parecer_analista_tecnico ADD COLUMN parecer TEXT;
COMMENT ON COLUMN analise.parecer_analista_tecnico.parecer IS 'Campo responsável por armazenar o parecer da análise técnica';

# --- !Downs

ALTER TABLE analise.parecer_analista_tecnico DROP COLUMN parecer;


