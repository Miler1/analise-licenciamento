# --- !Ups

UPDATE tramitacao.acao SET tx_descricao = 'Deferir análise técnica via gerente técnico' WHERE id_acao = 12;
UPDATE tramitacao.acao SET tx_descricao = 'Indeferir análise técnica via gerente técnico' WHERE id_acao = 13;
UPDATE tramitacao.acao SET tx_descricao = 'Solicitar ajustes parecer técnico pelo gerente técnico' WHERE id_acao = 15;
UPDATE tramitacao.acao SET tx_descricao = 'Validar parecer geo pelo gerente técnico' WHERE id_acao = 63;
UPDATE tramitacao.acao SET tx_descricao = 'Solicitar ajustes parecer GEO pelo gerente técnico' WHERE id_acao = 55;
UPDATE tramitacao.acao SET tx_descricao = 'Validar deferimento técnico pelo gerente técnico' WHERE id_acao = 16;
UPDATE tramitacao.acao SET tx_descricao = 'Invalidar parecer técnico pelo gerente técnico' WHERE id_acao = 14;
UPDATE tramitacao.acao SET tx_descricao = 'Validar indeferimento técnico pelo gerente técnico' WHERE id_acao = 17;
UPDATE tramitacao.acao SET tx_descricao = 'Vincular gerente técnico' WHERE id_acao = 19;
UPDATE tramitacao.acao SET tx_descricao = 'Deferir análise GEO via gerente técnico' WHERE id_acao = 49;
UPDATE tramitacao.acao SET tx_descricao = 'Indeferir análise GEO via gerente técnico' WHERE id_acao = 50;
UPDATE tramitacao.acao SET tx_descricao = 'Invalidar parecer GEO pelo gerente técnico' WHERE id_acao = 52;
UPDATE tramitacao.acao SET tx_descricao = 'Validar deferimento GEO pelo gerente técnico' WHERE id_acao = 53;
UPDATE tramitacao.acao SET tx_descricao = 'Validar indeferimento GEO pelo gerente técnico' WHERE id_acao = 54;
UPDATE tramitacao.acao SET tx_descricao = 'Iniciar análise gerente técnico' WHERE id_acao = 62;
UPDATE tramitacao.acao SET tx_descricao = 'Validar parecer técnico gerente técnico' WHERE id_acao = 68;
UPDATE tramitacao.acao SET tx_descricao = 'Iniciar análise técnica gerente técnico' WHERE id_acao = 67;
UPDATE tramitacao.acao SET tx_descricao = 'Iniciar análise pelo diretor técnico' WHERE id_acao = 72;
UPDATE tramitacao.acao SET tx_descricao = 'Validar análise pelo diretor técnico' WHERE id_acao = 74;
UPDATE tramitacao.acao SET tx_descricao = 'Invalidar análise pelo diretor técnico' WHERE id_acao = 75;


UPDATE tramitacao.condicao SET nm_condicao = 'Aguardando vinculação técnica pelo gerente técnico' WHERE id_condicao = 7;
UPDATE tramitacao.condicao SET nm_condicao = 'Aguardando validação técnica pelo gerente técnico' WHERE id_condicao = 10;
UPDATE tramitacao.condicao SET nm_condicao = 'Aguardando validação GEO pelo gerente técnico' WHERE id_condicao = 27;
UPDATE tramitacao.condicao SET nm_condicao = 'Aguardando vinculação GEO pelo gerente técnico' WHERE id_condicao = 24;
UPDATE tramitacao.condicao SET nm_condicao = 'Aguardando validação gerente técnico' WHERE id_condicao = 28;
UPDATE tramitacao.condicao SET nm_condicao = 'Em análise pelo gerente técnico' WHERE id_condicao = 31;
UPDATE tramitacao.condicao SET nm_condicao = 'Em análise técnica pelo gerente técnico' WHERE id_condicao = 36;
UPDATE tramitacao.condicao SET nm_condicao = 'Em análise pelo diretor técnico' WHERE id_condicao = 37;


