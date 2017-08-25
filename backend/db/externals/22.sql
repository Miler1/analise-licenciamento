# --- !Ups

INSERT INTO portal_seguranca.permissao( codigo, data_cadastro, nome, id_modulo)
VALUES ('APROVAR_ANALISE', now(), 'Aprovar Análise', (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'ANL'));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES (9, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'APROVAR_ANALISE'));

# --- !Downs

DELETE FROM portal_seguranca.permissao_perfil WHERE id_permissao=(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'APROVAR_ANALISE');

DELETE FROM portal_seguranca.permissao WHERE codigo = 'APROVAR_ANALISE';