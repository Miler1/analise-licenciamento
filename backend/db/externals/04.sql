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

-- Insert dos setores relacionados ao novos perfis
INSERT INTO portal_seguranca.setor (id, nome, sigla, id_setor_pai, id_perfil, tipo_setor) VALUES (1, 'Secretaria Adjunta de Gestão e Regularidade Ambiental', 'SAGRA', NULL, NULL, 0);
INSERT INTO portal_seguranca.setor (id, nome, sigla, id_setor_pai, id_perfil, tipo_setor) VALUES (2, 'Diretoria de Gestão Florestal e Agrossilvipastoril', 'DGFLOR', NULL, 9, 1);
INSERT INTO portal_seguranca.setor (id, nome, sigla, id_setor_pai, id_perfil, tipo_setor) VALUES (3, 'Diretoria de Licenciamento Ambiental', 'DLA', NULL, 9, 1);
INSERT INTO portal_seguranca.setor (id, nome, sigla, id_setor_pai, id_perfil, tipo_setor) VALUES (4, 'Coordenadoria  de Gestão Agropastoril e Industrial', 'COGAPI', 2, 6, 2);
INSERT INTO portal_seguranca.setor (id, nome, sigla, id_setor_pai, id_perfil, tipo_setor) VALUES (5, 'Coordenadoria de Gestão Florestal', 'COGEF', 2, 6, 2);
INSERT INTO portal_seguranca.setor (id, nome, sigla, id_setor_pai, id_perfil, tipo_setor) VALUES (6, 'Coordenadoria de Indústria Comércio Serviços e Resíduos', 'CIND', 3, 6, 2);
INSERT INTO portal_seguranca.setor (id, nome, sigla, id_setor_pai, id_perfil, tipo_setor) VALUES (7, 'Coordenadoria de Infraestrutura, Fauna, Aquicultura e Pesca', 'CINFAP', 3, 6, 2);
INSERT INTO portal_seguranca.setor (id, nome, sigla, id_setor_pai, id_perfil, tipo_setor) VALUES (8, 'Coordenadoria de Mineração', 'CMINA', 3, 6, 2);
INSERT INTO portal_seguranca.setor (id, nome, sigla, id_setor_pai, id_perfil, tipo_setor) VALUES (9, 'Gerência de Atividades Agropecuárias', 'GEAGRO', 4, 8, 3);
INSERT INTO portal_seguranca.setor (id, nome, sigla, id_setor_pai, id_perfil, tipo_setor) VALUES (10, 'Gerência de Cadastro, Transporte e Comercialização de Produtos e Subprodutos Florestais', 'GESFLORA', 5, 7, 3);
INSERT INTO portal_seguranca.setor (id, nome, sigla, id_setor_pai, id_perfil, tipo_setor) VALUES (11, 'Gerência de Fauna, Flora, Aquicultura e Pesca', 'GEFAP', 7, 7, 3);
INSERT INTO portal_seguranca.setor (id, nome, sigla, id_setor_pai, id_perfil, tipo_setor) VALUES (12, 'Gerência de Infraestrutura de Transporte e Obras Civis', 'GEINFRA', 7, 7, 3);
INSERT INTO portal_seguranca.setor (id, nome, sigla, id_setor_pai, id_perfil, tipo_setor) VALUES (13, 'Gerência de Projetos de Comércio e Serviços', 'GECOS', 6, 7, 3);
INSERT INTO portal_seguranca.setor (id, nome, sigla, id_setor_pai, id_perfil, tipo_setor) VALUES (14, 'Gerência de Projetos de Processamento de Produtos e Subprodutos Florestais', 'GEPROF', 4, 8, 3);
INSERT INTO portal_seguranca.setor (id, nome, sigla, id_setor_pai, id_perfil, tipo_setor) VALUES (15, 'Gerência de Projetos Industriais', 'GEIND', 6, 7, 3);
INSERT INTO portal_seguranca.setor (id, nome, sigla, id_setor_pai, id_perfil, tipo_setor) VALUES (16, 'Gerência de Projetos Minerários Metálicos', 'GEMIM', 8, 7, 3);
INSERT INTO portal_seguranca.setor (id, nome, sigla, id_setor_pai, id_perfil, tipo_setor) VALUES (17, 'Gerência de Projetos Minerários Não Metálicos', 'GEMINA', 8, 7, 3);
INSERT INTO portal_seguranca.setor (id, nome, sigla, id_setor_pai, id_perfil, tipo_setor) VALUES (18, 'Gerência de Projetos Silvipastoris', 'GEPAF', 5, 7, 3);
INSERT INTO portal_seguranca.setor (id, nome, sigla, id_setor_pai, id_perfil, tipo_setor) VALUES (19, 'Gerência de Infraestrutura de Energia, Parcelamento do Solo e Saneamento', 'GEPAS', 7, 7, 3);


# --- !Downs


INSERT INTO portal_seguranca.permissao_perfil (id_perfil, id_permissao) VALUES
(2,(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'acessarAnaliseLicenciamento'));
DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil IN (3,4,5,6,7,8,9);
DELETE FROM portal_seguranca.permissao WHERE codigo='LISTAR_PROCESSO_JURIDICO'; 
DELETE FROM portal_seguranca.permissao WHERE codigo='VINCULAR_PROCESSO_JURIDICO'; 
DELETE FROM portal_seguranca.perfil WHERE id IN (3,4,5,6,7,8,9);
DELETE FROM portal_seguranca.setor WHERE id IN (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19);
