# --- !Ups

CREATE TABLE analise.dispensa_licenciamento_cancelada (
  id serial NOT NULL,
  id_dispensa_licenciamento integer NOT NULL,
  justificativa text,
  id_usuario_executor integer NOT NULL,
  data_cancelamento timestamp without time zone NOT NULL,
  CONSTRAINT pk_dispensa_licenciamento_cancelada PRIMARY KEY (id),
  CONSTRAINT dispensa_licenciamento_cancelada_id_dispensa_licencamento_key UNIQUE (id_dispensa_licenciamento)
);
ALTER TABLE analise.dispensa_licenciamento_cancelada OWNER TO postgres;
GRANT ALL ON TABLE analise.dispensa_licenciamento_cancelada TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.dispensa_licenciamento_cancelada TO licenciamento_am;
GRANT SELECT, USAGE ON SEQUENCE analise.dispensa_licenciamento_cancelada_id_seq TO licenciamento_am; 
COMMENT ON TABLE analise.dispensa_licenciamento_cancelada IS 'Entidade responsável por armazenar as Dispensas de licenciamento Ambiental canceladas.';
COMMENT ON COLUMN analise.dispensa_licenciamento_cancelada.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.dispensa_licenciamento_cancelada.id_dispensa_licenciamento IS 'Identificador da entidade licenciamento.dispensa_licenciamento que realiza o relacionamento entre as entidades dispensa_licencamento_cancelada e licenciamento.dispensa_licenciamento.';
COMMENT ON COLUMN analise.dispensa_licenciamento_cancelada.justificativa IS 'Justificativa do cancelamento da Dispensa de licenciamento Ambiental.';
COMMENT ON COLUMN analise.dispensa_licenciamento_cancelada.id_usuario_executor IS 'Identifica o usuário executor da ação.';
COMMENT ON COLUMN analise.dispensa_licenciamento_cancelada.data_cancelamento IS 'Data do cancelamento da Dispensa de licenciamento Ambiental.';

CREATE TABLE analise.dispensa_licenciamento_suspensa (
  id serial NOT NULL,
  id_dispensa_licenciamento integer NOT NULL,
  justificativa text,
  id_usuario_executor integer NOT NULL,
  data_suspensao timestamp without time zone NOT NULL,
  ativo boolean DEFAULT TRUE NOT NULL,
  quantidade_dias_suspensao integer,
  CONSTRAINT pk_dispensa_licenciamento_suspensa PRIMARY KEY (id),
  CONSTRAINT dispensa_licenciamento_suspensa_id_dispensa_licencamento_key UNIQUE (id_dispensa_licenciamento)
);
ALTER TABLE analise.dispensa_licenciamento_suspensa OWNER TO postgres;
GRANT ALL ON TABLE analise.dispensa_licenciamento_suspensa TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.dispensa_licenciamento_suspensa TO licenciamento_am;
GRANT SELECT, USAGE ON SEQUENCE analise.dispensa_licenciamento_suspensa_id_seq TO licenciamento_am; 
COMMENT ON TABLE analise.dispensa_licenciamento_suspensa IS 'Entidade responsável por armazenar as Dispensas de licenciamento Ambiental suspensas.';
COMMENT ON COLUMN analise.dispensa_licenciamento_suspensa.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.dispensa_licenciamento_suspensa.id_dispensa_licenciamento IS 'Identificador da entidade licenciamento.dispensa_licenciamento que realiza o relacionamento entre as entidades dispensa_licencamento_suspensa e licenciamento.dispensa_licenciamento.';
COMMENT ON COLUMN analise.dispensa_licenciamento_suspensa.justificativa IS 'Justificativa da suspensão da Dispensa de licenciamento Ambiental.';
COMMENT ON COLUMN analise.dispensa_licenciamento_suspensa.id_usuario_executor IS 'Identifica o usuário executor da ação.';
COMMENT ON COLUMN analise.dispensa_licenciamento_suspensa.data_suspensao IS 'Data da suspensão da Dispensa de licenciamento Ambiental.';
COMMENT ON COLUMN analise.dispensa_licenciamento_suspensa.quantidade_dias_suspensao is 'Quantidade de dias em que a licença será suspensa.';
COMMENT ON COLUMN analise.dispensa_licenciamento_suspensa.ativo is 'Indica se a suspensão está ativa (TRUE - Ativa; FALSE - Inativa).';



# --- !Downs

DROP TABLE analise.dispensa_licenciamento_suspensa;

DROP TABLE analise.dispensa_licenciamento_cancelada;