# --- !Ups

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
	VALUES ((SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'PRESIDENTE' AND p.id_modulo_pertencente =(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
		(SELECT p.id  FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_BAIXAR_DOCUMENTO_MINUTA' AND p.id_modulo =(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')));

INSERT INTO tramitacao.condicao(id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES 
	((SELECT max(id_condicao+1) FROM tramitacao.condicao), 7, 'Solicitação da licença aprovada', 1);

INSERT INTO tramitacao.condicao(id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES 
	((SELECT max(id_condicao+1) FROM tramitacao.condicao), 7, 'Solicitação da licença negada', 1);
	
INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES 
	((SELECT max(id_acao+1) FROM tramitacao.acao), 'Aprovar solicitação de licença', 1, 1);

INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES 
	((SELECT max(id_acao) FROM tramitacao.acao), (SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao = 'Em análise pelo presidente'), (SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao = 'Solicitação da licença aprovada'), NULL, NULL);

INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES 
	((SELECT max(id_acao+1) FROM tramitacao.acao), 'Negar solicitação de licença', 1, 1);

INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES 
	((SELECT max(id_acao) FROM tramitacao.acao), (SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao = 'Em análise pelo presidente'), (SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao = 'Solicitação da licença negada'), NULL, NULL);


# --- !Downs

DELETE FROM tramitacao.transicao WHERE 
id_condicao_inicial = (SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao = 'Em análise pelo presidente') AND 
id_condicao_final = (SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao = 'Solicitação da licença negada');

DELETE FROM tramitacao.acao WHERE tx_descricao = 'Negar solicitação de licença';

DELETE FROM tramitacao.transicao WHERE 
id_condicao_inicial = (SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao = 'Em análise pelo presidente') AND 
id_condicao_final = (SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao = 'Solicitação da licença aprovada');

DELETE FROM tramitacao.condicao WHERE nm_condicao= 'Solicitação da licença negada';
DELETE FROM tramitacao.condicao WHERE nm_condicao= 'Solicitação da licença aprovada';

DELETE FROM tramitacao.acao WHERE tx_descricao = 'Aprovar solicitação de licença';

DELETE FROM portal_seguranca.permissao_perfil WHERE 
id_perfil = (SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'PRESIDENTE' AND p.id_modulo_pertencente =(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')
AND id_permissao = (SELECT p.id  FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_BAIXAR_DOCUMENTO_MINUTA' AND p.id_modulo =(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')));
