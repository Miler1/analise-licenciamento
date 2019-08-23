# --- !Ups

ALTER TABLE analise.inconsistencia
    ADD CONSTRAINT fk_i_sobreposicao FOREIGN KEY (id_sobreposicao)
        REFERENCES licenciamento.sobreposicao_caracterizacao_atividade (id);

# --- !Downs

ALTER TABLE analise.inconsistencia
    DROP CONSTRAINT fk_i_sobreposicao;