
# --- !Ups,
INSERT INTO analise.tipo_documento (id, nome, caminho_modelo, caminho_pasta, prefixo_nome_arquivo) 
	VALUES (24, 'Documento RIT', null, 'documento_rit', 'documento_rit');
INSERT INTO analise.tipo_documento (id, nome, caminho_modelo, caminho_pasta, prefixo_nome_arquivo) 
	VALUES (25, 'Documento vistoria', null, 'documento_vistoria', 'documento_vistoria');


# --- !Downs

DELETE FROM analise.tipo_documento WHERE id in (24, 25);