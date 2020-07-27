# --- !Ups

ALTER TABLE analise.notificacao 
	ADD COLUMN resposta_notificacao TEXT;
	
COMMENT ON COLUMN analise.notificacao.resposta_notificacao IS 'Campo responsável por armazenar a resposta do interessado';


ALTER TABLE analise.notificacao RENAME COLUMN data_cadastro TO data_notificacao;
ALTER TABLE analise.notificacao RENAME COLUMN data_leitura TO data_final_notificacao;
ALTER TABLE analise.notificacao ALTER COLUMN id_tipo_documento DROP NOT NULL;
ALTER TABLE analise.notificacao ALTER COLUMN id_analise_documento DROP NOT NULL;

# --- !Downs

ALTER TABLE analise.notificacao
    DROP COLUMN resposta_notificacao;

ALTER TABLE analise.notificacao RENAME COLUMN data_notificacao TO data_cadastro;
ALTER TABLE analise.notificacao RENAME COLUMN data_final_notificacao TO data_leitura;
ALTER TABLE analise.notificacao ALTER COLUMN id_tipo_documento SET NOT NULL;
ALTER TABLE analise.notificacao ALTER COLUMN id_analise_documento SET NOT NULL;




