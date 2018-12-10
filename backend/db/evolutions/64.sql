# --- !Ups

--- Removendo coluna escala da entidade base_vetorial

ALTER TABLE analise.base_vetorial DROP COLUMN escala;

# --- !Downs

ALTER TABLE analise.base_vetorial ADD COLUMN escala VARCHAR(200);
COMMENT ON COLUMN analise.base_vetorial.escala IS 'Escala da base vetorial.';