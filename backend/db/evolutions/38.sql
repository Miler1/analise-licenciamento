# --- !Ups

ALTER TABLE analise.analise_documento ADD COLUMN id_analise_documento_anterior INTEGER;

ALTER TABLE analise.analise_documento
	ADD CONSTRAINT fk_ad_analise_documento FOREIGN KEY (id_analise_documento_anterior)
	REFERENCES analise.analise_documento (id) MATCH SIMPLE;

COMMENT ON COLUMN analise.analise_documento.id_analise_documento_anterior IS 'Identificador da tabela analise_documento, responsável pelo auto-relacionamento. Identifica a análise anterior do documento.';

# --- !Downs

ALTER TABLE analise.analise_documento DROP COLUMN id_analise_documento_anterior;