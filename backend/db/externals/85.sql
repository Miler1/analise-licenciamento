# --- !Ups

INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior)
VALUES (37, 35, 6, NULL, NULL);

INSERT INTO tramitacao.acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel)
VALUES (73, 'Iniciar analise Técnica por volta de notificação', 1, 1);

INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior)
VALUES (73, 25, 8, NULL, NULL);


# --- !Downs

DELETE FROM tramitacao.transicao WHERE id_acao =73 AND id_condicao_inicial=25 AND id_condicao_final=8 ;

DELETE FROM tramitacao.acao WHERE id_acao=73;

DELETE FROM tramitacao.transicao WHERE id_acao=37 AND id_condicao_inicial=35 AND id_condicao_final=6; 


