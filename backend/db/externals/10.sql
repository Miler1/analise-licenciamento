# --- !Ups

INSERT INTO portal_seguranca.permissao( codigo, data_cadastro, nome, id_modulo)
VALUES ('VINCULAR_PROCESSO_TECNICO', now(), 'Validar parecer da análise jurídica', (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'ANL'));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES (6, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'VINCULAR_PROCESSO_TECNICO'));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES (7, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'VINCULAR_PROCESSO_TECNICO'));


# --- !Downs


DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil=6 AND id_permissao=(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'VINCULAR_PROCESSO_TECNICO');

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil=7 AND id_permissao=(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'VINCULAR_PROCESSO_TECNICO');

DELETE FROM portal_seguranca.permissao WHERE codigo='VINCULAR_PROCESSO_TECNICO';