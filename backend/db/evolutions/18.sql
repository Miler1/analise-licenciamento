# --- !Ups

COMMENT ON TABLE analise.analise_tecnica IS 'Entidade responsável por armazenar as análises técnicas.';
COMMENT ON COLUMN analise.analise_tecnica.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.analise_tecnica.id_analise IS 'Identificador da tabela analise, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.analise_tecnica.parecer IS 'Parecer da análise técnica.';
COMMENT ON COLUMN analise.analise_tecnica.data_vencimento_prazo IS 'Data de vencimento do prazo da análise.';
COMMENT ON COLUMN analise.analise_tecnica.revisao_solicitada IS 'Flag que indica se esta análise é uma revisão.';
COMMENT ON COLUMN analise.analise_tecnica.ativo IS 'Indica se a análise ainda está ativa.';
COMMENT ON COLUMN analise.analise_tecnica.id_analise_tecnica_revisada IS 'Identificador da tabela analise_tecnica, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.analise_tecnica.data_inicio IS 'Data de início da análise.';
COMMENT ON COLUMN analise.analise_tecnica.data_fim IS 'Data de fim de análise.';
COMMENT ON COLUMN analise.analise_tecnica.id_tipo_resultado_analise IS 'Campo responsavel por armazenar o resultado da análise do consultor técnico.';
COMMENT ON COLUMN analise.analise_tecnica.id_tipo_resultado_validacao IS 'Campo responsavel por armazenar o resultado da análise do coordenador técnico.';

COMMENT ON TABLE analise.licenca_analise IS 'Entidade responsável por armazenar as licenças que estão em análise.';
COMMENT ON COLUMN analise.licenca_analise.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.licenca_analise.id_analise_tecnica IS 'Identificador da entidade analise_tecnica que faz o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.licenca_analise.validade IS 'Validade da licença em dias.';
COMMENT ON COLUMN analise.licenca_analise.id_caracterizacao IS 'Identificador da caracterizacao que faz o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.licenca_analise.observacao IS 'Descrição da observação.';


COMMENT ON TABLE analise.condicionante IS 'Entidade responsável por armazenar as condicionantes das licença de análise.';
COMMENT ON COLUMN analise.condicionante.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.condicionante.id_licenca_analise IS 'Identificador da entidade licenca_analise que faz o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.condicionante.texto IS 'Descrição da condicionante.';
COMMENT ON COLUMN analise.condicionante.prazo IS 'Prazo em dias da condicionante.';
COMMENT ON COLUMN analise.condicionante.ordem IS 'Ordem de exibição das condicionantes.';


COMMENT ON TABLE analise.recomendacao IS 'Entidade responsável por armazenar as recomendações da licença de análise.';
COMMENT ON COLUMN analise.recomendacao.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.recomendacao.id_licenca_analise IS 'Identificador da entidade licenca_analise que faz o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.recomendacao.texto IS 'Descrição da recomendação.';
COMMENT ON COLUMN analise.recomendacao.ordem IS 'Ordem de exibição das recomendações.';


COMMENT ON TABLE analise.parecer_tecnico_restricao IS 'Entidade responsável por armazenar o parecer das restrições geográficas.';
COMMENT ON COLUMN analise.parecer_tecnico_restricao.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.parecer_tecnico_restricao.id_analise_tecnica IS '';
COMMENT ON COLUMN analise.parecer_tecnico_restricao.codigo_camada IS 'Código da camada do geoserver.';
COMMENT ON COLUMN analise.parecer_tecnico_restricao.parecer IS 'Descrição do parecer de restrição geográfico.';

COMMENT ON TABLE analise.analista_tecnico IS 'Entidade responsável por armazenar o analista da analise técnica.';
COMMENT ON COLUMN analise.analista_tecnico.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.analista_tecnico.id_analise_tecnica IS 'Identificador da entidade análise que faz o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.analista_tecnico.id_usuario IS 'Identificador da entidade usuario que faz o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.analista_tecnico.data_vinculacao IS 'Data em que o usuario foi vinculado.';

COMMENT ON TABLE analise.rel_documento_analise_tecnica IS 'Entidade responsável por armazenar o relacionamente entre as entidades documento e analise técnica.';
COMMENT ON COLUMN analise.rel_documento_analise_tecnica.id_documento IS 'Identificador da entidade documento.';
COMMENT ON COLUMN analise.rel_documento_analise_tecnica.id_analise_tecnica IS 'Identificador da entidade analise_tecnica.';

# --- !Downs