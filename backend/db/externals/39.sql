# --- !Ups

ALTER TABLE analise.analise_juridica ADD CONSTRAINT fk_aj_usuario FOREIGN KEY(id_usuario_validacao)
REFERENCES portal_seguranca.usuario(id);

ALTER TABLE analise.analise_tecnica ADD CONSTRAINT fk_at_usuario FOREIGN KEY(id_usuario_validacao)
REFERENCES portal_seguranca.usuario(id);


# --- !Downs

ALTER TABLE analise.analise_juridica DROP  CONSTRAINT fk_aj_usuario ;

ALTER TABLE analise.analise_tecnica DROP CONSTRAINT fk_at_usuario ;
