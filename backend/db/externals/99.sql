# --- !Ups

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor PRESIDENTE' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'DTI');

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor TECNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'DTI');
	
INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES 
	((SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Diretor TÉCNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'DT'));

INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES 
	((SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Diretor PRESIDENTE' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GAB'));


# --- !Downs

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor PRESIDENTE' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GAB');

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor TECNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'DT');

INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES 
	((SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Diretor TÉCNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'DTI'));

INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES 
	((SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Diretor PRESIDENTE' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'DTI'));