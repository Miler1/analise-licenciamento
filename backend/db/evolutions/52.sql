# --- !Ups

ALTER TABLE analise.processo ADD COLUMN renovacao BOOLEAN DEFAULT FALSE;
COMMENT ON COLUMN analise.processo.renovacao IS 'Flag que indica se o processo é de renovação de licença.';

# --- !Downs

ALTER TABLE analise.processo DROP COLUMN renovacao;
