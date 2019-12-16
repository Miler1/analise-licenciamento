
# --- !Ups

ALTER TABLE analise.parecer_analista_geo ALTER COLUMN id_historico_tramitacao DROP NOT NULL;

# --- !Downs

ALTER TABLE analise.parecer_analista_geo ALTER COLUMN id_historico_tramitacao SET NOT NULL;
