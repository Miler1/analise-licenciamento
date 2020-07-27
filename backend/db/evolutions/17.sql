# --- !Ups


ALTER TABLE analise.analise_juridica ADD COLUMN id_usuario_validacao INTEGER;
COMMENT ON COLUMN analise.analise_juridica.id_usuario_validacao IS 'Identificador da entidade portal_seguranca.usuario que realizará o relacionamento entre as duas entidades.';

ALTER TABLE analise.analise_tecnica ADD COLUMN id_usuario_validacao INTEGER;
COMMENT ON COLUMN analise.analise_tecnica.id_usuario_validacao IS 'Identificador da entidade portal_seguranca.usuario que realizará o relacionamento entre as duas entidades.';


# --- !Downs


ALTER TABLE analise.analise_juridica DROP COLUMN id_usuario_validacao;
ALTER TABLE analise.analise_tecnica DROP COLUMN id_usuario_validacao;

