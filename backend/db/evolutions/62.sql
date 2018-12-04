# --- !Ups

--- Inserir novos tipos de documentos para dados do imovel em analise manejo

INSERT INTO analise.tipo_documento (id, nome, caminho_pasta, prefixo_nome_arquivo) VALUES
 (13, 'Termo de Delimitação da área de reserva aprovada', 'documento-imovel-manejo', 'tdara'),
 (14, 'termo de ajustamento da conduta - TAC', 'documento-imovel-manejo', 'tac');

# --- !Downs

--- Remover novos tipos de documentos para dados do imovel em analise manejo

DELETE FROM analise.tipo_documento WHERE id in (14, 13);