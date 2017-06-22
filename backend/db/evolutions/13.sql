# --- !Ups

ALTER TABLE analise.analise_tecnica DROP CONSTRAINT fk_at_analise_juridica;
ALTER TABLE analise.analise_tecnica ADD CONSTRAINT fk_at_analise_tecnica FOREIGN KEY (id_analise_tecnica_revisada)
REFERENCES analise.analise_tecnica(id);

# --- !Downs

ALTER TABLE analise.analise_tecnica DROP CONSTRAINT fk_at_analise_tecnica;
ALTER TABLE analise.analise_tecnica ADD CONSTRAINT fk_at_analise_juridica FOREIGN KEY (id_analise_tecnica_revisada)
REFERENCES analise.analise_juridica (id);