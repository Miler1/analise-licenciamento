# --- !Ups

ALTER TABLE analise.licenca_analise ALTER COLUMN validade DROP NOT NULL;


# --- !Downs

ALTER TABLE analise.licenca_analise ALTER COLUMN validade NOT NULL;