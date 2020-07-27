# --- !Ups

UPDATE tramitacao.acao SET tx_descricao='Deferir análise GEO com comunicado' WHERE id_acao = 65;

# --- !Downs

UPDATE tramitacao.acao SET tx_descricao='Aguardar resposta comunicado' WHERE id_acao = 65;



