# --- !Ups

CREATE SEQUENCE "analise"."notificacao_id_seq"
  INCREMENT 1
  MINVALUE 1
  NO MAXVALUE
  START 20000
  CACHE 1;
ALTER TABLE "analise"."notificacao_id_seq"
OWNER TO postgres;
GRANT SELECT, USAGE ON SEQUENCE "analise"."notificacao_id_seq" TO licenciamento_pa;

CREATE TABLE "analise"."notificacao" ( 
	"id" Integer DEFAULT nextval('analise.notificacao_id_seq'::regclass) NOT NULL,
	"id_analise_juridica" Integer,
	"id_analise_tecnica" Integer,
	"id_tipo_documento" Integer NOT NULL,
	"id_documento_corrigido" Integer NOT NULL,
	"id_analise_documento" Integer NOT NULL,
	"resolvido" Boolean NOT NULL DEFAULT false,
	"ativo" Boolean NOT NULL DEFAULT true,
	PRIMARY KEY ( "id" ) );
 ;

COMMENT ON COLUMN "analise"."notificacao"."id" IS 'Identificador único da entidade.';
COMMENT ON COLUMN "analise"."notificacao"."id_analise_juridica" IS 'Identificador da tabela analise_juridica, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN "analise"."notificacao"."id_analise_tecnica" IS 'Identificador da tabela analise_tecnica, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN "analise"."notificacao"."id_tipo_documento" IS 'Identificador da tabela tipo_documento do schema licenciamento, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN "analise"."notificacao"."id_documento_corrigido" IS 'Identificador da tabela documento do schema licenciamento, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN "analise"."notificacao"."id_analise_documento" IS 'Identificador da tabela analise_documento, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN "analise"."notificacao"."resolvido" IS 'Flag que indica se a notificação foi resolvida.';
COMMENT ON COLUMN "analise"."notificacao"."ativo" IS 'Flag que indica se a notificação foi resolvida.';
COMMENT ON TABLE  "analise"."notificacao" IS 'Entidade responsável por armazenar os documentos que estão sendo analisados.';

ALTER TABLE "analise"."notificacao"
	ADD CONSTRAINT "fk_n_analise_juridica" FOREIGN KEY ( "id_analise_juridica" )
	REFERENCES "analise"."analise_juridica" ( "id" ) MATCH SIMPLE
	ON DELETE No Action
	ON UPDATE No Action;

ALTER TABLE "analise"."notificacao"
	ADD CONSTRAINT "fk_n_analise_tecnica" FOREIGN KEY ( "id_analise_tecnica" )
	REFERENCES "analise"."analise_tecnica" ( "id" ) MATCH SIMPLE
	ON DELETE No Action
	ON UPDATE No Action;

ALTER TABLE "analise"."notificacao"
	ADD CONSTRAINT "fk_n_tipo_documento" FOREIGN KEY ( "id_tipo_documento" )
	REFERENCES "licenciamento"."tipo_documento" ( "id" ) MATCH SIMPLE
	ON DELETE No Action
	ON UPDATE No Action;


ALTER TABLE "analise"."notificacao"
	ADD CONSTRAINT "fk_n_documento" FOREIGN KEY ( "id_documento_corrigido" )
	REFERENCES "licenciamento"."documento" ( "id" ) MATCH SIMPLE
	ON DELETE No Action
	ON UPDATE No Action;

ALTER TABLE "analise"."notificacao"
	ADD CONSTRAINT "fk_n_analise_documento" FOREIGN KEY ( "id_analise_documento" )
	REFERENCES "analise"."analise_documento" ( "id" ) MATCH SIMPLE
	ON DELETE No Action
	ON UPDATE No Action;

# --- !Downs

DROP TABLE analise.notificacao;

DROP SEQUENCE analise.notificacao_id_seq;