# --- !Ups

CREATE TABLE analise.desvinculo
(
    id               serial  NOT NULL,
    id_processo      integer NOT NULL,
    id_analista      integer NOT NULL,
    justificativa    text    NOT NULL,
    resposta_gerente text,
    aprovada         boolean,
    id_gerente       integer,
    data_solicitacao timestamp WITHOUT TIME ZONE,
    data_resposta    timestamp WITHOUT TIME ZONE,

    CONSTRAINT pk_desvinculo PRIMARY KEY (id),

    CONSTRAINT fk_d_processo FOREIGN KEY (id_processo)
        REFERENCES analise.processo (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,

    CONSTRAINT fk_d__analista_usuario_analise FOREIGN KEY (id_analista)
        REFERENCES analise.usuario_analise (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,

    CONSTRAINT fk_d_gerente_usuario_analise FOREIGN KEY (id_gerente)
        REFERENCES analise.usuario_analise (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE analise.desvinculo OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.desvinculo TO licenciamento_am;
GRANT SELECT, USAGE ON SEQUENCE analise.desvinculo_id_seq TO licenciamento_am;

COMMENT ON TABLE analise.desvinculo  IS 'Entidade responsável por armazenar o analista da analise geo.';
COMMENT ON COLUMN analise.desvinculo.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.desvinculo.id_processo IS 'Identificador da entidade processo que faz o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.desvinculo.id_analista IS 'Identificador da entidade usuario que faz o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.desvinculo.justificativa IS 'Campo para armazenar a justificativa da solicitação de desvínculo do analista.';
COMMENT ON COLUMN analise.desvinculo.resposta_gerente IS 'Campo para armazenar a resposta digitada pelo gerente.';
COMMENT ON COLUMN analise.desvinculo.aprovada IS 'Flag de controle para saber o status da solicitação de desvínculo.';
COMMENT ON COLUMN analise.desvinculo.id_gerente IS 'Identificador único da entidade gerentes que faz o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.desvinculo.data_solicitacao IS 'Data em que foi solicitado o desvínculo por um analista.';
COMMENT ON COLUMN analise.desvinculo.data_resposta IS 'Data em que o gerente responde o desvínculo.';

# --- !Downs

DROP TABLE analise.desvinculo;
