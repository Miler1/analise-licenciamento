
# --- !Ups

INSERT INTO analise.tipo_documento( id, nome, caminho_modelo, caminho_pasta, prefixo_nome_arquivo)
 VALUES (28, 'Parecer análise técnica', null, 'parecer_analise_tecnica', 'parecer_analise_tecnica');



# --- !Downs


DELETE FROM analise.tipo_documento WHERE id=28;
