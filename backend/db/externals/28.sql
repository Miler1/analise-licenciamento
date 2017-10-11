# --- !Ups

ALTER TABLE analise.suspensao ADD CONSTRAINT fk_usuario_suspensao FOREIGN KEY(id_usuario_suspensao) 
 REFERENCES portal_seguranca.usuario(id);
 
 ALTER TABLE analise.suspensao ADD CONSTRAINT fk_licenca_suspensao FOREIGN KEY(id_licenca) 
 REFERENCES licenciamento.licenca(id);
 
 # --- !Downs

ALTER TABLE analise.suspensao DROP CONSTRAINT fk_usuario_suspensao;