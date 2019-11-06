# --- !Ups

UPDATE tramitacao.acao SET tx_descricao = REPLACE(tx_descricao, 'processo', 'protocolo') WHERE tx_descricao LIKE '%processo%';

# --- !Downs

UPDATE tramitacao.acao SET tx_descricao = REPLACE(tx_descricao, 'protocolo', 'processo') WHERE tx_descricao LIKE '%protocolo%';


