# --- !Ups

INSERT INTO tramitacao.condicao(id_etapa, nm_condicao, fl_ativo) VALUES
((SELECT id_etapa FROM tramitacao.etapa where tx_etapa = 'Análise GEO'), 'Aguardando validação gerente', 1),
((SELECT id_etapa FROM tramitacao.etapa where tx_etapa = 'Análise GEO'), 'Aguardando validação pela diretoria', 1);

# --- !Downs

DELETE FROM tramitacao.condicao
WHERE id_etapa = (SELECT id_etapa FROM tramitacao.etapa WHERE tx_etapa = 'Análise GEO') AND
nm_condicao in ('Aguardando validação gerente', 'Aguardando validação pela diretoria');
