# --- !Ups

--- Adicionando atributo na entidade analise_tecnica_manejo

ALTER TABLE analise.analise_tecnica_manejo ADD COLUMN conclusao TEXT;


--- Criando entidade consideracao

CREATE TABLE analise.consideracao (
 id SERIAL NOT NULL,
 texto TEXT NOT NULL,
 CONSTRAINT pk_consideracao PRIMARY KEY (id)
);

COMMENT ON TABLE analise.consideracao IS 'Entidade responsável por armazenas as considerações da análise do manejo.';
COMMENT ON COLUMN analise.consideracao.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.consideracao.texto IS 'Conteúdo da consideração.';

ALTER TABLE analise.consideracao OWNER TO postgres;
GRANT ALL ON TABLE analise.consideracao TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.consideracao TO licenciamento_pa;
GRANT SELECT, USAGE ON SEQUENCE analise.consideracao_id_seq TO licenciamento_pa;

INSERT INTO analise.consideracao (texto) VALUES
 ('Toda a análise da GEOTEC foi realizada com base em dados apresentados pelo próprio Técnico Responsável, passível de sanções administrativas conforme art. 66 da Lei n° 9605 de 12/02/1998 e art. 82 do Decreto n° 6514 de 22/07/2008.'),
 ('As informações utilizadas, até a presente data, para analise do processo da GEOTEC, foram disponibilizadas do Banco de Dados de Raster e Vetores pela gerencia GTDI.'),
 ('No processo CONSTA o Relatório Técnico de Georreferenciamento do Imóvel, em meio analógico e digital, conforme as normas técnicas do INCRA (Lei 10.267/01).'),
 ('De acordo com a INSTRUÇÃO NORMATIVA Nº 001, de 14 de janeiro de 2014, a APAT não permite o início das atividades de manejo, não autoriza a exploração florestal e nem se constitui em prova de posse ou propriedade para fins de regularização fundiários, de autorização de desmatamento ou de obtenção de financiamento junto às instituições de crédito públicas ou privados;');


-- Criando da entidade vinculo_analise_tecnica_manejo_consideracao

CREATE TABLE analise.vinculo_analise_tecnica_manejo_consideracao (
 id SERIAL NOT NULL,
 id_analise_tecnica_manejo INTEGER NOT NULL,
 id_consideracao INTEGER NOT NULL,
 exibir_pdf BOOLEAN NOT NULL DEFAULT TRUE,
 CONSTRAINT pk_vinculo_analise_tecnica_manejo_consideracao PRIMARY KEY (id),
 CONSTRAINT fk_vatcm_analise_tecnica_manejo FOREIGN KEY (id_analise_tecnica_manejo) REFERENCES analise.analise_tecnica_manejo (id),
 CONSTRAINT fk_vatcm_consideracao FOREIGN KEY (id_consideracao) REFERENCES analise.consideracao (id)
);

COMMENT ON TABLE analise.vinculo_analise_tecnica_manejo_consideracao IS 'Entidade responsável por armazenar o relacionamento entre uma análise tecnica do manejo e uma consideração.';
COMMENT ON COLUMN analise.vinculo_analise_tecnica_manejo_consideracao.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.vinculo_analise_tecnica_manejo_consideracao.id_analise_tecnica_manejo IS 'Identificador da entidade analise_tecnica_manejo.';
COMMENT ON COLUMN analise.vinculo_analise_tecnica_manejo_consideracao.id_consideracao IS 'Identificador da entidade consideracao.';
COMMENT ON COLUMN analise.vinculo_analise_tecnica_manejo_consideracao.exibir_pdf IS 'Flag de exibição no pdf da análise.';

ALTER TABLE analise.vinculo_analise_tecnica_manejo_consideracao OWNER TO postgres;
GRANT ALL ON TABLE analise.vinculo_analise_tecnica_manejo_consideracao TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.vinculo_analise_tecnica_manejo_consideracao TO licenciamento_pa;
GRANT SELECT, USAGE ON SEQUENCE analise.vinculo_analise_tecnica_manejo_consideracao_id_seq TO licenciamento_pa;


