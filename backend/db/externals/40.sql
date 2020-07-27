# --- !Ups

BEGIN;

SELECT setval('tramitacao.acao_id_acao_seq', max(id_acao)) FROM tramitacao.acao;

INSERT INTO tramitacao.acao(tx_descricao, fl_ativo, fl_tramitavel) VALUES
('Deferir análise GEO via gerente', 1, 1),
('Indeferir análise GEO via gerente', 1, 1),
('Iniciar análise GEO', 1, 1),
('Invalidar parecer GEO pelo gerente', 1, 1),
('Validar deferimento GEO pelo gerente', 1, 1),
('Validar indeferimento GEO pelo gerente', 1, 1),
('Solicitar ajustes parecer GEO pelo gerente', 1, 1),
('Solicitar ajuste da análise GEO pelo presidente', 1, 1),
('Resolver notificação GEO', 1, 1),
('Invalidar parecer GEO encaminhando para outro GEO', 1, 1);

SELECT setval('tramitacao.etapa_id_etapa_seq', max(id_etapa)) FROM tramitacao.etapa;

INSERT INTO tramitacao.etapa(id_fluxo, tx_etapa, dt_prazo) VALUES
(1, 'Análise GEO', null);

SELECT setval('tramitacao.condicao_id_condicao_seq', max(id_condicao)) FROM tramitacao.condicao;

INSERT INTO tramitacao.condicao(id_etapa, nm_condicao, fl_ativo) VALUES
((SELECT id_etapa FROM tramitacao.etapa where tx_etapa ilike 'Análise GEO'), 'Aguardando vinculação GEO pelo gerente', 1),
((SELECT id_etapa FROM tramitacao.etapa where tx_etapa ilike 'Análise GEO'), 'Aguardando análise GEO', 1),
((SELECT id_etapa FROM tramitacao.etapa where tx_etapa ilike 'Análise GEO'), 'Em análise GEO', 1),
((SELECT id_etapa FROM tramitacao.etapa where tx_etapa ilike 'Análise GEO'), 'Aguardando validação GEO pelo gerente', 1);

SELECT setval('tramitacao.situacao_id_situacao_seq', max(id_situacao)) FROM tramitacao.situacao;

INSERT INTO tramitacao.situacao(tx_descricao, fl_ativo)
VALUES ('Notificado via GEO', 1);

COMMIT;

# --- !Downs

DELETE FROM tramitacao.situacao WHERE tx_descricao = 'Notificado via GEO';

DELETE FROM tramitacao.condicao
WHERE id_etapa = (SELECT id_etapa FROM tramitacao.etapa where tx_etapa ilike 'Análise GEO') AND
nm_condicao in ('Aguardando vinculação GEO pelo gerente', 'Aguardando análise GEO', 'Em análise GEO', 'Aguardando validação GEO pelo gerente');

DELETE FROM tramitacao.etapa WHERE id_fluxo = 1 AND tx_etapa = 'Análise GEO';

DELETE FROM tramitacao.acao WHERE tx_descricao IN (
    'Deferir análise GEO via gerente',
    'Indeferir análise GEO via gerente',
    'Iniciar análise GEO',
    'Invalidar parecer GEO pelo gerente',
    'Validar deferimento GEO pelo gerente',
    'Validar indeferimento GEO pelo gerente',
    'Solicitar ajustes parecer GEO pelo gerente',
    'Solicitar ajuste da análise GEO pelo presidente',
    'Resolver notificação GEO',
    'Invalidar parecer GEO encaminhando para outro GEO'
);
