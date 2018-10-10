
# --- !Ups

INSERT INTO analise.tipo_documento (id,nome,caminho_modelo,caminho_pasta,prefixo_nome_arquivo) VALUES
(7,'Documento análise manejo',NULL,'documento_analise_manejo','documento_analise_manejo');

# --- !Downs

DELETE FROM analise.tipo_documento WHERE id IN (7);