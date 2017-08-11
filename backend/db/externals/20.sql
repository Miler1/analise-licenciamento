# --- !Ups

DELETE FROM tramitacao.config_situacao WHERE id_transicao in (16,17,29);

# --- !Downs

INSERT INTO tramitacao.config_situacao VALUES (37,1,29,0,null);
INSERT INTO tramitacao.config_situacao VALUES (38,2,29,0,null);
INSERT INTO tramitacao.config_situacao VALUES (21,1,16,0,null);
INSERT INTO tramitacao.config_situacao VALUES (22,2,16,0,null);
INSERT INTO tramitacao.config_situacao VALUES (23,1,17,0,null);
INSERT INTO tramitacao.config_situacao VALUES (24,2,17,0,null);