# --- !Ups

ALTER TABLE analise.analise_juridica ALTER COLUMN parecer DROP NOT NULL;
ALTER TABLE analise.analise_juridica RENAME COLUMN revisao_soilicitada TO revisao_solicitada;
ALTER TABLE analise.analise_juridica ALTER COLUMN revisao_solicitada SET DEFAULT FALSE;
ALTER TABLE analise.analise_juridica ALTER COLUMN ativo SET DEFAULT TRUE;

# --- !Downs

ALTER TABLE analise.analise_juridica ALTER COLUMN parecer SET NOT NULL;
ALTER TABLE analise.analise_juridica RENAME COLUMN revisao_solicitada TO revisao_soilicitada;
ALTER TABLE analise.analise_juridica ALTER COLUMN revisao_soilicitada DROP DEFAULT;
ALTER TABLE analise.analise_juridica ALTER COLUMN ativo DROP DEFAULT;
