# --- !Ups

INSERT INTO analise.tipo_documento (id, nome, caminho_pasta, prefixo_nome_arquivo) VALUES 
	(33, 'Documento notificação análise técnica', 'documento-notificacao-analise-tecnica', 'documento_notificacao_analise_tecnica');


# --- !Downs

DELETE FROM analise.tipo_documento WHERE id = 33;