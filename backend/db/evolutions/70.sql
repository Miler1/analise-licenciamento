# --- !Ups


CREATE TABLE analise.historico_tramitacao_setor
(
  id_historico_tramitacao integer NOT NULL, -- Identificador único da entidade historico_tramitacao.
  sigla_setor character varying(255) NOT NULL, -- Identificador do setor que faz o relacionamento entre o histórico de tramitação e setor.
  CONSTRAINT pk_historico_tramitacao_setor PRIMARY KEY (id_historico_tramitacao, sigla_setor)
);

ALTER TABLE analise.historico_tramitacao_setor OWNER TO postgres;
GRANT ALL ON TABLE analise.historico_tramitacao_setor TO postgres;
GRANT SELECT,UPDATE,INSERT,DELETE ON TABLE analise.historico_tramitacao_setor TO licenciamento_am;

COMMENT ON TABLE analise.historico_tramitacao_setor IS 'Entidade responsável por armazenar o relacionamento entre historico_tramitacao e um setor.';
COMMENT ON COLUMN analise.historico_tramitacao_setor.id_historico_tramitacao IS 'Identificador único da entidade historico_tramitacao.';
COMMENT ON COLUMN analise.historico_tramitacao_setor.sigla_setor IS 'Identificador do setor que faz o relacionamento entre o histórico de tramitação e setor.';


# --- !Downs

DROP TABLE analise.historico_tramitacao_setor;