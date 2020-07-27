# --- !Ups

UPDATE portal_seguranca.perfil SET nome = 'Gerente TÉCNICO' WHERE codigo = 'GERENTE' AND id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL');
UPDATE portal_seguranca.perfil SET nome = 'Diretor TÉCNICO' WHERE codigo = 'DIRETOR' AND id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL');
UPDATE portal_seguranca.perfil SET nome = 'Diretor PRESIDENTE' WHERE codigo = 'PRESIDENTE' AND id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL');

# --- !Downs

UPDATE portal_seguranca.perfil SET nome = 'Gerente' WHERE codigo = 'GERENTE' AND id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL');
UPDATE portal_seguranca.perfil SET nome = 'Diretor' WHERE codigo = 'DIRETOR' AND id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL');
UPDATE portal_seguranca.perfil SET nome = 'Presidente' WHERE codigo = 'PRESIDENTE' AND id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL');
