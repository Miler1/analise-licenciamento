-- Limpeza de dados dinâmicos de analise, licenciamento, tramitacao e portal de seguranca

-- Obs:
-- Após executar o script o usuário deve ser criado pela interface do sistema
-- O perfil deve ser mudado no banco para administrador para acessar a área administrativa
--    (tabela "perfil_usuario", "id_perfil" mudado para 1)

-- Caso não queira perder pessoas e usúarios já cadastrados, não execute o bloco de limpar pessoas do licenciamneto e
--    portal de segurança.


-- Limpar analise
DELETE FROM analise.notificacao;
DELETE FROM analise.analise_documento;
DELETE FROM analise.consultor_juridico;
DELETE FROM analise.analise_juridica;
DELETE FROM analise.analista_tecnico;
DELETE FROM analise.licenca_suspensa;
DELETE FROM analise.licenca_cancelada;
DELETE FROM licenciamento.licenca;
DELETE FROM analise.licenca_analise;
DELETE FROM analise.gerente_tecnico;
DELETE FROM analise.analise_tecnica;
DELETE FROM analise.dia_analise;
DELETE FROM analise.analise;
DELETE FROM analise.dispensa_licencamento_cancelada;
DELETE FROM analise.documento;
DELETE FROM analise.rel_processo_caracterizacao;
-- manejo
DELETE FROM analise.rel_base_vetorial_analise_manejo;
DELETE FROM analise.analise_vetorial;
DELETE FROM analise.analise_ndfi;
DELETE FROM analise.observacao;
DELETE FROM analise.documento_manejo_shape;
DELETE FROM analise.analista_tecnico_manejo;
DELETE FROM analise.documento_manejo;
DELETE FROM analise.vinculo_analise_tecnica_manejo_consideracao;
DELETE FROM analise.vinculo_analise_tecnica_manejo_insumo;
DELETE FROM analise.vinculo_analise_tecnica_manejo_embasamento_legal;
DELETE FROM analise.analista_tecnico_manejo;
DELETE FROM analise.analise_tecnica_manejo;
DELETE FROM analise.processo_manejo;
DELETE FROM analise.empreendimento_manejo;

-- Limpar licenciamento
-- caracterização
DELETE FROM licenciamento.geometria_atividade;
DELETE FROM licenciamento.rel_atividade_caracterizacao_cnae;
DELETE FROM licenciamento.atividade_caracterizacao;
DELETE FROM licenciamento.dae;
DELETE FROM licenciamento.rel_caracterizacao_resposta;
DELETE FROM licenciamento.solicitacao_documento_caracterizacao;
DELETE FROM licenciamento.rel_tipo_licenca_caracterizacao_andamento;
DELETE FROM licenciamento.dispensa_licenciamento;
DELETE FROM licenciamento.rel_caracterizacao_municipio;
DELETE FROM licenciamento.caracterizacao;
-- empreendimento
DELETE FROM licenciamento.imovel_empreendimento;
DELETE FROM licenciamento.proprietario;
DELETE FROM licenciamento.documento_responsavel_empreendimento;
DELETE FROM licenciamento.responsavel_empreendimento;
DELETE FROM licenciamento.historico_alteracao_empreendimento_empreendedor;
DELETE FROM licenciamento.historico_alteracao_empreendimento_pessoa;
DELETE FROM licenciamento.empreendimento;
DELETE FROM licenciamento.documento;
-- empreendedor
DELETE FROM licenciamento.representante_legal;
DELETE FROM licenciamento.empreendedor;
-- pessoa
DELETE FROM licenciamento.endereco_pessoa;
DELETE FROM licenciamento.pessoa_fisica;
DELETE FROM licenciamento.pessoa_juridica;
DELETE FROM licenciamento.pessoa;
DELETE FROM licenciamento.contato;
DELETE FROM licenciamento.endereco;


-- Limpar tramitacao
DELETE FROM tramitacao.historico_objeto_tramitavel;
DELETE FROM tramitacao.rel_objeto_tramitavel_situacao;
DELETE FROM tramitacao.objeto_tramitavel;


-- Limpar porta de seguranca
DELETE FROM portal_seguranca.perfil_usuario;
DELETE FROM portal_seguranca.usuario;