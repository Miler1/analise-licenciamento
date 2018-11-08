# --- !Ups

--- Alteração na entidade analise_manejo

ALTER TABLE analise.analise_manejo DROP COLUMN path_arquivo_shape;

ALTER TABLE analise.analise_manejo ADD COLUMN the_geom public.geometry;
ALTER TABLE analise.analise_manejo ADD CONSTRAINT enforce_dims_geo_localizacao CHECK ((public.st_ndims(the_geom) = 2));
ALTER TABLE analise.analise_manejo ADD CONSTRAINT enforce_geotype_geo_localizacao CHECK (((public.geometrytype(the_geom) = 'MULTIPOLYGON'::text) OR (the_geom IS NULL)));
ALTER TABLE analise.analise_manejo ADD CONSTRAINT enforce_srid_geo_localizacao CHECK ((public.st_srid(the_geom) = 4674));
COMMENT ON COLUMN analise.analise_manejo.the_geom IS 'Coluna georreferenciada.';

ALTER TABLE analise.analise_manejo ALTER COLUMN analise_temporal DROP NOT NULL;
ALTER TABLE analise.analise_manejo ALTER COLUMN area_manejo_florestal_solicitada DROP NOT NULL;
ALTER TABLE analise.analise_manejo ALTER COLUMN area_sem_previa_exploracao DROP NOT NULL;
ALTER TABLE analise.analise_manejo ALTER COLUMN consideracoes DROP NOT NULL;
ALTER TABLE analise.analise_manejo ALTER COLUMN conclusao DROP NOT NULL;

ALTER TABLE analise.analise_manejo ADD COLUMN object_id INTEGER;
COMMENT ON COLUMN analise.analise_manejo.object_id IS 'Identificador da análise no serviço de validação de shape.';

# --- !Downs

ALTER TABLE analise.analise_manejo DROP COLUMN object_id;

ALTER TABLE analise.analise_manejo ALTER COLUMN analise_temporal SET NOT NULL;
ALTER TABLE analise.analise_manejo ALTER COLUMN area_manejo_florestal_solicitada SET NOT NULL;
ALTER TABLE analise.analise_manejo ALTER COLUMN area_sem_previa_exploracao SET NOT NULL;
ALTER TABLE analise.analise_manejo ALTER COLUMN consideracoes SET NOT NULL;
ALTER TABLE analise.analise_manejo ALTER COLUMN conclusao SET NOT NULL;

ALTER TABLE analise.analise_manejo DROP COLUMN the_geom;

ALTER TABLE analise.analise_manejo ADD COLUMN path_arquivo_shape VARCHAR(500) NOT NULL DEFAULT '';
ALTER TABLE analise.analise_manejo ALTER COLUMN path_arquivo_shape DROP DEFAULT;
COMMENT ON COLUMN analise.analise_manejo.path_arquivo_shape IS 'Caminho onde está armazenado o arquivo shape da análise.';