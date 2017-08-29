# --- !Ups

INSERT INTO portal_seguranca.permissao( codigo, data_cadastro, nome, id_modulo)
VALUES ('INICIAR_PARECER_JURIDICO', now(), 'Iniciar/continuar parecer da análise jurídica', (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'ANL'));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES (8, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'INICIAR_PARECER_JURIDICO'));


# --- !Downs

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil = 8 AND id_permissao IN (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'INICIAR_PARECER_JURIDICO');

DELETE FROM portal_seguranca.permissao WHERE codigo = 'INICIAR_PARECER_JURIDICO';