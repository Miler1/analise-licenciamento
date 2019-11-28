# --- !Ups

INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final) VALUES (11, 8, 9);


# --- !Downs

DELETE FROM tramitacao.transicao WHERE id_acao=11 and id_condicao_inicial=8 and id_condicao_final=9;

