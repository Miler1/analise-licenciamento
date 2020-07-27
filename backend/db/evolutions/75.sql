# --- !Ups

DROP TABLE analise.dispensa_licencamento_cancelada;

# --- !Downs

CREATE TABLE analise.dispensa_licencamento_cancelada
(
    id                       SERIAL                      NOT NULL,
    id_dispensa_licencamento INTEGER                     NOT NULL,
    justificativa            TEXT,
    id_usuario_executor      INTEGER                     NOT NULL,
    data_cancelamento        TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    CONSTRAINT pk_dispensa_licencamento_cancelada PRIMARY KEY (id),

    CONSTRAINT dispensa_licencamento_cancelada_id_dispensa_licencamento_key UNIQUE (id_dispensa_licencamento),
    
    CONSTRAINT fk_dlc_usuario_analise_executor FOREIGN KEY (id_usuario_executor)
        REFERENCES analise.usuario_analise (id)
);

ALTER TABLE analise.dispensa_licencamento_cancelada OWNER TO postgres;

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE analise.dispensa_licencamento_cancelada TO licenciamento_am;
GRANT SELECT, USAGE ON SEQUENCE analise.dispensa_licencamento_cancelada_id_seq TO licenciamento_am;

COMMENT ON TABLE analise.dispensa_licencamento_cancelada IS 'Entidade responsável por armazenar as Dispensas de licenciamento Ambiental canceladas.';
COMMENT ON COLUMN analise.dispensa_licencamento_cancelada.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.dispensa_licencamento_cancelada.id_dispensa_licencamento IS 'Identificador da entidade licenciamento.dispensa_licenciamento que realiza o relacionamento entre as entidades dispensa_licencamento_cancelada e licenciamento.dispensa_licenciamento.';
COMMENT ON COLUMN analise.dispensa_licencamento_cancelada.justificativa IS 'Justificativa do cancelamento da Dispensa de licenciamento Ambiental.';
COMMENT ON COLUMN analise.dispensa_licencamento_cancelada.id_usuario_executor IS 'Identificador da entidade analise.usuario_analise que realiza o relacionamento entre as entidades dispensa_licencamento_cancelada e portal_analise.usuario_analise identifica o usuário executor da ação.';
COMMENT ON COLUMN analise.dispensa_licencamento_cancelada.data_cancelamento IS 'Data do cancelamento da Dispensa de licenciamento Ambiental.';

