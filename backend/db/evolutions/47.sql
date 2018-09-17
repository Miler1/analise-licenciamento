# --- !Ups

-- remover restrição de que o campo id_caracterização seja único

ALTER TABLE analise.rel_processo_caracterizacao DROP CONSTRAINT ue_pc_id_caracterizacao;

-- alteração da tabela de processo para incluir o seu processo antigo

ALTER TABLE analise.processo ADD COLUMN id_processo_anterior INTEGER;
ALTER TABLE analise.processo ADD CONSTRAINT fk_processo_anterior FOREIGN KEY(id_processo_anterior) REFERENCES analise.processo (id);

COMMENT ON COLUMN analise.processo.id_processo_anterior IS 'Identificador da entidade processo que faz o relacionamento entre o processo e o processo anterior a renovação.';

# --- !Downs

ALTER TABLE analise.processo DROP COLUMN id_processo_anterior;

-- voltar restrição de que o campo id_caracterização seja único
-- TODO precisa de um script de limpeza

ALTER TABLE analise.rel_processo_caracterizacao ADD  CONSTRAINT ue_pc_id_caracterizacao UNIQUE(id_caracterizacao);

