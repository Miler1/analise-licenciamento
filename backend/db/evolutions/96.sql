# --- !Ups

CREATE TABLE analise.setor_usuario_analise
(
    id SERIAL NOT NULL,
    id_usuario_analise INTEGER NOT NULL,
    sigla_setor TEXT NOT NULL,
    nome_setor TEXT,
  CONSTRAINT pk_setor_usuario_analise PRIMARY KEY (id),
  CONSTRAINT fk_sua_usuario_analise FOREIGN KEY (id_usuario_analise)
      REFERENCES analise.usuario_analise (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE analise.setor_usuario_analise
  OWNER TO postgres;
GRANT ALL ON TABLE analise.setor_usuario_analise TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.setor_usuario_analise TO licenciamento_am;
GRANT SELECT, USAGE ON SEQUENCE analise.setor_usuario_analise_id_seq TO licenciamento_am; 

COMMENT ON TABLE analise.setor_usuario_analise
  IS 'Entidade responsável por armazenar o setor do usuário.';
COMMENT ON COLUMN analise.setor_usuario_analise.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.setor_usuario_analise.id_usuario_analise IS 'Identificador da entidade usuario_analise que realiza o relacionamento entre as entidades usuario_analise e setor_usuario_analise.';
COMMENT ON COLUMN analise.setor_usuario_analise.sigla_setor IS 'Sigla do setor do usuário.';
COMMENT ON COLUMN analise.setor_usuario_analise.nome_setor IS 'Nome do setor do usuário.';


CREATE TABLE analise.perfil_usuario_analise
(
	id SERIAL NOT NULL,
	id_usuario_analise INTEGER NOT NULL,
	codigo_perfil TEXT NOT NULL,
	nome_perfil TEXT,
  CONSTRAINT pk_perfil_usuario_analise PRIMARY KEY (id),
  CONSTRAINT fk_pua_usuario_analise FOREIGN KEY (id_usuario_analise)
      REFERENCES analise.usuario_analise (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE analise.perfil_usuario_analise
  OWNER TO postgres;
GRANT ALL ON TABLE analise.perfil_usuario_analise TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.perfil_usuario_analise TO licenciamento_am;
GRANT SELECT, USAGE ON SEQUENCE analise.perfil_usuario_analise_id_seq TO licenciamento_am; 

COMMENT ON TABLE analise.perfil_usuario_analise
  IS 'Entidade responsável por armazenar o perfil do usuário.';
COMMENT ON COLUMN analise.perfil_usuario_analise.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.perfil_usuario_analise.id_usuario_analise IS 'Identificador da entidade usuario_analise que realiza o relacionamento entre as entidades usuario_analise e perfil_usuario_analise.';
COMMENT ON COLUMN analise.perfil_usuario_analise.codigo_perfil IS 'Sigla do perfil do usuário.';
COMMENT ON COLUMN analise.perfil_usuario_analise.nome_perfil IS 'Nome do perfil do usuário.';

# --- !Downs

DROP TABLE analise.perfil_usuario_analise;
DROP TABLE analise.setor_usuario_analise;