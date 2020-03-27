# --- !Ups

CREATE ROLE tramitacao LOGIN
ENCRYPTED PASSWORD 'tramitacao'
NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION;

GRANT USAGE ON SCHEMA portal_seguranca TO tramitacao;
GRANT SELECT ON ALL TABLES IN SCHEMA portal_seguranca TO tramitacao;

GRANT USAGE ON SCHEMA public TO tramitacao;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO tramitacao;

GRANT USAGE ON SCHEMA tramitacao TO tramitacao;
GRANT SELECT, UPDATE, INSERT, DELETE ON ALL TABLES IN SCHEMA tramitacao TO tramitacao;
GRANT SELECT, USAGE ON ALL SEQUENCES IN SCHEMA tramitacao TO tramitacao;

SET search_path = tramitacao, pg_catalog;

INSERT INTO tramitacao.acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (1, 'Vincular', 1, 1);
INSERT INTO tramitacao.acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (2, 'Iniciar análise', 1, 1);
INSERT INTO tramitacao.acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (3, 'Notificar', 1, 1);
INSERT INTO tramitacao.acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (4, 'Analisar', 1, 1);
INSERT INTO tramitacao.acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (5, 'Recusar análise', 1, 1);
INSERT INTO tramitacao.acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (6, 'Deferir análise', 1, 1);
INSERT INTO tramitacao.acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (7, 'Indeferir análise', 1, 1);

SELECT pg_catalog.setval('acao_id_acao_seq', 7, true);

INSERT INTO tramitacao.fluxo (id_fluxo, id_condicao_inicial, tx_descricao, dt_prazo) VALUES (1, null, 'Processo de Análise do Licenciamento Ambiental', NULL);

--SELECT pg_catalog.setval('fluxo_id_fluxo_seq', 1, true);

INSERT INTO tramitacao.etapa (id_etapa, id_fluxo, tx_etapa, dt_prazo) VALUES (1, 1, 'Análise jurídica', NULL);
INSERT INTO tramitacao.etapa (id_etapa, id_fluxo, tx_etapa, dt_prazo) VALUES (2, 1, 'Análise técnica', NULL);

--SELECT pg_catalog.setval('etapa_id_etapa_seq', 2, true);

INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (1, 1, 'Aguardando vinculação jurídica', 1);
INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (2, 1, 'Aguardando análise jurídica', 1);
INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (3, 1, 'Em análise jurídica', 1);
INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (4, NULL, 'Notificado', 1);
INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (5, 1, 'Aguardando validação jurídica', 1);
INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (6, NULL, 'Arquivado', 1);
INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (7, 2, 'Aguardando vinculação técnica', 1);

--SELECT pg_catalog.setval('condicao_id_condicao_seq', 7, true);

UPDATE tramitacao.fluxo SET id_condicao_inicial = 1 WHERE id_fluxo = 1;

INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (1, 1, 1, 2, NULL, NULL);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (2, 2, 2, 3, NULL, NULL);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (3, 3, 3, 4, NULL, NULL);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (4, 4, 3, 5, NULL, NULL);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (5, 5, 5, 2, NULL, NULL);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (6, 6, 5, 7, NULL, NULL);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (7, 7, 5, 6, NULL, NULL);

--SELECT pg_catalog.setval('transicao_id_transicao_seq', 7, true);

INSERT INTO tramitacao.tipo_objeto_tramitavel (id_tipo_objeto_tramitavel, tx_descricao, fl_ativo) VALUES (1, 'Licenciamento Ambiental', 1);

--SELECT pg_catalog.setval('tipo_objeto_tramitavel_id_tipo_objeto_tramitavel_seq', 1, true);

SET search_path = licenciamento, pg_catalog, public;


# --- !Downs

SET search_path = tramitacao, pg_catalog;

DELETE FROM tipo_objeto_tramitavel;

SELECT pg_catalog.setval('tipo_objeto_tramitavel_id_tipo_objeto_tramitavel_seq', 1, false);

UPDATE fluxo SET id_condicao_inicial = null WHERE id_fluxo = 1;

DELETE FROM transicao;

SELECT pg_catalog.setval('transicao_id_transicao_seq', 1, false);

DELETE FROM condicao;

SELECT pg_catalog.setval('condicao_id_condicao_seq', 1, false);

DELETE FROM etapa;

SELECT pg_catalog.setval('etapa_id_etapa_seq', 1, false);

DELETE FROM fluxo;

SELECT pg_catalog.setval('fluxo_id_fluxo_seq', 1, false);

DELETE FROM acao;

SELECT pg_catalog.setval('acao_id_acao_seq', 1, false);

SET search_path = licenciamento, pg_catalog;