# --- !Ups

ALTER TABLE analise.notificacao ADD COLUMN id_historico_tramitacao INTEGER;
COMMENT ON COLUMN analise.notificacao.id_historico_tramitacao IS 'Identificador da entidade historico_tramitacao responsável pelo relacionamento entre historico_tramitacao e notificacao.';

# --- !Downs

ALTER TABLE analise.notificacao DROP COLUMN id_historico_tramitacao;