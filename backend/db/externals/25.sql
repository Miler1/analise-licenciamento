# --- !Ups

DELETE FROM tramitacao.config_situacao WHERE id_transicao = 21;

# --- !Downs

INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (27, 1, 21, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (28, 2, 21, 0);