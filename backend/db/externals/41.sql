# --- !Ups

INSERT INTO tramitacao.condicao(id_etapa, nm_condicao, fl_ativo) VALUES
((SELECT id_etapa FROM tramitacao.etapa where tx_etapa = 'Análise GEO'), 'Aguardando validação gerente', 1),
((SELECT id_etapa FROM tramitacao.etapa where tx_etapa = 'Análise GEO'), 'Aguardando validação pela diretoria', 1);


INSERT INTO licenciamento.status_caracterizacao (id, nome, codigo) VALUES
(15, 'Em análise', 'EM_ANALISE'),
(16, 'Indeferido', 'INDEFERIDO'),
(17, 'Solicitação de desvínculo', 'SOLICITACAO_DE_DESVINCULO');

# --- !Downs

DELETE FROM licenciamento.status_caracterizacao WHERE id BETWEEN  15 AND 17;

DELETE FROM tramitacao.condicao
WHERE id_etapa = (SELECT id_etapa FROM tramitacao.etapa WHERE tx_etapa = 'Análise GEO') AND
nm_condicao in ('Aguardando validação gerente', 'Aguardando validação pela diretoria');
