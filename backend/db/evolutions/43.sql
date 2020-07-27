# --- !Ups

ALTER TABLE analise.notificacao ADD COLUMN id_historico_tramitacao INTEGER;
COMMENT ON COLUMN analise.notificacao.id_historico_tramitacao IS 'Identificador da entidade historico_tramitacao responsável pelo relacionamento entre historico_tramitacao e notificacao.';

-- Tipos de documentos para gerar pdf das notificações
INSERT INTO analise.tipo_documento (id,nome,caminho_modelo,caminho_pasta,prefixo_nome_arquivo) VALUES
(5,'Notificação análise jurídica',NULL,'notificacao_analise','notificacao_analise_juridica'),
(6,'Notificação análise técnica',NULL,'notificacao_analise','notificacao_analise_tecnica');


# --- !Downs

ALTER TABLE analise.notificacao DROP COLUMN id_historico_tramitacao;

-- Tipos de documentos para gerar pdf das notificações
DELETE FROM analise.tipo_documento WHERE id IN (5, 6);




