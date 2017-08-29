# --- !Ups

INSERT INTO portal_seguranca.permissao( codigo, data_cadastro, nome, id_modulo)
VALUES ('VALIDAR_PARECERES_JURIDICO_TECNICO', now(), 'Validar pareceres das análises jurídica e técnica', (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'ANL'));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES (9, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'VALIDAR_PARECERES_JURIDICO_TECNICO'));

# --- !Downs

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil=9 
AND id_permissao=(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'VALIDAR_PARECERES_JURIDICO_TECNICO');

DELETE FROM portal_seguranca.permissao WHERE codigo = 'VALIDAR_PARECERES_JURIDICO_TECNICO';