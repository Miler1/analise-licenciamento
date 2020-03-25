# --- !Ups

UPDATE tramitacao.acao SET tx_descricao='Notificar pelo Analista GEO' WHERE id_acao = 3;

INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES 
	(66, 'Notificar pelo Analista técnico', 1, 1);

UPDATE tramitacao.condicao SET id_etapa = 5, nm_condicao = 'Notificado pelo Analista GEO' WHERE id_condicao = 4;

INSERT INTO tramitacao.condicao(id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES 
	(35, 2, 'Notificado pelo Analista técnico', 1);

INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final) VALUES 
	(66, 9, 35);

# --- !Downs

DELETE FROM tramitacao.transicao WHERE id_acao = 66 AND id_condicao_inicial = 9 AND id_condicao_final =35; 

DELETE FROM tramitacao.condicao WHERE id_condicao = 35 AND nm_condicao = 'Notificado pelo Analista técnico';

UPDATE tramitacao.condicao SET id_etapa = null AND nm_condicao = 'Notificado' WHERE id_condicao = 4;

DELETE FROM tramitacao.acao WHERE id_acao = 66 AND tx_descricao = 'Notificar pelo Analista técnico';

UPDATE tramitacao.acao SET tx_descricao='Notificar' WHERE id_acao = 3;




