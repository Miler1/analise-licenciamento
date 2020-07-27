# --- !Ups

-- adicionando nova coluna em observação

ALTER TABLE analise.observacao ADD COLUMN data TIMESTAMP NOT NULL DEFAULT now();

COMMENT ON COLUMN analise.observacao.data IS 'Data da observação.';

# --- !Downs

ALTER TABLE analise.observacao DROP COLUMN data;