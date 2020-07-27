# --- !Ups

--- Carga de dados na entidade tipo_licenca_manejo

INSERT INTO analise.tipo_licenca_manejo (id, codigo, nome) VALUES
 (1, 'APAT', 'APAT'),
 (2, 'AUTEF', 'AUTEF'),
 (3, 'LAR', 'LAR');


--- Carga de dados na entidade tipologia_manejo

INSERT INTO analise.tipologia_manejo (codigo, nome) VALUES
 ('AGROSILVIPASTORIL', 'Agrossilvipastoril');


--- Carga de dados na entidade atividade_manejo

INSERT INTO analise.atividade_manejo (codigo, nome, id_tipologia) VALUES
 ('MANEJO_FLORESTAL_REGIME_RENDIMENTO_SUSTENTAVEL', 'Manejo florestal em regime de rendimento sustentável', (SELECT id FROM analise.tipologia_manejo WHERE codigo = 'AGROSILVIPASTORIL')),
 ('MANEJO_ACAIZAIS', 'Manejo de açaizais', (SELECT id FROM analise.tipologia_manejo WHERE codigo = 'AGROSILVIPASTORIL')),
 ('COMERCIALIZACAO_MANEJO_RECURSOS_AQUATIVOS_VIVOS', 'Comercialização e manejo de recursos aquáticos vivos', (SELECT id FROM analise.tipologia_manejo WHERE codigo = 'AGROSILVIPASTORIL'));

--- Alterando entidade imovel_manejo

ALTER TABLE analise.imovel_manejo ALTER COLUMN area_total_imovel_documentado DROP NOT NULL;
ALTER TABLE analise.imovel_manejo ALTER COLUMN area_liquida_imovel DROP NOT NULL;
ALTER TABLE analise.imovel_manejo ADD COLUMN nome VARCHAR(450);

# --- !Downs

ALTER TABLE analise.imovel_manejo DROP COLUMN nome;
ALTER TABLE analise.imovel_manejo ALTER COLUMN area_total_imovel_documentado SET NOT NULL;
ALTER TABLE analise.imovel_manejo ALTER COLUMN area_liquida_imovel SET NOT NULL;

DELETE FROM analise.atividade_manejo WHERE codigo = 'MANEJO_FLORESTAL_REGIME_RENDIMENTO_SUSTENTAVEL' OR codigo = 'MANEJO_ACAIZAIS' OR codigo = 'COMERCIALIZACAO_MANEJO_RECURSOS_AQUATIVOS_VIVOS';
DELETE FROM analise.tipologia_manejo WHERE codigo = 'AGROSILVIPASTORIL';
DELETE FROM analise.tipo_licenca_manejo WHERE codigo = 'APAT' OR codigo = 'AUTEF' OR codigo = 'LAR';

