# --- !Ups

INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final) VALUES (39, 25, 25);

# --- !Downs

DELETE FROM tramitacao.transicao WHERE id_acao =39 and id_condicao_inicial=25 and id_condicao_final=25;

