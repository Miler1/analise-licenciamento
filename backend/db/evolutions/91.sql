# --- !Ups

ALTER TABLE analise.desvinculo ADD COLUMN id_usuario INTEGER;
COMMENT ON COLUMN analise.desvinculo.id_usuario IS 'Identificador da entidade usuario_analise';

ALTER TABLE analise.desvinculo ADD COLUMN id_usuario_destino INTEGER;
COMMENT ON COLUMN analise.desvinculo.id_usuario_destino IS 'Indentificador da entidade usuario_analise para guardar o destinatário da analise';

ALTER SEQUENCE analise.gerente_tecnico_id_seq RENAME TO gerente_id_seq;

ALTER TABLE analise.gerente ALTER COLUMN id_analise_tecnica DROP NOT NULL;
ALTER TABLE analise.gerente ALTER COLUMN id_analise_geo DROP NOT NULL;


# --- !Downs

ALTER TABLE analise.gerente ALTER COLUMN id_analise_tecnica SET NULL;

ALTER TABLE analise.gerente ALTER COLUMN id_analise_geo SET NULL;

ALTER SEQUENCE analise.gerente_id_seq RENAME TO gerente_tecnico_id_seq;

ALTER TABLE analise.desvinculo DROP COLUMN id_usuario_destino;
ALTER TABLE analise.desvinculo DROP COLUMN id_usuario;