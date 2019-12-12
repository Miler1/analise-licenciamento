# --- !Ups

INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) 
	SELECT 37, id_condicao, 6, null, null FROM tramitacao.condicao WHERE id_condicao NOT IN (6, 14, 15, 16, 34);

# --- !Downs

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE codigo = 'ANALISTA_TECNICO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) AND id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'SALVAR_INCONSISTENCIA_VISTORIA' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));


DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE codigo = 'ANALISTA_TECNICO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) AND id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'BUSCAR_INCONSISTENCIA_VISTORIA' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));


DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE codigo = 'ANALISTA_TECNICO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) AND id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'DELETAR_INCONSISTENCIA_VISTORIA' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

DELETE FROM portal_seguranca.permissao WHERE codigo='SALVAR_INCONSISTENCIA_VISTORIA';
DELETE FROM portal_seguranca.permissao WHERE codigo='BUSCAR_INCONSISTENCIA_VISTORIA';
DELETE FROM portal_seguranca.permissao WHERE codigo='DELETAR_INCONSISTENCIA_VISTORIA';
