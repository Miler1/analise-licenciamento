
--- Os valores de notificacao_aberta deve ser false quando estiverem NULL

UPDATE analise.analise SET notificacao_aberta=FALSE WHERE notificacao_aberta IS NULL;

UPDATE analise.analise AS a
 SET notificacao_aberta=FALSE
 FROM analise.processo p,tramitacao.objeto_tramitavel o
 WHERE a.id_processo = p.id AND p.id_objeto_tramitavel = o.id_objeto_tramitavel AND o.id_condicao = 6;