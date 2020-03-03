# --- !Ups

INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES 
	(39, 1, 'Aguardando resposta jurídica', 1);

INSERT INTO tramitacao.acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES 
	(77, 'Resolver análise jurídica', 1, 1);

UPDATE tramitacao.transicao SET id_condicao_final = 39 WHERE id_acao = 63; 

INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES 
	(77, 39, 8, NULL, NULL);

# --- !Downs

DELETE FROM tramitacao.transicao WHERE id_acao = 77 AND id_condicao_inicial = 39 AND id_condicao_final = 8;

UPDATE tramitacao.transicao SET id_condicao_final = 8 WHERE id_acao = 63;

DELETE FROM tramitacao.acao WHERE id_acao = 77;

DELETE FROM tramitacao.condicao WHERE id_condicao = 39; 
