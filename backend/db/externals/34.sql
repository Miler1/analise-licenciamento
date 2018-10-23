# --- !Ups

-- Adicionando nova condição do licenciamento

INSERT INTO tramitacao.condicao(id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (21, 3, 'Em prorrogação', 1);

SELECT setval('tramitacao.condicao_id_condicao_seq', max(id_condicao)) FROM tramitacao.condicao;

-- Adicionando novas ações do licenciamento

INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (43, 'Prorrogar licença', 1, 1);
INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (44, 'Arquivar prorrogação por renovação', 1, 1);

SELECT setval('tramitacao.acao_id_acao_seq', max(id_acao)) FROM tramitacao.acao;

-- Adicionanando novas transições do licenciamento

INSERT INTO tramitacao.transicao(id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (47, 43, 14, 21, NULL, NULL);
INSERT INTO tramitacao.transicao(id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (48, 44, 21, 6, NULL, NULL);


SELECT setval('tramitacao.transicao_id_transicao_seq', max(id_transicao)) FROM tramitacao.transicao;

# --- !Downs

DELETE FROM tramitacao.transicao WHERE id_transicao in (47, 48);

DELETE FROM tramitacao.acao WHERE id_acao in (43, 44);

DELETE FROM tramitacao.condicao WHERE id_condicao = 21;

SELECT setval('tramitacao.condicao_id_condicao_seq', max(id_condicao)) FROM tramitacao.condicao;
SELECT setval('tramitacao.acao_id_acao_seq', max(id_acao)) FROM tramitacao.acao;
SELECT setval('tramitacao.transicao_id_transicao_seq', max(id_transicao)) FROM tramitacao.transicao;