# --- !Ups

BEGIN;

ALTER TABLE analise.analise_juridica DROP CONSTRAINT fk_aj_usuario;
ALTER TABLE analise.analise_juridica DROP CONSTRAINT fk_aj_usuario_validacao_aprovador;
ALTER TABLE analise.analise_tecnica DROP CONSTRAINT fk_at_usuario;
ALTER TABLE analise.analise_tecnica DROP CONSTRAINT fk_at_usuario_validacao_aprovador;
ALTER TABLE analise.analise_tecnica DROP CONSTRAINT fk_at_usuario_validacao_gerente;
ALTER TABLE analise.analista_tecnico DROP CONSTRAINT fk_at_usuario;
ALTER TABLE analise.analista_tecnico_manejo DROP CONSTRAINT fk_antm_usuario;
ALTER TABLE analise.consultor_juridico DROP CONSTRAINT fk_cj_usuario;
ALTER TABLE analise.dispensa_licencamento_cancelada DROP CONSTRAINT fk_dlc_usuario_executor;
ALTER TABLE analise.gerente_tecnico DROP CONSTRAINT fk_gt_usuario;
ALTER TABLE analise.licenca_cancelada DROP CONSTRAINT fk_lc_usuario_executor;
ALTER TABLE analise.licenca_suspensa DROP CONSTRAINT fk_ls_usuario_executor;

ALTER TABLE analise.analise_juridica ADD CONSTRAINT fk_aj_usuario_analise FOREIGN KEY (id_usuario_validacao) REFERENCES analise.usuario_analise (id);
ALTER TABLE analise.analise_juridica ADD CONSTRAINT fk_aj_usuario_analise_validacao_aprovador FOREIGN KEY (id_usuario_validacao_aprovador) REFERENCES analise.usuario_analise (id);
ALTER TABLE analise.analise_tecnica ADD CONSTRAINT fk_at_usuario_analise FOREIGN KEY (id_usuario_validacao) REFERENCES analise.usuario_analise (id);
ALTER TABLE analise.analise_tecnica ADD CONSTRAINT fk_at_usuario_analise_validacao_aprovador FOREIGN KEY (id_usuario_validacao_aprovador) REFERENCES analise.usuario_analise (id);
ALTER TABLE analise.analise_tecnica ADD CONSTRAINT fk_at_usuario_analise_validacao_gerente FOREIGN KEY (id_usuario_validacao_gerente) REFERENCES analise.usuario_analise (id);
ALTER TABLE analise.analista_tecnico ADD CONSTRAINT fk_at_usuario_analise FOREIGN KEY (id_usuario) REFERENCES analise.usuario_analise (id);
ALTER TABLE analise.analista_tecnico_manejo ADD CONSTRAINT fk_antm_usuario_analise FOREIGN KEY (id_usuario) REFERENCES analise.usuario_analise (id);
ALTER TABLE analise.consultor_juridico ADD CONSTRAINT fk_cj_usuario_analise FOREIGN KEY (id_usuario) REFERENCES analise.usuario_analise (id);
ALTER TABLE analise.dispensa_licencamento_cancelada ADD CONSTRAINT fk_dlc_usuario_analise_executor FOREIGN KEY (id_usuario_executor) REFERENCES analise.usuario_analise (id);
ALTER TABLE analise.gerente_tecnico ADD CONSTRAINT fk_gt_usuario_analise FOREIGN KEY (id_usuario) REFERENCES analise.usuario_analise (id);
ALTER TABLE analise.licenca_cancelada ADD CONSTRAINT fk_lc_usuario_analise_executor FOREIGN KEY (id_usuario_executor) REFERENCES analise.usuario_analise (id);
ALTER TABLE analise.licenca_suspensa ADD CONSTRAINT fk_ls_usuario_analise_executor FOREIGN KEY (id_usuario_executor) REFERENCES analise.usuario_analise (id);

