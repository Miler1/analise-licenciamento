
# --- !Ups

UPDATE analise.tipo_documento SET caminho_pasta = 'parecer_analise_tecnica' WHERE id = 4;


# --- !Downs


UPDATE analise.tipo_documento SET caminho_pasta = 'parecer_analise' WHERE id = 4;