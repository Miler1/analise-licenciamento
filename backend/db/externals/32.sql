# --- !Ups

-- Adicionar ações para a renovação de licencas simples

INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (38, 'Renovar licença sem alterações', 1, 1);
INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (39, 'Renovar licença com alterações', 1, 1);

INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (38, 14, 12, NULL, NULL);
INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (39, 14, 1, NULL, NULL);

# --- !Downs

-- Remover ações que eram para renovação de licencas simples

DELETE FROM tramitacao.transicao WHERE id_acao IN (38, 39);
DELETE FROM tramitacao.acao WHERE id_acao IN (38, 39);