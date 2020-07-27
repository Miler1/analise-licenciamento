# --- !Ups

INSERT INTO analise.tipo_documento (id,nome,caminho_modelo,caminho_pasta,prefixo_nome_arquivo) VALUES
(3,'Documento parecer análise jurídica',NULL,'parecer_analise','parecer_analise_juridica'),
(4,'Documento parecer análise técnica',NULL,'parecer_analise','parecer_analise_tecnica');

# --- !Downs

DELETE FROM analise.tipo_documento WHERE id IN (3, 4);