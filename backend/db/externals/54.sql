# --- !Ups

# --- !Ups

--EXECUTAR NO ENTRADA UNICA (utilitarios)

-- Inserir permissão de visualizar notificações
INSERT INTO portal_seguranca.permissao (codigo, data_cadastro, nome, id_modulo) 
VALUES ('ANL_VISUALIZAR_NOTIFICACAO', now(), 'Visualizar Notificação', (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

-- Inserir permissão para o perfil Analista GEO
INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) 
VALUES ((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')), 
(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_VISUALIZAR_NOTIFICACAO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));



# --- !Downs

-- Deleta permissão para o perfil Analista GEO
DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista GEO'and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

-- Deleta permissão de visualizar notificações
DELETE FROM portal_seguranca.permissao WHERE codigo='ANL_VISUALIZAR_NOTIFICACAO';

