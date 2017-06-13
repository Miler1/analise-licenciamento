# --- !Ups

INSERT INTO tramitacao.acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (18, 'Iniciar processo', 1, 1);

SELECT pg_catalog.setval('tramitacao.acao_id_acao_seq', 18, true);

INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (0, null, 'Estado inicial', 1);

INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (18, 18, 0, 1, NULL, NULL);

SELECT pg_catalog.setval('tramitacao.transicao_id_transicao_seq', 18, true);

# ---!Downs

delete from tramitacao.transicao where id_transicao = 18;

delete from tramitacao.condicao where id_condicao = 0;

delete from tramitacao.acao where id_acao = 18;


