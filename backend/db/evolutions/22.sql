# --- !Ups

ALTER TABLE analise.analise_tecnica ADD id_tipo_resultado_validacao_gerente integer;
ALTER TABLE analise.analise_tecnica ADD parecer_validacao_gerente text;
ALTER TABLE analise.analise_tecnica ADD id_usuario_validacao_gerente integer;

ALTER TABLE analise.analise_tecnica ADD 
  CONSTRAINT fk_at_tipo_resultado_validacao_gerente FOREIGN KEY (id_tipo_resultado_validacao_gerente)
      REFERENCES analise.tipo_resultado_analise (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE analise.analise_tecnica ADD       
  CONSTRAINT fk_at_usuario_validacao_gerente FOREIGN KEY (id_usuario_validacao_gerente)
      REFERENCES portal_seguranca.usuario (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

COMMENT ON COLUMN analise.analise_tecnica.id_tipo_resultado_validacao_gerente IS 'Campo responsavel por armazenar o resultado da análise do gerente técnico.';
COMMENT ON COLUMN analise.analise_tecnica.id_usuario_validacao_gerente IS 'Campo responsável por armazernar o gerente que fez a validação.';
COMMENT ON COLUMN analise.analise_tecnica.parecer_validacao_gerente IS 'Campo responsável por armazenar a descrição da validação do gerente técnico.';


# --- !Downs


ALTER TABLE analise.analise_tecnica DROP COLUMN id_tipo_resultado_validacao_gerente;
ALTER TABLE analise.analise_tecnica DROP COLUMN parecer_validacao_gerente;
ALTER TABLE analise.analise_tecnica DROP COLUMN	 id_usuario_validacao_gerente;
