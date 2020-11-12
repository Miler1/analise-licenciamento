# --- !Ups

UPDATE tramitacao.acao SET tx_descricao = 'Notificar pelo analista GEO' WHERE id_acao = 3;
UPDATE tramitacao.acao SET tx_descricao = 'Deferir análise técnica' WHERE id_acao = 12;
UPDATE tramitacao.acao SET tx_descricao = 'Indeferir análise técnica' WHERE id_acao = 13;
UPDATE tramitacao.acao SET tx_descricao = 'Solicitar ajustes do parecer técnico pelo gerente técnico' WHERE id_acao = 15;
UPDATE tramitacao.acao SET tx_descricao = 'Deferir análise GEO' WHERE id_acao = 49;
UPDATE tramitacao.acao SET tx_descricao = 'Indeferir análise GEO' WHERE id_acao = 50;
UPDATE tramitacao.acao SET tx_descricao = 'Solicitar ajustes do parecer GEO pelo gerente técnico' WHERE id_acao = 55;
UPDATE tramitacao.acao SET tx_descricao = 'Solicitar ajuste da análise GEO pelo diretor presidente' WHERE id_acao = 56;
UPDATE tramitacao.acao SET tx_descricao = 'Solicitar desvínculo da análise GEO' WHERE id_acao = 59;
UPDATE tramitacao.acao SET tx_descricao = 'Iniciar análise GEO pelo gerente técnico' WHERE id_acao = 62;
UPDATE tramitacao.acao SET tx_descricao = 'Notificar pelo analista técnico' WHERE id_acao = 66;
UPDATE tramitacao.acao SET tx_descricao = 'Iniciar análise técnica pelo gerente técnico' WHERE id_acao = 67;
UPDATE tramitacao.acao SET tx_descricao = 'Validar parecer técnico pelo gerente técnico' WHERE id_acao = 68;
UPDATE tramitacao.acao SET tx_descricao = 'Aprovar solicitação de desvínculo do analista técnico' WHERE id_acao = 69;
UPDATE tramitacao.acao SET tx_descricao = 'Negar solicitação de desvínculo do analista técnico' WHERE id_acao = 70;
UPDATE tramitacao.acao SET tx_descricao = 'Solicitar desvínculo da análise técnica' WHERE id_acao = 71;
UPDATE tramitacao.acao SET tx_descricao = 'Iniciar analise técnica por volta de notificação' WHERE id_acao = 73;
UPDATE tramitacao.acao SET tx_descricao = 'Iniciar análise pelo diretor presidente' WHERE id_acao = 76;

UPDATE tramitacao.condicao SET nm_condicao= 'Notificado pelo analista GEO' WHERE id_condicao = 4;
UPDATE tramitacao.condicao SET nm_condicao= 'Aguardando validação pelo gerente técnico' WHERE id_condicao = 28;
UPDATE tramitacao.condicao SET nm_condicao= 'Solicitação de desvínculo pendente da análise GEO' WHERE id_condicao = 30;
UPDATE tramitacao.condicao SET nm_condicao= 'Aguardando resposta de comunicado ao órgão' WHERE id_condicao = 32;
UPDATE tramitacao.condicao SET nm_condicao= 'Solicitação de desvínculo pendente da análise técnica' WHERE id_condicao = 33;
UPDATE tramitacao.condicao SET nm_condicao= 'Notificado pelo analista técnico' WHERE id_condicao = 35;


# --- !Downs

UPDATE tramitacao.condicao SET nm_condicao = 'Notificado pelo Analista GEO' WHERE id_condicao = 4;
UPDATE tramitacao.condicao SET nm_condicao = 'Aguardando validação gerente técnico' WHERE id_condicao = 28;
UPDATE tramitacao.condicao SET nm_condicao = 'Solicitação de desvínculo pendente análise GEO' WHERE id_condicao = 30;
UPDATE tramitacao.condicao SET nm_condicao = 'Aguardando resposta comunicado' WHERE id_condicao = 32;
UPDATE tramitacao.condicao SET nm_condicao = 'Solicitação de desvínculo pendente análise técnica' WHERE id_condicao = 33;
UPDATE tramitacao.condicao SET nm_condicao = 'Notificado pelo Analista técnico' WHERE id_condicao = 35;

UPDATE tramitacao.acao SET tx_descricao = 'Notificar pelo Analista GEO' WHERE id_acao = 3;
UPDATE tramitacao.acao SET tx_descricao = 'Deferir análise técnica via gerente técnico' WHERE id_acao = 12;
UPDATE tramitacao.acao SET tx_descricao = 'Indeferir análise técnica via gerente técnico' WHERE id_acao = 13;
UPDATE tramitacao.acao SET tx_descricao = 'Solicitar ajustes parecer técnico pelo gerente técnico' WHERE id_acao = 15;
UPDATE tramitacao.acao SET tx_descricao = 'Deferir análise GEO via gerente técnico' WHERE id_acao = 49;
UPDATE tramitacao.acao SET tx_descricao = 'Indeferir análise GEO via gerente técnico' WHERE id_acao = 50;
UPDATE tramitacao.acao SET tx_descricao = 'Solicitar ajustes parecer GEO pelo gerente técnico' WHERE id_acao = 55;
UPDATE tramitacao.acao SET tx_descricao = 'Solicitar ajuste da análise GEO pelo presidente' WHERE id_acao = 56;
UPDATE tramitacao.acao SET tx_descricao = 'Solicitar desvínculo análise Geo' WHERE id_acao = 59;
UPDATE tramitacao.acao SET tx_descricao = 'Iniciar análise gerente técnico' WHERE id_acao = 62;
UPDATE tramitacao.acao SET tx_descricao = 'Notificar pelo Analista técnico' WHERE id_acao = 66;
UPDATE tramitacao.acao SET tx_descricao = 'Iniciar análise técnica gerente técnico' WHERE id_acao = 67;
UPDATE tramitacao.acao SET tx_descricao = 'Validar parecer técnico gerente técnico' WHERE id_acao = 68;
UPDATE tramitacao.acao SET tx_descricao = 'Aprovar solicitação de desvínculo do Analista técnico' WHERE id_acao = 69;
UPDATE tramitacao.acao SET tx_descricao = 'Negar solicitação de desvínculo do Analista técnico' WHERE id_acao = 70;
UPDATE tramitacao.acao SET tx_descricao = 'Solicitar desvínculo análise técnica' WHERE id_acao = 71;
UPDATE tramitacao.acao SET tx_descricao = 'Iniciar analise Técnica por volta de notificação' WHERE id_acao = 73;
UPDATE tramitacao.acao SET tx_descricao = 'Iniciar análise do presidente' WHERE id_acao = 76;

