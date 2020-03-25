# --- !Ups

INSERT INTO portal_seguranca.permissao (codigo, data_cadastro, nome, id_modulo) VALUES 
('ANL_VISUALIZAR_PROCESSO', now(), 'Visualizar processo', (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'));

INSERT INTO portal_seguranca.permissao_perfil VALUES ((SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_GEO' AND p.id_modulo_pertencente =
(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),(SELECT p.id FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_VISUALIZAR_PROCESSO'));

INSERT INTO portal_seguranca.permissao_perfil VALUES ((SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'GERENTE' AND p.id_modulo_pertencente =
(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),(SELECT p.id FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_VISUALIZAR_PROCESSO'));

INSERT INTO portal_seguranca.permissao_perfil VALUES ((SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' AND p.id_modulo_pertencente =
(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),(SELECT p.id FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_VISUALIZAR_PROCESSO'));


# --- !Downs

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil =(SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' AND p.id_modulo_pertencente =(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')) AND id_permissao=(SELECT p.id FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_VISUALIZAR_PROCESSO');

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil =(SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'GERENTE' AND p.id_modulo_pertencente =(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')) AND id_permissao=(SELECT p.id FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_VISUALIZAR_PROCESSO');

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil =(SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_GEO' AND p.id_modulo_pertencente =(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')) AND id_permissao=(SELECT p.id FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_VISUALIZAR_PROCESSO');

DELETE FROM portal_seguranca.permissao WHERE codigo='ANL_VISUALIZAR_PROCESSO' AND nome='Visualizar';
