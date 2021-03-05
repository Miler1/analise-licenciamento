# --- !Ups

-- GERENTE PARA COORDENADOR 

UPDATE analise.perfil_usuario_analise
    SET codigo_perfil = 'COORDENADOR', nome_perfil = 'Coordenador'
    WHERE codigo_perfil = 'GERENTE' AND nome_perfil = 'Gerente';

ALTER TABLE analise.analise_tecnica 
	RENAME id_tipo_resultado_validacao_gerente TO id_tipo_resultado_validacao_coordenador; 

ALTER TABLE analise.analise_tecnica 
	RENAME CONSTRAINT fk_at_tipo_resultado_validacao_gerente TO fk_at_tipo_resultado_validacao_coordenador;

ALTER TABLE analise.analise_tecnica 
	RENAME parecer_validacao_gerente TO parecer_validacao_coordenador; 

ALTER TABLE analise.analise_tecnica 
	RENAME id_usuario_validacao_gerente TO id_usuario_validacao_coordenador; 

ALTER TABLE analise.analise_tecnica 
	RENAME CONSTRAINT fk_at_usuario_analise_validacao_gerente TO fk_at_usuario_analise_validacao_coordenador;


ALTER TABLE analise.gerente RENAME TO coordenador;
ALTER TABLE analise.coordenador RENAME CONSTRAINT pk_gerente TO pk_coordenador;
ALTER SEQUENCE analise.gerente_id_seq RENAME TO coordenador_id_seq;

ALTER TABLE analise.desvinculo_analise_geo RENAME COLUMN id_gerente TO id_coordenador;
ALTER TABLE analise.desvinculo_analise_geo RENAME COLUMN resposta_gerente TO resposta_coordenador;
ALTER TABLE analise.desvinculo_analise_geo RENAME CONSTRAINT fk_dag_gerente_usuario_analise TO fk_dag_coordenador_usuario_analise;

ALTER TABLE analise.desvinculo_analise_tecnica RENAME COLUMN id_gerente TO id_coordenador;
ALTER TABLE analise.desvinculo_analise_tecnica RENAME COLUMN resposta_gerente TO resposta_coordenador;
ALTER TABLE analise.desvinculo_analise_tecnica RENAME CONSTRAINT fk_dat_gerente_usuario_analise TO fk_dat_coordenador_usuario_analise;

ALTER TABLE analise.parecer_gerente_analise_geo RENAME TO parecer_coordenador_analise_geo;
ALTER TABLE analise.parecer_coordenador_analise_geo RENAME COLUMN id_usuario_gerente TO id_usuario_coordenador;
ALTER TABLE analise.parecer_coordenador_analise_geo RENAME CONSTRAINT pk_parecer_gerente_analise_geo TO pk_parecer_coordenador_analise_geo;
ALTER SEQUENCE analise.parecer_gerente_analise_geo_id_seq RENAME TO parecer_coordenador_analise_geo_id_seq;

ALTER TABLE analise.parecer_gerente_analise_tecnica RENAME TO parecer_coordenador_analise_tecnica;
ALTER TABLE analise.parecer_coordenador_analise_tecnica RENAME COLUMN id_usuario_gerente TO id_usuario_coordenador;
ALTER TABLE analise.parecer_coordenador_analise_tecnica RENAME CONSTRAINT pk_parecer_gerente_analise_tecnica TO pk_parecer_coordenador_analise_tecnica;
ALTER SEQUENCE analise.parecer_gerente_analise_tecnica_id_seq RENAME TO parecer_coordenador_analise_tecnica_id_seq;

-- PRESIDENTE PARA SECRETARIO 

UPDATE analise.perfil_usuario_analise
    SET codigo_perfil = 'SECRETARIO', nome_perfil = 'Secretário'
    WHERE codigo_perfil = 'PRESIDENTE' AND nome_perfil = 'Presidente';

ALTER TABLE analise.presidente RENAME TO secretario;
ALTER TABLE analise.secretario RENAME CONSTRAINT pk_presidente TO pk_secretario;
ALTER SEQUENCE analise.presidente_id_seq RENAME TO secretario_id_seq;

ALTER TABLE analise.parecer_presidente RENAME TO parecer_secretario;
ALTER TABLE analise.parecer_secretario RENAME COLUMN id_usuario_presidente TO id_usuario_secretario;
ALTER TABLE analise.parecer_secretario RENAME CONSTRAINT pk_parecer_presidente TO pk_parecer_secretario;
ALTER SEQUENCE analise.parecer_presidente_id_seq rename to parecer_secretario_id_seq;


