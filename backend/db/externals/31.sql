# --- !Ups

INSERT INTO tramitacao.acao(id_acao,tx_descricao,fl_ativo,fl_tramitavel) VALUES
(35, 'Resolver notificação jurídica',1,1),
(36, 'Resolver notificação técnica',1,1),
(37, 'Arquivar processo',1,1);

SELECT pg_catalog.setval('tramitacao.acao_id_acao_seq', 37, TRUE);

INSERT INTO tramitacao.transicao(id_transicao,id_condicao_inicial,id_condicao_final,id_acao) VALUES
(37,4,2,35),
(38,4,8,36),
(39,4,6,37);

SELECT pg_catalog.setval('tramitacao.transicao_id_transicao_seq', 39, TRUE);

INSERT INTO tramitacao.situacao(id_situacao,tx_descricao,fl_ativo) VALUES
(4, 'Notificado via jurídico',1),
(5, 'Notificado via técnico',1);

SELECT pg_catalog.setval('tramitacao.situacao_id_situacao_seq', 5, TRUE);

INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES 
(39, 4, 3, 1),
(40, 4, 37, 0),
(41, 4, 39, 0),
(42, 5, 19, 1),
(43, 5, 38, 0),
(44, 5, 39, 0);

SELECT pg_catalog.setval('tramitacao.config_situacao_id_config_situacao_seq', 44, TRUE);

INSERT INTO tramitacao.impedimento_transicao (id_impedimento_transicao, id_situacao, id_transicao, tp_impedimento) VALUES 
(9, 4, 37, 1),
(10, 5, 38, 1);

SELECT pg_catalog.setval('tramitacao.impedimento_transicao_id_impedimento_transicao_seq', 10, TRUE);



# --- !Downs


DELETE FROM tramitacao.impedimento_transicao WHERE id_impedimento_transicao IN (9, 10);
SELECT pg_catalog.setval('tramitacao.impedimento_transicao_id_impedimento_transicao_seq', 8, TRUE);

DELETE FROM tramitacao.config_situacao WHERE id_config_situacao IN (39, 40, 41, 42, 43, 44);
SELECT pg_catalog.setval('tramitacao.config_situacao_id_config_situacao_seq', 38, TRUE);

DELETE FROM tramitacao.situacao WHERE id_situacao IN (4, 5);
SELECT pg_catalog.setval('tramitacao.situacao_id_situacao_seq', 3, TRUE);

DELETE FROM tramitacao.transicao WHERE id_transicao IN (37, 38, 39);
SELECT pg_catalog.setval('tramitacao.transicao_id_transicao_seq', 36, TRUE);

DELETE FROM  tramitacao.acao WHERE id_acao IN (35, 36, 37);
SELECT pg_catalog.setval('tramitacao.acao_id_acao_seq', 34, TRUE);
