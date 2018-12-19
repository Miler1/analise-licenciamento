# --- !Ups

--- Inserir novo tipo de documento para anexos do ARQGIS em analise manejo

INSERT INTO analise.tipo_documento (id, nome, caminho_pasta, prefixo_nome_arquivo) VALUES
 (15, 'Anexo do processo do manejo digital', 'anexo-processo-manejo', 'anexo_processo_manejo');


--- Corrigindo nome de TAC

UPDATE analise.tipo_documento SET nome = 'Termo de ajustamento da conduta - TAC' WHERE id = 14;

# --- !Downs

UPDATE analise.tipo_documento SET nome = 'termo de ajustamento da conduta - TAC' WHERE id = 14;

DELETE FROM analise.tipo_documento WHERE id = 15;