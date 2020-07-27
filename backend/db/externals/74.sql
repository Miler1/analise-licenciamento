# --- !Ups

UPDATE tramitacao.condicao SET id_etapa = 7 WHERE id_condicao = 6;

# --- !Downs

UPDATE tramitacao.condicao SET id_etapa = null WHERE id_condicao = 6;


