# --- !Ups

ALTER TABLE analise.notificacao ADD COLUMN segundo_email_enviado BOOLEAN NOT NULL DEFAULT FALSE;
COMMENT ON COLUMN analise.notificacao.segundo_email_enviado IS 'Indica se o segundo email da notificação foi enviado pelo vencimento do prazo da notificação';


# --- !Downs

ALTER TABLE analise.notificacao DROP COLUMN segundo_email_enviado;