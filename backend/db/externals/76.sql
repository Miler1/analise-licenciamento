# --- !Ups

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) VALUES 
	((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista TÉCNICO' AND id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')), 
	 (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_VISUALIZAR_NOTIFICACAO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));

# --- !Downs

DELETE FROM portal_seguranca.permissao_perfil WHERE 
id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista TÉCNICO' AND 
id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) AND 
id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_VISUALIZAR_NOTIFICACAO' AND 
id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));


