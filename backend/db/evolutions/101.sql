
# --- !Ups

INSERT INTO analise.tipo_documento (id,nome,caminho_modelo,caminho_pasta,prefixo_nome_arquivo) VALUES (23,'Documento análise temporal',NULL,'documento_analise_temporal','documento_analise_temporal');


# --- !Downs

DELETE FROM analise.tipo_documento WHERE id = 23;
