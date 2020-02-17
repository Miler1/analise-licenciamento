# --- !Ups

UPDATE tramitacao.condicao SET nm_condicao='Aguardando assinatura do presidente' WHERE id_condicao=11;

INSERT INTO tramitacao.acao(id_acao,tx_descricao,fl_ativo,fl_tramitavel) VALUES
	(76,'Iniciar análise do presidente',1,1);

INSERT INTO tramitacao.condicao(id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES 
	(38, 7, 'Em análise pelo presidente', 1);

INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final) VALUES 
	(76, 11, 38);

INSERT INTO portal_seguranca.permissao (codigo, data_cadastro, nome, id_modulo) VALUES 
	('ANL_INICIAR_PARECER_PRESIDENTE', now(), 'Iniciar parecer do presidente', (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));


INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) VALUES 
	((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Presidente' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')), 
	(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_INICIAR_PARECER_PRESIDENTE' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));


# --- !Downs

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE nome = 'Presidente' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'))
AND id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_INICIAR_PARECER_PRESIDENTE' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));
 
DELETE FROM portal_seguranca.permissao WHERE codigo = 'ANL_INICIAR_PARECER_PRESIDENTE' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL');

DELETE FROM tramitacao.transicao WHERE id_acao = 76 AND id_condicao_inicial= 11 AND id_condicao_final = 38; 

DELETE FROM tramitacao.condicao WHERE id_condicao = 38 AND id_etapa=7 AND nm_condicao = 'Em análise pelo presidente';

DELETE FROM tramitacao.acao WHERE id_acao = 76 AND tx_descricao = 'Iniciar análise do presidente';

UPDATE tramitacao.condicao SET nm_condicao='Aguardando assinatura aprovador' WHERE id_condicao=11;
