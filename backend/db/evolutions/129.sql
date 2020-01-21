
# --- !Ups

ALTER TABLE analise.parecer_analista_tecnico RENAME COLUMN data TO data_parecer;


# --- !Downs

ALTER TABLE analise.parecer_analista_tecnico RENAME COLUMN data_parecer TO data;

