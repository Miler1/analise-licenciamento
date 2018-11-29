# --- !Ups

-- Adicionar/Alterar ações do manejo digital

UPDATE tramitacao.acao SET tx_descricao = 'Iniciar análise técnica do manejo digital' WHERE id_acao = 40;
UPDATE tramitacao.acao SET tx_descricao = 'Deferir processo do manejo digital' WHERE id_acao = 41;
UPDATE tramitacao.acao SET tx_descricao = 'Indeferir processo do manejo digital' WHERE id_acao = 42;
UPDATE tramitacao.acao SET tx_descricao = 'Iniciar análise de Shape do manejo digital' WHERE id_acao = 45;
UPDATE tramitacao.acao SET tx_descricao = 'Finalizar análise de Shape do manejo digital' WHERE id_acao = 46;
INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (47, 'Solicitar revisão do Shape do manejo digital', 1, 1);


-- Adicionar novas transições do manejo digital

UPDATE tramitacao.transicao SET id_condicao_inicial = 22  WHERE id_transicao= 44;
INSERT INTO tramitacao.transicao(id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (51, 47, 23, 22, NULL, NULL);


# --- !Downs

-- Desfazer Adicionar novas transições do manejo digital

UPDATE tramitacao.transicao SET id_condicao_inicial = 18  WHERE id_transicao= 44;
DELETE FROM tramitacao.transicao WHERE id_transicao = 51;

-- Desfazer Adicionar/Alterar ações do manejo digital

UPDATE tramitacao.acao SET tx_descricao = 'Iniciar análise técnica do manejo florestal' WHERE id_acao = 40;
UPDATE tramitacao.acao SET tx_descricao = 'Deferir análise técnica do manejo florestal' WHERE id_acao = 41;
UPDATE tramitacao.acao SET tx_descricao = 'Indeferir análise técnica do manejo florestal' WHERE id_acao = 42;
UPDATE tramitacao.acao SET tx_descricao = 'Iniciar análise de Shape do manejo florestal' WHERE id_acao = 45;
UPDATE tramitacao.acao SET tx_descricao = 'Finalizar análise de Shape do manejo florestal' WHERE id_acao = 46;
DELETE FROM tramitacao.acao WHERE id_acao = 47;


