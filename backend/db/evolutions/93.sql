# --- !Ups

ALTER TABlE analise.inconsistencia ADD COLUMN id_sobreposicao_empreendimento INTEGER;
ALTER TABLE analise.inconsistencia ADD CONSTRAINT fk_i_sobreposicao_empreendimento FOREIGN KEY (id_sobreposicao_empreendimento)
  REFERENCES licenciamento.sobreposicao_caracterizacao_empreendimento (id);

ALTER TABlE analise.inconsistencia ADD COLUMN id_sobreposicao_complexo INTEGER;
ALTER TABLE analise.inconsistencia ADD CONSTRAINT fk_i_sobreposicao_complexo FOREIGN KEY (id_sobreposicao_complexo)
  REFERENCES licenciamento.sobreposicao_complexo (id);

COMMENT ON COLUMN analise.inconsistencia.id_sobreposicao_empreendimento IS 'Id da sobreposição do empreendimento no licenciamento';
COMMENT ON COLUMN analise.inconsistencia.id_sobreposicao_complexo IS 'Id da sobreposição do complexo no licenciamento';

# --- !Downs

ALTER TABlE analise.inconsistencia DROP COLUMN id_sobreposicao_empreendimento;
ALTER TABlE analise.inconsistencia DROP COLUMN id_sobreposicao_complexo;
