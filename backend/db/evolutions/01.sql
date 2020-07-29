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
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.rel_processo_caracterizacao TO licenciamento_ap;



--										02.sql

ALTER TABLE analise.analise_juridica ALTER COLUMN parecer DROP NOT NULL;
ALTER TABLE analise.analise_juridica RENAME COLUMN revisao_soilicitada TO revisao_solicitada;
ALTER TABLE analise.analise_juridica ALTER COLUMN revisao_solicitada SET DEFAULT FALSE;
ALTER TABLE analise.analise_juridica ALTER COLUMN ativo SET DEFAULT TRUE;

--										03.sql


COMMENT ON TABLE analise.processo IS 'Entidade responsável por armazenar um processo em análise.';
COMMENT ON COLUMN analise.processo.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.processo.numero IS 'Número do processo.';
COMMENT ON COLUMN analise.processo.id_empreendimento IS 'Identificador da tabela empreendimento, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.processo.id_objeto_tramitavel IS 'Identificador da tabela objeto_tramitavel, responsável pelo relacionamento entre as duas tabelas.';

COMMENT ON TABLE analise.analise IS 'Entidade responsável por armazenar as análises referente ao processo.';
COMMENT ON COLUMN analise.analise.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.analise.id_processo IS 'Identificador da tabela processo, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.analise.data_cadastro IS 'Data de cadastro da análise.';
COMMENT ON COLUMN analise.analise.data_vencimento_prazo IS 'Data de vencimento da análise.';

COMMENT ON TABLE analise.tipo_resultado_analise IS 'Entidade responsável por armazenar os tipos de possíveis resultados da análise.';
COMMENT ON COLUMN analise.tipo_resultado_analise.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.tipo_resultado_analise.nome IS 'Nomo do tipo de resultado.';

COMMENT ON TABLE analise.analise_juridica IS 'Entidade responsável por armazenar as análises jurídicas.';
COMMENT ON COLUMN analise.analise_juridica.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.analise_juridica.id_analise IS 'Identificador da tabela analise, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.analise_juridica.parecer IS 'Parecer da análise jurídica.';
COMMENT ON COLUMN analise.analise_juridica.data_vencimento_prazo IS 'Data de vencimento do prazo da análise.';
COMMENT ON COLUMN analise.analise_juridica.revisao_solicitada IS 'Flag que indica se esta análise é uma revisão.';
COMMENT ON COLUMN analise.analise_juridica.ativo IS 'Indica se a análise ainda está ativa.';
COMMENT ON COLUMN analise.analise_juridica.id_analise_juridica_revisada IS 'Identificador da tabela analise_juridica, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.analise_juridica.data_inicio IS 'Data de início da análise.';
COMMENT ON COLUMN analise.analise_juridica.data_fim IS 'Data de fim de análise.';
COMMENT ON COLUMN analise.analise_juridica.id_tipo_resultado IS 'Identificador da tabela tipo_resultado, responsável pelo relacionamento entre as duas tabelas.';

COMMENT ON TABLE analise.consultor_juridico IS 'Entidade responsável por armazenar, os usuários que são consultores jurídicos em uma análise juridica.';
COMMENT ON COLUMN analise.consultor_juridico.id IS 'Identificador único da tabela consultor jurídico.';
COMMENT ON COLUMN analise.consultor_juridico.id_analise_juridica IS 'Identificador da tabela analise_juridica, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.consultor_juridico.id_usuario IS 'Identificador da tabela usuario, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.consultor_juridico.data_vinculacao IS 'Data em que foi vinculado a análise.';

COMMENT ON TABLE analise.analise_documento IS 'Entidade responsável por armazenar os documentos que estão sendo analisados.';
COMMENT ON COLUMN analise.analise_documento.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.analise_documento.id_analise_juridica IS 'Identificador da tabela analise_juridica, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.analise_documento.validado IS 'Flag que indica se o documento foi validado.';
COMMENT ON COLUMN analise.analise_documento.parecer IS 'Parecer da análise de documento.';
COMMENT ON COLUMN analise.analise_documento.id_documento IS 'Identificador da tabela documento, responsável pelo relacionamento entre as duas tabelas.';

COMMENT ON TABLE analise.rel_documento_analise_juridica IS 'Entidade responsável por armazenar a relação entre as entidades documento e análise jurídica.';
COMMENT ON COLUMN analise.rel_documento_analise_juridica.id_documento IS 'Identificador da tabela documento, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.rel_documento_analise_juridica.id_analise_juridica IS 'Identificador da tabela analise_juridica, responsável pelo relacionamento entre as duas tabelas.';

COMMENT ON TABLE analise.rel_processo_caracterizacao IS 'Entidade responsável por armazenar a relação entre as entidades processo e caracterização.';
COMMENT ON COLUMN analise.rel_processo_caracterizacao.id_caracterizacao IS 'Identificador da tabela caracterizacao, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.rel_processo_caracterizacao.id_processo IS 'Identificador da tabela processo, responsável pelo relacionamento entre as duas tabelas.';


--										04.sql

ALTER TABLE analise.processo ADD COLUMN data_cadastro TIMESTAMP WITHOUT TIME ZONE NOT NULL; -- Data de cadastro do processo.
COMMENT ON COLUMN analise.processo.data_cadastro IS 'Data de cadastro do processo.';

--										05.sql

ALTER TABLE analise.analise ADD COLUMN ativo Boolean DEFAULT true;

COMMENT ON COLUMN analise.analise.ativo IS 'Indica se a analise está ativa ou não.';

--										06.sql

INSERT INTO analise.tipo_documento (id,nome,caminho_modelo,caminho_pasta,prefixo_nome_arquivo) VALUES
(1,'Documento análise jurídica',NULL,'documento_analise_juridica','documento_analise_juridica'),
(2,'Documento análise técnica',NULL,'documento_analise_tecnica','documento_analise_tecnica');

--										07.sql


CREATE TABLE analise.geoserver
(
id SERIAL NOT NULL,
url_getcapabilities VARCHAR(250) NOT NULL,
CONSTRAINT pk_geoserver PRIMARY KEY(id)
);
GRANT SELECT,INSERT,UPDATE,DELETE ON analise.geoserver TO licenciamento_ap;
GRANT SELECT,USAGE ON analise.geoserver_id_seq TO licenciamento_ap;

COMMENT ON TABLE analise.geoserver IS 'Entidade responsável por armazenar os endereços dos geoserver';
COMMENT ON COLUMN analise.geoserver.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.geoserver.url_getcapabilities IS 'Url onde pega o getcapabilities do geoserver para criar conexao.';



CREATE TABLE analise.configuracao_layer
(
id SERIAL NOT NULL,
atributo_descricao VARCHAR(100) NOT NULL,
nome_layer VARCHAR(100) NOT NULL,
descricao VARCHAR(100) NOT NULL,
buffer NUMERIC(5),
id_geoserver INTEGER,
CONSTRAINT pk_configuracao_layer PRIMARY KEY(id),
CONSTRAINT fk_cl_geoserver FOREIGN KEY(id_geoserver)
REFERENCES analise.geoserver(id)
);
GRANT SELECT,INSERT,UPDATE,DELETE ON analise.configuracao_layer TO licenciamento_ap;
GRANT SELECT,USAGE ON analise.configuracao_layer_id_seq TO licenciamento_ap;

COMMENT ON TABLE analise.configuracao_layer IS 'Entidade responsável por armazenar as configurações das layers que serão utilizadas para cacular regras de analise';
COMMENT ON COLUMN analise.configuracao_layer.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.configuracao_layer.atributo_descricao IS 'Atributo onde será buscado informações sobre a layer.';
COMMENT ON COLUMN analise.configuracao_layer.nome_layer IS 'Nome da configuração.';
COMMENT ON COLUMN analise.configuracao_layer.descricao IS 'Descrição da configuração.';
COMMENT ON COLUMN analise.configuracao_layer.buffer IS 'Buffer em metros.';
COMMENT ON COLUMN analise.configuracao_layer.id_geoserver IS 'Identificador da entidade geoserver que faz o relacionamento entre as duas entidades.';


--										08.sql

INSERT INTO analise.tipo_resultado_analise(id,nome) VALUES
(1,'Deferido'),
(2,'Indeferido'),
(3,'Emitir notificação');

--										09.sql

INSERT INTO analise.tipo_resultado_analise(id,nome) VALUES
(4,'Parecer validado'),
(5,'Solicitar ajustes'),
(6,'Parecer não validado');


ALTER TABLE analise.analise_juridica RENAME id_tipo_resultado TO id_tipo_resultado_analise;
COMMENT ON COLUMN analise.analise_juridica.id_tipo_resultado_analise IS 'Campo responsavel por armazenar o resultado da análise do consultor jurídico.';

ALTER TABLE analise.analise_juridica ADD COLUMN id_tipo_resultado_validacao INTEGER;
ALTER TABLE analise.analise_juridica ADD CONSTRAINT fk_aj_tipo_resultado_validacao FOREIGN KEY(id_tipo_resultado_validacao)
REFERENCES analise.tipo_resultado_analise;
COMMENT ON COLUMN analise.analise_juridica.id_tipo_resultado_validacao IS 'Campo responsavel por armazenar o resultado da análise do coordenador jurídico';

--                    10.sql
CREATE TABLE analise.analise_tecnica (
 id serial NOT NULL,
 id_analise integer NOT NULL, 
 parecer text, 
 data_vencimento_prazo date NOT NULL, 
 revisao_solicitada boolean DEFAULT false, 
 ativo boolean DEFAULT true,
 id_analise_tecnica_revisada integer,
 data_inicio timestamp without time zone, 
 data_fim timestamp without time zone,
 id_tipo_resultado_analise integer, 
 id_tipo_resultado_validacao integer,
 CONSTRAINT pk_analise_tecnica PRIMARY KEY (id),
 CONSTRAINT fk_at_analise FOREIGN KEY (id_analise)
 REFERENCES analise.analise(id),
 CONSTRAINT fk_at_analise_juridica FOREIGN KEY (id_analise_tecnica_revisada)
 REFERENCES analise.analise_juridica (id),
 CONSTRAINT fk_at_tipo_resultado_analise FOREIGN KEY (id_tipo_resultado_analise)
 REFERENCES analise.tipo_resultado_analise(id),
 CONSTRAINT fk_at_tipo_resultado_validacao FOREIGN KEY (id_tipo_resultado_validacao)
 REFERENCES analise.tipo_resultado_analise(id)
);
GRANT INSERT, SELECT,UPDATE,DELETE ON TABLE analise.analise_tecnica TO licenciamento_ap;
GRANT SELECT,USAGE ON SEQUENCE analise.analise_tecnica_id_seq TO licenciamento_ap;
ALTER TABLE analise.analise_tecnica OWNER TO postgres;



CREATE TABLE analise.licenca_analise(
 id SERIAL NOT NULL,
 id_analise_tecnica INTEGER NOT NULL,
 validade INTEGER NOT NULL,
 id_licenca INTEGER NOT NULL,
 observacao TEXT,
 CONSTRAINT pk_licenca_analise PRIMARY KEY(id),
 CONSTRAINT fk_la_analise_tecnica FOREIGN KEY(id_analise_tecnica)
 REFERENCES analise.analise_tecnica(id),
 CONSTRAINT fk_la_licenca FOREIGN KEY(id_licenca)
 REFERENCES licenciamento.licenca(id)
 
);
GRANT INSERT, SELECT,UPDATE,DELETE ON TABLE analise.licenca_analise TO licenciamento_ap;
GRANT SELECT,USAGE ON SEQUENCE analise.licenca_analise_id_seq TO licenciamento_ap;
ALTER TABLE analise.licenca_analise OWNER TO postgres;


CREATE TABLE analise.condicionante(
 id SERIAL NOT NULL,
 id_licenca_analise INTEGER NOT NULL,
 texto TEXT NOT NULL,
 prazo INTEGER NOT NULL,
 ordem INTEGER NOT NULL,
 CONSTRAINT pk_condicionante PRIMARY KEY(id),
 CONSTRAINT fk_c_licenca_analise FOREIGN KEY(id_licenca_analise)
 REFERENCES analise.licenca_analise(id) 
);
GRANT INSERT, SELECT,UPDATE,DELETE ON TABLE analise.condicionante TO licenciamento_ap;
GRANT SELECT,USAGE ON SEQUENCE analise.condicionante_id_seq TO licenciamento_ap;
ALTER TABLE analise.condicionante OWNER TO postgres;



CREATE TABLE analise.recomendacao (
 id SERIAL NOT NULL,
 id_licenca_analise INTEGER NOT NULL,
 texto TEXT NOT NULL,
 ordem INTEGER NOT NULL,
 CONSTRAINT pk_recomendacao PRIMARY KEY(id),
 CONSTRAINT fk_r_licenca_analise FOREIGN KEY(id_licenca_analise)
 REFERENCES analise.licenca_analise(id)
);
GRANT INSERT, SELECT,UPDATE,DELETE ON TABLE analise.recomendacao TO licenciamento_ap;
GRANT SELECT,USAGE ON SEQUENCE analise.recomendacao_id_seq TO licenciamento_ap;
ALTER TABLE analise.recomendacao OWNER TO postgres;


CREATE TABLE analise.parecer_tecnico_restricao(
 id SERIAL NOT NULL,
 id_analise_tecnica INTEGER NOT NULL,
 codigo_camada TEXT NOT NULL,
 parecer TEXT NOT NULL,
 CONSTRAINT pk_parecer_tecnico_restricoes PRIMARY KEY(id),
 CONSTRAINT fk_ptr_analise_tecnica FOREIGN KEY(id_analise_tecnica)
 REFERENCES analise.analise_tecnica(id)
);
GRANT INSERT, SELECT,UPDATE,DELETE ON TABLE analise.parecer_tecnico_restricao TO licenciamento_ap;
GRANT SELECT,USAGE ON SEQUENCE analise.parecer_tecnico_restricao_id_seq TO licenciamento_ap;
ALTER TABLE analise.parecer_tecnico_restricao OWNER TO postgres;

CREATE TABLE analise.analista_tecnico(
 id SERIAL NOT NULL,
 id_analise_tecnica INTEGER NOT NULL,
 id_usuario INTEGER NOT NULL,
 data_vinculacao TIMESTAMP WITHOUT TIME ZONE,
 CONSTRAINT pk_analista_tecnico PRIMARY KEY(id),
 CONSTRAINT fk_at_analise_tecnica FOREIGN KEY(id_analise_tecnica)
 REFERENCES analise.analise_tecnica(id)

);
GRANT INSERT, SELECT,UPDATE,DELETE ON TABLE analise.analista_tecnico TO licenciamento_ap;
GRANT SELECT,USAGE ON SEQUENCE analise.analista_tecnico_id_seq TO licenciamento_ap;
ALTER TABLE analise.analista_tecnico OWNER TO postgres;

CREATE TABLE analise.rel_documento_analise_tecnica(
 id_documento INTEGER NOT NULL,
 id_analise_tecnica INTEGER NOT NULL,
 CONSTRAINT pk_rel_documento_analise_tecnica PRIMARY KEY(id_documento,id_analise_tecnica),
 CONSTRAINT fk_rdat_documento FOREIGN KEY(id_documento)
 REFERENCES analise.documento(id),
 CONSTRAINT fk_rdat_analise_tecnica FOREIGN KEY(id_analise_tecnica)
 REFERENCES analise.analise_tecnica(id)
);
GRANT INSERT, SELECT,UPDATE,DELETE ON TABLE analise.rel_documento_analise_tecnica TO licenciamento_ap;
ALTER TABLE analise.rel_documento_analise_tecnica OWNER TO postgres;

ALTER TABLE analise.analise_documento ADD COLUMN id_analise_tecnica INTEGER;
ALTER TABLE analise.analise_documento ADD CONSTRAINT fk_ad_analise_tecnica FOREIGN KEY(id_analise_tecnica)
REFERENCES analise.analise_tecnica(id);

--										11.sql

ALTER TABLE analise.analise_juridica ADD COLUMN parecer_validacao TEXT;
COMMENT ON COLUMN analise.analise_juridica.parecer_validacao IS 'Parecer da validação da análise jurdica';

--										12.sql

ALTER TABLE analise.analise_documento ALTER COLUMN id_analise_juridica DROP NOT NULL;

ALTER TABLE analise.analise_tecnica ADD parecer_validacao TEXT;

COMMENT ON COLUMN analise.analise_tecnica.parecer_validacao IS 'Parecer da validação da análise técnica.';

--										13.sql

ALTER TABLE analise.analise_tecnica DROP CONSTRAINT fk_at_analise_juridica;
ALTER TABLE analise.analise_tecnica ADD CONSTRAINT fk_at_analise_tecnica FOREIGN KEY (id_analise_tecnica_revisada)
REFERENCES analise.analise_tecnica(id);

--										14.sql

ALTER TABLE analise.licenca_analise DROP COLUMN id_licenca; 

ALTER TABLE analise.licenca_analise ADD id_caracterizacao integer NOT NULL;

