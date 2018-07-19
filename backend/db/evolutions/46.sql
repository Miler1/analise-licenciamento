# --- !Ups


-- Criação da coluna notificacao_atendida para indicar se a notificaçao da analise juridica anterior foi atendida

ALTER TABLE analise.analise_juridica ADD COLUMN notificacao_atendida BOOLEAN DEFAULT FALSE;
COMMENT ON COLUMN analise.analise_juridica.notificacao_atendida IS 'Flag para identificar as notificações atendidas.';

-- Criação da coluna notificacao_atendida para indicar se a notificaçao da analise tecnica anterior foi atendida

ALTER TABLE analise.analise_tecnica ADD COLUMN notificacao_atendida BOOLEAN DEFAULT FALSE;
COMMENT ON COLUMN analise.analise_tecnica.notificacao_atendida IS 'Flag para identificar as notificações atendidas.';


# --- !Downs

ALTER TABLE analise.analise_juridica DROP COLUMN notificacao_atendida;

ALTER TABLE analise.analise_tecnica DROP COLUMN notificacao_atendida;