# --- !Ups

-- Executar no ambiente de testes
BEGIN;

UPDATE analise.geoserver
SET url_getcapabilities='http://amazonas01.ti.lemaf.ufla.br:8080/geoserver/wfs?REQUEST=GetCapabilities&version=1.0.0'
WHERE id = 1;

COMMIT;

-- Executar no ambiente de homologacao

BEGIN;

UPDATE analise.geoserver
SET url_getcapabilities='http://homologacao.sistemas.sema.ap.gov.br/geoserver/web/'
WHERE id = 1;

COMMIT;

# --- !Downs

BEGIN;

UPDATE analise.geoserver
SET url_getcapabilities='http://car.semas.pa.gov.br/geoserver/wfs?REQUEST=GetCapabilities&version=1.1.0'
WHERE id = 1;

COMMIT;