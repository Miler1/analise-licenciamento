
# --- !Ups

INSERT INTO analise.tipo_documento(id, nome, caminho_modelo, caminho_pasta, prefixo_nome_arquivo) VALUES 
	(30, 'Documento Relatório Técnico de Vistoria', null, 'documento_relatorio_tecnico_vistoria', 'documento_relatorio_tecnico_vistoria');

ALTER TABLE analise.vistoria ADD COLUMN id_documento_relatorio_tecnico_vistoria INTEGER;
COMMENT ON COLUMN analise.vistoria.id_documento_relatorio_tecnico_vistoria IS 'Identificador único da entidade documento.';

ALTER TABLE analise.vistoria ALTER COLUMN hora TYPE TIMESTAMP WITHOUT TIME ZONE;

# --- !Downs

ALTER TABLE analise.vistoria ALTER COLUMN hora TYPE date;

ALTER TABLE analise.vistoria DROP COLUMN id_documento_relatorio_tecnico_vistoria;

DELETE FROM analise.tipo_documento WHERE id=30 AND nome='Documento Relatório Técnico de Vistoria';

