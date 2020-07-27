# --- !Ups

BEGIN;

UPDATE analise.configuracao_layer SET nome_layer='areas_restritas_am:unidade_conservacao' WHERE id = 1;
UPDATE analise.configuracao_layer SET nome_layer='areas_restritas_am:terra_indigena' WHERE id = 2;

DELETE FROM analise.configuracao_layer WHERE id in (3,4);

SELECT setval('analise.configuracao_layer_id_seq', coalesce(max(id), 1)) FROM analise.configuracao_layer;

COMMIT;

# --- !Downs

BEGIN;

UPDATE analise.configuracao_layer SET nome_layer = 'secar-pa:unidade_conservacao' WHERE id = 1;
UPDATE analise.configuracao_layer SET nome_layer = 'secar-pa:terra_indigena' WHERE id = 2;
INSERT INTO analise.configuracao_layer (id, atributo_descricao, nome_layer, descricao, buffer, id_geoserver) VALUES (3, 'name', 'base_referencia:vw_mzee_terras_quilombolas', 'Terras quilombolas', 10000, 1);
INSERT INTO analise.configuracao_layer (id, atributo_descricao, nome_layer, descricao, buffer, id_geoserver) VALUES (4, 'name', 'base_referencia:vw_mzee_area_forcas_armadas', 'Forças armadas', 10000, 1);

SELECT setval('analise.configuracao_layer_id_seq', coalesce(max(id), 1)) FROM analise.configuracao_layer;

COMMIT;