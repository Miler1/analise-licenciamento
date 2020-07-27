# --- !Ups

DELETE FROM tramitacao.transicao WHERE id_acao = 37 and id_condicao_final=6;

INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) 
	SELECT 37, id_condicao, 6, null, null FROM tramitacao.condicao WHERE id_condicao NOT IN (6, 14, 15, 16, 34);


# --- !Downs

INSERT INTO tramitacao.transicao ( id_acao, id_condicao_inicial, id_condicao_final) VALUES (37, 1, 6);
INSERT INTO tramitacao.transicao ( id_acao, id_condicao_inicial, id_condicao_final) VALUES (37, 2, 6);
INSERT INTO tramitacao.transicao ( id_acao, id_condicao_inicial, id_condicao_final) VALUES (37, 3, 6);
INSERT INTO tramitacao.transicao ( id_acao, id_condicao_inicial, id_condicao_final) VALUES (37, 5, 6);
INSERT INTO tramitacao.transicao ( id_acao, id_condicao_inicial, id_condicao_final) VALUES (37, 8, 6);
INSERT INTO tramitacao.transicao ( id_acao, id_condicao_inicial, id_condicao_final) VALUES (37, 9, 6);
INSERT INTO tramitacao.transicao ( id_acao, id_condicao_inicial, id_condicao_final) VALUES (37, 0, 6);
INSERT INTO tramitacao.transicao ( id_acao, id_condicao_inicial, id_condicao_final) VALUES (37, 7, 6);
INSERT INTO tramitacao.transicao ( id_acao, id_condicao_inicial, id_condicao_final) VALUES (37, 10, 6);
INSERT INTO tramitacao.transicao ( id_acao, id_condicao_inicial, id_condicao_final) VALUES (37, 12, 6);
INSERT INTO tramitacao.transicao ( id_acao, id_condicao_inicial, id_condicao_final) VALUES (37, 13, 6);
INSERT INTO tramitacao.transicao ( id_acao, id_condicao_inicial, id_condicao_final) VALUES (37, 11, 6);
INSERT INTO tramitacao.transicao ( id_acao, id_condicao_inicial, id_condicao_final) VALUES (37, 17, 6);
INSERT INTO tramitacao.transicao ( id_acao, id_condicao_inicial, id_condicao_final) VALUES (37, 18, 6);
INSERT INTO tramitacao.transicao ( id_acao, id_condicao_inicial, id_condicao_final) VALUES (37, 21, 6);
INSERT INTO tramitacao.transicao ( id_acao, id_condicao_inicial, id_condicao_final) VALUES (37, 22, 6);
INSERT INTO tramitacao.transicao ( id_acao, id_condicao_inicial, id_condicao_final) VALUES (37, 23, 6);
INSERT INTO tramitacao.transicao ( id_acao, id_condicao_inicial, id_condicao_final) VALUES (37, 19, 6);
INSERT INTO tramitacao.transicao ( id_acao, id_condicao_inicial, id_condicao_final) VALUES (37, 20, 6);
INSERT INTO tramitacao.transicao ( id_acao, id_condicao_inicial, id_condicao_final) VALUES (37, 24, 6);
INSERT INTO tramitacao.transicao ( id_acao, id_condicao_inicial, id_condicao_final) VALUES (37, 25, 6);
INSERT INTO tramitacao.transicao ( id_acao, id_condicao_inicial, id_condicao_final) VALUES (37, 26, 6);
INSERT INTO tramitacao.transicao ( id_acao, id_condicao_inicial, id_condicao_final) VALUES (37, 27, 6);
INSERT INTO tramitacao.transicao ( id_acao, id_condicao_inicial, id_condicao_final) VALUES (37, 28, 6);
INSERT INTO tramitacao.transicao ( id_acao, id_condicao_inicial, id_condicao_final) VALUES (37, 29, 6);
INSERT INTO tramitacao.transicao ( id_acao, id_condicao_inicial, id_condicao_final) VALUES (37, 4, 6);
INSERT INTO tramitacao.transicao ( id_acao, id_condicao_inicial, id_condicao_final) VALUES (37, 31, 6);
INSERT INTO tramitacao.transicao ( id_acao, id_condicao_inicial, id_condicao_final) VALUES (37, 30, 6);
INSERT INTO tramitacao.transicao ( id_acao, id_condicao_inicial, id_condicao_final) VALUES (37, 33, 6);
INSERT INTO tramitacao.transicao ( id_acao, id_condicao_inicial, id_condicao_final) VALUES (37, 32, 6);
INSERT INTO tramitacao.transicao ( id_acao, id_condicao_inicial, id_condicao_final) VALUES (37, 35, 6);


DELETE FROM tramitacao.transicao WHERE id_acao = 37 and id_condicao_final=6 and id_condicao = (SELECT id_condicao FROM tramitacao.condicao WHERE id_condicao NOT IN (6, 14, 15, 16, 34));
