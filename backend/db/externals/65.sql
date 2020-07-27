# --- !Ups


INSERT INTO portal_seguranca.permissao (codigo, data_cadastro, nome, id_modulo) 
	VALUES ('ANL_SALVAR_INCONSISTENCIA_TECNICA', now(), 'Salvar inconsistência técnica', (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

INSERT INTO portal_seguranca.permissao (codigo, data_cadastro, nome, id_modulo) 
	VALUES ('ANL_SALVAR_INCONSISTENCIA_GEO', now(), 'Salvar inconsistência GEO', (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

INSERT INTO portal_seguranca.permissao (codigo, data_cadastro, nome, id_modulo) 
	VALUES ('ANL_EXCLUIR_INCONSISTENCIA_TECNICA', now(), 'Excluir inconsistência técnica', (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

INSERT INTO portal_seguranca.permissao (codigo, data_cadastro, nome, id_modulo) 
	VALUES ('ANL_EXCLUIR_INCONSISTENCIA_GEO', now(), 'Salvar inconsistência GEO', (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));


INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) 
	VALUES ((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista TÉCNICO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')), 
	(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_SALVAR_INCONSISTENCIA_TECNICA' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));


INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) 
	VALUES ((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')), 
	(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_SALVAR_INCONSISTENCIA_GEO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));


INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) 
	VALUES ((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista TÉCNICO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')), 
	(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_EXCLUIR_INCONSISTENCIA_TECNICA' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));


INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) 
	VALUES ((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')), 
	(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_EXCLUIR_INCONSISTENCIA_GEO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));


# --- !Downs

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) 
AND id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_EXCLUIR_INCONSISTENCIA_GEO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista TÉCNICO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'))
AND id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_EXCLUIR_INCONSISTENCIA_TECNICA' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'))
AND id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_SALVAR_INCONSISTENCIA_GEO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista TÉCNICO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'))
AND id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_SALVAR_INCONSISTENCIA_TECNICA' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));


DELETE FROM portal_seguranca.permissao WHERE codigo = 'ANL_EXCLUIR_INCONSISTENCIA_GEO';
DELETE FROM portal_seguranca.permissao WHERE codigo = 'ANL_EXCLUIR_INCONSISTENCIA_TECNICA';
DELETE FROM portal_seguranca.permissao WHERE codigo = 'ANL_SALVAR_INCONSISTENCIA_GEO';
DELETE FROM portal_seguranca.permissao WHERE codigo = 'ANL_SALVAR_INCONSISTENCIA_TECNICA';


