# --- !Ups
-- Perfil de coordenador geo
INSERT INTO portal_seguranca.perfil (data_cadastro, nome, id_modulo_pertencente, codigo, ativo) VALUES 
(now(), 'Coordenador GEO', (SELECT id FROM portal_seguranca.modulo WHERE sigla='MAL'), 'COORDENADOR_GEO', true);

-- Permissões do coordenador geo
INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) VALUES 
((SELECT id FROM portal_seguranca.perfil WHERE codigo = 'COORDENADOR_GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')),
(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_LISTAR_PROCESSO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) VALUES
((SELECT id FROM portal_seguranca.perfil WHERE codigo = 'COORDENADOR_GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')),
(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_VINCULAR_PROCESSO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) VALUES
((SELECT id FROM portal_seguranca.perfil WHERE codigo = 'COORDENADOR_GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')),
(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_DESVINCULAR_PROCESSO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) VALUES
((SELECT id FROM portal_seguranca.perfil WHERE codigo = 'COORDENADOR_GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')),
(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_CONSULTAR_PROCESSO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) VALUES
((SELECT id FROM portal_seguranca.perfil WHERE codigo = 'COORDENADOR_GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')),
(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_VALIDAR_PARECER_GEO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) VALUES
((SELECT id FROM portal_seguranca.perfil WHERE codigo = 'COORDENADOR_GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')),
(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_VISUALIZAR_NOTIFICACAO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) VALUES
((SELECT id FROM portal_seguranca.perfil WHERE codigo = 'COORDENADOR_GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')),
(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_VISUALIZAR_PROCESSO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) VALUES
((SELECT id FROM portal_seguranca.perfil WHERE codigo = 'COORDENADOR_GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')),
(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'GER_RESPONDER_SOLICITACAO_DESVINCULO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) VALUES
((SELECT id FROM portal_seguranca.perfil WHERE codigo = 'COORDENADOR_GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')),
(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_BUSCAR_INCONSISTENCIA_GEO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) VALUES
((SELECT id FROM portal_seguranca.perfil WHERE codigo = 'COORDENADOR_GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')),
(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_BAIXAR_DOCUMENTO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) VALUES
((SELECT id FROM portal_seguranca.perfil WHERE codigo = 'COORDENADOR_GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')),
(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'VISUALIZAR_PROTOCOLO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) VALUES
((SELECT id FROM portal_seguranca.perfil WHERE codigo = 'COORDENADOR_GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')),
(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_CONSULTAR_LICENCAS_EMITIDAS' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')));

--Novo setor Coordenadoria de Licenciamento Ambiental(CLA)
INSERT INTO portal_seguranca.setor (nome, sigla, tipo_setor, data_cadastro, removido, ativo) VALUES ('Coordenadoria de Licenciamento Ambiental', 'CLA', 3,now(), false, true);

--Relações perfil_setor para a Coordenadoria de Licenciamento Ambiental(CLA)
INSERT INTO portal_seguranca.perfil_setor (id_perfil, id_setor) VALUES 
((SELECT id FROM portal_seguranca.perfil WHERE codigo='COORDENADOR_GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')), (SELECT id FROM portal_seguranca.setor WHERE sigla='CLA'));

INSERT INTO portal_seguranca.perfil_setor (id_perfil, id_setor) VALUES 
((SELECT id FROM portal_seguranca.perfil WHERE codigo='ANALISTA_GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')), (SELECT id FROM portal_seguranca.setor WHERE sigla='CLA'));

INSERT INTO portal_seguranca.perfil_setor (id_perfil, id_setor) VALUES 
((SELECT id FROM portal_seguranca.perfil WHERE codigo='ANALISTA_TECNICO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')), (SELECT id FROM portal_seguranca.setor WHERE sigla='CLA'));

