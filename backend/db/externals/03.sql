# --- !Ups

UPDATE portal_seguranca.perfil SET nome = 'Secretário' WHERE nome = 'Diretor PRESIDENTE';
UPDATE portal_seguranca.perfil SET nome = 'Coordenador' WHERE nome = 'gerente TÉCNICO';

# --- !Downs

UPDATE portal_seguranca.perfil SET nome = 'Diretor PRESIDENTE' WHERE nome = 'Secretário';
UPDATE portal_seguranca.perfil SET nome = 'gerente TÉCNICO' WHERE nome = 'Coordenador';
