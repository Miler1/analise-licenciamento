# --- !Ups

INSERT INTO analise.tipo_documento (id,nome,caminho_modelo,caminho_pasta,prefixo_nome_arquivo) VALUES
(1,'Documento análise jurídica',NULL,'documento_analise_juridica','documento_analise_juridica'),
(2,'Documento análise técnica',NULL,'documento_analise_tecnica','documento_analise_tecnica');

# --- !Downs

DELETE FROM analise.tipo_documento WHERE id IN (1,2);