# --- !Ups

INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) 
	SELECT 37, id_condicao, 6, null, null FROM tramitacao.condicao WHERE id_condicao NOT IN (6, 14, 15, 16, 32);

INSERT INTO tramitacao.etapa (id_etapa, id_fluxo, tx_etapa, dt_prazo) VALUES (7, 1, 'Análise finalizada', NULL); 

INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (34, 7, 'Análise finalizada', 1);

SELECT setval('tramitacao.condicao_id_condicao_seq', coalesce(max(id_condicao), 1)) FROM tramitacao.condicao;


# --- !Downs


DELETE FROM tramitacao.condicao WHERE id_condicao=34 and id_etapa = 7;

DELETE FROM tramitacao.etapa WHERE id_etapa = 7 ;

DELETE FROM tramitacao.transicao WHERE id_acao = 37 and id_condicao_final=6 and id_condicao_inicial IN (SELECT id_condicao FROM tramitacao.condicao WHERE id_condicao NOT IN (6,32));

SELECT setval('tramitacao.condicao_id_condicao_seq', coalesce(max(id_condicao), 1)) FROM tramitacao.condicao;
