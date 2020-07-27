# --- !Ups

ALTER TABLE analise.inconsistencia

    ADD CONSTRAINT fk_i_geometria_atividade FOREIGN KEY (id_geometria_atividade)
        REFERENCES licenciamento.geometria_atividade (id),

    ADD CONSTRAINT fk_i_atividade_caracterizacao FOREIGN KEY (id_atividade_caracterizacao)
        REFERENCES licenciamento.atividade_caracterizacao (id);

# --- !Downs

ALTER TABLE analise.inconsistencia

    DROP CONSTRAINT fk_i_geometria_atividade,
    
    DROP CONSTRAINT fk_i_atividade_caracterizacao;