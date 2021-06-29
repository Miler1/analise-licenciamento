# --- !Ups

UPDATE portal_seguranca.perfil SET nome = 'Coordenador de Licenciamento' WHERE codigo = 'COORDENADOR' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL');

UPDATE portal_seguranca.setor SET nome = 'Coordenadoria de Licenciamento e Controle Ambiental', sigla = 'CLCA'  WHERE sigla = 'CLA';


# --- !Downs

UPDATE portal_seguranca.perfil SET nome = 'Coordenador' WHERE codigo = 'COORDENADOR' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL');

UPDATE portal_seguranca.setor SET nome = 'Coordenadoria de Licenciamento Ambiental', sigla = 'CLA'  WHERE sigla = 'CLCA';


