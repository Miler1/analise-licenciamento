# --- !Ups

INSERT INTO tramitacao.acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (68, 'Validar parecer técnico gerente', 1, 1);

INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (15, 36, 9, NULL, NULL);
INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (68, 36, 29, NULL, NULL);
INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (25, 36, 8, NULL, NULL);


# --- !Downs

DELETE FROM tramitacao.transicao WHERE id_acao = 25 AND id_condicao_inicial = 36 AND id_condicao_final = 8; 
DELETE FROM tramitacao.transicao WHERE id_acao = 68 AND id_condicao_inicial = 36 AND id_condicao_final = 29;
DELETE FROM tramitacao.transicao WHERE id_acao = 15 AND id_condicao_inicial = 36 AND id_condicao_final = 9

DELETE FROM tramitacao.acao WHERE id_acao = 68 AND tx_descricao = 'Validar parecer técnico gerente';



