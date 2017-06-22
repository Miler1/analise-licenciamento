# --- !Ups

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil = 8 AND id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'INICIAR_PARECER_JURIDICO');

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES (5, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'INICIAR_PARECER_JURIDICO'));

INSERT INTO portal_seguranca.permissao (codigo, data_cadastro, nome, id_modulo) VALUES ('INICIAR_PARECER_TECNICO', NOW(), 'Vincular esta permissão ao perfil Analista técnico', 5);

# ---!Downs

DELETE FROM portal_seguranca.permissao WHERE codigo = 'INICIAR_PARECER_TECNICO';

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil = 5 AND  id_permissao IN (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'INICIAR_PARECER_JURIDICO');

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES (8, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'INICIAR_PARECER_JURIDICO'));
