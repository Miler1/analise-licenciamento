# --- !Ups

INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final) 
	VALUES (32, 40, 15);
SELECT setval('tramitacao.transicao_id_transicao_seq', coalesce(max(id_transicao), 1)) FROM tramitacao.transicao;

INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final) 
	VALUES (34, 40, 16);
SELECT setval('tramitacao.transicao_id_transicao_seq', coalesce(max(id_transicao), 1)) FROM tramitacao.transicao;


# --- !Downs

DELETE FROM tramitacao.transicao WHERE id_acao = 34 AND id_condicao_inicial = 40 AND id_condicao_final = 16;
SELECT setval('tramitacao.transicao_id_transicao_seq', coalesce(max(id_transicao), 1)) FROM tramitacao.transicao;

DELETE FROM tramitacao.transicao WHERE id_acao = 32 AND id_condicao_inicial = 40 AND id_condicao_final = 15;
SELECT setval('tramitacao.transicao_id_transicao_seq', coalesce(max(id_transicao), 1)) FROM tramitacao.transicao;

