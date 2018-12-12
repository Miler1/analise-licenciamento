# --- !Ups

-- Alterar condições do manejo digital

UPDATE tramitacao.condicao SET nm_condicao = 'Manejo digital apto' WHERE id_condicao = 19;
UPDATE tramitacao.condicao SET nm_condicao = 'Manejo digital inapto' WHERE id_condicao = 20;

# --- !Downs

-- Desfazer alterações em condições do manejo digital

UPDATE tramitacao.condicao SET nm_condicao = 'Manejo digital indeferido' WHERE id_condicao = 20;
UPDATE tramitacao.condicao SET nm_condicao = 'Manejo digital deferido' WHERE id_condicao = 19;