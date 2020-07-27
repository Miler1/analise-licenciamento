# --- !Ups

ALTER TABLE analise.notificacao ADD COLUMN data_cadastro timestamp without time zone ;

COMMENT ON COLUMN analise.notificacao.justificativa IS 'Campo responsável por armazenar a data em que a notificação foi criada.';


# --- !Downs

ALTER TABLE analise.notificacao DROP COLUMN data_cadastro;
