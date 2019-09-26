# --- !Ups

ALTER TABlE analise.comunicado ADD COLUMN id_sobreposicao_empreendimento INTEGER;
ALTER TABLE analise.comunicado ADD CONSTRAINT fk_c_sobreposicao_empreendimento FOREIGN KEY (id_sobreposicao_empreendimento)
  REFERENCES licenciamento.sobreposicao_caracterizacao_empreendimento (id);

ALTER TABlE analise.comunicado ADD COLUMN id_sobreposicao_complexo INTEGER;
ALTER TABLE analise.comunicado ADD CONSTRAINT fk_c_sobreposicao_complexo FOREIGN KEY (id_sobreposicao_complexo)
  REFERENCES licenciamento.sobreposicao_complexo (id);

ALTER TABlE analise.comunicado ADD COLUMN id_sobreposicao_atividade INTEGER;
ALTER TABLE analise.comunicado ADD CONSTRAINT fk_c_sobreposicao_atividade FOREIGN KEY (id_sobreposicao_atividade)
  REFERENCES licenciamento.sobreposicao_caracterizacao_atividade (id);

COMMENT ON COLUMN analise.comunicado.id_sobreposicao_empreendimento IS 'Identificador da sobreposição do empreendimento no licenciamento';
COMMENT ON COLUMN analise.comunicado.id_sobreposicao_complexo IS 'Identificador da sobreposição do complexo no licenciamento';
COMMENT ON COLUMN analise.comunicado.id_sobreposicao_complexo IS 'Identificador da sobreposição da atividade no licenciamento';


# --- !Downs

ALTER TABlE analise.comunicado DROP COLUMN id_sobreposicao_empreendimento;
ALTER TABlE analise.comunicado DROP COLUMN id_sobreposicao_complexo;
ALTER TABlE analise.comunicado DROP COLUMN id_sobreposicao_atividade;