COMMENT ON COLUMN analise.analise_juridica.id_usuario_validacao_aprovador IS 'Campo responsável por armazernar o aprovador que fez a validação.';
COMMENT ON COLUMN analise.analise_juridica.id_usuario_validacao IS 'Identificador da entidade analise.usuario_analise que realizará o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.analise_tecnica.id_usuario_validacao_aprovador IS 'Campo responsável por armazernar o aprovador que fez a validação.';
COMMENT ON COLUMN analise.analise_tecnica.id_usuario_validacao_gerente IS 'Campo responsável por armazernar o gerente que fez a validação.';
COMMENT ON COLUMN analise.analise_tecnica.id_usuario_validacao IS 'Identificador da entidade analise.usuario_analise que realizará o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.analista_tecnico.id_usuario IS 'Identificador da entidade usuario que faz o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.analista_tecnico_manejo.id_usuario IS 'Identificador da entidade usuario que faz o relacionamento entre usuario_analise e o analista.';
COMMENT ON COLUMN analise.consultor_juridico.id_usuario IS 'Identificador da tabela usuario, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.dispensa_licencamento_cancelada.id_usuario_executor IS 'Identificador da entidade analise.usuario_analise que realiza o relacionamento entre as entidades dispensa_licencamento_cancelada e portal_analise.usuario_analise identifica o usuário executor da ação.';
COMMENT ON COLUMN analise.gerente_tecnico.id_usuario IS 'Identificador da entidade analise.usuario_analise que realiza o relacionamento entre as entidades gerente_tecnico e analise.usuario_analise.';
COMMENT ON COLUMN analise.licenca_cancelada.id_usuario_executor IS 'Identificador da entidade analise.usuario_analise que realiza o relacionamento entre as entidades licenca_cancelada e portal_analise.usuario_analise identifica o usuário executor da ação.';
COMMENT ON COLUMN analise.licenca_suspensa.id_usuario_executor IS 'Identificador da entidade analise.usuario_analise que faz o relacionamento entre as duas entidades.';

COMMIT;

# --- !Downs

BEGIN;

ALTER TABLE analise.analise_juridica DROP CONSTRAINT fk_aj_usuario;
ALTER TABLE analise.analise_juridica DROP CONSTRAINT fk_aj_usuario_validacao_aprovador;
ALTER TABLE analise.analise_tecnica DROP CONSTRAINT fk_at_usuario;
ALTER TABLE analise.analise_tecnica DROP CONSTRAINT fk_at_usuario_validacao_aprovador;
ALTER TABLE analise.analise_tecnica DROP CONSTRAINT fk_at_usuario_validacao_gerente;
ALTER TABLE analise.analista_tecnico DROP CONSTRAINT fk_at_usuario;
ALTER TABLE analise.analista_tecnico_manejo DROP CONSTRAINT fk_antm_usuario;
ALTER TABLE analise.consultor_juridico DROP CONSTRAINT fk_cj_usuario;
ALTER TABLE analise.dispensa_licencamento_cancelada DROP CONSTRAINT fk_dlc_usuario_executor;
ALTER TABLE analise.gerente_tecnico DROP CONSTRAINT fk_gt_usuario;
ALTER TABLE analise.licenca_cancelada DROP CONSTRAINT fk_lc_usuario_executor;
ALTER TABLE analise.licenca_suspensa DROP CONSTRAINT fk_ls_usuario_executor;

