# --- !Ups

ALTER TABLE analise.processo_manejo ADD COLUMN revisao_solicitada BOOLEAN NOT NULL DEFAULT FALSE;
COMMENT ON COLUMN analise.processo_manejo.revisao_solicitada IS 'Flag que indica se foi solicitada uma revisão dos arquivos shape do processo.';

# --- !Downs

ALTER TABLE analise.processo_manejo DROP COLUMN revisao_solicitada;