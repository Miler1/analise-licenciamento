# --- !Ups

CREATE TABLE analise.inconsistencia
(
    id                       serial  NOT NULL,
    id_analise_geo           integer NOT NULL,
    descricao_inconsistencia text    NOT NULL,
    tipo_inconsistencia      text    NOT NULL,
    categoria                text    NOT NULL,

    CONSTRAINT pk_inconsistencia PRIMARY KEY (id),

    CONSTRAINT fk_i_analise_geo FOREIGN KEY (id_analise_geo)
        REFERENCES analise.analise_geo (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE analise.inconsistencia OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.inconsistencia TO licenciamento_am;
GRANT SELECT ON TABLE analise.inconsistencia TO tramitacao;

COMMENT ON TABLE analise.inconsistencia IS 'Entidade responsável por armazenar as análises geo.';
COMMENT ON COLUMN analise.inconsistencia.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.inconsistencia.id_analise_geo IS 'Identificador da tabela analise, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.inconsistencia.descricao_inconsistencia IS 'Campo que armazena a descrição da inconsistência relatada pelo analista.';
COMMENT ON COLUMN analise.inconsistencia.tipo_inconsistencia IS 'Campo que armazena o tipo da inconsistência presente na análise.';


CREATE TABLE analise.rel_documento_inconsistencia
(
    id_documento      integer NOT NULL,
    id_inconsistencia integer NOT NULL,

    CONSTRAINT pk_rel_documento_inconsistencia PRIMARY KEY (id_documento, id_inconsistencia),
    CONSTRAINT fk_rdi_inconsistencia FOREIGN KEY (id_inconsistencia)
        REFERENCES analise.inconsistencia (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,

    CONSTRAINT fk_rdi_documento FOREIGN KEY (id_documento)
        REFERENCES analise.documento (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE analise.rel_documento_inconsistencia OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.rel_documento_inconsistencia TO licenciamento_am;
GRANT SELECT ON TABLE analise.rel_documento_inconsistencia TO tramitacao;

COMMENT ON TABLE analise.rel_documento_inconsistencia IS 'Entidade que armazena a relação dos documentos com a análise GEO.';
COMMENT ON COLUMN analise.rel_documento_inconsistencia.id_documento IS 'Identificador do documento.';
COMMENT ON COLUMN analise.rel_documento_inconsistencia.id_inconsistencia IS 'Identificador da inconsistência';

# --- !Downs

DROP TABLE analise.rel_documento_inconsistencia;

DROP TABLE analise.inconsistencia;