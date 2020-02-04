
# --- !Ups

ALTER TABLE analise.parecer_analista_geo ADD COLUMN id_documento INTEGER NOT NULL;
COMMENT ON COLUMN analise.parecer_analista_geo.id_documento IS 'Identificador único do documento parecer';

ALTER TABLE analise.parecer_analista_geo ADD 
CONSTRAINT fk_d_documento FOREIGN KEY (id_documento) 
	REFERENCES analise.documento (id);

ALTER TABLE analise.parecer_analista_geo ADD COLUMN id_carta_imagem INTEGER NOT NULL; 
COMMENT ON COLUMN analise.parecer_analista_geo.id_carta_imagem IS ' Identificador único do documento carta_imagem';

ALTER TABLE analise.parecer_analista_geo ADD 
CONSTRAINT fk_d_carta FOREIGN KEY (id_carta_imagem) 
	REFERENCES analise.documento (id);

ALTER TABLE analise.documento ADD COLUMN responsavel VARCHAR(200) NOT NULL;
COMMENT ON COLUMN analise.documento.responsavel IS 'Campo que apresenta o responsável por gerar o documento';


# --- !Downs

ALTER TABLE analise.documento DROP COLUMN responsavel;

ALTER TABLE analise.parecer_analista_geo DROP COLUMN id_carta_imagem;

ALTER TABLE analise.parecer_analista_geo DROP COLUMN id_documento;
