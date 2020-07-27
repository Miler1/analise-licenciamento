# --- !Ups

INSERT INTO tramitacao.acao(id_acao,tx_descricao,fl_ativo,fl_tramitavel) VALUES
(28, 'Solicitar ajuste da análise jurídica pelo aprovador',1,1),
(29, 'Deferir análise juridica pelo coordenador para o aprovador',1,1),
(30, 'Solicitar ajuste da análise técnica pelo aprovador',1,1);

SELECT pg_catalog.setval('tramitacao.acao_id_acao_seq', 30, TRUE);

INSERT INTO tramitacao.transicao(id_transicao,id_condicao_inicial,id_condicao_final,id_acao) VALUES
(30,11,5,28),
(31,5,11,29),
(32,11,13,30);

SELECT pg_catalog.setval('tramitacao.transicao_id_transicao_seq', 32, TRUE);

INSERT INTO tramitacao.situacao(id_situacao,tx_descricao,fl_ativo) VALUES
(3, 'Revisão solicitada aprovador',1);

SELECT pg_catalog.setval('tramitacao.situacao_id_situacao_seq', 3, TRUE);

INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES 
(37, 3, 30, 1),
(38, 3, 31, 0);

SELECT pg_catalog.setval('tramitacao.config_situacao_id_config_situacao_seq', 38, TRUE);

INSERT INTO tramitacao.impedimento_transicao (id_impedimento_transicao, id_situacao, id_transicao, tp_impedimento) VALUES 
(7, 3, 6, 0),
(8, 3, 31, 1);

SELECT pg_catalog.setval('tramitacao.impedimento_transicao_id_impedimento_transicao_seq', 8, TRUE);

# --- !Downs

DELETE FROM tramitacao.impedimento_transicao WHERE id_impedimento_transicao IN (7,8);

SELECT pg_catalog.setval('tramitacao.impedimento_transicao_id_impedimento_transicao_seq', 8, TRUE);

DELETE FROM tramitacao.config_situacao WHERE id_config_situacao IN (37,38);

SELECT pg_catalog.setval('tramitacao.config_situacao_id_config_situacao_seq', 38, TRUE);

DELETE FROM tramitacao.situacao WHERE id_situacao=3;

SELECT pg_catalog.setval('tramitacao.situacao_id_situacao_seq', 2, TRUE);

DELETE FROM tramitacao.transicao WHERE id_transicao IN (30,31,32);

SELECT pg_catalog.setval('tramitacao.transicao_id_transicao_seq', 29, TRUE);

DELETE FROM tramitacao.acao WHERE id_acao IN(28,29,30);

SELECT pg_catalog.setval('tramitacao.acao_id_acao_seq',27, TRUE);