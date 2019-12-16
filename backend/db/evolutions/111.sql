
# --- !Ups

CREATE TABLE analise.inconsistencia_tecnica (
  id serial NOT NULL,
  descricao_inconsistencia text NOT NULL, 
  tipo_inconsistencia text NOT NULL,
  id_analise_tecnica integer NOT NULL,
  CONSTRAINT pk_inconsistencia_tecnica PRIMARY KEY (id),
  CONSTRAINT fk_it_analise_tecnica FOREIGN KEY (id_analise_tecnica)
      REFERENCES analise.analise_tecnica (id) 
);
ALTER TABLE analise.inconsistencia_tecnica OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.inconsistencia_tecnica TO licenciamento_am;
GRANT SELECT ON TABLE analise.inconsistencia_tecnica TO tramitacao;
GRANT SELECT,USAGE ON SEQUENCE analise.inconsistencia_tecnica_id_seq TO licenciamento_am;
COMMENT ON TABLE analise.inconsistencia_tecnica IS 'Entidade responsável por armazenar as análises geo.';
COMMENT ON COLUMN analise.inconsistencia_tecnica.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.inconsistencia_tecnica.descricao_inconsistencia IS 'Campo que armazena a descrição da inconsistência relatada pelo analista.';
COMMENT ON COLUMN analise.inconsistencia_tecnica.tipo_inconsistencia IS 'Campo que armazena o tipo da inconsistência presente na análise.';



CREATE TABLE analise.inconsistencia_tecnica_atividade (
  id serial NOT NULL, 
  id_atividade_caracterizacao integer NOT NULL,
  id_inconsistencia_tecnica integer NOT NULL,
  CONSTRAINT pk_inconsistencia_tecnica_atividade PRIMARY KEY (id),
  CONSTRAINT fk_ita_inconsistencia_tecnica FOREIGN KEY (id_inconsistencia_tecnica)
      REFERENCES analise.inconsistencia_tecnica (id) 
);
ALTER TABLE analise.inconsistencia_tecnica_atividade OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.inconsistencia_tecnica TO licenciamento_am;
GRANT SELECT,USAGE ON SEQUENCE analise.inconsistencia_tecnica_atividade_id_seq TO licenciamento_am;
COMMENT ON TABLE analise.inconsistencia_tecnica_atividade IS 'Tabela responsável por armazenar todas informações das inconsistências do tipo ATIVIDADE adicionadas pelo analista técnico';
COMMENT ON COLUMN analise.inconsistencia_tecnica_atividade.id IS 'Identificador único da entidade';
COMMENT ON COLUMN analise.inconsistencia_tecnica_atividade.id_atividade_caracterizacao IS 'Identificador que faz o relacionamento com a tabela llicenciamento.atividade_caracterizacao';



CREATE TABLE analise.inconsistencia_tecnica_parametro (
  id serial NOT NULL, 
  id_inconsistencia_tecnica integer NOT NULL, 
  id_parametro integer NOT NULL,
  CONSTRAINT pk_inconsistencia_tecnica_parametro PRIMARY KEY (id),
  CONSTRAINT fk_itp_inconsistencia_tecnica FOREIGN KEY (id_inconsistencia_tecnica)
      REFERENCES analise.inconsistencia_tecnica (id)
);
ALTER TABLE analise.inconsistencia_tecnica_parametro OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.inconsistencia_tecnica_parametro TO licenciamento_am;
GRANT SELECT,USAGE ON SEQUENCE analise.inconsistencia_tecnica_parametro_id_seq TO licenciamento_am;
COMMENT ON TABLE analise.inconsistencia_tecnica_parametro IS 'Tabela responsável por relacionamento das tabelas';
COMMENT ON COLUMN analise.inconsistencia_tecnica_parametro.id IS 'Identificador único da entidade';
COMMENT ON COLUMN analise.inconsistencia_tecnica_parametro.id_inconsistencia_tecnica IS 'Identificador que faz relacionamento com a tabela inconsistencia_tecnica';
COMMENT ON COLUMN analise.inconsistencia_tecnica_parametro.id_parametro IS 'Identificador que faz o relacionamento com a tabela licenciamento.parametro_atividade';



CREATE TABLE analise.inconsistencia_tecnica_tipo_licenca (
  id serial NOT NULL,
  id_inconsistencia_tecnica integer NOT NULL, 
  id_tipo_licenca integer NOT NULL, 
  CONSTRAINT pk_inconsistencia_tecnica_tipo_licenca PRIMARY KEY (id),
  CONSTRAINT fk_ittl_inconsistencia_tecnica FOREIGN KEY (id_inconsistencia_tecnica)
      REFERENCES analise.inconsistencia_tecnica (id)
);
ALTER TABLE analise.inconsistencia_tecnica_tipo_licenca OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.inconsistencia_tecnica_tipo_licenca TO licenciamento_am;
GRANT SELECT,USAGE ON SEQUENCE analise.inconsistencia_tecnica_tipo_licenca_id_seq TO licenciamento_am;
COMMENT ON TABLE analise.inconsistencia_tecnica_tipo_licenca IS 'Tabela responsável por armazenar todas informações das inconsistências adicionadas pelo analista técnico com relaçao aos tipos de licença';
COMMENT ON COLUMN analise.inconsistencia_tecnica_tipo_licenca.id IS 'Identificador único da entidade';
COMMENT ON COLUMN analise.inconsistencia_tecnica_tipo_licenca.id_inconsistencia_tecnica IS 'Identificador que faz relacionamento com a tabela inconsistencia_tecnica';
COMMENT ON COLUMN analise.inconsistencia_tecnica_tipo_licenca.id_tipo_licenca IS 'Identificador que faz relacionamento com a tabela licenciamento.tipo_licenca';



