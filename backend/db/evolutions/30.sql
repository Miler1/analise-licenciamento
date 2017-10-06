# --- !Ups

ALTER TABLE analise.suspensao ADD COLUMN ativo BOOLEAN NOT NULL DEFAULT TRUE;

COMMENT ON COLUMN analise.suspensao.ativo IS 'Indica se a suspensão está ativa (TRUE - Ativa; FALSE - Inativa).';

# --- !Downs

ALTER TABLE analise.suspensao DROP COLUMN ativo;