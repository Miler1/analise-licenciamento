
# --- !Ups

ALTER TABLE analise.perfil_usuario_analise 
    DROP CONSTRAINT fk_pua_usuario_analise,
    DROP COLUMN id,
    ADD CONSTRAINT fk_pua_usuario_analise PRIMARY KEY (id_usuario_analise,codigo_perfil);

ALTER TABLE analise.setor_usuario_analise 
    DROP CONSTRAINT fk_sua_usuario_analise,
    DROP COLUMN id,
    ADD CONSTRAINT fk_sua_usuario_analise PRIMARY KEY (id_usuario_analise, sigla_setor);

    
# --- !Downs


 ALTER TABLE analise.setor_usuario_analise
    DROP CONSTRAINT fk_sua_usuario_analise,
    ADD COLUMN id SERIAL NOT NULL,
    ADD CONSTRAINT fk_sua_usuario_analise PRIMARY KEY (id);

 ALTER TABLE analise.perfil_usuario_analise 
    DROP CONSTRAINT fk_pua_usuario_analise,
    ADD COLUMN id SERIAL NOT NULL,
    ADD CONSTRAINT fk_pua_usuario_analise PRIMARY KEY (id);

