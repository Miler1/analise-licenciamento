# --- !Ups

INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final) 
	VALUES (12, 9, 10);
SELECT setval('tramitacao.transicao_id_transicao_seq', coalesce(max(id_transicao), 1)) FROM tramitacao.transicao;


# --- !Downs
DELETE FROM tramitacao.transicao WHERE id_acao = 12 AND id_condicao_inicial = 9 AND id_condicao_final = 10 ;
SELECT setval('tramitacao.transicao_id_transicao_seq', coalesce(max(id_transicao), 1)) FROM tramitacao.transicao;

