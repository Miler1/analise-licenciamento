# --- !Ups

-- adicionando colunas de tramitação em tabela de processo manejo

ALTER TABLE analise.processo_manejo  ADD COLUMN id_objeto_tramitavel INTEGER;

COMMENT ON COLUMN processo_manejo.id_objeto_tramitavel IS 'Identificador único da entidade objeto_tramitavel.';

# --- !Downs

ALTER TABLE analise.processo_manejo  DROP COLUMN id_objeto_tramitavel;