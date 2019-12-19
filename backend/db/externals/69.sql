# --- !Ups

UPDATE tramitacao.transicao SET id_condicao_final = 25 WHERE id_acao = 58 AND id_condicao_inicial = 31;


# --- !Downs

UPDATE tramitacao.transicao SET id_condicao_final = 26 WHERE id_acao = 58 AND id_condicao_inicial = 31;