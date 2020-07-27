# --- !Ups

DELETE FROM tramitacao.config_situacao WHERE id_transicao in (16,17,29);

# --- !Downs

INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES(21, 1, 16, 0, NULL);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES(22, 2, 16, 0, NULL);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES(23, 1, 17, 0, NULL);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES(24, 2, 17, 0, NULL);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES(37, 1, 29, 0, NULL);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES(38, 2, 29, 0, NULL);
