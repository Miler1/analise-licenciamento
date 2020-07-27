# --- !Ups

--
-- Criação da coluna data de leitura de uma notificação
--

ALTER TABLE analise.notificacao ADD COLUMN data_leitura TIMESTAMP;
COMMENT ON COLUMN analise.notificacao.data_leitura IS 'Data da leitura de uma notificação pelo usuário externo.';


# --- !Downs

ALTER TABLE analise.notificacao DROP COLUMN data_leitura;