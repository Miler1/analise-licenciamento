# --- !Ups

CREATE SEQUENCE analise.reenvio_email_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE analise.reenvio_email_id_seq
  OWNER TO postgres;
GRANT ALL ON SEQUENCE analise.reenvio_email_id_seq TO postgres;
GRANT SELECT, USAGE ON SEQUENCE analise.reenvio_email_id_seq TO licenciamento_am;

CREATE TABLE analise.reenvio_email ( 
	id Integer DEFAULT nextval('analise.reenvio_email_id_seq'::regclass) NOT NULL,
	id_itens_email Integer NOT NULL,
	tipo_email Integer NOT NULL,
	log Text,
	emails_destinatario Text NOT NULL,
CONSTRAINT pk_reenvio_email PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE analise.reenvio_email
  OWNER TO postgres;
GRANT ALL ON TABLE analise.reenvio_email TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.reenvio_email TO licenciamento_am;

COMMENT ON TABLE analise.reenvio_email
  IS 'Entidade responsável por armazenar o controle do reenvio de email. Guarad os emails que não foram enviados para serem enviados na próxima interação do job de reenvio de email.';
COMMENT ON COLUMN analise.reenvio_email.id IS 'Identificador primário da entidade.';
COMMENT ON COLUMN analise.reenvio_email.id_itens_email IS 'Identificador generico das entididades que podem ser utilizadas nos emails (não é uma FK).';
COMMENT ON COLUMN analise.reenvio_email.tipo_email IS 'Tipo de email que deve ser reenviado.';
COMMENT ON COLUMN analise.reenvio_email.log IS 'Motivo da tentativa de envio de email.';
COMMENT ON COLUMN analise.reenvio_email.emails_destinatario IS 'Lista de destinatários (separado por ;) que receberão os emails enviados.';

# --- !Downs

DROP TABLE analise.reenvio_email;
DROP SEQUENCE analise.reenvio_email_id_seq;