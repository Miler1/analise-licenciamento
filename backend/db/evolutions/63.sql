# --- !Ups

--- Retirando obrigatóriedade dos campos da tabela base vetorial

ALTER TABLE analise.base_vetorial ALTER COLUMN escala DROP NOT NULL;
ALTER TABLE analise.base_vetorial ALTER COLUMN observacao DROP NOT NULL;
ALTER TABLE analise.insumo ALTER COLUMN data DROP NOT NULL;
ALTER TABLE analise.analise_ndfi ALTER COLUMN data DROP NOT NULL;


# --- !Downs

ALTER TABLE analise.analise_ndfi ALTER COLUMN data SET NOT NULL;
ALTER TABLE analise.insumo ALTER COLUMN data SET NOT NULL;
ALTER TABLE analise.base_vetorial ALTER COLUMN observacao SET NOT NULL;
ALTER TABLE analise.base_vetorial ALTER COLUMN escala SET NOT NULL;