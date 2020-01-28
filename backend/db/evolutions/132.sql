
# --- !Ups

INSERT INTO analise.tipo_documento(id, nome, caminho_modelo, caminho_pasta, prefixo_nome_arquivo) VALUES 
	(31, 'Documento Minuta', null, 'documento_minuta', 'documento_minuta');

ALTER TABLE analise.parecer_analista_tecnico ADD COLUMN id_documento_minuta INTEGER;
COMMENT ON COLUMN analise.parecer_analista_tecnico.id_documento_minuta IS 'Identificador único da entidade documento.';


# --- !Downs

ALTER TABLE analise.vistoria DROP COLUMN id_documento_minuta;

DELETE FROM analise.tipo_documento WHERE id=31 AND nome='Documento Minuta';

