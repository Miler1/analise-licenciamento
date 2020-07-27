# --- !Ups

GRANT USAGE ON SCHEMA tramitacao TO licenciamento_am;

GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA tramitacao TO licenciamento_am;
GRANT SELECT, USAGE ON ALL SEQUENCES IN SCHEMA tramitacao TO licenciamento_am;

GRANT USAGE ON SCHEMA analise TO licenciamento_am;

GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA analise TO licenciamento_am;
GRANT SELECT, USAGE ON ALL SEQUENCES IN SCHEMA analise TO licenciamento_am;

# --- !Downs

REVOKE USAGE ON SCHEMA tramitacao FROM licenciamento_am;

REVOKE SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA tramitacao FROM licenciamento_am;
REVOKE SELECT, USAGE ON ALL SEQUENCES IN SCHEMA tramitacao FROM licenciamento_am;

REVOKE USAGE ON SCHEMA analise TO licenciamento_am;

REVOKE SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA analise FROM licenciamento_am;
REVOKE SELECT, USAGE ON ALL SEQUENCES IN SCHEMA analise FROM licenciamento_am;
