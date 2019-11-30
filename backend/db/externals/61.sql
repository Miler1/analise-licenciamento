# --- !Ups

ALTER TABLE analise.inconsistencia ADD CONSTRAINT fk_i_atividade_caracterizacao FOREIGN KEY(id_atividade_caracterizacao) REFERENCES licenciamento.atividade_caracterizacao(id);



# --- !Downs

ALTER TABLE analise.inconsistencia DROP CONSTRAINT fk_i_atividade_caracterizacao; 
