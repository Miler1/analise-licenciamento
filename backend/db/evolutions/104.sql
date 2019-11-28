
# --- !Ups
CREATE TABLE analise.rel_documento_parecer_analista_geo (
    id_documento integer NOT NULL,
    id_parecer_analista_geo integer NOT NULL,
    CONSTRAINT fk_rdpag_documento FOREIGN KEY(id_documento)
        REFERENCES analise.documento(id),
    CONSTRAINT fk_rdpag_parecer_analista_geo FOREIGN KEY(id_parecer_analista_geo)
        REFERENCES analise.parecer_analista_geo(id)
);
ALTER TABLE analise.rel_documento_parecer_analista_geo OWNER TO postgres;
ALTER TABLE analise.rel_documento_parecer_analista_geo OWNER TO licenciamento_am;
GRANT SELECT,INSERT,UPDATE,DELETE ON TABLE analise.rel_documento_parecer_analista_geo TO licenciamento_am;
GRANT ALL ON TABLE analise.rel_documento_parecer_analista_geo TO postgres;
COMMENT ON TABLE analise.rel_documento_parecer_analista_geo IS 'Entidade responsável por relacionar documentos de um parecer de um analista geo';
COMMENT ON COLUMN analise.rel_documento_parecer_analista_geo.id_documento IS 'Identificador do documento';
COMMENT ON COLUMN analise.rel_documento_parecer_analista_geo.id_parecer_analista_geo IS 'Identificador do parecer do analista geo';


# --- !Downs

DROP TABLE analise.rel_documento_parecer_analista_geo;
