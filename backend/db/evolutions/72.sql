# --- !Ups

BEGIN;

CREATE TABLE analise.analise_geo
(
    id                                    serial                      NOT NULL,
    id_analise                            integer                     NOT NULL,
    parecer                               text,
    data_vencimento_prazo                 date                        NOT NULL,
    revisao_solicitada                    boolean DEFAULT false,
    ativo                                 boolean DEFAULT true,
    id_analise_geo_revisada               integer,
    data_inicio                           timestamp without time zone,
    data_fim                              timestamp without time zone,
    id_tipo_resultado_analise             integer,
    id_tipo_resultado_validacao           integer,
    parecer_validacao                     text,
    id_usuario_validacao                  integer,
    justificativa_coordenador             text,
    id_tipo_resultado_validacao_gerente   integer,
    parecer_validacao_gerente             text,
    id_usuario_validacao_gerente          integer,
    data_cadastro                         timestamp without time zone NOT NULL,
    id_tipo_resultado_validacao_aprovador integer,
    parecer_validacao_aprovador           text,
    id_usuario_validacao_aprovador        integer,
    data_fim_validacao_aprovador          timestamp without time zone,
    notificacao_atendida                  boolean DEFAULT false,

    CONSTRAINT pk_analise_geo PRIMARY KEY (id),

    CONSTRAINT fk_ag_analise FOREIGN KEY (id_analise)
        REFERENCES analise.analise (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,

    CONSTRAINT fk_ag_analise_geo FOREIGN KEY (id_analise_geo_revisada)
        REFERENCES analise.analise_geo (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,

    CONSTRAINT fk_ag_tipo_resultado_analise FOREIGN KEY (id_tipo_resultado_analise)
        REFERENCES analise.tipo_resultado_analise (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,

    CONSTRAINT fk_ag_tipo_resultado_validacao FOREIGN KEY (id_tipo_resultado_validacao)
        REFERENCES analise.tipo_resultado_analise (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,

    CONSTRAINT fk_ag_tipo_resultado_validacao_aprovador FOREIGN KEY (id_tipo_resultado_validacao_aprovador)
        REFERENCES analise.tipo_resultado_analise (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,

    CONSTRAINT fk_ag_tipo_resultado_validacao_gerente FOREIGN KEY (id_tipo_resultado_validacao_gerente)
        REFERENCES analise.tipo_resultado_analise (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,

    CONSTRAINT fk_ag_usuario_analise FOREIGN KEY (id_usuario_validacao)
        REFERENCES analise.usuario_analise (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,

    CONSTRAINT fk_ag_usuario_analise_validacao_aprovador FOREIGN KEY (id_usuario_validacao_aprovador)
        REFERENCES analise.usuario_analise (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,

    CONSTRAINT fk_ag_usuario_analise_validacao_gerente FOREIGN KEY (id_usuario_validacao_gerente)
        REFERENCES analise.usuario_analise (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE analise.analise_geo OWNER TO postgres;
GRANT ALL ON TABLE analise.analise_geo TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.analise_geo TO licenciamento_am;
GRANT SELECT, USAGE ON SEQUENCE analise.analise_geo_id_seq TO licenciamento_am;

COMMENT ON TABLE analise.analise_geo IS 'Entidade respons??vel por armazenar as an??lises geo.';
COMMENT ON COLUMN analise.analise_geo.id IS 'Identificador ??nico da entidade.';
COMMENT ON COLUMN analise.analise_geo.id_analise IS 'Identificador da tabela analise, respons??vel pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.analise_geo.parecer IS 'Parecer da an??lise geo.';
COMMENT ON COLUMN analise.analise_geo.data_vencimento_prazo IS 'Data de vencimento do prazo da an??lise.';
COMMENT ON COLUMN analise.analise_geo.revisao_solicitada IS 'Flag que indica se esta an??lise ?? uma revis??o.';
COMMENT ON COLUMN analise.analise_geo.ativo IS 'Indica se a an??lise ainda est?? ativa.';
COMMENT ON COLUMN analise.analise_geo.id_analise_geo_revisada IS 'Identificador da tabela analise_geo, respons??vel pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.analise_geo.data_inicio IS 'Data de in??cio da an??lise.';
COMMENT ON COLUMN analise.analise_geo.data_fim IS 'Data de fim de an??lise.';
COMMENT ON COLUMN analise.analise_geo.id_tipo_resultado_analise IS 'Campo responsavel por armazenar o resultado da an??lise do gerente.';
COMMENT ON COLUMN analise.analise_geo.id_tipo_resultado_validacao IS 'Campo responsavel por armazenar o resultado da an??lise do gerente.';
COMMENT ON COLUMN analise.analise_geo.parecer_validacao IS 'Campo respons??vel por armazenar a justificativa do gerente quando o mesmo vincular diretamente um analista geo.';
COMMENT ON COLUMN analise.analise_geo.id_usuario_validacao IS 'Identificador da entidade analise.usuario_analise que realizar?? o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.analise_geo.id_tipo_resultado_validacao_gerente IS 'Campo responsavel por armazenar o resultado da an??lise do gerente geo.';
COMMENT ON COLUMN analise.analise_geo.parecer_validacao_gerente IS 'Campo respons??vel por armazenar a descri????o da valida????o do gerente geo.';
COMMENT ON COLUMN analise.analise_geo.id_usuario_validacao_gerente IS 'Campo respons??vel por armazernar o gerente que fez a valida????o.';
COMMENT ON COLUMN analise.analise_geo.data_cadastro IS 'Data de cadastro da an??lise.';
COMMENT ON COLUMN analise.analise_geo.id_tipo_resultado_validacao_aprovador IS 'Campo responsavel por armazenar o resultado da an??lise do aprovador.';
COMMENT ON COLUMN analise.analise_geo.parecer_validacao_aprovador IS 'Campo respons??vel por armazenar a descri????o da valida????o do aprovador.';
COMMENT ON COLUMN analise.analise_geo.id_usuario_validacao_aprovador IS 'Campo respons??vel por armazernar o aprovador que fez a valida????o.';
COMMENT ON COLUMN analise.analise_geo.data_fim_validacao_aprovador IS 'Data final da an??lise do aprovador.';
COMMENT ON COLUMN analise.analise_geo.notificacao_atendida IS 'Flag para identificar as notifica????es atendidas.';


CREATE TABLE analise.analista_geo
(
    id              serial  NOT NULL,
    id_analise_geo  integer NOT NULL,
    id_usuario      integer NOT NULL,
    data_vinculacao timestamp without time zone,

    CONSTRAINT pk_analista_geo PRIMARY KEY (id),

    CONSTRAINT fk_ag_analise_geo FOREIGN KEY (id_analise_geo)
        REFERENCES analise.analise_geo (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,

    CONSTRAINT fk_ag_usuario_analise FOREIGN KEY (id_usuario)
        REFERENCES analise.usuario_analise (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE analise.analista_geo OWNER TO postgres;
GRANT ALL ON TABLE analise.analista_geo TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.analista_geo TO licenciamento_am;
GRANT SELECT, USAGE ON SEQUENCE analise.analista_geo_id_seq TO licenciamento_am;

COMMENT ON TABLE analise.analista_geo IS 'Entidade respons??vel por armazenar o analista da analise geo.';
COMMENT ON COLUMN analise.analista_geo.id IS 'Identificador ??nico da entidade.';
COMMENT ON COLUMN analise.analista_geo.id_analise_geo IS 'Identificador da entidade an??lise que faz o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.analista_geo.id_usuario IS 'Identificador da entidade usuario que faz o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.analista_geo.data_vinculacao IS 'Data em que o usuario foi vinculado.';


CREATE TABLE analise.parecer_geo_restricao
(
    id             serial  NOT NULL,
    id_analise_geo integer NOT NULL,
    codigo_camada  text    NOT NULL,
    parecer        text    NOT NULL,

    CONSTRAINT pk_parecer_geo_restricoes PRIMARY KEY (id),

    CONSTRAINT fk_pgr_analise_geo FOREIGN KEY (id_analise_geo)
        REFERENCES analise.analise_geo (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE analise.parecer_geo_restricao OWNER TO postgres;
GRANT ALL ON TABLE analise.parecer_geo_restricao TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.parecer_geo_restricao TO licenciamento_am;
GRANT SELECT, USAGE ON SEQUENCE analise.parecer_geo_restricao_id_seq TO licenciamento_am;

COMMENT ON TABLE analise.parecer_geo_restricao IS 'Entidade respons??vel por armazenar o parecer das restri????es geogr??ficas.';
COMMENT ON COLUMN analise.parecer_geo_restricao.id IS 'Identificador ??nico da entidade.';
COMMENT ON COLUMN analise.parecer_geo_restricao.id_analise_geo IS 'Identificador da entidade analise_geo que faz o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.parecer_geo_restricao.codigo_camada IS 'C??digo da camada do geoserver.';
COMMENT ON COLUMN analise.parecer_geo_restricao.parecer IS 'Descri????o do parecer de restri????o geogr??fico.';

INSERT INTO analise.tipo_documento(id, nome, caminho_pasta, prefixo_nome_arquivo) VALUES
(16,'Parecer an??lise geo', 'parecer_analise_geo', 'parecer_analise_geo' ),
(17,'Notifica????o an??lise geo', 'notificacao_analise_geo', 'notificacao_analise_geo' );

ALTER TABLE analise.notificacao

    ADD COLUMN id_analise_geo integer,

    ADD CONSTRAINT fk_n_analise_geo FOREIGN KEY (id_analise_geo)
        REFERENCES analise.analise_geo (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

COMMENT ON COLUMN analise.notificacao.id_analise_geo IS 'Identificador da tabela analise_geo, respons??vel pelo relacionamento entre as duas tabelas.';


ALTER TABLE analise.licenca_analise

    ADD COLUMN id_analise_geo integer,

    ADD CONSTRAINT fk_la_analise_geo FOREIGN KEY (id_analise_geo)
        REFERENCES analise.analise_geo (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

COMMENT ON COLUMN analise.licenca_analise.id_analise_geo IS 'Identificador da tabela analise_geo, respons??vel pelo relacionamento entre as duas tabelas.';


ALTER TABLE analise.analise_documento

    ADD COLUMN id_analise_geo integer,

    ADD CONSTRAINT fk_ad_analise_geo FOREIGN KEY (id_analise_geo)
        REFERENCES analise.analise_geo (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

COMMENT ON COLUMN analise.analise_documento.id_analise_geo IS 'Identificador da tabela analise_geo, respons??vel pelo relacionamento entre as duas tabelas.';


ALTER TABLE analise.dia_analise ADD COLUMN quantidade_dias_geo integer;
COMMENT ON COLUMN analise.dia_analise.quantidade_dias_geo IS 'Quantidade de dias para an??lise geo.';

ALTER TABLE analise.gerente_tecnico RENAME TO gerente;

ALTER TABLE analise.gerente

    ADD COLUMN id_analise_geo integer,

    ADD CONSTRAINT fk_g_analise_geo FOREIGN KEY (id_analise_geo)
        REFERENCES analise.analise_geo (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

COMMENT ON COLUMN analise.gerente.id_analise_geo IS 'Identificador da entidade analise_geo que realiza o relacionamento entre as entidades gerente e analise_geo.';

ALTER TABLE analise.gerente RENAME CONSTRAINT pk_gerente_tecnico TO pk_gerente;

ALTER TABLE analise.gerente RENAME CONSTRAINT fk_gt_analise_tecnica TO fk_g_analise_tecnica;

ALTER TABLE analise.gerente RENAME CONSTRAINT fk_gt_usuario_analise TO fk_g_usuario_analise;

COMMIT;

# --- !Downs

BEGIN;


ALTER TABLE analise.gerente RENAME TO gerente_tecnico;

ALTER TABLE analise.gerente_tecnico
    DROP CONSTRAINT fk_g_analise_geo,
    DROP COLUMN id_analise_geo;

ALTER TABLE analise.gerente_tecnico RENAME CONSTRAINT pk_gerente TO pk_gerente_tecnico;
ALTER TABLE analise.gerente_tecnico RENAME CONSTRAINT fk_g_analise_tecnica TO fk_gt_analise_tecnica;
ALTER TABLE analise.gerente_tecnico RENAME CONSTRAINT fk_g_usuario_analise TO fk_gt_usuario_analise;

ALTER TABLE analise.dia_analise DROP COLUMN quantidade_dias_geo;

ALTER TABLE analise.licenca_analise
    DROP CONSTRAINT fk_la_analise_geo,
    DROP COLUMN id_analise_geo;

ALTER TABLE analise.notificacao
    DROP CONSTRAINT fk_n_analise_geo,
    DROP COLUMN id_analise_geo;

ALTER TABLE analise.analise_documento
    DROP CONSTRAINT fk_ad_analise_geo,
    DROP COLUMN id_analise_geo;

DELETE FROM analise.tipo_documento WHERE id BETWEEN 16 AND 17;

DROP TABLE analise.parecer_geo_restricao;
DROP TABLE analise.analista_geo;
DROP TABLE analise.analise_geo;

COMMIT;

