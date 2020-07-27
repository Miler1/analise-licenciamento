# --- !Ups

ALTER TABLE analise.licenca_suspensa ADD CONSTRAINT fk_usuario_suspensao FOREIGN KEY(id_usuario_executor) 
 REFERENCES portal_seguranca.usuario(id);
 
 ALTER TABLE analise.licenca_suspensa ADD CONSTRAINT fk_licenca_suspensao FOREIGN KEY(id_licenca) 
 REFERENCES licenciamento.licenca(id);
 
 # --- !Downs

ALTER TABLE analise.licenca_suspensa DROP CONSTRAINT fk_usuario_suspensao;

ALTER TABLE analise.licenca_suspensa DROP CONSTRAINT fk_licenca_suspensao;