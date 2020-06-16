# --- !Ups

--16
UPDATE analise.analise_juridica AS aj SET id_usuario_validacao=(SELECT h.id_usuario FROM tramitacao.historico_objeto_tramitavel h
JOIN analise.processo p ON h.id_historico_objeto_tramitavel=p.id_objeto_tramitavel
JOIN analise.analise a ON a.id_processo=p.id
WHERE aj.id_analise=a.id AND h.id_condicao_inicial=5 ORDER BY h.id_historico_objeto_tramitavel DESC LIMIT 1
);


UPDATE analise.analise_tecnica AS at SET id_usuario_validacao=(SELECT h.id_usuario FROM tramitacao.historico_objeto_tramitavel h
JOIN analise.processo p ON h.id_historico_objeto_tramitavel=p.id_objeto_tramitavel
JOIN analise.analise a ON a.id_processo=p.id
WHERE at.id_analise=a.id AND h.id_condicao_inicial=10 ORDER BY h.id_historico_objeto_tramitavel DESC LIMIT 1
);


--30
ALTER TABLE analise.licenca_cancelada
ADD CONSTRAINT fk_lc_licenca FOREIGN KEY(id_licenca) REFERENCES licenciamento.licenca(id);


ALTER TABLE analise.empreendimento_camada_geo
    ADD CONSTRAINT fk_ecg_empreendimento FOREIGN KEY (id_empreendimento)
        REFERENCES licenciamento.empreendimento (id);

GRANT USAGE ON SCHEMA analise TO tramitacao;
GRANT SELECT ON ALL TABLES IN SCHEMA analise TO tramitacao;


--46
ALTER TABLE analise.comunicado

    ADD CONSTRAINT fk_c_tipo_sobreposicao FOREIGN KEY (id_tipo_sobreposicao)
        REFERENCES licenciamento.tipo_sobreposicao (id);

--47
ALTER TABLE analise.comunicado
    ADD CONSTRAINT fk_c_orgao FOREIGN KEY (id_orgao) REFERENCES licenciamento.orgao (id);


--48
ALTER TABLE analise.inconsistencia

    ADD CONSTRAINT fk_i_geometria_atividade FOREIGN KEY (id_geometria_atividade)
        REFERENCES licenciamento.geometria_atividade (id),

    ADD CONSTRAINT fk_i_atividade_caracterizacao FOREIGN KEY (id_atividade_caracterizacao)
        REFERENCES licenciamento.atividade_caracterizacao (id);
--49
ALTER TABLE analise.inconsistencia
    ADD CONSTRAINT fk_i_sobreposicao FOREIGN KEY (id_sobreposicao)
        REFERENCES licenciamento.sobreposicao_caracterizacao_atividade (id);


--64
ALTER TABLE analise.inconsistencia_tecnica_tipo_licenca ADD 
CONSTRAINT fk_ittl_tipo_licenca FOREIGN KEY (id_tipo_licenca)
      REFERENCES licenciamento.tipo_licenca (id) ;

ALTER TABLE analise.inconsistencia_tecnica_questionario ADD 
CONSTRAINT fk_itq_id_inconsistencia_tecnica_questionario FOREIGN KEY (id_questionario)
      REFERENCES licenciamento.questionario_3 (id);

ALTER TABLE analise.inconsistencia_tecnica_atividade ADD 
CONSTRAINT fk_ita_atividade_caracterizacao FOREIGN KEY (id_atividade_caracterizacao)
    REFERENCES licenciamento.atividade_caracterizacao (id);

ALTER TABLE analise.inconsistencia_tecnica_parametro ADD
CONSTRAINT fk_itp_parametro_atividade FOREIGN KEY (id_parametro)
    REFERENCES licenciamento.parametro_atividade (id); 

# --- !Downs

--64

ALTER TABLE analise.inconsistencia_tecnica_tipo_licenca DROP 
CONSTRAINT fk_ittl_tipo_licenca;

ALTER TABLE analise.inconsistencia_tecnica_questionario DROP 
CONSTRAINT fk_itq_id_inconsistencia_tecnica_questionario;

ALTER TABLE analise.inconsistencia_tecnica_documento DROP  
CONSTRAINT fk_itd_documento_administrativo;
ALTER TABLE analise.inconsistencia_tecnica_documento DROP  
CONSTRAINT fk_itd_documento_tecnico;


ALTER TABLE analise.inconsistencia_tecnica_atividade DROP 
CONSTRAINT fk_ita_atividade_caracterizacao;

ALTER TABLE analise.inconsistencia_tecnica_parametro DROP
CONSTRAINT fk_itp_parametro_atividade; 


--49
ALTER TABLE analise.inconsistencia
    DROP CONSTRAINT fk_i_sobreposicao;

--48
ALTER TABLE analise.inconsistencia

    DROP CONSTRAINT fk_i_geometria_atividade,
    
    DROP CONSTRAINT fk_i_atividade_caracterizacao;

--47
ALTER TABLE analise.comunicado DROP CONSTRAINT fk_c_orgao;

--46

ALTER TABLE analise.comunicado
    DROP CONSTRAINT fk_c_atividade_caracterizacao,
    DROP CONSTRAINT fk_c_tipo_sobreposicao;

--30

ALTER TABLE analise.licenca_cancelada
DROP CONSTRAINT fk_lc_licenca;

ALTER TABLE analise.licenca_cancelada
DROP CONSTRAINT fk_lc_usuario_executor;

ALTER TABLE analise.dispensa_licencamento_cancelada
DROP CONSTRAINT fk_dlc_dispensa_licenciamento;

ALTER TABLE analise.dispensa_licencamento_cancelada
DROP CONSTRAINT fk_dlc_usuario_executor;

ALTER TABLE analise.licenca_suspensa
RENAME CONSTRAINT fk_ls_licenca TO fk_licenca_suspensao ;

ALTER TABLE analise.licenca_suspensa
RENAME CONSTRAINT fk_ls_usuario_executor TO  fk_usuario_suspensao;


--16 
 UPDATE analise.analise_juridica SET id_usuario_validacao=NULL;
 
 UPDATE analise.analise_tecnica SET id_usuario_validacao=NULL;
