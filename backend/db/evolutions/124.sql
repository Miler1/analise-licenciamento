
# --- !Ups

ALTER TABLE analise.notificacao ADD COLUMN id_parecer_analista_tecnico INTEGER;
COMMENT ON COLUMN analise.notificacao.id_parecer_analista_tecnico IS 'Identificador do parecer do analista tecnico';

ALTER TABLE analise.notificacao ADD CONSTRAINT fk_n_parecer_analista_tecnico 
FOREIGN KEY (id_parecer_analista_tecnico) REFERENCES analise.parecer_analista_tecnico(id);

ALTER TABLE analise.notificacao ALTER COLUMN id_parecer_analista_geo DROP NOT NULL;


# --- !Downs

ALTER TABLE analise.notificacao ALTER COLUMN id_parecer_analista_geo SET NOT NULL;

ALTER TABLE analise.notificacao DROP CONSTRAINT fk_n_parecer_analista_tecnico;

ALTER TABLE analise.notificacao DROP COLUMN id_parecer_analista_tecnico;



