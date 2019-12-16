# --- !Ups

ALTER TABLE analise.inconsistencia_tecnica_tipo_licenca ADD 
CONSTRAINT fk_ittl_tipo_licenca FOREIGN KEY (id_tipo_licenca)
      REFERENCES licenciamento.tipo_licenca (id) ;

ALTER TABLE analise.inconsistencia_tecnica_questionario ADD 
CONSTRAINT fk_itq_id_inconsistencia_tecnica_questionario FOREIGN KEY (id_questionario)
      REFERENCES licenciamento.questionario_3 (id);

ALTER TABLE analise.inconsistencia_tecnica_documento ADD  
CONSTRAINT fk_itd_documento_administrativo FOREIGN KEY (id_documento_administrativo)
    REFERENCES licenciamento.solicitacao_documento_caracterizacao (id);
ALTER TABLE analise.inconsistencia_tecnica_documento ADD  
CONSTRAINT fk_itd_documento_tecnico FOREIGN KEY (id_documento_tecnico)
    REFERENCES licenciamento.solicitacao_grupo_documento (id) ;


ALTER TABLE analise.inconsistencia_tecnica_atividade ADD 
CONSTRAINT fk_ita_atividade_caracterizacao FOREIGN KEY (id_atividade_caracterizacao)
    REFERENCES licenciamento.atividade_caracterizacao (id);

ALTER TABLE analise.inconsistencia_tecnica_parametro ADD
CONSTRAINT fk_itp_parametro_atividade FOREIGN KEY (id_parametro)
    REFERENCES licenciamento.parametro_atividade (id); 


# --- !Downs

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

