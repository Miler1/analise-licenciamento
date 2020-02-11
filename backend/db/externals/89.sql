# --- !Ups

INSERT INTO tramitacao.acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES 
	(74, 'Validar análise pelo diretor', 1, 1);
INSERT INTO tramitacao.acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES 
	(75, 'Invalidar análise pelo diretor', 1, 1);

INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES 
	(74, 37, 11, NULL, NULL); 

INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES 
	(75, 37, 11, NULL, NULL);

# --- !Downs

DELETE FROM tramitacao.transicao WHERE id_acao = 75 AND id_condicao_inicial=37 AND id_condicao_final=11;
DELETE FROM tramitacao.transicao WHERE id_acao = 74 AND id_condicao_inicial=37 AND id_condicao_final=11;

DELETE FROM tramitacao.acao WHERE id_acao IN (74, 75);