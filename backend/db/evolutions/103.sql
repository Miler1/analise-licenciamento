
# --- !Ups

ALTER TABLE analise.parecer_gerente_analise_geo ADD COLUMN id_historico_tramitacao integer NOT NULL;
COMMENT ON COLUMN analise.parecer_gerente_analise_geo.id_historico_tramitacao IS 'Identificador da tramitação gerada pelo parecer.';

ALTER TABLE analise.analise_geo DROP COLUMN parecer, DROP COLUMN id_tipo_resultado_analise, DROP COLUMN situacao_fundiaria, DROP COLUMN analise_temporal, DROP COLUMN despacho_analista;

CREATE TABLE analise.parecer_analista_geo (
    id integer NOT NULL,
    id_analise_geo integer NOT NULL,
    id_tipo_resultado_analise integer NOT NULL,
    parecer text NOT NULL,
    data_parecer timestamp default now(),
    id_usuario_analista_geo integer NOT NULL,
    id_historico_tramitacao integer NOT NULL,
    situacao_fundiaria TEXT,
    analise_temporal TEXT,
    conclusao TEXT NOT NULL,
    CONSTRAINT pk_parecer_analista_geo PRIMARY KEY(id),
    CONSTRAINT fk_pag_analise_geo FOREIGN KEY(id_analise_geo)
        REFERENCES analise.analise_geo, 
    CONSTRAINT fk_pag_tipo_resultado_analise FOREIGN KEY(id_tipo_resultado_analise)
        REFERENCES analise.tipo_resultado_analise,
    CONSTRAINT fk_pag_usuario_analise FOREIGN KEY(id_usuario_analista_geo)
        REFERENCES analise.usuario_analise    
);
ALTER TABLE analise.parecer_analista_geo OWNER TO postgres;
ALTER TABLE analise.parecer_analista_geo OWNER TO licenciamento_am;
GRANT SELECT,INSERT,UPDATE,DELETE ON TABLE analise.parecer_analista_geo TO licenciamento_am;
GRANT ALL ON TABLE analise.parecer_analista_geo TO postgres;
COMMENT ON TABLE analise.parecer_analista_geo IS 'Entidade responsável por armazenar informações sobre o parecer do analista geo em uma análise técnica';
COMMENT ON COLUMN analise.parecer_analista_geo.id IS 'Id do parecer do analista';
COMMENT ON COLUMN analise.parecer_analista_geo.id_analise_geo IS 'Id da analise';
COMMENT ON COLUMN analise.parecer_analista_geo.id_tipo_resultado_analise IS 'Tipo do resultado da análise';
COMMENT ON COLUMN analise.parecer_analista_geo.parecer IS 'Descrição do parecer feito pelo analista';
COMMENT ON COLUMN analise.parecer_analista_geo.data_parecer IS 'Data do parecer';
COMMENT ON COLUMN analise.parecer_analista_geo.id_usuario_analista_geo IS 'Id do analista geo responsável pelo parecer';
COMMENT ON COLUMN analise.parecer_analista_geo.id_historico_tramitacao IS 'Identificador da tramitação gerada pelo parecer.';
COMMENT ON COLUMN analise.parecer_analista_geo.situacao_fundiaria IS 'Situação fundiária do empreendimento da análise GEO.';
COMMENT ON COLUMN analise.parecer_analista_geo.analise_temporal IS 'Análise temporal do empreendimento da análise GEO';
COMMENT ON COLUMN analise.parecer_analista_geo.conclusao IS 'Notas de conclusão da análise.';


CREATE SEQUENCE analise.parecer_analista_geo_id_seq 
    INCREMENT 1
    MINVALUE 1
    NO MAXVALUE
    START 1
    CACHE 1;
ALTER TABLE analise.parecer_analista_geo ALTER COLUMN id SET DEFAULT pg_catalog.nextval('analise.parecer_analista_geo_id_seq');
ALTER TABLE analise.parecer_analista_geo_id_seq OWNER TO postgres;
GRANT SELECT, USAGE ON SEQUENCE analise.parecer_analista_geo_id_seq TO licenciamento_am; 
SELECT setval('analise.parecer_analista_geo_id_seq', coalesce(max(id), 1)) FROM analise.parecer_analista_geo;   


# --- !Downs

ALTER TABLE analise.parecer_analista_geo ALTER COLUMN id DROP DEFAULT; 
DROP SEQUENCE analise.parecer_analista_geo_id_seq;

DROP TABLE analise.parecer_analista_geo;

ALTER TABLE analise.analise_geo ADD COLUMN parecer text;
ALTER TABLE analise.analise_geo ADD COLUMN id_tipo_resultado_analise integer;
ALTER TABLE analise.analise_geo ADD COLUMN situacao_fundiaria text;
ALTER TABLE analise.analise_geo ADD COLUMN analise_temporal text;
ALTER TABLE analise.analise_geo ADD COLUMN despacho_analista text;
ALTER TABLE analise.analise_geo ADD CONSTRAINT fk_ag_tipo_resultado_analise FOREIGN KEY (id_tipo_resultado_analise)
      REFERENCES analise.tipo_resultado_analise (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
COMMENT ON COLUMN analise.analise_geo.parecer IS 'Parecer da análise geo.';
COMMENT ON COLUMN analise.analise_geo.id_tipo_resultado_analise IS 'Campo responsavel por armazenar o resultado da análise do analista';
COMMENT ON COLUMN analise.analise_geo.situacao_fundiaria IS 'Situação fundiária do empreendimento da análise GEO';
COMMENT ON COLUMN analise.analise_geo.analise_temporal IS 'Análise temporal do empreendimento da análise GEO';
COMMENT ON COLUMN analise.analise_geo.despacho_analista IS 'Campo responsavel por armazenar o despacho/justificativa do analista GEO';

ALTER TABLE analise.parecer_analista_geo DROP COLUMN situacao_fundiaria;
ALTER TABLE analise.parecer_analista_geo DROP COLUMN analise_temporal;
ALTER TABLE analise.parecer_analista_geo DROP COLUMN conclusao;

ALTER TABLE analise.parecer_gerente_analise_geo DROP COLUMN id_historico_tramitacao ;

