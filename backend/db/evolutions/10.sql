# --- !Ups

CREATE TABLE analise.analise_tecnica (
 id serial NOT NULL,
 id_analise integer NOT NULL, 
 parecer text, 
 data_vencimento_prazo date NOT NULL, 
 revisao_solicitada boolean DEFAULT false, 
 ativo boolean DEFAULT true,
 id_analise_tecnica_revisada integer,
 data_inicio timestamp without time zone, 
 data_fim timestamp without time zone,
 id_tipo_resultado_analise integer, 
 id_tipo_resultado_validacao integer,
 CONSTRAINT pk_analise_tecnica PRIMARY KEY (id),
 CONSTRAINT fk_at_analise FOREIGN KEY (id_analise)
 REFERENCES analise.analise(id),
 CONSTRAINT fk_at_analise_juridica FOREIGN KEY (id_analise_tecnica_revisada)
 REFERENCES analise.analise_juridica (id),
 CONSTRAINT fk_at_tipo_resultado_analise FOREIGN KEY (id_tipo_resultado_analise)
 REFERENCES analise.tipo_resultado_analise(id),
 CONSTRAINT fk_at_tipo_resultado_validacao FOREIGN KEY (id_tipo_resultado_validacao)
 REFERENCES analise.tipo_resultado_analise(id)
);
GRANT INSERT, SELECT,UPDATE,DELETE ON TABLE analise.analise_tecnica TO licenciamento_am;
GRANT SELECT,USAGE ON SEQUENCE analise.analise_tecnica_id_seq TO licenciamento_am;
ALTER TABLE analise.analise_tecnica OWNER TO postgres;



CREATE TABLE analise.licenca_analise(
 id SERIAL NOT NULL,
 id_analise_tecnica INTEGER NOT NULL,
 validade INTEGER NOT NULL,
 id_licenca INTEGER NOT NULL,
 observacao TEXT,
 CONSTRAINT pk_licenca_analise PRIMARY KEY(id),
 CONSTRAINT fk_la_analise_tecnica FOREIGN KEY(id_analise_tecnica)
 REFERENCES analise.analise_tecnica(id),
 CONSTRAINT fk_la_licenca FOREIGN KEY(id_licenca)
 REFERENCES licenciamento.licenca(id)
 
);
GRANT INSERT, SELECT,UPDATE,DELETE ON TABLE analise.licenca_analise TO licenciamento_am;
GRANT SELECT,USAGE ON SEQUENCE analise.licenca_analise_id_seq TO licenciamento_am;
ALTER TABLE analise.licenca_analise OWNER TO postgres;


CREATE TABLE analise.condicionante(
 id SERIAL NOT NULL,
 id_licenca_analise INTEGER NOT NULL,
 texto TEXT NOT NULL,
 prazo INTEGER NOT NULL,
 ordem INTEGER NOT NULL,
 CONSTRAINT pk_condicionante PRIMARY KEY(id),
 CONSTRAINT fk_c_licenca_analise FOREIGN KEY(id_licenca_analise)
 REFERENCES analise.licenca_analise(id) 
);
GRANT INSERT, SELECT,UPDATE,DELETE ON TABLE analise.condicionante TO licenciamento_am;
GRANT SELECT,USAGE ON SEQUENCE analise.condicionante_id_seq TO licenciamento_am;
ALTER TABLE analise.condicionante OWNER TO postgres;



CREATE TABLE analise.recomendacao (
 id SERIAL NOT NULL,
 id_licenca_analise INTEGER NOT NULL,
 texto TEXT NOT NULL,
 ordem INTEGER NOT NULL,
 CONSTRAINT pk_recomendacao PRIMARY KEY(id),
 CONSTRAINT fk_r_licenca_analise FOREIGN KEY(id_licenca_analise)
 REFERENCES analise.licenca_analise(id)
);
GRANT INSERT, SELECT,UPDATE,DELETE ON TABLE analise.recomendacao TO licenciamento_am;
GRANT SELECT,USAGE ON SEQUENCE analise.recomendacao_id_seq TO licenciamento_am;
ALTER TABLE analise.recomendacao OWNER TO postgres;


CREATE TABLE analise.parecer_tecnico_restricao(
 id SERIAL NOT NULL,
 id_analise_tecnica INTEGER NOT NULL,
 codigo_camada TEXT NOT NULL,
 parecer TEXT NOT NULL,
 CONSTRAINT pk_parecer_tecnico_restricoes PRIMARY KEY(id),
 CONSTRAINT fk_ptr_analise_tecnica FOREIGN KEY(id_analise_tecnica)
 REFERENCES analise.analise_tecnica(id)
);
GRANT INSERT, SELECT,UPDATE,DELETE ON TABLE analise.parecer_tecnico_restricao TO licenciamento_am;
GRANT SELECT,USAGE ON SEQUENCE analise.parecer_tecnico_restricao_id_seq TO licenciamento_am;
ALTER TABLE analise.parecer_tecnico_restricao OWNER TO postgres;

CREATE TABLE analise.analista_tecnico(
 id SERIAL NOT NULL,
 id_analise_tecnica INTEGER NOT NULL,
 id_usuario INTEGER NOT NULL,
 data_vinculacao TIMESTAMP WITHOUT TIME ZONE,
 CONSTRAINT pk_analista_tecnico PRIMARY KEY(id),
 CONSTRAINT fk_at_analise_tecnica FOREIGN KEY(id_analise_tecnica)
 REFERENCES analise.analise_tecnica(id),
 CONSTRAINT fk_at_usuario FOREIGN KEY(id_usuario)
 REFERENCES portal_seguranca.usuario(id)

);
GRANT INSERT, SELECT,UPDATE,DELETE ON TABLE analise.analista_tecnico TO licenciamento_am;
GRANT SELECT,USAGE ON SEQUENCE analise.analista_tecnico_id_seq TO licenciamento_am;
ALTER TABLE analise.analista_tecnico OWNER TO postgres;

CREATE TABLE analise.rel_documento_analise_tecnica(
 id_documento INTEGER NOT NULL,
 id_analise_tecnica INTEGER NOT NULL,
 CONSTRAINT pk_rel_documento_analise_tecnica PRIMARY KEY(id_documento,id_analise_tecnica),
 CONSTRAINT fk_rdat_documento FOREIGN KEY(id_documento)
 REFERENCES analise.documento(id),
 CONSTRAINT fk_rdat_analise_tecnica FOREIGN KEY(id_analise_tecnica)
 REFERENCES analise.analise_tecnica(id)
);
GRANT INSERT, SELECT,UPDATE,DELETE ON TABLE analise.rel_documento_analise_tecnica TO licenciamento_am;
ALTER TABLE analise.rel_documento_analise_tecnica OWNER TO postgres;

ALTER TABLE analise.analise_documento ADD COLUMN id_analise_tecnica INTEGER;
ALTER TABLE analise.analise_documento ADD CONSTRAINT fk_ad_analise_tecnica FOREIGN KEY(id_analise_tecnica)
REFERENCES analise.analise_tecnica(id);


# --- !Downs

DROP TABLE analise.condicionante;

DROP TABLE analise.recomendacao;

DROP TABLE analise.parecer_tecnico_restricao;

DROP TABLE analise.analista_tecnico;

DROP TABLE analise.rel_documento_analise_tecnica;

ALTER TABLE analise.analise_documento DROP COLUMN id_analise_tecnica;

DROP TABLE analise.licenca_analise;

DROP TABLE analise.analise_tecnica;
 