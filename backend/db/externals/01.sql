# --- !Ups
--1
INSERT INTO portal_seguranca.modulo(id, data_cadastro, descricao, nome, sigla, ips, ativo, url, id_documento_logotipo) VALUES 
	(0, now(), 'Análise das solicitações de licença', 'Análise', 'ANL', '127.0.0.1,0:0:0:0:0:0:0:1', true, 'http://localhost:9011/authenticate',3);
--
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro, removido, ativo) VALUES ('Secretaria Adjunta de Gestão e Regularidade Ambiental', 'SAGRA', NULL, 0,'27-03-2020', false, false);
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro, removido, ativo) VALUES ('Diretoria de Gestão Florestal e Agrossilvipastoril', 'DGFLOR', NULL, 1,'27-03-2020', false, false);
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro, removido, ativo) VALUES ('Diretoria de Licenciamento Ambiental', 'DLA', NULL, 1,'27-03-2020', false, false);
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro, removido, ativo) VALUES ('Coordenadoria  de Gestão Agropastoril e Industrial', 'COGAPI', (SELECT id FROM portal_seguranca.setor WHERE sigla='DGFLOR'), 2,'27-03-2020', false, false);
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro, removido, ativo) VALUES ('Coordenadoria de Gestão Florestal', 'COGEF', (SELECT id FROM portal_seguranca.setor WHERE sigla='DGFLOR'), 2,'27-03-2020', false, false);
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro, removido, ativo) VALUES ('Coordenadoria de Indústria Comércio Serviços e Resíduos', 'CIND', (SELECT id FROM portal_seguranca.setor WHERE sigla='DLA'), 2,'27-03-2020', false, false);
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro, removido, ativo) VALUES ('Coordenadoria de Infraestrutura, Fauna, Aquicultura e Pesca', 'CINFAP', (SELECT id FROM portal_seguranca.setor WHERE sigla='DLA'), 2,'27-03-2020', false, false);
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro, removido, ativo) VALUES ('Coordenadoria de Mineração', 'CMINA', (SELECT id FROM portal_seguranca.setor WHERE sigla='DLA'), 2,'27-03-2020', false, false);
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro, removido, ativo) VALUES ('Gerência de Atividades Agropecuárias', 'GEAGRO', (SELECT id FROM portal_seguranca.setor WHERE sigla='COGAPI'), 3,'27-03-2020', false, false);
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro, removido, ativo) VALUES ('Gerência de Cadastro, Transporte e Comercialização de Produtos e Subprodutos Florestais', 'GESFLORA', (SELECT id FROM portal_seguranca.setor WHERE sigla='COGEF'), 3,'27-03-2020', false, false);
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro, removido, ativo) VALUES ('Gerência de Fauna, Flora, Aquicultura e Pesca', 'GEFAP', (SELECT id FROM portal_seguranca.setor WHERE sigla='CINFAP'), 3,'27-03-2020', false, false);
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro, removido, ativo) VALUES ('Gerência de Infraestrutura de Transporte e Obras Civis', 'GEINFRA', (SELECT id FROM portal_seguranca.setor WHERE sigla='CINFAP'), 3,'27-03-2020', false, false);
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro, removido, ativo) VALUES ('Gerência de Projetos de Comércio e Serviços', 'GECOS', (SELECT id FROM portal_seguranca.setor WHERE sigla='CIND'), 3,'27-03-2020', false, false);
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro, removido, ativo) VALUES ('Gerência de Projetos de Processamento de Produtos e Subprodutos Florestais', 'GEPROF', (SELECT id FROM portal_seguranca.setor WHERE sigla='COGAPI'), 3,'27-03-2020', false, false);
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro, removido, ativo) VALUES ('Gerência de Projetos Industriais', 'GEIND', (SELECT id FROM portal_seguranca.setor WHERE sigla='CIND'), 3,'27-03-2020', false, false);
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro, removido, ativo) VALUES ('Gerência de Projetos Minerários Metálicos', 'GEMIM', (SELECT id FROM portal_seguranca.setor WHERE sigla='CMINA'), 3,'27-03-2020', false, false);
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro, removido, ativo) VALUES ('Gerência de Projetos Minerários Não Metálicos', 'GEMINA', (SELECT id FROM portal_seguranca.setor WHERE sigla='CMINA'), 3,'27-03-2020', false, false);
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro, removido, ativo) VALUES ('Gerência de Projetos Silvipastoris', 'GEPAF', (SELECT id FROM portal_seguranca.setor WHERE sigla='COGEF'), 3,'27-03-2020', false, false);
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro, removido, ativo) VALUES ('Gerência de Infraestrutura de Energia, Parcelamento do Solo e Saneamento', 'GEPAS', (SELECT id FROM portal_seguranca.setor WHERE sigla='CINFAP'), 3,'27-03-2020', false, false);

