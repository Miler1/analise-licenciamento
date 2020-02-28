# --- !Ups

INSERT INTO portal_seguranca.permissao (codigo, data_cadastro, nome, id_modulo) 
	VALUES ('ANL_INICIAR_PARECER_DIRETOR', now(), 'Iniciar parecer pelo diretor', (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) 
	VALUES ((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Diretor' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')), 
	(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_INICIAR_PARECER_DIRETOR' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));


INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES 
	(72, 'Iniciar análise pelo diretor', 1, 1);

INSERT INTO tramitacao.condicao(id_condicao,id_etapa,nm_condicao,fl_ativo) VALUES
(37,2,'Em análise pelo diretor',1);

INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final) VALUES 
	(72, 29, 37);


# --- !Downs

DELETE FROM tramitacao.transicao WHERE id_acao = 72 AND id_condicao_inicial = 29 AND id_condicao_final = 37;

DELETE FROM tramitacao.condicao WHERE id_condicao=37 AND id_etapa=2;

DELETE FROM tramitacao.acao WHERE id_acao = 72;

DELETE FROM portal_seguranca.permissao_perfil WHERE 
id_perfil = (SELECT p.id FROM portal_seguranca.perfil p WHERE p.nome = 'Diretor' AND 
id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')) AND 
id_permissao = (SELECT p.id FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_INICIAR_PARECER_DIRETOR');

DELETE FROM portal_seguranca.permissao WHERE codigo = 'ANL_INICIAR_PARECER_DIRETOR';