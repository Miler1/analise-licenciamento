# --- !Ups

CREATE TABLE analise.dia_analise(
id SERIAL NOT NULL,
id_analise INTEGER NOT NULL,
qtde_dias_analise INTEGER NOT NULL,
qtde_dias_juridica INTEGER,
qtde_dias_tecnica INTEGER,
CONSTRAINT pk_dia_analise PRIMARY KEY(id),
CONSTRAINT fk_da_analise FOREIGN KEY(id_analise) REFERENCES analise.analise(id)
);
GRANT SELECT,INSERT,UPDATE,DELETE ON TABLE analise.dia_analise TO licenciamento_pa;
GRANT SELECT, USAGE ON SEQUENCE analise.dia_analise_id_seq TO licenciamento_pa;
ALTER TABLE analise.dia_analise OWNER TO licenciamento_pa;

COMMENT ON TABLE analise.dia_analise IS 'Entidade responsável por armazenar a contagem de dias da análise em cada etapa.';
COMMENT ON COLUMN analise.dia_analise.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.dia_analise.id_analise IS 'Identificador da entidade analise que faz o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.dia_analise.qtde_dias_analise IS 'Quantidade de dias da análise.';
COMMENT ON COLUMN analise.dia_analise.qtde_dias_juridica IS 'Quantidade de dias da análise Jurídica.';
COMMENT ON COLUMN analise.dia_analise.qtde_dias_tecnica IS 'Quantidade de dias da análise Técnica.';

# --- !Downs

DROP TABLE analise.dia_analise;