# --- !Ups

INSERT INTO portal_seguranca.permissao( codigo, data_cadastro, nome, id_modulo)
VALUES ('VALIDAR_PARECER_JURIDICO', now(), 'Validar parecer da análise jurídica', (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'ANL'));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES (3, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'VALIDAR_PARECER_JURIDICO'));

# --- !Downs

DELETE FROM portal_seguranca.permissao_perfil WHERE id_permissao IN (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'VALIDAR_PARECER_JURIDICO');

DELETE FROM portal_seguranca.permissao WHERE codigo = 'VALIDAR_PARECER_JURIDICO';