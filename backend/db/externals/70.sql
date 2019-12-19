# --- !Ups

INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES 
	((SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Gerente' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GCAP'));

INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES 
	((SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Gerente' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GFAU'));

INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES 
	((SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Gerente' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GERM'));

INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES 
	((SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Gerente' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	 (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GECF'));

INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES 
	((SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Gerente' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GELI'));

INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES 
	((SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Gerente' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	 (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GECP'));

INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES 
	((SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Gerente' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GERH'));


# --- !Downs

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Gerente' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
 AND id_setor =(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GERH');

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Gerente' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
 AND id_setor =(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GECP');

 DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Gerente' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
 AND id_setor =(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GELI');

  DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Gerente' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
 AND id_setor =(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GECF');

   DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Gerente' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
 AND id_setor =(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GERM');

 DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Gerente' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
 AND id_setor =(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GFAU');

  DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Gerente' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
 AND id_setor =(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GCAP');