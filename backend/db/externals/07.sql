# --- !Ups

INSERT INTO tramitacao.etapa (id_etapa, id_fluxo, tx_etapa, dt_prazo) VALUES (8, 1, 'Pendência de vistoria', NULL);

UPDATE tramitacao.condicao SET id_etapa = 8 WHERE id_condicao = 43;


# --- !Downs

DELETE FROM tramitacao.etapa WHERE id_etapa = 8 AND id_fluxo = 1;

