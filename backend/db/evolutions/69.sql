# --- !Ups

CREATE TABLE analise.usuario_analise (
	id serial NOT NULL , 
	login character varying(50), 
	CONSTRAINT pk_usuario_analise PRIMARY KEY (id)
);

ALTER TABLE analise.usuario_analise OWNER TO postgres;

GRANT ALL ON TABLE analise.usuario_analise TO postgres;

GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.usuario_analise TO licenciamento_am;

GRANT SELECT ON TABLE analise.usuario_analise TO tramitacao;

GRANT SELECT,USAGE ON SEQUENCE analise.usuario_analise_id_seq TO licenciamento_am;

COMMENT ON TABLE analise.usuario_analise IS 'Entidade responsável por armazenar os usuários que possui permissão para acessar o módulo de análise do licenciamento';

COMMENT ON COLUMN analise.usuario_analise.id IS 'Identificador único da entidade usuario.';

COMMENT ON COLUMN analise.usuario_analise.login IS 'Login do usuário que pode ser cpf ou cnpj';



# --- !Downs

DROP TABLE analise.usuario_analise;