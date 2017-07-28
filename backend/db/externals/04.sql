# --- !Ups


-- Grant select do schema licenciamento para a role tramitacao
GRANT USAGE ON SCHEMA licenciamento TO tramitacao;
GRANT SELECT ON ALL TABLES IN SCHEMA licenciamento TO tramitacao;


-- Insert dos novos perfis do sistema
INSERT INTO portal_seguranca.perfil(id, avatar, data_cadastro, nome) VALUES (3, '3.png', now(), 'Coordenador JURÍDICO');
INSERT INTO portal_seguranca.perfil(id, avatar, data_cadastro, nome) VALUES (4, '4.png', now(), 'Administrativo JURÍDICO');
INSERT INTO portal_seguranca.perfil(id, avatar, data_cadastro, nome) VALUES (5, '5.png', now(), 'Consultor JURÍDICO');
INSERT INTO portal_seguranca.perfil(id, avatar, data_cadastro, nome) VALUES (6, '6.png', now(), 'Coordenador TÉCNICO');
INSERT INTO portal_seguranca.perfil(id, avatar, data_cadastro, nome) VALUES (7, '7.png', now(), 'Gerente TÉCNICO');
INSERT INTO portal_seguranca.perfil(id, avatar, data_cadastro, nome) VALUES (8, '8.png', now(), 'Analista TÉCNICO');
INSERT INTO portal_seguranca.perfil(id, avatar, data_cadastro, nome) VALUES (9, '9.png', now(), 'DIRETOR');

-- Insert de duas novas permissões
INSERT INTO portal_seguranca.permissao( codigo, data_cadastro, nome, id_modulo)
VALUES ('LISTAR_PROCESSO_JURIDICO',now(),'Listar Processo',(SELECT id
FROM portal_seguranca.modulo
WHERE sigla = 'ANL'));

INSERT INTO portal_seguranca.permissao( codigo, data_cadastro, nome, id_modulo)
VALUES ('VINCULAR_PROCESSO_JURIDICO',now(),'Vincular Processo',
(SELECT id
FROM portal_seguranca.modulo
WHERE sigla = 'ANL'));


-- Vinculação da permissão de visualizar o modulo de analise para os novos perfis
INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) 
VALUES (3, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'acessarAnaliseLicenciamento'));
INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) 
VALUES (4, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'acessarAnaliseLicenciamento'));
INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) 
VALUES (5, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'acessarAnaliseLicenciamento'));
INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) 
VALUES (6, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'acessarAnaliseLicenciamento'));
INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) 
VALUES (7, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'acessarAnaliseLicenciamento'));
INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) 
VALUES (8, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'acessarAnaliseLicenciamento'));
INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) 
VALUES (9, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'acessarAnaliseLicenciamento'));


-- Vinculação das novas permissões para os perfis competentes
INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES (3, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'LISTAR_PROCESSO_JURIDICO'));
INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES (4, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'LISTAR_PROCESSO_JURIDICO'));
INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES (5, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'LISTAR_PROCESSO_JURIDICO'));
INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES (6, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'LISTAR_PROCESSO_JURIDICO'));
INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES (7, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'LISTAR_PROCESSO_JURIDICO'));
INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES (8, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'LISTAR_PROCESSO_JURIDICO'));
INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES (9, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'LISTAR_PROCESSO_JURIDICO'));


INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES (3, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'VINCULAR_PROCESSO_JURIDICO'));
INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES (4, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'VINCULAR_PROCESSO_JURIDICO'));
INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES (6, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'VINCULAR_PROCESSO_JURIDICO'));
INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao)
VALUES (7, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'VINCULAR_PROCESSO_JURIDICO'));

-- Remoção da permissão de visualizar o modulo de analise para o perfil externo (este só deve ter permissões referentes ao modulo de licenciamento)
DELETE FROM portal_seguranca.permissao_perfil where id_perfil = 2 AND id_permissao = (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'acessarAnaliseLicenciamento');



# --- !Downs


INSERT INTO portal_seguranca.permissao_perfil (id_perfil, id_permissao) VALUES
(2,(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'acessarAnaliseLicenciamento'));
DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil IN (3,4,5,6,7,8,9);
DELETE FROM portal_seguranca.permissao WHERE codigo='LISTAR_PROCESSO_JURIDICO'; 
DELETE FROM portal_seguranca.permissao WHERE codigo='VINCULAR_PROCESSO_JURIDICO'; 
DELETE FROM portal_seguranca.perfil WHERE id IN (3,4,5,6,7,8,9);
