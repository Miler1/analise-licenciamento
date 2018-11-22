# --- !Ups

-- Criando entidade documento_complementar_manejo

CREATE TABLE analise.documento_complementar_manejo (
 id_documento INTEGER NOT NULL,
 id_analise_tecnica_manejo INTEGER NOT NULL,
 CONSTRAINT pk_documento_complementar_manejo PRIMARY KEY (id_documento),
 CONSTRAINT fk_dcm_documento FOREIGN KEY (id_documento) REFERENCES analise.documento(id),
 CONSTRAINT fk_dcm_analise_manejo FOREIGN KEY (id_analise_tecnica_manejo) REFERENCES analise.analise_tecnica_manejo (id)
);

COMMENT ON TABLE analise.documento_complementar_manejo IS 'Entidade responsável por armazenas os documentos complementares do manejo.';
COMMENT ON COLUMN analise.documento_complementar_manejo.id_documento IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.documento_complementar_manejo.id_analise_tecnica_manejo IS 'Identificador da entidade analise_tecnica_manejo que faz o relacionamento entre a análise do manejo e documento complementar.';

ALTER TABLE analise.documento_complementar_manejo OWNER TO postgres;
GRANT ALL ON TABLE analise.documento_complementar_manejo TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.documento_complementar_manejo TO licenciamento_pa;

-- Adicionando tipos de documento do imóvel do manejo na entidade tipo_documento

INSERT INTO analise.tipo_documento (id, nome, caminho_pasta, prefixo_nome_arquivo) VALUES
 (12, 'Documento complementar manejo', 'documento-complementar-manejo', 'documento_complementar');

# --- !Downs

DELETE FROM analise.tipo_documento WHERE id = 12;

DROP TABLE analise.documento_complementar_manejo;