# --- !Ups

COMMENT ON COLUMN analise.comunicado.data_cadastro is 'Data em que o comunicado foi cadastrado.';
COMMENT ON COLUMN analise.comunicado.data_leitura is 'Data em que o comunicado foi lido.';

ALTER TABLE analise.inconsistencia
    ADD COLUMN id_sobreposicao INTEGER;

COMMENT ON COLUMN analise.inconsistencia.id_sobreposicao IS 'Campo responsável por armazenar o id da sobreposição';

ALTER TABLE analise.comunicado RENAME COLUMN data_leitura TO data_vencimento;

COMMENT ON COLUMN analise.comunicado.data_vencimento is 'Data de venciamento do comunicado.';

# --- !Downs

ALTER TABLE analise.comunicado RENAME COLUMN data_vencimento TO data_leitura;
COMMENT ON COLUMN analise.comunicado.data_leitura is 'Data em que o comunicado foi lido.';
ALTER TABLE analise.inconsistencia DROP COLUMN id_sobreposicao;