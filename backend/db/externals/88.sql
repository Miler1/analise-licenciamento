# --- !Ups

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) VALUES 
 ((SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'DIRETOR' AND p.id_modulo_pertencente =(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
  (SELECT p.id  FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_BAIXAR_DOCUMENTO_RELATORIO_TECNICO_VISTORIA' AND p.id_modulo =(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) VALUES 
 ((SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'DIRETOR' AND p.id_modulo_pertencente =(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
 (SELECT p.id  FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_BAIXAR_DOCUMENTO_MINUTA' AND p.id_modulo =(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')));


# --- !Downs

DELETE FROM portal_seguranca.permissao_perfil WHERE 
id_perfil = (SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'DIRETOR' AND
id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')) AND 
id_permissao = (SELECT p.id FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_BAIXAR_DOCUMENTO_MINUTA');


DELETE FROM portal_seguranca.permissao_perfil WHERE 
id_perfil = (SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'DIRETOR' AND
id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')) AND 
id_permissao = (SELECT p.id FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_BAIXAR_DOCUMENTO_RELATORIO_TECNICO_VISTORIA');
