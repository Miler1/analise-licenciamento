# --- !Ups

ALTER TABLE analise.analise_tecnica 
	RENAME id_tipo_resultado_validacao_gerente TO id_tipo_resultado_validacao_coordenador; 

ALTER TABLE analise.analise_tecnica 
	RENAME CONSTRAINT fk_at_tipo_resultado_validacao_gerente TO fk_at_tipo_resultado_validacao_coordenador;

ALTER TABLE analise.analise_tecnica 
	RENAME parecer_validacao_gerente TO parecer_validacao_coordenador; 

ALTER TABLE analise.analise_tecnica 
	RENAME id_usuario_validacao_gerente TO id_usuario_validacao_coordenador; 

ALTER TABLE analise.analise_tecnica 
	RENAME CONSTRAINT fk_at_usuario_analise_validacao_gerente TO fk_at_usuario_analise_validacao_coordenador;


# --- !Downs

ALTER TABLE analise.analise_tecnica 
	RENAME CONSTRAINT fk_at_usuario_analise_validacao_coordenador TO  fk_at_usuario_analise_validacao_gerente;

ALTER TABLE analise.analise_tecnica 
	RENAME id_usuario_validacao_coordenador  TO id_usuario_validacao_gerente; 

ALTER TABLE analise.analise_tecnica 
	RENAME parecer_validacao_coordenador  TO parecer_validacao_gerente; 

ALTER TABLE analise.analise_tecnica 
	RENAME CONSTRAINT fk_at_tipo_resultado_validacao_coordenador TO  fk_at_tipo_resultado_validacao_gerente;

ALTER TABLE analise.analise_tecnica 
	RENAME id_tipo_resultado_validacao_coordenador TO  id_tipo_resultado_validacao_gerente; 




