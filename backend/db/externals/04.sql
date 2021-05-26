# --- !Ups

UPDATE tramitacao.acao SET tx_descricao = 'Notificar pelo analista geo' WHERE id_acao = 3;
UPDATE tramitacao.acao SET tx_descricao = 'Deferir análise técnica' WHERE id_acao = 12;
UPDATE tramitacao.acao SET tx_descricao = 'Indeferir análise técnica' WHERE id_acao = 13; 
UPDATE tramitacao.acao SET tx_descricao = 'Invalidar parecer técnico pelo coordenador' WHERE id_acao = 14;
UPDATE tramitacao.acao SET tx_descricao = 'Solicitar ajustes do parecer técnico pelo coordenador' WHERE id_acao = 15;
UPDATE tramitacao.acao SET tx_descricao = 'Validar deferimento técnico pelo coordenador' WHERE id_acao = 16;
UPDATE tramitacao.acao SET tx_descricao = 'Validar indeferimento técnico pelo coordenador' WHERE id_acao = 17;
UPDATE tramitacao.acao SET tx_descricao = 'Vincular coordenador' WHERE id_acao = 19;
UPDATE tramitacao.acao SET tx_descricao = 'Invalidar parecer técnico pelo coordenador encaminhando para outro analista' WHERE id_acao = 24; 
UPDATE tramitacao.acao SET tx_descricao = 'Solicitar ajustes do parecer técnico pelo coordenador para o analista' WHERE id_acao = 27; 
UPDATE tramitacao.acao SET tx_descricao = 'Deferir análise geo' WHERE id_acao = 49;
UPDATE tramitacao.acao SET tx_descricao = 'Indeferir análise geo' WHERE id_acao = 50;
UPDATE tramitacao.acao SET tx_descricao = 'Iniciar análise geo' WHERE id_acao = 51;
UPDATE tramitacao.acao SET tx_descricao = 'Invalidar parecer geo pelo coordenador' WHERE id_acao = 52;
UPDATE tramitacao.acao SET tx_descricao = 'Validar deferimento geo pelo coordenador' WHERE id_acao = 53;
UPDATE tramitacao.acao SET tx_descricao = 'Validar indeferimento geo pelo coordenador' WHERE id_acao = 54;
UPDATE tramitacao.acao SET tx_descricao = 'Solicitar ajustes do parecer geo pelo coordenador' WHERE id_acao = 55;
UPDATE tramitacao.acao SET tx_descricao = 'Solicitar ajuste da análise geo pelo secretário' WHERE id_acao = 56;
UPDATE tramitacao.acao SET tx_descricao = 'Resolver notificação geo' WHERE id_acao = 57;
UPDATE tramitacao.acao SET tx_descricao = 'Invalidar parecer geo encaminhando para outro geo' WHERE id_acao = 58;
UPDATE tramitacao.acao SET tx_descricao = 'Solicitar desvínculo da análise geo' WHERE id_acao = 59;
UPDATE tramitacao.acao SET tx_descricao = 'Iniciar análise geo do coordenador' WHERE id_acao = 62;
UPDATE tramitacao.acao SET tx_descricao = 'Validar parecer geo pelo coordenador' WHERE id_acao = 63;
UPDATE tramitacao.acao SET tx_descricao = 'Deferir análise geo com comunicado' WHERE id_acao = 65;
UPDATE tramitacao.acao SET tx_descricao = 'Iniciar análise técnica do coordenador' WHERE id_acao = 67;
UPDATE tramitacao.acao SET tx_descricao = 'Validar parecer técnico pelo coordenador' WHERE id_acao = 68;
UPDATE tramitacao.acao SET tx_descricao = 'Iniciar análise do secretário' WHERE id_acao = 76;
UPDATE tramitacao.acao SET tx_descricao = 'Finalizar análise jurídica' WHERE id_acao = 77;

UPDATE tramitacao.condicao SET nm_condicao = 'Notificado pelo analista geo' WHERE id_condicao = 4;
UPDATE tramitacao.condicao SET nm_condicao = 'Aguardando vinculação técnica pelo coordenador' WHERE id_condicao = 7;
UPDATE tramitacao.condicao SET nm_condicao = 'Aguardando validação técnica pelo coordenador' WHERE id_condicao = 10;
UPDATE tramitacao.condicao SET nm_condicao = 'Aguardando assinatura do secretário' WHERE id_condicao = 11;
UPDATE tramitacao.condicao SET nm_condicao = 'Aguardando vinculação geo pelo coordenador' WHERE id_condicao = 24;
UPDATE tramitacao.condicao SET nm_condicao = 'Aguardando análise geo' WHERE id_condicao = 25;
UPDATE tramitacao.condicao SET nm_condicao = 'Em análise geo' WHERE id_condicao = 26;
UPDATE tramitacao.condicao SET nm_condicao = 'Aguardando validação geo do coordenador' WHERE id_condicao = 27;
UPDATE tramitacao.condicao SET nm_condicao = 'Aguardando validação pelo coordenador' WHERE id_condicao = 28;
UPDATE tramitacao.condicao SET nm_condicao = 'Solicitação de desvínculo pendente da análise geo' WHERE id_condicao = 30;
UPDATE tramitacao.condicao SET nm_condicao = 'Em análise geo pelo coordenador' WHERE id_condicao = 31;
UPDATE tramitacao.condicao SET nm_condicao = 'Em análise técnica pelo coordenador' WHERE id_condicao = 36;
UPDATE tramitacao.condicao SET nm_condicao = 'Em análise pelo secretário' WHERE id_condicao = 38;
UPDATE tramitacao.condicao SET nm_condicao = 'Aguardando análise jurídica' WHERE id_condicao = 39;


