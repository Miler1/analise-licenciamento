# --- !Ups

INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) 
VALUES (19, 3, 9, 4, NULL, NULL);

SELECT pg_catalog.setval('tramitacao.transicao_id_transicao_seq', 19, true);

# --- !Downs

DELETE FROM tramitacao.transicao WHERE id_transicao=19;

SELECT pg_catalog.setval('tramitacao.transicao_id_transicao_seq', 18, true);