# --- !Downs

UPDATE tramitacao.condicao SET nm_condicao = 'Aguardando validação técnica pelo gerente' WHERE id_condicao = 10;
UPDATE tramitacao.condicao SET nm_condicao = 'Em análise pelo diretor' WHERE id_condicao = 37;
UPDATE tramitacao.condicao SET nm_condicao = 'Em análise técnica pelo gerente' WHERE id_condicao = 36;
UPDATE tramitacao.condicao SET nm_condicao = 'Em análise pelo gerente' WHERE id_condicao = 31;
UPDATE tramitacao.condicao SET nm_condicao = 'Aguardando validação gerente' WHERE id_condicao = 28;
UPDATE tramitacao.condicao SET nm_condicao = 'Aguardando vinculação GEO pelo gerente' WHERE id_condicao = 24;
UPDATE tramitacao.condicao SET nm_condicao = 'Aguardando validação GEO pelo gerente' WHERE id_condicao = 27;
UPDATE tramitacao.condicao SET nm_condicao = 'Aguardando validação técnica pelo gerente' WHERE id_condicao = 10;
UPDATE tramitacao.condicao SET nm_condicao = 'Aguardando validação técnica pelo gerente' WHERE id_condicao = 10;

UPDATE tramitacao.acao SET tx_descricao = 'Invalidar análise pelo diretor' WHERE id_acao = 75;
UPDATE tramitacao.acao SET tx_descricao = 'Validar análise pelo diretor' WHERE id_acao = 74;
UPDATE tramitacao.acao SET tx_descricao = 'Iniciar análise pelo diretor' WHERE id_acao = 72;
UPDATE tramitacao.acao SET tx_descricao = 'Iniciar análise técnica gerente' WHERE id_acao = 67;
UPDATE tramitacao.acao SET tx_descricao = 'Validar parecer técnico gerente' WHERE id_acao = 68;
UPDATE tramitacao.acao SET tx_descricao = 'Iniciar análise gerente' WHERE id_acao = 62;
UPDATE tramitacao.acao SET tx_descricao = 'Validar indeferimento GEO pelo gerente' WHERE id_acao = 54;
UPDATE tramitacao.acao SET tx_descricao = 'Validar deferimento GEO pelo gerente' WHERE id_acao = 53;
UPDATE tramitacao.acao SET tx_descricao = 'Invalidar parecer GEO pelo gerente' WHERE id_acao = 52;
UPDATE tramitacao.acao SET tx_descricao = 'Indeferir análise GEO via gerente' WHERE id_acao = 50;
UPDATE tramitacao.acao SET tx_descricao = 'Deferir análise GEO via gerente' WHERE id_acao = 49;
UPDATE tramitacao.acao SET tx_descricao = 'Vincular gerente ' WHERE id_acao = 19;
UPDATE tramitacao.acao SET tx_descricao = 'Validar indeferimento técnico pelo gerente ' WHERE id_acao = 17;
UPDATE tramitacao.acao SET tx_descricao = 'Invalidar parecer técnico pelo gerente ' WHERE id_acao = 14;
UPDATE tramitacao.acao SET tx_descricao = 'Validar deferimento técnico pelo gerente ' WHERE id_acao = 16;
UPDATE tramitacao.acao SET tx_descricao = 'Solicitar ajustes parecer GEO pelo gerente ' WHERE id_acao = 55;
UPDATE tramitacao.acao SET tx_descricao = 'Validar parecer geo pelo gerente' WHERE id_acao = 63;
UPDATE tramitacao.acao SET tx_descricao = 'Solicitar ajustes parecer técnico pelo gerente' WHERE id_acao = 15;
UPDATE tramitacao.acao SET tx_descricao = 'Indeferir análise técnica via gerente' WHERE id_acao = 13;
UPDATE tramitacao.acao SET tx_descricao = 'Deferir análise técnica via gerente' WHERE id_acao = 12;
