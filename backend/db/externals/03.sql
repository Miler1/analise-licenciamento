# --- !Ups

UPDATE portal_seguranca.perfil SET nome = 'Presidente' WHERE nome = 'Diretor PRESIDENTE';
UPDATE portal_seguranca.perfil SET nome = 'Gerente' WHERE nome = 'Gerente TÉCNICO';

UPDATE portal_seguranca.permissao
SET codigo = 'ANL_INICIAR_PARECER_SECRETARIO',
    nome='Iniciar parecer do secretário'
WHERE codigo = 'ANL_INICIAR_PARECER_PRESIDENTE';

UPDATE portal_seguranca.perfil
SET nome = 'Secretário', codigo = 'SECRETARIO'
WHERE nome = 'Presidente' AND codigo = 'PRESIDENTE';

UPDATE portal_seguranca.perfil
SET nome = 'Coordenador', codigo = 'COORDENADOR'
WHERE nome = 'Gerente' AND codigo = 'GERENTE';

UPDATE portal_seguranca.setor
SET sigla = 'CCAP', nome = 'Coordenadoria de Controle Agropecuário'
WHERE sigla = 'GCAP';

UPDATE portal_seguranca.setor
SET sigla = 'CFAU', nome = 'Coordenadoria de Fauna'
WHERE sigla = 'GFAU';

UPDATE portal_seguranca.setor
SET sigla = 'CERM', nome = 'Coordenadoria de Recursos Hídricos e Minerais'
WHERE sigla = 'GERM';

UPDATE portal_seguranca.setor
SET sigla = 'CECF', nome = 'Coordenadoria de Controle Florestal'
WHERE sigla = 'GECF';

UPDATE portal_seguranca.setor
SET sigla = 'CELI', nome = 'Coordenadoria de Licenciamento Industrial'
WHERE sigla = 'GELI';

UPDATE portal_seguranca.setor
SET sigla = 'CECP', nome = 'Coordenadoria de Controle de Pesca'
WHERE sigla = 'GECP';

UPDATE portal_seguranca.setor
SET sigla = 'CERH', nome = 'Coordenadoria de Recursos Humanos'
WHERE sigla = 'GERH';

# --- !Downs

UPDATE portal_seguranca.permissao
SET codigo = 'ANL_INICIAR_PARECER_PRESIDENTE',
    nome='Iniciar parecer do presidente'
WHERE codigo = 'ANL_INICIAR_PARECER_SECRETARIO';

UPDATE portal_seguranca.perfil
SET nome = 'Presidente', codigo = 'PRESIDENTE'
WHERE nome = 'Secretário' AND codigo = 'SECRETARIO';

UPDATE portal_seguranca.perfil
SET nome = 'Gerente', codigo = 'GERENTE'
WHERE nome = 'Coordenador' AND codigo = 'COORDENADOR';

UPDATE portal_seguranca.setor
SET sigla = 'GCAP', nome = 'Gerência de Controle Agropecuário'
WHERE sigla = 'CCAP';

UPDATE portal_seguranca.setor
SET sigla = 'GFAU', nome = 'Gerência de Fauna'
WHERE sigla = 'CFAU';

UPDATE portal_seguranca.setor
SET sigla = 'GERM', nome = 'Gerência de Recursos Hídricos e Minerais'
WHERE sigla = 'CERM';

UPDATE portal_seguranca.setor
SET sigla = 'GECF', nome = 'Gerência de Controle Florestal'
WHERE sigla = 'CECF';

UPDATE portal_seguranca.setor
SET sigla = 'GELI', nome = 'Gerência de Licenciamento Industrial'
WHERE sigla = 'CELI';

UPDATE portal_seguranca.setor
SET sigla = 'GECP', nome = 'Gerência de Controle de Pesca'
WHERE sigla = 'CECP';

UPDATE portal_seguranca.setor
SET sigla = 'GERH', nome = 'Gerência de Recursos Humanos'
WHERE sigla = 'CERH';

