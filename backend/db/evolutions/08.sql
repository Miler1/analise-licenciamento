# --- !Ups

INSERT INTO analise.tipo_resultado_analise(id,nome) VALUES
(1,'Deferido'),
(2,'Indeferido'),
(3,'Emitir notificação');

# --- !Downs

DELETE FROM analise.tipo_resultado_analise WHERE id IN (1,2,3);