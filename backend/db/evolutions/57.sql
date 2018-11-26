# --- !Ups

-- Alterando o nome da sequence analise_manejo_id_seq

ALTER SEQUENCE analise.analise_manejo_id_seq RENAME TO analise_tecnica_manejo_id_seq;

-- Removendo path_anexo de analise_tecnica_manejo

ALTER TABLE analise.analise_tecnica_manejo DROP COLUMN path_anexo;

-- Criando entidade documento_imovel_manejo

CREATE TABLE analise.documento_imovel_manejo (
 id_documento INTEGER NOT NULL,
 id_analise_tecnica_manejo INTEGER NOT NULL,
 nome VARCHAR(200),
 CONSTRAINT pk_documento_imovel_manejo PRIMARY KEY (id_documento),
 CONSTRAINT fk_dmi_documento FOREIGN KEY (id_documento) REFERENCES analise.documento(id),
 CONSTRAINT fk_dmi_analise_manejo FOREIGN KEY (id_analise_tecnica_manejo) REFERENCES analise.analise_tecnica_manejo (id)
);

COMMENT ON TABLE analise.documento_imovel_manejo IS 'Entidade responsável por armazenas os documentos do imóvel do manejo.';
COMMENT ON COLUMN analise.documento_imovel_manejo.id_documento IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.documento_imovel_manejo.id_analise_tecnica_manejo IS 'Identificador da entidade analise_tecnica_manejo que faz o relacionamento entre a análise do manejo e documento do imóvel do manejo.';
COMMENT ON COLUMN analise.documento_imovel_manejo.nome IS 'Nome do documento.';

ALTER TABLE analise.documento_imovel_manejo OWNER TO postgres;
GRANT ALL ON TABLE analise.documento_imovel_manejo TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.documento_imovel_manejo TO licenciamento_pa;

-- Adicionando tipos de documento do imóvel do manejo na entidade tipo_documento

INSERT INTO analise.tipo_documento (id, nome, caminho_pasta, prefixo_nome_arquivo) VALUES
 (11, 'Documento imóvel manejo', 'documento-imovel-manejo', 'documento_imovel_manejo');

-- Renomeando atributo da entidade observacao

ALTER TABLE analise.observacao RENAME COLUMN id_analise_manejo TO id_analise_tecnica_manejo;

# --- !Downs

ALTER TABLE analise.observacao RENAME COLUMN id_analise_tecnica_manejo TO id_analise_manejo;

DROP TABLE analise.documento_imovel_manejo;

DELETE FROM analise.tipo_documento WHERE id = 11;

ALTER TABLE analise.analise_tecnica_manejo ADD COLUMN path_anexo VARCHAR(500);
COMMENT ON COLUMN analise.analise_tecnica_manejo.path_anexo IS 'Caminho onde está armazenado o anéxo da análise.';

ALTER SEQUENCE analise.analise_tecnica_manejo_id_seq RENAME TO analise_manejo_id_seq;