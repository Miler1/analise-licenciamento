# --- !Ups

-- Removendo atributo na entidade analise_manejo

ALTER TABLE analise.analise_manejo DROP COLUMN geojson;

-- Criando entidade documento_manejo_shape

CREATE TABLE analise.documento_manejo_shape (
 id_documento INTEGER NOT NULL,
 geojson_arcgis TEXT NOT NULL,
 tp_documento_manejo_shape INTEGER NOT NULL,
 id_analise_manejo INTEGER NOT NULL,
 CONSTRAINT pk_documento_manejo_shape PRIMARY KEY (id_documento),
 CONSTRAINT fk_dms_documento FOREIGN KEY (id_documento) REFERENCES analise.documento(id),
 CONSTRAINT fk_dms_analise_manejo FOREIGN KEY (id_analise_manejo) REFERENCES analise.analise_manejo
);

COMMENT ON TABLE analise.documento_manejo_shape IS 'Entidade responsável por armazenas os documentos shape do manejo.';
COMMENT ON COLUMN analise.documento_manejo_shape.id_documento IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.documento_manejo_shape.geojson_arcgis IS 'Geojson arcgis do arquivo shapefile.';
COMMENT ON COLUMN analise.documento_manejo_shape.tp_documento_manejo_shape IS 'Tipo de documento shape do manejo. 0 - Shape da propriedade, 1 - Shape da área de manejo, 2 - Shape do manejo.';
COMMENT ON COLUMN analise.documento_manejo_shape.id_analise_manejo IS 'Identificador da entidade analise_manejo que faz o relacionamento entre a análise do manejo e documento shape do manejo.';

ALTER TABLE analise.documento_manejo_shape OWNER TO postgres;
GRANT ALL ON TABLE analise.documento_manejo_shape TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.documento_manejo_shape TO licenciamento_pa;

# --- !Downs

DROP TABLE analise.documento_manejo_shape;

ALTER TABLE analise.analise_manejo ADD COLUMN geojson TEXT;
COMMENT ON COLUMN analise.analise_manejo.geojson IS 'Geojson arcgis do arquivo shapefile.';