# --- !Ups

CREATE TABLE analise.suspensao(
 id SERIAL NOT NULL,
 id_licenca INTEGER NOT NULL,
 id_usuario_suspensao INTEGER NOT NULL,
 qtde_dias_suspensao INTEGER,
 data_suspensao DATE,
 CONSTRAINT pk_suspensao PRIMARY KEY(id)
);
GRANT SELECT,INSERT,UPDATE,DELETE ON TABLE analise.suspensao TO licenciamento_am;
GRANT SELECT, USAGE ON SEQUENCE analise.suspensao_id_seq TO licenciamento_am;
ALTER TABLE analise.suspensao OWNER TO licenciamento_am;

COMMENT ON TABLE analise.suspensao IS 'Entidade responsável por armazenar a contagem de dias que cada licença fica suspensa.';
COMMENT ON COLUMN analise.suspensao.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.suspensao.id_licenca IS 'Identificador da entidade licenca que faz o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.suspensao.id_usuario_suspensao IS 'Identificador da entidade usuario que faz o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.suspensao.qtde_dias_suspensao IS 'Quantidade de dias em que a licença será suspensa.';
COMMENT ON COLUMN analise.suspensao.data_suspensao IS 'Data em que a licença foi suspensa.';

# --- !Downs

DROP TABLE analise.suspensao;