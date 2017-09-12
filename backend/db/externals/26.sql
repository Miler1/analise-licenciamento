# --- !Ups

INSERT INTO portal_seguranca.permissao(codigo, data_cadastro, nome, id_modulo) VALUES 
('CONSULTAR_LICENCAS_EMITIDAS',now(),'Consultar licenças emitidas', (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'ANL'));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES (3, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'CONSULTAR_LICENCAS_EMITIDAS'));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES (4, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'CONSULTAR_LICENCAS_EMITIDAS'));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES (5, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'CONSULTAR_LICENCAS_EMITIDAS'));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES (6, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'CONSULTAR_LICENCAS_EMITIDAS'));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES (7, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'CONSULTAR_LICENCAS_EMITIDAS'));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES (8, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'CONSULTAR_LICENCAS_EMITIDAS'));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES (9, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'CONSULTAR_LICENCAS_EMITIDAS'));

# --- !Downs

DELETE FROM portal_seguranca.permissao_perfil WHERE id_permissao = (SELECT id_permissao FROM portal_seguranca.permissao WHERE codigo = 'CONSULTAR_LICENCAS_EMITIDAS');

DELETE FROM portal_seguranca.permissao WHERE codigo = 'CONSULTAR_LICENCAS_EMITIDAS';