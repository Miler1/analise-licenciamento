# --- !Ups

INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES
	((SELECT id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GCAP' AND ativo));

INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES
	((SELECT id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GFAU' AND ativo));

INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES
	((SELECT id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GERM' AND ativo));

INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES
	((SELECT id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	 (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GECF' AND ativo));

INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES
	((SELECT id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GELI' AND ativo));

INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES
	((SELECT id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	 (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GECP' AND ativo));

INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES
	((SELECT id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GERH' AND ativo));

# --- !Downs

DELETE FROM  portal_seguranca.perfil_setor WHERE id_perfil = 
	(SELECT id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')) 
	AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GCAP' AND ativo);

DELETE FROM  portal_seguranca.perfil_setor WHERE id_perfil = 
	(SELECT id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')) 
	AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GFAU' AND ativo);

DELETE FROM  portal_seguranca.perfil_setor WHERE id_perfil = 
	(SELECT id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')) 
	AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GERM' AND ativo);

DELETE FROM  portal_seguranca.perfil_setor WHERE id_perfil = 
	(SELECT id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')) 
	AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GECF' AND ativo);

DELETE FROM  portal_seguranca.perfil_setor WHERE id_perfil = 
	(SELECT id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')) 
	AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GELI' AND ativo);

DELETE FROM  portal_seguranca.perfil_setor WHERE id_perfil = 
	(SELECT id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')) 
	AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GECP' AND ativo);

DELETE FROM  portal_seguranca.perfil_setor WHERE id_perfil = 
	(SELECT id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')) 
	AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GERH' AND ativo);
