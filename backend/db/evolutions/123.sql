
# --- !Ups

ALTER TABLE analise.parecer_analista_geo ALTER COLUMN conclusao DROP NOT NULL;
ALTER TABLE analise.parecer_analista_tecnico ALTER COLUMN da_conclusao DROP NOT NULL;

# --- !Downs

ALTER TABLE analise.parecer_analista_geo ALTER COLUMN conclusao SET NOT NULL;
ALTER TABLE analise.parecer_analista_tecnico ALTER COLUMN da_conclusao SET NOT NULL;


