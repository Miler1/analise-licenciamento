
# --- !Ups,

CREATE TABLE analise.vistoria (
    id integer NOT NULL,
    realizada boolean NOT NULL,
    tx_conclusao text NOT NULL,
    id_documento_rit integer,
    data date,
    hora date,
    tx_descricao text,
    tx_cursos_dagua text,
    tx_tipologia_vegetal text,
    tx_app text,
    tx_ocorrencia text,
    tx_residuos_liquidos text,
    tx_outras_informacoes text,
    id_analise_tecnica integer NOT NULL,
    CONSTRAINT pk_vistoria PRIMARY KEY(id),
    CONSTRAINT fk_v_documento FOREIGN KEY(id_documento_rit)  
        REFERENCES analise.documento(id),
    CONSTRAINT fk_v_analise_tecnica FOREIGN KEY(id_analise_tecnica) 
        REFERENCES analise.analise_tecnica(id)
);
ALTER TABLE analise.vistoria OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.vistoria TO licenciamento_am;
COMMENT ON TABLE analise.vistoria is 'Entidade responsável por armazenar informações da vistoria da análise técnica';
COMMENT ON COLUMN analise.vistoria.id IS 'Identificador único da entidade vistoria';
COMMENT ON COLUMN analise.vistoria.realizada IS 'Flag responsável por dizer se uma vistoria foi realizada ou não em uma análise técnica';
COMMENT ON COLUMN analise.vistoria.tx_conclusao IS 'Conclusão da vistoria, sendo ela realiza ou não';
COMMENT ON COLUMN analise.vistoria.id_documento_rit IS 'Documento de Registro de Inspeção Técnica da vistoria realizada';
COMMENT ON COLUMN analise.vistoria.data IS 'Data da vistoria realizada';
COMMENT ON COLUMN analise.vistoria.hora IS 'Hora da vistoria realizada';
COMMENT ON COLUMN analise.vistoria.tx_descricao IS 'Descrição da atividade da vistoria realizada';
COMMENT ON COLUMN analise.vistoria.tx_cursos_dagua IS 'Cursos de agua da vistoria realizada';
COMMENT ON COLUMN analise.vistoria.tx_tipologia_vegetal IS 'Tipologia vegtal da vistoria realizada';
COMMENT ON COLUMN analise.vistoria.tx_app IS 'Áreas de Preservação Permanente da vistoria realizada';
COMMENT ON COLUMN analise.vistoria.tx_ocorrencia IS 'Ocorrência de processos erosivos da vistoria realizada';
COMMENT ON COLUMN analise.vistoria.tx_residuos_liquidos IS 'Gestão de resíduos sólidos e líquidos gerados na atividade produtiva da vistoria realizada';
COMMENT ON COLUMN analise.vistoria.tx_outras_informacoes IS ' Outras informações da vistoria realizada';
COMMENT ON COLUMN analise.vistoria.id_analise_tecnica IS 'Identificador da análise técnica relacionada à vistoria';



CREATE TABLE analise.equipe_vistoria (
    id_vistoria integer NOT NULL,
    id_usuario  integer NOT NULL,
    CONSTRAINT fk_ev_vistoria FOREIGN KEY(id_vistoria)
        REFERENCES analise.vistoria(id),
    CONSTRAINT fk_ev_usuario_analise FOREIGN KEY(id_usuario)
        REFERENCES analise.usuario_analise(id)
);
ALTER TABLE analise.equipe_vistoria OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.equipe_vistoria TO licenciamento_am;
COMMENT ON TABLE analise.equipe_vistoria is 'Entidade responsável por armazenar a equipe da vistoria de uma análise técnica caso a mesma foi realizada';
COMMENT ON COLUMN analise.equipe_vistoria.id_vistoria IS 'Identificador da vistoria';
COMMENT ON COLUMN analise.equipe_vistoria.id_usuario IS 'Identificador do usuário relacionado a equipe de uma vistoria';


