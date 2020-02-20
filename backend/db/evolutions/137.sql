
# --- !Ups

CREATE TABLE analise.parecer_diretor_tecnico
(
  id serial NOT NULL,
  id_tipo_resultado_analise integer NOT NULL,
  parecer text NOT NULL,
  data_parecer timestamp without time zone DEFAULT now(),
  id_usuario_diretor integer NOT NULL,
  id_analise integer NOT NULL,
  id_historico_tramitacao integer NOT NULL,
  CONSTRAINT pk_parecer_diretor_tecnico PRIMARY KEY (id),
  CONSTRAINT fk_pdt_analise FOREIGN KEY (id_analise)
      REFERENCES analise.analise (id) ,
  CONSTRAINT fk_pdt_tipo_resultado_analise FOREIGN KEY (id_tipo_resultado_analise)
      REFERENCES analise.tipo_resultado_analise (id),
  CONSTRAINT fk_pdt_usuario_analise FOREIGN KEY (id_usuario_diretor)
      REFERENCES analise.usuario_analise (id)
);
ALTER TABLE analise.parecer_diretor_tecnico OWNER TO postgres;
ALTER TABLE analise.parecer_diretor_tecnico OWNER TO licenciamento_am;
GRANT SELECT,INSERT,UPDATE,DELETE ON TABLE analise.parecer_diretor_tecnico TO licenciamento_am;
GRANT ALL ON TABLE analise.parecer_diretor_tecnico TO postgres;
GRANT SELECT,USAGE ON SEQUENCE analise.parecer_diretor_tecnico_id_seq TO licenciamento_am;
COMMENT ON TABLE analise.parecer_diretor_tecnico IS 'Entidade responsável por armazenar informações sobre o parecer do diretor técnico';
COMMENT ON COLUMN analise.parecer_diretor_tecnico.id IS 'Identificador do parecer do diretor técnico';
COMMENT ON COLUMN analise.parecer_diretor_tecnico.id_tipo_resultado_analise IS 'Tipo do resultado da análise';
COMMENT ON COLUMN analise.parecer_diretor_tecnico.parecer IS 'Descrição do parecer feito pelo diretor técnico';
COMMENT ON COLUMN analise.parecer_diretor_tecnico.data_parecer IS 'Data do parecer';
COMMENT ON COLUMN analise.parecer_diretor_tecnico.id_usuario_diretor IS 'Identificador do diretor técnico responsável pelo parecer';
COMMENT ON COLUMN analise.parecer_diretor_tecnico.id_analise IS 'Identificador da análise que teve parecer do diretor técnico';
COMMENT ON COLUMN analise.parecer_diretor_tecnico.id_historico_tramitacao IS 'Identificador do histórico da tramitação';


INSERT INTO analise.tipo_resultado_analise(id,nome) VALUES
(7,'Solicitação desvínculo'),
(8,'Análise aprovada'),
(9,'Análise não aprovada');


CREATE TABLE analise.presidente
(
  id serial NOT NULL,
  id_analise_tecnica integer,
  id_usuario integer NOT NULL,
  data_vinculacao timestamp without time zone,
  id_analise_geo integer,
  id_analise integer,
  CONSTRAINT pk_presidente PRIMARY KEY (id),
  CONSTRAINT fk_p_analise_geo FOREIGN KEY (id_analise_geo)
      REFERENCES analise.analise_geo (id),
  CONSTRAINT fk_p_usuario_analise FOREIGN KEY (id_usuario)
      REFERENCES analise.usuario_analise (id),
  CONSTRAINT fk_p_analise_tecnica FOREIGN KEY (id_analise_tecnica)
      REFERENCES analise.analise_tecnica (id),
  CONSTRAINT fk_p_analise FOREIGN KEY (id_analise) 
  	  REFERENCES analise.analise (id)
);
ALTER TABLE analise.presidente OWNER TO postgres;
GRANT ALL ON TABLE analise.presidente TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.presidente TO licenciamento_am;
ALTER TABLE  analise.presidente OWNER TO licenciamento_am;
GRANT SELECT,USAGE ON SEQUENCE  analise.presidente_id_seq TO licenciamento_am;
COMMENT ON TABLE analise.presidente IS 'Entidade responsável por armazenar o Presidente.';
COMMENT ON COLUMN analise.presidente.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.presidente.id_analise_tecnica IS 'Identificador da entidade analise_tecnica que realiza o relacionamento entre as entidades presidente e analise_tecnica.';
COMMENT ON COLUMN analise.presidente.id_usuario IS 'Identificador da entidade analise.usuario_analise que realiza o relacionamento entre as entidades presidente e analise.usuario_analise.';
COMMENT ON COLUMN analise.presidente.data_vinculacao IS 'Data em que o usuário foi vinculado a análise técnica.';
COMMENT ON COLUMN analise.presidente.id_analise_geo IS 'Identificador da entidade analise_geo que realiza o relacionamento entre as entidades presidente e analise_geo.';
COMMENT ON COLUMN analise.presidente.id_analise IS 'Identificador da entidade analiseo que realiza o relacionamento entre as entidades presidente e analise.';


ALTER TABLE analise.diretor ADD COLUMN id_analise integer;
COMMENT ON COLUMN analise.diretor.id_analise IS 'Identificador da entidade analise que realiza o relacionamento entre as entidades diretor e analise.';

ALTER TABLE analise.diretor 
	ADD CONSTRAINT fk_d_analise FOREIGN KEY (id_analise) 
		REFERENCES analise.analise (id);



# --- !Downs

ALTER TABLE analise.diretor DROP CONSTRAINT fk_d_analise;
ALTER TABLE analise.diretor DROP COLUMN id_analise;

DROP TABLE analise.presidente;

DELETE FROM analise.tipo_resultado_analise WHERE id IN (7,8,9);

DROP TABLE analise.parecer_diretor_tecnico;



