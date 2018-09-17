# --- !Ups

-- remover restrição de que o campo id_caracterização seja único

ALTER TABLE analise.rel_processo_caracterizacao DROP CONSTRAINT ue_pc_id_caracterizacao;


# --- !Downs

-- voltar restrição de que o campo id_caracterização seja único
-- TODO precisa de um script de limpeza

ALTER TABLE analise.rel_processo_caracterizacao ADD  CONSTRAINT ue_pc_id_caracterizacao UNIQUE(id_caracterizacao);