ALTER TABLE analise.analise_juridica ADD CONSTRAINT fk_aj_usuario_analise FOREIGN KEY (id_usuario_validacao) REFERENCES portal_seguranca.usuario (id);
ALTER TABLE analise.analise_juridica ADD CONSTRAINT fk_aj_usuario_analise_validacao_aprovador FOREIGN KEY (id_usuario_validacao_aprovador) REFERENCES portal_seguranca.usuario (id);
ALTER TABLE analise.analise_tecnica ADD CONSTRAINT fk_at_usuario_analise FOREIGN KEY (id_usuario_validacao) REFERENCES portal_seguranca.usuario (id);
ALTER TABLE analise.analise_tecnica ADD CONSTRAINT fk_at_usuario_analise_validacao_aprovador FOREIGN KEY (id_usuario_validacao_aprovador) REFERENCES portal_seguranca.usuario (id);
ALTER TABLE analise.analise_tecnica ADD CONSTRAINT fk_at_usuario_analise_validacao_gerente FOREIGN KEY (id_usuario_validacao_gerente) REFERENCES portal_seguranca.usuario (id);
ALTER TABLE analise.analista_tecnico ADD CONSTRAINT fk_at_usuario_analise FOREIGN KEY (id_usuario) REFERENCES portal_seguranca.usuario (id);
ALTER TABLE analise.analista_tecnico_manejo ADD CONSTRAINT fk_antm_usuario_analise FOREIGN KEY (id_usuario) REFERENCES portal_seguranca.usuario (id);
ALTER TABLE analise.consultor_juridico ADD CONSTRAINT fk_cj_usuario_analise FOREIGN KEY (id_usuario) REFERENCES portal_seguranca.usuario (id);
ALTER TABLE analise.dispensa_licencamento_cancelada ADD CONSTRAINT fk_dlc_usuario_analise_executor FOREIGN KEY (id_usuario_executor) REFERENCES portal_seguranca.usuario (id);
ALTER TABLE analise.gerente_tecnico ADD CONSTRAINT fk_gt_usuario_analise FOREIGN KEY (id_usuario) REFERENCES portal_seguranca.usuario (id);
ALTER TABLE analise.licenca_cancelada ADD CONSTRAINT fk_lc_usuario_analise_executor FOREIGN KEY (id_usuario_executor) REFERENCES portal_seguranca.usuario (id);
ALTER TABLE analise.licenca_suspensa ADD CONSTRAINT fk_ls_usuario_analise_executor FOREIGN KEY (id_usuario_executor) REFERENCES portal_seguranca.usuario (id);

COMMENT ON COLUMN analise.analise_juridica.id_usuario_validacao_aprovador IS 'Campo responsável por armazernar o aprovador que fez a validação.';
COMMENT ON COLUMN analise.analise_juridica.id_usuario_validacao IS 'Identificador da entidade portal_seguranca.usuario que realizará o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.analise_tecnica.id_usuario_validacao_aprovador IS 'Campo responsável por armazernar o aprovador que fez a validação.';
COMMENT ON COLUMN analise.analise_tecnica.id_usuario_validacao_gerente IS 'Campo responsável por armazernar o gerente que fez a validação.';
COMMENT ON COLUMN analise.analise_tecnica.id_usuario_validacao IS 'Identificador da entidade portal_seguranca.usuario que realizará o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.analista_tecnico.id_usuario IS 'Identificador da entidade usuario que faz o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.analista_tecnico_manejo.id_usuario IS 'Identificador da entidade usuario que faz o relacionamento entre um usuário e o análista.';
COMMENT ON COLUMN analise.consultor_juridico.id_usuario IS 'Identificador da tabela usuario, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.dispensa_licencamento_cancelada.id_usuario_executor IS 'Identificador da entidade portal_seguranca.usuario que realiza o relacionamento entre as entidades dispensa_licencamento_cancelada e portal_seguranca.usuario.Identifica o usuário executor da ação.';
COMMENT ON COLUMN analise.gerente_tecnico.id_usuario IS 'Identificador da entidade portal_seguranca.usuario que realiza o relacionamento entre as entidades gerente_tecnico e portal_seguranca.usuario.';
COMMENT ON COLUMN analise.licenca_cancelada.id_usuario_executor IS 'Identificador da entidade portal_seguranca.usuario que realiza o relacionamento entre as entidades licenca_cancelada e portal_seguranca.usuario.Identifica o usuário executor da ação.';
COMMENT ON COLUMN analise.licenca_suspensa.id_usuario_executor IS 'Identificador da entidade usuario que faz o relacionamento entre as duas entidades.';

COMMIT;