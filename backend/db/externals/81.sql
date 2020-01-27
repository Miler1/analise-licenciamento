# --- !Ups

UPDATE tramitacao.acao SET tx_descricao = 'Solicitar desvínculo análise Geo' WHERE tx_descricao = 'Solicitar desvínculo';

INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES 
	(69, 'Aprovar solicitação de desvínculo do Analista técnico', 1, 1);
INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES 
	(70, 'Negar solicitação de desvínculo do Analista técnico', 1, 1);
INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES 
	(71, 'Solicitar desvínculo análise técnica', 1, 1);

DELETE FROM tramitacao.transicao WHERE id_acao = 59 AND id_condicao_inicial = 8 AND id_condicao_final = 33;

INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final) VALUES 
	(69, 33, 8);
INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final) VALUES 
	(70, 33, 8);
INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final) VALUES 
	(71, 8, 33);


# --- !Downs

DELETE FROM tramitacao.transicao WHERE id_acao IN (69, 70, 71);

INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final) VALUES
	(59, 8, 33);

DELETE FROM tramitacao.acao WHERE id_acao IN (69, 70, 71);

UPDATE tramitacao.acao SET tx_descricao = 'Solicitar desvínculo' WHERE tx_descricao = 'Solicitar desvínculo análise Geo';






