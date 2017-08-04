# --- !Ups


UPDATE tramitacao.condicao SET nm_condicao='Aguardando vinculação técnica pelo gerente' WHERE id_condicao=7;
UPDATE tramitacao.condicao SET nm_condicao='Aguardando validação técnica pelo gerente' WHERE id_condicao=10;

INSERT INTO tramitacao.condicao(id_condicao,id_etapa,nm_condicao,fl_ativo) VALUES
(12,2,'Aguardando vinculação técnica pelo coordenador',1),
(13,2,'Aguardando validação técnica pelo coordenador',1);



UPDATE tramitacao.acao SET tx_descricao='Deferir análise técnica via gerente' WHERE id_acao=12;
UPDATE tramitacao.acao SET tx_descricao='Indeferir análise técnica via gerente' WHERE id_acao=13;
UPDATE tramitacao.acao SET tx_descricao='Validar deferimento técnico pelo gerente' WHERE id_acao=16;
UPDATE tramitacao.acao SET tx_descricao='Invalidar parecer técnico pelo gerente' WHERE id_acao=14;
UPDATE tramitacao.acao SET tx_descricao='Solicitar ajustes parecer técnico pelo gerente' WHERE id_acao=15;
UPDATE tramitacao.acao SET tx_descricao='Validar indeferimento técnico pelo gerente' WHERE id_acao=17;

INSERT INTO tramitacao.acao(id_acao,tx_descricao,fl_ativo,fl_tramitavel) VALUES
(19,'Vincular gerente',1,1),
(20,'Indeferir análise técnica via coordenador',1,1),
(21,'Deferir análise técnica via coordenador',1,1),
(22,'Validar deferimento técnico pelo coordenador',1,1),
(23,'Validar indeferimento técnico pelo coordenador',1,1),
(24,'Invalidar parecer técnico pelo coordenador encaminhando para outro gerente',1,1),
(25,'Invalidar parecer técnico encaminhando para outro técnico',1,1),
(26,'Solicitar ajuste do parecer técnico pelo coordenador',1,1),
(27,'Invalidar deferimento técnico pelo gerente',1,1),
(28,'Deferir análise técnica via coordenador',1,1);


UPDATE tramitacao.transicao SET id_condicao_final=12 WHERE id_condicao_inicial=5 AND id_condicao_final=7 AND id_acao=6;

UPDATE tramitacao.transicao SET id_condicao_final=13 
WHERE id_condicao_inicial=10
AND id_acao=16;

UPDATE tramitacao.transicao SET id_condicao_final=13 
WHERE id_condicao_inicial=10 
AND id_acao=27;

UPDATE tramitacao.transicao SET id_condicao_final = 13 WHERE id_transicao = 17;

INSERT INTO tramitacao.transicao(id_transicao,id_condicao_inicial,id_condicao_final,id_acao) VALUES
(20,12,7,19),
(21,10,11,16),
(22,13,11,22),
(23,13,6,23),
(24,13,8,25),
(25,13,7,24),
(26,13,8,26),
(27,12,9,10),
(28,9,13,28),
(29,9,13,20);

INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (25, 1, 27, 1);
--Ação 20 (Indeferir análise técnica via coordenador) - Adicionar situação "Indeferido" - (Nº da transição: 28)
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (26, 2, 28, 1);

--Ação 22 (Validar deferimento técnico pelo coordenador) - Remover situações "Deferido" e "Indeferido" - (Nº da transição: 21)
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (27, 1, 21, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (28, 2, 21, 0);

--Ação 23 (Validar indeferimento técnico pelo coordenador) - Remover situações "Deferido" e "Indeferido" - (Nº da transição: 22)
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (29, 1, 22, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (30, 2, 22, 0);

--Ação 24 (Invalidar parecer técnico pelo coordenador encaminhando para outro gerente) - Remover situações "Deferido" e "Indeferido" - (Nº da transição: 24)
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (31, 1, 24, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (32, 2, 24, 0);

--Ação 25 (Invalidar parecer técnico encaminhando para outro técnico) - Remover situações "Deferido" e "Indeferido" - (Nº da transição: 23)
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (33, 1, 23, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (34, 2, 23, 0);

--Ação 26 (Solicitar ajuste do parecer técnico pelo coordenador) - Remover situações "Deferido" e "Indeferido" - (Nº da transição: 25)
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (35, 1, 25, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (36, 2, 25, 0);


-- [FIM] Configurar adição e remoção de situações

-- [INICIO] Configuração dos impedimentos

--Ação 16 (Validar deferimento técnico pelo gerente) - Situação "Deferido" obrigatória - (Nº da transição: 16)
INSERT INTO tramitacao.impedimento_transicao (id_impedimento_transicao, id_situacao, id_transicao, tp_impedimento) VALUES (3, 1, 16, 1);
--Ação 17 (Validar indeferimento técnico pelo gerente) - Situação "Indeferido" obrigatória - (Nº da transição: 17)
INSERT INTO tramitacao.impedimento_transicao (id_impedimento_transicao, id_situacao, id_transicao, tp_impedimento) VALUES (4, 2, 17, 1);

--Ação 22 (Validar deferimento técnico pelo coordenador) - Situação "Deferido" obrigatória - (Nº da transição: 21)
INSERT INTO tramitacao.impedimento_transicao (id_impedimento_transicao, id_situacao, id_transicao, tp_impedimento) VALUES (5, 1, 21, 1);
--Ação 23 (Validar indeferimento pelo coordenador) - Situação "Indeferido" obrigatória - (Nº da transição: 22)
INSERT INTO tramitacao.impedimento_transicao (id_impedimento_transicao, id_situacao, id_transicao, tp_impedimento) VALUES (6, 2, 22, 1);


# --- !Downs

DELETE FROM tramitacao.impedimento_transicao WHERE id_impedimento_transicao IN (3,4,5,6);

DELETE FROM tramitacao.config_situacao WHERE id_config_situacao IN(25,26,27,28,29,30,31,32,33,34,35);

UPDATE tramitacao.transicao SET id_condicao_final=7 WHERE id_condicao_inicial=5 AND id_condicao_final=12 AND id_acao=6;

UPDATE tramitacao.transicao SET id_condicao_final=10
WHERE id_condicao_inicial=10
AND id_acao=16;

UPDATE tramitacao.transicao SET id_condicao_final = 6 WHERE id_transicao = 17;

DELETE FROM tramitacao.transicao WHERE id_transicao IN (20,21,22,23,24,25,26,27,28,29);

DELETE FROM tramitacao.acao WHERE id_acao IN (19,20,21,22,23,24,25,26,27,28);

UPDATE tramitacao.acao SET tx_descricao='Deferir análise técnica' WHERE id_acao=12;
UPDATE tramitacao.acao SET tx_descricao='Indeferir análise técnica' WHERE id_acao=13;
UPDATE tramitacao.acao SET tx_descricao='Validar deferimento técnico' WHERE id_acao=16;
UPDATE tramitacao.acao SET tx_descricao='Invalidar parecer técnico pelo' WHERE id_acao=14;
UPDATE tramitacao.acao SET tx_descricao='Solicitar ajustes parecer técnico' WHERE id_acao=15;
UPDATE tramitacao.acao SET tx_descricao='Validar indeferimento técnico' WHERE id_acao=17;

UPDATE tramitacao.condicao SET nm_condicao='Aguardando vinculação técnica' WHERE id_condicao=7;
UPDATE tramitacao.condicao SET nm_condicao='Aguardando validação técnica' WHERE id_condicao=10;

DELETE FROM tramitacao.condicao WHERE id_condicao IN (12,13);