# --- !Ups

ALTER TABLE analise.comunicado

    ADD CONSTRAINT fk_c_atividade_caracterizacao FOREIGN KEY (id_caracterizacao)
        REFERENCES licenciamento.atividade_caracterizacao (id),

    ADD CONSTRAINT fk_c_tipo_sobreposicao FOREIGN KEY (id_tipo_sobreposicao)
        REFERENCES licenciamento.tipo_sobreposicao (id);

# --- !Downs

ALTER TABLE analise.comunicado
    DROP CONSTRAINT fk_c_atividade_caracterizacao,
    DROP CONSTRAINT fk_c_tipo_sobreposicao;