# --- !Ups

ALTER TABLE analise.licenca_cancelada
ADD CONSTRAINT fk_lc_licenca FOREIGN KEY(id_licenca) REFERENCES licenciamento.licenca(id);

# --- !Downs

ALTER TABLE analise.licenca_cancelada
DROP CONSTRAINT fk_lc_licenca;

