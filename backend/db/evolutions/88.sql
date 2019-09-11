# --- !Ups

CREATE TABLE analise.rel_documento_comunicado
(
    id_documento  INTEGER NOT NULL,
    id_comunicado INTEGER NOT NULL,

    CONSTRAINT pk_rel_documento_comunicado PRIMARY KEY (id_documento, id_comunicado),

    CONSTRAINT fk_rdc_documento FOREIGN KEY (id_documento)
        REFERENCES analise.documento (id),

    CONSTRAINT fk_rdc_comunicado FOREIGN KEY (id_comunicado)
        REFERENCES analise.comunicado (id)
);

ALTER TABLE analise.rel_documento_comunicado OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.rel_documento_comunicado TO licenciamento_am;

COMMENT ON TABLE analise.rel_documento_comunicado IS 'Entidade que relaciona a Entidade Documento e a Entidade Comunicado';
COMMENT ON COLUMN analise.rel_documento_comunicado.id_documento IS 'Entidade que relaciona a Entidade Documento e a Entidade Comunicado';
COMMENT ON COLUMN analise.rel_documento_comunicado.id_comunicado IS 'Entidade que relaciona a Entidade Documento e a Entidade Comunicado';

INSERT INTO analise.tipo_documento(id,nome,caminho_modelo,caminho_pasta,prefixo_nome_arquivo)
VALUES (20,'Documento comunicado órgão', NULL, 'documento_comunicado', 'documento_comunicado');

# --- !Downs

DROP TABLE analise.rel_documento_comunicado;
DELETE FROM analise.tipo_documento WHERE id = 20;