# --- !Ups

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor PRESIDENTE' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GELI');

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor PRESIDENTE' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GERAL');

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor PRESIDENTE' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'DTI');

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor PRESIDENTE' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GCAP');

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor PRESIDENTE' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GECF');

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor PRESIDENTE' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GECP');

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor PRESIDENTE' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GFAU');

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor PRESIDENTE' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GGEO');


DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor TÉCNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GELI');

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor TÉCNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GERAL');

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor TÉCNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'DTI');

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor TÉCNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GCAP');

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor TÉCNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GECF');

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor TÉCNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GECP');

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor TÉCNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GFAU');

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor TÉCNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GGEO');


# --- !Downs

-- fazer backup dos dados 