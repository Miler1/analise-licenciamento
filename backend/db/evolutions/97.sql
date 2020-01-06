
# --- !Ups

-- Evolution com problemas, as sequences ja existem na tabela


--CREATE SEQUENCE analise.perfil_usuario_analise_id_seq
--  INCREMENT 1
--  MINVALUE 1
--  NO MAXVALUE
--  START 1
--  CACHE 1;
--
--ALTER TABLE analise.perfil_usuario_analise ALTER COLUMN id SET DEFAULT pg_catalog.nextval('analise.perfil_usuario_analise_id_seq');
--
--ALTER TABLE analise.perfil_usuario_analise_id_seq OWNER TO postgres;
--
--ALTER SEQUENCE analise.perfil_usuario_analise_id_seq OWNED BY analise.perfil_usuario_analise.id;
--
--GRANT SELECT, USAGE ON SEQUENCE analise.perfil_usuario_analise_id_seq TO licenciamento_am;
--
--SELECT setval('analise.perfil_usuario_analise_id_seq', coalesce(max(id), 1)) FROM analise.perfil_usuario_analise;
--
--
--
--CREATE SEQUENCE analise.setor_usuario_analise_id_seq
--  INCREMENT 1
--  MINVALUE 1
--  NO MAXVALUE
--  START 1
--  CACHE 1;
--
--ALTER TABLE analise.setor_usuario_analise ALTER COLUMN id SET DEFAULT pg_catalog.nextval('analise.setor_usuario_analise_id_seq');
--
--ALTER TABLE analise.setor_usuario_analise_id_seq OWNER TO postgres;
--
--ALTER SEQUENCE analise.setor_usuario_analise_id_seq OWNED BY analise.setor_usuario_analise.id;
--
--GRANT SELECT, USAGE ON SEQUENCE analise.setor_usuario_analise_id_seq TO licenciamento_am;
--
--SELECT setval('analise.setor_usuario_analise_id_seq', coalesce(max(id), 1)) FROM analise.setor_usuario_analise;


# --- !Downs

--ALTER TABLE analise.setor_usuario_analise_id_seq ALTER COLUMN id DROP DEFAULT;
--DROP SEQUENCE analise.setor_usuario_analise_id_seq;
--
--ALTER TABLE analise.perfil_usuario_analise_id_seq ALTER COLUMN id DROP DEFAULT;
--DROP SEQUENCE analise.perfil_usuario_analise_id_seq;