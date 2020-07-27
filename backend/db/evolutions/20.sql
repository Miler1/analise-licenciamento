# --- !Ups

CREATE TABLE analise.gerente_tecnico
(
  id serial NOT NULL,
  id_analise_tecnica integer NOT NULL,
  id_usuario integer NOT NULL,
  data_vinculacao timestamp without time zone,
  CONSTRAINT pk_gerente_tecnico PRIMARY KEY (id),
  CONSTRAINT fk_gt_analise_tecnica FOREIGN KEY (id_analise_tecnica)REFERENCES analise.analise_tecnica (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_gt_usuario FOREIGN KEY (id_usuario)REFERENCES portal_seguranca.usuario (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);

GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.gerente_tecnico TO licenciamento_am;
GRANT SELECT, USAGE ON SEQUENCE analise.gerente_tecnico_id_seq TO licenciamento_am;

COMMENT ON TABLE analise.gerente_tecnico IS 'Entidade responsável por armazenar o Gerente responsável pela análise técnica.';
COMMENT ON COLUMN analise.gerente_tecnico.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.gerente_tecnico.id_analise_tecnica IS 'Identificador da entidade analise_tecnica que realiza o relacionamento entre as entidades gerente_tecnico e analise_tecnica.';
COMMENT ON COLUMN analise.gerente_tecnico.id_usuario IS 'Identificador da entidade portal_seguranca.usuario que realiza o relacionamento entre as entidades gerente_tecnico e portal_seguranca.usuario.';
COMMENT ON COLUMN analise.gerente_tecnico.data_vinculacao IS 'Data em que o usuário foi vinculado a análise técnica.';

# --- !Downs

DROP TABLE analise.gerente_tecnico;