# --- !Ups


INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor) VALUES ('Secretaria Adjunta de Gestão e Regularidade Ambiental', 'SAGRA', NULL, 0);
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor) VALUES ('Diretoria de Gestão Florestal e Agrossilvipastoril', 'DGFLOR', NULL, 1);
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor) VALUES ('Diretoria de Licenciamento Ambiental', 'DLA', NULL, 1);
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor) VALUES ('Coordenadoria  de Gestão Agropastoril e Industrial', 'COGAPI', (SELECT id FROM portal_seguranca.setor WHERE sigla='DGFLOR'), 2);
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor) VALUES ('Coordenadoria de Gestão Florestal', 'COGEF', (SELECT id FROM portal_seguranca.setor WHERE sigla='DGFLOR'), 2);
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor) VALUES ('Coordenadoria de Indústria Comércio Serviços e Resíduos', 'CIND', (SELECT id FROM portal_seguranca.setor WHERE sigla='DLA'), 2);
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor) VALUES ('Coordenadoria de Infraestrutura, Fauna, Aquicultura e Pesca', 'CINFAP', (SELECT id FROM portal_seguranca.setor WHERE sigla='DLA'), 2);
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor) VALUES ('Coordenadoria de Mineração', 'CMINA', (SELECT id FROM portal_seguranca.setor WHERE sigla='DLA'), 2);
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor) VALUES ('Gerência de Atividades Agropecuárias', 'GEAGRO', (SELECT id FROM portal_seguranca.setor WHERE sigla='COGAPI'), 3);
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor) VALUES ('Gerência de Cadastro, Transporte e Comercialização de Produtos e Subprodutos Florestais', 'GESFLORA', (SELECT id FROM portal_seguranca.setor WHERE sigla='COGEF'), 3);
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor) VALUES ('Gerência de Fauna, Flora, Aquicultura e Pesca', 'GEFAP', (SELECT id FROM portal_seguranca.setor WHERE sigla='CINFAP'), 3);
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor) VALUES ('Gerência de Infraestrutura de Transporte e Obras Civis', 'GEINFRA', (SELECT id FROM portal_seguranca.setor WHERE sigla='CINFAP'), 3);
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor) VALUES ('Gerência de Projetos de Comércio e Serviços', 'GECOS', (SELECT id FROM portal_seguranca.setor WHERE sigla='CIND'), 3);
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor) VALUES ('Gerência de Projetos de Processamento de Produtos e Subprodutos Florestais', 'GEPROF', (SELECT id FROM portal_seguranca.setor WHERE sigla='COGAPI'), 3);
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor) VALUES ('Gerência de Projetos Industriais', 'GEIND', (SELECT id FROM portal_seguranca.setor WHERE sigla='CIND'), 3);
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor) VALUES ('Gerência de Projetos Minerários Metálicos', 'GEMIM', (SELECT id FROM portal_seguranca.setor WHERE sigla='CMINA'), 3);
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor) VALUES ('Gerência de Projetos Minerários Não Metálicos', 'GEMINA', (SELECT id FROM portal_seguranca.setor WHERE sigla='CMINA'), 3);
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor) VALUES ('Gerência de Projetos Silvipastoris', 'GEPAF', (SELECT id FROM portal_seguranca.setor WHERE sigla='COGEF'), 3);
INSERT INTO portal_seguranca.setor (nome, sigla, id_setor_pai, tipo_setor) VALUES ('Gerência de Infraestrutura de Energia, Parcelamento do Solo e Saneamento', 'GEPAS', (SELECT id FROM portal_seguranca.setor WHERE sigla='CINFAP'), 3);

INSERT INTO portal_seguranca.perfil_setor(id_perfil,id_setor) VALUES
(9,(SELECT id FROM portal_seguranca.setor WHERE sigla='DGFLOR')),
(9,(SELECT id FROM portal_seguranca.setor WHERE sigla='DLA')),
(6,(SELECT id FROM portal_seguranca.setor WHERE sigla='COGAPI')),
(6,(SELECT id FROM portal_seguranca.setor WHERE sigla='COGEF')),
(6,(SELECT id FROM portal_seguranca.setor WHERE sigla='CIND')),
(6,(SELECT id FROM portal_seguranca.setor WHERE sigla='CINFAP')),
(6,(SELECT id FROM portal_seguranca.setor WHERE sigla='CMINA')),
(8,(SELECT id FROM portal_seguranca.setor WHERE sigla='GEAGRO')),
(7,(SELECT id FROM portal_seguranca.setor WHERE sigla='GESFLORA')),
(7,(SELECT id FROM portal_seguranca.setor WHERE sigla='GEFAP')),
(7,(SELECT id FROM portal_seguranca.setor WHERE sigla='GEINFRA')),
(7,(SELECT id FROM portal_seguranca.setor WHERE sigla='GECOS')),
(8,(SELECT id FROM portal_seguranca.setor WHERE sigla='GEPROF')),
(7,(SELECT id FROM portal_seguranca.setor WHERE sigla='GEIND')),
(7,(SELECT id FROM portal_seguranca.setor WHERE sigla='GEMIM')),
(7,(SELECT id FROM portal_seguranca.setor WHERE sigla='GEMINA')),
(7,(SELECT id FROM portal_seguranca.setor WHERE sigla='GEPAF')),
(7,(SELECT id FROM portal_seguranca.setor WHERE sigla='GEPAS'));

INSERT INTO portal_seguranca.perfil_setor(id_perfil,id_setor) VALUES
(7,(SELECT id FROM portal_seguranca.setor WHERE sigla='GEAGRO')),
(7,(SELECT id FROM portal_seguranca.setor WHERE sigla='GEPROF'));


# --- !Downs


DELETE FROM portal_seguranca.perfil_setor WHERE id_setor IN (SELECT id FROM portal_seguranca.setor WHERE sigla IN ('SAGRA','DGFLOR','DLA','COGAPI','COGEF','CIND','CINFAP','CMINA','GEAGRO','GESFLORA','GEFAP','GEINFRA','GECOS','GEPROF','GEIND','GEMIM','GEMINA','GEPAF','GEPAS'));

DELETE FROM portal_seguranca.setor WHERE sigla IN ('SAGRA','DGFLOR','DLA','COGAPI','COGEF','CIND','CINFAP','CMINA','GEAGRO','GESFLORA','GEFAP','GEINFRA','GECOS','GEPROF','GEIND','GEMIM','GEMINA','GEPAF','GEPAS');