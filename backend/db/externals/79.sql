# --- !Ups

INSERT INTO portal_seguranca.permissao (codigo, data_cadastro, nome, id_modulo) 
	VALUES ('ANL_BAIXAR_DOCUMENTO_RELATORIO_TECNICO_VISTORIA', now(), 'Baixar Documento Relatório Técnico Vistoria',(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
	VALUES ((SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' AND p.id_modulo_pertencente =(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
		(SELECT p.id FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_BAIXAR_DOCUMENTO_RELATORIO_TECNICO_VISTORIA'));

# --- !Downs

DELETE FROM portal_seguranca.permissao_perfil WHERE 
id_perfil = (SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' AND
id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')) AND 
id_permissao = (SELECT p.id FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_BAIXAR_DOCUMENTO_RELATORIO_TECNICO_VISTORIA');

DELETE FROM portal_seguranca.permissao WHERE codigo = 'ANL_BAIXAR_DOCUMENTO_RELATORIO_TECNICO_VISTORIA' AND nome = 'Baixar Documento Relatório Técnico Vistoria' AND id_modulo =  (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'); 



