
# --- !Ups

ALTER TABLE analise.parecer_analista_tecnico ADD COLUMN id_documento INTEGER;
COMMENT ON COLUMN analise.parecer_analista_tecnico.id_documento IS 'Identificador do documento do parecer do analista tecnico';

ALTER TABLE analise.parecer_analista_tecnico ADD 
CONSTRAINT fk_pat_documento FOREIGN KEY (id_documento) 
	REFERENCES analise.documento(id);


# --- !Downs

ALTER TABLE analise.parecer_analista_tecnico DROP CONSTRAINT fk_pat_documento;

ALTER TABLE analise.parecer_analista_tecnico DROP COLUMN id_documento;


