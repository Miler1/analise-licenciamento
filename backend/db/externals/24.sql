# --- !Ups

UPDATE tramitacao.condicao set nm_condicao = 'Aguardando assinatura aprovador' where id_condicao = 11;

UPDATE portal_seguranca.perfil set nome = 'Aprovador' where id = 9;

# --- !Downs

UPDATE tramitacao.condicao set nm_condicao = 'Aguardando assinatura diretor' where id_condicao = 11;

UPDATE portal_seguranca.perfil set nome = 'DIRETOR' where id = 9;