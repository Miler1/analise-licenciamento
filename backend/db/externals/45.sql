# --- !Ups

INSERT INTO tramitacao.condicao(id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES
(30, (SELECT id_etapa FROM tramitacao.etapa where tx_etapa = 'Análise GEO'), 'Solicitação de desvínculo pendente', 1),
(31, (SELECT id_etapa FROM tramitacao.etapa where tx_etapa = 'Análise GEO'), 'Solicitação de desvínculo atendida', 1),
(32, (SELECT id_etapa FROM tramitacao.etapa where tx_etapa = 'Análise GEO'), 'Solicitação de desvínculo negada', 1);

INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES
(59, 'Solicitar desvínculo', 1, 1),
(60, 'Aprovar solicitação de desvínculo', 1, 1),
(61, 'Negar solicitação de desvínculo', 1, 1);

INSERT INTO tramitacao.transicao(id_transicao, id_acao, id_condicao_inicial, id_condicao_final) VALUES (13, 59, 25, 30);
INSERT INTO tramitacao.transicao(id_transicao, id_acao, id_condicao_inicial, id_condicao_final) VALUES (14, 60, 30, 31);
INSERT INTO tramitacao.transicao(id_transicao, id_acao, id_condicao_inicial, id_condicao_final) VALUES (15, 61, 30, 32);

SELECT setval('tramitacao.condicao_id_condicao_seq', max(id_condicao)) FROM tramitacao.condicao;
SELECT setval('tramitacao.acao_id_acao_seq', max(id_acao)) FROM tramitacao.acao;
SELECT setval('tramitacao.transicao_id_transicao_seq', max(id_transicao)) FROM tramitacao.transicao;


# --- !Downs

DELETE FROM  tramitacao.transicao WHERE id_transicao IN (13,14,15);

DELETE FROM tramitacao.acao WHERE id_acao IN (59, 60, 61);

DELETE FROM tramitacao.condicao WHERE id_condicao IN (30,31,32);

SELECT setval('tramitacao.condicao_id_condicao_seq', max(id_condicao)) FROM tramitacao.condicao;
SELECT setval('tramitacao.acao_id_acao_seq', max(id_acao)) FROM tramitacao.acao;
SELECT setval('tramitacao.transicao_id_transicao_seq', max(id_transicao)) FROM tramitacao.transicao;