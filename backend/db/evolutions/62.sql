# --- !Ups

--- Inserir novos tipos de documentos para dados do imovel em analise manejo

INSERT INTO analise.tipo_documento (id, nome, caminho_pasta, prefixo_nome_arquivo) VALUES
 (13, 'Termo de Delimitação da área de reserva aprovada', 'documento-imovel-manejo', 'tdara'),
 (14, 'termo de ajustamento da conduta - TAC', 'documento-imovel-manejo', 'tac');

--- Alterar tipo de documento do manejo

UPDATE analise.tipo_documento SET nome = 'Área da propriedade - APM', prefixo_nome_arquivo = 'apm' WHERE id = 9

--- Adicionar campo de exibir pdf na análise vetorial

ALTER TABLE analise.base_vetorial ADD COLUMN exibir_pdf BOOLEAN NOT NULL DEFAULT TRUE;
COMMENT ON COLUMN analise.base_vetorial.exibir_pdf IS 'Flag de exibição no pdf da análise.';



# --- !Downs

--- Desfazer Adicionar campo de exibir pdf na análise vetorial

ALTER TABLE analise.base_vetorial DROP COLUMN exibir_pdf;

--- Desfazer Alterar tipo de documento do manejo

UPDATE analise.tipo_documento SET nome = 'Área de preservação permanente - APP', prefixo_nome_arquivo = 'app' WHERE id = 9

--- Remover novos tipos de documentos para dados do imovel em analise manejo

DELETE FROM analise.tipo_documento WHERE id in (14, 13);