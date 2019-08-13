# --- !Ups

UPDATE analise.tipo_documento SET caminho_pasta = 'parecer_analise_geo'
WHERE prefixo_nome_arquivo = 'parecer_analise_geo';

INSERT INTO analise.tipo_documento(id, nome, caminho_pasta, prefixo_nome_arquivo)
VALUES (19, 'Documento carta imagem', 'carta_imagem', 'carta_imagem_analise_geo');

# --- !Downs

UPDATE analise.tipo_documento SET caminho_pasta = 'parecer_analise'
WHERE prefixo_nome_arquivo = 'parecer_analise_geo';

DELETE FROM analise.tipo_documento WHERE id = 19;