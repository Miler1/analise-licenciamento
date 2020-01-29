
# --- !Ups

ALTER TABLE analise.comunicado ADD COLUMN aguardando_resposta BOOLEAN DEFAULT FALSE;
COMMENT ON COLUMN analise.comunicado.aguardando_resposta IS 'Campo que idenfitica se o comunicado está aguardando a resposta do órgão.';

ALTER TABLE analise.comunicado ADD COLUMN segundo_email_enviado BOOLEAN DEFAULT FALSE;
COMMENT ON COLUMN analise.comunicado.segundo_email_enviado IS 'Campo que idenfitica se houve o envio do segundo e-mail para o órgão.';


# --- !Downs

ALTER TABLE analise.comunicado DROP COLUMN segundo_email_enviado;

ALTER TABLE analise.comunicado DROP COLUMN aguardando_resposta;

