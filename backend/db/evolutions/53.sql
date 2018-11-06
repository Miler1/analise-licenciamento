# --- !Ups

-- Alterando entidade de processo_manejo

ALTER TABLE analise.processo_manejo DROP COLUMN id_municipio_simlam;
ALTER TABLE analise.processo_manejo DROP COLUMN nome_municipio_simlam;
ALTER TABLE analise.processo_manejo RENAME COLUMN id_empreendimento_simlam TO id_empreendimento;
ALTER TABLE analise.processo_manejo DROP COLUMN cpf_cnpj_empreendimento;
ALTER TABLE analise.processo_manejo DROP COLUMN denominacao_empreendimento_simlam;
ALTER TABLE analise.processo_manejo DROP COLUMN nome_tipo_licenca;
ALTER TABLE analise.processo_manejo DROP COLUMN id_imovel_manejo;


-- Criação da tabela tipo_licenca_manejo

CREATE TABLE analise.tipo_licenca_manejo (
 id SERIAL NOT NULL,
 nome VARCHAR(200) NOT NULL,
 codigo VARCHAR(200) NOT NULL,
 CONSTRAINT pk_tipo_licenca_manejo PRIMARY KEY(id)
);

COMMENT ON TABLE analise.tipo_licenca_manejo IS 'Entidade responsável por armazenas o tipo de licença de um processo do manejo.';
COMMENT ON COLUMN analise.tipo_licenca_manejo.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.tipo_licenca_manejo.nome IS 'Nome do tipo de licença.';
COMMENT ON COLUMN analise.tipo_licenca_manejo.codigo IS 'Código do tipo de licença.';

ALTER TABLE analise.tipo_licenca_manejo OWNER TO postgres;
GRANT ALL ON TABLE analise.tipo_licenca_manejo TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.tipo_licenca_manejo TO licenciamento_pa;
GRANT SELECT, USAGE ON SEQUENCE analise.tipo_licenca_manejo_id_seq TO licenciamento_pa;


-- Criação da tabela tipologia_manejo

CREATE TABLE analise.tipologia_manejo (
 id SERIAL NOT NULL,
 nome VARCHAR(200) NOT NULL,
 codigo VARCHAR(200) NOT NULL,
 CONSTRAINT pk_tipologia_manejo PRIMARY KEY(id)
);

COMMENT ON TABLE analise.tipologia_manejo IS 'Entidade responsável por armazenas a tipologia de um processo do manejo.';
COMMENT ON COLUMN analise.tipologia_manejo.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.tipologia_manejo.nome IS 'Nome da tipologia.';
COMMENT ON COLUMN analise.tipologia_manejo.codigo IS 'Código da tipologia.';

ALTER TABLE analise.tipologia_manejo OWNER TO postgres;
GRANT ALL ON TABLE analise.tipologia_manejo TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.tipologia_manejo TO licenciamento_pa;
GRANT SELECT, USAGE ON SEQUENCE analise.tipologia_manejo_id_seq TO licenciamento_pa;

-- Criação da tabela atividade_manejo

CREATE TABLE analise.atividade_manejo (
 id SERIAL NOT NULL,
 nome VARCHAR(200) NOT NULL,
 codigo VARCHAR(200) NOT NULL,
 id_tipologia INTEGER NOT NULL,
 CONSTRAINT pk_atividade_manejo PRIMARY KEY(id),
 CONSTRAINT fk_atm_tm FOREIGN KEY (id_tipologia) REFERENCES analise.tipologia_manejo (id)
);

COMMENT ON TABLE analise.atividade_manejo IS 'Entidade responsável por armazenas a atividade de um processo do manejo.';
COMMENT ON COLUMN analise.atividade_manejo.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.atividade_manejo.nome IS 'Nome da atividade.';
COMMENT ON COLUMN analise.atividade_manejo.codigo IS 'Código da atividade.';
COMMENT ON COLUMN analise.atividade_manejo.id_tipologia IS 'Identificador da entidade tipologia que faz o relacionamento entre tipologia e atividade do manejo.';