# --- !Downs

UPDATE tramitacao.condicao SET nm_condicao = 'Notificado pelo Analista GEO' WHERE id_condicao = 4;
UPDATE tramitacao.condicao SET nm_condicao = 'Aguardando vinculação técnica pelo gerente técnico' WHERE id_condicao = 7;
UPDATE tramitacao.condicao SET nm_condicao = 'Aguardando validação técnica pelo gerente técnico' WHERE id_condicao = 10;
UPDATE tramitacao.condicao SET nm_condicao = 'Aguardando assinatura do presidente' WHERE id_condicao = 11;
UPDATE tramitacao.condicao SET nm_condicao = 'Aguardando vinculação GEO pelo gerente técnico' WHERE id_condicao = 24;
UPDATE tramitacao.condicao SET nm_condicao = 'Aguardando análise GEO' WHERE id_condicao = 25;
UPDATE tramitacao.condicao SET nm_condicao = 'Em análise GEO' WHERE id_condicao = 26;
UPDATE tramitacao.condicao SET nm_condicao = 'Aguardando validação GEO pelo gerente técnico' WHERE id_condicao = 27;
UPDATE tramitacao.condicao SET nm_condicao = 'Aguardando validação gerente técnico' WHERE id_condicao = 28;
UPDATE tramitacao.condicao SET nm_condicao = 'Solicitação de desvínculo pendente análise GEO' WHERE id_condicao = 30;
UPDATE tramitacao.condicao SET nm_condicao = 'Em análise pelo gerente técnico' WHERE id_condicao = 31;
UPDATE tramitacao.condicao SET nm_condicao = 'Em análise técnica pelo gerente técnico' WHERE id_condicao = 36;
UPDATE tramitacao.condicao SET nm_condicao = 'Em análise pelo presidente' WHERE id_condicao = 38;
UPDATE tramitacao.condicao SET nm_condicao = 'Aguardando resposta jurídica' WHERE id_condicao = 39;

UPDATE tramitacao.acao SET tx_descricao = 'Notificar pelo Analista GEO' WHERE id_acao = 3;
UPDATE tramitacao.acao SET tx_descricao = 'Invalidar parecer técnico pelo gerente técnico' WHERE id_acao = 14;
UPDATE tramitacao.acao SET tx_descricao = 'Solicitar ajustes parecer técnico pelo gerente técnico' WHERE id_acao = 15;
UPDATE tramitacao.acao SET tx_descricao = 'Validar deferimento técnico pelo gerente técnico' WHERE id_acao = 16;
UPDATE tramitacao.acao SET tx_descricao = 'Validar indeferimento técnico pelo gerente técnico' WHERE id_acao = 17;
UPDATE tramitacao.acao SET tx_descricao = 'Vincular gerente técnico' WHERE id_acao = 19;
UPDATE tramitacao.acao SET tx_descricao = 'Deferir análise GEO via gerente técnico' WHERE id_acao = 49;
UPDATE tramitacao.acao SET tx_descricao = 'Indeferir análise GEO via gerente técnico' WHERE id_acao = 50;
UPDATE tramitacao.acao SET tx_descricao = 'Iniciar análise GEO' WHERE id_acao = 51;
UPDATE tramitacao.acao SET tx_descricao = 'Invalidar parecer GEO pelo gerente técnico' WHERE id_acao = 52;
UPDATE tramitacao.acao SET tx_descricao = 'Validar deferimento GEO pelo gerente técnico' WHERE id_acao = 53;
UPDATE tramitacao.acao SET tx_descricao = 'Validar indeferimento GEO pelo gerente técnico' WHERE id_acao = 54;
UPDATE tramitacao.acao SET tx_descricao = 'Solicitar ajustes parecer GEO pelo gerente técnico' WHERE id_acao = 55;
UPDATE tramitacao.acao SET tx_descricao = 'Solicitar ajuste da análise GEO pelo presidente' WHERE id_acao = 56;
UPDATE tramitacao.acao SET tx_descricao = 'Resolver notificação GEO' WHERE id_acao = 57;
UPDATE tramitacao.acao SET tx_descricao = 'Invalidar parecer GEO encaminhando para outro GEO' WHERE id_acao = 58;
UPDATE tramitacao.acao SET tx_descricao = 'Solicitar desvínculo análise Geo' WHERE id_acao = 59;
UPDATE tramitacao.acao SET tx_descricao = 'Iniciar análise gerente técnico' WHERE id_acao = 62;
UPDATE tramitacao.acao SET tx_descricao = 'Validar parecer geo pelo gerente técnico' WHERE id_acao = 63;
UPDATE tramitacao.acao SET tx_descricao = 'Deferir análise GEO com comunicado' WHERE id_acao = 65;
UPDATE tramitacao.acao SET tx_descricao = 'Iniciar análise técnica gerente técnico' WHERE id_acao = 67;
UPDATE tramitacao.acao SET tx_descricao = 'Validar parecer técnico gerente técnico' WHERE id_acao = 68;
UPDATE tramitacao.acao SET tx_descricao = 'Iniciar análise do presidente' WHERE id_acao = 76;
UPDATE tramitacao.acao SET tx_descricao = 'Resolver análise jurídica' WHERE id_acao = 77;
