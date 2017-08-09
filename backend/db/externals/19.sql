# --- !Ups

UPDATE tramitacao.acao SET tx_descricao = 'Solicitar ajustes do parecer técnico pelo coordenador para o analista' WHERE id_acao = 26;

INSERT INTO tramitacao.acao(id_acao,tx_descricao,fl_ativo,fl_tramitavel) VALUES
(27,'Solicitar ajustes do parecer técnico pelo coordenador para o gerente',1,1);

INSERT INTO tramitacao.transicao(id_transicao,id_condicao_inicial,id_condicao_final,id_acao) VALUES (29,13,10,27);

--Ação 27 (Solicitar ajuste do parecer técnico pelo coordenador) - Remover situações "Deferido" e "Indeferido" - (Nº da transição: 29)
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (37, 1, 29, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (38, 2, 29, 0);

# --- !Downs

DELETE FROM tramitacao.config_situacao WHERE id_config_situacao in (37, 38);

DELETE FROM  tramitacao.transicao WHERE id_transicao = 29;

DELETE FROM  tramitacao.acao WHERE id_acao = 27;

UPDATE tramitacao.acao SET tx_descricao = 'Solicitar ajuste do parecer técnico pelo coordenador' WHERE id_acao = 26;