-- Criando da entidade embasamento_legal

CREATE TABLE analise.embasamento_legal (
 id SERIAL NOT NULL,
 texto TEXT NOT NULL,
 CONSTRAINT pk_embasamento_legal PRIMARY KEY (id)
);

COMMENT ON TABLE analise.embasamento_legal IS 'Entidade responsável por armazenas os embasamentos legais da análise do manejo.';
COMMENT ON COLUMN analise.embasamento_legal.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.embasamento_legal.texto IS 'Conteúdo do embasamento.';

ALTER TABLE analise.embasamento_legal OWNER TO postgres;
GRANT ALL ON TABLE analise.embasamento_legal TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.embasamento_legal TO licenciamento_pa;
GRANT SELECT, USAGE ON SEQUENCE analise.embasamento_legal_id_seq TO licenciamento_pa;

INSERT INTO analise.embasamento_legal (texto) VALUES
 ('Código Florestal - Lei n°12651/2012'),
 ('Instrução Normativa n°02/2014'),
 ('Decreto n°7830/2012'),
 ('Instrução Normativa n° 01/2014'),
 ('Portaria n° 63/2014'),
 ('Instrução Normativa n°14/2011'),
 ('Decreto n° 216/2011'),
 ('Lei Federal 10.267/01'),
 ('Código Florestal - Lei n°12651/2012'),
 ('Lei n° 9605 de 12/02/1998'),
 ('Decreto n° 6514 de 22/07/2008'),
 ('Resolução CONAMA nº 378/2006');


-- Criando da entidade vinculo_analise_tecnica_manejo_embasamento_legal

CREATE TABLE analise.vinculo_analise_tecnica_manejo_embasamento_legal (
 id SERIAL NOT NULL,
 id_analise_tecnica_manejo INTEGER NOT NULL,
 id_embasamento_legal INTEGER NOT NULL,
 exibir_pdf BOOLEAN NOT NULL DEFAULT TRUE,
 CONSTRAINT pk_vinculo_analise_tecnica_manejo_embasamento_legal PRIMARY KEY (id),
 CONSTRAINT fk_vatel_analise_tecnica_manejo FOREIGN KEY (id_analise_tecnica_manejo) REFERENCES analise.analise_tecnica_manejo (id),
 CONSTRAINT fk_vatel_embasamento_legal FOREIGN KEY (id_embasamento_legal) REFERENCES analise.embasamento_legal (id)
);

COMMENT ON TABLE analise.vinculo_analise_tecnica_manejo_embasamento_legal IS 'Entidade responsável por armazenar o relacionamento entre uma análise tecnica do manejo e um embasamento legal.';
COMMENT ON COLUMN analise.vinculo_analise_tecnica_manejo_embasamento_legal.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.vinculo_analise_tecnica_manejo_embasamento_legal.id_analise_tecnica_manejo IS 'Identificador da entidade analise_tecnica_manejo.';
COMMENT ON COLUMN analise.vinculo_analise_tecnica_manejo_embasamento_legal.id_embasamento_legal IS 'Identificador da entidade embasamento_legal.';
COMMENT ON COLUMN analise.vinculo_analise_tecnica_manejo_embasamento_legal.exibir_pdf IS 'Flag de exibição no pdf da análise.';

ALTER TABLE analise.vinculo_analise_tecnica_manejo_embasamento_legal OWNER TO postgres;
GRANT ALL ON TABLE analise.vinculo_analise_tecnica_manejo_embasamento_legal TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.vinculo_analise_tecnica_manejo_embasamento_legal TO licenciamento_pa;
GRANT SELECT, USAGE ON SEQUENCE analise.vinculo_analise_tecnica_manejo_embasamento_legal_id_seq TO licenciamento_pa;


# --- !Downs

DROP TABLE analise.vinculo_analise_tecnica_manejo_embasamento_legal;
DROP TABLE analise.embasamento_legal;

DROP TABLE analise.vinculo_analise_tecnica_manejo_consideracao;
DROP TABLE analise.consideracao;

ALTER TABLE analise.analise_tecnica_manejo DROP COLUMN conclusao;