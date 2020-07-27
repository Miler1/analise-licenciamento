# --- !Ups

UPDATE tramitacao.acao SET tx_descricao = REPLACE(tx_descricao, 'Deferir análise GEO via gerente', 'Deferir análise GEO') WHERE id_acao = 49;
UPDATE tramitacao.acao SET tx_descricao = REPLACE(tx_descricao, 'Indeferir análise GEO via gerente', 'Indeferir análise GEO') WHERE id_acao = 50;


# --- !Downs

UPDATE tramitacao.acao SET tx_descricao = REPLACE(tx_descricao, 'Deferir análise GEO', 'Deferir análise GEO via gerente') WHERE id_acao = 49;
UPDATE tramitacao.acao SET tx_descricao = REPLACE(tx_descricao, 'Indeferir análise GEO', 'Indeferir análise GEO via gerente') WHERE id_acao = 50;

