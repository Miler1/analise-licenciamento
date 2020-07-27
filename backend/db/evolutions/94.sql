# --- !Ups

INSERT INTO analise.tipo_documento(id,nome,caminho_modelo,caminho_pasta,prefixo_nome_arquivo)
VALUES (21,'Documento oficio ao órgão', NULL, 'documento_oficio_orgao', 'documento_oficio_orgao');

# --- !Downs
DELETE FROM analise.tipo_documento WHERE id = 21;