CREATE TABLE analise.rel_documento_vistoria (
    id_documento integer NOT NULL,
    id_vistoria  integer NOT NULL,
    CONSTRAINT pk_rel_documento_vistoria PRIMARY KEY (id_documento, id_vistoria),
    CONSTRAINT fk_rdv_documento FOREIGN KEY(id_documento)
        REFERENCES analise.documento,
    CONSTRAINT fk_rdv_vistoria FOREIGN KEY(id_vistoria)
        REFERENCES analise.vistoria(id)
);
ALTER TABLE analise.rel_documento_vistoria OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE  analise.rel_documento_vistoria TO licenciamento_am;
COMMENT ON  TABLE analise.rel_documento_vistoria is 'Entidade responsável por armazenar os documentos de uma vistoria de análise técnica';
COMMENT ON COLUMN analise.rel_documento_vistoria.id_documento IS 'Identificador do documento da vistoria';
COMMENT ON COLUMN analise.rel_documento_vistoria.id_vistoria IS 'Identificador da vistoria';



CREATE TABLE analise.inconsistencia_vistoria (
  id serial NOT NULL, 
  id_vistoria integer, 
  descricao_inconsistencia text NOT NULL, 
  CONSTRAINT pk_inconsistencia_vistoria PRIMARY KEY (id),
  CONSTRAINT fk_iv_vistoria FOREIGN KEY(id_vistoria)
      REFERENCES analise.vistoria(id) 
);
ALTER TABLE analise.inconsistencia_vistoria OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.inconsistencia_vistoria TO licenciamento_am;
GRANT SELECT, USAGE ON SEQUENCE analise.inconsistencia_vistoria_id_seq TO licenciamento_am; 
COMMENT ON  TABLE analise.inconsistencia_vistoria IS 'Entidade responsável por armazenar as análises geo.';
COMMENT ON  COLUMN analise.inconsistencia_vistoria.id IS 'Identificador único da entidade.';
COMMENT ON  COLUMN analise.inconsistencia_vistoria.id_vistoria IS 'Identificador da tabela vistoria, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON  COLUMN analise.inconsistencia_vistoria.descricao_inconsistencia IS 'Campo que armazena a descrição da inconsistência relatada pelo analista.';


CREATE TABLE analise.rel_documento_inconsistencia_vistoria (
    id_inconsistencia_vistoria integer NOT NULL,
    id_documento  integer NOT NULL,
    CONSTRAINT pk_rel_documento_inconsistencia_vistoria PRIMARY KEY (id_inconsistencia_vistoria, id_documento),
    CONSTRAINT fk_rdiv_inconsistencia_vistoria FOREIGN KEY(id_inconsistencia_vistoria)
        REFERENCES analise.inconsistencia_vistoria(id),
    CONSTRAINT fk_rdiv_documento FOREIGN KEY(id_documento)
        REFERENCES analise.documento(id)
);
ALTER TABLE analise.rel_documento_inconsistencia_vistoria OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.rel_documento_inconsistencia_vistoria TO licenciamento_am;
COMMENT ON  TABLE analise.rel_documento_inconsistencia_vistoria is 'Entidade responsável por armazenar documentos da inconsistência da vistoria';
COMMENT ON COLUMN analise.rel_documento_inconsistencia_vistoria.id_inconsistencia_vistoria IS 'Identificador da inconsistência relacionada a vistoria';
COMMENT ON COLUMN analise.rel_documento_inconsistencia_vistoria.id_documento IS 'Identificador do documento da inconsistência relacionada a vistoria';



INSERT INTO analise.tipo_documento (id, nome, caminho_modelo, caminho_pasta, prefixo_nome_arquivo) 
    VALUES (27, 'Documentos inconsistência vistoria', null, 'documento_inconsistencia_vistoria', 'documento_inconsistencia_vistoria');

CREATE SEQUENCE analise.vistoria_id_seq
    INCREMENT 1
    MINVALUE 1
    NO MAXVALUE
    START 1
    CACHE 1;
ALTER TABLE analise.vistoria ALTER COLUMN id SET DEFAULT pg_catalog.nextval('analise.vistoria_id_seq');
ALTER TABLE analise.vistoria_id_seq OWNER TO postgres;
GRANT SELECT, USAGE ON SEQUENCE analise.vistoria_id_seq TO licenciamento_am;
SELECT setval('analise.vistoria_id_seq', coalesce(max(id), 1)) FROM analise.vistoria;

# --- !Downs

DELETE FROM analise.tipo_documento WHERE id=27; 

DROP TABLE analise.rel_documento_inconsistencia_vistoria;

DROP TABLE analise.inconsistencia_vistoria;

DROP TABLE analise.rel_documento_vistoria ;

DROP TABLE analise.equipe_vistoria;

DROP TABLE analise.vistoria;
