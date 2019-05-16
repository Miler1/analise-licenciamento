# --- !Ups


-- Grant select do schema licenciamento para a role tramitacao
GRANT USAGE ON SCHEMA licenciamento TO tramitacao;
GRANT SELECT ON ALL TABLES IN SCHEMA licenciamento TO tramitacao;



# --- !Downs


REVOKE USAGE ON SCHEMA licenciamento FROM tramitacao;
REVOKE SELECT ON ALL TABLES IN SCHEMA licenciamento FROM tramitacao;