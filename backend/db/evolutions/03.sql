# --- !Ups


COMMENT ON TABLE analise.processo IS 'Entidade responsável por armazenar um processo em análise.';
COMMENT ON COLUMN analise.processo.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.processo.numero IS 'Número do processo.';
COMMENT ON COLUMN analise.processo.id_empreendimento IS 'Identificador da tabela empreendimento, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.processo.id_objeto_tramitavel IS 'Identificador da tabela objeto_tramitavel, responsável pelo relacionamento entre as duas tabelas.';

COMMENT ON TABLE analise.analise IS 'Entidade responsável por armazenar as análises referente ao processo.';
COMMENT ON COLUMN analise.analise.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.analise.id_processo IS 'Identificador da tabela processo, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.analise.data_cadastro IS 'Data de cadastro da análise.';
COMMENT ON COLUMN analise.analise.data_vencimento_prazo IS 'Data de vencimento da análise.';

COMMENT ON TABLE analise.tipo_resultado_analise IS 'Entidade responsável por armazenar os tipos de possíveis resultados da análise.';
COMMENT ON COLUMN analise.tipo_resultado_analise.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.tipo_resultado_analise.nome IS 'Nomo do tipo de resultado.';

COMMENT ON TABLE analise.analise_juridica IS 'Entidade responsável por armazenar as análises jurídicas.';
COMMENT ON COLUMN analise.analise_juridica.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.analise_juridica.id_analise IS 'Identificador da tabela analise, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.analise_juridica.parecer IS 'Parecer da análise jurídica.';
COMMENT ON COLUMN analise.analise_juridica.data_vencimento_prazo IS 'Data de vencimento do prazo da análise.';
COMMENT ON COLUMN analise.analise_juridica.revisao_solicitada IS 'Flag que indica se esta análise é uma revisão.';
COMMENT ON COLUMN analise.analise_juridica.ativo IS 'Indica se a análise ainda está ativa.';
COMMENT ON COLUMN analise.analise_juridica.id_analise_juridica_revisada IS 'Identificador da tabela analise_juridica, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.analise_juridica.data_inicio IS 'Data de início da análise.';
COMMENT ON COLUMN analise.analise_juridica.data_fim IS 'Data de fim de análise.';
COMMENT ON COLUMN analise.analise_juridica.id_tipo_resultado IS 'Identificador da tabela tipo_resultado, responsável pelo relacionamento entre as duas tabelas.';

COMMENT ON TABLE analise.consultor_juridico IS 'Entidade responsável por armazenar, os usuários que são consultores jurídicos em uma análise juridica.';
COMMENT ON COLUMN analise.consultor_juridico.id IS 'Identificador único da tabela consultor jurídico.';
COMMENT ON COLUMN analise.consultor_juridico.id_analise_juridica IS 'Identificador da tabela analise_juridica, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.consultor_juridico.id_usuario IS 'Identificador da tabela usuario, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.consultor_juridico.data_vinculacao IS 'Data em que foi vinculado a análise.';

COMMENT ON TABLE analise.analise_documento IS 'Entidade responsável por armazenar os documentos que estão sendo analisados.';
COMMENT ON COLUMN analise.analise_documento.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.analise_documento.id_analise_juridica IS 'Identificador da tabela analise_juridica, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.analise_documento.validado IS 'Flag que indica se o documento foi validado.';
COMMENT ON COLUMN analise.analise_documento.parecer IS 'Parecer da análise de documento.';
COMMENT ON COLUMN analise.analise_documento.id_documento IS 'Identificador da tabela documento, responsável pelo relacionamento entre as duas tabelas.';

COMMENT ON TABLE analise.rel_documento_analise_juridica IS 'Entidade responsável por armazenar';
COMMENT ON COLUMN analise.rel_documento_analise_juridica.id_documento IS 'Identificador da tabela documento, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.rel_documento_analise_juridica.id_analise_juridica IS 'Identificador da tabela analise_juridica, responsável pelo relacionamento entre as duas tabelas.';

COMMENT ON TABLE analise.rel_processo_caracterizacao IS 'Entidade responsável por armazenar';
COMMENT ON COLUMN analise.rel_processo_caracterizacao.id_caracterizacao IS 'Identificador da tabela caracterizacao, responsável pelo relacionamento entre as duas tabelas.';
COMMENT ON COLUMN analise.rel_processo_caracterizacao.id_processo IS 'Identificador da tabela processo, responsável pelo relacionamento entre as duas tabelas.';


# --- !Downs













