
# --- !Ups

CREATE TABLE analise.parecer_gerente_analise_tecnica
(
  id serial NOT NULL, 
  id_tipo_resultado_analise integer NOT NULL, 
  parecer text NOT NULL, 
  data_parecer timestamp without time zone DEFAULT now(), 
  id_usuario_gerente integer NOT NULL,
  id_analise_tecnica integer NOT NULL, 
  id_historico_tramitacao integer NOT NULL,
  CONSTRAINT pk_parecer_gerente_analise_tecnica PRIMARY KEY (id),
  CONSTRAINT fk_pgat_analise_tecnica FOREIGN KEY (id_analise_tecnica)
      REFERENCES analise.analise_tecnica (id) ,
  CONSTRAINT fk_pgat_tipo_resultado_analise FOREIGN KEY (id_tipo_resultado_analise)
      REFERENCES analise.tipo_resultado_analise (id),
  CONSTRAINT fk_pgat_usuario_analise FOREIGN KEY (id_usuario_gerente)
      REFERENCES analise.usuario_analise (id) 
);
ALTER TABLE analise.parecer_gerente_analise_tecnica OWNER TO postgres;
ALTER TABLE analise.parecer_gerente_analise_tecnica OWNER TO licenciamento_am;
GRANT SELECT,INSERT,UPDATE,DELETE ON TABLE analise.parecer_gerente_analise_tecnica TO licenciamento_am;
GRANT ALL ON TABLE analise.parecer_gerente_analise_tecnica TO postgres;
GRANT SELECT,USAGE ON SEQUENCE analise.parecer_gerente_analise_tecnica_id_seq TO licenciamento_am;
COMMENT ON TABLE analise.parecer_gerente_analise_tecnica IS 'Entidade responsável por armazenar informações sobre o parecer do gerente em uma análise técnica';
COMMENT ON COLUMN analise.parecer_gerente_analise_tecnica.id IS 'Identificador do parecer do gerente';
COMMENT ON COLUMN analise.parecer_gerente_analise_tecnica.id_tipo_resultado_analise IS 'Tipo do resultado da análise';
COMMENT ON COLUMN analise.parecer_gerente_analise_tecnica.parecer IS 'Descrição do parecer feito pelo gerente';
COMMENT ON COLUMN analise.parecer_gerente_analise_tecnica.data_parecer IS 'Data do parecer';
COMMENT ON COLUMN analise.parecer_gerente_analise_tecnica.id_usuario_gerente IS 'Identificador do gerente responsável pelo parecer';
COMMENT ON COLUMN analise.parecer_gerente_analise_tecnica.id_analise_tecnica IS 'Identificador da análise tecnica que teve parecer';
COMMENT ON COLUMN analise.parecer_gerente_analise_tecnica.id_historico_tramitacao IS 'Identificador do histórico da tramitação';



CREATE TABLE analise.diretor
(
  id serial NOT NULL, 
  id_analise_tecnica integer,
  id_usuario integer NOT NULL, 
  data_vinculacao timestamp without time zone, 
  id_analise_geo integer, 
  CONSTRAINT pk_diretor PRIMARY KEY (id),
  CONSTRAINT fk_d_analise_geo FOREIGN KEY (id_analise_geo)
      REFERENCES analise.analise_geo (id),
  CONSTRAINT fk_d_usuario_analise FOREIGN KEY (id_usuario)
      REFERENCES analise.usuario_analise (id),
  CONSTRAINT fk_d_analise_tecnica FOREIGN KEY (id_analise_tecnica)
      REFERENCES analise.analise_tecnica (id) 
);
ALTER TABLE analise.diretor OWNER TO postgres;
GRANT ALL ON TABLE analise.diretor TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.diretor TO licenciamento_am;
ALTER TABLE  analise.diretor OWNER TO licenciamento_am;
GRANT SELECT,USAGE ON SEQUENCE  analise.diretor_id_seq TO licenciamento_am;
COMMENT ON TABLE analise.diretor IS 'Entidade responsável por armazenar o Gerente responsável pela análise técnica.';
COMMENT ON COLUMN analise.diretor.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.diretor.id_analise_tecnica IS 'Identificador da entidade analise_tecnica que realiza o relacionamento entre as entidades diretor e analise_tecnica.';
COMMENT ON COLUMN analise.diretor.id_usuario IS 'Identificador da entidade analise.usuario_analise que realiza o relacionamento entre as entidades diretor e analise.usuario_analise.';
COMMENT ON COLUMN analise.diretor.data_vinculacao IS 'Data em que o usuário foi vinculado a análise técnica.';
COMMENT ON COLUMN analise.diretor.id_analise_geo IS 'Identificador da entidade analise_geo que realiza o relacionamento entre as entidades diretor e analise_geo.';



# --- !Downs

DROP TABLE analise.diretor;

DROP TABLE analise.parecer_gerente_analise_tecnica;


