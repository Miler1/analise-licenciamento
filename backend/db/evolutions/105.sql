# --- !Ups
CREATE TABLE analise.desvinculo_analise_tecnica
(
  id serial NOT NULL, -- Identificador único da entidade.
  justificativa text NOT NULL, -- Campo para armazenar a justificativa da solicitação de desvínculo do analista.
  resposta_gerente text, -- Campo para armazenar a resposta digitada pelo gerente.
  aprovada boolean, -- Flag de controle para saber o status da solicitação de desvínculo.
  id_gerente integer, -- Identificador único da entidade gerentes que faz o relacionamento entre as duas entidades.
  data_solicitacao timestamp without time zone, -- Data em que foi solicitado o desvínculo por um analista.
  data_resposta timestamp without time zone, -- Data em que o gerente responde o desvínculo.
  id_analise_tecnica integer, -- Identificador da tabela id_analise_geo, responsável pelo relacionamento entre as duas tabelas.
  id_usuario integer, -- Identificador da entidade usuario_analise
  id_usuario_destino integer, -- Indentificador da entidade usuario_analise para guardar o destinatário da analise
  CONSTRAINT pk_desvinculo_analise_tecnica PRIMARY KEY (id),
  CONSTRAINT fk_dat_gerente_usuario_analise FOREIGN KEY (id_gerente)
      REFERENCES analise.usuario_analise (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE analise.desvinculo_analise_tecnica OWNER TO postgres;
GRANT ALL ON TABLE analise.desvinculo_analise_tecnica TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.desvinculo_analise_tecnica TO licenciamento_am;
GRANT SELECT ON TABLE analise.desvinculo_analise_tecnica TO tramitacao;
COMMENT ON TABLE analise.desvinculo_analise_tecnica IS 'Entidade responsável por armazenar o analista da analise geo.';
COMMENT ON COLUMN analise.desvinculo_analise_tecnica.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.desvinculo_analise_tecnica.justificativa IS 'Campo para armazenar a justificativa da solicitação de desvínculo do analista.';
COMMENT ON COLUMN analise.desvinculo_analise_tecnica.resposta_gerente IS 'Campo para armazenar a resposta digitada pelo gerente.';
COMMENT ON COLUMN analise.desvinculo_analise_tecnica.aprovada IS 'Flag de controle para saber o status da solicitação de desvínculo.';
COMMENT ON COLUMN analise.desvinculo_analise_tecnica.id_gerente IS 'Identificador único da entidade gerentes que faz o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.desvinculo_analise_tecnica.data_solicitacao IS 'Data em que foi solicitado o desvínculo por um analista.';
COMMENT ON COLUMN analise.desvinculo_analise_tecnica.data_resposta IS 'Data em que o gerente responde o desvínculo.';
COMMENT ON COLUMN analise.desvinculo_analise_tecnica.id_analise_tecnica IS 'Identificador da tabela id_analise_geo, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.desvinculo_analise_tecnica.id_usuario IS 'Identificador da entidade usuario_analise';
COMMENT ON COLUMN analise.desvinculo_analise_tecnica.id_usuario_destino IS 'Indentificador da entidade usuario_analise para guardar o destinatário da analise';


ALTER TABLE analise.desvinculo RENAME TO desvinculo_analise_geo;

ALTER TABLE analise.desvinculo_analise_geo RENAME CONSTRAINT pk_desvinculo TO pk_desvinculo_analise_geo;

ALTER TABLE analise.desvinculo_analise_geo RENAME CONSTRAINT fk_d_gerente_usuario_analise TO fk_dag_gerente_usuario_analise;

ALTER TABLE analise.desvinculo_analise_geo  DROP COLUMN id_analise_tecnica; 

ALTER TABLE analise.desvinculo_analise_geo  DROP COLUMN id_analise_juridica;


# --- !Downs


ALTER TABLE analise.desvinculo_analise_geo RENAME TO desvinculo;

ALTER TABLE analise.desvinculo ADD COLUMN id_analise_juridica integer;

ALTER TABLE analise.desvinculo ADD COLUMN id_analise_tecnica integer;

ALTER TABLE analise.desvinculo RENAME CONSTRAINT fk_dag_gerente_usuario_analise TO fk_d_gerente_usuario_analise;

ALTER TABLE analise.desvinculo RENAME CONSTRAINT pk_desvinculo_analise_geo TO pk_desvinculo; 

DROP TABLE analise.desvinculo_analise_tecnica;
