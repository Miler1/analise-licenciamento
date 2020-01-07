
# --- !Ups

ALTER TABLE analise.vistoria DROP CONSTRAINT fk_v_analise_tecnica;

ALTER TABLE analise.vistoria DROP COLUMN id_analise_tecnica;

ALTER TABLE analise.vistoria ADD COLUMN id_parecer_analista_tecnico integer NOT NULL;
COMMENT ON COLUMN analise.vistoria.id_parecer_analista_tecnico IS 'Identificador do parecer do analista técnico relacionado a vistoria';

ALTER TABLE analise.vistoria ADD 
CONSTRAINT fk_v_parecer_analista_tecnico FOREIGN KEY(id_parecer_analista_tecnico)
    REFERENCES analise.parecer_analista_tecnico(id);         

ALTER TABLE analise.inconsistencia_vistoria ADD COLUMN tipo_inconsistencia text NOT NULL;
COMMENT ON COLUMN analise.inconsistencia_vistoria.tipo_inconsistencia IS 'Campo que armazena o tipo da inconsistência relatada pelo analista.';


# --- !Downs

ALTER TABLE analise.inconsistencia_vistoria DROP COLUMN tipo_inconsistencia;

ALTER TABLE analise.vistoria DROP CONSTRAINT fk_v_parecer_analista_tecnico;

ALTER TABLE analise.vistoria DROP COLUMN id_parecer_analista_tecnico;

ALTER TABLE analise.vistoria ADD COLUMN id_analise_tecnica integer NOT NULL;
COMMENT ON COLUMN analise.vistoria.id_analise_tecnica IS 'Identificador da análise técnica relacionada à vistoria';

ALTER TABLE analise.vistoria ADD 
CONSTRAINT fk_v_analise_tecnica FOREIGN KEY(id_analise_tecnica)
	REFERENCES analise.analise_tecnica (id);

