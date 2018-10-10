
# --- !Ups

INSERT INTO analise.tipo_documento (id,nome,caminho_modelo,caminho_pasta,prefixo_nome_arquivo) VALUES
(7,'Documento análise manejo',NULL,'analise_manejo','analise_manejo');

-- Adiçao do documento que irá vincular ao pdf gerado

ALTER TABLE analise.analise_manejo ADD COLUMN id_documento INTEGER;
ALTER TABLE analise.analise_manejo ADD CONSTRAINT fk_documento FOREIGN KEY(id_documento) REFERENCES analise.documento (id);

COMMENT ON COLUMN analise.analise_manejo.id_documento IS 'Identificador da entidade documento.';



# --- !Downs

ALTER TABLE analise.analise_manejo DROP COLUMN id_documento;

DELETE FROM analise.tipo_documento WHERE id IN (7);