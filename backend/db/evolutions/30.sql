# --- !Ups

ALTER TABLE analise.suspensao ADD COLUMN ativo BOOLEAN NOT NULL DEFAULT TRUE;
ALTER TABLE analise.suspensao ADD COLUMN data_validade_licenca DATE;
ALTER TABLE analise.suspensao ADD COLUMN data_validade_prorrogada DATE;

COMMENT ON COLUMN analise.suspensao.ativo IS 'Indica se a suspensão está ativa (TRUE - Ativa; FALSE - Inativa).';
COMMENT ON COLUMN analise.suspensao.data_validade_licenca IS 'Data de validade da licença.';
COMMENT ON COLUMN analise.suspensao.data_validade_prorrogada IS 'Data da nova data de prorrogação.';
	
# --- !Downs

ALTER TABLE analise.suspensao DROP COLUMN ativo;
ALTER TABLE analise.suspensao DROP COLUMN data_validade_licenca;
ALTER TABLE analise.suspensao DROP COLUMN data_validade_prorrogada;