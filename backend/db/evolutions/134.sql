# --- !Ups

ALTER TABLE analise.processo ADD COLUMN retificacao BOOLEAN DEFAULT FALSE NOT NULL;
COMMENT ON COLUMN analise.processo.retificacao IS 'Flag que indica se um processo é proveniente de retificação de uma caracterização';

ALTER TABLE analise.processo ADD COLUMN ativo BOOLEAN DEFAULT TRUE NOT NULL;
COMMENT ON COLUMN analise.processo.ativo IS 'Flag que indica se um processo está ativo ou inativo';

ALTER TABLE analise.processo
    ADD COLUMN id_origem_notificacao INTEGER,
    ADD CONSTRAINT fk_p_origem_notificacao FOREIGN KEY (id_origem_notificacao)
        REFERENCES analise.origem_notificacao (id);
COMMENT ON COLUMN analise.processo.id_origem_notificacao IS 'Referência para a entidade que guarda a origem de notificação do processo';

ALTER TABLE analise.notificacao ADD COLUMN data_conclusao TIMESTAMP WITH TIME ZONE;
COMMENT ON COLUMN analise.notificacao.data_conclusao IS 'Data em que a notificação foi concluída';

CREATE TABLE analise.origem_notificacao
(
    id        INTEGER      NOT NULL,
    codigo    VARCHAR(150) NOT NULL,
    descricao TEXT         NOT NULL,

    CONSTRAINT pk_origem_notificacao PRIMARY KEY (id),
    CONSTRAINT uq_codigo UNIQUE (codigo)
);

COMMENT ON TABLE analise.origem_notificacao IS 'Entidade responsável pela referência para a origem da notificação do processo';
COMMENT ON COLUMN analise.origem_notificacao.id IS 'Chave primária da entidade';
COMMENT ON COLUMN analise.origem_notificacao.codigo IS 'Código da origem da notificação';
COMMENT ON COLUMN analise.origem_notificacao.descricao IS 'Descrição da origem da notificação';

INSERT INTO analise.origem_notificacao (id, codigo, descricao) VALUES
(1, 'ANALISE_GEO', 'Notificação gerada pelo analista GEO'),
(2, 'ANALISE_TECNICA', 'Notificação gerada pelo analista técnico');


UPDATE analise.processo SET id_origem_notificacao = result.id_origem_notificacao
FROM (
    SELECT p.id id_processo,
       CASE
           WHEN n.id_analise_geo IS NOT NULL THEN 1
           WHEN n.id_analise_tecnica IS NOT NULL THEN 2
       END id_origem_notificacao FROM analise.processo p
    INNER JOIN analise.processo pa ON p.id_processo_anterior = pa.id
    INNER JOIN analise.analise a ON pa.id = a.id_processo
    LEFT JOIN analise.analise_tecnica at ON a.id = at.id_analise
    LEFT JOIN analise.analise_geo ag ON a.id = ag.id_analise
    INNER JOIN analise.notificacao n ON ag.id = n.id_analise_geo OR at.id = id_analise_tecnica
 ) AS result
WHERE processo.id = result.id_processo;



# --- !Downs

ALTER TABLE analise.processo DROP CONSTRAINT fk_p_origem_notificacao;

ALTER TABLE analise.processo DROP COLUMN id_origem_notificacao;

DROP TABLE analise.origem_notificacao;

ALTER TABLE analise.notificacao DROP COLUMN data_conclusao ;

ALTER TABLE analise.processo DROP COLUMN ativo, DROP COLUMN retificacao;
