# --- !Ups

INSERT INTO tramitacao.etapa(id_etapa, id_fluxo, tx_etapa) VALUES (6, 1, 'Análise gerente');

INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (31, 6, 'Em análise pelo gerente', 1);
INSERT INTO tramitacao.acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (62, 'Iniciar análise gerente', 1, 1);

INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (62, 27, 31, NULL, NULL);
INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (62, 10, 31, NULL, NULL);

# --- !Downs

DELETE FROM tramitacao.transicao WHERE id_acao = 62 AND id_condicao_inicial = 27 AND id_condicao_final = 31;
DELETE FROM tramitacao.transicao WHERE id_acao = 62 AND id_condicao_inicial = 10 AND id_condicao_final = 31;

DELETE FROM tramitacao.acao WHERE id_acao = 62;
DELETE FROM tramitacao.condicao WHERE id_condicao = 31;

DELETE FROM tramitacao.etapa WHERE id_etapa = 6 AND id_fluxo = 1 AND tx_etapa = 'Análise gerente';