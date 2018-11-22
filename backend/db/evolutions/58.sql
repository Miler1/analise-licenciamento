# --- !Ups

-- Renomeando a entidade para documento_imovel_manejo para documento_manejo

ALTER INDEX analise.pk_documento_imovel_manejo RENAME TO pk_documento_manejo;
ALTER TABLE analise.documento_imovel_manejo RENAME CONSTRAINT fk_dmi_documento TO fk_dm_documento;
ALTER TABLE analise.documento_imovel_manejo RENAME CONSTRAINT fk_dmi_analise_manejo TO fk_dm_analise_manejo;
ALTER TABLE analise.documento_imovel_manejo RENAME TO documento_manejo;

-- Adicionando tipos de documento do imóvel do manejo na entidade tipo_documento

INSERT INTO analise.tipo_documento (id, nome, caminho_pasta, prefixo_nome_arquivo) VALUES
 (12, 'Documento complementar manejo', 'documento-complementar-manejo', 'documento_complementar');

# --- !Downs

DELETE FROM analise.tipo_documento WHERE id = 12;

ALTER INDEX analise.pk_documento_manejo RENAME TO pk_documento_imovel_manejo;
ALTER TABLE analise.documento_manejo RENAME CONSTRAINT fk_dm_documento TO fk_dmi_documento;
ALTER TABLE analise.documento_manejo RENAME CONSTRAINT fk_dm_analise_manejo TO fk_dmi_analise_manejo;
ALTER TABLE analise.documento_manejo RENAME TO documento_imovel_manejo;