CREATE TABLE analise.inconsistencia_tecnica_questionario (
  id serial NOT NULL,
  id_inconsistencia_tecnica integer NOT NULL, 
  id_questionario integer NOT NULL,
  CONSTRAINT pk_inconsistencia_tecnica_questionario PRIMARY KEY (id),
  CONSTRAINT fk_itq_inconsistencia_tecnica FOREIGN KEY (id_inconsistencia_tecnica)
      REFERENCES analise.inconsistencia_tecnica (id)
);
ALTER TABLE analise.inconsistencia_tecnica_questionario OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.inconsistencia_tecnica_questionario TO licenciamento_am;
GRANT SELECT,USAGE ON SEQUENCE analise.inconsistencia_tecnica_questionario_id_seq TO licenciamento_am;
COMMENT ON TABLE analise.inconsistencia_tecnica_questionario IS 'Tabela responsável por armazenar todas informações das inconsistências adicionadas pelo analista técnico com relaçao ao questionário';
COMMENT ON COLUMN analise.inconsistencia_tecnica_questionario.id IS 'Identificador único da entidade';
COMMENT ON COLUMN analise.inconsistencia_tecnica_questionario.id_inconsistencia_tecnica IS 'Identificador que faz relacionamento com a tabela inconsistencia_tecnica';
COMMENT ON COLUMN analise.inconsistencia_tecnica_questionario.id_questionario IS 'Identificador que faz relacionamento com a tabela licenciamento.questionario';



CREATE TABLE analise.inconsistencia_tecnica_documento (
  id serial NOT NULL,
  id_inconsistencia_tecnica integer NOT NULL,
  id_documento_administrativo integer,
  id_documento_tecnico integer,
  CONSTRAINT pk_inconsistencia_tecnica_documento PRIMARY KEY (id),
  CONSTRAINT fk_itd_inconsistencia_tecnica FOREIGN KEY (id_inconsistencia_tecnica)
      REFERENCES analise.inconsistencia_tecnica (id)
);
ALTER TABLE analise.inconsistencia_tecnica_documento OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.inconsistencia_tecnica_documento TO licenciamento_am;
GRANT SELECT,USAGE ON SEQUENCE analise.inconsistencia_tecnica_documento_id_seq TO licenciamento_am;
COMMENT ON TABLE analise.inconsistencia_tecnica_documento IS 'Tabela responsável por armazenar todas informações das inconsistências adicionadas pelo analista técnico com relaçao aos documentos';
COMMENT ON COLUMN analise.inconsistencia_tecnica_documento.id IS 'Identificador único da entidade';
COMMENT ON COLUMN analise.inconsistencia_tecnica_documento.id_inconsistencia_tecnica IS 'Identificador que faz relacionamento com a tabela inconsistencia_tecnica';
COMMENT ON COLUMN analise.inconsistencia_tecnica_documento.id_documento_administrativo IS 'Identificador que faz relacionamento com a tabela licenciamento.id_documento_administrativo';
COMMENT ON COLUMN analise.inconsistencia_tecnica_documento.id_documento_tecnico IS 'Identificador que faz relacionamento com a tabela licenciamento.id_documento_tecnico';




CREATE TABLE analise.rel_documento_inconsistencia_tecnica (
  id_documento integer NOT NULL,
  id_inconsistencia_tecnica integer NOT NULL,
  CONSTRAINT pk_rel_documento_inconsistencia_tecnica PRIMARY KEY (id_documento, id_inconsistencia_tecnica),
  CONSTRAINT fk_rdit_documento FOREIGN KEY (id_documento)
      REFERENCES analise.documento (id),
  CONSTRAINT fk_rdi_inconsistencia_tecnica FOREIGN KEY (id_inconsistencia_tecnica)
      REFERENCES analise.inconsistencia_tecnica (id) 
);
ALTER TABLE analise.rel_documento_inconsistencia_tecnica OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.rel_documento_inconsistencia_tecnica TO licenciamento_am;
GRANT SELECT ON TABLE analise.rel_documento_inconsistencia_tecnica TO tramitacao;



INSERT INTO analise.tipo_documento(id, nome, caminho_pasta, prefixo_nome_arquivo) 
  VALUES(26, 'Documento inconsistência técnica', 'documento_inconsistencia_tecnica', 'documento_inconsistencia_tecnica' );


# --- !Downs

DELETE FROM analise.tipo_documento WHERE id = 26;

DROP TABLE analise.rel_documento_inconsistencia_tecnica;

DROP TABLE analise.inconsistencia_tecnica_documento;

DROP TABLE analise.inconsistencia_tecnica_questionario;

DROP TABLE analise.inconsistencia_tecnica_tipo_licenca;

DROP TABLE analise.inconsistencia_tecnica_parametro;

DROP TABLE analise.inconsistencia_tecnica_atividade;

DROP TABLE analise.inconsistencia_tecnica;


