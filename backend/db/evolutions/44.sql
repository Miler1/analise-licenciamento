# --- !Ups

--
-- Alterando configurações do geoserver
--

UPDATE analise.geoserver set url_getcapabilities = 'http://car.semas.pa.gov.br/geoserver/wfs?REQUEST=GetCapabilities&version=1.0.0'
WHERE url_getcapabilities = 'http://car.semas.pa.gov.br/geoserver/wfs?REQUEST=GetCapabilities&version=1.1.0';


# --- !Downs

UPDATE analise.geoserver set url_getcapabilities = 'http://car.semas.pa.gov.br/geoserver/wfs?REQUEST=GetCapabilities&version=1.1.0'
WHERE url_getcapabilities = 'http://car.semas.pa.gov.br/geoserver/wfs?REQUEST=GetCapabilities&version=1.0.0';