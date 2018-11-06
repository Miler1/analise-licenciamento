# --- !Ups

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
COMMENT ON COLUMN analise.empreendimento_manejo.cpfCnpj IS 'Denominação do empreendimento.';
COMMENT ON COLUMN analise.empreendimento_manejo.id_imovel IS 'Identificador da tabela imóvel.';
COMMENT ON COLUMN analise.empreendimento_manejo.id_municipio IS 'Identificador da tabela município.';


-- alteração imóvel manejo

ALTER TABLE analise.imovel_manejo DROP COLUMN endereco;
ALTER TABLE analise.imovel_manejo DROP COLUMN bairro;
ALTER TABLE analise.imovel_manejo DROP COLUMN cep;

DELETE FROM analise.imovel_manejo;

ALTER TABLE analise.imovel_manejo ADD COLUMN id_municipio integer NOT NULL;
ALTER TABLE analise.imovel_manejo ADD CONSTRAINT fk_i_municipio FOREIGN KEY (id_municipio)
      REFERENCES licenciamento.municipio (id_municipio);

COMMENT ON COLUMN analise.imovel_manejo.id_municipio IS 'Identificador da tabela município.';



# --- !Downs

DROP TABLE analise.empreendimento_manejo;
