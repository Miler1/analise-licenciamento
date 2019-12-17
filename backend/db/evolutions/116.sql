
# --- !Ups

CREATE TABLE analise.parecer_analista_tecnico (
    id integer NOT NULL,
    id_analise_tecnica integer NOT NULL,
    do_processo text,
    da_analise_tecnica text,
    da_conclusao text,
    data timestamp,
    id_tipo_resultado_analise integer,
    id_usuario_analista_tecnico integer,
    CONSTRAINT pk_parecer_analista_tecnico PRIMARY KEY (id),
    CONSTRAINT fk_pat_analise_tecnica FOREIGN KEY (id_analise_tecnica)
        REFERENCES analise.analise_tecnica (id),
    CONSTRAINT fk_pat_tipo_resultado_analise FOREIGN KEY (id_tipo_resultado_analise)
        REFERENCES analise.tipo_resultado_analise (id),
     CONSTRAINT fk_pat_usuario_analise FOREIGN KEY (id_usuario_analista_tecnico)
        REFERENCES analise.usuario_analise (id)
);
ALTER TABLE analise.parecer_analista_tecnico OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.parecer_analista_tecnico TO licenciamento_am;
COMMENT ON TABLE analise.parecer_analista_tecnico IS 'Entidade responsável por armazenar os pareceres dos analistas técnicos';
COMMENT ON COLUMN analise.parecer_analista_tecnico.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.parecer_analista_tecnico.id_analise_tecnica IS 'Identificador da análise técnica ligada ao parecer';
COMMENT ON COLUMN analise.parecer_analista_tecnico.do_processo IS 'Texto do parecer';
COMMENT ON COLUMN analise.parecer_analista_tecnico.da_analise_tecnica IS 'Texto do parecer';
COMMENT ON COLUMN analise.parecer_analista_tecnico.da_conclusao IS 'Texto do parecer';
COMMENT ON COLUMN analise.parecer_analista_tecnico.data IS 'Data do parecer tramitado';
COMMENT ON COLUMN analise.parecer_analista_tecnico.id_tipo_resultado_analise IS 'Tipo do resultado do parecer tramitado';
COMMENT ON COLUMN analise.parecer_analista_tecnico.id_usuario_analista_tecnico IS 'Identificador do usuário analista técnico que realizou o parecer';



CREATE TABLE analise.rel_documento_parecer_analista_tecnico (
    id_parecer_analista_tecnico integer NOT NULL,
    id_documento integer NOT NULL,
    CONSTRAINT fk_rdpat_parecer_analista_tecnico FOREIGN KEY (id_parecer_analista_tecnico)
        REFERENCES analise.parecer_analista_tecnico (id),
    CONSTRAINT fk_rdpat_documento FOREIGN KEY (id_documento)
        REFERENCES analise.documento (id)
);
ALTER TABLE  analise.rel_documento_parecer_analista_tecnico OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE  analise.rel_documento_parecer_analista_tecnico TO licenciamento_am;
COMMENT ON TABLE analise.rel_documento_parecer_analista_tecnico IS 'Entidade responsável por armazenar a relação dos documentos de um parecer de analista técnico';
COMMENT ON COLUMN analise.rel_documento_parecer_analista_tecnico.id_parecer_analista_tecnico IS 'Identificador do parecer';
COMMENT ON COLUMN analise.rel_documento_parecer_analista_tecnico.id_documento IS 'Identificador do documento relacionado ao parecer';



# --- !Downs

DROP TABLE analise.rel_documento_parecer_analista_tecnico;
DROP TABLE analise.parecer_analista_tecnico;
