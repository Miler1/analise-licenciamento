
--- Os valores de notificacao_aberta deve ser false quando estiverem NULL

UPDATE analise.analise SET notificacao_aberta=FALSE WHERE notificacao_aberta IS NULL;