# --- !Ups

BEGIN;

DELETE FROM tramitacao.config_situacao WHERE id_transicao IS NOT NULL;
DELETE FROM tramitacao.impedimento_transicao WHERE id_transicao IS NOT NULL;
DELETE FROM tramitacao.transicao;

ALTER SEQUENCE tramitacao.transicao_id_transicao_seq RESTART WITH 1;
ALTER SEQUENCE tramitacao.impedimento_transicao_id_impedimento_transicao_seq RESTART WITH 1;
ALTER SEQUENCE tramitacao.config_situacao_id_config_situacao_seq RESTART WITH 1;

INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final) VALUES (18, 0, 25);
INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final) VALUES (51, 25, 26);
INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final) VALUES (49, 26, 27);
INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final) VALUES (50, 26, 27);
INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final) VALUES (52, 27, 25);
INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final) VALUES (55, 27, 25);
INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final) VALUES (53, 27, 29);
INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final) VALUES (54, 27, 29);
INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final) VALUES (3, 26, 4);
INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final) VALUES (58, 29, 25);
INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final) VALUES (58, 29, 25);
INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final) VALUES (10, 27, 8);

UPDATE tramitacao.fluxo SET id_condicao_inicial = 25 WHERE id_fluxo = 1;

COMMIT;

# --- !Downs

BEGIN;

DELETE FROM tramitacao.config_situacao WHERE id_transicao IS NOT NULL;
DELETE FROM tramitacao.impedimento_transicao WHERE id_transicao IS NOT NULL;
DELETE FROM tramitacao.transicao;

ALTER SEQUENCE tramitacao.transicao_id_transicao_seq RESTART WITH 1;
ALTER SEQUENCE tramitacao.impedimento_transicao_id_impedimento_transicao_seq RESTART WITH 1;
ALTER SEQUENCE tramitacao.config_situacao_id_config_situacao_seq RESTART WITH 1;

INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (1, 1, 1, 2, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (2, 2, 2, 3, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (3, 3, 3, 4, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (4, 4, 3, 5, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (5, 5, 5, 2, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (6, 6, 5, 12, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (7, 7, 5, 6, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (8, 8, 3, 5, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (9, 9, 5, 2, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (10, 10, 7, 8, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (11, 11, 8, 9, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (12, 12, 9, 10, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (13, 13, 9, 10, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (14, 14, 10, 8, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (15, 15, 10, 8, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (16, 16, 10, 13, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (17, 17, 10, 13, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (18, 18, 0, 1, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (19, 3, 9, 4, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (20, 19, 12, 7, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (21, 22, 13, 11, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (22, 23, 13, 6, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (23, 25, 13, 8, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (24, 24, 13, 7, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (25, 26, 13, 8, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (26, 10, 12, 8, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (27, 21, 9, 13, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (28, 20, 9, 13, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (29, 27, 13, 10, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (30, 28, 11, 5, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (31, 29, 5, 11, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (32, 30, 11, 13, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (33, 31, 11, 14, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (34, 32, 14, 15, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (35, 33, 15, 14, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (36, 34, 14, 16, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (37, 35, 4, 2, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (38, 36, 4, 8, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (39, 37, 4, 6, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (40, 38, 14, 6, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (41, 39, 1, 12, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (44, 40, 17, 18, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (45, 41, 18, 19, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (46, 42, 18, 20, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (47, 43, 14, 21, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (48, 44, 21, 6, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (49, 45, 22, 23, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (50, 46, 23, 17, null, null);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (52, 48, 18, 20, null, null);

INSERT INTO tramitacao.impedimento_transicao (id_impedimento_transicao, id_situacao, id_transicao, tp_impedimento) VALUES (1, 1, 6, 1);
INSERT INTO tramitacao.impedimento_transicao (id_impedimento_transicao, id_situacao, id_transicao, tp_impedimento) VALUES (2, 2, 7, 1);
INSERT INTO tramitacao.impedimento_transicao (id_impedimento_transicao, id_situacao, id_transicao, tp_impedimento) VALUES (3, 1, 16, 1);
INSERT INTO tramitacao.impedimento_transicao (id_impedimento_transicao, id_situacao, id_transicao, tp_impedimento) VALUES (4, 2, 17, 1);
INSERT INTO tramitacao.impedimento_transicao (id_impedimento_transicao, id_situacao, id_transicao, tp_impedimento) VALUES (5, 1, 21, 1);
INSERT INTO tramitacao.impedimento_transicao (id_impedimento_transicao, id_situacao, id_transicao, tp_impedimento) VALUES (6, 2, 22, 1);
INSERT INTO tramitacao.impedimento_transicao (id_impedimento_transicao, id_situacao, id_transicao, tp_impedimento) VALUES (7, 3, 6, 0);
INSERT INTO tramitacao.impedimento_transicao (id_impedimento_transicao, id_situacao, id_transicao, tp_impedimento) VALUES (8, 3, 31, 1);
INSERT INTO tramitacao.impedimento_transicao (id_impedimento_transicao, id_situacao, id_transicao, tp_impedimento) VALUES (9, 4, 37, 1);
INSERT INTO tramitacao.impedimento_transicao (id_impedimento_transicao, id_situacao, id_transicao, tp_impedimento) VALUES (10, 5, 38, 1);

INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (1, 1, 4, 1, null);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (2, 2, 8, 1, null);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (3, 1, 3, 0, null);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (4, 2, 3, 0, null);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (5, 1, 5, 0, null);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (6, 2, 5, 0, null);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (7, 1, 6, 0, null);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (8, 2, 6, 0, null);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (9, 1, 7, 0, null);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (10, 2, 7, 0, null);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (11, 1, 9, 0, null);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (12, 2, 9, 0, null);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (13, 1, 12, 1, null);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (14, 2, 13, 1, null);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (15, 1, 3, 0, null);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (16, 2, 3, 0, null);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (17, 1, 14, 0, null);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (18, 2, 14, 0, null);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (19, 1, 15, 0, null);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (20, 2, 15, 0, null);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (25, 1, 27, 1, null);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (26, 2, 28, 1, null);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (29, 1, 22, 0, null);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (30, 2, 22, 0, null);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (31, 1, 24, 0, null);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (32, 2, 24, 0, null);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (33, 1, 23, 0, null);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (34, 2, 23, 0, null);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (35, 1, 25, 0, null);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (36, 2, 25, 0, null);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (37, 3, 30, 1, null);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (38, 3, 31, 0, null);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (39, 4, 3, 1, null);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (40, 4, 37, 0, null);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (41, 4, 39, 0, null);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (42, 5, 19, 1, null);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (43, 5, 38, 0, null);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar, fl_pai) VALUES (44, 5, 39, 0, null);

ALTER SEQUENCE tramitacao.transicao_id_transicao_seq RESTART WITH 52;
ALTER SEQUENCE tramitacao.impedimento_transicao_id_impedimento_transicao_seq RESTART WITH 10;
ALTER SEQUENCE tramitacao.config_situacao_id_config_situacao_seq RESTART WITH 44;

UPDATE tramitacao.fluxo SET id_condicao_inicial = 1 WHERE id_fluxo = 1;

COMMIT;
