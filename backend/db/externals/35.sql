# --- !Ups

-- Adicionar novas condições do manejo digital

INSERT INTO tramitacao.condicao(id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (22, 4, 'Manejo digital Aguardando Análise de Shape', 1);
INSERT INTO tramitacao.condicao(id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (23, 4, 'Manejo digital em análise de Shape', 1);

-- Adicionar novas ações do manejo digital

INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (45, 'Iniciar análise de Shape do manejo florestal', 1, 1);
INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (46, 'Finalizar análise de Shape do manejo florestal', 1, 1);

-- Adicionar novas tramitações do manejo digital

INSERT INTO tramitacao.transicao(id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (49, 45, 22, 23, NULL, NULL);
INSERT INTO tramitacao.transicao(id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (50, 46, 23, 17, NULL, NULL);

-- Alterando fluxo de manejo digital

UPDATE tramitacao.fluxo SET id_condicao_inicial = 22 WHERE id_fluxo = 2;

# --- !Downs

-- Alterando fluxo de manejo digital

UPDATE tramitacao.fluxo SET id_condicao_inicial = 17 WHERE id_fluxo = 2;

-- Remover tramitação do manejo digital

DELETE FROM tramitacao.transicao WHERE id_transicao in (50, 49);

-- Remover ação do manejo digital

DELETE FROM tramitacao.acao WHERE id_acao in (46, 45);

-- Remover condições do manejo digital

DELETE FROM tramitacao.condicao WHERE id_condicao in (23, 22);
