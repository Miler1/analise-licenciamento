
# --- !Ups

INSERT INTO analise.tipo_documento(id, nome, caminho_pasta, prefixo_nome_arquivo) VALUES
(29, 'Documento Auto de Infração', 'documento_auto_infracao', 'documento_auto_infracao' );
    
# --- !Downs

DELETE FROM analise.tipo_documento where id= 29;


