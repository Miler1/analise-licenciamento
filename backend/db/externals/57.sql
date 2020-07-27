# --- !Ups

INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (64, 'Resolver comunicado', 1, 1);
INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (65, 'Aguardar resposta comunicado', 1, 1);

INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (32, 5, 'Aguardando resposta comunicado', 1);

INSERT INTO tramitacao.transicao(id_transicao, id_acao, id_condicao_inicial, id_condicao_final) VALUES (30, 64, 32, 27);
INSERT INTO tramitacao.transicao(id_transicao, id_acao, id_condicao_inicial, id_condicao_final) VALUES (31, 65, 26, 32);



# --- !Downs

DELETE FROM tramitacao.acao WHERE id_acao IN (64,65);  

DELETE FROM tramitacao.condicao WHERE id_condicao = 32; 

DELETE FROM tramitacao.transicao WHERE id_transicao IN(30,31);
