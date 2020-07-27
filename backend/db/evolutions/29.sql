# --- !Ups

ALTER TABLE analise.dia_analise ADD COLUMN quantidade_dias_aprovador INTEGER;
COMMENT ON COLUMN analise.dia_analise.quantidade_dias_aprovador IS 'Quantidade de dias em que a análise ficará no aprovador.';

ALTER TABLE analise.dia_analise RENAME COLUMN qtde_dias_analise TO quantidade_dias_analise;
ALTER TABLE analise.dia_analise RENAME COLUMN qtde_dias_juridica TO quantidade_dias_juridica;
ALTER TABLE analise.dia_analise RENAME COLUMN qtde_dias_tecnica TO quantidade_dias_tecnica;

ALTER TABLE analise.suspensao RENAME COLUMN qtde_dias_suspensao TO quantidade_dias_suspensao;

# --- !Downs

ALTER TABLE analise.dia_analise DROP COLUMN quantidade_dias_aprovador;

ALTER TABLE analise.dia_analise RENAME COLUMN quantidade_dias_analise TO qtde_dias_analise;
ALTER TABLE analise.dia_analise RENAME COLUMN quantidade_dias_juridica TO qtde_dias_juridica;
ALTER TABLE analise.dia_analise RENAME COLUMN quantidade_dias_tecnica TO qtde_dias_tecnica;

ALTER TABLE analise.suspensao RENAME COLUMN quantidade_dias_suspensao TO qtde_dias_suspensao;