INSERT INTO portal_seguranca.perfil_setor (id_perfil, id_setor) VALUES 
((SELECT id FROM portal_seguranca.perfil WHERE codigo='COORDENADOR' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')), (SELECT id FROM portal_seguranca.setor WHERE sigla='CLA'));	

--Desativar coordenadorias 
UPDATE portal_seguranca.setor SET ativo = false WHERE  portal_seguranca.setor.sigla NOT LIKE 'CLA' AND portal_seguranca.setor.sigla NOT LIKE 'DTI' AND portal_seguranca.setor.sigla NOT LIKE 'GAB';


# --- !Downs
--Desativar coordenadorias 
UPDATE portal_seguranca.setor SET ativo = true WHERE  portal_seguranca.setor.sigla NOT LIKE 'CLA' AND portal_seguranca.setor.sigla NOT LIKE 'DTI' AND portal_seguranca.setor.sigla NOT LIKE 'GAB';

--Relações perfil_setor para a Coordenadoria de Licenciamento Ambiental(CLA)
DELETE FROM portal_seguranca.perfil_setor WHERE 
id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE codigo='COORDENADOR_GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor WHERE sigla='CLA');

DELETE FROM portal_seguranca.perfil_setor WHERE 
id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE codigo='ANALISTA_GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) 
AND id_setor = (SELECT id FROM portal_seguranca.setor WHERE sigla='CLA');

DELETE FROM portal_seguranca.perfil_setor WHERE 
id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE codigo='ANALISTA_TECNICO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) 
AND id_setor = (SELECT id FROM portal_seguranca.setor WHERE sigla='CLA');

DELETE FROM portal_seguranca.perfil_setor WHERE 
id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE codigo='COORDENADOR' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'))
AND id_setor = (SELECT id FROM portal_seguranca.setor WHERE sigla='CLA');	

--Novo setor Coordenadoria de Licenciamento Ambiental(CLA)
DELETE FROM portal_seguranca.setor WHERE nome = 'Coordenadoria de Licenciamento Ambiental';

-- Permissões do coordenador geo
DELETE FROM portal_seguranca.permissao_perfil WHERE 
id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE codigo = 'COORDENADOR_GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) AND 
id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_CONSULTAR_LICENCAS_EMITIDAS' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

DELETE FROM portal_seguranca.permissao_perfil WHERE 
id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE codigo = 'COORDENADOR_GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) AND 
id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_LISTAR_PROCESSO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

DELETE FROM portal_seguranca.permissao_perfil WHERE
id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE codigo = 'COORDENADOR_GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) AND 
id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_VINCULAR_PROCESSO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

DELETE FROM portal_seguranca.permissao_perfil WHERE
id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE codigo = 'COORDENADOR_GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) AND 
id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_DESVINCULAR_PROCESSO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

DELETE FROM portal_seguranca.permissao_perfil WHERE
id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE codigo = 'COORDENADOR_GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) AND 
id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_CONSULTAR_PROCESSO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

DELETE FROM portal_seguranca.permissao_perfil WHERE
id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE codigo = 'COORDENADOR_GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) AND 
id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_VALIDAR_PARECER_GEO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

DELETE FROM portal_seguranca.permissao_perfil WHERE
id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE codigo = 'COORDENADOR_GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) AND 
id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_VISUALIZAR_NOTIFICACAO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

DELETE FROM portal_seguranca.permissao_perfil WHERE
id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE codigo = 'COORDENADOR_GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) AND 
id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_VISUALIZAR_PROCESSO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

DELETE FROM portal_seguranca.permissao_perfil WHERE
id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE codigo = 'COORDENADOR_GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) AND 
id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'GER_RESPONDER_SOLICITACAO_DESVINCULO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

DELETE FROM portal_seguranca.permissao_perfil WHERE
id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE codigo = 'COORDENADOR_GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) AND 
id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_BUSCAR_INCONSISTENCIA_GEO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

DELETE FROM portal_seguranca.permissao_perfil WHERE
id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE codigo = 'COORDENADOR_GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) AND 
id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'ANL_BAIXAR_DOCUMENTO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

DELETE FROM portal_seguranca.permissao_perfil WHERE 
id_perfil = (SELECT id FROM portal_seguranca.perfil WHERE codigo = 'COORDENADOR_GEO' and id_modulo_pertencente = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL')) AND
id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'VISUALIZAR_PROTOCOLO' AND id_modulo = (SELECT id FROM portal_seguranca.modulo WHERE sigla = 'MAL'));

-- Perfil de coordenador geo
DELETE FROM portal_seguranca.perfil WHERE codigo = 'COORDENADOR_GEO';
