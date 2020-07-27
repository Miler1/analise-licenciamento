# --- !Ups

INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES
(14, 3, 'Licenca emitida', 1);
SELECT pg_catalog.setval('tramitacao.condicao_id_condicao_seq', 14, TRUE);

INSERT INTO tramitacao.acao(id_acao,tx_descricao,fl_ativo,fl_tramitavel) VALUES
(31, 'Emitir licença', 1, 1);
SELECT pg_catalog.setval('tramitacao.acao_id_acao_seq', 31, TRUE);

INSERT INTO tramitacao.transicao(id_transicao,id_condicao_inicial,id_condicao_final,id_acao) VALUES
(33,11,14,31);
SELECT pg_catalog.setval('tramitacao.transicao_id_transicao_seq', 33, TRUE);

# --- !Downs

DELETE FROM tramitacao.transicao WHERE id_transicao=33;
SELECT pg_catalog.setval('tramitacao.transicao_id_transicao_seq', 32, TRUE);

DELETE FROM tramitacao.acao WHERE id_acao=31;
SELECT pg_catalog.setval('tramitacao.acao_id_acao_seq', 30, TRUE);

DELETE FROM tramitacao.condicao WHERE id_condicao=14;
SELECT pg_catalog.setval('tramitacao.condicao_id_condicao_seq', 13, TRUE);
