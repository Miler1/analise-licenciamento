# --- !Ups

INSERT INTO portal_seguranca.permissao (codigo, data_cadastro, nome, id_modulo)	VALUES 
	('VISUALIZAR_PROTOCOLO', now(), 'Visualizar protocolo', (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));
	

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) VALUES 
	((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista TÉCNICO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'VISUALIZAR_PROTOCOLO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) VALUES 
	((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'VISUALIZAR_PROTOCOLO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) VALUES 
	((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Gerente' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'VISUALIZAR_PROTOCOLO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));



# --- !Downs

DELETE FROM portal_seguranca.permissao_perfil WHERE 
id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE nome = 'Gerente' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) AND 
id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'VISUALIZAR_PROTOCOLO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

DELETE FROM portal_seguranca.permissao_perfil WHERE 
id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) AND 
id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'VISUALIZAR_PROTOCOLO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

DELETE FROM portal_seguranca.permissao_perfil WHERE 
id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista TÉCNICO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) AND 
id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'VISUALIZAR_PROTOCOLO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));


DELETE FROM portal_seguranca.permissao WHERE codigo = 'VISUALIZAR_PROTOCOLO';