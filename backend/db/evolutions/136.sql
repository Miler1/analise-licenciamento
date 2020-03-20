
# --- !Ups

ALTER TABLE analise.comunicado ADD COLUMN interessado_notificado BOOLEAN DEFAULT FALSE;
COMMENT ON COLUMN analise.comunicado.interessado_notificado IS 'Campo que identifica se houve o envio da notificação ao interessado.';


# --- !Downs

ALTER TABLE analise.comunicado DROP COLUMN interessado_notificado;