ALTER TABLE analise.atividade_manejo OWNER TO postgres;
GRANT ALL ON TABLE analise.atividade_manejo TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.atividade_manejo TO licenciamento_pa;
GRANT SELECT, USAGE ON SEQUENCE analise.atividade_manejo_id_seq TO licenciamento_pa;

-- tabela empreendimento_manejo

CREATE TABLE analise.empreendimento_manejo
(
  id integer NOT NULL,
  denominacao varchar(500) NOT NULL,
  cpfCnpj varchar(14) NOT NULL,
  id_imovel integer NOT NULL,
  id_municipio integer NOT NULL,

  CONSTRAINT pk_empreendimento_manejo PRIMARY KEY (id),
  CONSTRAINT fk_e_imovel FOREIGN KEY (id_imovel)
      REFERENCES analise.imovel_manejo (id),
  CONSTRAINT fk_e_municipio FOREIGN KEY (id_municipio)
      REFERENCES licenciamento.municipio (id_municipio)
);

ALTER TABLE analise.empreendimento_manejo
  OWNER TO postgres;
GRANT ALL ON TABLE analise.empreendimento_manejo TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.empreendimento_manejo TO licenciamento_pa;

COMMENT ON TABLE analise.empreendimento_manejo IS 'Entidade responsavel por armazenar o empreendimento do manejo.';
COMMENT ON COLUMN analise.empreendimento_manejo.id IS 'Identificado único da entidade.';
COMMENT ON COLUMN analise.empreendimento_manejo.denominacao IS 'Denominação do empreendimento.';
COMMENT ON COLUMN analise.empreendimento_manejo.cpfCnpj IS 'CPF/CNPJ do empreendimento.';
COMMENT ON COLUMN analise.empreendimento_manejo.id_imovel IS 'Identificador da tabela imóvel.';
COMMENT ON COLUMN analise.empreendimento_manejo.id_municipio IS 'Identificador da tabela município.';


-- Alteração imóvel manejo

ALTER TABLE analise.imovel_manejo DROP COLUMN endereco;
ALTER TABLE analise.imovel_manejo DROP COLUMN bairro;
ALTER TABLE analise.imovel_manejo DROP COLUMN cep;

DELETE FROM analise.imovel_manejo;

ALTER TABLE analise.imovel_manejo ADD COLUMN  descricao_acesso TEXT;
ALTER TABLE analise.imovel_manejo ADD COLUMN id_municipio integer NOT NULL;
ALTER TABLE analise.imovel_manejo ADD CONSTRAINT fk_i_municipio FOREIGN KEY (id_municipio)
      REFERENCES licenciamento.municipio (id_municipio);

COMMENT ON COLUMN analise.imovel_manejo.id_municipio IS 'Identificador da tabela município.';
COMMENT ON COLUMN analise.imovel_manejo.descricao_acesso IS 'Descrição de acesso (endereço) do imóvel.';

-- Adicionando constraints a entidade processo_manejo

ALTER TABLE analise.processo_manejo ADD CONSTRAINT fk_pm_em FOREIGN KEY (id_empreendimento) REFERENCES analise.empreendimento_manejo(id);
COMMENT ON COLUMN analise.processo_manejo.id_empreendimento IS 'Identificador da entidade empreendimento_manejo que faz o relacionamento entre empreendimento manejo e processo manejo.';

ALTER TABLE analise.processo_manejo ADD CONSTRAINT fk_pm_tlm FOREIGN KEY (id_tipo_licenca) REFERENCES analise.tipo_licenca_manejo(id);
COMMENT ON COLUMN analise.processo_manejo.id_tipo_licenca IS 'Identificador da entidade tipo_licenca_manejo que faz o relacionamento entre tipo licença manejo e processo manejo.';

