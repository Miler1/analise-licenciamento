
# --- !Ups
ALTER TABLE analise.parecer_analista_tecnico ADD COLUMN id_historico_tramitacao integer;
COMMENT ON COLUMN analise.parecer_analista_tecnico.id_historico_tramitacao IS 'Campo responsável por armazenar o id do histórico de tramitação';

# --- !Downs

ALTER TABLE analise.parecer_analista_tecnico DROP COLUMN id_historico_tramitacao;

