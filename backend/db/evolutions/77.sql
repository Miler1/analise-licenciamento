# --- !Ups

BEGIN;

ALTER SEQUENCE analise.analise_geo_anexo_id_seq RENAME TO empreendimento_camada_geo_id_seq;

ALTER TABLE analise.analise_geo_anexo RENAME CONSTRAINT pk_analise_geo_anexo TO pk_empreendimento_camada_geo;

ALTER TABLE analise.analise_geo_anexo RENAME CONSTRAINT fk_aga_tipo_area_geometria TO fk_ecg_tipo_area_geometria;

ALTER TABLE analise.analise_geo_anexo ADD COLUMN area DOUBLE PRECISION NOT NULL DEFAULT 0;

ALTER TABLE analise.analise_geo_anexo RENAME TO empreendimento_camada_geo;

INSERT INTO analise.tipo_documento(id, nome, caminho_pasta, prefixo_nome_arquivo) VALUES
(18, 'Documento inconsistência', 'documento_inconsistencia', 'documento_inconsistencia' );

COMMIT;

# --- !Downs

BEGIN;

ALTER SEQUENCE analise.empreendimento_camada_geo_id_seq RENAME TO analise_geo_anexo_id_seq;

ALTER TABLE analise.empreendimento_camada_geo RENAME CONSTRAINT pk_empreendimento_camada_geo TO pk_analise_geo_anexo;

ALTER TABLE analise.empreendimento_camada_geo RENAME CONSTRAINT fk_ecg_tipo_area_geometria TO fk_aga_tipo_area_geometria;

ALTER TABLE analise.empreendimento_camada_geo DROP COLUMN area;

ALTER TABLE analise.empreendimento_camada_geo RENAME TO analise_geo_anexo;

DELETE FROM analise.tipo_documento WHERE id = 18;

COMMIT;