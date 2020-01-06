# --- !Ups

INSERT INTO tramitacao.transicao(id_condicao_inicial,id_condicao_final,id_acao) 
	VALUES (9,10,13);
SELECT setval('tramitacao.transicao_id_transicao_seq', coalesce(max(id_transicao), 1)) FROM tramitacao.transicao;


# --- !Downs

DELETE FROM tramitacao.transicao WHERE id_condicao_inicial = 9 AND id_condicao_final = 10 AND id_acao = 13;
SELECT setval('tramitacao.transicao_id_transicao_seq', coalesce(max(id_transicao), 1)) FROM tramitacao.transicao;

