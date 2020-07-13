var disabledFilterFields = {
    SITUACAO: 'SITUACAO',
    PERIODO_PROCESSO: 'PERIODO_PROCESSO',
    ANALISTA_TECNICO: 'ANALISTA_TECNICO',
    GERENCIA: 'GERENCIA',
    COORDENADORIA: 'COORDENADORIA',
    CONSULTOR_JURIDICO: 'CONSULTOR_JURIDICO',
    ANALISTA_GEO: 'ANALISTA_GEO'
};

exports.DISABLED_FILTER_FIELDS = disabledFilterFields;

var origemLicenca = {
    DISPENSA: 0,
    SIMPLIFICADO: 1
};

exports.ORIGEM_LICENCA = origemLicenca;

var tiposCaracterizacoes = {
    DISPENSA: 1,
    DECLARATORIO: 2,
    SIMPLIFICADO: 3,
    ORDINARIO: 4
};

exports.TIPOS_CARACTERIZACOES = tiposCaracterizacoes;

var tipoContato = {
	EMAIL: 1,
	TELEFONE_RESIDENCIAL: 2,
	TELEFONE_COMERCIAL: 3,
	TELEFONE_CELULAR: 4
};
 exports.TIPO_CONTATO = tipoContato;

 var tipoEndereco = {
	PRINCIPAL: 1,
	CORRESPONDENCIA: 2

};
exports.TIPO_ENDERECO = tipoEndereco;