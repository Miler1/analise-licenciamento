
# --- !Ups

ALTER TABLE analise.notificacao ADD COLUMN id_parecer_analista_geo INTEGER NOT NULL;
COMMENT ON COLUMN analise.notificacao.id_parecer_analista_geo IS 'Identificador do parecer do analista geo';

ALTER TABLE analise.notificacao ADD CONSTRAINT fk_n_parecer_analista_geo FOREIGN KEY (id_parecer_analista_geo) REFERENCES analise.parecer_analista_geo(id); 

ALTER TABLE analise.notificacao DROP COLUMN justificativa;

# --- !Downs

ALTER TABLE analise.notificacao ADD COLUMN justificativa text;

ALTER TABLE analise.notificacao DROP CONSTRAINT fk_n_parecer_analista_geo ;

ALTER TABLE analise.notificacao DROP COLUMN id_parecer_analista_geo;


