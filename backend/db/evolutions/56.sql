# --- !Ups

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
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.documento_manejo_shape TO licenciamento_am;

-- Adicionando tipos de documento manejo na entidade

INSERT INTO analise.tipo_documento (id, nome, caminho_pasta, prefixo_nome_arquivo) VALUES
 (8, 'Área de manejo florestal solicitada - AMF (hectares)', 'shape-manejo', 'amf'),
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
 CONSTRAINT fk_antm_analise_tecnica_manejo FOREIGN KEY (id_analise_tecnica_manejo) REFERENCES analise.analise_tecnica_manejo(id),
 CONSTRAINT fk_antm_usuario FOREIGN KEY (id_usuario) REFERENCES portal_seguranca.usuario(id)
);

COMMENT ON TABLE analise.analista_tecnico_manejo IS 'Entidade responsável por armazenar os analistas técnicos do manejo.';
COMMENT ON COLUMN analise.analista_tecnico_manejo.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.analista_tecnico_manejo.data_vinculacao IS 'Data da vinculação do analista a análise.';
COMMENT ON COLUMN analise.analista_tecnico_manejo.id_analise_tecnica_manejo IS 'Identificador da entidade analise_tecnica_manejo que faz o relacionamento entre a análise do manejo e o analista técnico.';
COMMENT ON COLUMN analise.analista_tecnico_manejo.id_usuario IS 'Identificador da entidade usuario que faz o relacionamento entre um usuário e o análista.';

ALTER TABLE analise.analista_tecnico_manejo OWNER TO postgres;
GRANT ALL ON TABLE analise.analista_tecnico_manejo TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.analista_tecnico_manejo TO licenciamento_am;
GRANT SELECT, USAGE ON SEQUENCE analise.analista_tecnico_manejo_id_seq TO licenciamento_am;


# --- !Downs

DROP TABLE analise.analista_tecnico_manejo;

ALTER TABLE analise.analise_tecnica_manejo ADD COLUMN id_usuario INTEGER NOT NULL;
ALTER TABLE analise.analise_tecnica_manejo ADD CONSTRAINT fk_am_u FOREIGN KEY (id_usuario) REFERENCES portal_seguranca.usuario (id);
COMMENT ON COLUMN analise.analise_tecnica_manejo.id_usuario IS 'Identificador da entidade usuário que denota o usuário responsável por fazer a análise.';

ALTER TABLE analise.analise_ndfi RENAME COLUMN id_analise_tecnica_manejo TO id_analise_manejo;
ALTER TABLE analise.rel_base_vetorial_analise_manejo RENAME COLUMN id_analise_tecnica_manejo TO id_analise_manejo;
ALTER TABLE analise.analise_vetorial RENAME COLUMN id_analise_tecnica_manejo TO id_analise_manejo;

ALTER INDEX analise.pk_analise_tecnica_manejo RENAME TO pk_analise_manejo;
ALTER TABLE analise.analise_tecnica_manejo RENAME TO analise_manejo;

ALTER TABLE analise.analise_manejo DROP COLUMN id_processo_manejo;

ALTER TABLE analise.processo_manejo ADD COLUMN id_analise_manejo INTEGER;
ALTER TABLE analise.processo_manejo ADD CONSTRAINT fk_pm_analise_manejo FOREIGN KEY (id_analise_manejo) REFERENCES analise.analise_manejo (id);
COMMENT ON COLUMN analise.processo_manejo.id_analise_manejo IS 'Identificador da análise manejo.';

DELETE FROM analise.tipo_documento WHERE caminho_pasta = 'shape-manejo';

DROP TABLE analise.documento_manejo_shape;

ALTER TABLE analise.analise_manejo ADD COLUMN geojson TEXT;
COMMENT ON COLUMN analise.analise_manejo.geojson IS 'Geojson arcgis do arquivo shapefile.';