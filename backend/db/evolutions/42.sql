# --- !Ups

ALTER TABLE analise.notificacao ADD COLUMN codigo_sequencia INTEGER;

COMMENT ON COLUMN analise.notificacao.codigo_sequencia IS 'Campo responsável por armazenar a parte sequencial do codigo.';

ALTER TABLE analise.notificacao ADD COLUMN codigo_ano INTEGER;

COMMENT ON COLUMN analise.notificacao.codigo_ano IS 'Campo responsável por armazenar a parte referente ao ano do codigo.';


# --- !Downs

ALTER TABLE analise.notificacao DROP COLUMN codigo_sequencia;

ALTER TABLE analise.notificacao DROP COLUMN codigo_ano;