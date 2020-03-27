# --- !Ups

INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro) VALUES ('Secretaria Adjunta de Gestão e Regularidade Ambiental', 'SAGRA', NULL, 0'27-03-2020');
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro) VALUES ('Diretoria de Gestão Florestal e Agrossilvipastoril', 'DGFLOR', NULL, 1'27-03-2020');
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro) VALUES ('Diretoria de Licenciamento Ambiental', 'DLA', NULL, 1'27-03-2020');
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro) VALUES ('Coordenadoria  de Gestão Agropastoril e Industrial', 'COGAPI', (SELECT id FROM portal_seguranca.setor WHERE sigla='DGFLOR'), 2'27-03-2020');
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro) VALUES ('Coordenadoria de Gestão Florestal', 'COGEF', (SELECT id FROM portal_seguranca.setor WHERE sigla='DGFLOR'), 2'27-03-2020');
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro) VALUES ('Coordenadoria de Indústria Comércio Serviços e Resíduos', 'CIND', (SELECT id FROM portal_seguranca.setor WHERE sigla='DLA'), 2'27-03-2020');
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro) VALUES ('Coordenadoria de Infraestrutura, Fauna, Aquicultura e Pesca', 'CINFAP', (SELECT id FROM portal_seguranca.setor WHERE sigla='DLA'), 2'27-03-2020');
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro) VALUES ('Coordenadoria de Mineração', 'CMINA', (SELECT id FROM portal_seguranca.setor WHERE sigla='DLA'), 2'27-03-2020');
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro) VALUES ('Gerência de Atividades Agropecuárias', 'GEAGRO', (SELECT id FROM portal_seguranca.setor WHERE sigla='COGAPI'), 3'27-03-2020');
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro) VALUES ('Gerência de Cadastro, Transporte e Comercialização de Produtos e Subprodutos Florestais', 'GESFLORA', (SELECT id FROM portal_seguranca.setor WHERE sigla='COGEF'), 3'27-03-2020');
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro) VALUES ('Gerência de Fauna, Flora, Aquicultura e Pesca', 'GEFAP', (SELECT id FROM portal_seguranca.setor WHERE sigla='CINFAP'), 3'27-03-2020');
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro) VALUES ('Gerência de Infraestrutura de Transporte e Obras Civis', 'GEINFRA', (SELECT id FROM portal_seguranca.setor WHERE sigla='CINFAP'), 3'27-03-2020');
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro) VALUES ('Gerência de Projetos de Comércio e Serviços', 'GECOS', (SELECT id FROM portal_seguranca.setor WHERE sigla='CIND'), 3'27-03-2020');
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro) VALUES ('Gerência de Projetos de Processamento de Produtos e Subprodutos Florestais', 'GEPROF', (SELECT id FROM portal_seguranca.setor WHERE sigla='COGAPI'), 3'27-03-2020');
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro) VALUES ('Gerência de Projetos Industriais', 'GEIND', (SELECT id FROM portal_seguranca.setor WHERE sigla='CIND'), 3'27-03-2020');
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro) VALUES ('Gerência de Projetos Minerários Metálicos', 'GEMIM', (SELECT id FROM portal_seguranca.setor WHERE sigla='CMINA'), 3'27-03-2020');
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro) VALUES ('Gerência de Projetos Minerários Não Metálicos', 'GEMINA', (SELECT id FROM portal_seguranca.setor WHERE sigla='CMINA'), 3'27-03-2020');
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro) VALUES ('Gerência de Projetos Silvipastoris', 'GEPAF', (SELECT id FROM portal_seguranca.setor WHERE sigla='COGEF'), 3'27-03-2020');
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor, data_cadastro) VALUES ('Gerência de Infraestrutura de Energia, Parcelamento do Solo e Saneamento', 'GEPAS', (SELECT id FROM portal_seguranca.setor WHERE sigla='CINFAP'), 3'27-03-2020');


# --- !Downs


DELETE FROM portal_seguranca.setor WHERE sigla IN ('SAGRA','DGFLOR','DLA','COGAPI','COGEF','CIND','CINFAP','CMINA','GEAGRO','GESFLORA','GEFAP','GEINFRA','GECOS','GEPROF','GEIND','GEMIM','GEMINA','GEPAF','GEPAS');