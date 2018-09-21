# --- !Ups

-- Adicionar ações para a renovação de licencas simples

INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (38, 'Arquivar por renovação', 1, 1);
INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (38, 14, 6, NULL, NULL);

INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (39, 'Renovar licença sem alterações', 1, 1);
INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (39, 1, 12, NULL, NULL);

SELECT pg_catalog.setval('tramitacao.acao_id_acao_seq', 39, TRUE);

# --- !Downs

-- Remover ações que eram para renovação de licencas simples

-- Down Antiga v1
-- Remover depois
-- UPDATE tramitacao.historico_objeto_tramitavel SET id_acao = 31 WHERE id_acao in (38, 39);
-- DELETE FROM tramitacao.transicao WHERE id_acao in (38, 39);
-- DELETE FROM tramitacao.acao WHERE id_acao in (38, 39);

-- Down Antiga v2
-- Remover depois
-- UPDATE tramitacao.historico_objeto_tramitavel SET id_acao = 31 WHERE id_acao = 38;
-- DELETE FROM tramitacao.transicao WHERE id_acao = 38;
-- DELETE FROM tramitacao.acao WHERE id_acao = 38;

UPDATE tramitacao.historico_objeto_tramitavel SET id_acao = 31 WHERE id_acao in (38, 39);
DELETE FROM tramitacao.transicao WHERE id_acao in (38, 39);
DELETE FROM tramitacao.acao WHERE id_acao in (38, 39);