
# --- !Ups

ALTER TABLE analise.processo ADD COLUMN id_caracterizacao INTEGER;
COMMENT ON COLUMN analise.processo.id_caracterizacao IS 'Id da caracterização da solicitação do licenciamento ambiental relacionada ao processo';

UPDATE analise.processo p SET id_caracterizacao = pc.id_caracterizacao FROM analise.rel_processo_caracterizacao pc WHERE p.id = pc.id_processo;

ALTER TABLE analise.processo ALTER COLUMN id_caracterizacao SET NOT NULL;

ALTER TABLE analise.processo ADD CONSTRAINT fk_caracterizacao FOREIGN KEY (id_caracterizacao) REFERENCES licenciamento.caracterizacao;

DROP TABLE analise.rel_processo_caracterizacao;



# --- !Downs

CREATE TABLE analise.rel_processo_caracterizacao(
  id_caracterizacao INTEGER NOT NULL,
  id_processo INTEGER NOT NULL,
  CONSTRAINT pk_pc_processo_caracterizacao PRIMARY KEY(id_caracterizacao,id_processo),
  CONSTRAINT fk_rpc_caracterizacao FOREIGN KEY(id_caracterizacao)
  REFERENCES licenciamento.caracterizacao(id),
  CONSTRAINT fk_rpc_processo FOREIGN KEY(id_processo)
  REFERENCES analise.processo(id),
  CONSTRAINT ue_pc_id_caracterizacao UNIQUE(id_caracterizacao)
  );
  ALTER TABLE analise.rel_processo_caracterizacao OWNER TO postgres;
  GRANT ALL ON TABLE analise.rel_processo_caracterizacao TO postgres;
  GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.rel_processo_caracterizacao TO licenciamento_am;


ALTER TABLE analise.processo DROP CONSTRAINT fk_caracterizacao;

ALTER TABLE analise.processo ALTER COLUMN id_caracterizacao SET DEFAULT NULL;

UPDATE analise.processo p SET id_caracterizacao = NULL FROM analise.rel_processo_caracterizacao pc WHERE p.id_caracterizacao = pc.id_caracterizacao AND p.id = pc.id_processo;

ALTER TABLE analise.processo DROP COLUMN id_caracterizacao;
