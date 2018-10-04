# --- !Ups

-- Adicionar condições do manejo digital

INSERT INTO tramitacao.condicao(id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (17, NULL, 'Manejo digital aguardando análise técnica', 1);
INSERT INTO tramitacao.condicao(id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (18, NULL, 'Manejo digital em análise técnica', 1);
INSERT INTO tramitacao.condicao(id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (19, NULL, 'Manejo digital deferido', 1);
INSERT INTO tramitacao.condicao(id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (20, NULL, 'Manejo digital indeferido', 1);

-- Adicionar fluxo do manejo digital

INSERT INTO tramitacao.fluxo(id_fluxo, id_condicao_inicial, tx_descricao, dt_prazo) VALUES (2, 17, 'Processo de Análise do Manejo Digital', NULL);

-- Adicionar condições do manejo digital

INSERT INTO tramitacao.tipo_objeto_tramitavel(id_tipo_objeto_tramitavel, tx_descricao, fl_ativo) VALUES (2,'Manejo digital', 2);

-- Adicionar etapa do manejo digital e definir a etapa nas condições criadas

INSERT INTO tramitacao.etapa(id_etapa, id_fluxo, tx_etapa, dt_prazo) VALUES (4, 2, 'Análise técnica do manejo digital', NULL);
UPDATE tramitacao.condicao SET id_etapa = 4 WHERE id_condicao in (17, 18, 19, 20);

-- Adicionar ação do manejo digital

INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (40, 'Iniciar análise técnica do manejo florestal', 1, 1);
INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (41, 'Deferir análise técnica do manejo florestal', 1, 1);
INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (42, 'Indeferir análise técnica do manejo florestal', 1, 1);

-- Adicionar tramitação do manejo digital

INSERT INTO tramitacao.transicao(id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (42, 40, 17, 18, NULL, NULL);
INSERT INTO tramitacao.transicao(id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (43, 41, 18, 19, NULL, NULL);
INSERT INTO tramitacao.transicao(id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (44, 42, 18, 20, NULL, NULL);


# --- !Downs

-- Remover tramitação do manejo digital

DELETE FROM tramitacao.transicao WHERE id_transicao in (42, 43, 44);

-- Remover ação do manejo digital

DELETE FROM tramitacao.acao WHERE id_acao in (40, 41, 42);

-- Remover etapa do manejo digital

UPDATE tramitacao.condicao SET id_etapa = NULL WHERE id_condicao in (17, 18, 19, 20);
DELETE FROM tramitacao.etapa WHERE id_etapa = 4;

-- Remover fluxo do manejo digital

DELETE FROM tramitacao.fluxo WHERE id_fluxo = 2;

-- Remover condições do manejo digital

DELETE FROM tramitacao.condicao WHERE id_condicao in (17, 18, 19, 20);