# --- !Ups

-- Criação da tabela tipo_licenca_manejo

CREATE TABLE analise.tipo_licenca_manejo (
 id SERIAL NOT NULL,
 nome VARCHAR(200) NOT NULL,
 codigo VARCHAR(200) NOT NULL,
 CONSTRAINT pk_tipo_licenca_manejo PRIMARY KEY(id)
);

COMMENT ON TABLE analise.tipo_licenca_manejo IS 'Entidade responsável por armazenas os tipos de licença do manejo florestal.';
COMMENT ON COLUMN analise.tipo_licenca_manejo.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.tipo_licenca_manejo.nome IS 'Nome do tipo de licença.';
COMMENT ON COLUMN analise.tipo_licenca_manejo.codigo IS 'Código do tipo de licença.';

ALTER TABLE analise.tipo_licenca_manejo OWNER TO postgres;
GRANT ALL ON TABLE analise.tipo_licenca_manejo TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.tipo_licenca_manejo TO licenciamento_pa;
GRANT SELECT, USAGE ON SEQUENCE analise.tipo_licenca_manejo_id_seq TO licenciamento_pa;

-- Carga de dados na tabela tipo_licenca_manejo

INSERT INTO analise.tipo_licenca_manejo (id, nome, codigo) VALUES
 (1, 'Autorização prévia à análise de plano de manejo florestal sustentável', 'APAT'),
 (2, 'Autorização de Exploração Florestal', 'AUTEF'),
 (3, 'Licença ambiental de regularização', 'LAR');

SELECT setval('analise.tipo_licenca_manejo_id_seq',(SELECT MAX(id) from analise.tipo_licenca_manejo) , true);

-- Criação da tabela status_processo_manejo

CREATE TABLE analise.status_processo_manejo (
 id SERIAL NOT NULL,
 nome VARCHAR(200) NOT NULL,
 codigo VARCHAR(200) NOT NULL,
 CONSTRAINT pk_status_processo_manejo PRIMARY KEY(id)
);

COMMENT ON TABLE analise.status_processo_manejo IS 'Entidade responsável por armazenas os status de um processo de manejo florestal no SIMLAM.';
COMMENT ON COLUMN analise.status_processo_manejo.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.status_processo_manejo.nome IS 'Nome do status.';
COMMENT ON COLUMN analise.status_processo_manejo.codigo IS 'Código do status.';

ALTER TABLE analise.status_processo_manejo OWNER TO postgres;
GRANT ALL ON TABLE analise.status_processo_manejo TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.status_processo_manejo TO licenciamento_pa;
GRANT SELECT, USAGE ON SEQUENCE analise.status_processo_manejo_id_seq TO licenciamento_pa;

-- Carga de dados na tabela status_processo_manejo

INSERT INTO analise.status_processo_manejo (id, nome, codigo) VALUES
 (1, 'Aguardando análise', 'AGUARDANDO_ANALISE'),
 (2, 'Em análise', 'EM_ANALISE'),
 (3, 'Deferido', 'DEFERIDO'),
 (4, 'Indeferido', 'INDEFERIDO');

SELECT setval('analise.status_processo_manejo_id_seq',(SELECT MAX(id) from analise.status_processo_manejo) , true);

# --- !Downs

DROP TABLE analise.status_processo_manejo;

DROP TABLE analise.tipo_licenca_manejo;