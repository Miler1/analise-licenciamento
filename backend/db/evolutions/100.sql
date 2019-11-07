# --- !Ups

ALTER TABLE analise.notificacao ADD COLUMN prazo_notificacao INTEGER;
COMMENT ON COLUMN analise.notificacao.prazo_notificacao IS 'Campo resposável por armazenar o prazo em dias para o atendimento da notificação.';

ALTER TABLE analise.notificacao RENAME COLUMN resposta_notificacao TO justificativa_documentacao;
COMMENT ON COLUMN analise.notificacao.justificativa_documentacao IS 'Campo resposável por armazenar a justificativa adicionada no licenciamento referente ao atendimento de documentação.';

ALTER TABLE analise.notificacao ADD COLUMN justificativa_retificacao_empreendimento TEXT;
COMMENT ON COLUMN analise.notificacao.justificativa_retificacao_empreendimento IS 'Campo resposável por armazenar a justificativa adicionada no licenciamento referente ao atendimento da retificação do empreendimento.';

ALTER TABLE analise.notificacao ADD COLUMN justificativa_retificacao_solicitacao TEXT;
COMMENT ON COLUMN analise.notificacao.justificativa_retificacao_solicitacao IS 'Campo resposável por armazenar a justificativa adicionada no licenciamento referente ao atendimento da retificação da solicitação.';


# --- !Downs

ALTER TABLE analise.notificacao DROP COLUMN justificativa_retificacao_solicitacao;

ALTER TABLE analise.notificacao DROP COLUMN justificativa_retificacao_empreendimento;

ALTER TABLE analise.notificacao RENAME COLUMN justificativa_documentacao TO resposta_notificacao;

ALTER TABLE analise.notificacao DROP COLUMN prazo_notificacao;



