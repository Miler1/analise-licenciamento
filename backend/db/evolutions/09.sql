# --- !Ups

INSERT INTO analise.tipo_resultado_analise(id,nome) VALUES
(4,'Parecer validado'),
(5,'Solicitar ajustes'),
(6,'Parecer não validado');


ALTER TABLE analise.analise_juridica RENAME id_tipo_resultado TO id_tipo_resultado_analise;
COMMENT ON COLUMN analise.analise_juridica.id_tipo_resultado_analise IS 'Campo responsavel por armazenar o resultado da análise do consultor jurídico.';

ALTER TABLE analise.analise_juridica ADD COLUMN id_tipo_resultado_validacao INTEGER;
ALTER TABLE analise.analise_juridica ADD CONSTRAINT fk_aj_tipo_resultado_validacao FOREIGN KEY(id_tipo_resultado_validacao)
REFERENCES analise.tipo_resultado_analise;
COMMENT ON COLUMN analise.analise_juridica.id_tipo_resultado_validacao IS 'Campo responsavel por armazenar o resultado da análise do coordenador jurídico';

# --- !Downs

ALTER TABLE analise.analise_juridica DROP COLUMN id_tipo_resultado_validacao;
ALTER TABLE analise.analise_juridica RENAME id_tipo_resultado_analise TO id_tipo_resultado;
DELETE FROM analise.tipo_resultado_analise WHERE id IN(4,5,6);