--54
-- Inserir permissão de visualizar notificações
INSERT INTO portal_seguranca.permissao (codigo, data_cadastro, nome, id_modulo) 
VALUES ('ANL_VISUALIZAR_NOTIFICACAO', now(), 'Visualizar Notificação', (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));


-- Inserir permissão para o perfil Analista GEO
INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) 
VALUES ((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')), 
(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_VISUALIZAR_NOTIFICACAO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));


--58
INSERT INTO portal_seguranca.permissao (codigo, data_cadastro, nome, id_modulo) VALUES 
('ANL_VISUALIZAR_PROCESSO', now(), 'Visualizar processo', (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'));

INSERT INTO portal_seguranca.permissao_perfil VALUES ((SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_GEO' AND p.id_modulo_pertencente =
(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),(SELECT p.id FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_VISUALIZAR_PROCESSO'));

INSERT INTO portal_seguranca.permissao_perfil VALUES ((SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'GERENTE' AND p.id_modulo_pertencente =
(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),(SELECT p.id FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_VISUALIZAR_PROCESSO'));

INSERT INTO portal_seguranca.permissao_perfil VALUES ((SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' AND p.id_modulo_pertencente =
(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),(SELECT p.id FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_VISUALIZAR_PROCESSO'));

--60
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

--65
INSERT INTO portal_seguranca.permissao (codigo, data_cadastro, nome, id_modulo) 
	VALUES ('ANL_SALVAR_INCONSISTENCIA_TECNICA', now(), 'Salvar inconsistência técnica', (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

INSERT INTO portal_seguranca.permissao (codigo, data_cadastro, nome, id_modulo) 
	VALUES ('ANL_SALVAR_INCONSISTENCIA_GEO', now(), 'Salvar inconsistência GEO', (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

INSERT INTO portal_seguranca.permissao (codigo, data_cadastro, nome, id_modulo) 
	VALUES ('ANL_EXCLUIR_INCONSISTENCIA_TECNICA', now(), 'Excluir inconsistência técnica', (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

INSERT INTO portal_seguranca.permissao (codigo, data_cadastro, nome, id_modulo) 
	VALUES ('ANL_EXCLUIR_INCONSISTENCIA_GEO', now(), 'Salvar inconsistência GEO', (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));


INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) 
	VALUES ((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista TÉCNICO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')), 
	(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_SALVAR_INCONSISTENCIA_TECNICA' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));


INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) 
	VALUES ((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')), 
	(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_SALVAR_INCONSISTENCIA_GEO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));


INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) 
	VALUES ((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista TÉCNICO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')), 
	(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_EXCLUIR_INCONSISTENCIA_TECNICA' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));


INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) 
	VALUES ((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')), 
	(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_EXCLUIR_INCONSISTENCIA_GEO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));

--66
INSERT INTO portal_seguranca.permissao (codigo, data_cadastro, nome, id_modulo) 
	VALUES ('ANL_BUSCAR_INCONSISTENCIA_GEO', now(), 'Buscar inconsistência GEO', (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

INSERT INTO portal_seguranca.permissao (codigo, data_cadastro, nome, id_modulo) 
	VALUES ('ANL_BUSCAR_INCONSISTENCIA_TECNICA', now(), 'Buscar inconsistência técnica', (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) 
	VALUES ((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')), 
	(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_BUSCAR_INCONSISTENCIA_GEO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) 
	VALUES ((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Gerente' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')), 
	(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_BUSCAR_INCONSISTENCIA_GEO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) 
	VALUES ((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista TÉCNICO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')), 
	(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_BUSCAR_INCONSISTENCIA_TECNICA' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) 
	VALUES ((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Gerente' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')), 
	(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_BUSCAR_INCONSISTENCIA_TECNICA' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));

--67
INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
	VALUES ((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Gerente' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')), (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_VISUALIZAR_NOTIFICACAO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));


--68
INSERT INTO portal_seguranca.permissao (codigo, data_cadastro, nome, id_modulo) 
	VALUES ('ANL_VISUALIZAR_QUESTIONARIO', now(), 'Visualizar Questionário', (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

INSERT INTO portal_seguranca.permissao (codigo, data_cadastro, nome, id_modulo) 
	VALUES ('ANL_BAIXAR_DOCUMENTO', now(), 'Baixar Documentos', (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) 
	VALUES ((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista TÉCNICO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')), 
	(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_VISUALIZAR_QUESTIONARIO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) 
	VALUES ((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista TÉCNICO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')), 
	(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_BAIXAR_DOCUMENTO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) 
	VALUES ((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')), 
	(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_BAIXAR_DOCUMENTO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) 
	VALUES ((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Gerente' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')), 
	(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_BAIXAR_DOCUMENTO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) 
	VALUES ((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Presidente' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')), 
	(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_BAIXAR_DOCUMENTO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) 
	VALUES ((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista CAR' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')), 
	(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_BAIXAR_DOCUMENTO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) 
	VALUES ((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Diretor' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')), 
	(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_BAIXAR_DOCUMENTO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));




--70
INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES 
	((SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Gerente' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GCAP'));

INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES 
	((SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Gerente' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GFAU'));

INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES 
	((SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Gerente' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GERM'));

INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES 
	((SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Gerente' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	 (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GECF'));

INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES 
	((SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Gerente' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GELI'));

INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES 
	((SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Gerente' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	 (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GECP'));

INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES 
	((SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Gerente' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GERH'));

--76
INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) VALUES 
    ((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista TÉCNICO' AND id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')), 
     (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_VISUALIZAR_NOTIFICACAO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));


--79
INSERT INTO portal_seguranca.permissao (codigo, data_cadastro, nome, id_modulo) 
	VALUES ('ANL_BAIXAR_DOCUMENTO_RELATORIO_TECNICO_VISTORIA', now(), 'Baixar Documento Relatório Técnico Vistoria',(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
	VALUES ((SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' AND p.id_modulo_pertencente =(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
		(SELECT p.id FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_BAIXAR_DOCUMENTO_RELATORIO_TECNICO_VISTORIA'));

--80
INSERT INTO portal_seguranca.permissao (codigo, data_cadastro, nome, id_modulo)	VALUES 
	('VISUALIZAR_PROTOCOLO', now(), 'Visualizar protocolo', (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));
	

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) VALUES 
	((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista TÉCNICO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'VISUALIZAR_PROTOCOLO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) VALUES 
	((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'VISUALIZAR_PROTOCOLO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) VALUES 
	((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Gerente' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'VISUALIZAR_PROTOCOLO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));



--82
INSERT INTO portal_seguranca.permissao (codigo, data_cadastro, nome, id_modulo) 
	VALUES ('ANL_BAIXAR_DOCUMENTO_MINUTA', now(), 'Baixar Documento Minuta',(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
	VALUES ((SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' AND p.id_modulo_pertencente =(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
		(SELECT p.id FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_BAIXAR_DOCUMENTO_MINUTA'));

--84
INSERT INTO portal_seguranca.setor(data_cadastro, nome, sigla, tipo_setor, removido, ativo) VALUES 
	('2020-01-06', 'Diretoria', 'DTI', 3, false, true);

INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES 
	((SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Diretor' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'DTI'));

INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES 
	((SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Presidente' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'DTI'));


--86
INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) VALUES 
 ((SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'GERENTE' AND p.id_modulo_pertencente =(SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')), 
 (SELECT p.id FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_BAIXAR_DOCUMENTO_RELATORIO_TECNICO_VISTORIA' AND p.id_modulo =(SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) VALUES 
((SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'GERENTE' AND p.id_modulo_pertencente =(SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')), 
(SELECT p.id FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_BAIXAR_DOCUMENTO_MINUTA' AND p.id_modulo =(SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));

--87
INSERT INTO portal_seguranca.permissao (codigo, data_cadastro, nome, id_modulo) 
	VALUES ('ANL_INICIAR_PARECER_DIRETOR', now(), 'Iniciar parecer pelo diretor', (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) 
	VALUES ((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Diretor' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')), 
	(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_INICIAR_PARECER_DIRETOR' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));

--88
INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) VALUES 
 ((SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'DIRETOR' AND p.id_modulo_pertencente =(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
  (SELECT p.id  FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_BAIXAR_DOCUMENTO_RELATORIO_TECNICO_VISTORIA' AND p.id_modulo =(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) VALUES 
 ((SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'DIRETOR' AND p.id_modulo_pertencente =(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
 (SELECT p.id  FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_BAIXAR_DOCUMENTO_MINUTA' AND p.id_modulo =(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')));


--90
INSERT INTO portal_seguranca.permissao (codigo, data_cadastro, nome, id_modulo) VALUES 
	('ANL_INICIAR_PARECER_PRESIDENTE', now(), 'Iniciar parecer do presidente', (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));


INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) VALUES 
	((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Presidente' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')), 
	(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_INICIAR_PARECER_PRESIDENTE' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));


--91
INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) VALUES 
	((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Diretor' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'VISUALIZAR_PROTOCOLO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));
	
INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) VALUES 
	((SELECT id FROM portal_seguranca.perfil WHERE nome = 'Presidente' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'VISUALIZAR_PROTOCOLO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));

--93
INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
    VALUES ((SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'PRESIDENTE' AND p.id_modulo_pertencente =(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
        (SELECT p.id  FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_BAIXAR_DOCUMENTO_MINUTA' AND p.id_modulo =(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')));


--94
INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES
	((SELECT id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GCAP' AND ativo));

INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES
	((SELECT id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GFAU' AND ativo));

INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES
	((SELECT id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GERM' AND ativo));

INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES
	((SELECT id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	 (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GECF' AND ativo));

INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES
	((SELECT id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GELI' AND ativo));

INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES
	((SELECT id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	 (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GECP' AND ativo));

INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES
	((SELECT id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GERH' AND ativo));

--95
UPDATE portal_seguranca.perfil SET nome = 'Gerente TÉCNICO' WHERE codigo = 'GERENTE' AND id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL');
UPDATE portal_seguranca.perfil SET nome = 'Diretor TÉCNICO' WHERE codigo = 'DIRETOR' AND id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL');
UPDATE portal_seguranca.perfil SET nome = 'Diretor PRESIDENTE' WHERE codigo = 'PRESIDENTE' AND id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL');

--99
DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor PRESIDENTE' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'DTI');

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor TECNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'DTI');

INSERT INTO portal_seguranca.setor (data_cadastro, nome, sigla, tipo_setor, removido, ativo, id_setor_pai) VALUES 
	('2019-12-12', 'Gabinete', 'GAB', 3, false, true, null),
	('2019-12-12', 'Diretoria Técnica', 'DT', 1, false, true, null);

INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES 
	((SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Diretor TÉCNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'DT'));

INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES 
	((SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Diretor PRESIDENTE' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GAB'));

--100
DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor PRESIDENTE' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GELI');

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor PRESIDENTE' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GERAL');

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor PRESIDENTE' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'DTI');

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor PRESIDENTE' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GCAP');

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor PRESIDENTE' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GECF');

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor PRESIDENTE' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GECP');

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor PRESIDENTE' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GFAU');

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor PRESIDENTE' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GGEO');


DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor TÉCNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GELI');

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor TÉCNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GERAL');

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor TÉCNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'DTI');

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor TÉCNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GCAP');

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor TÉCNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GECF');

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor TÉCNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GECP');

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor TÉCNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GFAU');

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE 
p.nome = 'Diretor TÉCNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GGEO');

INSERT INTO portal_seguranca.modulo(alvo, chave, data_cadastro, descricao, logotipo, nome, sigla, ativo, url, fixo) VALUES (0, 'apRWmiizr4Hq6aL672wwjwIjAE9n7Rm56RlkmqkYJdsCDmkLsSWGnGzXnkgQw8L6', now(), 'Análise das solicitações de licença', '3.png', 'Análise', 'ANL', true, 'http://localhost:9011/authenticate', true);

# --- !Downs
--100

--99
INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES 
	((SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Diretor TÉCNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'DTI'));

INSERT INTO portal_seguranca.perfil_setor(id_perfil, id_setor) VALUES 
	((SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Diretor PRESIDENTE' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')),
	(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'DTI'));

--95
UPDATE portal_seguranca.perfil SET nome = 'Gerente' WHERE codigo = 'GERENTE' AND id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL');
UPDATE portal_seguranca.perfil SET nome = 'Diretor' WHERE codigo = 'DIRETOR' AND id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL');
UPDATE portal_seguranca.perfil SET nome = 'Presidente' WHERE codigo = 'PRESIDENTE' AND id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL');

--94
DELETE FROM  portal_seguranca.perfil_setor WHERE id_perfil = 
	(SELECT id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')) 
	AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GCAP' AND ativo);

DELETE FROM  portal_seguranca.perfil_setor WHERE id_perfil = 
	(SELECT id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')) 
	AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GFAU' AND ativo);

DELETE FROM  portal_seguranca.perfil_setor WHERE id_perfil = 
	(SELECT id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')) 
	AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GERM' AND ativo);

DELETE FROM  portal_seguranca.perfil_setor WHERE id_perfil = 
	(SELECT id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')) 
	AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GECF' AND ativo);

DELETE FROM  portal_seguranca.perfil_setor WHERE id_perfil = 
	(SELECT id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')) 
	AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GELI' AND ativo);

DELETE FROM  portal_seguranca.perfil_setor WHERE id_perfil = 
	(SELECT id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')) 
	AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GECP' AND ativo);

DELETE FROM  portal_seguranca.perfil_setor WHERE id_perfil = 
	(SELECT id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')) 
	AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GERH' AND ativo);

--93
DELETE FROM portal_seguranca.permissao_perfil WHERE 
id_perfil = (SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'PRESIDENTE' AND p.id_modulo_pertencente =(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')
AND id_permissao = (SELECT p.id  FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_BAIXAR_DOCUMENTO_MINUTA' AND p.id_modulo =(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')));

--91
DELETE FROM portal_seguranca.permissao_perfil WHERE 
id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE nome = 'Diretor' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) 
AND id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'VISUALIZAR_PROTOCOLO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));


DELETE FROM portal_seguranca.permissao_perfil WHERE 
id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE nome = 'Presidente' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'))
AND id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'VISUALIZAR_PROTOCOLO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

--90
DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE nome = 'Presidente' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'))
AND id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_INICIAR_PARECER_PRESIDENTE' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));
 
DELETE FROM portal_seguranca.permissao WHERE codigo = 'ANL_INICIAR_PARECER_PRESIDENTE' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL');

--88
DELETE FROM portal_seguranca.permissao_perfil WHERE 
id_perfil = (SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'DIRETOR' AND
id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')) AND 
id_permissao = (SELECT p.id FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_BAIXAR_DOCUMENTO_MINUTA');


DELETE FROM portal_seguranca.permissao_perfil WHERE 
id_perfil = (SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'DIRETOR' AND
id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')) AND 
id_permissao = (SELECT p.id FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_BAIXAR_DOCUMENTO_RELATORIO_TECNICO_VISTORIA');

--87

DELETE FROM portal_seguranca.permissao_perfil WHERE 
id_perfil = (SELECT p.id FROM portal_seguranca.perfil p WHERE p.nome = 'Diretor' AND 
id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')) AND 
id_permissao = (SELECT p.id FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_INICIAR_PARECER_DIRETOR');

DELETE FROM portal_seguranca.permissao WHERE codigo = 'ANL_INICIAR_PARECER_DIRETOR';--86
DELETE FROM portal_seguranca.permissao_perfil WHERE 
id_perfil = (SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'GERENTE' AND
id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')) AND 
id_permissao = (SELECT p.id FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_BAIXAR_DOCUMENTO_MINUTA');


DELETE FROM portal_seguranca.permissao_perfil WHERE 
id_perfil = (SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'GERENTE' AND
id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')) AND 
id_permissao = (SELECT p.id FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_BAIXAR_DOCUMENTO_RELATORIO_TECNICO_VISTORIA');
--84
DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Presidente' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'DTI');

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Diretor' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'DTI');

DELETE FROM portal_seguranca.setor WHERE nome = 'Diretoria' and sigla = 'DTI';--82
DELETE FROM portal_seguranca.permissao_perfil WHERE 
id_perfil = (SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' AND
id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')) AND 
id_permissao = (SELECT p.id FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_BAIXAR_DOCUMENTO_MINUTA');

DELETE FROM portal_seguranca.permissao WHERE codigo = 'ANL_BAIXAR_DOCUMENTO_MINUTA' AND nome = 'Baixar Documento Minuta' AND id_modulo =  (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'); 

--80
DELETE FROM portal_seguranca.permissao_perfil WHERE 
id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE nome = 'Gerente' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) AND 
id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'VISUALIZAR_PROTOCOLO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

DELETE FROM portal_seguranca.permissao_perfil WHERE 
id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) AND 
id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'VISUALIZAR_PROTOCOLO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

DELETE FROM portal_seguranca.permissao_perfil WHERE 
id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista TÉCNICO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) AND 
id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'VISUALIZAR_PROTOCOLO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));


DELETE FROM portal_seguranca.permissao WHERE codigo = 'VISUALIZAR_PROTOCOLO';

--79
DELETE FROM portal_seguranca.permissao_perfil WHERE 
id_perfil = (SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' AND
id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')) AND 
id_permissao = (SELECT p.id FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_BAIXAR_DOCUMENTO_RELATORIO_TECNICO_VISTORIA');

DELETE FROM portal_seguranca.permissao WHERE codigo = 'ANL_BAIXAR_DOCUMENTO_RELATORIO_TECNICO_VISTORIA' AND nome = 'Baixar Documento Relatório Técnico Vistoria' AND id_modulo =  (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'); 

--76
DELETE FROM portal_seguranca.permissao_perfil WHERE 
id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista TÉCNICO' AND 
id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) AND 
id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_VISUALIZAR_NOTIFICACAO' AND 
id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));


--70
DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Gerente' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
 AND id_setor =(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GERH');

DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Gerente' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
 AND id_setor =(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GECP');

 DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Gerente' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
 AND id_setor =(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GELI');

  DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Gerente' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
 AND id_setor =(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GECF');

   DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Gerente' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
 AND id_setor =(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GERM');

 DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Gerente' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
 AND id_setor =(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GFAU');

  DELETE FROM portal_seguranca.perfil_setor WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil p WHERE p.nome = 'Gerente' and p.id_modulo_pertencente = (SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL'))
 AND id_setor =(SELECT id FROM portal_seguranca.setor s WHERE s.sigla = 'GCAP');

--68
DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil WHERE nome = 'Diretor' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) AND id_permissao =( SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_BAIXAR_DOCUMENTO');

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista CAR' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) AND id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_BAIXAR_DOCUMENTO' );

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE nome = 'Presidente' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) AND id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_BAIXAR_DOCUMENTO' );

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE nome = 'Gerente' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) AND id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_BAIXAR_DOCUMENTO' );

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) AND id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_BAIXAR_DOCUMENTO' );

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista TÉCNICO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) AND id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_BAIXAR_DOCUMENTO' );

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista TÉCNICO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) AND id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_VISUALIZAR_QUESTIONARIO' );


DELETE FROM portal_seguranca.permissao WHERE codigo='ANL_BAIXAR_DOCUMENTO' AND nome='Baixar Documentos' ;

DELETE FROM portal_seguranca.permissao WHERE codigo='ANL_VISUALIZAR_QUESTIONARIO' AND nome='Visualizar Questionário' ;

--67
DELETE FROM portal_seguranca.permissao_perfil 
WHERE id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE nome = 'Gerente' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) 
AND id_permissao in (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_VISUALIZAR_NOTIFICACAO');

--66
DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil WHERE nome = 'Gerente' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) 
AND id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_BUSCAR_INCONSISTENCIA_TECNICA' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista TÉCNICO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'))
AND id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_BUSCAR_INCONSISTENCIA_TECNICA' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE nome = 'Gerente' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'))
AND id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_BUSCAR_INCONSISTENCIA_GEO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'))
AND id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_BUSCAR_INCONSISTENCIA_GEO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

DELETE FROM portal_seguranca.permissao WHERE codigo = 'ANL_BUSCAR_INCONSISTENCIA_TECNICA';
DELETE FROM portal_seguranca.permissao WHERE codigo = 'ANL_BUSCAR_INCONSISTENCIA_GEO';


--65
DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil =(SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) 
AND id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_EXCLUIR_INCONSISTENCIA_GEO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista TÉCNICO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'))
AND id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_EXCLUIR_INCONSISTENCIA_TECNICA' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'))
AND id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_SALVAR_INCONSISTENCIA_GEO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista TÉCNICO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'))
AND id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_SALVAR_INCONSISTENCIA_TECNICA' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));


DELETE FROM portal_seguranca.permissao WHERE codigo = 'ANL_EXCLUIR_INCONSISTENCIA_GEO';
DELETE FROM portal_seguranca.permissao WHERE codigo = 'ANL_EXCLUIR_INCONSISTENCIA_TECNICA';
DELETE FROM portal_seguranca.permissao WHERE codigo = 'ANL_SALVAR_INCONSISTENCIA_GEO';
DELETE FROM portal_seguranca.permissao WHERE codigo = 'ANL_SALVAR_INCONSISTENCIA_TECNICA';


--60
DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil =(SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'GERENTE' AND p.id_modulo_pertencente =(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')) AND id_permissao=(SELECT p.id FROM portal_seguranca.permissao p WHERE p.codigo = 'GER_RESPONDER_SOLICITACAO_DESVINCULO');

DELETE FROM portal_seguranca.permissao WHERE codigo='GER_RESPONDER_SOLICITACAO_DESVINCULO' AND nome='Responder solicitação de desvínculo' ;

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil =(SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' AND p.id_modulo_pertencente =(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')) AND id_permissao=(SELECT p.id FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_SOLICITAR_DESVINCULO_TECNICO');

DELETE FROM portal_seguranca.permissao WHERE codigo='ANL_SOLICITAR_DESVINCULO_TECNICO' AND nome='Solicitar desvínculo análise Técnica';

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil =(SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_GEO' AND p.id_modulo_pertencente =(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')) AND id_permissao=(SELECT p.id FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_SOLICITAR_DESVINCULO_GEO');

DELETE FROM portal_seguranca.permissao WHERE codigo='ANL_SOLICITAR_DESVINCULO_GEO' AND nome='Solicitar desvínculo análise GEO';


--58

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil =(SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_TECNICO' AND p.id_modulo_pertencente =(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')) AND id_permissao=(SELECT p.id FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_VISUALIZAR_PROCESSO');

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil =(SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'GERENTE' AND p.id_modulo_pertencente =(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')) AND id_permissao=(SELECT p.id FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_VISUALIZAR_PROCESSO');

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil =(SELECT p.id FROM portal_seguranca.perfil p WHERE p.codigo = 'ANALISTA_GEO' AND p.id_modulo_pertencente =(SELECT m.id FROM portal_seguranca.modulo m WHERE m.sigla = 'MAL')) AND id_permissao=(SELECT p.id FROM portal_seguranca.permissao p WHERE p.codigo = 'ANL_VISUALIZAR_PROCESSO');

DELETE FROM portal_seguranca.permissao WHERE codigo='ANL_VISUALIZAR_PROCESSO' AND nome='Visualizar';

--54
-- Deleta permissão para o perfil Analista GEO
DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE nome = 'Analista GEO'and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

-- Deleta permissão de visualizar notificações
DELETE FROM portal_seguranca.permissao WHERE codigo='ANL_VISUALIZAR_NOTIFICACAO';


DELETE FROM portal_seguranca.modulo WHERE nome ='Análise' and sigla =  'ANL' and chave = 'apRWmiizr4Hq6aL672wwjwIjAE9n7Rm56RlkmqkYJdsCDmkLsSWGnGzXnkgQw8L6';
