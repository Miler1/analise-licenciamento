
# --- !Ups

ALTER TABLE analise.parecer_analista_tecnico ADD COLUMN validade_permitida integer;
COMMENT ON COLUMN analise.parecer_analista_tecnico.validade_permitida IS 'Nova validade permitida da licença';

ALTER TABLE analise.parecer_analista_tecnico ADD COLUMN finalidade_atividade text;
COMMENT ON COLUMN analise.parecer_analista_tecnico.finalidade_atividade IS 'Finalidade da atividade da licença';

ALTER TABLE analise.condicionante DROP COLUMN ordem;

ALTER TABLE analise.condicionante DROP CONSTRAINT fk_c_licenca_analise;

ALTER TABLE analise.condicionante DROP COLUMN id_licenca_analise;

ALTER TABLE analise.condicionante ADD COLUMN id_parecer_analista_tecnico int;
COMMENT ON COLUMN analise.condicionante.id_parecer_analista_tecnico IS 'Identificador do parecer da análise técnica da condicionante';

ALTER TABLE analise.condicionante ADD 
CONSTRAINT fk_c_parecer_analista_tecnico FOREIGN KEY (id_parecer_analista_tecnico) 
        REFERENCES analise.parecer_analista_tecnico(id);


CREATE TABLE analise.restricao (
    id serial NOT NULL,       
    texto text NOT NULL,
    id_parecer_analista_tecnico integer,
    CONSTRAINT pk_restricao PRIMARY KEY(id),
    CONSTRAINT fk_r_parecer_analista_tecnico FOREIGN KEY(id_parecer_analista_tecnico)
        REFERENCES analise.parecer_analista_tecnico(id)
);
ALTER TABLE analise.restricao OWNER TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.restricao TO licenciamento_am;
GRANT SELECT, USAGE ON SEQUENCE analise.restricao_id_seq TO licenciamento_am; 
COMMENT ON TABLE analise.restricao IS 'Entidade responsável por armazenar as restrições das licença de análise.';
COMMENT ON COLUMN analise.restricao.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.restricao.texto IS 'Descrição da restrição.';
COMMENT ON COLUMN analise.restricao.id_parecer_analista_tecnico IS 'Identificador do parecer da análise técnica da condicionante';


# --- !Downs

DROP TABLE analise.restricao;

ALTER TABLE analise.condicionante DROP CONSTRAINT fk_c_parecer_analista_tecnico ;

ALTER TABLE analise.condicionante DROP COLUMN id_parecer_analista_tecnico;

ALTER TABLE analise.condicionante ADD COLUMN id_licenca_analise integer;
COMMENT ON COLUMN analise.condicionante.id_licenca_analise IS 'Identificador da entidade licenca_analise que faz o relacionamento entre as duas entidades.';

ALTER TABLE analise.condicionante ADD 
CONSTRAINT fk_c_licenca_analise FOREIGN KEY (id_licenca_analise)
    REFERENCES analise.licenca_analise (id) ;

ALTER TABLE analise.condicionante ADD COLUMN ordem integer;
COMMENT ON COLUMN analise.condicionante.ordem IS 'Ordem de exibição das condicionantes.';

ALTER TABLE analise.parecer_analista_tecnico DROP COLUMN finalidade_atividade;

ALTER TABLE analise.parecer_analista_tecnico DROP COLUMN validade_permitida;



