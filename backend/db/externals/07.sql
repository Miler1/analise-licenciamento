# --- !Ups


SET search_path = tramitacao, pg_catalog;

UPDATE acao SET tx_descricao = 'Vincular consultor' WHERE id_acao = 1;
UPDATE acao SET tx_descricao = 'Iniciar análise jurídica' WHERE id_acao = 2;
UPDATE acao SET tx_descricao = 'Deferir análise jurídica' WHERE id_acao = 4;
UPDATE acao SET tx_descricao = 'Invalidar parecer jurídico' WHERE id_acao = 5;
UPDATE acao SET tx_descricao = 'Validar deferimento jurídico' WHERE id_acao = 6;
UPDATE acao SET tx_descricao = 'Validar indeferimento jurídico' WHERE id_acao = 7;

INSERT INTO acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (8, 'Indeferir análise jurídica', 1, 1);
INSERT INTO acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (9, 'Solicitar ajustes parecer jurídico', 1, 1);
INSERT INTO acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (10, 'Vincular analista', 1, 1);
INSERT INTO acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (11, 'Iniciar análise técnica', 1, 1);
INSERT INTO acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (12, 'Deferir análise técnica', 1, 1);
INSERT INTO acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (13, 'Indeferir análise técnica', 1, 1);
INSERT INTO acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (14, 'Invalidar parecer técnico', 1, 1);
INSERT INTO acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (15, 'Solicitar ajustes parecer técnico', 1, 1);
INSERT INTO acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (16, 'Validar deferimento técnico', 1, 1);
INSERT INTO acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (17, 'Validar indeferimento técnico', 1, 1);

SELECT pg_catalog.setval('acao_id_acao_seq', 17, TRUE);

INSERT INTO tramitacao.etapa (id_etapa, id_fluxo, tx_etapa, dt_prazo) VALUES (3, 1, 'Liberação da licença', NULL);

--SELECT pg_catalog.setval('etapa_id_etapa_seq', 3, TRUE);

INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (8, 2, 'Aguardando análise técnica', 1);
INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (9, 2, 'Em análise técnica', 1);
INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (10, 2, 'Aguardando validação técnica', 1);
INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (11, 3, 'Aguardando assinatura diretor', 1);

--SELECT pg_catalog.setval('condicao_id_condicao_seq', 11, TRUE);

INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (8, 8, 3, 5, NULL, NULL);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (9, 9, 5, 2, NULL, NULL);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (10, 10, 7, 8, NULL, NULL);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (11, 11, 8, 9, NULL, NULL);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (12, 12, 9, 10, NULL, NULL);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (13, 13, 9, 10, NULL, NULL);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (14, 14, 10, 8, NULL, NULL);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (15, 15, 10, 8, NULL, NULL);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (16, 16, 10, 11, NULL, NULL);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (17, 17, 10, 6, NULL, NULL);

--SELECT pg_catalog.setval('transicao_id_transicao_seq', 17, TRUE);

INSERT INTO tramitacao.situacao (id_situacao, fl_ativo, tx_descricao) VALUES (1,1,'Deferido');
INSERT INTO tramitacao.situacao (id_situacao, fl_ativo, tx_descricao) VALUES (2,1,'Indeferido');

SELECT pg_catalog.setval('situacao_id_situacao_seq', 2, TRUE);

INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (1, 1, 4, 1);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (2, 2, 8, 1);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (3, 1, 3, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (4, 2, 3, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (5, 1, 5, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (6, 2, 5, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (7, 1, 6, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (8, 2, 6, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (9, 1, 7, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (10, 2, 7, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (11, 1, 9, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (12, 2, 9, 0);

INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (13, 1, 12, 1);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (14, 2, 13, 1);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (15, 1, 3, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (16, 2, 3, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (17, 1, 14, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (18, 2, 14, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (19, 1, 15, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (20, 2, 15, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (21, 1, 16, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (22, 2, 16, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (23, 1, 17, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (24, 2, 17, 0);

-- SELECT pg_catalog.setval('config_situacao_id_config_situacao_seq', 24, TRUE);

-- INSERT INTO impedimento_transicao (id_impedimento_transicao, id_situacao, id_transicao, tp_impedimento) VALUES (1, 1, 6, 1);
-- INSERT INTO impedimento_transicao (id_impedimento_transicao, id_situacao, id_transicao, tp_impedimento) VALUES (2, 2, 7, 1);

-- SELECT pg_catalog.setval('impedimento_transicao_id_impedimento_transicao_seq', 2, TRUE);


# --- !Downs

SET search_path = tramitacao, pg_catalog;

DELETE FROM impedimento_transicao WHERE id_impedimento_transicao IN (1,2);
SELECT pg_catalog.setval('impedimento_transicao_id_impedimento_transicao_seq', 1, FALSE);

DELETE FROM config_situacao WHERE id_config_situacao BETWEEN 1 AND 24;
SELECT pg_catalog.setval('config_situacao_id_config_situacao_seq', 1, FALSE);

DELETE FROM situacao WHERE id_situacao IN (1,2);
SELECT pg_catalog.setval('situacao_id_situacao_seq', 1, FALSE);

DELETE FROM transicao WHERE id_transicao BETWEEN 8 AND 17;
SELECT pg_catalog.setval('transicao_id_transicao_seq', 7, TRUE);

DELETE FROM condicao WHERE id_condicao BETWEEN 8 AND 11;
SELECT pg_catalog.setval('condicao_id_condicao_seq', 7, TRUE);

DELETE FROM etapa WHERE id_etapa=3;
SELECT pg_catalog.setval('etapa_id_etapa_seq', 2, TRUE);


DELETE FROM acao WHERE id_acao BETWEEN 8 AND 17;
SELECT pg_catalog.setval('acao_id_acao_seq', 7, TRUE);

UPDATE acao SET tx_descricao = 'Vincular' WHERE id_acao = 1;
UPDATE acao SET tx_descricao = 'Iniciar análise' WHERE id_acao = 2;
UPDATE acao SET tx_descricao = 'Analisar' WHERE id_acao = 4;
UPDATE acao SET tx_descricao = 'Recusar análise' WHERE id_acao = 5;
UPDATE acao SET tx_descricao = 'Deferir análise' WHERE id_acao = 6;
UPDATE acao SET tx_descricao = 'Indeferir análise' WHERE id_acao = 7;