# --- !Ups


UPDATE tramitacao.condicao SET nm_condicao='Aguardando vinculação técnica pelo gerente' WHERE nm_condicao ILIKE 'Aguardando vinculação técnica';
UPDATE tramitacao.condicao SET nm_condicao='Aguardando validação técnica pelo gerente' WHERE nm_condicao ILIKE 'Aguardando validação técnica';

INSERT INTO tramitacao.condicao(id_etapa,nm_condicao,fl_ativo) VALUES
(2,'Aguardando vinculação técnica pelo coordenador',1),
(2,'Aguardando validação técnica pelo coordenador',1);


UPDATE tramitacao.acao SET tx_descricao='Deferir análise técnica via gerente' WHERE tx_descricao ILIKE 'Deferir análise técnica';
UPDATE tramitacao.acao SET tx_descricao='Indeferir análise técnica via gerente' WHERE tx_descricao ILIKE 'Indeferir análise técnica';
UPDATE tramitacao.acao SET tx_descricao='Validar deferimento técnico pelo gerente' WHERE tx_descricao ILIKE 'Validar deferimento técnico';
UPDATE tramitacao.acao SET tx_descricao='Invalidar parecer técnico pelo gerente' WHERE tx_descricao ILIKE 'Invalidar parecer técnico';
UPDATE tramitacao.acao SET tx_descricao='Solicitar ajustes parecer técnico pelo gerente' WHERE tx_descricao ILIKE 'Solicitar ajustes parecer técnico';
UPDATE tramitacao.acao SET tx_descricao='Validar indeferimento técnico pelo gerente' WHERE tx_descricao ILIKE 'Validar indeferimento técnico';

INSERT INTO tramitacao.acao(tx_descricao,fl_ativo,fl_tramitavel) VALUES
('Vincular gerente',1,1),
('Indeferir análise técnica via coordenador',1,1),
('Deferir análise técnica via coordenador',1,1),
('Validar deferimento técnico pelo coordenador',1,1),
('Validar indeferimento técnico pelo coordenador',1,1),
('Invalidar parecer técnico pelo coordenador encaminhando para outro gerente',1,1),
('Invalidar parecer técnico encaminhando para outro técnico',1,1),
('Solicitar ajuste do parecer técnico pelo coordenador',1,1);


UPDATE tramitacao.transicao SET id_condicao_final=(SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao ILIKE 'Aguardando vinculação técnica pelo coordenador')
WHERE id_condicao_inicial=(SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao ILIKE 'Aguardando validação jurídica')
AND id_condicao_final=(SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao ILIKE 'Aguardando vinculação técnica pelo gerente') 
AND id_acao=(SELECT id_acao FROM tramitacao.acao WHERE tx_descricao ILIKE 'Validar deferimento jurídico');

UPDATE tramitacao.transicao SET id_condicao_final=(SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao ILIKE 'Aguardando validação técnica pelo coordenador') 
WHERE id_condicao_inicial=(SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao ILIKE 'Aguardando validação técnica pelo gerente') 
AND id_acao=(SELECT id_acao FROM tramitacao.acao WHERE tx_descricao ILIKE 'Validar deferimento técnico pelo gerente');

UPDATE tramitacao.transicao SET id_condicao_final=(SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao ILIKE 'Aguardando validação técnica pelo coordenador') 
WHERE id_condicao_inicial=(SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao ILIKE 'Aguardando validação técnica pelo gerente') 
AND id_acao=(SELECT id_acao FROM tramitacao.acao WHERE tx_descricao ILIKE 'Invalidar deferimento técnico pelo gerente');


INSERT INTO tramitacao.transicao(id_condicao_inicial,id_condicao_final,id_acao) VALUES
((SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao ILIKE 'Aguardando vinculação técnica pelo coordenador'),
(SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao ILIKE 'Aguardando vinculação técnica pelo gerente'),
(SELECT id_acao FROM tramitacao.acao WHERE tx_descricao ILIKE 'Vincular gerente')),

((SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao ILIKE 'Aguardando validação técnica pelo gerente'),
(SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao ILIKE 'Aguardando validação técnica pelo coordenador'),
(SELECT id_acao FROM tramitacao.acao WHERE tx_descricao ILIKE 'Validar deferimento técnico pelo gerente')),

((SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao ILIKE 'Aguardando validação técnica pelo coordenador'),
(SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao ILIKE 'Aguardando assinatura do diretor'),
(SELECT id_acao FROM tramitacao.acao WHERE tx_descricao ILIKE 'Validar deferimento técnico pelo coordenador')),

((SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao ILIKE 'Aguardando validação técnica pelo coordenador'),
(SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao ILIKE 'Arquivado'),
(SELECT id_acao FROM tramitacao.acao WHERE tx_descricao ILIKE 'Validar indeferimento técnico pelo coordenador')),

((SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao ILIKE 'Aguardando validação técnica pelo coordenador'),
(SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao ILIKE 'Aguardando análise técnica'),
(SELECT id_acao FROM tramitacao.acao WHERE tx_descricao ILIKE 'Invalidar parecer técnico encaminhando para outro técnico')),

((SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao ILIKE 'Aguardando validação técnica pelo coordenador'),
(SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao ILIKE 'Aguardando vinculação técnica pelo gerente'),
(SELECT id_acao FROM tramitacao.acao WHERE tx_descricao ILIKE 'Invalidar parecer técnico pelo coordenador encaminhando para outro gerente')),

((SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao ILIKE 'Aguardando validação técnica pelo coordenador'),
(SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao ILIKE 'Aguardando análise técnica'),
(SELECT id_acao FROM tramitacao.acao WHERE tx_descricao ILIKE 'Solicitar ajuste do parecer técnico pelo coordenador')),

((SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao ILIKE 'Aguardando vinculação técnica pelo coordenador'),
(SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao ILIKE 'Em análise técnica'),
(SELECT id_acao FROM tramitacao.acao WHERE tx_descricao ILIKE 'Vincular analista')),

((SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao ILIKE 'Em análise técnica'),
(SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao ILIKE 'Aguardando validação técnica pelo coordenador'),
(SELECT id_acao FROM tramitacao.acao WHERE tx_descricao ILIKE 'Deferir análise técnica via coordenador')),

((SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao ILIKE 'Em análise técnica'),
(SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao ILIKE 'Aguardando validação técnica pelo coordenador'),
(SELECT id_acao FROM tramitacao.acao WHERE tx_descricao ILIKE 'Indeferir análise técnica via coordenador'));


# --- !Downs


UPDATE tramitacao.transicao SET id_condicao_final=(SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao ILIKE 'Aguardando vinculação técnica pelo gerente')
WHERE id_condicao_inicial=(SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao ILIKE 'Aguardando validação jurídica')
AND id_condicao_final=(SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao ILIKE 'Aguardando vinculação técnica pelo coordenador') 
AND id_acao=(SELECT id_acao FROM tramitacao.acao WHERE tx_descricao ILIKE 'Validar deferimento jurídico');

UPDATE tramitacao.transicao SET id_condicao_final=(SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao ILIKE 'Aguardando validação técnica pelo gerente') 
WHERE id_condicao_inicial=(SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao ILIKE 'Aguardando validação técnica pelo gerente') 
AND id_acao=(SELECT id_acao FROM tramitacao.acao WHERE tx_descricao ILIKE 'Validar deferimento técnico pelo gerente');

DELETE FROM tramitacao.transicao WHERE id_transicao BETWEEN 
	(SELECT id_transicao FROM tramitacao.transicao WHERE id_condicao_inicial=(SELECT id_condicao FROM tramitacao.condicao 
		WHERE nm_condicao ILIKE 'Aguardando vinculação técnica pelo coordenador') 
		AND id_condicao_final =(SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao ILIKE 'Aguardando vinculação técnica pelo gerente')
		AND id_acao =(SELECT id_acao FROM tramitacao.acao WHERE tx_descricao ILIKE 'Vincular gerente')
	)
   AND
	(SELECT id_transicao FROM tramitacao.transicao WHERE id_condicao_inicial=(SELECT id_condicao FROM tramitacao.condicao 
		WHERE nm_condicao ILIKE 'Em análise técnica') 
		AND id_condicao_final =(SELECT id_condicao FROM tramitacao.condicao WHERE nm_condicao ILIKE 'Aguardando validação técnica pelo coordenador')
		AND id_acao =(SELECT id_acao FROM tramitacao.acao WHERE tx_descricao ILIKE 'Indeferir análise técnica via coordenador')
	);

UPDATE tramitacao.condicao SET nm_condicao='Aguardando vinculação técnica' WHERE nm_condicao ILIKE 'Aguardando vinculação técnica pelo gerente';
UPDATE tramitacao.condicao SET nm_condicao='Aguardando validação técnica' WHERE nm_condicao ILIKE 'Aguardando validação técnica pelo gerente';

DELETE FROM tramitacao.condicao WHERE nm_condicao IN ('Aguardando vinculação técnica pelo coordenador','Aguardando validação técnica pelo coordenador');

UPDATE tramitacao.acao SET tx_descricao='Deferir análise técnica' WHERE tx_descricao ILIKE 'Deferir análise técnica via gerente';
UPDATE tramitacao.acao SET tx_descricao='Indeferir análise técnica' WHERE tx_descricao ILIKE 'Indeferir análise técnica via gerente';
UPDATE tramitacao.acao SET tx_descricao='Validar deferimento técnico' WHERE tx_descricao ILIKE 'Validar deferimento técnico pelo gerente';
UPDATE tramitacao.acao SET tx_descricao='Invalidar parecer técnico' WHERE tx_descricao ILIKE 'Invalidar parecer técnico pelo gerente';
UPDATE tramitacao.acao SET tx_descricao='Solicitar ajustes parecer técnico' WHERE tx_descricao ILIKE 'Solicitar ajustes parecer técnico pelo gerente';
UPDATE tramitacao.acao SET tx_descricao='Validar indeferimento técnico' WHERE tx_descricao ILIKE 'Validar indeferimento técnico pelo gerente';

DELETE FROM tramitacao.acao WHERE tx_descricao IN
('Vincular gerente','Indeferir análise técnica via coordenador','Deferir análise técnica via coordenador','Validar deferimento técnico pelo coordenador','Validar indeferimento técnico pelo coordenador','Invalidar parecer técnico pelo coordenador encaminhando para outro gerente','Invalidar parecer técnico encaminhando para outro técnico','Solicitar ajuste do parecer técnico pelo coordenador');