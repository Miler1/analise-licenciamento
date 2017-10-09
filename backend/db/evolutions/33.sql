# --- !Ups

--LICENCA_CANCELADA
CREATE TABLE analise.licenca_cancelada(
id SERIAL NOT NULL,
id_licenca INTEGER NOT NULL UNIQUE,
data_cancelamento INTEGER NOT NULL,
justificativa INTEGER,
id_usuario_executor INTEGER NOT NULL,
CONSTRAINT pk_licenca_cancelada PRIMARY KEY(id)
);

GRANT SELECT,INSERT,UPDATE,DELETE ON TABLE analise.licenca_cancelada TO licenciamento_pa;
GRANT SELECT, USAGE ON SEQUENCE analise.licenca_cancelada_id_seq TO licenciamento_pa;

COMMENT ON TABLE analise.licenca_cancelada IS 'Entidade responsável por armazenar as licenças canceladas.';
COMMENT ON COLUMN analise.licenca_cancelada.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.licenca_cancelada.id_licenca IS 'Identificador da entidade licenciamento.licenca que realiza o relacionamento entre as entidades licenca_cancelada e licenciamento.licenca.';
COMMENT ON COLUMN analise.licenca_cancelada.data_cancelamento IS 'Data do cancelamento da licença.';
COMMENT ON COLUMN analise.licenca_cancelada.justificativa IS 'Justificativa do cancelamento da licença.';
COMMENT ON COLUMN analise.licenca_cancelada.id_usuario_executor IS 'Identificador da entidade portal_seguranca.usuario que realiza o relacionamento entre as entidades licenca_cancelada e portal_seguranca.usuario.Identifica o usuário executor da ação.';

--DISPENSA_CANCELADA
CREATE TABLE analise.dispensa_licencamento_cancelada(
id SERIAL NOT NULL,
id_dispensa_licencamento INTEGER NOT NULL UNIQUE,
data_cancelamento INTEGER NOT NULL,
justificativa INTEGER,
id_usuario_executor INTEGER NOT NULL,
CONSTRAINT pk_dispensa_licencamento_cancelada PRIMARY KEY(id)
);

GRANT SELECT,INSERT,UPDATE,DELETE ON TABLE analise.dispensa_licencamento_cancelada TO licenciamento_pa;
GRANT SELECT, USAGE ON SEQUENCE analise.dispensa_licencamento_cancelada_id_seq TO licenciamento_pa;

COMMENT ON TABLE analise.dispensa_licencamento_cancelada IS 'Entidade responsável por armazenar as Dispensas de licenciamento Ambiental canceladas.';
COMMENT ON COLUMN analise.dispensa_licencamento_cancelada.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.dispensa_licencamento_cancelada.id_dispensa_licencamento IS 'Identificador da entidade licenciamento.dispensa_licenciamento que realiza o relacionamento entre as entidades dispensa_licencamento_cancelada e licenciamento.dispensa_licenciamento.';
COMMENT ON COLUMN analise.dispensa_licencamento_cancelada.data_cancelamento IS 'Data do cancelamento da Dispensa de licenciamento Ambiental.';
COMMENT ON COLUMN analise.dispensa_licencamento_cancelada.justificativa IS 'Justificativa do cancelamento da Dispensa de licenciamento Ambiental.';
COMMENT ON COLUMN analise.dispensa_licencamento_cancelada.id_usuario_executor IS 'Identificador da entidade portal_seguranca.usuario que realiza o relacionamento entre as entidades dispensa_licencamento_cancelada e portal_seguranca.usuario.Identifica o usuário executor da ação.';

--ALTERAÇÃO NA TABELA analise.suspensao

ALTER TABLE analise.suspensao RENAME CONSTRAINT pk_suspensao TO pk_licenca_suspensa;

ALTER TABLE analise.suspensao RENAME id_usuario_suspensao  TO id_usuario_executor;

ALTER TABLE analise.suspensao RENAME qtde_dias_suspensao  TO quantidade_dias_suspensao;

ALTER TABLE analise.suspensao RENAME TO licenca_suspensa;

# --- !Downs

DROP TABLE analise.licenca_cancelada;

DROP TABLE analise.dispensa_licencamento_cancelada;

--ALTERAÇÃO NA TABELA analise.suspensao

ALTER TABLE analise.licenca_suspensa RENAME TO suspensao;

ALTER TABLE analise.suspensao RENAME CONSTRAINT pk_licenca_suspensa  TO pk_suspensao;

ALTER TABLE analise.suspensao RENAME id_usuario_executor TO id_usuario_suspensao;

ALTER TABLE analise.suspensao RENAME quantidade_dias_suspensao TO qtde_dias_suspensao;