# --- !Downs

-- COORDENADOR PARA GERENTE 

UPDATE analise.perfil_usuario_analise
    SET codigo_perfil = 'GERENTE', nome_perfil = 'Gerente'
    WHERE codigo_perfil = 'COORDENADOR' AND nome_perfil = 'Coordenador';

ALTER TABLE analise.analise_tecnica 
	RENAME CONSTRAINT fk_at_usuario_analise_validacao_coordenador TO  fk_at_usuario_analise_validacao_gerente;

ALTER TABLE analise.analise_tecnica 
	RENAME id_usuario_validacao_coordenador  TO id_usuario_validacao_gerente; 

ALTER TABLE analise.analise_tecnica 
	RENAME parecer_validacao_coordenador  TO parecer_validacao_gerente; 

ALTER TABLE analise.analise_tecnica 
	RENAME CONSTRAINT fk_at_tipo_resultado_validacao_coordenador TO  fk_at_tipo_resultado_validacao_gerente;

ALTER TABLE analise.analise_tecnica 
	RENAME id_tipo_resultado_validacao_coordenador TO  id_tipo_resultado_validacao_gerente; 


ALTER TABLE analise.coordenador RENAME TO gerente;
ALTER TABLE analise.gerente RENAME CONSTRAINT pk_coordenador TO pk_gerente;
ALTER SEQUENCE analise.coordenador_id_seq RENAME TO gerente_id_seq;

ALTER TABLE analise.desvinculo_analise_geo RENAME COLUMN id_coordenador TO id_gerente;
ALTER TABLE analise.desvinculo_analise_geo RENAME COLUMN resposta_coordenador TO resposta_gerente;
ALTER TABLE analise.desvinculo_analise_geo RENAME CONSTRAINT fk_dag_coordenador_usuario_analise TO fk_dag_gerente_usuario_analise;

ALTER TABLE analise.desvinculo_analise_tecnica RENAME COLUMN id_coordenador TO id_gerente;
ALTER TABLE analise.desvinculo_analise_tecnica RENAME COLUMN resposta_coordenador TO resposta_gerente;
ALTER TABLE analise.desvinculo_analise_tecnica RENAME CONSTRAINT fk_dat_coordenador_usuario_analise TO fk_dat_gerente_usuario_analise;

ALTER TABLE analise.parecer_coordenador_analise_geo RENAME TO parecer_gerente_analise_geo;
ALTER TABLE analise.parecer_gerente_analise_geo RENAME COLUMN id_usuario_coordenador TO id_usuario_gerente;
ALTER TABLE analise.parecer_gerente_analise_geo RENAME CONSTRAINT pk_parecer_coordenador_analise_geo TO pk_parecer_gerente_analise_geo;
ALTER SEQUENCE analise.parecer_coordenador_analise_geo_id_seq RENAME TO parecer_gerente_analise_geo_id_seq;

ALTER TABLE analise.parecer_coordenador_analise_tecnica RENAME TO parecer_gerente_analise_tecnica;
ALTER TABLE analise.parecer_gerente_analise_tecnica RENAME COLUMN id_usuario_coordenador TO id_usuario_gerente;
ALTER TABLE analise.parecer_gerente_analise_tecnica RENAME CONSTRAINT pk_parecer_coordenador_analise_tecnica TO pk_parecer_gerente_analise_tecnica;
ALTER SEQUENCE analise.parecer_coordenador_analise_tecnica_id_seq RENAME TO parecer_gerente_analise_tecnica_id_seq;

-- SECRETARIO PARA PRESIDENTE 

UPDATE analise.perfil_usuario_analise
    SET codigo_perfil = 'PRESIDENTE', nome_perfil = 'Presidente'
    WHERE codigo_perfil = 'SECRETARIO' AND nome_perfil = 'Secretário';

ALTER TABLE analise.secretario RENAME TO presidente;
ALTER TABLE analise.presidente RENAME CONSTRAINT pk_secretario TO pk_presidente;
ALTER SEQUENCE analise.secretario_id_seq RENAME TO presidente_id_seq;

ALTER TABLE analise.parecer_secretario RENAME TO parecer_presidente;
ALTER TABLE analise.parecer_presidente RENAME COLUMN id_usuario_secretario TO id_usuario_presidente;
ALTER TABLE analise.parecer_presidente RENAME CONSTRAINT pk_parecer_secretario TO pk_parecer_presidente;
ALTER SEQUENCE analise.parecer_secretario_id_seq rename to parecer_presidente_id_seq;



