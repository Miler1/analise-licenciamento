# --- !Ups

GRANT USAGE ON SCHEMA tramitacao TO licenciamento_ap;

GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA tramitacao TO licenciamento_ap;
GRANT SELECT, USAGE ON ALL SEQUENCES IN SCHEMA tramitacao TO licenciamento_ap;

GRANT USAGE ON SCHEMA analise TO licenciamento_ap;

GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA analise TO licenciamento_ap;
GRANT SELECT, USAGE ON ALL SEQUENCES IN SCHEMA analise TO licenciamento_ap;

# --- !Downs

REVOKE USAGE ON SCHEMA tramitacao FROM licenciamento_ap;

REVOKE SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA tramitacao FROM licenciamento_ap;
REVOKE SELECT, USAGE ON ALL SEQUENCES IN SCHEMA tramitacao FROM licenciamento_ap;

REVOKE USAGE ON SCHEMA analise TO licenciamento_ap;

REVOKE SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA analise FROM licenciamento_ap;
REVOKE SELECT, USAGE ON ALL SEQUENCES IN SCHEMA analise FROM licenciamento_ap;
