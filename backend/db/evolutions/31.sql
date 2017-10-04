# --- !Ups

ALTER TABLE analise.suspensao ADD CONSTRAINT un_suspensao_id_licenca UNIQUE (id_licenca);

# --- !Downs

ALTER TABLE analise.suspensao DROP CONSTRAINT un_suspensao_id_licenca;