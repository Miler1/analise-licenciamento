# --- !Ups

UPDATE analise.tipo_documento
SET caminho_pasta        = 'notificacao_analise_geo',
    prefixo_nome_arquivo = 'notificacao_analise_geo'
WHERE id = 17;

# --- !Downs

UPDATE analise.tipo_documento
SET caminho_pasta        = '',
    prefixo_nome_arquivo = ''
WHERE id = 17;




