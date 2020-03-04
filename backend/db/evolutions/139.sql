# --- !Ups

ALTER TABLE analise.licenca_suspensa DROP CONSTRAINT fk_ls_usuario_analise_executor;

ALTER TABLE analise.licenca_cancelada DROP CONSTRAINT fk_lc_usuario_analise_executor ;

# --- !Downs

ALTER TABLE analise.licenca_cancelada ADD 
  CONSTRAINT fk_lc_usuario_analise_executor FOREIGN KEY (id_usuario_executor)
      REFERENCES analise.usuario_analise (id);

ALTER TABLE analise.licenca_suspensa ADD 
  CONSTRAINT fk_ls_usuario_analise_executor FOREIGN KEY (id_usuario_executor)
      REFERENCES analise.usuario_analise (id);
