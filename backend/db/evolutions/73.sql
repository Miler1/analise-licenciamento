# --- !Ups

CREATE TABLE analise.rel_documento_analise_geo
(
    id_documento   INTEGER NOT NULL,
    id_analise_geo INTEGER NOT NULL,

    CONSTRAINT pk_rel_documento_analise_geo
        PRIMARY KEY (id_documento, id_analise_geo),

    CONSTRAINT fk_rdag_documento FOREIGN KEY (id_documento)
        REFERENCES analise.documento (id),

    CONSTRAINT fk_rdag_analise_geo FOREIGN KEY (id_analise_geo)
        REFERENCES analise.analise_geo (id)
);

ALTER TABLE analise.rel_documento_analise_geo OWNER TO postgres;
GRANT INSERT, SELECT,UPDATE,DELETE ON TABLE analise.rel_documento_analise_geo TO licenciamento_am;

COMMENT ON TABLE analise.rel_documento_analise_juridica is 'Entidade responsável por armazenar a relação entre as entidades documento e análise geo.';
COMMENT ON COLUMN analise.rel_documento_analise_juridica.id_documento is 'Identificador da tabela documento, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.rel_documento_analise_juridica.id_analise_juridica is 'Identificador da tabela analise_geo, responsável pelo relacionamento entre as duas tabelas.';

# --- !Downs

DROP TABLE analise.rel_documento_analise_geo;

