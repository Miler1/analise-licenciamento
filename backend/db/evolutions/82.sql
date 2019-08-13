# --- !Ups

ALTER TABLE analise.analise_geo ADD COLUMN situacao_fundiaria TEXT;
COMMENT ON COLUMN analise.analise_geo.situacao_fundiaria IS 'Situação fundiária do empreendimento da análise GEO';

ALTER TABLE analise.analise_geo ADD COLUMN analise_temporal TEXT;
COMMENT ON COLUMN analise.analise_geo.analise_temporal IS 'Análise temporal do empreendimento da análise GEO';

# --- !Downs

ALTER TABLE analise.analise_geo DROP COLUMN situacao_fundiaria;
ALTER TABLE analise.analise_geo DROP COLUMN analise_temporal;