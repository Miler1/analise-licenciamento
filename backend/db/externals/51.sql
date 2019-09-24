# --- !Ups

INSERT INTO tramitacao.acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (63, 'Validar parecer geo pelo gerente', 1, 1);

INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (55, 31, 26, NULL, NULL);
INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (63, 31, 8, NULL, NULL);
INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (58, 31, 26, NULL, NULL);

# --- !Downs

DELETE FROM tramitacao.acao WHERE id_acao = 63

DELETE FROM tramitacao.transicao WHERE id_acao = 55 AND id_condicao_inicial = 31 AND id_condicao_final = 26;
DELETE FROM tramitacao.transicao WHERE id_acao = 63 AND id_condicao_inicial = 31 AND id_condicao_final = 6;
DELETE FROM tramitacao.transicao WHERE id_acao = 58 AND id_condicao_inicial = 31 AND id_condicao_final = 26;


