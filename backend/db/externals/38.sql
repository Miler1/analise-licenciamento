# --- !Ups

-- Adicionar/Alterar ações do manejo digital

UPDATE tramitacao.acao SET tx_descricao = 'Indeferir análise do shape do manejo digital' WHERE id_acao = 42;
INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (48, 'Indeferir análise técnica do manejo digital', 1, 1);

-- Adicionar novas transições do manejo digital

INSERT INTO tramitacao.transicao(id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (52, 48, 18, 20, NULL, NULL);




# --- !Downs

-- Desfazer Adicionar novas transições do manejo digital

DELETE FROM tramitacao.transicao WHERE  id_transicao = 52;


-- Desfazer Adicionar/Alterar ações do manejo digital

UPDATE tramitacao.acao SET tx_descricao = 'Indeferir processo do manejo digital' WHERE id_acao = 42;
DELETE FROM tramitacao.acao WHERE id_acao = 48;