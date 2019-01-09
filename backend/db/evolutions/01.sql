# --- !Ups


DROP SCHEMA IF EXISTS analise CASCADE;

CREATE SCHEMA analise;

GRANT ALL ON SCHEMA analise TO postgres;
GRANT USAGE ON SCHEMA analise TO licenciamento_am;



CREATE TABLE analise.tipo_documento
(
  id integer NOT NULL, -- Identificado único da entidade.
  nome character varying(500) NOT NULL, -- Nome do tipo de documento.
  caminho_modelo text, -- URL do modelo do documento para o usuário preencher.
  caminho_pasta text NOT NULL, -- Caminho da pasta onde os documentos deste tipo serão armazenados, relativo a pasta raiz de armazenamento dos documentos.
  prefixo_nome_arquivo text NOT NULL, -- Prefixo adicionado no nome do arquivo ao ser armazenado no disco para documentos deste tipo.
  CONSTRAINT pk_tipo_documento PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE analise.tipo_documento
  OWNER TO postgres;
GRANT ALL ON TABLE analise.tipo_documento TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.tipo_documento TO licenciamento_am;
COMMENT ON TABLE analise.tipo_documento
  IS 'Entidade responsavel por armazenar os possíveis tipos de documentos exigidos pelo analise.';
COMMENT ON COLUMN analise.tipo_documento.id IS 'Identificado único da entidade.';
COMMENT ON COLUMN analise.tipo_documento.nome IS 'Nome do tipo de documento.';
COMMENT ON COLUMN analise.tipo_documento.caminho_modelo IS 'URL do modelo do documento para o usuário preencher.';
COMMENT ON COLUMN analise.tipo_documento.caminho_pasta IS 'Caminho da pasta onde os documentos deste tipo serão armazenados, relativo a pasta raiz de armazenamento dos documentos.';
COMMENT ON COLUMN analise.tipo_documento.prefixo_nome_arquivo IS 'Prefixo adicionado no nome do arquivo ao ser armazenado no disco para documentos deste tipo.';



CREATE TABLE analise.documento
(
  id serial NOT NULL, -- Identificado único da entidade.
  id_tipo_documento integer NOT NULL, -- Identificador único da entidade tipo_documento que realizará o relacionamento entre documento e tipo_documento
  caminho text NOT NULL, -- Path do arquivo.
  data_cadastro date NOT NULL DEFAULT now(), -- Data em que o documento foi cadastrado na tabela.
  CONSTRAINT pk_documento PRIMARY KEY (id),
  CONSTRAINT fk_d_tipo_documento FOREIGN KEY (id_tipo_documento)
      REFERENCES analise.tipo_documento (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE analise.documento
  OWNER TO postgres;
GRANT ALL ON TABLE analise.documento TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.documento TO licenciamento_am;
GRANT SELECT, USAGE ON SEQUENCE analise.documento_id_seq TO licenciamento_am;
COMMENT ON TABLE analise.documento
  IS 'Entidade responsavel por armazenar aos documentos que passaram pela análise.';
COMMENT ON COLUMN analise.documento.id IS 'Identificado único da entidade.';
COMMENT ON COLUMN analise.documento.id_tipo_documento IS 'Identificador único da entidade tipo_documento que realizará o relacionamento entre documento e tipo_documento';
COMMENT ON COLUMN analise.documento.caminho IS 'Path do arquivo.';
COMMENT ON COLUMN analise.documento.data_cadastro IS 'Data em que o documento foi cadastrado na tabela.';

CREATE TABLE analise.processo(
id SERIAL NOT NULL,
numero TEXT NOT NULL,
id_empreendimento INTEGER NOT NULL,
id_objeto_tramitavel INTEGER,
CONSTRAINT pk_processo PRIMARY KEY(id),
CONSTRAINT fk_p_empreendimento FOREIGN KEY(id_empreendimento)
REFERENCES licenciamento.empreendimento(id)
);
ALTER TABLE analise.processo OWNER TO postgres;
GRANT ALL ON TABLE analise.processo TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.processo TO licenciamento_am;
GRANT SELECT, USAGE ON SEQUENCE analise.processo_id_seq TO licenciamento_am;


CREATE TABLE analise.analise(
id SERIAL NOT NULL,
id_processo INTEGER NOT NULL,
data_cadastro TIMESTAMP WITHOUT TIME ZONE NOT NULL,
data_vencimento_prazo DATE NOT NULL,
CONSTRAINT pk_analise PRIMARY KEY(id),
CONSTRAINT fk_a_processo FOREIGN KEY(id_processo)
REFERENCES analise.processo(id)
);
ALTER TABLE analise.analise OWNER TO postgres;
GRANT ALL ON TABLE analise.analise TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.analise TO licenciamento_am;
GRANT SELECT, USAGE ON SEQUENCE analise.analise_id_seq TO licenciamento_am;


CREATE TABLE analise.tipo_resultado_analise(
id SERIAL NOT NULL,
nome VARCHAR(200) NOT NULL,
CONSTRAINT pk_tipo_resultado_analise PRIMARY KEY(id)
);
ALTER TABLE analise.tipo_resultado_analise OWNER TO postgres;
GRANT ALL ON TABLE analise.tipo_resultado_analise TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.tipo_resultado_analise TO licenciamento_am;
GRANT SELECT, USAGE ON SEQUENCE analise.tipo_resultado_analise_id_seq TO licenciamento_am;





CREATE TABLE analise.analise_juridica(
id SERIAL NOT NULL,
id_analise INTEGER NOT NULL,
parecer TEXT NOT NULL,
data_vencimento_prazo DATE NOT NULL,
revisao_soilicitada BOOLEAN,
ativo BOOLEAN,
id_analise_juridica_revisada INTEGER,
data_inicio TIMESTAMP WITHOUT TIME ZONE,
data_fim TIMESTAMP WITHOUT TIME ZONE,
id_tipo_resultado INTEGER,
CONSTRAINT pk_analise_juridica PRIMARY KEY(id),
CONSTRAINT fk_aj_analise FOREIGN KEY(id_analise)
REFERENCES analise.analise(id),
CONSTRAINT fk_aj_analise_juridica FOREIGN KEY(id_analise_juridica_revisada)
REFERENCES analise.analise_juridica(id),
CONSTRAINT fk_aj_tipo_resultado_analise FOREIGN KEY(id_tipo_resultado)
REFERENCES analise.tipo_resultado_analise(id)
);
ALTER TABLE analise.analise_juridica OWNER TO postgres;
GRANT ALL ON TABLE analise.analise_juridica TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.analise_juridica TO licenciamento_am;
GRANT SELECT, USAGE ON SEQUENCE analise.analise_juridica_id_seq TO licenciamento_am;


CREATE TABLE analise.consultor_juridico(
id SERIAL NOT NULL,
id_analise_juridica INTEGER NOT NULL,
id_usuario INTEGER NOT NULL,
data_vinculacao TIMESTAMP WITHOUT TIME ZONE NOT NULL,
CONSTRAINT pk_consultor_juridico PRIMARY KEY(id),
CONSTRAINT fk_cj_analise_juridica FOREIGN KEY(id_analise_juridica)
REFERENCES analise.analise_juridica,
CONSTRAINT fk_cj_usuario FOREIGN KEY(id_usuario)
REFERENCES portal_seguranca.usuario
);
ALTER TABLE analise.processo OWNER TO postgres;
GRANT ALL ON TABLE analise.processo TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.processo TO licenciamento_am;
GRANT SELECT, USAGE ON SEQUENCE analise.processo_id_seq TO licenciamento_am;


CREATE TABLE analise.analise_documento(
id SERIAL NOT NULL,
id_analise_juridica INTEGER NOT NULL,
validado BOOLEAN,
parecer TEXT,
id_documento INTEGER NOT NULL,
CONSTRAINT pk_analise_documento PRIMARY KEY(id),
CONSTRAINT fk_ad_analise_juridica FOREIGN KEY(id_analise_juridica)
REFERENCES analise.analise_juridica(id),
CONSTRAINT fk_ad_documento FOREIGN KEY(id_documento)
REFERENCES licenciamento.documento(id)
);
ALTER TABLE analise.analise_documento OWNER TO postgres;
GRANT ALL ON TABLE analise.analise_documento TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.analise_documento TO licenciamento_am;
GRANT SELECT, USAGE ON SEQUENCE analise.analise_documento_id_seq TO licenciamento_am;


CREATE TABLE analise.rel_documento_analise_juridica(
id_documento INTEGER NOT NULL,
id_analise_juridica INTEGER NOT NULL,
CONSTRAINT pk_rel_documento_analise_juridica PRIMARY KEY(id_documento,id_analise_juridica),
CONSTRAINT fk_rdaj_documento FOREIGN KEY(id_documento)
REFERENCES analise.documento(id),
CONSTRAINT fk_rdaj_analise_juridica FOREIGN KEY(id_analise_juridica)
REFERENCES analise.analise_juridica(id)
);
ALTER TABLE analise.rel_documento_analise_juridica OWNER TO postgres;
GRANT ALL ON TABLE analise.rel_documento_analise_juridica TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.rel_documento_analise_juridica TO licenciamento_am;


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



# --- !Downs


DROP SCHEMA IF EXISTS analise CASCADE;

