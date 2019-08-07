# --- !Ups

BEGIN;

ALTER TABLE analise.processo ADD COLUMN status TEXT DEFAULT 'AGUARDANDO_ANALISE_GEO';

COMMENT ON COLUMN analise.processo.status IS 'Coluna que guarda os possíveis status do processo (AGUARDANDO_ANALISE_GEO, SOLICITACAO_DESVINCULO_PENDENTE, NOTIFICACAO_ATENDIDA)';

UPDATE analise.processo SET status = 'AGUARDANDO_ANALISE_GEO' WHERE status IS NULL;

ALTER TABLE analise.processo ALTER COLUMN status SET NOT NULL;

COMMIT;

# --- !Downs

ALTER TABLE analise.processo DROP COLUMN status;
