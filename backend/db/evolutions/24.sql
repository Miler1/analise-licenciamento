# --- !Ups

ALTER TABLE analise.analise_tecnica ADD id_tipo_resultado_validacao_aprovador integer;
ALTER TABLE analise.analise_tecnica ADD parecer_validacao_aprovador text;
ALTER TABLE analise.analise_tecnica ADD id_usuario_validacao_aprovador integer;

ALTER TABLE analise.analise_tecnica ADD 
CONSTRAINT fk_at_tipo_resultado_validacao_aprovador FOREIGN KEY (id_tipo_resultado_validacao_aprovador)
REFERENCES analise.tipo_resultado_analise (id) MATCH SIMPLE
ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE analise.analise_tecnica ADD 
CONSTRAINT fk_at_usuario_validacao_aprovador FOREIGN KEY (id_usuario_validacao_aprovador)
REFERENCES portal_seguranca.usuario (id) MATCH SIMPLE
ON UPDATE NO ACTION ON DELETE NO ACTION;

COMMENT ON COLUMN analise.analise_tecnica.id_tipo_resultado_validacao_aprovador IS 'Campo responsavel por armazenar o resultado da análise do aprovador.';
COMMENT ON COLUMN analise.analise_tecnica.id_usuario_validacao_aprovador IS 'Campo responsável por armazernar o aprovador que fez a validação.';
COMMENT ON COLUMN analise.analise_tecnica.parecer_validacao_aprovador IS 'Campo responsável por armazenar a descrição da validação do aprovador.';

ALTER TABLE analise.analise_juridica ADD id_tipo_resultado_validacao_aprovador integer;
ALTER TABLE analise.analise_juridica ADD parecer_validacao_aprovador text;
ALTER TABLE analise.analise_juridica ADD id_usuario_validacao_aprovador integer;

ALTER TABLE analise.analise_juridica ADD 
CONSTRAINT fk_aj_tipo_resultado_validacao_aprovador FOREIGN KEY (id_tipo_resultado_validacao_aprovador)
REFERENCES analise.tipo_resultado_analise (id) MATCH SIMPLE
ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE analise.analise_juridica ADD 
CONSTRAINT fk_aj_usuario_validacao_aprovador FOREIGN KEY (id_usuario_validacao_aprovador)
REFERENCES portal_seguranca.usuario (id) MATCH SIMPLE
ON UPDATE NO ACTION ON DELETE NO ACTION;

COMMENT ON COLUMN analise.analise_juridica.id_tipo_resultado_validacao_aprovador IS 'Campo responsavel por armazenar o resultado da análise do aprovador.';
COMMENT ON COLUMN analise.analise_juridica.id_usuario_validacao_aprovador IS 'Campo responsável por armazernar o aprovador que fez a validação.';
COMMENT ON COLUMN analise.analise_juridica.parecer_validacao_aprovador IS 'Campo responsável por armazenar a descrição da validação do aprovador.';


# --- !Downs


ALTER TABLE analise.analise_juridica DROP COLUMN  id_tipo_resultado_validacao_aprovador;
ALTER TABLE analise.analise_juridica DROP COLUMN parecer_validacao_aprovador;
ALTER TABLE analise.analise_juridica DROP COLUMN id_usuario_validacao_aprovador;

ALTER TABLE analise.analise_tecnica DROP COLUMN  id_tipo_resultado_validacao_aprovador;
ALTER TABLE analise.analise_tecnica DROP COLUMN parecer_validacao_aprovador;
ALTER TABLE analise.analise_tecnica DROP COLUMN id_usuario_validacao_aprovador;
