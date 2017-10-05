# --- !Ups

--[INICIO] Alterações no portal de segurança

INSERT INTO portal_seguranca.permissao(codigo, data_cadastro, nome, id_modulo)
VALUES ('SUSPENDER_LICENCA_EMITIDA', now(), 'Suspender licenca emitida', (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'ANL'));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES ((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Aprovador'), (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'SUSPENDER_LICENCA_EMITIDA'));

INSERT INTO portal_seguranca.permissao( codigo, data_cadastro, nome, id_modulo)
VALUES ('CANCELAR_LICENCA_EMITIDA', now(), 'Cancelar licenca emitida', (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'ANL'));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES ((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Aprovador'), (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'CANCELAR_LICENCA_EMITIDA'));

--[FIM] Alterações no portal de segurança

--[INICIO] Alterações na Tramitação

INSERT INTO tramitacao.acao(id_acao,tx_descricao,fl_ativo,fl_tramitavel) VALUES
(32, 'Suspender processo',1,1),
(33, 'Reemitir licença',1,1),
(34, 'Cancelar processo',1,1);

SELECT pg_catalog.setval('tramitacao.acao_id_acao_seq', 34, TRUE);

INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (15, 3, 'Suspenso', 1);
INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (16, 3, 'Cancelado', 1);

SELECT pg_catalog.setval('tramitacao.condicao_id_condicao_seq', 16, TRUE);

INSERT INTO tramitacao.transicao(id_transicao,id_condicao_inicial,id_condicao_final,id_acao) VALUES
(34,14,15,32),
(35,15,14,33),
(36,14,16,34);

SELECT pg_catalog.setval('tramitacao.transicao_id_transicao_seq', 36, TRUE);

UPDATE tramitacao.condicao SET nm_condicao = 'Licença emitida' WHERE id_condicao = 14;

--[FIM] Alterações na Tramitação

# --- !Downs

DELETE FROM tramitacao.transicao WHERE id_transicao IN (34, 35, 36);

UPDATE tramitacao.condicao SET nm_condicao = 'Licença emitida' WHERE id_condicao = 14;

SELECT pg_catalog.setval('tramitacao.transicao_id_transicao_seq', 33, TRUE);

DELETE FROM tramitacao.condicao WHERE id_condicao IN (15, 16);

SELECT pg_catalog.setval('tramitacao.condicao_id_condicao_seq', 14, TRUE);

DELETE FROM tramitacao.acao WHERE id_acao IN (32, 33, 34);

SELECT pg_catalog.setval('tramitacao.acao_id_acao_seq', 31, TRUE);

DELETE FROM portal_seguranca.permissao_perfil WHERE (id_perfil, id_permissao) = ((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Aprovador'), (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'CANCELAR_LICENCA_EMITIDA'));

DELETE FROM portal_seguranca.permissao WHERE codigo = 'CANCELAR_LICENCA_EMITIDA';

DELETE FROM portal_seguranca.permissao_perfil WHERE (id_perfil, id_permissao) = ((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Aprovador'), (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'SUSPENDER_LICENCA_EMITIDA'));

DELETE FROM portal_seguranca.permissao WHERE codigo = 'SUSPENDER_LICENCA_EMITIDA';

UPDATE tramitacao.condicao SET nm_condicao = 'Licenca emitida' WHERE id_condicao = 14;