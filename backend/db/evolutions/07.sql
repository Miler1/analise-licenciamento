# --- !Ups


CREATE TABLE analise.geoserver
(
id SERIAL NOT NULL,
url_getcapabilities VARCHAR(250) NOT NULL,
CONSTRAINT pk_geoserver PRIMARY KEY(id)
);
GRANT SELECT,INSERT,UPDATE,DELETE ON analise.geoserver TO licenciamento_am;
GRANT SELECT,USAGE ON analise.geoserver_id_seq TO licenciamento_am;

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
GRANT SELECT,INSERT,UPDATE,DELETE ON analise.configuracao_layer TO licenciamento_am;
GRANT SELECT,USAGE ON analise.configuracao_layer_id_seq TO licenciamento_am;

COMMENT ON TABLE analise.configuracao_layer IS 'Entidade responsável por armazenar as configurações das layers que serão utilizadas para cacular regras de analise';
COMMENT ON COLUMN analise.configuracao_layer.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.configuracao_layer.atributo_descricao IS 'Atributo onde será buscado informações sobre a layer.';
COMMENT ON COLUMN analise.configuracao_layer.nome_layer IS 'Nome da configuração.';
COMMENT ON COLUMN analise.configuracao_layer.descricao IS 'Descrição da configuração.';
COMMENT ON COLUMN analise.configuracao_layer.buffer IS 'Buffer em metros.';
COMMENT ON COLUMN analise.configuracao_layer.id_geoserver IS 'Identificador da entidade geoserver que faz o relacionamento entre as duas entidades.';


# --- !Downs


DROP TABLE analise.configuracao_layer;
DROP TABLE analise.geoserver;