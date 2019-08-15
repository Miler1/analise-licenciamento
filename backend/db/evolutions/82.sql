# --- !Ups

ALTER TABLE analise.analise_geo 
	ADD COLUMN situacao_fundiaria TEXT,
	ADD COLUMN analise_temporal TEXT,
	ADD COLUMN despacho_analista TEXT;

COMMENT ON COLUMN analise.analise_geo.situacao_fundiaria IS 'Situação fundiária do empreendimento da análise GEO';
COMMENT ON COLUMN analise.analise_geo.analise_temporal IS 'Análise temporal do empreendimento da análise GEO';
COMMENT ON COLUMN analise.analise_geo.despacho_analista IS 'Campo responsavel por armazenar o despacho/justificativa do analista GEO';

# --- !Downs

ALTER TABLE analise.analise_geo 
	DROP COLUMN situacao_fundiaria,
	DROP COLUMN analise_temporal,
	DROP COLUMN despacho_analista;