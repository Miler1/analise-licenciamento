# --- !Ups

ALTER TABLE "analise"."notificacao" ADD COLUMN "justificativa" Text;

COMMENT ON COLUMN "analise"."notificacao"."justificativa" IS 'Campo responsável por armazenar uma justificativa caso o requerente não queira subir outro documento para resolver a notificação.';


# --- !Downs

ALTER TABLE "analise"."notificacao" DROP COLUMN "justificativa";