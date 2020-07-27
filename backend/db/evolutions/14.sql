# --- !Ups

ALTER TABLE analise.licenca_analise DROP COLUMN id_licenca; 

ALTER TABLE analise.licenca_analise ADD id_caracterizacao integer NOT NULL;

ALTER TABLE analise.licenca_analise ADD
  CONSTRAINT fk_la_caracterizacao FOREIGN KEY (id_caracterizacao)
      REFERENCES licenciamento.caracterizacao (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
# --- !Downs

ALTER TABLE analise.licenca_analise DROP COLUMN id_caracterizacao; 

ALTER TABLE analise.licenca_analise ADD id_licenca integer NOT NULL;

ALTER TABLE analise.licenca_analise ADD
  CONSTRAINT fk_la_licenca FOREIGN KEY (id_licenca)
      REFERENCES licenciamento.licenca (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;