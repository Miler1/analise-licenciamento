# --- !Ups

ALTER TABLE analise.comunicado
    DROP COLUMN justificativa,
    ADD COLUMN id_orgao INTEGER NOT NULL;

COMMENT ON COLUMN analise.comunicado.id_orgao IS 
	'Campo responsável por armazenar o id do órgão responsável pela sobreposição encontrada.';

# --- !Downs

ALTER TABLE analise.comunicado
    ADD COLUMN justificativa TEXT,
    DROP COLUMN id_orgao;

COMMENT ON COLUMN analise.comunicado.justificativa IS 
	'Identificador da tabela tipo_documento do schema licenciamento, responsável pelo relacionamento entre as duas tabelas.';