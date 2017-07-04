# --- !Ups


ALTER TABLE analise.analise_juridica ADD COLUMN id_usuario_validacao INTEGER;

ALTER TABLE analise.analise_juridica ADD CONSTRAINT fk_aj_usuario FOREIGN KEY(id_usuario_validacao)
REFERENCES portal_seguranca.usuario(id);

UPDATE analise.analise_juridica AS aj SET id_usuario_validacao=(SELECT h.id_usuario FROM tramitacao.historico_objeto_tramitavel h
JOIN analise.processo p ON h.id_historico_objeto_tramitavel=p.id_objeto_tramitavel
JOIN analise.analise a ON a.id_processo=p.id
WHERE aj.id_analise=a.id AND h.id_condicao_inicial=5 ORDER BY h.id_historico_objeto_tramitavel DESC LIMIT 1
);

COMMENT ON COLUMN analise.analise_juridica.id_usuario_validacao IS 'Identificador da entidade portal_seguranca.usuario que realizará o relacionamento entre as duas entidades.';


ALTER TABLE analise.analise_tecnica ADD COLUMN id_usuario_validacao INTEGER;

ALTER TABLE analise.analise_tecnica ADD CONSTRAINT fk_at_usuario FOREIGN KEY(id_usuario_validacao)
REFERENCES portal_seguranca.usuario(id);

UPDATE analise.analise_tecnica AS at SET id_usuario_validacao=(SELECT h.id_usuario FROM tramitacao.historico_objeto_tramitavel h
JOIN analise.processo p ON h.id_historico_objeto_tramitavel=p.id_objeto_tramitavel
JOIN analise.analise a ON a.id_processo=p.id
WHERE at.id_analise=a.id AND h.id_condicao_inicial=10 ORDER BY h.id_historico_objeto_tramitavel DESC LIMIT 1
);

COMMENT ON COLUMN analise.analise_tecnica.id_usuario_validacao IS 'Identificador da entidade portal_seguranca.usuario que realizará o relacionamento entre as duas entidades.';

COMMENT ON COLUMN tramitacao.historico_objeto_tramitavel.id_usuario IS 'Identificador da entidade portal_seguranca.usuario que realizará o relacionamento entre as entidades usuario e histórico_objeto_tramitavel. Identifica qual usuário executou a ação da tramitação.';


# --- !Downs


ALTER TABLE analise.analise_juridica DROP COLUMN id_usuario_validacao;
ALTER TABLE analise.analise_tecnica DROP COLUMN id_usuario_validacao;
COMMENT ON COLUMN tramitacao.historico_objeto_tramitavel.id_usuario IS 'Identificador da entidade usr_geocar_aplicacao.usuario que realizará o relacionamento entre as entidades usuario e histórico_objeto_tramitavel. Identifica qual usuário executou a ação da tramitação.';
