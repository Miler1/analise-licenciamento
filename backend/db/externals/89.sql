# --- !Ups

INSERT INTO tramitacao.acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES 
	(74, 'Validar análise pelo diretor', 1, 1);
INSERT INTO tramitacao.acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES 
	(75, 'Invalidar análise pelo diretor', 1, 1);


# --- !Downs

DELETE FROM tramitacao.acao WHERE id_acao in (74, 75);