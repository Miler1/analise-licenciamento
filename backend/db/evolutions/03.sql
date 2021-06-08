# --- !Ups

CREATE TABLE analise.coordenador_geo (
    id serial NOT NULL,
    id_usuario integer NOT NULL,
    data_vinculacao timestamp,
    id_analise_geo integer,
    CONSTRAINT pk_coordenador_geo PRIMARY KEY (id),
    CONSTRAINT fk_cg_usuario_analise FOREIGN KEY (id_usuario)
        REFERENCES analise.usuario_analise (id),
    CONSTRAINT fk_cg_analise_geo FOREIGN KEY (id_analise_geo)
        REFERENCES analise.analise_geo (id)
);
GRANT SELECT, USAGE ON SEQUENCE analise.coordenador_geo_id_seq TO licenciamento_ap;
GRANT INSERT, SELECT, UPDATE, DELETE ON analise.coordenador_geo TO licenciamento_ap;
ALTER TABLE analise.coordenador_geo owner TO postgres;
COMMENT ON TABLE analise.coordenador_geo IS 'Entidade responsável por armazenar o Gerente responsável pela análise geo.';
COMMENT ON COLUMN analise.coordenador_geo.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.coordenador_geo.id_usuario IS 'Identificador da entidade analise.usuario_analise que realiza o relacionamento entre as entidades coordenador_geo e analise.usuario_analise.';
COMMENT ON COLUMN analise.coordenador_geo.data_vinculacao IS 'Data em que o usuário foi vinculado a análise.';
COMMENT ON COLUMN analise.coordenador_geo.id_analise_geo IS 'Identificador da entidade analise_geo que realiza o relacionamento entre as entidades coordenador_geo e analise_geo.';

CREATE TABLE analise.parecer_coordenador_geo_analise_geo (
    id serial NOT NULL,
    id_tipo_resultado_analise integer NOT NULL,
    parecer text NOT NULL,
    data_parecer timestamp default now(),
    id_usuario_coordenador_geo integer NOT NULL,
    id_analise_geo integer NOT NULL,
    id_historico_tramitacao integer NOT NULL,
    CONSTRAINT pk_parecer_coordenador_geo_analise_geo PRIMARY KEY(id),
    CONSTRAINT fk_pcgag_tipo_resultado_analise FOREIGN KEY(id_tipo_resultado_analise)
        REFERENCES  analise.tipo_resultado_analise(id),
    CONSTRAINT fk_pcgag_usuario_analise FOREIGN KEY(id_usuario_coordenador_geo)
        REFERENCES  analise.usuario_analise(id),
    CONSTRAINT fk_pcgag_analise_geo FOREIGN KEY(id_analise_geo)
        REFERENCES  analise.analise_geo(id)
);
GRANT SELECT, USAGE ON SEQUENCE analise.parecer_coordenador_geo_analise_geo_id_seq TO licenciamento_ap;
GRANT INSERT, SELECT, UPDATE, DELETE ON analise.parecer_coordenador_geo_analise_geo TO licenciamento_ap;
ALTER TABLE analise.parecer_coordenador_geo_analise_geo owner TO postgres;
COMMENT ON TABLE analise.parecer_coordenador_geo_analise_geo is 'Entidade responsável por armazenar informações sobre o parecer do coordenador geo em uma análise geo';
COMMENT ON COLUMN analise.parecer_coordenador_geo_analise_geo.id is 'Identificador do parecer do coordenador geo';
COMMENT ON COLUMN analise.parecer_coordenador_geo_analise_geo.id_tipo_resultado_analise is 'Tipo do resultado da análise';
COMMENT ON COLUMN analise.parecer_coordenador_geo_analise_geo.parecer is 'Descrição do parecer feito pelo coordenador geo';
COMMENT ON COLUMN analise.parecer_coordenador_geo_analise_geo.data_parecer is 'Data do parecer';
COMMENT ON COLUMN analise.parecer_coordenador_geo_analise_geo.id_usuario_coordenador_geo is 'Identificador do coordenador geo responsável pelo parecer';
COMMENT ON COLUMN analise.parecer_coordenador_geo_analise_geo.id_analise_geo is 'Identificador da análise geo que teve parecer';
COMMENT ON COLUMN analise.parecer_coordenador_geo_analise_geo.id_historico_tramitacao IS 'Identificador da tramitação gerada pelo parecer.';

# --- !Downs

DROP TABLE analise.parecer_coordenador_geo_analise_geo;

DROP TABLE analise.coordenador_geo;