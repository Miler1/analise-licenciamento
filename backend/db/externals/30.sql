# --- !Ups

ALTER TABLE analise.licenca_cancelada
ADD CONSTRAINT fk_lc_licenca FOREIGN KEY(id_licenca) REFERENCES licenciamento.licenca(id);

ALTER TABLE analise.licenca_cancelada
ADD CONSTRAINT fk_lc_usuario_executor FOREIGN KEY (id_usuario_executor) REFERENCES portal_seguranca.usuario (id); 

ALTER TABLE analise.dispensa_licencamento_cancelada
ADD CONSTRAINT fk_dlc_dispensa_licenciamento FOREIGN KEY(id_dispensa_licencamento) REFERENCES licenciamento.dispensa_licenciamento(id);

ALTER TABLE analise.dispensa_licencamento_cancelada
ADD CONSTRAINT fk_dlc_usuario_executor FOREIGN KEY (id_usuario_executor) REFERENCES portal_seguranca.usuario (id); 

ALTER TABLE analise.licenca_suspensa
RENAME CONSTRAINT fk_licenca_suspensao TO fk_ls_licenca;

ALTER TABLE analise.licenca_suspensa
RENAME CONSTRAINT fk_usuario_suspensao TO fk_ls_usuario_executor;

# --- !Downs

ALTER TABLE analise.licenca_cancelada
DROP CONSTRAINT fk_lc_licenca;

ALTER TABLE analise.licenca_cancelada
DROP CONSTRAINT fk_lc_usuario_executor;

ALTER TABLE analise.dispensa_licencamento_cancelada
DROP CONSTRAINT fk_dlc_dispensa_licenciamento;

ALTER TABLE analise.dispensa_licencamento_cancelada
DROP CONSTRAINT fk_dlc_usuario_executor;

ALTER TABLE analise.licenca_suspensa
RENAME CONSTRAINT fk_ls_licenca TO fk_licenca_suspensao ;

ALTER TABLE analise.licenca_suspensa
RENAME CONSTRAINT fk_ls_usuario_executor TO  fk_usuario_suspensao;
