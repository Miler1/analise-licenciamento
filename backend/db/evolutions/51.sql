
# --- !Ups

INSERT INTO analise.tipo_documento (id,nome,caminho_modelo,caminho_pasta,prefixo_nome_arquivo) VALUES
(7,'Documento análise manejo',NULL,'analise_manejo','analise_manejo');

-- Adiçao do documento que irá vincular ao pdf gerado

ALTER TABLE analise.analise_manejo ADD COLUMN id_documento INTEGER;
ALTER TABLE analise.analise_manejo ADD CONSTRAINT fk_documento FOREIGN KEY(id_documento) REFERENCES analise.documento (id);

COMMENT ON COLUMN analise.analise_manejo.id_documento IS 'Identificador da entidade documento.';


-- Adição de novos atributos em imóvel manejo

ALTER TABLE analise.imovel_manejo ADD COLUMN endereco TEXT;
COMMENT ON COLUMN analise.imovel_manejo.endereco IS 'Endereço do imóvel.';

ALTER TABLE analise.imovel_manejo ADD COLUMN bairro VARCHAR(250);
COMMENT ON COLUMN analise.imovel_manejo.bairro IS 'Bairro do imóvel.';

ALTER TABLE analise.imovel_manejo ADD COLUMN cep VARCHAR(10);
COMMENT ON COLUMN analise.imovel_manejo.bairro IS 'Código de endereçamento postal do imóvel.';


# --- !Downs

ALTER TABLE analise.imovel_manejo DROP COLUMN cep;
ALTER TABLE analise.imovel_manejo DROP COLUMN bairro;
ALTER TABLE analise.imovel_manejo DROP COLUMN endereco;

ALTER TABLE analise.analise_manejo DROP COLUMN id_documento;

DELETE FROM analise.tipo_documento WHERE id IN (7);