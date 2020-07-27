# --- !Ups

ALTER TABLE analise.dia_analise ADD COLUMN quantidade_dias_notificacao INTEGER;
COMMENT ON COLUMN analise.dia_analise.quantidade_dias_notificacao IS 'Quantidade de dias em que a análise ficará com notificação não resolvida. Esse campo é resetado toda vez que uma notificação é finalizada.';

ALTER TABLE analise.analise ADD COLUMN notificacao_aberta BOOLEAN default false;
COMMENT ON COLUMN analise.analise.notificacao_aberta IS 'Frag que indica se a analise possui notificações abertas (que não foram resolvidas), podendo ser: TRUE - quando existem notificações abertas e FALSE - quando não existe notificações abertas.';

# --- !Downs

ALTER TABLE analise.dia_analise DROP COLUMN quantidade_dias_notificacao;

ALTER TABLE analise.analise DROP COLUMN notificacao_aberta;