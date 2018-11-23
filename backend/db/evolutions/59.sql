# --- !Ups

-- Alterando a entidade analise_vetorial para incluir flag de exibição no pdf

ALTER TABLE analise.analise_vetorial ADD COLUMN exibir_pdf BOOLEAN NOT NULL DEFAULT TRUE;
COMMENT ON COLUMN analise.analise_vetorial.exibir_pdf IS 'Flag de exibição no pdf da análise.';

-- Alterando a entidade analise_ndfi para incluir flag de exibição no pdf

ALTER TABLE analise.analise_ndfi ADD COLUMN exibir_pdf BOOLEAN NOT NULL DEFAULT TRUE;
COMMENT ON COLUMN analise.analise_ndfi.exibir_pdf IS 'Flag de exibição no pdf da análise.';

-- Criando da entidade insumo

CREATE TABLE analise.insumo (
 id SERIAL NOT NULL,
 data TIMESTAMP NOT NULL,
 satelite VARCHAR(200) NOT NULL,
 orb_ponto VARCHAR(200) NOT NULL,
 CONSTRAINT pk_insumo PRIMARY KEY (id)
);

COMMENT ON TABLE analise.insumo IS 'Entidade responsável por armazenas os insumos de uma análise do manejo.';
COMMENT ON COLUMN analise.insumo.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.insumo.data IS 'Data do insumo.';
COMMENT ON COLUMN analise.insumo.satelite IS 'Nome do satelite utilizado.';
COMMENT ON COLUMN analise.insumo.orb_ponto IS 'Orb e ponto utilizados.';

ALTER TABLE analise.insumo OWNER TO postgres;
GRANT ALL ON TABLE analise.insumo TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.insumo TO licenciamento_pa;
GRANT SELECT, USAGE ON SEQUENCE analise.insumo_id_seq TO licenciamento_pa;

-- Criando da entidade vinculo_analise_tecnica_manejo_insumo

CREATE TABLE analise.vinculo_analise_tecnica_manejo_insumo (
 id SERIAL NOT NULL,
 id_analise_tecnica_manejo INTEGER NOT NULL,
 id_insumo INTEGER NOT NULL,
 exibir_pdf BOOLEAN NOT NULL DEFAULT TRUE,
 CONSTRAINT pk_vinculo_analise_tecnica_manejo_insumo PRIMARY KEY (id),
 CONSTRAINT fk_rati_analise_tecnica_manejo FOREIGN KEY (id_analise_tecnica_manejo) REFERENCES analise.analise_tecnica_manejo (id),
 CONSTRAINT fk_rati_insumo FOREIGN KEY (id_insumo) REFERENCES analise.insumo (id)
);

COMMENT ON TABLE analise.vinculo_analise_tecnica_manejo_insumo IS 'Entidade responsável por armazenar o relacionamento entre uma análise tecnica do manejo e um insumo.';
COMMENT ON COLUMN analise.vinculo_analise_tecnica_manejo_insumo.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.vinculo_analise_tecnica_manejo_insumo.id_analise_tecnica_manejo IS 'Identificador da entidade analise_tecnica_manejo.';
COMMENT ON COLUMN analise.vinculo_analise_tecnica_manejo_insumo.id_insumo IS 'Identificador da entidade insumo.';
COMMENT ON COLUMN analise.vinculo_analise_tecnica_manejo_insumo.exibir_pdf IS 'Flag de exibição no pdf da análise.';

ALTER TABLE analise.vinculo_analise_tecnica_manejo_insumo OWNER TO postgres;
GRANT ALL ON TABLE analise.vinculo_analise_tecnica_manejo_insumo TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.vinculo_analise_tecnica_manejo_insumo TO licenciamento_pa;
GRANT SELECT, USAGE ON SEQUENCE analise.vinculo_analise_tecnica_manejo_insumo_id_seq TO licenciamento_pa;

# --- !Downs

DROP TABLE analise.vinculo_analise_tecnica_manejo_insumo;

DROP TABLE analise.insumo;

ALTER TABLE analise.analise_ndfi DROP COLUMN exibir_pdf;

ALTER TABLE analise.analise_vetorial DROP COLUMN exibir_pdf;