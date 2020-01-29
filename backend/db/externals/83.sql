# --- !Ups

INSERT INTO licenciamento.rel_tipo_sobreposicao_orgao(id_tipo_sobreposicao, id_orgao) VALUES (19, 2);


# --- !Downs

DELETE FROM licenciamento.rel_tipo_sobreposicao_orgao WHERE id_tipo_sobreposicao = 19 AND  id_orgao = 2;