ALTER TABLE analise.licenca_analise ADD
  CONSTRAINT fk_la_caracterizacao FOREIGN KEY (id_caracterizacao)
      REFERENCES licenciamento.caracterizacao (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
--										15.sql

ALTER TABLE analise.licenca_analise ADD COLUMN emitir BOOLEAN;
COMMENT ON COLUMN analise.licenca_analise.emitir IS 'Flag que indica se irá emitir a licença(True: emite, False: não emite e Null: aguardando ação do usuário).';

--										16.sql

INSERT INTO analise.geoserver (id, url_getcapabilities) 
VALUES (1, 'http://car.semas.pa.gov.br/geoserver/wfs?REQUEST=GetCapabilities&version=1.1.0');

INSERT INTO analise.configuracao_layer (atributo_descricao, buffer, descricao, id_geoserver, nome_layer) 
VALUES ('nome_1', 10000, 'Unidade de conservação', 1, 'secar-pa:unidade_conservacao');
INSERT INTO analise.configuracao_layer (atributo_descricao, buffer, descricao, id_geoserver, nome_layer) 
VALUES ('terrai_nom', 10000, 'Terras indígenas', 1, 'secar-pa:terra_indigena');
INSERT INTO analise.configuracao_layer (atributo_descricao, buffer, descricao, id_geoserver, nome_layer) 
VALUES ('name', 10000, 'Terras quilombolas', 1, 'base_referencia:vw_mzee_terras_quilombolas');
INSERT INTO analise.configuracao_layer (atributo_descricao, buffer, descricao, id_geoserver, nome_layer) 
VALUES ('name', 10000, 'Forças armadas', 1, 'base_referencia:vw_mzee_area_forcas_armadas');

--										17.sql


ALTER TABLE analise.analise_juridica ADD COLUMN id_usuario_validacao INTEGER;
COMMENT ON COLUMN analise.analise_juridica.id_usuario_validacao IS 'Identificador da entidade portal_seguranca.usuario que realizará o relacionamento entre as duas entidades.';

ALTER TABLE analise.analise_tecnica ADD COLUMN id_usuario_validacao INTEGER;
COMMENT ON COLUMN analise.analise_tecnica.id_usuario_validacao IS 'Identificador da entidade portal_seguranca.usuario que realizará o relacionamento entre as duas entidades.';


--										18.sql

COMMENT ON TABLE analise.analise_tecnica IS 'Entidade responsável por armazenar as análises técnicas.';
COMMENT ON COLUMN analise.analise_tecnica.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.analise_tecnica.id_analise IS 'Identificador da tabela analise, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.analise_tecnica.parecer IS 'Parecer da análise técnica.';
COMMENT ON COLUMN analise.analise_tecnica.data_vencimento_prazo IS 'Data de vencimento do prazo da análise.';
COMMENT ON COLUMN analise.analise_tecnica.revisao_solicitada IS 'Flag que indica se esta análise é uma revisão.';
COMMENT ON COLUMN analise.analise_tecnica.ativo IS 'Indica se a análise ainda está ativa.';
COMMENT ON COLUMN analise.analise_tecnica.id_analise_tecnica_revisada IS 'Identificador da tabela analise_tecnica, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.analise_tecnica.data_inicio IS 'Data de início da análise.';
COMMENT ON COLUMN analise.analise_tecnica.data_fim IS 'Data de fim de análise.';
COMMENT ON COLUMN analise.analise_tecnica.id_tipo_resultado_analise IS 'Campo responsavel por armazenar o resultado da análise do consultor técnico.';
COMMENT ON COLUMN analise.analise_tecnica.id_tipo_resultado_validacao IS 'Campo responsavel por armazenar o resultado da análise do coordenador técnico.';

COMMENT ON TABLE analise.licenca_analise IS 'Entidade responsável por armazenar as licenças que estão em análise.';
COMMENT ON COLUMN analise.licenca_analise.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.licenca_analise.id_analise_tecnica IS 'Identificador da entidade analise_tecnica que faz o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.licenca_analise.validade IS 'Validade da licença em dias.';
COMMENT ON COLUMN analise.licenca_analise.id_caracterizacao IS 'Identificador da caracterizacao que faz o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.licenca_analise.observacao IS 'Descrição da observação.';


COMMENT ON TABLE analise.condicionante IS 'Entidade responsável por armazenar as condicionantes das licença de análise.';
COMMENT ON COLUMN analise.condicionante.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.condicionante.id_licenca_analise IS 'Identificador da entidade licenca_analise que faz o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.condicionante.texto IS 'Descrição da condicionante.';
COMMENT ON COLUMN analise.condicionante.prazo IS 'Prazo em dias da condicionante.';
COMMENT ON COLUMN analise.condicionante.ordem IS 'Ordem de exibição das condicionantes.';


COMMENT ON TABLE analise.recomendacao IS 'Entidade responsável por armazenar as recomendações da licença de análise.';
COMMENT ON COLUMN analise.recomendacao.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.recomendacao.id_licenca_analise IS 'Identificador da entidade licenca_analise que faz o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.recomendacao.texto IS 'Descrição da recomendação.';
COMMENT ON COLUMN analise.recomendacao.ordem IS 'Ordem de exibição das recomendações.';


COMMENT ON TABLE analise.parecer_tecnico_restricao IS 'Entidade responsável por armazenar o parecer das restrições geográficas.';
COMMENT ON COLUMN analise.parecer_tecnico_restricao.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.parecer_tecnico_restricao.id_analise_tecnica IS 'Identificador da entidade analise_tecnica que faz o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.parecer_tecnico_restricao.codigo_camada IS 'Código da camada do geoserver.';
COMMENT ON COLUMN analise.parecer_tecnico_restricao.parecer IS 'Descrição do parecer de restrição geográfico.';

COMMENT ON TABLE analise.analista_tecnico IS 'Entidade responsável por armazenar o analista da analise técnica.';
COMMENT ON COLUMN analise.analista_tecnico.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.analista_tecnico.id_analise_tecnica IS 'Identificador da entidade análise que faz o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.analista_tecnico.id_usuario IS 'Identificador da entidade usuario que faz o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.analista_tecnico.data_vinculacao IS 'Data em que o usuario foi vinculado.';

COMMENT ON TABLE analise.rel_documento_analise_tecnica IS 'Entidade responsável por armazenar o relacionamente entre as entidades documento e analise técnica.';
COMMENT ON COLUMN analise.rel_documento_analise_tecnica.id_documento IS 'Identificador da entidade documento.';
COMMENT ON COLUMN analise.rel_documento_analise_tecnica.id_analise_tecnica IS 'Identificador da entidade analise_tecnica.';

--										19.sql

UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 101 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 102 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 103 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 104 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 105 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 106 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 107 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 108 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 109 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 110 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 111 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 112 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 113 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 114 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 115 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 116 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 117 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 118 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 119 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 120 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 121 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 122 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 123 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 124 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 125 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 126 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 127 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 128 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 129 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 130 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 131 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 132 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 133 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 134 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 135 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 136 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 137 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 138 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 139 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 140 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 141 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 142 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 143 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 144 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 145 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 146 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 147 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 148 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 149 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 150 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 151 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 152 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 153 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 154 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 155 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 156 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 157 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 158 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 159 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 160 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 161 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 162 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 163 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 164 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 165 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 166 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 168 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 169 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 170 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 171 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 172 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 173 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 174 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 175 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 0 WHERE id= 177 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 178 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 179 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 180 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 181 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 182 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 183 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 184 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 186 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 187 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 188 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 189 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 190 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 191 ;
UPDATE licenciamento.tipo_documento SET tipo_analise= 1 WHERE id= 192 ;

--                    20.sql

CREATE TABLE analise.gerente_tecnico
(
  id serial NOT NULL,
  id_analise_tecnica integer NOT NULL,
  id_usuario integer NOT NULL,
  data_vinculacao timestamp without time zone,
  CONSTRAINT pk_gerente_tecnico PRIMARY KEY (id),
  CONSTRAINT fk_gt_analise_tecnica FOREIGN KEY (id_analise_tecnica)REFERENCES analise.analise_tecnica (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
  );

GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.gerente_tecnico TO licenciamento_ap;
GRANT SELECT, USAGE ON SEQUENCE analise.gerente_tecnico_id_seq TO licenciamento_ap;

COMMENT ON TABLE analise.gerente_tecnico IS 'Entidade responsável por armazenar o Gerente responsável pela análise técnica.';
COMMENT ON COLUMN analise.gerente_tecnico.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.gerente_tecnico.id_analise_tecnica IS 'Identificador da entidade analise_tecnica que realiza o relacionamento entre as entidades gerente_tecnico e analise_tecnica.';
COMMENT ON COLUMN analise.gerente_tecnico.id_usuario IS 'Identificador da entidade portal_seguranca.usuario que realiza o relacionamento entre as entidades gerente_tecnico e portal_seguranca.usuario.';
COMMENT ON COLUMN analise.gerente_tecnico.data_vinculacao IS 'Data em que o usuário foi vinculado a análise técnica.';

--										21.sql

ALTER TABLE analise.analise_tecnica ADD justificativa_coordenador TEXT;

COMMENT ON COLUMN analise.analise_tecnica.parecer_validacao IS 'Campo responsável por armazenar a justificativa do coordenador quando o mesmo vincular diretamente um analista técnico.';

--										22.sql

ALTER TABLE analise.analise_tecnica ADD id_tipo_resultado_validacao_gerente integer;
ALTER TABLE analise.analise_tecnica ADD parecer_validacao_gerente text;
ALTER TABLE analise.analise_tecnica ADD id_usuario_validacao_gerente integer;

ALTER TABLE analise.analise_tecnica ADD 
  CONSTRAINT fk_at_tipo_resultado_validacao_gerente FOREIGN KEY (id_tipo_resultado_validacao_gerente)
      REFERENCES analise.tipo_resultado_analise (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

COMMENT ON COLUMN analise.analise_tecnica.id_tipo_resultado_validacao_gerente IS 'Campo responsavel por armazenar o resultado da análise do gerente técnico.';
COMMENT ON COLUMN analise.analise_tecnica.id_usuario_validacao_gerente IS 'Campo responsável por armazernar o gerente que fez a validação.';
COMMENT ON COLUMN analise.analise_tecnica.parecer_validacao_gerente IS 'Campo responsável por armazenar a descrição da validação do gerente técnico.';


--										23.sql

ALTER TABLE analise.analise_tecnica ADD COLUMN data_cadastro TIMESTAMP WITHOUT TIME ZONE;
COMMENT ON COLUMN analise.analise_tecnica.data_cadastro IS 'Data de cadastro da análise.';

UPDATE analise.analise_tecnica SET data_cadastro=(data_vencimento_prazo-10);

ALTER TABLE analise.analise_tecnica ALTER COLUMN data_cadastro SET NOT NULL;

--										24.sql

ALTER TABLE analise.analise_tecnica ADD id_tipo_resultado_validacao_aprovador integer;
ALTER TABLE analise.analise_tecnica ADD parecer_validacao_aprovador text;
ALTER TABLE analise.analise_tecnica ADD id_usuario_validacao_aprovador integer;

ALTER TABLE analise.analise_tecnica ADD 
CONSTRAINT fk_at_tipo_resultado_validacao_aprovador FOREIGN KEY (id_tipo_resultado_validacao_aprovador)
REFERENCES analise.tipo_resultado_analise (id) MATCH SIMPLE
ON UPDATE NO ACTION ON DELETE NO ACTION;


COMMENT ON COLUMN analise.analise_tecnica.id_tipo_resultado_validacao_aprovador IS 'Campo responsavel por armazenar o resultado da análise do aprovador.';
COMMENT ON COLUMN analise.analise_tecnica.id_usuario_validacao_aprovador IS 'Campo responsável por armazernar o aprovador que fez a validação.';
COMMENT ON COLUMN analise.analise_tecnica.parecer_validacao_aprovador IS 'Campo responsável por armazenar a descrição da validação do aprovador.';

ALTER TABLE analise.analise_juridica ADD id_tipo_resultado_validacao_aprovador integer;
ALTER TABLE analise.analise_juridica ADD parecer_validacao_aprovador text;
ALTER TABLE analise.analise_juridica ADD id_usuario_validacao_aprovador integer;

ALTER TABLE analise.analise_juridica ADD 
CONSTRAINT fk_aj_tipo_resultado_validacao_aprovador FOREIGN KEY (id_tipo_resultado_validacao_aprovador)
REFERENCES analise.tipo_resultado_analise (id) MATCH SIMPLE
ON UPDATE NO ACTION ON DELETE NO ACTION;


COMMENT ON COLUMN analise.analise_juridica.id_tipo_resultado_validacao_aprovador IS 'Campo responsavel por armazenar o resultado da análise do aprovador.';
COMMENT ON COLUMN analise.analise_juridica.id_usuario_validacao_aprovador IS 'Campo responsável por armazernar o aprovador que fez a validação.';
COMMENT ON COLUMN analise.analise_juridica.parecer_validacao_aprovador IS 'Campo responsável por armazenar a descrição da validação do aprovador.';



# --- !Downs


DROP SCHEMA IF EXISTS analise CASCADE;


-- Criação da tabela analise_manejo

CREATE TABLE analise.analise_manejo (
 id SERIAL NOT NULL,
 data TIMESTAMP NOT NULL,
 dias_analise INTEGER NOT NULL,
 path_arquivo_shape VARCHAR(500) NOT NULL,
 path_anexo VARCHAR(500),
 analise_temporal TEXT NOT NULL,
 area_manejo_florestal_solicitada DOUBLE PRECISION NOT NULL,
 area_preservacao_permanente DOUBLE PRECISION,
 area_servidao DOUBLE PRECISION,
 area_antropizada_nao_consolidada DOUBLE PRECISION,
 area_consolidada DOUBLE PRECISION,
 area_uso_restrito DOUBLE PRECISION,
 area_sem_potencial DOUBLE PRECISION,
 area_corpos_agua DOUBLE PRECISION,
 area_embargada_ibama DOUBLE PRECISION,
 area_embargada_ldi DOUBLE PRECISION,
 area_seletiva_ndfi DOUBLE PRECISION,
 area_efetivo_manejo DOUBLE PRECISION,
 area_com_exploraca_ndfi_baixo DOUBLE PRECISION,
 area_com_exploraca_ndfi_medio DOUBLE PRECISION,
 area_sem_previa_exploracao DOUBLE PRECISION,
 consideracoes TEXT NOT NULL,
 conclusao TEXT NOT NULL,
 id_usuario INTEGER NOT NULL,
 CONSTRAINT pk_analise_manejo PRIMARY KEY(id)
);

COMMENT ON TABLE analise.analise_manejo IS 'Entidade responsável por armazenas uma análise de manejo.';
COMMENT ON COLUMN analise.analise_manejo.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.analise_manejo.dias_analise IS 'Quantidade de dias corridos desde o inicio da análise.';
COMMENT ON COLUMN analise.analise_manejo.data IS 'Data do inicio da análise de manejo.';
COMMENT ON COLUMN analise.analise_manejo.path_arquivo_shape IS 'Caminho onde está armazenado o arquivo shape da análise.';
COMMENT ON COLUMN analise.analise_manejo.path_anexo IS 'Caminho onde está armazenado o anéxo da análise.';
COMMENT ON COLUMN analise.analise_manejo.analise_temporal IS 'Dados da análise temporal.';
COMMENT ON COLUMN analise.analise_manejo.area_manejo_florestal_solicitada IS 'Área de manejo florestal solicitada em hectares.';
COMMENT ON COLUMN analise.analise_manejo.area_preservacao_permanente IS 'Área de preservação permanente em hectares definida pela análise.';
COMMENT ON COLUMN analise.analise_manejo.area_servidao IS 'Área de servidão em hectares definida pela análise.';
COMMENT ON COLUMN analise.analise_manejo.area_antropizada_nao_consolidada IS 'Área antropizada não consolidada em hectares definida pela análise.';
COMMENT ON COLUMN analise.analise_manejo.area_consolidada IS 'Área consolidada em hectares definida pela análise.';
COMMENT ON COLUMN analise.analise_manejo.area_uso_restrito IS 'Área de uso restrito em hectares definida pela análise.';
COMMENT ON COLUMN analise.analise_manejo.area_sem_potencial IS 'Área sem potencial em hectares definida pela análise.';
COMMENT ON COLUMN analise.analise_manejo.area_corpos_agua IS 'Área dos corpos de agua em hectares defindos pela análise.';
COMMENT ON COLUMN analise.analise_manejo.area_embargada_ibama IS 'Área embargada pelo IBAMA em hectares.';
COMMENT ON COLUMN analise.analise_manejo.area_embargada_ldi IS 'Área embargada pelo LDI em hectares.';
COMMENT ON COLUMN analise.analise_manejo.area_efetivo_manejo IS 'Área de efetivo manejo em hectares definida pela análise.';
COMMENT ON COLUMN analise.analise_manejo.area_com_exploraca_ndfi_baixo IS 'Área com exploração NDFI (Baixo) em hectares.';
COMMENT ON COLUMN analise.analise_manejo.area_com_exploraca_ndfi_medio IS 'Área com exploração NDFI (Médio) em hectares.';
COMMENT ON COLUMN analise.analise_manejo.area_sem_previa_exploracao IS 'Área sem previa exploração em hectares.';
COMMENT ON COLUMN analise.analise_manejo.consideracoes IS 'Considerações da análise.';
COMMENT ON COLUMN analise.analise_manejo.conclusao IS 'Notas de conclusão da análise.';
COMMENT ON COLUMN analise.analise_manejo.id_usuario IS 'Identificador da entidade usuário que denota o usuário responsável por fazer a análise.';

ALTER TABLE analise.analise_manejo OWNER TO postgres;
GRANT ALL ON TABLE analise.analise_manejo TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.analise_manejo TO licenciamento_ap;
GRANT SELECT, USAGE ON SEQUENCE analise.analise_manejo_id_seq TO licenciamento_ap;


-- Criação da tabela rel_base_vetorial_analise_manejo

CREATE TABLE analise.rel_base_vetorial_analise_manejo (
 id SERIAL NOT NULL,
 id_base_vetorial INTEGER NOT NULL,
 id_analise_manejo INTEGER NOT NULL,
 CONSTRAINT pk_rel_base_vetorial_analise_manejo PRIMARY KEY(id),
 CONSTRAINT fk_rbvam_base_vetorial FOREIGN KEY (id_base_vetorial) REFERENCES analise.base_vetorial (id),
 CONSTRAINT fk_rbvam_analise_manejo FOREIGN KEY (id_analise_manejo) REFERENCES analise.analise_manejo (id)
);

COMMENT ON TABLE analise.rel_base_vetorial_analise_manejo IS 'Entidade responsável por armazenas o relacionamento entre análise manejo e a base vetorial.';
COMMENT ON COLUMN analise.rel_base_vetorial_analise_manejo.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.rel_base_vetorial_analise_manejo.id_base_vetorial IS 'Identificador da base vetorial.';
COMMENT ON COLUMN analise.rel_base_vetorial_analise_manejo.id_analise_manejo IS 'Identificador da análise manejo.';

ALTER TABLE analise.rel_base_vetorial_analise_manejo OWNER TO postgres;
GRANT ALL ON TABLE analise.rel_base_vetorial_analise_manejo TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.rel_base_vetorial_analise_manejo TO licenciamento_ap;
GRANT SELECT, USAGE ON SEQUENCE analise.rel_base_vetorial_analise_manejo_id_seq TO licenciamento_ap;


-- Criação da tabela analise_vetorial

CREATE TABLE analise.analise_vetorial (
 id SERIAL NOT NULL,
 tipo VARCHAR(200) NOT NULL,
 nome VARCHAR(200) NOT NULL,
 distancia_propriedade DOUBLE PRECISION NOT NULL,
 sobreposicao_propriedade DOUBLE PRECISION NOT NULL,
 distancia_amf DOUBLE PRECISION NOT NULL,
 sobreposicao_amf DOUBLE PRECISION NOT NULL,
 observacao TEXT NOT NULL,
 id_analise_manejo INTEGER NOT NULL,
 CONSTRAINT pk_analise_vetorial PRIMARY KEY(id),
 CONSTRAINT fk_av_analise_manejo FOREIGN KEY (id_analise_manejo) REFERENCES analise.analise_manejo (id)
);

COMMENT ON TABLE analise.analise_vetorial IS 'Entidade responsável por armazenas os dados de uma análise vetorial.';
COMMENT ON COLUMN analise.analise_vetorial.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.analise_vetorial.tipo IS 'Tipo da análise vetorial.';
COMMENT ON COLUMN analise.analise_vetorial.nome IS 'Nome da análise vetorial.';
COMMENT ON COLUMN analise.analise_vetorial.distancia_propriedade IS 'Distância da propriedade em kilometros.';
COMMENT ON COLUMN analise.analise_vetorial.sobreposicao_propriedade IS 'Sobreposição da propriedade em hectares.';
COMMENT ON COLUMN analise.analise_vetorial.distancia_amf IS 'Distância da AMF em kilometros.';
COMMENT ON COLUMN analise.analise_vetorial.sobreposicao_amf IS 'Sobreposição da AMF em hectares.';
COMMENT ON COLUMN analise.analise_vetorial.observacao IS 'Observação da análise vetorial.';
COMMENT ON COLUMN analise.analise_vetorial.id_analise_manejo IS 'Identificador da análise manejo.';

ALTER TABLE analise.analise_vetorial OWNER TO postgres;
GRANT ALL ON TABLE analise.analise_vetorial TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.analise_vetorial TO licenciamento_ap;
GRANT SELECT, USAGE ON SEQUENCE analise.analise_vetorial_id_seq TO licenciamento_ap;


-- Criação da tabela analise_ndfi

CREATE TABLE analise.analise_ndfi (
 id SERIAL NOT NULL,
 data TIMESTAMP NOT NULL,
 orbita INTEGER NOT NULL,
 ponto INTEGER NOT NULL,
 satelite VARCHAR(200) NOT NULL,
 nivel_exploracao VARCHAR(200) NOT NULL,
 valor_ndfi DOUBLE PRECISION NOT NULL,
 area DOUBLE PRECISION NOT NULL,
 id_analise_manejo INTEGER NOT NULL,
 CONSTRAINT pk_analise_ndfi PRIMARY KEY(id),
 CONSTRAINT fk_an_analise_manejo FOREIGN KEY (id_analise_manejo) REFERENCES analise.analise_manejo (id)
);

COMMENT ON TABLE analise.analise_ndfi IS 'Entidade responsável por armazenas os dados de uma análise NDFI.';
COMMENT ON COLUMN analise.analise_ndfi.id IS 'Data da análise NDFI.';
COMMENT ON COLUMN analise.analise_ndfi.orbita IS 'Orbita da análise NDFI.';
COMMENT ON COLUMN analise.analise_ndfi.ponto IS 'Ponto da análise NDFI.';
COMMENT ON COLUMN analise.analise_ndfi.satelite IS 'Satélite usado na análise.';
COMMENT ON COLUMN analise.analise_ndfi.nivel_exploracao IS 'Nível de exploração da análise.';
COMMENT ON COLUMN analise.analise_ndfi.valor_ndfi IS 'Valor do NDFI.';
COMMENT ON COLUMN analise.analise_ndfi.area IS 'Área da análise NDFI em hectares.';
COMMENT ON COLUMN analise.analise_ndfi.id_analise_manejo IS 'Identificador da análise manejo.';

ALTER TABLE analise.analise_ndfi OWNER TO postgres;
GRANT ALL ON TABLE analise.analise_ndfi TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.analise_ndfi TO licenciamento_ap;
GRANT SELECT, USAGE ON SEQUENCE analise.analise_ndfi_id_seq TO licenciamento_ap;


-- Criação da tabela observacao

CREATE TABLE analise.observacao (
 id SERIAL NOT NULL,
 texto TEXT NOT NULL,
 id_analise_manejo INTEGER NOT NULL,
 num_passo SMALLINT NOT NULL,
 CONSTRAINT pk_observacao PRIMARY KEY(id),
 CONSTRAINT fk_o_analise_manejo FOREIGN KEY (id_analise_manejo) REFERENCES analise.analise_manejo (id)
);

COMMENT ON TABLE analise.observacao IS 'Entidade responsável por armazenas as observações de uma análise de manejo.';
COMMENT ON COLUMN analise.observacao.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.observacao.texto IS 'Conteúdo da observação.';
COMMENT ON COLUMN analise.observacao.id_analise_manejo IS 'Identificador da entidade analise_manejo que faz o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.observacao.num_passo IS 'Número do passo em que a observação foi inserida.';

ALTER TABLE analise.observacao OWNER TO postgres;
GRANT ALL ON TABLE analise.observacao TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.observacao TO licenciamento_ap;
GRANT SELECT, USAGE ON SEQUENCE analise.observacao_id_seq TO licenciamento_ap;


-- Criação da tabela processo_manejo

CREATE TABLE analise.processo_manejo (
 id SERIAL NOT NULL,
 num_processo VARCHAR(200) NOT NULL,
 id_empreendimento_simlam INTEGER NOT NULL,
 cpf_cnpj_empreendimento VARCHAR(20) NOT NULL,
 denominacao_empreendimento_simlam VARCHAR(1000) NOT NULL,
 id_municipio_simlam INTEGER NOT NULL,
 nome_municipio_simlam VARCHAR(1000) NOT NULL,
 id_tipo_licenca INTEGER NOT NULL,
 nome_tipo_licenca VARCHAR(200) NOT NULL,
 id_imovel_manejo INTEGER NOT NULL,
 id_analise_manejo INTEGER,
 CONSTRAINT pk_processo_manejo PRIMARY KEY(id),
 CONSTRAINT fk_pm_imovel_manejo FOREIGN KEY (id_imovel_manejo) REFERENCES analise.imovel_manejo (id),
 CONSTRAINT fk_pm_analise_manejo FOREIGN KEY (id_analise_manejo) REFERENCES analise.analise_manejo (id)
);

COMMENT ON TABLE analise.processo_manejo IS 'Entidade responsável por armazenas os dados de um processo de manejo do SIMLAM.';
COMMENT ON COLUMN analise.processo_manejo.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.processo_manejo.num_processo IS 'Número do processo.';
COMMENT ON COLUMN analise.processo_manejo.id_empreendimento_simlam IS 'Identificador do empreendimento no SIMLAM.';
COMMENT ON COLUMN analise.processo_manejo.cpf_cnpj_empreendimento IS 'CPF ou CNPJ do empreendimento no SIMLAM.';
COMMENT ON COLUMN analise.processo_manejo.denominacao_empreendimento_simlam IS 'Denominação do empreeendimento no SIMLAM.';
COMMENT ON COLUMN analise.processo_manejo.id_municipio_simlam IS 'Identificador do município no SIMLAM.';
COMMENT ON COLUMN analise.processo_manejo.nome_municipio_simlam IS 'Nome do município no SIMLAM.';
COMMENT ON COLUMN analise.processo_manejo.id_tipo_licenca IS 'Identificador do tipo de licença de manejo do SIMLAM.';
COMMENT ON COLUMN analise.processo_manejo.nome_tipo_licenca IS 'Nome do tipo de licença de manejo do SIMLAM.';
COMMENT ON COLUMN analise.processo_manejo.id_imovel_manejo IS 'Identificador da imóvelo do manejo.';
COMMENT ON COLUMN analise.processo_manejo.id_analise_manejo IS 'Identificador da análise manejo.';

ALTER TABLE analise.processo_manejo OWNER TO postgres;
GRANT ALL ON TABLE analise.processo_manejo TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.processo_manejo TO licenciamento_ap;
GRANT SELECT, USAGE ON SEQUENCE analise.processo_manejo_id_seq TO licenciamento_ap;


--										49.sql

-- adicionando colunas de tramitação em tabela de processo manejo

ALTER TABLE analise.processo_manejo  ADD COLUMN id_objeto_tramitavel INTEGER;

COMMENT ON COLUMN analise.processo_manejo.id_objeto_tramitavel IS 'Identificador único da entidade objeto_tramitavel.';

--                    50.sql

-- adicionando nova coluna em observação

ALTER TABLE analise.observacao ADD COLUMN data TIMESTAMP NOT NULL DEFAULT now();

COMMENT ON COLUMN analise.observacao.data IS 'Data da observação.';

--										51.sql

INSERT INTO analise.tipo_documento (id,nome,caminho_modelo,caminho_pasta,prefixo_nome_arquivo) VALUES
(7,'Documento análise manejo',NULL,'analise_manejo','analise_manejo');

-- Adiçao do documento que irá vincular ao pdf gerado

ALTER TABLE analise.analise_manejo ADD COLUMN id_documento INTEGER;
ALTER TABLE analise.analise_manejo ADD CONSTRAINT fk_documento FOREIGN KEY(id_documento) REFERENCES analise.documento (id);

COMMENT ON COLUMN analise.analise_manejo.id_documento IS 'Identificador da entidade documento.';


-- Adição de novos atributos em imóvel manejo

ALTER TABLE analise.imovel_manejo ADD COLUMN endereco TEXT;
COMMENT ON COLUMN analise.imovel_manejo.endereco IS 'Endereço do imóvel.';

ALTER TABLE analise.imovel_manejo ADD COLUMN bairro VARCHAR(250);
COMMENT ON COLUMN analise.imovel_manejo.bairro IS 'Bairro do imóvel.';

ALTER TABLE analise.imovel_manejo ADD COLUMN cep VARCHAR(10);
COMMENT ON COLUMN analise.imovel_manejo.bairro IS 'Código de endereçamento postal do imóvel.';


--										52.sql

ALTER TABLE analise.processo ADD COLUMN renovacao BOOLEAN DEFAULT FALSE;
COMMENT ON COLUMN analise.processo.renovacao IS 'Flag que indica se o processo é de renovação de licença.';

--										53.sql

-- Alterando entidade de processo_manejo

ALTER TABLE analise.processo_manejo DROP COLUMN id_municipio_simlam;
ALTER TABLE analise.processo_manejo DROP COLUMN nome_municipio_simlam;
ALTER TABLE analise.processo_manejo RENAME COLUMN id_empreendimento_simlam TO id_empreendimento;
ALTER TABLE analise.processo_manejo DROP COLUMN cpf_cnpj_empreendimento;
ALTER TABLE analise.processo_manejo DROP COLUMN denominacao_empreendimento_simlam;
ALTER TABLE analise.processo_manejo DROP COLUMN nome_tipo_licenca;
ALTER TABLE analise.processo_manejo DROP COLUMN id_imovel_manejo;


-- Criação da tabela tipo_licenca_manejo

CREATE TABLE analise.tipo_licenca_manejo (
 id SERIAL NOT NULL,
 nome VARCHAR(200) NOT NULL,
 codigo VARCHAR(200) NOT NULL,
 CONSTRAINT pk_tipo_licenca_manejo PRIMARY KEY(id)
);

COMMENT ON TABLE analise.tipo_licenca_manejo IS 'Entidade responsável por armazenas o tipo de licença de um processo do manejo.';
COMMENT ON COLUMN analise.tipo_licenca_manejo.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.tipo_licenca_manejo.nome IS 'Nome do tipo de licença.';
COMMENT ON COLUMN analise.tipo_licenca_manejo.codigo IS 'Código do tipo de licença.';

ALTER TABLE analise.tipo_licenca_manejo OWNER TO postgres;
GRANT ALL ON TABLE analise.tipo_licenca_manejo TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.tipo_licenca_manejo TO licenciamento_ap;
GRANT SELECT, USAGE ON SEQUENCE analise.tipo_licenca_manejo_id_seq TO licenciamento_ap;


-- Criação da tabela tipologia_manejo

CREATE TABLE analise.tipologia_manejo (
 id SERIAL NOT NULL,
 nome VARCHAR(200) NOT NULL,
 codigo VARCHAR(200) NOT NULL,
 CONSTRAINT pk_tipologia_manejo PRIMARY KEY(id)
);

COMMENT ON TABLE analise.tipologia_manejo IS 'Entidade responsável por armazenas a tipologia de um processo do manejo.';
COMMENT ON COLUMN analise.tipologia_manejo.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.tipologia_manejo.nome IS 'Nome da tipologia.';
COMMENT ON COLUMN analise.tipologia_manejo.codigo IS 'Código da tipologia.';

ALTER TABLE analise.tipologia_manejo OWNER TO postgres;
GRANT ALL ON TABLE analise.tipologia_manejo TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.tipologia_manejo TO licenciamento_ap;
GRANT SELECT, USAGE ON SEQUENCE analise.tipologia_manejo_id_seq TO licenciamento_ap;

-- Criação da tabela atividade_manejo

CREATE TABLE analise.atividade_manejo (
 id SERIAL NOT NULL,
 nome VARCHAR(200) NOT NULL,
 codigo VARCHAR(200) NOT NULL,
 id_tipologia INTEGER NOT NULL,
 CONSTRAINT pk_atividade_manejo PRIMARY KEY(id),
 CONSTRAINT fk_atm_tm FOREIGN KEY (id_tipologia) REFERENCES analise.tipologia_manejo (id)
);

COMMENT ON TABLE analise.atividade_manejo IS 'Entidade responsável por armazenas a atividade de um processo do manejo.';
COMMENT ON COLUMN analise.atividade_manejo.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.atividade_manejo.nome IS 'Nome da atividade.';
COMMENT ON COLUMN analise.atividade_manejo.codigo IS 'Código da atividade.';
COMMENT ON COLUMN analise.atividade_manejo.id_tipologia IS 'Identificador da entidade tipologia que faz o relacionamento entre tipologia e atividade do manejo.';

ALTER TABLE analise.atividade_manejo OWNER TO postgres;
GRANT ALL ON TABLE analise.atividade_manejo TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.atividade_manejo TO licenciamento_ap;
GRANT SELECT, USAGE ON SEQUENCE analise.atividade_manejo_id_seq TO licenciamento_ap;

-- tabela empreendimento_manejo

CREATE TABLE analise.empreendimento_manejo
(
  id SERIAL NOT NULL,
  denominacao VARCHAR(500) NOT NULL,
  cpf_cnpj VARCHAR(14) NOT NULL,
  id_imovel INTEGER NOT NULL,
  id_municipio INTEGER NOT NULL,

  CONSTRAINT pk_empreendimento_manejo PRIMARY KEY (id),
  CONSTRAINT fk_e_imovel FOREIGN KEY (id_imovel)
      REFERENCES analise.imovel_manejo (id),
  CONSTRAINT fk_e_municipio FOREIGN KEY (id_municipio)
      REFERENCES licenciamento.municipio (id_municipio)
);

ALTER TABLE analise.empreendimento_manejo
  OWNER TO postgres;
GRANT ALL ON TABLE analise.empreendimento_manejo TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.empreendimento_manejo TO licenciamento_ap;
GRANT SELECT, USAGE ON SEQUENCE analise.empreendimento_manejo_id_seq TO licenciamento_ap;

COMMENT ON TABLE analise.empreendimento_manejo IS 'Entidade responsavel por armazenar o empreendimento do manejo.';
COMMENT ON COLUMN analise.empreendimento_manejo.id IS 'Identificado único da entidade.';
COMMENT ON COLUMN analise.empreendimento_manejo.denominacao IS 'Denominação do empreendimento.';
COMMENT ON COLUMN analise.empreendimento_manejo.cpf_cnpj IS 'CPF/CNPJ do empreendimento.';
COMMENT ON COLUMN analise.empreendimento_manejo.id_imovel IS 'Identificador da tabela imóvel.';
COMMENT ON COLUMN analise.empreendimento_manejo.id_municipio IS 'Identificador da tabela município.';


-- Alteração imóvel manejo

ALTER TABLE analise.imovel_manejo DROP COLUMN endereco;
ALTER TABLE analise.imovel_manejo DROP COLUMN bairro;
ALTER TABLE analise.imovel_manejo DROP COLUMN cep;

DELETE FROM analise.imovel_manejo;

ALTER TABLE analise.imovel_manejo ADD COLUMN  descricao_acesso TEXT;
ALTER TABLE analise.imovel_manejo ADD COLUMN id_municipio integer NOT NULL;
ALTER TABLE analise.imovel_manejo ADD CONSTRAINT fk_i_municipio FOREIGN KEY (id_municipio)
      REFERENCES licenciamento.municipio (id_municipio);

COMMENT ON COLUMN analise.imovel_manejo.id_municipio IS 'Identificador da tabela município.';
COMMENT ON COLUMN analise.imovel_manejo.descricao_acesso IS 'Descrição de acesso (endereço) do imóvel.';

-- Adicionando constraints a entidade processo_manejo

ALTER TABLE analise.processo_manejo ADD CONSTRAINT fk_pm_em FOREIGN KEY (id_empreendimento) REFERENCES analise.empreendimento_manejo(id);
COMMENT ON COLUMN analise.processo_manejo.id_empreendimento IS 'Identificador da entidade empreendimento_manejo que faz o relacionamento entre empreendimento manejo e processo manejo.';

ALTER TABLE analise.processo_manejo ADD CONSTRAINT fk_pm_tlm FOREIGN KEY (id_tipo_licenca) REFERENCES analise.tipo_licenca_manejo(id);
COMMENT ON COLUMN analise.processo_manejo.id_tipo_licenca IS 'Identificador da entidade tipo_licenca_manejo que faz o relacionamento entre tipo licença manejo e processo manejo.';

ALTER TABLE analise.processo_manejo ADD COLUMN id_atividade_manejo INTEGER NOT NULL;
ALTER TABLE analise.processo_manejo ADD CONSTRAINT fk_pm_atm FOREIGN KEY (id_atividade_manejo) REFERENCES analise.atividade_manejo(id);
COMMENT ON COLUMN analise.processo_manejo.id_atividade_manejo IS 'Identificador da entidade atividade_manejo que faz o relacionamento entre atividade manejo e processo manejo.';


--										54.sql

--- Carga de dados na entidade tipo_licenca_manejo

INSERT INTO analise.tipo_licenca_manejo (id, codigo, nome) VALUES
 (1, 'APAT', 'APAT'),
 (2, 'AUTEF', 'AUTEF'),
 (3, 'LAR', 'LAR');


--- Carga de dados na entidade tipologia_manejo

INSERT INTO analise.tipologia_manejo (codigo, nome) VALUES
 ('AGROSILVIPASTORIL', 'Agrossilvipastoril');


--- Carga de dados na entidade atividade_manejo

INSERT INTO analise.atividade_manejo (codigo, nome, id_tipologia) VALUES
 ('MANEJO_FLORESTAL_REGIME_RENDIMENTO_SUSTENTAVEL', 'Manejo florestal em regime de rendimento sustentável', (SELECT id FROM analise.tipologia_manejo WHERE codigo = 'AGROSILVIPASTORIL')),
 ('MANEJO_ACAIZAIS', 'Manejo de açaizais', (SELECT id FROM analise.tipologia_manejo WHERE codigo = 'AGROSILVIPASTORIL')),
 ('COMERCIALIZACAO_MANEJO_RECURSOS_AQUATIVOS_VIVOS', 'Comercialização e manejo de recursos aquáticos vivos', (SELECT id FROM analise.tipologia_manejo WHERE codigo = 'AGROSILVIPASTORIL'));

--- Alterando entidade imovel_manejo

ALTER TABLE analise.imovel_manejo ALTER COLUMN area_total_imovel_documentado DROP NOT NULL;
ALTER TABLE analise.imovel_manejo ALTER COLUMN area_liquida_imovel DROP NOT NULL;
ALTER TABLE analise.imovel_manejo ADD COLUMN nome VARCHAR(450);

--										55.sql

--- Alteração na entidade analise_manejo

ALTER TABLE analise.analise_manejo DROP COLUMN path_arquivo_shape;

ALTER TABLE analise.analise_manejo ADD COLUMN geojson TEXT;
COMMENT ON COLUMN analise.analise_manejo.geojson IS 'Geojson arcgis do arquivo shapefile.';

ALTER TABLE analise.analise_manejo ALTER COLUMN analise_temporal DROP NOT NULL;
ALTER TABLE analise.analise_manejo ALTER COLUMN area_manejo_florestal_solicitada DROP NOT NULL;
ALTER TABLE analise.analise_manejo ALTER COLUMN area_sem_previa_exploracao DROP NOT NULL;
ALTER TABLE analise.analise_manejo ALTER COLUMN consideracoes DROP NOT NULL;
ALTER TABLE analise.analise_manejo ALTER COLUMN conclusao DROP NOT NULL;

ALTER TABLE analise.analise_manejo ADD COLUMN object_id VARCHAR(200);
COMMENT ON COLUMN analise.analise_manejo.object_id IS 'Identificador da análise no serviço de validação de shape.';

--										56.sql

-- Removendo atributo na entidade analise_manejo

ALTER TABLE analise.analise_manejo DROP COLUMN geojson;

-- Criando entidade documento_manejo_shape

CREATE TABLE analise.documento_manejo_shape (
 id_documento INTEGER NOT NULL,
 geojson_arcgis TEXT NOT NULL,
 id_analise_tecnica_manejo INTEGER NOT NULL,
 CONSTRAINT pk_documento_manejo_shape PRIMARY KEY (id_documento),
 CONSTRAINT fk_dms_documento FOREIGN KEY (id_documento) REFERENCES analise.documento(id),
 CONSTRAINT fk_dms_analise_manejo FOREIGN KEY (id_analise_tecnica_manejo) REFERENCES analise.analise_manejo (id)
);

COMMENT ON TABLE analise.documento_manejo_shape IS 'Entidade responsável por armazenar os documentos shape do manejo.';
COMMENT ON COLUMN analise.documento_manejo_shape.id_documento IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.documento_manejo_shape.geojson_arcgis IS 'Geojson arcgis do arquivo shapefile.';
COMMENT ON COLUMN analise.documento_manejo_shape.id_analise_tecnica_manejo IS 'Identificador da entidade analise_tecnica_manejo que faz o relacionamento entre a análise do manejo e documento shape do manejo.';

ALTER TABLE analise.documento_manejo_shape OWNER TO postgres;
GRANT ALL ON TABLE analise.documento_manejo_shape TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.documento_manejo_shape TO licenciamento_ap;

-- Adicionando tipos de documento manejo na entidade

INSERT INTO analise.tipo_documento (id, nome, caminho_pasta, prefixo_nome_arquivo) VALUES
 (8, 'Área de manejo florestal solicitada - AMF', 'shape-manejo', 'amf'),
 (9, 'Área de preservação permanente - APP', 'shape-manejo', 'app'),
 (10, 'Área sem potencial', 'shape-manejo', 'area_sem_potencial');

-- Alterando entidade processo manejo para suportar muitas análises técnicas

ALTER TABLE analise.processo_manejo DROP COLUMN id_analise_manejo;

DELETE FROM analise.analise_ndfi;
DELETE FROM analise.analise_vetorial;
DELETE FROM analise.rel_base_vetorial_analise_manejo;
DELETE FROM analise.observacao;
DELETE FROM analise.analise_manejo;
DELETE FROM analise.processo_manejo;

ALTER TABLE analise.analise_manejo ADD COLUMN id_processo_manejo INTEGER NOT NULL;
ALTER TABLE analise.analise_manejo ADD CONSTRAINT fk_atm_pm FOREIGN KEY (id_processo_manejo) REFERENCES analise.processo_manejo(id);
COMMENT ON COLUMN analise.analise_manejo.id_processo_manejo IS 'Identificador da entidade processo_manejo que identifica qual processo essa análise pertence.';

-- Renomeando a entidade para analise_manejo para analise_tecnica_manejo

ALTER INDEX analise.pk_analise_manejo RENAME TO pk_analise_tecnica_manejo;
ALTER TABLE analise.analise_manejo RENAME TO analise_tecnica_manejo;

-- Renomeando atributos de outras tabelas

ALTER TABLE analise.analise_ndfi RENAME COLUMN id_analise_manejo TO id_analise_tecnica_manejo;
ALTER TABLE analise.rel_base_vetorial_analise_manejo RENAME COLUMN id_analise_manejo TO id_analise_tecnica_manejo;
ALTER TABLE analise.analise_vetorial RENAME COLUMN id_analise_manejo TO id_analise_tecnica_manejo;

-- Removendo a coluna id_usuario da analise_tecnica_manejo

ALTER TABLE analise.analise_tecnica_manejo DROP COLUMN id_usuario;

-- Criando entidade analista_tecnico_manejo

CREATE TABLE analise.analista_tecnico_manejo (
 id SERIAL NOT NULL,
 data_vinculacao TIMESTAMP NOT NULL,
 id_analise_tecnica_manejo INTEGER NOT NULL,
 id_usuario INTEGER NOT NULL,
 CONSTRAINT pk_analista_tecnico_manejo PRIMARY KEY (id),
 CONSTRAINT fk_antm_analise_tecnica_manejo FOREIGN KEY (id_analise_tecnica_manejo) REFERENCES analise.analise_tecnica_manejo(id)
 );

COMMENT ON TABLE analise.analista_tecnico_manejo IS 'Entidade responsável por armazenar os analistas técnicos do manejo.';
COMMENT ON COLUMN analise.analista_tecnico_manejo.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.analista_tecnico_manejo.data_vinculacao IS 'Data da vinculação do analista a análise.';
COMMENT ON COLUMN analise.analista_tecnico_manejo.id_analise_tecnica_manejo IS 'Identificador da entidade analise_tecnica_manejo que faz o relacionamento entre a análise do manejo e o analista técnico.';
COMMENT ON COLUMN analise.analista_tecnico_manejo.id_usuario IS 'Identificador da entidade usuario que faz o relacionamento entre um usuário e o análista.';

ALTER TABLE analise.analista_tecnico_manejo OWNER TO postgres;
GRANT ALL ON TABLE analise.analista_tecnico_manejo TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.analista_tecnico_manejo TO licenciamento_ap;
GRANT SELECT, USAGE ON SEQUENCE analise.analista_tecnico_manejo_id_seq TO licenciamento_ap;


--										57.sql

-- Alterando o nome da sequence analise_manejo_id_seq

ALTER SEQUENCE analise.analise_manejo_id_seq RENAME TO analise_tecnica_manejo_id_seq;

-- Removendo path_anexo de analise_tecnica_manejo

ALTER TABLE analise.analise_tecnica_manejo DROP COLUMN path_anexo;

-- Criando entidade documento_imovel_manejo

CREATE TABLE analise.documento_imovel_manejo (
 id_documento INTEGER NOT NULL,
 id_analise_tecnica_manejo INTEGER NOT NULL,
 nome VARCHAR(200),
 CONSTRAINT pk_documento_imovel_manejo PRIMARY KEY (id_documento),
 CONSTRAINT fk_dmi_documento FOREIGN KEY (id_documento) REFERENCES analise.documento(id),
 CONSTRAINT fk_dmi_analise_manejo FOREIGN KEY (id_analise_tecnica_manejo) REFERENCES analise.analise_tecnica_manejo (id)
);

COMMENT ON TABLE analise.documento_imovel_manejo IS 'Entidade responsável por armazenar os documentos do imóvel do manejo.';
COMMENT ON COLUMN analise.documento_imovel_manejo.id_documento IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.documento_imovel_manejo.id_analise_tecnica_manejo IS 'Identificador da entidade analise_tecnica_manejo que faz o relacionamento entre a análise do manejo e documento do imóvel do manejo.';
COMMENT ON COLUMN analise.documento_imovel_manejo.nome IS 'Nome do documento.';

ALTER TABLE analise.documento_imovel_manejo OWNER TO postgres;
GRANT ALL ON TABLE analise.documento_imovel_manejo TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.documento_imovel_manejo TO licenciamento_ap;

-- Adicionando tipos de documento do imóvel do manejo na entidade tipo_documento

INSERT INTO analise.tipo_documento (id, nome, caminho_pasta, prefixo_nome_arquivo) VALUES
 (11, 'Documento imóvel manejo', 'documento-imovel-manejo', 'documento_imovel_manejo');

-- Renomeando atributo da entidade observacao

ALTER TABLE analise.observacao RENAME COLUMN id_analise_manejo TO id_analise_tecnica_manejo;

--										58.sql

-- Renomeando a entidade para documento_imovel_manejo para documento_manejo

ALTER INDEX analise.pk_documento_imovel_manejo RENAME TO pk_documento_manejo;
ALTER TABLE analise.documento_imovel_manejo RENAME CONSTRAINT fk_dmi_documento TO fk_dm_documento;
ALTER TABLE analise.documento_imovel_manejo RENAME CONSTRAINT fk_dmi_analise_manejo TO fk_dm_analise_manejo;
ALTER TABLE analise.documento_imovel_manejo RENAME TO documento_manejo;

-- Adicionando tipos de documento do imóvel do manejo na entidade tipo_documento

INSERT INTO analise.tipo_documento (id, nome, caminho_pasta, prefixo_nome_arquivo) VALUES
 (12, 'Documento complementar manejo', 'documento-complementar-manejo', 'documento_complementar');

--										59.sql

-- Alterando a entidade analise_vetorial para incluir flag de exibição no pdf

ALTER TABLE analise.analise_vetorial ADD COLUMN exibir_pdf BOOLEAN NOT NULL DEFAULT TRUE;
COMMENT ON COLUMN analise.analise_vetorial.exibir_pdf IS 'Flag de exibição no pdf da análise.';

-- Alterando a entidade analise_ndfi para incluir flag de exibição no pdf

ALTER TABLE analise.analise_ndfi ADD COLUMN exibir_pdf BOOLEAN NOT NULL DEFAULT TRUE;
COMMENT ON COLUMN analise.analise_ndfi.exibir_pdf IS 'Flag de exibição no pdf da análise.';

-- Criando da entidade insumo

CREATE TABLE analise.insumo (
 id SERIAL NOT NULL,
 data TIMESTAMP NOT NULL,
 satelite VARCHAR(200) NOT NULL,
 orb_ponto VARCHAR(200) NOT NULL,
 CONSTRAINT pk_insumo PRIMARY KEY (id)
);

COMMENT ON TABLE analise.insumo IS 'Entidade responsável por armazenas os insumos de uma análise do manejo.';
COMMENT ON COLUMN analise.insumo.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.insumo.data IS 'Data do insumo.';
COMMENT ON COLUMN analise.insumo.satelite IS 'Nome do satelite utilizado.';
COMMENT ON COLUMN analise.insumo.orb_ponto IS 'Orb e ponto utilizados.';

ALTER TABLE analise.insumo OWNER TO postgres;
GRANT ALL ON TABLE analise.insumo TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.insumo TO licenciamento_ap;
GRANT SELECT, USAGE ON SEQUENCE analise.insumo_id_seq TO licenciamento_ap;

-- Criando da entidade vinculo_analise_tecnica_manejo_insumo

CREATE TABLE analise.vinculo_analise_tecnica_manejo_insumo (
 id SERIAL NOT NULL,
 id_analise_tecnica_manejo INTEGER NOT NULL,
 id_insumo INTEGER NOT NULL,
 exibir_pdf BOOLEAN NOT NULL DEFAULT TRUE,
 CONSTRAINT pk_vinculo_analise_tecnica_manejo_insumo PRIMARY KEY (id),
 CONSTRAINT fk_rati_analise_tecnica_manejo FOREIGN KEY (id_analise_tecnica_manejo) REFERENCES analise.analise_tecnica_manejo (id),
 CONSTRAINT fk_rati_insumo FOREIGN KEY (id_insumo) REFERENCES analise.insumo (id)
);

COMMENT ON TABLE analise.vinculo_analise_tecnica_manejo_insumo IS 'Entidade responsável por armazenar o relacionamento entre uma análise tecnica do manejo e um insumo.';
COMMENT ON COLUMN analise.vinculo_analise_tecnica_manejo_insumo.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.vinculo_analise_tecnica_manejo_insumo.id_analise_tecnica_manejo IS 'Identificador da entidade analise_tecnica_manejo.';
COMMENT ON COLUMN analise.vinculo_analise_tecnica_manejo_insumo.id_insumo IS 'Identificador da entidade insumo.';
COMMENT ON COLUMN analise.vinculo_analise_tecnica_manejo_insumo.exibir_pdf IS 'Flag de exibição no pdf da análise.';

ALTER TABLE analise.vinculo_analise_tecnica_manejo_insumo OWNER TO postgres;
GRANT ALL ON TABLE analise.vinculo_analise_tecnica_manejo_insumo TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.vinculo_analise_tecnica_manejo_insumo TO licenciamento_ap;
GRANT SELECT, USAGE ON SEQUENCE analise.vinculo_analise_tecnica_manejo_insumo_id_seq TO licenciamento_ap;

-- Removendo atributos desnecessários

ALTER TABLE analise.analise_vetorial DROP COLUMN observacao;
ALTER TABLE analise.analise_tecnica_manejo DROP COLUMN analise_temporal;
ALTER TABLE analise.analise_tecnica_manejo DROP COLUMN consideracoes;
ALTER TABLE analise.analise_tecnica_manejo DROP COLUMN conclusao;

--                    60.sql

ALTER TABLE analise.processo_manejo ADD COLUMN revisao_solicitada BOOLEAN NOT NULL DEFAULT FALSE;
COMMENT ON COLUMN analise.processo_manejo.revisao_solicitada IS 'Flag que indica se foi solicitada uma revisão dos arquivos shape do processo.';

--										61.sql

ALTER TABLE analise.processo_manejo ADD COLUMN justificativa_indeferimento TEXT;
COMMENT ON COLUMN analise.processo_manejo.justificativa_indeferimento IS 'Justificativa dada pelo análista técnico para o indeferimento do processo.';

--										62.sql

--- Inserir novos tipos de documentos para dados do imovel em analise manejo

INSERT INTO analise.tipo_documento (id, nome, caminho_pasta, prefixo_nome_arquivo) VALUES
 (13, 'Termo de Delimitação da área de reserva aprovada', 'documento-imovel-manejo', 'tdara'),
 (14, 'termo de ajustamento da conduta - TAC', 'documento-imovel-manejo', 'tac');

--- Alterar tipo de documento do manejo

UPDATE analise.tipo_documento SET nome = 'Área da propriedade - APM', prefixo_nome_arquivo = 'apm' WHERE id = 9;

--- Adicionar campo de exibir pdf na análise vetorial

ALTER TABLE analise.base_vetorial ADD COLUMN exibir_pdf BOOLEAN NOT NULL DEFAULT TRUE;
COMMENT ON COLUMN analise.base_vetorial.exibir_pdf IS 'Flag de exibição no pdf da análise.';


--										63.sql

--- Retirando obrigatóriedade dos campos da tabela base vetorial

ALTER TABLE analise.base_vetorial ALTER COLUMN escala DROP NOT NULL;
ALTER TABLE analise.base_vetorial ALTER COLUMN observacao DROP NOT NULL;
ALTER TABLE analise.insumo ALTER COLUMN data DROP NOT NULL;
ALTER TABLE analise.analise_ndfi ALTER COLUMN data DROP NOT NULL;


--										64.sql

--- Removendo coluna escala da entidade base_vetorial

ALTER TABLE analise.base_vetorial DROP COLUMN escala;

--										65.sql

--- Adicionando atributo na entidade analise_tecnica_manejo

ALTER TABLE analise.analise_tecnica_manejo ADD COLUMN conclusao TEXT;
COMMENT ON COLUMN analise.analise_tecnica_manejo.conclusao IS 'Conclusão da análise técnica do manejo.';

--- Criando entidade consideracao

CREATE TABLE analise.consideracao (
 id SERIAL NOT NULL,
 texto TEXT NOT NULL,
 CONSTRAINT pk_consideracao PRIMARY KEY (id)
);

COMMENT ON TABLE analise.consideracao IS 'Entidade responsável por armazenas as considerações da análise do manejo.';
COMMENT ON COLUMN analise.consideracao.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.consideracao.texto IS 'Conteúdo da consideração.';

ALTER TABLE analise.consideracao OWNER TO postgres;
GRANT ALL ON TABLE analise.consideracao TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.consideracao TO licenciamento_ap;
GRANT SELECT, USAGE ON SEQUENCE analise.consideracao_id_seq TO licenciamento_ap;

INSERT INTO analise.consideracao (texto) VALUES
 ('Toda a análise da GEOTEC foi realizada com base em dados apresentados pelo próprio Técnico Responsável, passível de sanções administrativas conforme art. 66 da Lei n° 9605 de 12/02/1998 e art. 82 do Decreto n° 6514 de 22/07/2008.'),
 ('As informações utilizadas, até a presente data, para analise do processo da GEOTEC, foram disponibilizadas do Banco de Dados de Raster e Vetores pela gerencia GTDI.'),
 ('No processo CONSTA o Relatório Técnico de Georreferenciamento do Imóvel, em meio analógico e digital, conforme as normas técnicas do INCRA (Lei 10.267/01).'),
 ('De acordo com a INSTRUÇÃO NORMATIVA Nº 001, de 14 de janeiro de 2014, a APAT não permite o início das atividades de manejo, não autoriza a exploração florestal e nem se constitui em prova de posse ou propriedade para fins de regularização fundiários, de autorização de desmatamento ou de obtenção de financiamento junto às instituições de crédito públicas ou privados;');


-- Criando da entidade vinculo_analise_tecnica_manejo_consideracao

CREATE TABLE analise.vinculo_analise_tecnica_manejo_consideracao (
 id SERIAL NOT NULL,
 id_analise_tecnica_manejo INTEGER NOT NULL,
 id_consideracao INTEGER NOT NULL,
 exibir_pdf BOOLEAN NOT NULL DEFAULT TRUE,
 CONSTRAINT pk_vinculo_analise_tecnica_manejo_consideracao PRIMARY KEY (id),
 CONSTRAINT fk_vatcm_analise_tecnica_manejo FOREIGN KEY (id_analise_tecnica_manejo) REFERENCES analise.analise_tecnica_manejo (id),
 CONSTRAINT fk_vatcm_consideracao FOREIGN KEY (id_consideracao) REFERENCES analise.consideracao (id)
);

COMMENT ON TABLE analise.vinculo_analise_tecnica_manejo_consideracao IS 'Entidade responsável por armazenar o relacionamento entre uma análise tecnica do manejo e uma consideração.';
COMMENT ON COLUMN analise.vinculo_analise_tecnica_manejo_consideracao.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.vinculo_analise_tecnica_manejo_consideracao.id_analise_tecnica_manejo IS 'Identificador da entidade analise_tecnica_manejo.';
COMMENT ON COLUMN analise.vinculo_analise_tecnica_manejo_consideracao.id_consideracao IS 'Identificador da entidade consideracao.';
COMMENT ON COLUMN analise.vinculo_analise_tecnica_manejo_consideracao.exibir_pdf IS 'Flag de exibição no pdf da análise.';

ALTER TABLE analise.vinculo_analise_tecnica_manejo_consideracao OWNER TO postgres;
GRANT ALL ON TABLE analise.vinculo_analise_tecnica_manejo_consideracao TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.vinculo_analise_tecnica_manejo_consideracao TO licenciamento_ap;
GRANT SELECT, USAGE ON SEQUENCE analise.vinculo_analise_tecnica_manejo_consideracao_id_seq TO licenciamento_ap;


-- Criando da entidade embasamento_legal

CREATE TABLE analise.embasamento_legal (
 id SERIAL NOT NULL,
 texto TEXT NOT NULL,
 CONSTRAINT pk_embasamento_legal PRIMARY KEY (id)
);

COMMENT ON TABLE analise.embasamento_legal IS 'Entidade responsável por armazenas os embasamentos legais da análise do manejo.';
COMMENT ON COLUMN analise.embasamento_legal.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.embasamento_legal.texto IS 'Conteúdo do embasamento.';

ALTER TABLE analise.embasamento_legal OWNER TO postgres;
GRANT ALL ON TABLE analise.embasamento_legal TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.embasamento_legal TO licenciamento_ap;
GRANT SELECT, USAGE ON SEQUENCE analise.embasamento_legal_id_seq TO licenciamento_ap;

INSERT INTO analise.embasamento_legal (texto) VALUES
 ('Código Florestal - Lei n°12651/2012'),
 ('Instrução Normativa n°02/2014'),
 ('Decreto n°7830/2012'),
 ('Instrução Normativa n° 01/2014'),
 ('Portaria n° 63/2014'),
 ('Instrução Normativa n°14/2011'),
 ('Decreto n° 216/2011'),
 ('Lei Federal 10.267/01'),
 ('Código Florestal - Lei n°12651/2012'),
 ('Lei n° 9605 de 12/02/1998'),
 ('Decreto n° 6514 de 22/07/2008'),
 ('Resolução CONAMA nº 378/2006');


-- Criando da entidade vinculo_analise_tecnica_manejo_embasamento_legal

CREATE TABLE analise.vinculo_analise_tecnica_manejo_embasamento_legal (
 id SERIAL NOT NULL,
 id_analise_tecnica_manejo INTEGER NOT NULL,
 id_embasamento_legal INTEGER NOT NULL,
 exibir_pdf BOOLEAN NOT NULL DEFAULT TRUE,
 CONSTRAINT pk_vinculo_analise_tecnica_manejo_embasamento_legal PRIMARY KEY (id),
 CONSTRAINT fk_vatel_analise_tecnica_manejo FOREIGN KEY (id_analise_tecnica_manejo) REFERENCES analise.analise_tecnica_manejo (id),
 CONSTRAINT fk_vatel_embasamento_legal FOREIGN KEY (id_embasamento_legal) REFERENCES analise.embasamento_legal (id)
);

COMMENT ON TABLE analise.vinculo_analise_tecnica_manejo_embasamento_legal IS 'Entidade responsável por armazenar o relacionamento entre uma análise tecnica do manejo e um embasamento legal.';
COMMENT ON COLUMN analise.vinculo_analise_tecnica_manejo_embasamento_legal.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.vinculo_analise_tecnica_manejo_embasamento_legal.id_analise_tecnica_manejo IS 'Identificador da entidade analise_tecnica_manejo.';
COMMENT ON COLUMN analise.vinculo_analise_tecnica_manejo_embasamento_legal.id_embasamento_legal IS 'Identificador da entidade embasamento_legal.';
COMMENT ON COLUMN analise.vinculo_analise_tecnica_manejo_embasamento_legal.exibir_pdf IS 'Flag de exibição no pdf da análise.';

ALTER TABLE analise.vinculo_analise_tecnica_manejo_embasamento_legal OWNER TO postgres;
GRANT ALL ON TABLE analise.vinculo_analise_tecnica_manejo_embasamento_legal TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.vinculo_analise_tecnica_manejo_embasamento_legal TO licenciamento_ap;
GRANT SELECT, USAGE ON SEQUENCE analise.vinculo_analise_tecnica_manejo_embasamento_legal_id_seq TO licenciamento_ap;


--										66.sql

--- Alteração na entidade imovel_manejo

ALTER TABLE analise.imovel_manejo ADD COLUMN status TEXT;
COMMENT ON COLUMN analise.imovel_manejo.status IS 'Indicador do status atual do imóvel no CAR.';

--										67.sql

--- Alterando texto das considerações

UPDATE analise.consideracao
 SET texto = 'Toda a análise da GEOTEC/DIGEO foi realizada com base em dados apresentados pelo próprio técnico responsável, passível de sanções administrativas conforme art. 66 da Lei n° 9605 de 12/02/1998 e art. 82 do Decreto n° 6514 de 22/07/2008.'
 WHERE texto = 'Toda a análise da GEOTEC foi realizada com base em dados apresentados pelo próprio Técnico Responsável, passível de sanções administrativas conforme art. 66 da Lei n° 9605 de 12/02/1998 e art. 82 do Decreto n° 6514 de 22/07/2008.';

UPDATE analise.consideracao
 SET texto = 'As informações utilizadas, até a presente data, para análise do processo da GEOTEC/DIGEO, foram disponibilizados do banco de dados de Raster pela GTDI, DIGEO  e Vetorial GEOSIG/DIGEO.'
 WHERE texto = 'As informações utilizadas, até a presente data, para analise do processo da GEOTEC, foram disponibilizadas do Banco de Dados de Raster e Vetores pela gerencia GTDI.';

UPDATE analise.consideracao
 SET texto = 'Consta no processo memorial e planta gerada automaticamente pelo SIGEF - Sistema de Gestão Fundiária, com base nas informações transmitidas e assinadas digitalmente pelo responsável técnico credenciado.'
 WHERE texto = 'De acordo com a INSTRUÇÃO NORMATIVA Nº 001, de 14 de janeiro de 2014, a APAT não permite o início das atividades de manejo, não autoriza a exploração florestal e nem se constitui em prova de posse ou propriedade para fins de regularização fundiários, de autorização de desmatamento ou de obtenção de financiamento junto às instituições de crédito públicas ou privados;';

--										68.sql

--- Inserir novo tipo de documento para anexos do ARQGIS em analise manejo

INSERT INTO analise.tipo_documento (id, nome, caminho_pasta, prefixo_nome_arquivo) VALUES
 (15, 'Anexo do processo do manejo digital', 'anexo-processo-manejo', 'anexo_processo_manejo');


--- Corrigindo nome de TAC

UPDATE analise.tipo_documento SET nome = 'Termo de ajustamento da conduta - TAC' WHERE id = 14;

--										69.sql

CREATE TABLE analise.usuario_analise (
	id serial NOT NULL , 
	login character varying(50), 
	CONSTRAINT pk_usuario_analise PRIMARY KEY (id)
);

ALTER TABLE analise.usuario_analise OWNER TO postgres;

GRANT ALL ON TABLE analise.usuario_analise TO postgres;

GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.usuario_analise TO licenciamento_ap;

GRANT SELECT ON TABLE analise.usuario_analise TO tramitacao;

GRANT SELECT,USAGE ON SEQUENCE analise.usuario_analise_id_seq TO licenciamento_ap;

COMMENT ON TABLE analise.usuario_analise IS 'Entidade responsável por armazenar os usuários que possui permissão para acessar o módulo de análise do licenciamento';

COMMENT ON COLUMN analise.usuario_analise.id IS 'Identificador único da entidade usuario.';

COMMENT ON COLUMN analise.usuario_analise.login IS 'Login do usuário que pode ser cpf ou cnpj';

--                    70.sql


CREATE TABLE analise.historico_tramitacao_setor
(
  id_historico_tramitacao integer NOT NULL, -- Identificador único da entidade historico_tramitacao.
  sigla_setor character varying(255) NOT NULL, -- Identificador do setor que faz o relacionamento entre o histórico de tramitação e setor.
  CONSTRAINT pk_historico_tramitacao_setor PRIMARY KEY (id_historico_tramitacao, sigla_setor)
);

ALTER TABLE analise.historico_tramitacao_setor OWNER TO postgres;
GRANT ALL ON TABLE analise.historico_tramitacao_setor TO postgres;
GRANT SELECT,UPDATE,INSERT,DELETE ON TABLE analise.historico_tramitacao_setor TO licenciamento_ap;

COMMENT ON TABLE analise.historico_tramitacao_setor IS 'Entidade responsável por armazenar o relacionamento entre historico_tramitacao e um setor.';
COMMENT ON COLUMN analise.historico_tramitacao_setor.id_historico_tramitacao IS 'Identificador único da entidade historico_tramitacao.';
COMMENT ON COLUMN analise.historico_tramitacao_setor.sigla_setor IS 'Identificador do setor que faz o relacionamento entre o histórico de tramitação e setor.';


--										71.sql

BEGIN;

ALTER TABLE analise.analise_juridica DROP CONSTRAINT fk_aj_usuario;
ALTER TABLE analise.analise_juridica DROP CONSTRAINT fk_aj_usuario_validacao_aprovador;
ALTER TABLE analise.analise_tecnica DROP CONSTRAINT fk_at_usuario;
ALTER TABLE analise.analise_tecnica DROP CONSTRAINT fk_at_usuario_validacao_aprovador;
ALTER TABLE analise.analise_tecnica DROP CONSTRAINT fk_at_usuario_validacao_gerente;
ALTER TABLE analise.analista_tecnico DROP CONSTRAINT fk_at_usuario;
ALTER TABLE analise.analista_tecnico_manejo DROP CONSTRAINT fk_antm_usuario;
ALTER TABLE analise.consultor_juridico DROP CONSTRAINT fk_cj_usuario;
ALTER TABLE analise.dispensa_licenciamento_cancelada DROP CONSTRAINT fk_dlc_usuario_executor;
ALTER TABLE analise.gerente DROP CONSTRAINT fk_gt_usuario;
ALTER TABLE analise.licenca_cancelada DROP CONSTRAINT fk_lc_usuario_executor;
ALTER TABLE analise.licenca_suspensa DROP CONSTRAINT fk_ls_usuario_executor;

ALTER TABLE analise.analise_juridica ADD CONSTRAINT fk_aj_usuario_analise 
  FOREIGN KEY (id_usuario_validacao) REFERENCES analise.usuario_analise (id);
ALTER TABLE analise.analise_juridica ADD CONSTRAINT fk_aj_usuario_analise_validacao_aprovador 
  FOREIGN KEY (id_usuario_validacao_aprovador) REFERENCES analise.usuario_analise (id);
ALTER TABLE analise.analise_tecnica ADD CONSTRAINT fk_at_usuario_analise FOREIGN KEY (id_usuario_validacao) 
  REFERENCES analise.usuario_analise (id);
ALTER TABLE analise.analise_tecnica ADD CONSTRAINT fk_at_usuario_analise_validacao_aprovador 
  FOREIGN KEY (id_usuario_validacao_aprovador) REFERENCES analise.usuario_analise (id);
ALTER TABLE analise.analise_tecnica ADD CONSTRAINT fk_at_usuario_analise_validacao_gerente 
  FOREIGN KEY (id_usuario_validacao_gerente) REFERENCES analise.usuario_analise (id);
ALTER TABLE analise.analista_tecnico ADD CONSTRAINT fk_at_usuario_analise FOREIGN KEY (id_usuario) 
  REFERENCES analise.usuario_analise (id);
ALTER TABLE analise.analista_tecnico_manejo ADD CONSTRAINT fk_antm_usuario_analise FOREIGN KEY (id_usuario) 
  REFERENCES analise.usuario_analise (id);
ALTER TABLE analise.consultor_juridico ADD CONSTRAINT fk_cj_usuario_analise FOREIGN KEY (id_usuario) 
  REFERENCES analise.usuario_analise (id);
ALTER TABLE analise.dispensa_licenciamento_cancelada ADD CONSTRAINT fk_dlc_usuario_analise_executor 
  FOREIGN KEY (id_usuario_executor) REFERENCES analise.usuario_analise (id);
ALTER TABLE analise.gerente ADD CONSTRAINT fk_gt_usuario_analise FOREIGN KEY (id_usuario) 
  REFERENCES analise.usuario_analise (id);
ALTER TABLE analise.licenca_cancelada ADD CONSTRAINT fk_lc_usuario_analise_executor 
  FOREIGN KEY (id_usuario_executor) REFERENCES analise.usuario_analise (id);
ALTER TABLE analise.licenca_suspensa ADD CONSTRAINT fk_ls_usuario_analise_executor 
  FOREIGN KEY (id_usuario_executor) REFERENCES analise.usuario_analise (id);

COMMENT ON COLUMN analise.analise_juridica.id_usuario_validacao_aprovador IS 'Campo responsável por armazernar o aprovador que fez a validação.';
COMMENT ON COLUMN analise.analise_juridica.id_usuario_validacao IS 'Identificador da entidade analise.usuario_analise que realizará o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.analise_tecnica.id_usuario_validacao_aprovador IS 'Campo responsável por armazernar o aprovador que fez a validação.';
COMMENT ON COLUMN analise.analise_tecnica.id_usuario_validacao_gerente IS 'Campo responsável por armazernar o gerente que fez a validação.';
COMMENT ON COLUMN analise.analise_tecnica.id_usuario_validacao IS 'Identificador da entidade analise.usuario_analise que realizará o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.analista_tecnico.id_usuario IS 'Identificador da entidade usuario que faz o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.analista_tecnico_manejo.id_usuario IS 'Identificador da entidade usuario que faz o relacionamento entre usuario_analise e o analista.';
COMMENT ON COLUMN analise.consultor_juridico.id_usuario IS 'Identificador da tabela usuario, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.dispensa_licencamento_cancelada.id_usuario_executor IS 'Identificador da entidade analise.usuario_analise que realiza o relacionamento entre as entidades dispensa_licencamento_cancelada e portal_analise.usuario_analise identifica o usuário executor da ação.';
COMMENT ON COLUMN analise.gerente_tecnico.id_usuario IS 'Identificador da entidade analise.usuario_analise que realiza o relacionamento entre as entidades gerente_tecnico e analise.usuario_analise.';
COMMENT ON COLUMN analise.licenca_cancelada.id_usuario_executor IS 'Identificador da entidade analise.usuario_analise que realiza o relacionamento entre as entidades licenca_cancelada e portal_analise.usuario_analise identifica o usuário executor da ação.';
COMMENT ON COLUMN analise.licenca_suspensa.id_usuario_executor IS 'Identificador da entidade analise.usuario_analise que faz o relacionamento entre as duas entidades.';

COMMIT;


--										72.sql

BEGIN;

CREATE TABLE analise.analise_geo
(
    id                                    serial                      NOT NULL,
    id_analise                            integer                     NOT NULL,
    parecer                               text,
    data_vencimento_prazo                 date                        NOT NULL,
    revisao_solicitada                    boolean DEFAULT false,
    ativo                                 boolean DEFAULT true,
    id_analise_geo_revisada               integer,
    data_inicio                           timestamp without time zone,
    data_fim                              timestamp without time zone,
    id_tipo_resultado_analise             integer,
    id_tipo_resultado_validacao           integer,
    parecer_validacao                     text,
    id_usuario_validacao                  integer,
    justificativa_coordenador             text,
    id_tipo_resultado_validacao_gerente   integer,
    parecer_validacao_gerente             text,
    id_usuario_validacao_gerente          integer,
    data_cadastro                         timestamp without time zone NOT NULL,
    id_tipo_resultado_validacao_aprovador integer,
    parecer_validacao_aprovador           text,
    id_usuario_validacao_aprovador        integer,
    data_fim_validacao_aprovador          timestamp without time zone,
    notificacao_atendida                  boolean DEFAULT false,

    CONSTRAINT pk_analise_geo PRIMARY KEY (id),

    CONSTRAINT fk_ag_analise FOREIGN KEY (id_analise)
        REFERENCES analise.analise (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,

    CONSTRAINT fk_ag_analise_geo FOREIGN KEY (id_analise_geo_revisada)
        REFERENCES analise.analise_geo (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,

    CONSTRAINT fk_ag_tipo_resultado_analise FOREIGN KEY (id_tipo_resultado_analise)
        REFERENCES analise.tipo_resultado_analise (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,

    CONSTRAINT fk_ag_tipo_resultado_validacao FOREIGN KEY (id_tipo_resultado_validacao)
        REFERENCES analise.tipo_resultado_analise (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,

    CONSTRAINT fk_ag_tipo_resultado_validacao_aprovador FOREIGN KEY (id_tipo_resultado_validacao_aprovador)
        REFERENCES analise.tipo_resultado_analise (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,

    CONSTRAINT fk_ag_tipo_resultado_validacao_gerente FOREIGN KEY (id_tipo_resultado_validacao_gerente)
        REFERENCES analise.tipo_resultado_analise (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,

    CONSTRAINT fk_ag_usuario_analise FOREIGN KEY (id_usuario_validacao)
        REFERENCES analise.usuario_analise (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,

    CONSTRAINT fk_ag_usuario_analise_validacao_aprovador FOREIGN KEY (id_usuario_validacao_aprovador)
        REFERENCES analise.usuario_analise (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,

    CONSTRAINT fk_ag_usuario_analise_validacao_gerente FOREIGN KEY (id_usuario_validacao_gerente)
        REFERENCES analise.usuario_analise (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE analise.analise_geo OWNER TO postgres;
GRANT ALL ON TABLE analise.analise_geo TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.analise_geo TO licenciamento_ap;
GRANT SELECT, USAGE ON SEQUENCE analise.analise_geo_id_seq TO licenciamento_ap;

COMMENT ON TABLE analise.analise_geo IS 'Entidade responsável por armazenar as análises geo.';
COMMENT ON COLUMN analise.analise_geo.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.analise_geo.id_analise IS 'Identificador da tabela analise, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.analise_geo.parecer IS 'Parecer da análise geo.';
COMMENT ON COLUMN analise.analise_geo.data_vencimento_prazo IS 'Data de vencimento do prazo da análise.';
COMMENT ON COLUMN analise.analise_geo.revisao_solicitada IS 'Flag que indica se esta análise é uma revisão.';
COMMENT ON COLUMN analise.analise_geo.ativo IS 'Indica se a análise ainda está ativa.';
COMMENT ON COLUMN analise.analise_geo.id_analise_geo_revisada IS 'Identificador da tabela analise_geo, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.analise_geo.data_inicio IS 'Data de início da análise.';
COMMENT ON COLUMN analise.analise_geo.data_fim IS 'Data de fim de análise.';
COMMENT ON COLUMN analise.analise_geo.id_tipo_resultado_analise IS 'Campo responsavel por armazenar o resultado da análise do gerente.';
COMMENT ON COLUMN analise.analise_geo.id_tipo_resultado_validacao IS 'Campo responsavel por armazenar o resultado da análise do gerente.';
COMMENT ON COLUMN analise.analise_geo.parecer_validacao IS 'Campo responsável por armazenar a justificativa do gerente quando o mesmo vincular diretamente um analista geo.';
COMMENT ON COLUMN analise.analise_geo.id_usuario_validacao IS 'Identificador da entidade analise.usuario_analise que realizará o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.analise_geo.id_tipo_resultado_validacao_gerente IS 'Campo responsavel por armazenar o resultado da análise do gerente geo.';
COMMENT ON COLUMN analise.analise_geo.parecer_validacao_gerente IS 'Campo responsável por armazenar a descrição da validação do gerente geo.';
COMMENT ON COLUMN analise.analise_geo.id_usuario_validacao_gerente IS 'Campo responsável por armazernar o gerente que fez a validação.';
COMMENT ON COLUMN analise.analise_geo.data_cadastro IS 'Data de cadastro da análise.';
COMMENT ON COLUMN analise.analise_geo.id_tipo_resultado_validacao_aprovador IS 'Campo responsavel por armazenar o resultado da análise do aprovador.';
COMMENT ON COLUMN analise.analise_geo.parecer_validacao_aprovador IS 'Campo responsável por armazenar a descrição da validação do aprovador.';
COMMENT ON COLUMN analise.analise_geo.id_usuario_validacao_aprovador IS 'Campo responsável por armazernar o aprovador que fez a validação.';
COMMENT ON COLUMN analise.analise_geo.data_fim_validacao_aprovador IS 'Data final da análise do aprovador.';
COMMENT ON COLUMN analise.analise_geo.notificacao_atendida IS 'Flag para identificar as notificações atendidas.';


CREATE TABLE analise.analista_geo
(
    id              serial  NOT NULL,
    id_analise_geo  integer NOT NULL,
    id_usuario      integer NOT NULL,
    data_vinculacao timestamp without time zone,

    CONSTRAINT pk_analista_geo PRIMARY KEY (id),

    CONSTRAINT fk_ag_analise_geo FOREIGN KEY (id_analise_geo)
        REFERENCES analise.analise_geo (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,

    CONSTRAINT fk_ag_usuario_analise FOREIGN KEY (id_usuario)
        REFERENCES analise.usuario_analise (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE analise.analista_geo OWNER TO postgres;
GRANT ALL ON TABLE analise.analista_geo TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.analista_geo TO licenciamento_ap;
GRANT SELECT, USAGE ON SEQUENCE analise.analista_geo_id_seq TO licenciamento_ap;

COMMENT ON TABLE analise.analista_geo IS 'Entidade responsável por armazenar o analista da analise geo.';
COMMENT ON COLUMN analise.analista_geo.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.analista_geo.id_analise_geo IS 'Identificador da entidade análise que faz o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.analista_geo.id_usuario IS 'Identificador da entidade usuario que faz o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.analista_geo.data_vinculacao IS 'Data em que o usuario foi vinculado.';


CREATE TABLE analise.parecer_geo_restricao
(
    id             serial  NOT NULL,
    id_analise_geo integer NOT NULL,
    codigo_camada  text    NOT NULL,
    parecer        text    NOT NULL,

    CONSTRAINT pk_parecer_geo_restricoes PRIMARY KEY (id),

    CONSTRAINT fk_pgr_analise_geo FOREIGN KEY (id_analise_geo)
        REFERENCES analise.analise_geo (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE analise.parecer_geo_restricao OWNER TO postgres;
GRANT ALL ON TABLE analise.parecer_geo_restricao TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.parecer_geo_restricao TO licenciamento_ap;
GRANT SELECT, USAGE ON SEQUENCE analise.parecer_geo_restricao_id_seq TO licenciamento_ap;

COMMENT ON TABLE analise.parecer_geo_restricao IS 'Entidade responsável por armazenar o parecer das restrições geográficas.';
COMMENT ON COLUMN analise.parecer_geo_restricao.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.parecer_geo_restricao.id_analise_geo IS 'Identificador da entidade analise_geo que faz o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.parecer_geo_restricao.codigo_camada IS 'Código da camada do geoserver.';
COMMENT ON COLUMN analise.parecer_geo_restricao.parecer IS 'Descrição do parecer de restrição geográfico.';

INSERT INTO analise.tipo_documento(id, nome, caminho_pasta, prefixo_nome_arquivo) VALUES
(16,'Parecer análise geo', 'parecer_analise_geo', 'parecer_analise_geo' ),
(17,'Notificação análise geo', 'notificacao_analise_geo', 'notificacao_analise_geo' );

ALTER TABLE analise.notificacao

    ADD COLUMN id_analise_geo integer,

    ADD CONSTRAINT fk_n_analise_geo FOREIGN KEY (id_analise_geo)
        REFERENCES analise.analise_geo (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

COMMENT ON COLUMN analise.notificacao.id_analise_geo IS 'Identificador da tabela analise_geo, responsável pelo relacionamento entre as duas tabelas.';


ALTER TABLE analise.licenca_analise

    ADD COLUMN id_analise_geo integer,

    ADD CONSTRAINT fk_la_analise_geo FOREIGN KEY (id_analise_geo)
        REFERENCES analise.analise_geo (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

COMMENT ON COLUMN analise.licenca_analise.id_analise_geo IS 'Identificador da tabela analise_geo, responsável pelo relacionamento entre as duas tabelas.';


ALTER TABLE analise.analise_documento

    ADD COLUMN id_analise_geo integer,

    ADD CONSTRAINT fk_ad_analise_geo FOREIGN KEY (id_analise_geo)
        REFERENCES analise.analise_geo (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

COMMENT ON COLUMN analise.analise_documento.id_analise_geo IS 'Identificador da tabela analise_geo, responsável pelo relacionamento entre as duas tabelas.';


ALTER TABLE analise.dia_analise ADD COLUMN quantidade_dias_geo integer;
COMMENT ON COLUMN analise.dia_analise.quantidade_dias_geo IS 'Quantidade de dias para análise geo.';

ALTER TABLE analise.gerente_tecnico RENAME TO gerente;

ALTER TABLE analise.gerente

    ADD COLUMN id_analise_geo integer,

    ADD CONSTRAINT fk_g_analise_geo FOREIGN KEY (id_analise_geo)
        REFERENCES analise.analise_geo (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

COMMENT ON COLUMN analise.gerente.id_analise_geo IS 'Identificador da entidade analise_geo que realiza o relacionamento entre as entidades gerente e analise_geo.';

ALTER TABLE analise.gerente RENAME CONSTRAINT pk_gerente_tecnico TO pk_gerente;

ALTER TABLE analise.gerente RENAME CONSTRAINT fk_gt_analise_tecnica TO fk_g_analise_tecnica;

ALTER TABLE analise.gerente RENAME CONSTRAINT fk_gt_usuario_analise TO fk_g_usuario_analise;

COMMIT;

--										73.sql

CREATE TABLE analise.rel_documento_analise_geo
(
    id_documento   INTEGER NOT NULL,
    id_analise_geo INTEGER NOT NULL,

    CONSTRAINT pk_rel_documento_analise_geo
        PRIMARY KEY (id_documento, id_analise_geo),

    CONSTRAINT fk_rdag_documento FOREIGN KEY (id_documento)
        REFERENCES analise.documento (id),

    CONSTRAINT fk_rdag_analise_geo FOREIGN KEY (id_analise_geo)
        REFERENCES analise.analise_geo (id)
);

ALTER TABLE analise.rel_documento_analise_geo OWNER TO postgres;
GRANT INSERT, SELECT,UPDATE,DELETE ON TABLE analise.rel_documento_analise_geo TO licenciamento_ap;

COMMENT ON TABLE analise.rel_documento_analise_juridica is 'Entidade responsável por armazenar a relação entre as entidades documento e análise geo.';
COMMENT ON COLUMN analise.rel_documento_analise_juridica.id_documento is 'Identificador da tabela documento, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.rel_documento_analise_juridica.id_analise_juridica is 'Identificador da tabela analise_geo, responsável pelo relacionamento entre as duas tabelas.';

--										74.sql

BEGIN;

CREATE TABLE analise.tipo_area_geometria
(
    id     INT          NOT NULL,
    codigo VARCHAR(100) NOT NULL,
    nome   VARCHAR(150) NOT NULL,

    CONSTRAINT pk_tipo_geometria PRIMARY KEY (id),

    CONSTRAINT uq_codigo_tipo_geometria_anexo UNIQUE (codigo)
);

ALTER TABLE analise.tipo_area_geometria OWNER TO postgres;
GRANT SELECT, DELETE, UPDATE, INSERT ON TABLE analise.tipo_area_geometria TO licenciamento_ap;

COMMENT ON TABLE analise.tipo_area_geometria IS 'Entidade que vai armazenar os tipos de geometrias do emprrendimento do licenciamento';
COMMENT ON COLUMN analise.tipo_area_geometria.id IS 'Identificador e chave primária da entidade';
COMMENT ON COLUMN analise.tipo_area_geometria.codigo IS 'Código de indentificação em texto da entidade';
COMMENT ON COLUMN analise.tipo_area_geometria.nome IS 'Nome de referência para os tipos de anexo de geometrias';

CREATE TABLE analise.analise_geo_anexo
(

    id                     SERIAL NOT NULL,
    id_empreendimento      INT    NOT NULL,
    id_tipo_area_geometria INT    NOT NULL,

    CONSTRAINT pk_analise_geo_anexo PRIMARY KEY (id),

    CONSTRAINT fk_aga_tipo_area_geometria FOREIGN KEY (id_tipo_area_geometria)
        REFERENCES analise.tipo_area_geometria (id),

    CONSTRAINT uq_cpf_cnpj_id_tipo_area_geometria UNIQUE (id_empreendimento, id_tipo_area_geometria)
);

SELECT addGeometryColumn('analise', 'analise_geo_anexo', 'geom', 4674, 'MULTIPOLYGON', 2);

ALTER TABLE analise.analise_geo_anexo OWNER TO postgres;
GRANT SELECT, DELETE, UPDATE, INSERT ON TABLE analise.analise_geo_anexo TO licenciamento_ap;
GRANT SELECT, USAGE ON SEQUENCE analise.analise_geo_anexo_id_seq TO licenciamento_ap;

COMMENT ON TABLE analise.analise_geo_anexo IS 'Entidade que vai armazenar os tipos de geometrias do emprrendimento do licenciamento';
COMMENT ON COLUMN analise.analise_geo_anexo.id IS 'Identificador e chave primária da entidade';
COMMENT ON COLUMN analise.analise_geo_anexo.id_empreendimento IS 'Referecia para o empreendimento que o anexo da analise geo pertence';
COMMENT ON COLUMN analise.analise_geo_anexo.id_tipo_area_geometria IS 'Referência para o tipo de área da geometria do anexo que foi feito o upload para o empreendimento';
COMMENT ON COLUMN analise.analise_geo_anexo.geom IS 'Geometria que estava no anexo que foi feito o upload';

CREATE TABLE analise.pessoa
(
    id_pessoa serial       NOT NULL,
    nome      varchar(200) NOT NULL,

    CONSTRAINT pk_pessoa PRIMARY KEY (id_pessoa)
);

ALTER TABLE analise.pessoa OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.pessoa TO licenciamento_ap;
GRANT SELECT, USAGE ON SEQUENCE analise.pessoa_id_pessoa_seq TO licenciamento_ap;

COMMENT ON TABLE analise.pessoa IS 'Entidade responsavel por armazenar as informações referentes a pessoa, utilizado pela tramitação.';
COMMENT ON COLUMN analise.pessoa.id_pessoa IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.pessoa.nome IS 'Nome ou razão social da pessoa.';

ALTER TABLE analise.licenca_analise 
    ALTER COLUMN id_analise_tecnica DROP NOT NULL,
    ALTER COLUMN id_analise_geo DROP NOT NULL;

ALTER TABLE analise.usuario_analise
    
    ADD COLUMN id_pessoa INTEGER NOT NULL,

    ADD CONSTRAINT fk_ua_pessoa FOREIGN KEY (id_pessoa)
        REFERENCES analise.pessoa (id_pessoa);

COMMENT ON COLUMN analise.usuario_analise.id_pessoa IS 'Identificador da tabela pessoa, responsável pelo relacionamento entre as duas tabelas.';

INSERT INTO analise.tipo_area_geometria (id, codigo, nome) VALUES
(1, 'HID', 'Hidrografia'),
(2, 'APP', 'Área de preservação permanente'),
(3, 'AA', 'Área Antropizada');

COMMIT;

--										75.sql

DROP TABLE analise.dispensa_licencamento_cancelada;

--										76.sql

CREATE TABLE analise.inconsistencia
(
    id                       serial  NOT NULL,
    id_analise_geo           integer NOT NULL,
    descricao_inconsistencia text    NOT NULL,
    tipo_inconsistencia      text    NOT NULL,
    categoria                text    NOT NULL,

    CONSTRAINT pk_inconsistencia PRIMARY KEY (id),

    CONSTRAINT fk_i_analise_geo FOREIGN KEY (id_analise_geo)
        REFERENCES analise.analise_geo (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE analise.inconsistencia OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.inconsistencia TO licenciamento_ap;
GRANT SELECT ON TABLE analise.inconsistencia TO tramitacao;

COMMENT ON TABLE analise.inconsistencia IS 'Entidade responsável por armazenar as análises geo.';
COMMENT ON COLUMN analise.inconsistencia.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.inconsistencia.id_analise_geo IS 'Identificador da tabela analise, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.inconsistencia.descricao_inconsistencia IS 'Campo que armazena a descrição da inconsistência relatada pelo analista.';
COMMENT ON COLUMN analise.inconsistencia.tipo_inconsistencia IS 'Campo que armazena o tipo da inconsistência presente na análise.';


CREATE TABLE analise.rel_documento_inconsistencia
(
    id_documento      integer NOT NULL,
    id_inconsistencia integer NOT NULL,

    CONSTRAINT pk_rel_documento_inconsistencia PRIMARY KEY (id_documento, id_inconsistencia),
    CONSTRAINT fk_rdi_inconsistencia FOREIGN KEY (id_inconsistencia)
        REFERENCES analise.inconsistencia (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,

    CONSTRAINT fk_rdi_documento FOREIGN KEY (id_documento)
        REFERENCES analise.documento (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE analise.rel_documento_inconsistencia OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.rel_documento_inconsistencia TO licenciamento_ap;
GRANT SELECT ON TABLE analise.rel_documento_inconsistencia TO tramitacao;

COMMENT ON TABLE analise.rel_documento_inconsistencia IS 'Entidade que armazena a relação dos documentos com a análise GEO.';
COMMENT ON COLUMN analise.rel_documento_inconsistencia.id_documento IS 'Identificador do documento.';
COMMENT ON COLUMN analise.rel_documento_inconsistencia.id_inconsistencia IS 'Identificador da inconsistência';

--										77.sql

BEGIN;

ALTER SEQUENCE analise.analise_geo_anexo_id_seq RENAME TO empreendimento_camada_geo_id_seq;

ALTER TABLE analise.analise_geo_anexo RENAME CONSTRAINT pk_analise_geo_anexo TO pk_empreendimento_camada_geo;

ALTER TABLE analise.analise_geo_anexo RENAME CONSTRAINT fk_aga_tipo_area_geometria TO fk_ecg_tipo_area_geometria;

ALTER TABLE analise.analise_geo_anexo ADD COLUMN area DOUBLE PRECISION NOT NULL DEFAULT 0;

ALTER TABLE analise.analise_geo_anexo RENAME TO empreendimento_camada_geo;

INSERT INTO analise.tipo_documento(id, nome, caminho_pasta, prefixo_nome_arquivo) VALUES
(18, 'Documento inconsistência', 'documento_inconsistencia', 'documento_inconsistencia' );

COMMIT;

--										78.sql

BEGIN;

UPDATE analise.configuracao_layer SET nome_layer='areas_restritas_am:unidade_conservacao' WHERE id = 1;
UPDATE analise.configuracao_layer SET nome_layer='areas_restritas_am:terra_indigena' WHERE id = 2;

DELETE FROM analise.configuracao_layer WHERE id in (3,4);

SELECT setval('analise.configuracao_layer_id_seq', coalesce(max(id), 1)) FROM analise.configuracao_layer;

COMMIT;

--										79.sql

ALTER TABLE analise.documento ADD COLUMN nome_arquivo TEXT;
COMMENT ON COLUMN analise.documento.nome_arquivo IS 'Nome de referência para upload e download do arquivo armazenado na máquina';

--                    80.sql

CREATE TABLE analise.desvinculo
(
    id               serial  NOT NULL,
    id_processo      integer NOT NULL,
    id_analista      integer NOT NULL,
    justificativa    text    NOT NULL,
    resposta_gerente text,
    aprovada         boolean,
    id_gerente       integer,
    data_solicitacao timestamp WITHOUT TIME ZONE,
    data_resposta    timestamp WITHOUT TIME ZONE,

    CONSTRAINT pk_desvinculo PRIMARY KEY (id),

    CONSTRAINT fk_d_processo FOREIGN KEY (id_processo)
        REFERENCES analise.processo (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,

    CONSTRAINT fk_d__analista_usuario_analise FOREIGN KEY (id_analista)
        REFERENCES analise.usuario_analise (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,

    CONSTRAINT fk_d_gerente_usuario_analise FOREIGN KEY (id_gerente)
        REFERENCES analise.usuario_analise (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE analise.desvinculo OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.desvinculo TO licenciamento_ap;
GRANT SELECT, USAGE ON SEQUENCE analise.desvinculo_id_seq TO licenciamento_ap;

COMMENT ON TABLE analise.desvinculo  IS 'Entidade responsável por armazenar o analista da analise geo.';
COMMENT ON COLUMN analise.desvinculo.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.desvinculo.id_processo IS 'Identificador da entidade processo que faz o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.desvinculo.id_analista IS 'Identificador da entidade usuario que faz o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.desvinculo.justificativa IS 'Campo para armazenar a justificativa da solicitação de desvínculo do analista.';
COMMENT ON COLUMN analise.desvinculo.resposta_gerente IS 'Campo para armazenar a resposta digitada pelo gerente.';
COMMENT ON COLUMN analise.desvinculo.aprovada IS 'Flag de controle para saber o status da solicitação de desvínculo.';
COMMENT ON COLUMN analise.desvinculo.id_gerente IS 'Identificador único da entidade gerentes que faz o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.desvinculo.data_solicitacao IS 'Data em que foi solicitado o desvínculo por um analista.';
COMMENT ON COLUMN analise.desvinculo.data_resposta IS 'Data em que o gerente responde o desvínculo.';

--										81.sql

UPDATE analise.tipo_documento SET caminho_pasta = 'parecer_analise_geo'
WHERE prefixo_nome_arquivo = 'parecer_analise_geo';

INSERT INTO analise.tipo_documento(id, nome, caminho_pasta, prefixo_nome_arquivo)
VALUES (19, 'Documento carta imagem', 'carta_imagem', 'carta_imagem_analise_geo');

--										82.sql

ALTER TABLE analise.analise_geo 
	ADD COLUMN situacao_fundiaria TEXT,
	ADD COLUMN analise_temporal TEXT,
	ADD COLUMN despacho_analista TEXT;

COMMENT ON COLUMN analise.analise_geo.situacao_fundiaria IS 'Situação fundiária do empreendimento da análise GEO';
COMMENT ON COLUMN analise.analise_geo.analise_temporal IS 'Análise temporal do empreendimento da análise GEO';
COMMENT ON COLUMN analise.analise_geo.despacho_analista IS 'Campo responsavel por armazenar o despacho/justificativa do analista GEO';

--										83.sql

COMMENT ON COLUMN analise.analise_geo.id_tipo_resultado_analise IS 'Campo responsavel por armazenar o resultado da análise do analista';

--										84.sql

ALTER TABLE analise.desvinculo
    ADD COLUMN id_analise_geo INTEGER,
    ADD COLUMN id_analise_tecnica INTEGER,
    ADD COLUMN id_analise_juridica INTEGER,

    ADD CONSTRAINT fk_d_analise_geo FOREIGN KEY (id_analise_geo)
        REFERENCES analise.analise_geo (id),
    ADD CONSTRAINT fk_d_analise_tecnica FOREIGN KEY (id_analise_tecnica)
        REFERENCES analise.analise_tecnica (id),
    ADD CONSTRAINT fk_d_analise_juridica FOREIGN KEY (id_analise_juridica)
        REFERENCES analise.analise_juridica (id),

    DROP COLUMN id_processo,
    DROP COLUMN id_analista;

COMMENT ON COLUMN analise.desvinculo.id_analise_geo IS 'Identificador da tabela id_analise_geo, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.desvinculo.id_analise_tecnica IS 'Identificador da tabela id_analise_tecnica, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.desvinculo.id_analise_juridica IS 'Identificador da tabela analise_juridica, responsável pelo relacionamento entre as duas tabelas.';

CREATE TABLE analise.comunicado
(
    id                          SERIAL                      NOT NULL,
    id_analise_geo              INTEGER,
    id_atividade_caracterizacao INTEGER                     NOT NULL,
    justificativa               TEXT,
    data_cadastro               TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    data_leitura                TIMESTAMP WITHOUT TIME ZONE,
    data_resposta               TIMESTAMP WITHOUT TIME ZONE,
    id_tipo_sobreposicao        INTEGER                     NOT NULL,
    parecer_orgao               TEXT,
    resolvido                   BOOLEAN                     NOT NULL DEFAULT FALSE,
    ativo                       BOOLEAN                     NOT NULL DEFAULT TRUE,

    CONSTRAINT pk_comunicado PRIMARY KEY (id),

    CONSTRAINT fk_c_analise_geo FOREIGN KEY (id_analise_geo)
        REFERENCES analise.analise_geo (id)
);

ALTER TABLE analise.comunicado OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.comunicado TO licenciamento_ap;
GRANT SELECT, USAGE ON SEQUENCE analise.comunicado_id_seq TO licenciamento_ap;

COMMENT ON TABLE analise.comunicado IS 'Entidade responsável por armazenar os dados referentes ao comunicado que será enviado ao órgão responsável para resolver a restrição encontrada na análise.';
COMMENT ON COLUMN analise.comunicado.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.comunicado.id_analise_geo IS 'Identificador da tabela analise_geo, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.comunicado.id_atividade_caracterizacao IS 'Identificador da tabela atividade_caracterizacao, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.comunicado.justificativa IS 'Identificador da tabela tipo_documento do schema licenciamento, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.comunicado.data_cadastro IS 'Identificador da tabela documento do schema licenciamento, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.comunicado.data_leitura IS 'Identificador da tabela analise_documento, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.comunicado.data_resposta IS 'Campo responsável por armazenar a data em que a notificação foi criada.';
COMMENT ON COLUMN analise.comunicado.id_tipo_sobreposicao IS 'Campo responsável por armazenar o id do tipo de sobreposicao.';
COMMENT ON COLUMN analise.comunicado.parecer_orgao IS 'Campo responsável por armazenar a resposta do órgão.';
COMMENT ON COLUMN analise.comunicado.resolvido IS 'Flag que indica se o comunicado foi resolvido.';
COMMENT ON COLUMN analise.comunicado.ativo IS ' Flag que indica se o comunicado está ativo.';

--										85.sql

ALTER TABLE analise.comunicado
    DROP COLUMN justificativa,
    ADD COLUMN id_orgao INTEGER NOT NULL;

COMMENT ON COLUMN analise.comunicado.id_orgao IS 
	'Campo responsável por armazenar o id do órgão responsável pela sobreposição encontrada.';

--										86.sql

ALTER TABLE analise.inconsistencia
    ADD COLUMN id_atividade_caracterizacao INTEGER,
    ADD COLUMN id_geometria_atividade INTEGER;

COMMENT ON COLUMN analise.inconsistencia.id_atividade_caracterizacao IS 'Campo responsável por armazenar o id da atividade_caracterizacao da inconsistencia.';
COMMENT ON COLUMN analise.inconsistencia.id_geometria_atividade IS 'Campo responsável por armazenar o id da geometria_atividade da inconsistencia.';

--										87.sql

COMMENT ON COLUMN analise.comunicado.data_cadastro is 'Data em que o comunicado foi cadastrado.';
COMMENT ON COLUMN analise.comunicado.data_leitura is 'Data em que o comunicado foi lido.';

ALTER TABLE analise.inconsistencia
    ADD COLUMN id_sobreposicao INTEGER;

COMMENT ON COLUMN analise.inconsistencia.id_sobreposicao IS 'Campo responsável por armazenar o id da sobreposição';

ALTER TABLE analise.comunicado RENAME COLUMN data_leitura TO data_vencimento;

COMMENT ON COLUMN analise.comunicado.data_vencimento is 'Data de venciamento do comunicado.';

--										88.sql

CREATE TABLE analise.rel_documento_comunicado
(
    id_documento  INTEGER NOT NULL,
    id_comunicado INTEGER NOT NULL,

    CONSTRAINT pk_rel_documento_comunicado PRIMARY KEY (id_documento, id_comunicado),

    CONSTRAINT fk_rdc_documento FOREIGN KEY (id_documento)
        REFERENCES analise.documento (id),

    CONSTRAINT fk_rdc_comunicado FOREIGN KEY (id_comunicado)
        REFERENCES analise.comunicado (id)
);

ALTER TABLE analise.rel_documento_comunicado OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.rel_documento_comunicado TO licenciamento_ap;

COMMENT ON TABLE analise.rel_documento_comunicado IS 'Entidade que relaciona a Entidade Documento e a Entidade Comunicado';
COMMENT ON COLUMN analise.rel_documento_comunicado.id_documento IS 'Entidade que relaciona a Entidade Documento e a Entidade Comunicado';
COMMENT ON COLUMN analise.rel_documento_comunicado.id_comunicado IS 'Entidade que relaciona a Entidade Documento e a Entidade Comunicado';

INSERT INTO analise.tipo_documento(id,nome,caminho_modelo,caminho_pasta,prefixo_nome_arquivo)
VALUES (20,'Documento comunicado órgão', NULL, 'documento_comunicado', 'documento_comunicado');

--										89.sql

BEGIN;

UPDATE analise.inconsistencia SET id_atividade_caracterizacao = ac.id_caracterizacao
FROM (
    SELECT a.id_caracterizacao, a.id FROM licenciamento.atividade_caracterizacao a
        INNER JOIN analise.inconsistencia i ON a.id = i.id_atividade_caracterizacao
    ) AS ac
WHERE id_atividade_caracterizacao = ac.id;

ALTER TABLE analise.inconsistencia RENAME COLUMN id_atividade_caracterizacao TO id_caracterizacao;
COMMENT ON COLUMN analise.inconsistencia.id_caracterizacao IS 'Campo responsável por armazenar o id da caracterizacao da inconsistencia.';


--ALTER TABLE analise.comunicado DROP CONSTRAINT fk_c_atividade_caracterizacao;

UPDATE analise.comunicado SET id_atividade_caracterizacao = ac.id_caracterizacao
FROM (
    SELECT a.id_caracterizacao, a.id FROM licenciamento.atividade_caracterizacao a
        INNER JOIN analise.comunicado c ON a.id = c.id_atividade_caracterizacao
    ) AS ac
WHERE id_atividade_caracterizacao = ac.id;

ALTER TABLE analise.comunicado RENAME COLUMN id_atividade_caracterizacao TO id_caracterizacao;

COMMENT ON COLUMN analise.comunicado.id_caracterizacao IS 'Coluna que relaciona um comunicado a uma caracterização';

COMMIT;

--                    90.sql

ALTER TABLE analise.notificacao 
  ADD COLUMN resposta_notificacao TEXT;
  
COMMENT ON COLUMN analise.notificacao.resposta_notificacao IS 'Campo responsável por armazenar a resposta do interessado';


ALTER TABLE analise.notificacao RENAME COLUMN data_cadastro TO data_notificacao;
ALTER TABLE analise.notificacao RENAME COLUMN data_leitura TO data_final_notificacao;
ALTER TABLE analise.notificacao ALTER COLUMN id_tipo_documento DROP NOT NULL;
ALTER TABLE analise.notificacao ALTER COLUMN id_analise_documento DROP NOT NULL;

--										91.sql

ALTER TABLE analise.desvinculo ADD COLUMN id_usuario INTEGER;
COMMENT ON COLUMN analise.desvinculo.id_usuario IS 'Identificador da entidade usuario_analise';

ALTER TABLE analise.desvinculo ADD COLUMN id_usuario_destino INTEGER;
COMMENT ON COLUMN analise.desvinculo.id_usuario_destino IS 'Indentificador da entidade usuario_analise para guardar o destinatário da analise';

ALTER SEQUENCE analise.gerente_tecnico_id_seq RENAME TO gerente_id_seq;

ALTER TABLE analise.gerente ALTER COLUMN id_analise_tecnica DROP NOT NULL;
ALTER TABLE analise.gerente ALTER COLUMN id_analise_geo DROP NOT NULL;


--										92.sql

UPDATE analise.tipo_documento
SET caminho_pasta        = 'notificacao_analise_geo',
    prefixo_nome_arquivo = 'notificacao_analise_geo'
WHERE id = 17;

--										93.sql

ALTER TABlE analise.inconsistencia ADD COLUMN id_sobreposicao_empreendimento INTEGER;
COMMENT ON COLUMN analise.inconsistencia.id_sobreposicao_empreendimento IS 'Id da sobreposição do empreendimento no licenciamento';

ALTER TABlE analise.inconsistencia ADD COLUMN id_sobreposicao_complexo INTEGER;
COMMENT ON COLUMN analise.inconsistencia.id_sobreposicao_complexo IS 'Id da sobreposição do complexo no licenciamento';

--										94.sql

INSERT INTO analise.tipo_documento(id,nome,caminho_modelo,caminho_pasta,prefixo_nome_arquivo)
VALUES (21,'Documento oficio ao órgão', NULL, 'documento_oficio_orgao', 'documento_oficio_orgao');

--										95.sql

ALTER TABlE analise.comunicado ADD COLUMN id_sobreposicao_empreendimento INTEGER;
ALTER TABlE analise.comunicado ADD COLUMN id_sobreposicao_complexo INTEGER;
ALTER TABlE analise.comunicado ADD COLUMN id_sobreposicao_atividade INTEGER;
COMMENT ON COLUMN analise.comunicado.id_sobreposicao_empreendimento IS 'Identificador da sobreposição do empreendimento no licenciamento';
COMMENT ON COLUMN analise.comunicado.id_sobreposicao_complexo IS 'Identificador da sobreposição do complexo no licenciamento';
COMMENT ON COLUMN analise.comunicado.id_sobreposicao_complexo IS 'Identificador da sobreposição da atividade no licenciamento';


--										96.sql

CREATE TABLE analise.setor_usuario_analise
(
    id SERIAL NOT NULL,
    id_usuario_analise INTEGER NOT NULL,
    sigla_setor TEXT NOT NULL,
    nome_setor TEXT,
  CONSTRAINT pk_setor_usuario_analise PRIMARY KEY (id),
  CONSTRAINT fk_sua_usuario_analise FOREIGN KEY (id_usuario_analise)
      REFERENCES analise.usuario_analise (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE analise.setor_usuario_analise
  OWNER TO postgres;
GRANT ALL ON TABLE analise.setor_usuario_analise TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.setor_usuario_analise TO licenciamento_ap;
GRANT SELECT, USAGE ON SEQUENCE analise.setor_usuario_analise_id_seq TO licenciamento_ap; 

COMMENT ON TABLE analise.setor_usuario_analise
  IS 'Entidade responsável por armazenar o setor do usuário.';
COMMENT ON COLUMN analise.setor_usuario_analise.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.setor_usuario_analise.id_usuario_analise IS 'Identificador da entidade usuario_analise que realiza o relacionamento entre as entidades usuario_analise e setor_usuario_analise.';
COMMENT ON COLUMN analise.setor_usuario_analise.sigla_setor IS 'Sigla do setor do usuário.';
COMMENT ON COLUMN analise.setor_usuario_analise.nome_setor IS 'Nome do setor do usuário.';


CREATE TABLE analise.perfil_usuario_analise
(
	id SERIAL NOT NULL,
	id_usuario_analise INTEGER NOT NULL,
	codigo_perfil TEXT NOT NULL,
	nome_perfil TEXT,
  CONSTRAINT pk_perfil_usuario_analise PRIMARY KEY (id),
  CONSTRAINT fk_pua_usuario_analise FOREIGN KEY (id_usuario_analise)
      REFERENCES analise.usuario_analise (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE analise.perfil_usuario_analise
  OWNER TO postgres;
GRANT ALL ON TABLE analise.perfil_usuario_analise TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.perfil_usuario_analise TO licenciamento_ap;
GRANT SELECT, USAGE ON SEQUENCE analise.perfil_usuario_analise_id_seq TO licenciamento_ap; 

COMMENT ON TABLE analise.perfil_usuario_analise
  IS 'Entidade responsável por armazenar o perfil do usuário.';
COMMENT ON COLUMN analise.perfil_usuario_analise.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.perfil_usuario_analise.id_usuario_analise IS 'Identificador da entidade usuario_analise que realiza o relacionamento entre as entidades usuario_analise e perfil_usuario_analise.';
COMMENT ON COLUMN analise.perfil_usuario_analise.codigo_perfil IS 'Sigla do perfil do usuário.';
COMMENT ON COLUMN analise.perfil_usuario_analise.nome_perfil IS 'Nome do perfil do usuário.';


--										98.sql

INSERT INTO analise.tipo_documento(
            id, nome, caminho_modelo, caminho_pasta, prefixo_nome_arquivo)
    VALUES (22, 'Documento notificaçao análise geo', null, 'documento_notificacao_analise_geo', 'documento_notificacao_analise_geo');

CREATE TABLE analise.rel_documento_notificacao
(
    id_documento   INTEGER NOT NULL,
    id_notificacao INTEGER NOT NULL,

    CONSTRAINT pk_rel_documento_notificacao
        PRIMARY KEY (id_documento, id_notificacao),

    CONSTRAINT fk_rdag_documento FOREIGN KEY (id_documento)
        REFERENCES analise.documento (id),

    CONSTRAINT fk_rdag_notificacao FOREIGN KEY (id_notificacao)
        REFERENCES analise.notificacao (id)
);

ALTER TABLE analise.rel_documento_notificacao OWNER TO postgres;
GRANT INSERT, SELECT,UPDATE,DELETE ON TABLE analise.rel_documento_notificacao TO licenciamento_ap;

COMMENT ON TABLE analise.rel_documento_notificacao is 'Entidade responsável por armazenar a relação entre as entidades documento e notificação.';
COMMENT ON COLUMN analise.rel_documento_notificacao.id_documento is 'Identificador da tabela documento, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.rel_documento_notificacao.id_notificacao is 'Identificador da tabela notificacao, responsável pelo relacionamento entre as duas tabelas.';

COMMENT ON TABLE analise.rel_documento_analise_geo is 'Entidade responsável por armazenar a relação entre as entidades documento e análise geo.';
COMMENT ON COLUMN analise.rel_documento_analise_geo.id_documento is 'Identificador da tabela documento, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.rel_documento_analise_geo.id_analise_geo is 'Identificador da tabela analise_geo, responsável pelo relacionamento entre as duas tabelas.';

COMMENT ON TABLE analise.rel_documento_analise_juridica is 'Entidade responsável por armazenar a relação entre as entidades documento e análise juridica.';
COMMENT ON COLUMN analise.rel_documento_analise_juridica.id_documento is 'Identificador da tabela documento, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.rel_documento_analise_juridica.id_analise_juridica is 'Identificador da tabela analise_juridica, responsável pelo relacionamento entre as duas tabelas.';

ALTER TABLE analise.notificacao ADD COLUMN documentacao BOOLEAN;
COMMENT ON COLUMN analise.notificacao.documentacao IS 'Indica se a notificação necessita de documentação.';

ALTER TABLE analise.notificacao ADD COLUMN retificacao_empreendimento BOOLEAN;
COMMENT ON COLUMN analise.notificacao.retificacao_empreendimento IS 'Indica se a notificação necessita de retificação do empreendimento.';

ALTER TABLE analise.notificacao ADD COLUMN retificacao_solicitacao BOOLEAN;
COMMENT ON COLUMN analise.notificacao.retificacao_solicitacao IS 'Indica se a notificação necessita retificação da solicitação.';

ALTER TABLE analise.notificacao ADD COLUMN retificacao_solicitacao_com_geo BOOLEAN;
COMMENT ON COLUMN analise.notificacao.retificacao_solicitacao_com_geo IS 'Indica se a notificação possui retificacao da solicitação com geometria.';



--										99.sql


ALTER TABLE analise.processo ADD COLUMN id_caracterizacao INTEGER;
COMMENT ON COLUMN analise.processo.id_caracterizacao IS 'Id da caracterização da solicitação do licenciamento ambiental relacionada ao processo';

UPDATE analise.processo p SET id_caracterizacao = pc.id_caracterizacao FROM analise.rel_processo_caracterizacao pc WHERE p.id = pc.id_processo;

ALTER TABLE analise.processo ALTER COLUMN id_caracterizacao SET NOT NULL;

DROP TABLE analise.rel_processo_caracterizacao;

--                    100.sql

ALTER TABLE analise.notificacao ADD COLUMN prazo_notificacao INTEGER;
COMMENT ON COLUMN analise.notificacao.prazo_notificacao IS 'Campo resposável por armazenar o prazo em dias para o atendimento da notificação.';

ALTER TABLE analise.notificacao RENAME COLUMN resposta_notificacao TO justificativa_documentacao;
COMMENT ON COLUMN analise.notificacao.justificativa_documentacao IS 'Campo resposável por armazenar a justificativa adicionada no licenciamento referente ao atendimento de documentação.';

ALTER TABLE analise.notificacao ADD COLUMN justificativa_retificacao_empreendimento TEXT;
COMMENT ON COLUMN analise.notificacao.justificativa_retificacao_empreendimento IS 'Campo resposável por armazenar a justificativa adicionada no licenciamento referente ao atendimento da retificação do empreendimento.';

ALTER TABLE analise.notificacao ADD COLUMN justificativa_retificacao_solicitacao TEXT;
COMMENT ON COLUMN analise.notificacao.justificativa_retificacao_solicitacao IS 'Campo resposável por armazenar a justificativa adicionada no licenciamento referente ao atendimento da retificação da solicitação.';

--                    101.sql

INSERT INTO analise.tipo_documento (id,nome,caminho_modelo,caminho_pasta,prefixo_nome_arquivo) VALUES (23,'Documento análise temporal',NULL,'documento_analise_temporal','documento_analise_temporal');

--                    102.sql
ALTER TABLE analise.analise_geo DROP COLUMN id_tipo_resultado_validacao_gerente, DROP COLUMN parecer_validacao_gerente, DROP COLUMN id_usuario_validacao_gerente;

CREATE TABLE analise.parecer_gerente_analise_geo (
    id integer NOT NULL,
    id_tipo_resultado_analise integer NOT NULL,
    parecer text NOT NULL,
    data_parecer timestamp default now(),
    id_usuario_gerente integer NOT NULL,
    id_analise_geo integer NOT NULL,
    CONSTRAINT pk_parecer_gerente_analise_geo PRIMARY KEY(id),
    CONSTRAINT fk_pgag_tipo_resultado_analise FOREIGN KEY(id_tipo_resultado_analise) 
        REFERENCES  analise.tipo_resultado_analise(id),
    CONSTRAINT fk_pgag_usuario_analise FOREIGN KEY(id_usuario_gerente) 
        REFERENCES  analise.usuario_analise(id),
    CONSTRAINT fk_pgag_analise_geo FOREIGN KEY(id_analise_geo) 
        REFERENCES  analise.analise_geo(id)
);
ALTER TABLE analise.parecer_gerente_analise_geo OWNER TO postgres;
ALTER TABLE analise.parecer_gerente_analise_geo OWNER TO licenciamento_ap;
GRANT SELECT,INSERT,UPDATE,DELETE ON TABLE analise.parecer_gerente_analise_geo TO licenciamento_ap;
GRANT ALL ON TABLE analise.parecer_gerente_analise_geo TO postgres;
COMMENT ON TABLE analise.parecer_gerente_analise_geo is 'Entidade responsável por armazenar informações sobre o parecer do gerente em uma análise técnica';
COMMENT ON COLUMN analise.parecer_gerente_analise_geo.id is 'Id do parecer do gerente';
COMMENT ON COLUMN analise.parecer_gerente_analise_geo.id_tipo_resultado_analise is 'Tipo do resultado da análise';
COMMENT ON COLUMN analise.parecer_gerente_analise_geo.parecer is 'Descrição do parecer feito pelo gerente';
COMMENT ON COLUMN analise.parecer_gerente_analise_geo.data_parecer is 'Data do parecer';
COMMENT ON COLUMN analise.parecer_gerente_analise_geo.id_usuario_gerente is 'Id do gerente responsável pelo parecer';
COMMENT ON COLUMN analise.parecer_gerente_analise_geo.id_analise_geo is 'Id da análise geo que teve parecer';

CREATE SEQUENCE analise.parecer_gerente_analise_geo_id_seq 
    INCREMENT 1
    MINVALUE 1
    NO MAXVALUE
    START 1
    CACHE 1;
ALTER TABLE analise.parecer_gerente_analise_geo ALTER COLUMN id SET DEFAULT pg_catalog.nextval('analise.parecer_gerente_analise_geo_id_seq');
ALTER TABLE analise.parecer_gerente_analise_geo_id_seq OWNER TO postgres;
GRANT SELECT, USAGE ON SEQUENCE analise.parecer_gerente_analise_geo_id_seq TO licenciamento_ap; 
SELECT setval('analise.parecer_gerente_analise_geo_id_seq', coalesce(max(id), 1)) FROM analise.parecer_gerente_analise_geo;

--                    103.sql


ALTER TABLE analise.parecer_gerente_analise_geo ADD COLUMN id_historico_tramitacao integer NOT NULL;
COMMENT ON COLUMN analise.parecer_gerente_analise_geo.id_historico_tramitacao IS 'Identificador da tramitação gerada pelo parecer.';

ALTER TABLE analise.analise_geo DROP COLUMN parecer, DROP COLUMN id_tipo_resultado_analise, DROP COLUMN situacao_fundiaria, DROP COLUMN analise_temporal, DROP COLUMN despacho_analista;

CREATE TABLE analise.parecer_analista_geo (
    id integer NOT NULL,
    id_analise_geo integer NOT NULL,
    id_tipo_resultado_analise integer NOT NULL,
    parecer text NOT NULL,
    data_parecer timestamp default now(),
    id_usuario_analista_geo integer NOT NULL,
    id_historico_tramitacao integer NOT NULL,
    situacao_fundiaria TEXT,
    analise_temporal TEXT,
    conclusao TEXT NOT NULL,
    CONSTRAINT pk_parecer_analista_geo PRIMARY KEY(id),
    CONSTRAINT fk_pag_analise_geo FOREIGN KEY(id_analise_geo)
        REFERENCES analise.analise_geo(id), 
    CONSTRAINT fk_pag_tipo_resultado_analise FOREIGN KEY(id_tipo_resultado_analise)
        REFERENCES analise.tipo_resultado_analise(id),
    CONSTRAINT fk_pag_usuario_analise FOREIGN KEY(id_usuario_analista_geo)
        REFERENCES analise.usuario_analise(id)    
);
ALTER TABLE analise.parecer_analista_geo OWNER TO postgres;
ALTER TABLE analise.parecer_analista_geo OWNER TO licenciamento_ap;
GRANT SELECT,INSERT,UPDATE,DELETE ON TABLE analise.parecer_analista_geo TO licenciamento_ap;
GRANT ALL ON TABLE analise.parecer_analista_geo TO postgres;
COMMENT ON TABLE analise.parecer_analista_geo IS 'Entidade responsável por armazenar informações sobre o parecer do analista geo em uma análise técnica';
COMMENT ON COLUMN analise.parecer_analista_geo.id IS 'Id do parecer do analista';
COMMENT ON COLUMN analise.parecer_analista_geo.id_analise_geo IS 'Id da analise';
COMMENT ON COLUMN analise.parecer_analista_geo.id_tipo_resultado_analise IS 'Tipo do resultado da análise';
COMMENT ON COLUMN analise.parecer_analista_geo.parecer IS 'Descrição do parecer feito pelo analista';
COMMENT ON COLUMN analise.parecer_analista_geo.data_parecer IS 'Data do parecer';
COMMENT ON COLUMN analise.parecer_analista_geo.id_usuario_analista_geo IS 'Id do analista geo responsável pelo parecer';
COMMENT ON COLUMN analise.parecer_analista_geo.id_historico_tramitacao IS 'Identificador da tramitação gerada pelo parecer.';
COMMENT ON COLUMN analise.parecer_analista_geo.situacao_fundiaria IS 'Situação fundiária do empreendimento da análise GEO.';
COMMENT ON COLUMN analise.parecer_analista_geo.analise_temporal IS 'Análise temporal do empreendimento da análise GEO';
COMMENT ON COLUMN analise.parecer_analista_geo.conclusao IS 'Notas de conclusão da análise.';


CREATE SEQUENCE analise.parecer_analista_geo_id_seq 
    INCREMENT 1
    MINVALUE 1
    NO MAXVALUE
    START 1
    CACHE 1;
ALTER TABLE analise.parecer_analista_geo ALTER COLUMN id SET DEFAULT pg_catalog.nextval('analise.parecer_analista_geo_id_seq');
ALTER TABLE analise.parecer_analista_geo_id_seq OWNER TO postgres;
GRANT SELECT, USAGE ON SEQUENCE analise.parecer_analista_geo_id_seq TO licenciamento_ap; 
SELECT setval('analise.parecer_analista_geo_id_seq', coalesce(max(id), 1)) FROM analise.parecer_analista_geo;   


--                    104.sql

CREATE TABLE analise.rel_documento_parecer_analista_geo (
    id_documento integer NOT NULL,
    id_parecer_analista_geo integer NOT NULL,
    CONSTRAINT fk_rdpag_documento FOREIGN KEY(id_documento)
        REFERENCES analise.documento(id),
    CONSTRAINT fk_rdpag_parecer_analista_geo FOREIGN KEY(id_parecer_analista_geo)
        REFERENCES analise.parecer_analista_geo(id)
);
ALTER TABLE analise.rel_documento_parecer_analista_geo OWNER TO postgres;
ALTER TABLE analise.rel_documento_parecer_analista_geo OWNER TO licenciamento_ap;
GRANT SELECT,INSERT,UPDATE,DELETE ON TABLE analise.rel_documento_parecer_analista_geo TO licenciamento_ap;
GRANT ALL ON TABLE analise.rel_documento_parecer_analista_geo TO postgres;
COMMENT ON TABLE analise.rel_documento_parecer_analista_geo IS 'Entidade responsável por relacionar documentos de um parecer de um analista geo';
COMMENT ON COLUMN analise.rel_documento_parecer_analista_geo.id_documento IS 'Identificador do documento';
COMMENT ON COLUMN analise.rel_documento_parecer_analista_geo.id_parecer_analista_geo IS 'Identificador do parecer do analista geo';

--                    105.sql
CREATE TABLE analise.desvinculo_analise_tecnica
(
  id serial NOT NULL, -- Identificador único da entidade.
  justificativa text NOT NULL, -- Campo para armazenar a justificativa da solicitação de desvínculo do analista.
  resposta_gerente text, -- Campo para armazenar a resposta digitada pelo gerente.
  aprovada boolean, -- Flag de controle para saber o status da solicitação de desvínculo.
  id_gerente integer, -- Identificador único da entidade gerentes que faz o relacionamento entre as duas entidades.
  data_solicitacao timestamp without time zone, -- Data em que foi solicitado o desvínculo por um analista.
  data_resposta timestamp without time zone, -- Data em que o gerente responde o desvínculo.
  id_analise_tecnica integer, -- Identificador da tabela id_analise_geo, responsável pelo relacionamento entre as duas tabelas.
  id_usuario integer, -- Identificador da entidade usuario_analise
  id_usuario_destino integer, -- Indentificador da entidade usuario_analise para guardar o destinatário da analise
  CONSTRAINT pk_desvinculo_analise_tecnica PRIMARY KEY (id),
  CONSTRAINT fk_dat_gerente_usuario_analise FOREIGN KEY (id_gerente)
      REFERENCES analise.usuario_analise (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE analise.desvinculo_analise_tecnica OWNER TO postgres;
GRANT ALL ON TABLE analise.desvinculo_analise_tecnica TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.desvinculo_analise_tecnica TO licenciamento_ap;
GRANT SELECT ON TABLE analise.desvinculo_analise_tecnica TO tramitacao;
COMMENT ON TABLE analise.desvinculo_analise_tecnica IS 'Entidade responsável por armazenar o analista da analise geo.';
COMMENT ON COLUMN analise.desvinculo_analise_tecnica.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.desvinculo_analise_tecnica.justificativa IS 'Campo para armazenar a justificativa da solicitação de desvínculo do analista.';
COMMENT ON COLUMN analise.desvinculo_analise_tecnica.resposta_gerente IS 'Campo para armazenar a resposta digitada pelo gerente.';
COMMENT ON COLUMN analise.desvinculo_analise_tecnica.aprovada IS 'Flag de controle para saber o status da solicitação de desvínculo.';
COMMENT ON COLUMN analise.desvinculo_analise_tecnica.id_gerente IS 'Identificador único da entidade gerentes que faz o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.desvinculo_analise_tecnica.data_solicitacao IS 'Data em que foi solicitado o desvínculo por um analista.';
COMMENT ON COLUMN analise.desvinculo_analise_tecnica.data_resposta IS 'Data em que o gerente responde o desvínculo.';
COMMENT ON COLUMN analise.desvinculo_analise_tecnica.id_analise_tecnica IS 'Identificador da tabela id_analise_geo, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.desvinculo_analise_tecnica.id_usuario IS 'Identificador da entidade usuario_analise';
COMMENT ON COLUMN analise.desvinculo_analise_tecnica.id_usuario_destino IS 'Indentificador da entidade usuario_analise para guardar o destinatário da analise';


ALTER TABLE analise.desvinculo RENAME TO desvinculo_analise_geo;

ALTER TABLE analise.desvinculo_analise_geo RENAME CONSTRAINT pk_desvinculo TO pk_desvinculo_analise_geo;

ALTER TABLE analise.desvinculo_analise_geo RENAME CONSTRAINT fk_d_gerente_usuario_analise TO fk_dag_gerente_usuario_analise;

ALTER TABLE analise.desvinculo_analise_geo  DROP COLUMN id_analise_tecnica; 

ALTER TABLE analise.desvinculo_analise_geo  DROP COLUMN id_analise_juridica;


--                    106.sql

ALTER TABLE analise.inconsistencia ADD COLUMN id_atividade_caracterizacao INTEGER;
COMMENT ON COLUMN analise.inconsistencia.id_atividade_caracterizacao IS 'Campo responsável por armazenar o id da atividade de caracterização quando a categoria é ATIVIDADE.';

--                    107.sql

ALTER TABLE analise.notificacao ADD COLUMN segundo_email_enviado BOOLEAN NOT NULL DEFAULT FALSE;
COMMENT ON COLUMN analise.notificacao.segundo_email_enviado IS 'Indica se o segundo email da notificação foi enviado pelo vencimento do prazo da notificação';

--                    108.sql

INSERT INTO analise.tipo_documento (id, nome, caminho_modelo, caminho_pasta, prefixo_nome_arquivo) 
  VALUES (24, 'Documento RIT', null, 'documento_rit', 'documento_rit');
INSERT INTO analise.tipo_documento (id, nome, caminho_modelo, caminho_pasta, prefixo_nome_arquivo) 
  VALUES (25, 'Documento vistoria', null, 'documento_vistoria', 'documento_vistoria');

--                    109.sql

ALTER TABLE analise.notificacao ADD COLUMN id_parecer_analista_geo INTEGER NOT NULL;
COMMENT ON COLUMN analise.notificacao.id_parecer_analista_geo IS 'Identificador do parecer do analista geo';

ALTER TABLE analise.notificacao ADD CONSTRAINT fk_n_parecer_analista_geo FOREIGN KEY (id_parecer_analista_geo) REFERENCES analise.parecer_analista_geo(id); 

ALTER TABLE analise.notificacao DROP COLUMN justificativa;

--										110.sql


CREATE TABLE analise.vistoria (
    id integer NOT NULL,
    realizada boolean NOT NULL,
    tx_conclusao text NOT NULL,
    id_documento_rit integer,
    data date,
    hora date,
    tx_descricao text,
    tx_cursos_dagua text,
    tx_tipologia_vegetal text,
    tx_app text,
    tx_ocorrencia text,
    tx_residuos_liquidos text,
    tx_outras_informacoes text,
    id_analise_tecnica integer NOT NULL,
    CONSTRAINT pk_vistoria PRIMARY KEY(id),
    CONSTRAINT fk_v_documento FOREIGN KEY(id_documento_rit)  
        REFERENCES analise.documento(id),
    CONSTRAINT fk_v_analise_tecnica FOREIGN KEY(id_analise_tecnica) 
        REFERENCES analise.analise_tecnica(id)
);
ALTER TABLE analise.vistoria OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.vistoria TO licenciamento_ap;
COMMENT ON TABLE analise.vistoria is 'Entidade responsável por armazenar informações da vistoria da análise técnica';
COMMENT ON COLUMN analise.vistoria.id IS 'Identificador único da entidade vistoria';
COMMENT ON COLUMN analise.vistoria.realizada IS 'Flag responsável por dizer se uma vistoria foi realizada ou não em uma análise técnica';
COMMENT ON COLUMN analise.vistoria.tx_conclusao IS 'Conclusão da vistoria, sendo ela realiza ou não';
COMMENT ON COLUMN analise.vistoria.id_documento_rit IS 'Documento de Registro de Inspeção Técnica da vistoria realizada';
COMMENT ON COLUMN analise.vistoria.data IS 'Data da vistoria realizada';
COMMENT ON COLUMN analise.vistoria.hora IS 'Hora da vistoria realizada';
COMMENT ON COLUMN analise.vistoria.tx_descricao IS 'Descrição da atividade da vistoria realizada';
COMMENT ON COLUMN analise.vistoria.tx_cursos_dagua IS 'Cursos de agua da vistoria realizada';
COMMENT ON COLUMN analise.vistoria.tx_tipologia_vegetal IS 'Tipologia vegtal da vistoria realizada';
COMMENT ON COLUMN analise.vistoria.tx_app IS 'Áreas de Preservação Permanente da vistoria realizada';
COMMENT ON COLUMN analise.vistoria.tx_ocorrencia IS 'Ocorrência de processos erosivos da vistoria realizada';
COMMENT ON COLUMN analise.vistoria.tx_residuos_liquidos IS 'Gestão de resíduos sólidos e líquidos gerados na atividade produtiva da vistoria realizada';
COMMENT ON COLUMN analise.vistoria.tx_outras_informacoes IS ' Outras informações da vistoria realizada';
COMMENT ON COLUMN analise.vistoria.id_analise_tecnica IS 'Identificador da análise técnica relacionada à vistoria';



CREATE TABLE analise.equipe_vistoria (
    id_vistoria integer NOT NULL,
    id_usuario  integer NOT NULL,
    CONSTRAINT fk_ev_vistoria FOREIGN KEY(id_vistoria)
        REFERENCES analise.vistoria(id),
    CONSTRAINT fk_ev_usuario_analise FOREIGN KEY(id_usuario)
        REFERENCES analise.usuario_analise(id)
);
ALTER TABLE analise.equipe_vistoria OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.equipe_vistoria TO licenciamento_ap;
COMMENT ON TABLE analise.equipe_vistoria is 'Entidade responsável por armazenar a equipe da vistoria de uma análise técnica caso a mesma foi realizada';
COMMENT ON COLUMN analise.equipe_vistoria.id_vistoria IS 'Identificador da vistoria';
COMMENT ON COLUMN analise.equipe_vistoria.id_usuario IS 'Identificador do usuário relacionado a equipe de uma vistoria';


CREATE TABLE analise.rel_documento_vistoria (
    id_documento integer NOT NULL,
    id_vistoria  integer NOT NULL,
    CONSTRAINT pk_rel_documento_vistoria PRIMARY KEY (id_documento, id_vistoria),
    CONSTRAINT fk_rdv_documento FOREIGN KEY(id_documento)
        REFERENCES analise.documento,
    CONSTRAINT fk_rdv_vistoria FOREIGN KEY(id_vistoria)
        REFERENCES analise.vistoria(id)
);
ALTER TABLE analise.rel_documento_vistoria OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE  analise.rel_documento_vistoria TO licenciamento_ap;
COMMENT ON  TABLE analise.rel_documento_vistoria is 'Entidade responsável por armazenar os documentos de uma vistoria de análise técnica';
COMMENT ON COLUMN analise.rel_documento_vistoria.id_documento IS 'Identificador do documento da vistoria';
COMMENT ON COLUMN analise.rel_documento_vistoria.id_vistoria IS 'Identificador da vistoria';



CREATE TABLE analise.inconsistencia_vistoria (
  id serial NOT NULL, 
  id_vistoria integer, 
  descricao_inconsistencia text NOT NULL, 
  CONSTRAINT pk_inconsistencia_vistoria PRIMARY KEY (id),
  CONSTRAINT fk_iv_vistoria FOREIGN KEY(id_vistoria)
      REFERENCES analise.vistoria(id) 
);
ALTER TABLE analise.inconsistencia_vistoria OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.inconsistencia_vistoria TO licenciamento_ap;
GRANT SELECT, USAGE ON SEQUENCE analise.inconsistencia_vistoria_id_seq TO licenciamento_ap; 
COMMENT ON  TABLE analise.inconsistencia_vistoria IS 'Entidade responsável por armazenar as análises geo.';
COMMENT ON  COLUMN analise.inconsistencia_vistoria.id IS 'Identificador único da entidade.';
COMMENT ON  COLUMN analise.inconsistencia_vistoria.id_vistoria IS 'Identificador da tabela vistoria, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON  COLUMN analise.inconsistencia_vistoria.descricao_inconsistencia IS 'Campo que armazena a descrição da inconsistência relatada pelo analista.';


CREATE TABLE analise.rel_documento_inconsistencia_vistoria (
    id_inconsistencia_vistoria integer NOT NULL,
    id_documento  integer NOT NULL,
    CONSTRAINT pk_rel_documento_inconsistencia_vistoria PRIMARY KEY (id_inconsistencia_vistoria, id_documento),
    CONSTRAINT fk_rdiv_inconsistencia_vistoria FOREIGN KEY(id_inconsistencia_vistoria)
        REFERENCES analise.inconsistencia_vistoria(id),
    CONSTRAINT fk_rdiv_documento FOREIGN KEY(id_documento)
        REFERENCES analise.documento(id)
);
ALTER TABLE analise.rel_documento_inconsistencia_vistoria OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.rel_documento_inconsistencia_vistoria TO licenciamento_ap;
COMMENT ON  TABLE analise.rel_documento_inconsistencia_vistoria is 'Entidade responsável por armazenar documentos da inconsistência da vistoria';
COMMENT ON COLUMN analise.rel_documento_inconsistencia_vistoria.id_inconsistencia_vistoria IS 'Identificador da inconsistência relacionada a vistoria';
COMMENT ON COLUMN analise.rel_documento_inconsistencia_vistoria.id_documento IS 'Identificador do documento da inconsistência relacionada a vistoria';



INSERT INTO analise.tipo_documento (id, nome, caminho_modelo, caminho_pasta, prefixo_nome_arquivo) 
    VALUES (27, 'Documentos inconsistência vistoria', null, 'documento_inconsistencia_vistoria', 'documento_inconsistencia_vistoria');

CREATE SEQUENCE analise.vistoria_id_seq
    INCREMENT 1
    MINVALUE 1
    NO MAXVALUE
    START 1
    CACHE 1;
ALTER TABLE analise.vistoria ALTER COLUMN id SET DEFAULT pg_catalog.nextval('analise.vistoria_id_seq');
ALTER TABLE analise.vistoria_id_seq OWNER TO postgres;
GRANT SELECT, USAGE ON SEQUENCE analise.vistoria_id_seq TO licenciamento_ap;
SELECT setval('analise.vistoria_id_seq', coalesce(max(id), 1)) FROM analise.vistoria;

--										111.sql


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
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.inconsistencia_tecnica TO licenciamento_ap;
GRANT SELECT ON TABLE analise.inconsistencia_tecnica TO tramitacao;
GRANT SELECT,USAGE ON SEQUENCE analise.inconsistencia_tecnica_id_seq TO licenciamento_ap;
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
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.inconsistencia_tecnica TO licenciamento_ap;
GRANT SELECT,USAGE ON SEQUENCE analise.inconsistencia_tecnica_atividade_id_seq TO licenciamento_ap;
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
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.inconsistencia_tecnica_parametro TO licenciamento_ap;
GRANT SELECT,USAGE ON SEQUENCE analise.inconsistencia_tecnica_parametro_id_seq TO licenciamento_ap;
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
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.inconsistencia_tecnica_tipo_licenca TO licenciamento_ap;
GRANT SELECT,USAGE ON SEQUENCE analise.inconsistencia_tecnica_tipo_licenca_id_seq TO licenciamento_ap;
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
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.inconsistencia_tecnica_questionario TO licenciamento_ap;
GRANT SELECT,USAGE ON SEQUENCE analise.inconsistencia_tecnica_questionario_id_seq TO licenciamento_ap;
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
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.inconsistencia_tecnica_documento TO licenciamento_ap;
GRANT SELECT,USAGE ON SEQUENCE analise.inconsistencia_tecnica_documento_id_seq TO licenciamento_ap;
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
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.rel_documento_inconsistencia_tecnica TO licenciamento_ap;
GRANT SELECT ON TABLE analise.rel_documento_inconsistencia_tecnica TO tramitacao;



INSERT INTO analise.tipo_documento(id, nome, caminho_pasta, prefixo_nome_arquivo) 
  VALUES(26, 'Documento inconsistência técnica', 'documento_inconsistencia_tecnica', 'documento_inconsistencia_tecnica' );


--										112.sql


ALTER TABLE analise.parecer_analista_geo ALTER COLUMN id_historico_tramitacao DROP NOT NULL;

--										113.sql


INSERT INTO analise.tipo_documento( id, nome, caminho_modelo, caminho_pasta, prefixo_nome_arquivo)
 VALUES (28, 'Parecer análise técnica', null, 'parecer_analise_tecnica', 'parecer_analise_tecnica');



--										114.sql


ALTER TABLE analise.perfil_usuario_analise 
    DROP CONSTRAINT fk_pua_usuario_analise,
    DROP COLUMN id,
    ADD CONSTRAINT fk_pua_usuario_analise PRIMARY KEY (id_usuario_analise,codigo_perfil);

ALTER TABLE analise.setor_usuario_analise 
    DROP CONSTRAINT fk_sua_usuario_analise,
    DROP COLUMN id,
    ADD CONSTRAINT fk_sua_usuario_analise PRIMARY KEY (id_usuario_analise, sigla_setor);

    
--										115.sql


DROP TABLE analise.inconsistencia_tecnica_documento;


CREATE TABLE analise.inconsistencia_tecnica_documento_tecnico_ambiental (
  id serial NOT NULL, 
  id_inconsistencia_tecnica integer NOT NULL, 
  id_documento_tecnico integer NOT NULL, 
  CONSTRAINT pk_inconsistencia_tecnica_documento_tecnico_ambiental PRIMARY KEY (id),
  CONSTRAINT fk_itdta_inconsistencia_tecnica FOREIGN KEY (id_inconsistencia_tecnica)
      REFERENCES analise.inconsistencia_tecnica (id)
);
ALTER TABLE analise.inconsistencia_tecnica_documento_tecnico_ambiental OWNER TO licenciamento_ap;
ALTER TABLE analise.inconsistencia_tecnica_documento_tecnico_ambiental OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.inconsistencia_tecnica_documento_tecnico_ambiental TO licenciamento_ap;
GRANT SELECT,USAGE ON SEQUENCE analise.inconsistencia_tecnica_documento_tecnico_ambiental_id_seq TO licenciamento_ap;
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
      REFERENCES analise.inconsistencia_tecnica (id) 
);
ALTER TABLE analise.inconsistencia_tecnica_documento_administrativo OWNER TO licenciamento_ap;
ALTER TABLE analise.inconsistencia_tecnica_documento_administrativo OWNER TO licenciamento_ap;
ALTER TABLE analise.inconsistencia_tecnica_documento_administrativo OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.inconsistencia_tecnica_documento_administrativo TO licenciamento_ap;
GRANT SELECT,USAGE ON SEQUENCE analise.inconsistencia_tecnica_documento_administrativo_id_seq TO licenciamento_ap;
COMMENT ON TABLE analise.inconsistencia_tecnica_documento_administrativo IS 'Tabela responsável por armazenar todas informações das inconsistências adicionadas pelo analista técnico com relaçao aos documentos';
COMMENT ON COLUMN analise.inconsistencia_tecnica_documento_administrativo.id IS 'Identificador único da entidade';
COMMENT ON COLUMN analise.inconsistencia_tecnica_documento_administrativo.id_inconsistencia_tecnica IS 'Identificador que faz relacionamento com a tabela inconsistencia_tecnica';
COMMENT ON COLUMN analise.inconsistencia_tecnica_documento_administrativo.id_documento_administrativo IS 'Identificador que faz relacionamento com a tabela licenciamento.id_documento_administrativo';


    
--										116.sql


CREATE TABLE analise.parecer_analista_tecnico (
    id integer NOT NULL,
    id_analise_tecnica integer NOT NULL,
    do_processo text,
    da_analise_tecnica text,
    da_conclusao text,
    data timestamp,
    id_tipo_resultado_analise integer,
    id_usuario_analista_tecnico integer,
    CONSTRAINT pk_parecer_analista_tecnico PRIMARY KEY (id),
    CONSTRAINT fk_pat_analise_tecnica FOREIGN KEY (id_analise_tecnica)
        REFERENCES analise.analise_tecnica (id),
    CONSTRAINT fk_pat_tipo_resultado_analise FOREIGN KEY (id_tipo_resultado_analise)
        REFERENCES analise.tipo_resultado_analise (id),
     CONSTRAINT fk_pat_usuario_analise FOREIGN KEY (id_usuario_analista_tecnico)
        REFERENCES analise.usuario_analise (id)
);
ALTER TABLE analise.parecer_analista_tecnico OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.parecer_analista_tecnico TO licenciamento_ap;
COMMENT ON TABLE analise.parecer_analista_tecnico IS 'Entidade responsável por armazenar os pareceres dos analistas técnicos';
COMMENT ON COLUMN analise.parecer_analista_tecnico.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.parecer_analista_tecnico.id_analise_tecnica IS 'Identificador da análise técnica ligada ao parecer';
COMMENT ON COLUMN analise.parecer_analista_tecnico.do_processo IS 'Texto do parecer';
COMMENT ON COLUMN analise.parecer_analista_tecnico.da_analise_tecnica IS 'Texto do parecer';
COMMENT ON COLUMN analise.parecer_analista_tecnico.da_conclusao IS 'Texto do parecer';
COMMENT ON COLUMN analise.parecer_analista_tecnico.data IS 'Data do parecer tramitado';
COMMENT ON COLUMN analise.parecer_analista_tecnico.id_tipo_resultado_analise IS 'Tipo do resultado do parecer tramitado';
COMMENT ON COLUMN analise.parecer_analista_tecnico.id_usuario_analista_tecnico IS 'Identificador do usuário analista técnico que realizou o parecer';

CREATE SEQUENCE analise.parecer_analista_tecnico_id_seq
    INCREMENT 1
    MINVALUE 1
    NO MAXVALUE
    START 1
    CACHE 1;
ALTER TABLE analise.parecer_analista_tecnico ALTER COLUMN id SET DEFAULT pg_catalog.nextval('analise.parecer_analista_tecnico_id_seq');
ALTER TABLE analise.parecer_analista_tecnico_id_seq OWNER TO postgres;
GRANT SELECT, USAGE ON SEQUENCE analise.parecer_analista_tecnico_id_seq TO licenciamento_ap;
SELECT setval('analise.parecer_analista_tecnico_id_seq', coalesce(max(id), 1)) FROM analise.parecer_analista_tecnico;

CREATE TABLE analise.rel_documento_parecer_analista_tecnico (
    id_parecer_analista_tecnico integer NOT NULL,
    id_documento integer NOT NULL,
    CONSTRAINT fk_rdpat_parecer_analista_tecnico FOREIGN KEY (id_parecer_analista_tecnico)
        REFERENCES analise.parecer_analista_tecnico (id),
    CONSTRAINT fk_rdpat_documento FOREIGN KEY (id_documento)
        REFERENCES analise.documento (id)
);
ALTER TABLE  analise.rel_documento_parecer_analista_tecnico OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE  analise.rel_documento_parecer_analista_tecnico TO licenciamento_ap;
COMMENT ON TABLE analise.rel_documento_parecer_analista_tecnico IS 'Entidade responsável por armazenar a relação dos documentos de um parecer de analista técnico';
COMMENT ON COLUMN analise.rel_documento_parecer_analista_tecnico.id_parecer_analista_tecnico IS 'Identificador do parecer';
COMMENT ON COLUMN analise.rel_documento_parecer_analista_tecnico.id_documento IS 'Identificador do documento relacionado ao parecer';



--										117.sql


ALTER TABLE analise.parecer_analista_tecnico ADD COLUMN parecer TEXT;
COMMENT ON COLUMN analise.parecer_analista_tecnico.parecer IS 'Campo responsável por armazenar o parecer da análise técnica';

--										118.sql


INSERT INTO analise.tipo_documento(id, nome, caminho_pasta, prefixo_nome_arquivo) VALUES
(29, 'Documento Auto de Infração', 'documento_auto_infracao', 'documento_auto_infracao' );
    
--										119.sql


ALTER TABLE analise.inconsistencia_tecnica_parametro ADD COLUMN id_atividade_caracterizacao integer;
COMMENT ON COLUMN analise.inconsistencia_tecnica_parametro.id_atividade_caracterizacao IS 'Identificador da atividade caracterizacao a qual os parâmetros pertencem.';

--										120.sql

ALTER TABLE analise.parecer_analista_tecnico ADD COLUMN id_historico_tramitacao integer;
COMMENT ON COLUMN analise.parecer_analista_tecnico.id_historico_tramitacao IS 'Campo responsável por armazenar o id do histórico de tramitação';

--										121.sql


ALTER TABLE analise.parecer_analista_tecnico ADD COLUMN validade_permitida integer;
COMMENT ON COLUMN analise.parecer_analista_tecnico.validade_permitida IS 'Nova validade permitida da licença';

ALTER TABLE analise.parecer_analista_tecnico ADD COLUMN finalidade_atividade text;
COMMENT ON COLUMN analise.parecer_analista_tecnico.finalidade_atividade IS 'Finalidade da atividade da licença';

ALTER TABLE analise.condicionante DROP COLUMN ordem;

ALTER TABLE analise.condicionante DROP CONSTRAINT fk_c_licenca_analise;

ALTER TABLE analise.condicionante DROP COLUMN id_licenca_analise;

ALTER TABLE analise.condicionante ADD COLUMN id_parecer_analista_tecnico int;
COMMENT ON COLUMN analise.condicionante.id_parecer_analista_tecnico IS 'Identificador do parecer da análise técnica da condicionante';

ALTER TABLE analise.condicionante ADD 
CONSTRAINT fk_c_parecer_analista_tecnico FOREIGN KEY (id_parecer_analista_tecnico) 
        REFERENCES analise.parecer_analista_tecnico(id);


CREATE TABLE analise.restricao (
    id serial NOT NULL,       
    texto text NOT NULL,
    id_parecer_analista_tecnico integer,
    CONSTRAINT pk_restricao PRIMARY KEY(id),
    CONSTRAINT fk_r_parecer_analista_tecnico FOREIGN KEY(id_parecer_analista_tecnico)
        REFERENCES analise.parecer_analista_tecnico(id)
);
ALTER TABLE analise.restricao OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.restricao TO licenciamento_ap;
GRANT SELECT, USAGE ON SEQUENCE analise.restricao_id_seq TO licenciamento_ap; 
COMMENT ON TABLE analise.restricao IS 'Entidade responsável por armazenar as restrições das licença de análise.';
COMMENT ON COLUMN analise.restricao.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.restricao.texto IS 'Descrição da restrição.';
COMMENT ON COLUMN analise.restricao.id_parecer_analista_tecnico IS 'Identificador do parecer da análise técnica da condicionante';


--										122.sql


ALTER TABLE analise.vistoria DROP CONSTRAINT fk_v_analise_tecnica;

ALTER TABLE analise.vistoria DROP COLUMN id_analise_tecnica;

ALTER TABLE analise.vistoria ADD COLUMN id_parecer_analista_tecnico integer NOT NULL;
COMMENT ON COLUMN analise.vistoria.id_parecer_analista_tecnico IS 'Identificador do parecer do analista técnico relacionado a vistoria';

ALTER TABLE analise.vistoria ADD 
CONSTRAINT fk_v_parecer_analista_tecnico FOREIGN KEY(id_parecer_analista_tecnico)
    REFERENCES analise.parecer_analista_tecnico(id);         

ALTER TABLE analise.inconsistencia_vistoria ADD COLUMN tipo_inconsistencia text NOT NULL;
COMMENT ON COLUMN analise.inconsistencia_vistoria.tipo_inconsistencia IS 'Campo que armazena o tipo da inconsistência relatada pelo analista.';


--										123.sql


ALTER TABLE analise.parecer_analista_geo ALTER COLUMN conclusao DROP NOT NULL;
ALTER TABLE analise.parecer_analista_tecnico ALTER COLUMN da_conclusao DROP NOT NULL;

--										124.sql


ALTER TABLE analise.notificacao ADD COLUMN id_parecer_analista_tecnico INTEGER;
COMMENT ON COLUMN analise.notificacao.id_parecer_analista_tecnico IS 'Identificador do parecer do analista tecnico';

ALTER TABLE analise.notificacao ADD CONSTRAINT fk_n_parecer_analista_tecnico 
FOREIGN KEY (id_parecer_analista_tecnico) REFERENCES analise.parecer_analista_tecnico(id);

ALTER TABLE analise.notificacao ALTER COLUMN id_parecer_analista_geo DROP NOT NULL;


--										125.sql


ALTER TABLE analise.parecer_analista_tecnico ADD COLUMN id_documento INTEGER;
COMMENT ON COLUMN analise.parecer_analista_tecnico.id_documento IS 'Identificador do documento do parecer do analista tecnico';

ALTER TABLE analise.parecer_analista_tecnico ADD 
CONSTRAINT fk_pat_documento FOREIGN KEY (id_documento) 
	REFERENCES analise.documento(id);


--										126.sql


UPDATE analise.tipo_documento
SET caminho_pasta='notificacao_analise_tecnica'
WHERE id=6;


CREATE TABLE analise.rel_documento_notificacao_tecnica
(
    id_documento   INTEGER NOT NULL,
    id_notificacao INTEGER NOT NULL,

    CONSTRAINT pk_rel_documento_notificacao_tecnica
        PRIMARY KEY (id_documento, id_notificacao),
    CONSTRAINT fk_rdnt_documento FOREIGN KEY (id_documento)
        REFERENCES analise.documento (id),
    CONSTRAINT fk_rdnt_notificacao FOREIGN KEY (id_notificacao)
        REFERENCES analise.notificacao (id)
);
ALTER TABLE analise.rel_documento_notificacao_tecnica OWNER TO postgres;
GRANT INSERT, SELECT,UPDATE,DELETE ON TABLE analise.rel_documento_notificacao_tecnica TO licenciamento_ap;
COMMENT ON TABLE analise.rel_documento_notificacao_tecnica is 'Entidade responsável por armazenar a relação entre as entidades documento e notificação referentes aos documentos gerados na notificação do técnico.';
COMMENT ON COLUMN analise.rel_documento_notificacao_tecnica.id_documento is 'Identificador da tabela documento, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.rel_documento_notificacao_tecnica.id_notificacao is 'Identificador da tabela notificacao, responsável pelo relacionamento entre as duas tabelas referentes aos documentos gerados na notificação do técnico.';


--										127.sql


CREATE TABLE analise.parecer_gerente_analise_tecnica
(
  id serial NOT NULL, 
  id_tipo_resultado_analise integer NOT NULL, 
  parecer text NOT NULL, 
  data_parecer timestamp without time zone DEFAULT now(), 
  id_usuario_gerente integer NOT NULL,
  id_analise_tecnica integer NOT NULL, 
  id_historico_tramitacao integer NOT NULL,
  CONSTRAINT pk_parecer_gerente_analise_tecnica PRIMARY KEY (id),
  CONSTRAINT fk_pgat_analise_tecnica FOREIGN KEY (id_analise_tecnica)
      REFERENCES analise.analise_tecnica (id) ,
  CONSTRAINT fk_pgat_tipo_resultado_analise FOREIGN KEY (id_tipo_resultado_analise)
      REFERENCES analise.tipo_resultado_analise (id),
  CONSTRAINT fk_pgat_usuario_analise FOREIGN KEY (id_usuario_gerente)
      REFERENCES analise.usuario_analise (id) 
);
ALTER TABLE analise.parecer_gerente_analise_tecnica OWNER TO postgres;
ALTER TABLE analise.parecer_gerente_analise_tecnica OWNER TO licenciamento_ap;
GRANT SELECT,INSERT,UPDATE,DELETE ON TABLE analise.parecer_gerente_analise_tecnica TO licenciamento_ap;
GRANT ALL ON TABLE analise.parecer_gerente_analise_tecnica TO postgres;
GRANT SELECT,USAGE ON SEQUENCE analise.parecer_gerente_analise_tecnica_id_seq TO licenciamento_ap;
COMMENT ON TABLE analise.parecer_gerente_analise_tecnica IS 'Entidade responsável por armazenar informações sobre o parecer do gerente em uma análise técnica';
COMMENT ON COLUMN analise.parecer_gerente_analise_tecnica.id IS 'Identificador do parecer do gerente';
COMMENT ON COLUMN analise.parecer_gerente_analise_tecnica.id_tipo_resultado_analise IS 'Tipo do resultado da análise';
COMMENT ON COLUMN analise.parecer_gerente_analise_tecnica.parecer IS 'Descrição do parecer feito pelo gerente';
COMMENT ON COLUMN analise.parecer_gerente_analise_tecnica.data_parecer IS 'Data do parecer';
COMMENT ON COLUMN analise.parecer_gerente_analise_tecnica.id_usuario_gerente IS 'Identificador do gerente responsável pelo parecer';
COMMENT ON COLUMN analise.parecer_gerente_analise_tecnica.id_analise_tecnica IS 'Identificador da análise tecnica que teve parecer';
COMMENT ON COLUMN analise.parecer_gerente_analise_tecnica.id_historico_tramitacao IS 'Identificador do histórico da tramitação';


CREATE TABLE analise.diretor
(
  id serial NOT NULL, 
  id_analise_tecnica integer,
  id_usuario integer NOT NULL, 
  data_vinculacao timestamp without time zone, 
  id_analise_geo integer, 
  CONSTRAINT pk_diretor PRIMARY KEY (id),
  CONSTRAINT fk_d_analise_geo FOREIGN KEY (id_analise_geo)
      REFERENCES analise.analise_geo (id),
  CONSTRAINT fk_d_usuario_analise FOREIGN KEY (id_usuario)
      REFERENCES analise.usuario_analise (id),
  CONSTRAINT fk_d_analise_tecnica FOREIGN KEY (id_analise_tecnica)
      REFERENCES analise.analise_tecnica (id) 
);
ALTER TABLE analise.diretor OWNER TO postgres;
GRANT ALL ON TABLE analise.diretor TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.diretor TO licenciamento_ap;
ALTER TABLE  analise.diretor OWNER TO licenciamento_ap;
GRANT SELECT,USAGE ON SEQUENCE  analise.diretor_id_seq TO licenciamento_ap;
COMMENT ON TABLE analise.diretor IS 'Entidade responsável por armazenar o Gerente responsável pela análise técnica.';
COMMENT ON COLUMN analise.diretor.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.diretor.id_analise_tecnica IS 'Identificador da entidade analise_tecnica que realiza o relacionamento entre as entidades diretor e analise_tecnica.';
COMMENT ON COLUMN analise.diretor.id_usuario IS 'Identificador da entidade analise.usuario_analise que realiza o relacionamento entre as entidades diretor e analise.usuario_analise.';
COMMENT ON COLUMN analise.diretor.data_vinculacao IS 'Data em que o usuário foi vinculado a análise técnica.';
COMMENT ON COLUMN analise.diretor.id_analise_geo IS 'Identificador da entidade analise_geo que realiza o relacionamento entre as entidades diretor e analise_geo.';



--										128.sql


INSERT INTO analise.tipo_documento(id, nome, caminho_modelo, caminho_pasta, prefixo_nome_arquivo) VALUES 
	(30, 'Documento Relatório Técnico de Vistoria', null, 'documento_relatorio_tecnico_vistoria', 'documento_relatorio_tecnico_vistoria');

ALTER TABLE analise.vistoria ADD COLUMN id_documento_relatorio_tecnico_vistoria INTEGER;
COMMENT ON COLUMN analise.vistoria.id_documento_relatorio_tecnico_vistoria IS 'Identificador único da entidade documento.';

ALTER TABLE analise.vistoria ALTER COLUMN hora TYPE TIMESTAMP WITHOUT TIME ZONE;

--										129.sql


ALTER TABLE analise.parecer_analista_tecnico RENAME COLUMN data TO data_parecer;


--										130.sql


UPDATE analise.tipo_documento SET caminho_pasta = 'parecer_analise_tecnica' WHERE id = 4;


--										131.sql


ALTER TABLE analise.comunicado ADD COLUMN aguardando_resposta BOOLEAN DEFAULT FALSE;
COMMENT ON COLUMN analise.comunicado.aguardando_resposta IS 'Campo que idenfitica se o comunicado está aguardando a resposta do órgão.';

ALTER TABLE analise.comunicado ADD COLUMN segundo_email_enviado BOOLEAN DEFAULT FALSE;
COMMENT ON COLUMN analise.comunicado.segundo_email_enviado IS 'Campo que idenfitica se houve o envio do segundo e-mail para o órgão.';


--										132.sql


INSERT INTO analise.tipo_documento(id, nome, caminho_modelo, caminho_pasta, prefixo_nome_arquivo) VALUES 
	(31, 'Documento Minuta', null, 'documento_minuta', 'documento_minuta');

ALTER TABLE analise.parecer_analista_tecnico ADD COLUMN id_documento_minuta INTEGER;
COMMENT ON COLUMN analise.parecer_analista_tecnico.id_documento_minuta IS 'Identificador único da entidade documento.';


--										133.sql


ALTER TABLE analise.parecer_analista_geo ADD COLUMN id_documento INTEGER;
COMMENT ON COLUMN analise.parecer_analista_geo.id_documento IS 'Identificador único do documento parecer';

ALTER TABLE analise.parecer_analista_geo ADD 
CONSTRAINT fk_d_documento FOREIGN KEY (id_documento) 
	REFERENCES analise.documento (id);

ALTER TABLE analise.parecer_analista_geo ADD COLUMN id_carta_imagem INTEGER;
COMMENT ON COLUMN analise.parecer_analista_geo.id_carta_imagem IS 'Identificador único do documento carta_imagem';

ALTER TABLE analise.parecer_analista_geo ADD 
CONSTRAINT fk_d_carta FOREIGN KEY (id_carta_imagem) 
	REFERENCES analise.documento (id);

ALTER TABLE analise.documento ADD COLUMN responsavel VARCHAR(200);
COMMENT ON COLUMN analise.documento.responsavel IS 'Campo que apresenta o responsável por gerar o documento';


--										134.sql

ALTER TABLE analise.processo ADD COLUMN retificacao BOOLEAN DEFAULT FALSE NOT NULL;
COMMENT ON COLUMN analise.processo.retificacao IS 'Flag que indica se um processo é proveniente de retificação de uma caracterização';

ALTER TABLE analise.processo ADD COLUMN ativo BOOLEAN DEFAULT TRUE NOT NULL;
COMMENT ON COLUMN analise.processo.ativo IS 'Flag que indica se um processo está ativo ou inativo';

ALTER TABLE analise.notificacao ADD COLUMN data_conclusao TIMESTAMP WITH TIME ZONE;
COMMENT ON COLUMN analise.notificacao.data_conclusao IS 'Data em que a notificação foi concluída';

CREATE TABLE analise.origem_notificacao
(
    id        INTEGER      NOT NULL,
    codigo    VARCHAR(150) NOT NULL,
    descricao TEXT         NOT NULL,

    CONSTRAINT pk_origem_notificacao PRIMARY KEY (id),
    CONSTRAINT uq_codigo UNIQUE (codigo)
);

COMMENT ON TABLE analise.origem_notificacao IS 'Entidade responsável pela referência para a origem da notificação do processo';
COMMENT ON COLUMN analise.origem_notificacao.id IS 'Chave primária da entidade';
COMMENT ON COLUMN analise.origem_notificacao.codigo IS 'Código da origem da notificação';
COMMENT ON COLUMN analise.origem_notificacao.descricao IS 'Descrição da origem da notificação';

INSERT INTO analise.origem_notificacao (id, codigo, descricao) VALUES
(1, 'ANALISE_GEO', 'Notificação gerada pelo analista GEO'),
(2, 'ANALISE_TECNICA', 'Notificação gerada pelo analista técnico');


ALTER TABLE analise.processo
    ADD COLUMN id_origem_notificacao INTEGER,
    ADD CONSTRAINT fk_p_origem_notificacao FOREIGN KEY (id_origem_notificacao)
        REFERENCES analise.origem_notificacao (id);
COMMENT ON COLUMN analise.processo.id_origem_notificacao IS 'Referência para a entidade que guarda a origem de notificação do processo';


UPDATE analise.processo SET id_origem_notificacao = result.id_origem_notificacao
FROM (
    SELECT p.id id_processo,
       CASE
           WHEN n.id_analise_geo IS NOT NULL THEN 1
           WHEN n.id_analise_tecnica IS NOT NULL THEN 2
       END id_origem_notificacao FROM analise.processo p
    INNER JOIN analise.processo pa ON p.id_processo_anterior = pa.id
    INNER JOIN analise.analise a ON pa.id = a.id_processo
    LEFT JOIN analise.analise_tecnica at ON a.id = at.id_analise
    LEFT JOIN analise.analise_geo ag ON a.id = ag.id_analise
    INNER JOIN analise.notificacao n ON ag.id = n.id_analise_geo OR at.id = id_analise_tecnica
 ) AS result
WHERE processo.id = result.id_processo;



--										135.sql


CREATE TABLE analise.parecer_diretor_tecnico
(
  id serial NOT NULL,
  id_tipo_resultado_analise integer NOT NULL,
  parecer text NOT NULL,
  data_parecer timestamp without time zone DEFAULT now(),
  id_usuario_diretor integer NOT NULL,
  id_analise integer NOT NULL,
  id_historico_tramitacao integer NOT NULL,
  CONSTRAINT pk_parecer_diretor_tecnico PRIMARY KEY (id),
  CONSTRAINT fk_pdt_analise FOREIGN KEY (id_analise)
      REFERENCES analise.analise (id) ,
  CONSTRAINT fk_pdt_tipo_resultado_analise FOREIGN KEY (id_tipo_resultado_analise)
      REFERENCES analise.tipo_resultado_analise (id),
  CONSTRAINT fk_pdt_usuario_analise FOREIGN KEY (id_usuario_diretor)
      REFERENCES analise.usuario_analise (id)
);
ALTER TABLE analise.parecer_diretor_tecnico OWNER TO postgres;
ALTER TABLE analise.parecer_diretor_tecnico OWNER TO licenciamento_ap;
GRANT SELECT,INSERT,UPDATE,DELETE ON TABLE analise.parecer_diretor_tecnico TO licenciamento_ap;
GRANT ALL ON TABLE analise.parecer_diretor_tecnico TO postgres;
GRANT SELECT,USAGE ON SEQUENCE analise.parecer_diretor_tecnico_id_seq TO licenciamento_ap;
COMMENT ON TABLE analise.parecer_diretor_tecnico IS 'Entidade responsável por armazenar informações sobre o parecer do diretor técnico';
COMMENT ON COLUMN analise.parecer_diretor_tecnico.id IS 'Identificador do parecer do diretor técnico';
COMMENT ON COLUMN analise.parecer_diretor_tecnico.id_tipo_resultado_analise IS 'Tipo do resultado da análise';
COMMENT ON COLUMN analise.parecer_diretor_tecnico.parecer IS 'Descrição do parecer feito pelo diretor técnico';
COMMENT ON COLUMN analise.parecer_diretor_tecnico.data_parecer IS 'Data do parecer';
COMMENT ON COLUMN analise.parecer_diretor_tecnico.id_usuario_diretor IS 'Identificador do diretor técnico responsável pelo parecer';
COMMENT ON COLUMN analise.parecer_diretor_tecnico.id_analise IS 'Identificador da análise que teve parecer do diretor técnico';
COMMENT ON COLUMN analise.parecer_diretor_tecnico.id_historico_tramitacao IS 'Identificador do histórico da tramitação';


INSERT INTO analise.tipo_resultado_analise(id,nome) VALUES
(7,'Solicitação desvínculo'),
(8,'Análise aprovada'),
(9,'Análise não aprovada');


CREATE TABLE analise.presidente
(
  id serial NOT NULL,
  id_analise_tecnica integer,
  id_usuario integer NOT NULL,
  data_vinculacao timestamp without time zone,
  id_analise_geo integer,
  id_analise integer,
  CONSTRAINT pk_presidente PRIMARY KEY (id),
  CONSTRAINT fk_p_analise_geo FOREIGN KEY (id_analise_geo)
      REFERENCES analise.analise_geo (id),
  CONSTRAINT fk_p_usuario_analise FOREIGN KEY (id_usuario)
      REFERENCES analise.usuario_analise (id),
  CONSTRAINT fk_p_analise_tecnica FOREIGN KEY (id_analise_tecnica)
      REFERENCES analise.analise_tecnica (id),
  CONSTRAINT fk_p_analise FOREIGN KEY (id_analise) 
  	  REFERENCES analise.analise (id)
);
ALTER TABLE analise.presidente OWNER TO postgres;
GRANT ALL ON TABLE analise.presidente TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.presidente TO licenciamento_ap;
ALTER TABLE  analise.presidente OWNER TO licenciamento_ap;
GRANT SELECT,USAGE ON SEQUENCE  analise.presidente_id_seq TO licenciamento_ap;
COMMENT ON TABLE analise.presidente IS 'Entidade responsável por armazenar o Presidente.';
COMMENT ON COLUMN analise.presidente.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.presidente.id_analise_tecnica IS 'Identificador da entidade analise_tecnica que realiza o relacionamento entre as entidades presidente e analise_tecnica.';
COMMENT ON COLUMN analise.presidente.id_usuario IS 'Identificador da entidade analise.usuario_analise que realiza o relacionamento entre as entidades presidente e analise.usuario_analise.';
COMMENT ON COLUMN analise.presidente.data_vinculacao IS 'Data em que o usuário foi vinculado a análise técnica.';
COMMENT ON COLUMN analise.presidente.id_analise_geo IS 'Identificador da entidade analise_geo que realiza o relacionamento entre as entidades presidente e analise_geo.';
COMMENT ON COLUMN analise.presidente.id_analise IS 'Identificador da entidade analiseo que realiza o relacionamento entre as entidades presidente e analise.';


ALTER TABLE analise.diretor ADD COLUMN id_analise integer;
COMMENT ON COLUMN analise.diretor.id_analise IS 'Identificador da entidade analise que realiza o relacionamento entre as entidades diretor e analise.';

ALTER TABLE analise.diretor 
	ADD CONSTRAINT fk_d_analise FOREIGN KEY (id_analise) 
		REFERENCES analise.analise (id);



--										136.sql


ALTER TABLE analise.comunicado ADD COLUMN interessado_notificado BOOLEAN DEFAULT FALSE;
COMMENT ON COLUMN analise.comunicado.interessado_notificado IS 'Campo que identifica se houve o envio da notificação ao interessado.';


--										137.sql


CREATE TABLE analise.parecer_presidente
(
  id serial NOT NULL,
  id_tipo_resultado_analise integer NOT NULL,
  parecer text NOT NULL,
  data_parecer timestamp without time zone DEFAULT now(),
  id_usuario_presidente integer NOT NULL,
  id_analise integer NOT NULL,
  id_historico_tramitacao integer NOT NULL,
  CONSTRAINT pk_parecer_presidente PRIMARY KEY (id),
  CONSTRAINT fk_pp_analise FOREIGN KEY (id_analise)
      REFERENCES analise.analise (id) ,
  CONSTRAINT fk_pp_tipo_resultado_analise FOREIGN KEY (id_tipo_resultado_analise)
      REFERENCES analise.tipo_resultado_analise (id),
  CONSTRAINT fk_pp_usuario_analise FOREIGN KEY (id_usuario_presidente)
      REFERENCES analise.usuario_analise (id)
);
ALTER TABLE analise.parecer_presidente OWNER TO postgres;
ALTER TABLE analise.parecer_presidente OWNER TO licenciamento_ap;
GRANT SELECT,INSERT,UPDATE,DELETE ON TABLE analise.parecer_presidente TO licenciamento_ap;
GRANT ALL ON TABLE analise.parecer_presidente TO postgres;
GRANT SELECT,USAGE ON SEQUENCE analise.parecer_presidente_id_seq TO licenciamento_ap;
COMMENT ON TABLE analise.parecer_presidente IS 'Entidade responsável por armazenar informações sobre o parecer do presidente';
COMMENT ON COLUMN analise.parecer_presidente.id IS 'Identificador do parecer do presidente';
COMMENT ON COLUMN analise.parecer_presidente.id_tipo_resultado_analise IS 'Tipo do resultado da análise';
COMMENT ON COLUMN analise.parecer_presidente.parecer IS 'Descrição do parecer feito pelo presidente';
COMMENT ON COLUMN analise.parecer_presidente.data_parecer IS 'Data do parecer';
COMMENT ON COLUMN analise.parecer_presidente.id_usuario_presidente IS 'Identificador do presidente responsável pelo parecer';
COMMENT ON COLUMN analise.parecer_presidente.id_analise IS 'Identificador da análise que teve parecer do presidente';
COMMENT ON COLUMN analise.parecer_presidente.id_historico_tramitacao IS 'Identificador do histórico da tramitação';

--										138.sql


INSERT INTO analise.tipo_documento(id, nome, caminho_pasta, prefixo_nome_arquivo) VALUES
(32, 'Documento jurídico', 'documento_juridico', 'documento_juridico' );

INSERT INTO analise.tipo_resultado_analise(id,nome) VALUES
(10,'Apto'),
(11,'Não Apto');


CREATE TABLE analise.parecer_juridico (
    id SERIAL NOT NULL,
    id_analise_geo INTEGER,
    data_cadastro TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    data_resposta TIMESTAMP WITHOUT TIME ZONE,
    parecer TEXT,
    resolvido BOOLEAN NOT NULL DEFAULT FALSE,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    id_parecer_analista_geo INTEGER NOT NULL,
    id_tipo_resultado_validacao_juridica INTEGER,
    id_historico_tramitacao INTEGER,
    id_analise_tecnica INTEGER NOT NULL,
    id_documento_fundiario INTEGER NOT NULL,
    CONSTRAINT pk_parecer_juridico PRIMARY KEY (id),
    CONSTRAINT fk_pj_parecer_analista_geo FOREIGN KEY (id_parecer_analista_geo)
        REFERENCES analise.parecer_analista_geo(id),
    CONSTRAINT fk_pj_tipo_resultado_validacao_juridica FOREIGN KEY (id_tipo_resultado_validacao_juridica)
        REFERENCES analise.tipo_resultado_analise (id),
    CONSTRAINT fk_pj_analise_tecnica FOREIGN KEY (id_analise_tecnica)
        REFERENCES analise.analise_tecnica(id),
    CONSTRAINT fk_pj_analise_geo FOREIGN KEY (id_analise_geo)
        REFERENCES analise.analise_geo (id)
);
ALTER TABLE analise.parecer_juridico OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.parecer_juridico TO licenciamento_ap;
GRANT SELECT, USAGE ON SEQUENCE analise.parecer_juridico_id_seq TO licenciamento_ap;
COMMENT ON TABLE analise.parecer_juridico IS 'Entidade responsável por armazenar os dados referentes ao parecer jurídico que será enviado ao setor jurídico responsável.';
COMMENT ON COLUMN analise.parecer_juridico.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.parecer_juridico.id_analise_geo IS 'Identificador da tabela analise_geo, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.parecer_juridico.data_cadastro IS 'Identificador da tabela documento do schema licenciamento, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.parecer_juridico.data_resposta IS 'Campo responsável por armazenar a data em que a notificação foi criada.';
COMMENT ON COLUMN analise.parecer_juridico.parecer IS 'Campo responsável por armazenar a resposta do parecer jurídico.';
COMMENT ON COLUMN analise.parecer_juridico.resolvido IS 'Flag que indica se o parecer foi resolvido.';
COMMENT ON COLUMN analise.parecer_juridico.ativo IS ' Flag que indica se o parecer está ativo.';
COMMENT ON COLUMN analise.parecer_juridico.id_parecer_analista_geo IS 'Identificador da tabela parecer_analista_geo, responśavel pelo relacionamento entre as duas tabelas';
COMMENT ON COLUMN analise.parecer_juridico.id_tipo_resultado_validacao_juridica IS 'Identificador da tabela tipo_resultado, responśavel pelo relacionamento entre as duas tabelas';
COMMENT ON COLUMN analise.parecer_juridico.id_historico_tramitacao IS 'Identificador da tramitação gerada pelo parecer.';
COMMENT ON COLUMN analise.parecer_juridico.id_analise_tecnica IS 'Identificador da tabela analise_tecnica, responśavel pelo relacionamento entre as duas tabelas';
COMMENT ON COLUMN analise.parecer_juridico.id_documento_fundiario IS 'Identificador da tabela documento, no schema licenciamento, responśavel pelo relacionamento entre as duas tabelas';


CREATE TABLE analise.rel_documento_juridico (
    id_documento  INTEGER NOT NULL,
    id_parecer_juridico INTEGER NOT NULL,
    CONSTRAINT pk_rel_documento_juridico PRIMARY KEY (id_documento, id_parecer_juridico),
    CONSTRAINT fk_rdj_documento FOREIGN KEY (id_documento)
        REFERENCES analise.documento (id),
    CONSTRAINT fk_rdj_comunicado FOREIGN KEY (id_parecer_juridico)
        REFERENCES analise.parecer_juridico (id)
);
ALTER TABLE analise.rel_documento_juridico OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.rel_documento_juridico TO licenciamento_ap;
COMMENT ON TABLE analise.rel_documento_juridico IS 'Entidade que relaciona a Entidade Documento e a Entidade Parecer Juridico';
COMMENT ON COLUMN analise.rel_documento_juridico.id_documento IS 'Identificador da tabela documento, responśavel pelo relacionamento entre as duas tabelas';
COMMENT ON COLUMN analise.rel_documento_juridico.id_parecer_juridico IS 'Identificador da tabela parecer_juridico, responśavel pelo relacionamento entre as duas tabelas';


--										139.sql

ALTER TABLE analise.licenca_suspensa DROP CONSTRAINT fk_ls_usuario_analise_executor;

ALTER TABLE analise.licenca_cancelada DROP CONSTRAINT fk_lc_usuario_analise_executor ;

--										140.sql

CREATE TABLE analise.dispensa_licenciamento_cancelada (
  id serial NOT NULL,
  id_dispensa_licenciamento integer NOT NULL,
  justificativa text,
  id_usuario_executor integer NOT NULL,
  data_cancelamento timestamp without time zone NOT NULL,
  CONSTRAINT pk_dispensa_licenciamento_cancelada PRIMARY KEY (id),
  CONSTRAINT dispensa_licenciamento_cancelada_id_dispensa_licencamento_key UNIQUE (id_dispensa_licenciamento)
);
ALTER TABLE analise.dispensa_licenciamento_cancelada OWNER TO postgres;
GRANT ALL ON TABLE analise.dispensa_licenciamento_cancelada TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.dispensa_licenciamento_cancelada TO licenciamento_ap;
GRANT SELECT, USAGE ON SEQUENCE analise.dispensa_licenciamento_cancelada_id_seq TO licenciamento_ap; 
COMMENT ON TABLE analise.dispensa_licenciamento_cancelada IS 'Entidade responsável por armazenar as Dispensas de licenciamento Ambiental canceladas.';
COMMENT ON COLUMN analise.dispensa_licenciamento_cancelada.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.dispensa_licenciamento_cancelada.id_dispensa_licenciamento IS 'Identificador da entidade licenciamento.dispensa_licenciamento que realiza o relacionamento entre as entidades dispensa_licencamento_cancelada e licenciamento.dispensa_licenciamento.';
COMMENT ON COLUMN analise.dispensa_licenciamento_cancelada.justificativa IS 'Justificativa do cancelamento da Dispensa de licenciamento Ambiental.';
COMMENT ON COLUMN analise.dispensa_licenciamento_cancelada.id_usuario_executor IS 'Identifica o usuário executor da ação.';
COMMENT ON COLUMN analise.dispensa_licenciamento_cancelada.data_cancelamento IS 'Data do cancelamento da Dispensa de licenciamento Ambiental.';

CREATE TABLE analise.dispensa_licenciamento_suspensa (
  id serial NOT NULL,
  id_dispensa_licenciamento integer NOT NULL,
  justificativa text,
  id_usuario_executor integer NOT NULL,
  data_suspensao timestamp without time zone NOT NULL,
  ativo boolean DEFAULT TRUE NOT NULL,
  quantidade_dias_suspensao integer,
  CONSTRAINT pk_dispensa_licenciamento_suspensa PRIMARY KEY (id),
  CONSTRAINT dispensa_licenciamento_suspensa_id_dispensa_licencamento_key UNIQUE (id_dispensa_licenciamento)
);
ALTER TABLE analise.dispensa_licenciamento_suspensa OWNER TO postgres;
GRANT ALL ON TABLE analise.dispensa_licenciamento_suspensa TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.dispensa_licenciamento_suspensa TO licenciamento_ap;
GRANT SELECT, USAGE ON SEQUENCE analise.dispensa_licenciamento_suspensa_id_seq TO licenciamento_ap; 
COMMENT ON TABLE analise.dispensa_licenciamento_suspensa IS 'Entidade responsável por armazenar as Dispensas de licenciamento Ambiental suspensas.';
COMMENT ON COLUMN analise.dispensa_licenciamento_suspensa.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.dispensa_licenciamento_suspensa.id_dispensa_licenciamento IS 'Identificador da entidade licenciamento.dispensa_licenciamento que realiza o relacionamento entre as entidades dispensa_licencamento_suspensa e licenciamento.dispensa_licenciamento.';
COMMENT ON COLUMN analise.dispensa_licenciamento_suspensa.justificativa IS 'Justificativa da suspensão da Dispensa de licenciamento Ambiental.';
COMMENT ON COLUMN analise.dispensa_licenciamento_suspensa.id_usuario_executor IS 'Identifica o usuário executor da ação.';
COMMENT ON COLUMN analise.dispensa_licenciamento_suspensa.data_suspensao IS 'Data da suspensão da Dispensa de licenciamento Ambiental.';
COMMENT ON COLUMN analise.dispensa_licenciamento_suspensa.quantidade_dias_suspensao is 'Quantidade de dias em que a licença será suspensa.';
COMMENT ON COLUMN analise.dispensa_licenciamento_suspensa.ativo is 'Indica se a suspensão está ativa (TRUE - Ativa; FALSE - Inativa).';



--										141.sql

INSERT INTO analise.tipo_documento (id, nome, caminho_pasta, prefixo_nome_arquivo) VALUES 
	(33, 'Documento notificação análise técnica', 'documento-notificacao-analise-tecnica', 'documento_notificacao_analise_tecnica');


--COMMIT;


# --- !Downs

DROP SCHEMA IF EXISTS analise CASCADE;
