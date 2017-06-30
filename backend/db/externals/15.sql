# --- !Ups

INSERT INTO portal_seguranca.permissao( codigo, data_cadastro, nome, id_modulo)
VALUES ('VALIDAR_PARECER_TECNICO', now(), 'Validar parecer da análise técnica', (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'ANL'));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES (6, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'VALIDAR_PARECER_TECNICO'));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES (7, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'VALIDAR_PARECER_TECNICO'));

# --- !Downs

DELETE FROM portal_seguranca.permissao_perfil WHERE id_permissao=(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'VALIDAR_PARECER_TECNICO');
DELETE FROM portal_seguranca.permissao WHERE codigo='VALIDAR_PARECER_TECNICO';