# --- !Ups

INSERT INTO portal_seguranca.permissao (codigo, data_cadastro, nome, id_modulo) 
	VALUES ('ANL_SOLICITAR_DESVINCULO_GEO', now(), 'Solicitar desvínculo análise GEO',(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) 
	VALUES ((SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_GEO' AND p.id_modulo_pertencente =(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),(SELECT p.id FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_SOLICITAR_DESVINCULO_GEO'));

INSERT INTO portal_seguranca.permissao (codigo, data_cadastro, nome, id_modulo) 
	VALUES ('ANL_SOLICITAR_DESVINCULO_TECNICO', now(), 'Solicitar desvínculo análise Técnica',(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) 
	VALUES ((SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' AND p.id_modulo_pertencente =(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),(SELECT p.id FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_SOLICITAR_DESVINCULO_TECNICO'));

INSERT INTO portal_seguranca.permissao (codigo, data_cadastro, nome, id_modulo) 
	VALUES ('GER_RESPONDER_SOLICITACAO_DESVINCULO', now(), 'Responder solicitação de desvínculo',(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'));                                                     

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) 
	VALUES ((SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'GERENTE' AND p.id_modulo_pertencente =(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),(SELECT p.id FROM portal_seguranca.permissao p WHERE p.codigo = 'GER_RESPONDER_SOLICITACAO_DESVINCULO'));


# --- !Downs

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil =(SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'GERENTE' AND p.id_modulo_pertencente =(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')) AND id_permissao=(SELECT p.id FROM portal_seguranca.permissao p WHERE p.codigo = 'GER_RESPONDER_SOLICITACAO_DESVINCULO');

DELETE FROM portal_seguranca.permissao WHERE codigo='GER_RESPONDER_SOLICITACAO_DESVINCULO' AND nome='Responder solicitação de desvínculo' ;

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil =(SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' AND p.id_modulo_pertencente =(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')) AND id_permissao=(SELECT p.id FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_SOLICITAR_DESVINCULO_TECNICO');

DELETE FROM portal_seguranca.permissao WHERE codigo='ANL_SOLICITAR_DESVINCULO_TECNICO' AND nome='Solicitar desvínculo análise Técnica';

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil =(SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_GEO' AND p.id_modulo_pertencente =(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')) AND id_permissao=(SELECT p.id FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_SOLICITAR_DESVINCULO_GEO');

DELETE FROM portal_seguranca.permissao WHERE codigo='ANL_SOLICITAR_DESVINCULO_GEO' AND nome='Solicitar desvínculo análise GEO';

