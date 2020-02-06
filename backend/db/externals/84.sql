# --- !Ups

INSERT INTO portal_seguranca.setor(data_cadastro, nome, sigla, tipo_setor, removido, ativo) VALUES 
	('2020-01-06', 'Diretoria', 'DTI', 3, false, true);

INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES 
	((SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Diretor' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'AAL')),
	(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'DTI'));

INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES 
	((SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Presidente' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'AAL')),
	(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'DTI'));


# --- !Downs

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Presidente' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'AAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'DTI');

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Diretor' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'AAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'DTI');

DELETE FROM portal_seguranca.setor WHERE nome = 'Diretoria' and sigla = 'DTI';