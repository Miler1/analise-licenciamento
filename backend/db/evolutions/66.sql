# --- !Ups

--- Alteração na entidade imovel_manejo

ALTER TABLE analise.imovel_manejo ADD COLUMN status TEXT;
COMMENT ON COLUMN analise.imovel_manejo.status IS 'Indicador do status atual do imóvel no CAR.';

# --- !Downs

ALTER TABLE analise.imovel_manejo DROP COLUMN status;