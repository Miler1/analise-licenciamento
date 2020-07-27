# --- !Ups

BEGIN;

CREATE TABLE analise.tipo_area_geometria
(
    id     INT          NOT NULL,
    codigo VARCHAR(100) NOT NULL,
    nome   VARCHAR(150) NOT NULL,

    CONSTRAINT pk_tipo_geometria PRIMARY KEY (id),

    CONSTRAINT uq_codigo_tipo_geometria_anexo UNIQUE (codigo)
);

ALTER TABLE analise.tipo_area_geometria OWNER TO postgres;
GRANT SELECT, DELETE, UPDATE, INSERT ON TABLE analise.tipo_area_geometria TO licenciamento_am;

COMMENT ON TABLE analise.tipo_area_geometria IS 'Entidade que vai armazenar os tipos de geometrias do emprrendimento do licenciamento';
COMMENT ON COLUMN analise.tipo_area_geometria.id IS 'Identificador e chave primária da entidade';
COMMENT ON COLUMN analise.tipo_area_geometria.codigo IS 'Código de indentificação em texto da entidade';
COMMENT ON COLUMN analise.tipo_area_geometria.nome IS 'Nome de referência para os tipos de anexo de geometrias';

CREATE TABLE analise.analise_geo_anexo
(

    id                     SERIAL NOT NULL,
    id_empreendimento      INT    NOT NULL,
    id_tipo_area_geometria INT    NOT NULL,

    CONSTRAINT pk_analise_geo_anexo PRIMARY KEY (id),

    CONSTRAINT fk_aga_tipo_area_geometria FOREIGN KEY (id_tipo_area_geometria)
        REFERENCES analise.tipo_area_geometria (id),

    CONSTRAINT uq_cpf_cnpj_id_tipo_area_geometria UNIQUE (id_empreendimento, id_tipo_area_geometria)
);

SELECT addGeometryColumn('analise', 'analise_geo_anexo', 'geom', 4674, 'MULTIPOLYGON', 2);

ALTER TABLE analise.analise_geo_anexo OWNER TO postgres;
GRANT SELECT, DELETE, UPDATE, INSERT ON TABLE analise.analise_geo_anexo TO licenciamento_am;
GRANT SELECT, USAGE ON SEQUENCE analise.analise_geo_anexo_id_seq TO licenciamento_am;

COMMENT ON TABLE analise.analise_geo_anexo IS 'Entidade que vai armazenar os tipos de geometrias do emprrendimento do licenciamento';
COMMENT ON COLUMN analise.analise_geo_anexo.id IS 'Identificador e chave primária da entidade';
COMMENT ON COLUMN analise.analise_geo_anexo.id_empreendimento IS 'Referecia para o empreendimento que o anexo da analise geo pertence';
COMMENT ON COLUMN analise.analise_geo_anexo.id_tipo_area_geometria IS 'Referência para o tipo de área da geometria do anexo que foi feito o upload para o empreendimento';
COMMENT ON COLUMN analise.analise_geo_anexo.geom IS 'Geometria que estava no anexo que foi feito o upload';

CREATE TABLE analise.pessoa
(
    id_pessoa serial       NOT NULL,
    nome      varchar(200) NOT NULL,

    CONSTRAINT pk_pessoa PRIMARY KEY (id_pessoa)
);

ALTER TABLE analise.pessoa OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.pessoa TO licenciamento_am;
GRANT SELECT, USAGE ON SEQUENCE analise.pessoa_id_pessoa_seq TO licenciamento_am;

COMMENT ON TABLE analise.pessoa IS 'Entidade responsavel por armazenar as informações referentes a pessoa, utilizado pela tramitação.';
COMMENT ON COLUMN analise.pessoa.id_pessoa IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.pessoa.nome IS 'Nome ou razão social da pessoa.';

ALTER TABLE analise.licenca_analise 
    ALTER COLUMN id_analise_tecnica DROP NOT NULL,
    ALTER COLUMN id_analise_geo DROP NOT NULL;

ALTER TABLE analise.usuario_analise
    
    ADD COLUMN id_pessoa INTEGER NOT NULL,

    ADD CONSTRAINT fk_ua_pessoa FOREIGN KEY (id_pessoa)
        REFERENCES analise.pessoa (id_pessoa);

COMMENT ON COLUMN analise.usuario_analise.id_pessoa IS 'Identificador da tabela pessoa, responsável pelo relacionamento entre as duas tabelas.';

INSERT INTO analise.tipo_area_geometria (id, codigo, nome) VALUES
(1, 'HID', 'Hidrografia'),
(2, 'APP', 'Área de preservação permanente'),
(3, 'AA', 'Área Antropizada');

COMMIT;

# --- !Downs

BEGIN;

ALTER TABLE analise.usuario_analise

	DROP CONSTRAINT fk_ua_pessoa,
    
    DROP COLUMN id_pessoa;

DROP TABLE analise.pessoa;

DROP TABLE analise.analise_geo_anexo;

DROP TABLE analise.tipo_area_geometria;

COMMIT;

