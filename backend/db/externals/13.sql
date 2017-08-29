# --- !Ups

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil = 8 AND id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'INICIAR_PARECER_JURIDICO');

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES (5, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'INICIAR_PARECER_JURIDICO'));

INSERT INTO portal_seguranca.permissao (codigo, data_cadastro, nome, id_modulo) VALUES ('INICIAR_PARECER_TECNICO', NOW(), 'Iniciar/continuar parecer da análise técnica', (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'ANL'));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) VALUES (8, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'INICIAR_PARECER_TECNICO'));


# --- !Downs

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil = 8 AND  id_permissao IN (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'INICIAR_PARECER_TECNICO');

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil = 5 AND  id_permissao IN (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'INICIAR_PARECER_JURIDICO');

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) VALUES (8, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'INICIAR_PARECER_JURIDICO'));

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil = 8 AND id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'INICIAR_PARECER_TECNICO');
