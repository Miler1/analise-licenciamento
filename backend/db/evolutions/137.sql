
# --- !Ups

CREATE TABLE analise.parecer_presidente
(
  id serial NOT NULL,
  id_tipo_resultado_analise integer NOT NULL,
  parecer text NOT NULL,
  data_parecer timestamp without time zone DEFAULT now(),
  id_usuario_presidente integer NOT NULL,
  id_analise integer NOT NULL,
  id_historico_tramitacao integer NOT NULL,
  CONSTRAINT pk_parecer_presidente PRIMARY KEY (id),
  CONSTRAINT fk_pp_analise FOREIGN KEY (id_analise)
      REFERENCES analise.analise (id) ,
  CONSTRAINT fk_pp_tipo_resultado_analise FOREIGN KEY (id_tipo_resultado_analise)
      REFERENCES analise.tipo_resultado_analise (id),
  CONSTRAINT fk_pp_usuario_analise FOREIGN KEY (id_usuario_presidente)
      REFERENCES analise.usuario_analise (id)
);
ALTER TABLE analise.parecer_presidente OWNER TO postgres;
ALTER TABLE analise.parecer_presidente OWNER TO licenciamento_am;
GRANT SELECT,INSERT,UPDATE,DELETE ON TABLE analise.parecer_presidente TO licenciamento_am;
GRANT ALL ON TABLE analise.parecer_presidente TO postgres;
GRANT SELECT,USAGE ON SEQUENCE analise.parecer_presidente_id_seq TO licenciamento_am;
COMMENT ON TABLE analise.parecer_presidente IS 'Entidade responsável por armazenar informações sobre o parecer do presidente';
COMMENT ON COLUMN analise.parecer_presidente.id IS 'Identificador do parecer do presidente';
COMMENT ON COLUMN analise.parecer_presidente.id_tipo_resultado_analise IS 'Tipo do resultado da análise';
COMMENT ON COLUMN analise.parecer_presidente.parecer IS 'Descrição do parecer feito pelo presidente';
COMMENT ON COLUMN analise.parecer_presidente.data_parecer IS 'Data do parecer';
COMMENT ON COLUMN analise.parecer_presidente.id_usuario_presidente IS 'Identificador do presidente responsável pelo parecer';
COMMENT ON COLUMN analise.parecer_presidente.id_analise IS 'Identificador da análise que teve parecer do presidente';
COMMENT ON COLUMN analise.parecer_presidente.id_historico_tramitacao IS 'Identificador do histórico da tramitação';

# --- !Downs

DROP TABLE analise.parecer_presidente;

