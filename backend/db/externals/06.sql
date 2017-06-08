# --- !Ups

-- Insert de um permissão
INSERT INTO portal_seguranca.permissao( codigo, data_cadastro, nome, id_modulo)
VALUES ('CONSULTAR_PROCESSO',now(),'Listar Processo',(SELECT id
FROM portal_seguranca.modulo
WHERE sigla = 'ANL'));

-- Vinculação da nova permissão para os perfis competentes
INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES (3, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'CONSULTAR_PROCESSO'));
INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES (4, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'CONSULTAR_PROCESSO'));
INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES (5, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'CONSULTAR_PROCESSO'));
INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES (6, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'CONSULTAR_PROCESSO'));
INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES (7, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'CONSULTAR_PROCESSO'));
INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES (8, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'CONSULTAR_PROCESSO'));
INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES (9, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'CONSULTAR_PROCESSO'));


# --- !Downs

DELETE FROM portal_seguranca.permissao_perfil WHERE id_permissao=(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'CONSULTAR_PROCESSO');

DELETE FROM portal_seguranca.permissao WHERE codigo='CONSULTAR_PROCESSO';