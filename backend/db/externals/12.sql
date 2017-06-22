# --- !Ups

GRANT USAGE ON SCHEMA tramitacao TO licenciamento_pa;

GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA tramitacao TO licenciamento_pa;
GRANT SELECT, USAGE ON ALL SEQUENCES IN SCHEMA tramitacao TO licenciamento_pa;

GRANT USAGE ON SCHEMA analise TO licenciamento_pa;

GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA analise TO licenciamento_pa;
GRANT SELECT, USAGE ON ALL SEQUENCES IN SCHEMA analise TO licenciamento_pa;

# ---!Downs

REVOKE USAGE ON SCHEMA tramitacao FROM licenciamento_pa;

REVOKE SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA tramitacao FROM licenciamento_pa;
REVOKE SELECT, USAGE ON ALL SEQUENCES IN SCHEMA tramitacao FROM licenciamento_pa;

REVOKE USAGE ON SCHEMA analise TO licenciamento_pa;

REVOKE SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA analise FROM licenciamento_pa;
REVOKE SELECT, USAGE ON ALL SEQUENCES IN SCHEMA analise FROM licenciamento_pa;
