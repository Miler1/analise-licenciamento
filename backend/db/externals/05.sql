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


# --- !Downs

-- Permissões do coordenador geo
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
