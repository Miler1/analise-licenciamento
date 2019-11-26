
# --- !Ups

ALTER TABLE analise.analise_geo DROP COLUMN id_tipo_resultado_validacao_gerente, DROP COLUMN parecer_validacao_gerente, DROP COLUMN id_usuario_validacao_gerente;

CREATE TABLE analise.parecer_gerente_analise_geo (
    id integer NOT NULL,
    id_tipo_resultado_analise integer NOT NULL,
    parecer text NOT NULL,
    data_parecer timestamp default now(),
    id_usuario_gerente integer NOT NULL,
    id_analise_geo integer NOT NULL,
    CONSTRAINT pk_parecer_gerente_analise_geo PRIMARY KEY(id),
    CONSTRAINT fk_pgag_tipo_resultado_analise FOREIGN KEY(id_tipo_resultado_analise) 
        REFERENCES  analise.tipo_resultado_analise,
    CONSTRAINT fk_pgag_usuario_analise FOREIGN KEY(id_usuario_gerente) 
        REFERENCES  analise.usuario_analise,
    CONSTRAINT fk_pgag_analise_geo FOREIGN KEY(id_analise_geo) 
        REFERENCES  analise.analise_geo
);
ALTER TABLE analise.parecer_gerente_analise_geo OWNER TO postgres;
ALTER TABLE analise.parecer_gerente_analise_geo OWNER TO licenciamento_am;
COMMENT ON TABLE analise.parecer_gerente_analise_geo is 'Entidade responsável por armazenar informações sobre o parecer do gerente em uma análise técnica';
COMMENT ON COLUMN analise.parecer_gerente_analise_geo.id is 'Id do parecer do gerente';
COMMENT ON COLUMN analise.parecer_gerente_analise_geo.id_tipo_resultado_analise is 'Tipo do resultado da análise';
COMMENT ON COLUMN analise.parecer_gerente_analise_geo.parecer is 'Descrição do parecer feito pelo gerente';
COMMENT ON COLUMN analise.parecer_gerente_analise_geo.data_parecer is 'Data do parecer';
COMMENT ON COLUMN analise.parecer_gerente_analise_geo.id_usuario_gerente is 'Id do gerente responsável pelo parecer';
COMMENT ON COLUMN analise.parecer_gerente_analise_geo.id_analise_geo is 'Id da análise geo que teve parecer';

CREATE SEQUENCE analise.parecer_gerente_analise_geo_id_seq 
    INCREMENT 1
    MINVALUE 1
    NO MAXVALUE
    START 1
    CACHE 1;
ALTER TABLE analise.parecer_gerente_analise_geo ALTER COLUMN id SET DEFAULT pg_catalog.nextval('analise.parecer_gerente_analise_geo_id_seq');
ALTER TABLE analise.parecer_gerente_analise_geo_id_seq OWNER TO postgres;
GRANT SELECT, USAGE ON SEQUENCE analise.parecer_gerente_analise_geo_id_seq TO licenciamento_am; 
SELECT setval('analise.parecer_gerente_analise_geo_id_seq', coalesce(max(id), 1)) FROM analise.parecer_gerente_analise_geo;


# --- !Downs

ALTER TABLE analise.parecer_gerente_analise_geo ALTER COLUMN id DROP DEFAULT; 
DROP SEQUENCE analise.parecer_gerente_analise_geo_id_seq;

DROP TABLE analise.parecer_gerente_analise_geo;

ALTER TABLE analise.analise_geo ADD COLUMN id_tipo_resultado_validacao_gerente integer;
ALTER TABLE analise.analise_geo ADD COLUMN parecer_validacao_gerente text;
ALTER TABLE analise.analise_geo ADD COLUMN id_usuario_validacao_gerente integer;
ALTER TABLE analise.analise_geo ADD CONSTRAINT fk_ag_tipo_resultado_validacao_gerente FOREIGN KEY (id_tipo_resultado_validacao_gerente)
    REFERENCES analise.tipo_resultado_analise (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE analise.analise_geo ADD CONSTRAINT fk_ag_usuario_analise_validacao_gerente FOREIGN KEY (id_usuario_validacao_gerente)
    REFERENCES analise.usuario_analise (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;
COMMENT ON COLUMN analise.analise_geo.id_tipo_resultado_validacao_gerente IS 'Campo responsavel por armazenar o resultado da análise do gerente geo.';
COMMENT ON COLUMN analise.analise_geo.parecer_validacao_gerente IS 'Campo responsável por armazenar a descrição da validação do gerente geo.';
COMMENT ON COLUMN analise.analise_geo.id_usuario_validacao_gerente IS 'Campo responsável por armazernar o gerente que fez a validação.';

