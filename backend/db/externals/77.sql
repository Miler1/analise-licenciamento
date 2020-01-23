# --- !Ups

INSERT INTO tramitacao.acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES 
	(67, 'Iniciar análise técnica gerente', 1, 1);

INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES 
	(36, 6, 'Em análise técnica pelo gerente', 1);

INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES 
	(67, 10, 36, NULL, NULL);


# --- !Downs

DELETE FROM tramitacao.transicao WHERE id_acao = 67 AND id_condicao_inicial = 10 AND id_condicao_final = 36 ;

DELETE FROM tramitacao.condicao WHERE id_condicao = 36 AND id_etapa = 6;

DELETE FROM tramitacao.acao WHERE id_acao = 67 AND tx_descricao = 'Iniciar análise técnica gerente'; 
