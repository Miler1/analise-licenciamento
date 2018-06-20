
--- Os valores de notificacao_aberta deve ser false quando estiverem NULL

UPDATE analise.analise SET notificacao_aberta=FALSE WHERE notificacao_aberta IS NULL;

UPDATE analise.analise AS a
 SET notificacao_aberta=FALSE
 FROM analise.processo p,tramitacao.objeto_tramitavel o
 WHERE a.id_processo = p.id AND p.id_objeto_tramitavel = o.id_objeto_tramitavel AND o.id_condicao = 6;

UPDATE analise.analise AS a
 SET notificacao_aberta=FALSE
 FROM analise.notificacao n, analise.analise_juridica aj, analise.analise_tecnica att
 WHERE (a.id = att.id_analise OR a.id = aj.id_analise) AND (n.id_analise_juridica = aj.id OR n.id_analise_juridica = att.id)
 AND n.id_historico_tramitacao IS NULL AND a.notificacao_aberta = TRUE;