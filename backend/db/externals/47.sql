# --- !Ups

ALTER TABLE analise.comunicado
    ADD CONSTRAINT fk_c_orgao FOREIGN KEY (id_orgao) REFERENCES licenciamento.orgao (id);

# --- !Downs

ALTER TABLE analise.comunicado DROP CONSTRAINT fk_c_orgao;