# --- !Ups

ALTER TABLE analise.documento ADD COLUMN nome_arquivo TEXT;
COMMENT ON COLUMN analise.documento.nome_arquivo IS 'Nome de referência para upload e download do arquivo armazenado na máquina';

# --- !Downs

ALTER TABLE analise.documento DROP COLUMN nome_arquivo;