ALTER TABLE analise.processo_manejo ADD COLUMN id_antividade_manejo INTEGER NOT NULL;
ALTER TABLE analise.processo_manejo ADD CONSTRAINT fk_pm_am FOREIGN KEY (id_antividade_manejo) REFERENCES analise.atividade_manejo(id);
COMMENT ON COLUMN analise.processo_manejo.id_antividade_manejo IS 'Identificador da entidade atividade_manejo que faz o relacionamento entre atividade manejo e processo manejo.';


# --- !Downs

-- Removendo constraints a entidade processo_manejo

ALTER TABLE analise.processo_manejo DROP COLUMN id_antividade_manejo;
ALTER TABLE analise.processo_manejo DROP CONSTRAINT fk_pm_tlm;
ALTER TABLE analise.processo_manejo DROP CONSTRAINT fk_pm_em;

-- Alteração imóvel manejo

ALTER TABLE analise.imovel_manejo ADD COLUMN endereco TEXT;
ALTER TABLE analise.imovel_manejo ADD COLUMN bairro VARCHAR(250);
ALTER TABLE analise.imovel_manejo ADD COLUMN cep VARCHAR(10);
ALTER TABLE analise.imovel_manejo DROP COLUMN id_municipio;


-- tabela empreendimento_manejo

DROP TABLE analise.empreendimento_manejo;

-- tabela atividade_manejo

DROP TABLE analise.atividade_manejo;

-- tabela tipologia_manejo

DROP TABLE analise.tipologia_manejo;

-- tabela tipo_licenca_manejo

DROP TABLE analise.tipo_licenca_manejo;

-- Alterando entidade de processo_manejo

ALTER TABLE analise.processo_manejo ADD COLUMN id_imovel_manejo INTEGER NOT NULL;
ALTER TABLE analise.processo_manejo ADD CONSTRAINT fk_pm_imovel_manejo FOREIGN KEY (id_imovel_manejo) REFERENCES analise.imovel_manejo (id);
ALTER TABLE analise.processo_manejo ADD COLUMN nome_tipo_licenca VARCHAR(200) NOT NULL;
ALTER TABLE analise.processo_manejo ADD COLUMN denominacao_empreendimento_simlam VARCHAR(1000) NOT NULL;
ALTER TABLE analise.processo_manejo ADD COLUMN cpf_cnpj_empreendimento VARCHAR(20) NOT NULL;
ALTER TABLE analise.processo_manejo RENAME COLUMN id_empreendimento TO id_empreendimento_simlam;
ALTER TABLE analise.processo_manejo ADD COLUMN nome_municipio_simlam VARCHAR(1000) NOT NULL;
ALTER TABLE analise.processo_manejo ADD COLUMN id_municipio_simlam INTEGER NOT NULL;

COMMENT ON COLUMN analise.processo_manejo.id_empreendimento_simlam IS 'Identificador do empreendimento no SIMLAM.';
COMMENT ON COLUMN analise.processo_manejo.cpf_cnpj_empreendimento IS 'CPF ou CNPJ do empreendimento no SIMLAM.';
COMMENT ON COLUMN analise.processo_manejo.denominacao_empreendimento_simlam IS 'Denominação do empreeendimento no SIMLAM.';
COMMENT ON COLUMN analise.processo_manejo.id_municipio_simlam IS 'Identificador do município no SIMLAM.';
COMMENT ON COLUMN analise.processo_manejo.nome_municipio_simlam IS 'Nome do município no SIMLAM.';
COMMENT ON COLUMN analise.processo_manejo.id_tipo_licenca IS 'Identificador do tipo de licença de manejo do SIMLAM.';
COMMENT ON COLUMN analise.processo_manejo.nome_tipo_licenca IS 'Nome do tipo de licença de manejo do SIMLAM.';
COMMENT ON COLUMN analise.processo_manejo.id_imovel_manejo IS 'Identificador da imóvelo do manejo.';