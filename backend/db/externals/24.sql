# --- !Ups

UPDATE tramitacao.condicao set nm_condicao = 'Aguardando assinatura aprovador' where id_condicao = 11;


# --- !Downs

UPDATE tramitacao.condicao set nm_condicao = 'Aguardando assinatura diretor' where id_condicao = 11;

