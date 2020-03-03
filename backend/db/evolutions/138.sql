
# --- !Ups

INSERT INTO analise.tipo_documento(id, nome, caminho_pasta, prefixo_nome_arquivo) VALUES
(32, 'Documento jurídico', 'documento_juridico', 'documento_juridico' );

INSERT INTO analise.tipo_resultado_analise(id,nome) VALUES
(10,'Apto'),
(11,'Não Apto');


CREATE TABLE analise.parecer_juridico (
    id SERIAL NOT NULL,
    id_analise_geo INTEGER,
    data_cadastro TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    data_resposta TIMESTAMP WITHOUT TIME ZONE,
    parecer TEXT,
    resolvido BOOLEAN NOT NULL DEFAULT FALSE,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    id_parecer_analista_geo INTEGER NOT NULL,
    id_tipo_resultado_validacao_juridica INTEGER,
    id_historico_tramitacao INTEGER,
    id_analise_tecnica INTEGER NOT NULL,
    id_documento_fundiario INTEGER NOT NULL,
    CONSTRAINT pk_parecer_juridico PRIMARY KEY (id),
    CONSTRAINT fk_pj_parecer_analista_geo FOREIGN KEY (id_parecer_analista_geo)
        REFERENCES analise.parecer_analista_geo(id),
    CONSTRAINT fk_pj_tipo_resultado_validacao_juridica FOREIGN KEY (id_tipo_resultado_validacao_juridica)
        REFERENCES analise.tipo_resultado_analise (id),
    CONSTRAINT fk_pj_analise_tecnica FOREIGN KEY (id_analise_tecnica)
        REFERENCES analise.analise_tecnica(id),
    CONSTRAINT fk_pj_documento_fundiario_licenciamento FOREIGN KEY (id_documento_fundiario)
        REFERENCES licenciamento.documento(id),
    CONSTRAINT fk_pj_analise_geo FOREIGN KEY (id_analise_geo)
        REFERENCES analise.analise_geo (id)
);
ALTER TABLE analise.parecer_juridico OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.parecer_juridico TO licenciamento_am;
GRANT SELECT, USAGE ON SEQUENCE analise.parecer_juridico_id_seq TO licenciamento_am;
COMMENT ON TABLE analise.parecer_juridico IS 'Entidade responsável por armazenar os dados referentes ao parecer jurídico que será enviado ao setor jurídico responsável.';
COMMENT ON COLUMN analise.parecer_juridico.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.parecer_juridico.id_analise_geo IS 'Identificador da tabela analise_geo, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.parecer_juridico.data_cadastro IS 'Identificador da tabela documento do schema licenciamento, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.parecer_juridico.data_resposta IS 'Campo responsável por armazenar a data em que a notificação foi criada.';
COMMENT ON COLUMN analise.parecer_juridico.parecer IS 'Campo responsável por armazenar a resposta do parecer jurídico.';
COMMENT ON COLUMN analise.parecer_juridico.resolvido IS 'Flag que indica se o parecer foi resolvido.';
COMMENT ON COLUMN analise.parecer_juridico.ativo IS ' Flag que indica se o parecer está ativo.';
COMMENT ON COLUMN analise.parecer_juridico.id_parecer_analista_geo IS 'Identificador da tabela parecer_analista_geo, responśavel pelo relacionamento entre as duas tabelas';
COMMENT ON COLUMN analise.parecer_juridico.id_tipo_resultado_validacao_juridica IS 'Identificador da tabela tipo_resultado, responśavel pelo relacionamento entre as duas tabelas';
COMMENT ON COLUMN analise.parecer_juridico.id_historico_tramitacao IS 'Identificador da tramitação gerada pelo parecer.';
COMMENT ON COLUMN analise.parecer_juridico.id_analise_tecnica IS 'Identificador da tabela analise_tecnica, responśavel pelo relacionamento entre as duas tabelas';
COMMENT ON COLUMN analise.parecer_juridico.id_documento_fundiario IS 'Identificador da tabela documento, no schema licenciamento, responśavel pelo relacionamento entre as duas tabelas';


CREATE TABLE analise.rel_documento_juridico (
    id_documento  INTEGER NOT NULL,
    id_parecer_juridico INTEGER NOT NULL,
    CONSTRAINT pk_rel_documento_juridico PRIMARY KEY (id_documento, id_parecer_juridico),
    CONSTRAINT fk_rdj_documento FOREIGN KEY (id_documento)
        REFERENCES analise.documento (id),
    CONSTRAINT fk_rdj_comunicado FOREIGN KEY (id_parecer_juridico)
        REFERENCES analise.parecer_juridico (id)
);
ALTER TABLE analise.rel_documento_juridico OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.rel_documento_juridico TO licenciamento_am;
COMMENT ON TABLE analise.rel_documento_juridico IS 'Entidade que relaciona a Entidade Documento e a Entidade Parecer Juridico';
COMMENT ON COLUMN analise.parecer_juridico.id_documento IS 'Identificador da tabela documento, responśavel pelo relacionamento entre as duas tabelas';
COMMENT ON COLUMN analise.parecer_juridico.id_parecer_juridico IS 'Identificador da tabela parecer_juridico, responśavel pelo relacionamento entre as duas tabelas';


# --- !Downs

DROP TABLE analise.rel_documento_juridico ;

DROP TABLE analise.parecer_juridico ;

DELETE FROM analise.tipo_resultado_analise WHERE id IN (10, 11);

DELETE FROM analise.tipo_documento WHERE id = 32;