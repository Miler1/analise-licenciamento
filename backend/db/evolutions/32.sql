# --- !Ups

ALTER TABLE analise.suspensao ALTER COLUMN data_suspensao TYPE TIMESTAMP WITHOUT TIME ZONE;

# --- !Downs

ALTER TABLE analise.suspensao ALTER COLUMN data_suspensao TYPE DATE;