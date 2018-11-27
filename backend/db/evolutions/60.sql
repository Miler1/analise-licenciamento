# --- !Ups

-- Criando a entidade analise_juridica_manejo

CREATE TABLE analise.analise_juridica_manejo (
 id SERIAL NOT NULL,
 data TIMESTAMP NOT NULL,
 resultado_analise BOOLEAN,
 id_processo_manejo INTEGER NOT NULL,
 CONSTRAINT pk_analise_juridica_manejo PRIMARY KEY (id),
 CONSTRAINT fk_ajm_processo_manejo FOREIGN KEY (id_processo_manejo) REFERENCES analise.processo_manejo(id)
);

COMMENT ON TABLE analise.analise_juridica_manejo IS 'Entidade responsável por armazenas as análises jurídicas do manejo.';
COMMENT ON COLUMN analise.analise_juridica_manejo.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.analise_juridica_manejo.data IS 'Data da análise.';
COMMENT ON COLUMN analise.analise_juridica_manejo.resultado_analise IS 'Flag que indica o resultado da análise. True - Deferido, False - Indeferido.';

ALTER TABLE analise.analise_juridica_manejo OWNER TO postgres;
GRANT ALL ON TABLE analise.analise_juridica_manejo TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.analise_juridica_manejo TO licenciamento_pa;
GRANT SELECT, USAGE ON SEQUENCE analise.analise_juridica_manejo_id_seq TO licenciamento_pa;

-- Criando entidade consultor_juridico_manejo

CREATE TABLE analise.consultor_juridico_manejo (
 id SERIAL NOT NULL,
 data_vinculacao TIMESTAMP NOT NULL,
 id_analise_juridica_manejo INTEGER NOT NULL,
 id_usuario INTEGER NOT NULL,
 CONSTRAINT pk_consultor_juridico_manejo PRIMARY KEY (id),
 CONSTRAINT fk_cjm_analise_juridica_manejo FOREIGN KEY (id_analise_juridica_manejo) REFERENCES analise.analise_juridica_manejo(id),
 CONSTRAINT fk_cjm_usuario FOREIGN KEY (id_usuario) REFERENCES portal_seguranca.usuario(id)
);

COMMENT ON TABLE analise.consultor_juridico_manejo IS 'Entidade responsável por armazenas os consultores jurídicos do manejo.';
COMMENT ON COLUMN analise.consultor_juridico_manejo.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.consultor_juridico_manejo.data_vinculacao IS 'Data da vinculação do analista a análise.';
COMMENT ON COLUMN analise.consultor_juridico_manejo.id_analise_juridica_manejo IS 'Identificador da entidade analise_juridica_manejo que faz o relacionamento entre a análise jurídica do manejo e o consultor jurídico.';
COMMENT ON COLUMN analise.consultor_juridico_manejo.id_usuario IS 'Identificador da entidade usuario que faz o relacionamento entre um usuário e o consultor.';

ALTER TABLE analise.consultor_juridico_manejo OWNER TO postgres;
GRANT ALL ON TABLE analise.consultor_juridico_manejo TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.consultor_juridico_manejo TO licenciamento_pa;
GRANT SELECT, USAGE ON SEQUENCE analise.consultor_juridico_manejo_id_seq TO licenciamento_pa;

-- Criando a entidade consideracao

CREATE TABLE analise.consideracao (
 id SERIAL NOT NULL,
 texto TEXT NOT NULL,
 id_analise_juridica_manejo INTEGER NOT NULL,
 num_passo SMALLINT NOT NULL,
 data TIMESTAMP NOT NULL,
 CONSTRAINT pk_consideracao PRIMARY KEY(id),
 CONSTRAINT fk_c_analise_juridica_manejo FOREIGN KEY (id_analise_juridica_manejo) REFERENCES analise.analise_juridica_manejo (id)
);

COMMENT ON TABLE analise.consideracao IS 'Entidade responsável por armazenas as considerações de uma análise jurídica do manejo.';
COMMENT ON COLUMN analise.consideracao.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.consideracao.texto IS 'Conteúdo da consideração.';
COMMENT ON COLUMN analise.consideracao.id_analise_juridica_manejo IS 'Identificador da entidade analise_juridica_manejo que faz o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.consideracao.num_passo IS 'Número do passo em que a consideração foi inserida.';
COMMENT ON COLUMN analise.consideracao.data IS 'Data da consideração.';

ALTER TABLE analise.consideracao OWNER TO postgres;
GRANT ALL ON TABLE analise.consideracao TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.consideracao TO licenciamento_pa;
GRANT SELECT, USAGE ON SEQUENCE analise.consideracao_id_seq TO licenciamento_pa;

# --- !Downs

DROP TABLE analise.consideracao;

DROP TABLE analise.consultor_juridico_manejo;

DROP TABLE analise.analise_juridica_manejo;