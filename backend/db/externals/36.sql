# --- !Ups

DROP VIEW tramitacao.vw_objeto_tramitavel;

# --- !Downs

CREATE VIEW tramitacao.vw_objeto_tramitavel AS
    SELECT o.id_objeto_tramitavel, s.id_condicao, s.nm_condicao, e.id_etapa, e.tx_etapa AS nm_etapa, o.id_usuario AS id_usuario_responsavel,
    p.nome AS nm_usuario_responsavel, o.id_pai, o.id_fluxo, o.id_responsavel_anterior, p_ant.nome AS nm_usuario_anterior,
    o.id_responsavel_fluxo_anterior, p_fx_ant.nome AS nm_usuario_fluxo_anterior,
    string_agg(DISTINCT (si.tx_descricao)::text, ', '::text) AS string_agg
    FROM ((((((((((objeto_tramitavel o JOIN condicao s ON ((o.id_condicao = s.id_condicao)))
    JOIN etapa e ON ((o.id_etapa = e.id_etapa)))
    LEFT JOIN portal_seguranca.usuario ui ON ((o.id_usuario = ui.id)))
    LEFT JOIN licenciamento.pessoa_fisica p ON ((ui.id_pessoa = p.id_pessoa)))
    LEFT JOIN portal_seguranca.usuario u_ant ON ((o.id_responsavel_anterior = u_ant.id)))
    LEFT JOIN licenciamento.pessoa_fisica p_ant ON ((u_ant.id_pessoa = p_ant.id_pessoa)))
    LEFT JOIN portal_seguranca.usuario u_fx_ant ON ((o.id_responsavel_fluxo_anterior = u_fx_ant.id)))
    LEFT JOIN licenciamento.pessoa_fisica p_fx_ant ON ((u_fx_ant.id_pessoa = p_fx_ant.id_pessoa)))
    LEFT JOIN rel_objeto_tramitavel_situacao rots ON ((rots.id_objeto_tramitavel = o.id_objeto_tramitavel)))
    LEFT JOIN situacao si ON ((si.id_situacao = rots.id_situacao)))
    GROUP BY o.id_objeto_tramitavel, s.id_condicao, s.nm_condicao, e.id_etapa, e.tx_etapa, o.id_usuario, p.nome, o.id_pai, o.id_fluxo,
    o.id_responsavel_anterior, p_ant.nome, o.id_responsavel_fluxo_anterior, p_fx_ant.nome;