# --- !Ups
INSERT INTO portal_seguranca.permissao (codigo, data_cadastro, nome, id_modulo)
	VALUES ('SALVAR_INCONSISTENCIA_VISTORIA', now(), 'Salvar inconsistência de vistoria', (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));
INSERT INTO portal_seguranca.permissao (codigo, data_cadastro, nome, id_modulo)
	VALUES ('BUSCAR_INCONSISTENCIA_VISTORIA', now(), 'Visualizar inconsistência de vistoria', (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));
INSERT INTO portal_seguranca.permissao (codigo, data_cadastro, nome, id_modulo)
	VALUES ('DELETAR_INCONSISTENCIA_VISTORIA', now(), 'Deletar inconsistência de vistoria', (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));


INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
	VALUES ((SELECT id FROM portal_seguranca.perfil WHERE codigo = 'ANALISTA_TECNICO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'SALVAR_INCONSISTENCIA_VISTORIA' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
	VALUES ((SELECT id FROM portal_seguranca.perfil WHERE codigo = 'ANALISTA_TECNICO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'BUSCAR_INCONSISTENCIA_VISTORIA' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
	VALUES ((SELECT id FROM portal_seguranca.perfil WHERE codigo = 'ANALISTA_TECNICO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'DELETAR_INCONSISTENCIA_VISTORIA' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));


# --- !Downs

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE codigo = 'ANALISTA_TECNICO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) AND id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'SALVAR_INCONSISTENCIA_VISTORIA' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));


DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE codigo = 'ANALISTA_TECNICO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) AND id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'BUSCAR_INCONSISTENCIA_VISTORIA' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));


DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE codigo = 'ANALISTA_TECNICO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) AND id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'DELETAR_INCONSISTENCIA_VISTORIA' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

DELETE FROM portal_seguranca.permissao WHERE codigo='SALVAR_INCONSISTENCIA_VISTORIA';
DELETE FROM portal_seguranca.permissao WHERE codigo='BUSCAR_INCONSISTENCIA_VISTORIA';
DELETE FROM portal_seguranca.permissao WHERE codigo='DELETAR_INCONSISTENCIA_VISTORIA';
