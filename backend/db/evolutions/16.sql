# --- !Ups

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

# --- !Downs

DELETE FROM analise.configuracao_layer WHERE nome_layer IN('secar-pa:unidade_conservacao','secar-pa:terra_indigena','base_referencia:vw_mzee_terras_quilombolas','base_referencia:vw_mzee_area_forcas_armadas') ;
DELETE FROM analise.geoserver WHERE id=1;