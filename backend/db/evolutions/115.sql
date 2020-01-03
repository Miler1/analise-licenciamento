
# --- !Ups

DROP TABLE analise.inconsistencia_tecnica_documento;


CREATE TABLE analise.inconsistencia_tecnica_documento_tecnico_ambiental (
  id serial NOT NULL, 
  id_inconsistencia_tecnica integer NOT NULL, 
  id_documento_tecnico integer NOT NULL, 
  CONSTRAINT pk_inconsistencia_tecnica_documento_tecnico_ambiental PRIMARY KEY (id),
  CONSTRAINT fk_itdta_inconsistencia_tecnica FOREIGN KEY (id_inconsistencia_tecnica)
      REFERENCES analise.inconsistencia_tecnica (id),
  CONSTRAINT fk_itdta_documento_tecnico FOREIGN KEY (id_documento_tecnico)
      REFERENCES licenciamento.solicitacao_grupo_documento (id)
);
ALTER TABLE analise.inconsistencia_tecnica_documento_tecnico_ambiental OWNER TO licenciamento_am;
ALTER TABLE analise.inconsistencia_tecnica_documento_tecnico_ambiental OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.inconsistencia_tecnica_documento_tecnico_ambiental TO licenciamento_am;
GRANT SELECT,USAGE ON SEQUENCE analise.inconsistencia_tecnica_documento_tecnico_ambiental_id_seq TO licenciamento_am;
COMMENT ON TABLE analise.inconsistencia_tecnica_documento_tecnico_ambiental IS 'Tabela responsável por armazenar todas informações das inconsistências adicionadas pelo analista técnico com relaçao aos documentos';
COMMENT ON COLUMN analise.inconsistencia_tecnica_documento_tecnico_ambiental.id IS 'Identificador único da entidade';
COMMENT ON COLUMN analise.inconsistencia_tecnica_documento_tecnico_ambiental.id_inconsistencia_tecnica IS 'Identificador que faz relacionamento com a tabela inconsistencia_tecnica';
COMMENT ON COLUMN analise.inconsistencia_tecnica_documento_tecnico_ambiental.id_documento_tecnico IS 'Identificador que faz relacionamento com a tabela licenciamento.id_documento_tecnico';


CREATE TABLE analise.inconsistencia_tecnica_documento_administrativo (
  id serial NOT NULL, 
  id_inconsistencia_tecnica integer NOT NULL, 
  id_documento_administrativo integer NOT NULL, 
  CONSTRAINT pk_inconsistencia_tecnica_documento_administrativo PRIMARY KEY (id),
  CONSTRAINT fk_itda_inconsistencia_tecnica FOREIGN KEY (id_inconsistencia_tecnica)
      REFERENCES analise.inconsistencia_tecnica (id),
  CONSTRAINT fk_itda_documento_administrativo FOREIGN KEY (id_documento_administrativo)
      REFERENCES licenciamento.solicitacao_documento_caracterizacao (id) 
);
ALTER TABLE analise.inconsistencia_tecnica_documento_administrativo OWNER TO licenciamento_am;
ALTER TABLE analise.inconsistencia_tecnica_documento_administrativo OWNER TO licenciamento_am;
ALTER TABLE analise.inconsistencia_tecnica_documento_administrativo OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.inconsistencia_tecnica_documento_administrativo TO licenciamento_am;
GRANT SELECT,USAGE ON SEQUENCE analise.inconsistencia_tecnica_documento_administrativo_id_seq TO licenciamento_am;
COMMENT ON TABLE analise.inconsistencia_tecnica_documento_administrativo IS 'Tabela responsável por armazenar todas informações das inconsistências adicionadas pelo analista técnico com relaçao aos documentos';
COMMENT ON COLUMN analise.inconsistencia_tecnica_documento_administrativo.id IS 'Identificador único da entidade';
COMMENT ON COLUMN analise.inconsistencia_tecnica_documento_administrativo.id_inconsistencia_tecnica IS 'Identificador que faz relacionamento com a tabela inconsistencia_tecnica';
COMMENT ON COLUMN analise.inconsistencia_tecnica_documento_administrativo.id_documento_administrativo IS 'Identificador que faz relacionamento com a tabela licenciamento.id_documento_administrativo';


    
# --- !Downs

DROP TABLE analise.inconsistencia_tecnica_documento_administrativo;


DROP TABLE analise.inconsistencia_tecnica_documento_tecnico_ambiental;


CREATE TABLE analise.inconsistencia_tecnica_documento (
  id serial NOT NULL, 
  id_inconsistencia_tecnica integer NOT NULL, 
  id_documento_administrativo integer, 
  id_documento_tecnico integer, 
  CONSTRAINT pk_inconsistencia_tecnica_documento PRIMARY KEY (id),
  CONSTRAINT fk_itd_documento_administrativo FOREIGN KEY (id_documento_administrativo)
      REFERENCES licenciamento.solicitacao_documento_caracterizacao (id),
  CONSTRAINT fk_itd_documento_tecnico FOREIGN KEY (id_documento_tecnico)
      REFERENCES licenciamento.solicitacao_grupo_documento (id),
  CONSTRAINT fk_itd_inconsistencia_tecnica FOREIGN KEY (id_inconsistencia_tecnica)
      REFERENCES analise.inconsistencia_tecnica (id)
);
ALTER TABLE analise.inconsistencia_tecnica_documento OWNER TO postgres;
GRANT ALL ON TABLE analise.inconsistencia_tecnica_documento TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.inconsistencia_tecnica_documento TO licenciamento_am;
COMMENT ON TABLE analise.inconsistencia_tecnica_documento IS 'Tabela responsável por armazenar todas informações das inconsistências adicionadas pelo analista técnico com relaçao aos documentos';
COMMENT ON COLUMN analise.inconsistencia_tecnica_documento.id IS 'Identificador único da entidade';
COMMENT ON COLUMN analise.inconsistencia_tecnica_documento.id_inconsistencia_tecnica IS 'Identificador que faz relacionamento com a tabela inconsistencia_tecnica';
COMMENT ON COLUMN analise.inconsistencia_tecnica_documento.id_documento_administrativo IS 'Identificador que faz relacionamento com a tabela licenciamento.id_documento_administrativo';
COMMENT ON COLUMN analise.inconsistencia_tecnica_documento.id_documento_tecnico IS 'Identificador que faz relacionamento com a tabela licenciamento.id_documento_tecnico';

