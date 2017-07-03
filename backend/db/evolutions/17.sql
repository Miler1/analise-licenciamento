# --- !Ups


ALTER TABLE analise.analise_juridica ADD COLUMN id_usuario_validacao INTEGER;

ALTER TABLE analise.analise_juridica ADD CONSTRAINT fk_aj_usuario FOREIGN KEY(id_usuario_validacao)
REFERENCES portal_seguranca.usuario(id);

UPDATE analise.analise_juridica AS aj SET id_usuario_validacao=(SELECT h.id_usuario FROM tramitacao.historico_objeto_tramitavel h
JOIN analise.processo p ON h.id_historico_objeto_tramitavel=p.id_objeto_tramitavel
JOIN analise.analise a ON a.id_processo=p.id
WHERE aj.id_analise=a.id AND h.id_condicao_inicial=5 ORDER BY h.id_historico_objeto_tramitavel DESC LIMIT 1
);



ALTER TABLE analise.analise_tecnica ADD COLUMN id_usuario_validacao INTEGER;

ALTER TABLE analise.analise_tecnica ADD CONSTRAINT fk_at_usuario FOREIGN KEY(id_usuario_validacao)
REFERENCES portal_seguranca.usuario(id);

UPDATE analise.analise_tecnica AS at SET id_usuario_validacao=(SELECT h.id_usuario FROM tramitacao.historico_objeto_tramitavel h
JOIN analise.processo p ON h.id_historico_objeto_tramitavel=p.id_objeto_tramitavel
JOIN analise.analise a ON a.id_processo=p.id
WHERE at.id_analise=a.id AND h.id_condicao_inicial=10 ORDER BY h.id_historico_objeto_tramitavel DESC LIMIT 1
);



# --- !Downs


ALTER TABLE analise.analise_juridica DROP COLUMN id_usuario_validacao;
ALTER TABLE analise.analise_tecnica DROP COLUMN id_usuario_validacao;