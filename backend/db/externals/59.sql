# --- !Ups

UPDATE tramitacao.condicao SET nm_condicao = 'Solicitação de desvínculo pendente análise GEO' WHERE nm_condicao = 'Solicitação de desvínculo pendente' ;

INSERT INTO tramitacao.condicao(id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES
(33, (SELECT id_etapa FROM tramitacao.etapa where tx_etapa = 'Análise técnica'), 'Solicitação de desvínculo pendente análise técnica', 1);

INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final) VALUES (32, 59, 8, 33);



# --- !Downs

DELETE FROM tramitacao.transicao WHERE id_transicao = 32;

DELETE FROM tramitacao.condicao WHERE id_condicao = 33;

UPDATE tramitacao.condicao SET nm_condicao = 'Solicitação de desvínculo pendente' WHERE nm_condicao = 'Solicitação de desvínculo pendente análise GEO' ;



