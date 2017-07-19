# --- !Ups

UPDATE analise.analise_juridica AS aj SET id_usuario_validacao=(SELECT h.id_usuario FROM tramitacao.historico_objeto_tramitavel h
JOIN analise.processo p ON h.id_historico_objeto_tramitavel=p.id_objeto_tramitavel
JOIN analise.analise a ON a.id_processo=p.id
WHERE aj.id_analise=a.id AND h.id_condicao_inicial=5 ORDER BY h.id_historico_objeto_tramitavel DESC LIMIT 1
);


UPDATE analise.analise_tecnica AS at SET id_usuario_validacao=(SELECT h.id_usuario FROM tramitacao.historico_objeto_tramitavel h
JOIN analise.processo p ON h.id_historico_objeto_tramitavel=p.id_objeto_tramitavel
JOIN analise.analise a ON a.id_processo=p.id
WHERE at.id_analise=a.id AND h.id_condicao_inicial=10 ORDER BY h.id_historico_objeto_tramitavel DESC LIMIT 1
);

# --- !Downs
 
 UPDATE analise.analise_juridica SET id_usuario_validacao=NULL;
 
 UPDATE analise.analise_tecnica SET id_usuario_validacao=NULL;