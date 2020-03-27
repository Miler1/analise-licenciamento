# --- !Ups

CREATE SCHEMA tramitacao;

COMMENT ON SCHEMA tramitacao IS 'Este esquema é utilizado para armazenar as tabelas referentes ao objeto tramitável, que deverá ser tramitado da sua origem até seu destino, dentro dos variados fluxos dos sistemas. Através desse objeto tramitável, deverá ser possível identificar onde o processo/documento se encontra no fluxo que está percorrendo, sua condição, etapa, situações e qual o seu usuário (CAR) responsável naquele momento.';

SET search_path = tramitacao, pg_catalog;

CREATE TABLE acao (
    id_acao integer NOT NULL,
    tx_descricao character varying(1000) NOT NULL,
    fl_ativo numeric(1,0) NOT NULL,
    fl_tramitavel numeric(1,0)
);

COMMENT ON TABLE acao IS 'Entidade utilizada para representar uma ação que pode ser executada sobre um objeto tramitavel. Toda ação deve estar presente em um fluxo, e, para isto, deve pertencer a uma de suas transições. A execução de uma ação sobre um objeto tramitavel em um fluxo resulta na execução de uma transição, o que pode levar o objeto a uma ou outra condição, fazendo o objeto caminhar pelo fluxo.';

COMMENT ON COLUMN acao.id_acao IS 'Identificador único da entidade acao.';

COMMENT ON COLUMN acao.tx_descricao IS 'Descrição da ação.';

COMMENT ON COLUMN acao.fl_ativo IS 'Flag para exclusão lógica da ação. 0 - Inativo  1 - Ativo';

COMMENT ON COLUMN acao.fl_tramitavel IS 'Flag que representa se a ação efetua ou não um tramite do objeto entre usuários, ou seja, se o objeto terá seu responsável alterado. ';

CREATE SEQUENCE acao_id_acao_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE acao_id_acao_seq OWNED BY acao.id_acao;

CREATE TABLE condicao (
    id_condicao integer NOT NULL,
    id_etapa integer,
    nm_condicao character varying(100) NOT NULL,
    fl_ativo numeric(1,0) NOT NULL
);

COMMENT ON TABLE condicao IS 'Esta entidade é utilizada para identificar as posições em um fluxo que podem ser ocupadas pelos objetos tramitáveis. A condição de um objeto tramitável, juntamente com as transições relacionadas a ele em um fluxo, determina quais os caminhos que podem ser percorridos pelo objeto. Uma condição é exclusiva de um fluxo.';

COMMENT ON COLUMN condicao.id_condicao IS 'Identificador único da entidade status.';

COMMENT ON COLUMN condicao.id_etapa IS 'Identificador da entidade etapa que realizará o relacionamento entre as entidades status e etapa. Identifica a etapa do fluxo a qual o status pertence, por exemplo, Etapa Balcão – Aguardando Formalização. ';

COMMENT ON COLUMN condicao.nm_condicao IS 'Nome da condição.';

COMMENT ON COLUMN condicao.fl_ativo IS 'Flag para exclusão lógica da condição. (0- Inativo; 1- ativo).';

CREATE SEQUENCE condicao_id_condicao_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE condicao_id_condicao_seq OWNED BY condicao.id_condicao;

CREATE TABLE config_situacao (
    id_config_situacao integer NOT NULL,
    id_situacao integer,
    id_transicao integer,
    fl_adicionar numeric(1,0),
    fl_pai numeric(1,0)
);

COMMENT ON TABLE config_situacao IS 'Esta entidade é utilizada para indicar uma alteração nas configurações de situações do objeto tramitável ao passar por uma transição. Uma configuração de situação indica que uma situação será adicionada ou removida do objeto que atravessar a transição. Esta configuração pode indicar uma alteração do objeto pai. ';

COMMENT ON COLUMN config_situacao.id_config_situacao IS 'Identificador único da entidade config_situacao.';

COMMENT ON COLUMN config_situacao.id_situacao IS 'Identificador da entidade situacao que realizará o relacionamento entre as entidades config_situacao e situacao. Identifica a situação que será adicionada ou removida do objeto. ';

COMMENT ON COLUMN config_situacao.id_transicao IS 'Identificador da entidade transicao que realizará o relacionamento entre as entidades transicao e situacao. Identifica a transição na qual, ao ser atravessada, o objeto terá a situação adicionada ou removida. ';

COMMENT ON COLUMN config_situacao.fl_adicionar IS 'Indica se a situação será adicionada ou removida                        (0 - Remover 1 - Adicionar).';

COMMENT ON COLUMN config_situacao.fl_pai IS 'Indica se a situação será adicionada/removida no próprio objeto tramitável ou em seu objeto pai (0 - Altera no próprio objeto  1 - Altera objeto pai).';

CREATE SEQUENCE config_situacao_id_config_situacao_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE config_situacao_id_config_situacao_seq OWNED BY config_situacao.id_config_situacao;

CREATE TABLE etapa (
    id_etapa integer NOT NULL,
    id_fluxo integer,
    tx_etapa character varying(2000),
    dt_prazo integer
);

COMMENT ON TABLE etapa IS 'Esta entidade é utilizada para identificar as etapas pertencentes a um determinado fluxo, sendo a etapa, um subconjunto do fluxo. Exemplo: Balcão, Digitalização, etc. As etapas indicam de forma ampla onde o objeto tramitável se encontra no fluxo. Para um determinado fluxo, uma etapa é composta por várias condições. ';

COMMENT ON COLUMN etapa.id_etapa IS 'Identificador único da entidade etapa.';

COMMENT ON COLUMN etapa.id_fluxo IS 'Identificador da entidade fluxo que realizará o relacionamento entre as entidades fluxo e a entidade etapa. Identifica a qual fluxo a etapa pertence.';

COMMENT ON COLUMN etapa.tx_etapa IS 'Descrição da etapa.';

COMMENT ON COLUMN etapa.dt_prazo IS 'Data estipulada para que a etapa seja finalizada (em dias).';

CREATE SEQUENCE etapa_id_etapa_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE etapa_id_etapa_seq OWNED BY etapa.id_etapa;

CREATE TABLE evento (
    id_evento integer NOT NULL,
    tx_descricao character varying(100) NOT NULL
);

COMMENT ON TABLE evento IS 'Entidade utilizada para representar os eventos que podem ocorrer após a execução da ação de uma determinada transição.';

COMMENT ON COLUMN evento.id_evento IS 'Identificador único da entidade evento.';

COMMENT ON COLUMN evento.tx_descricao IS 'Descrição do evento.';

CREATE SEQUENCE evento_id_evento_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE evento_id_evento_seq OWNED BY evento.id_evento;

CREATE TABLE fluxo (
    id_fluxo integer NOT NULL,
    id_condicao_inicial integer,
    tx_descricao character varying(1000),
    dt_prazo integer
);

COMMENT ON TABLE fluxo IS 'Esta entidade é utilizada para identificar os fluxos existentes que podem ser percorridos por um objeto tramitável. Um fluxo é composto de vários status, que são agrupados por etapas. Estes status são conectados através de transições. Desta forma os status representam as possíveis posições no fluxo onde um objeto pode estar e as transições representam os possíveis caminhos para os quais o objeto pode ir a partir de um determinado status. O fluxo possui um status inicial que representa seu ponto de partida.';

COMMENT ON COLUMN fluxo.id_fluxo IS 'Identificador único da entidade fluxo.';

COMMENT ON COLUMN fluxo.id_condicao_inicial IS 'Identificador da entidade condicao que realizará o relacionamento entre as entidades condicao e fluxo. Identifica qual a condição inicial do fluxo, ou seja, a primeira condição a ser atribuída ao objeto tramitável ao iniciar o fluxo.';

COMMENT ON COLUMN fluxo.tx_descricao IS 'Descrição do fluxo para sua identificação.';

COMMENT ON COLUMN fluxo.dt_prazo IS 'Data estipulada para que o fluxo seja concluído (em dias).';

CREATE SEQUENCE fluxo_id_fluxo_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE fluxo_id_fluxo_seq OWNED BY fluxo.id_fluxo;

CREATE TABLE historico_objeto_tramitavel (
    id_historico_objeto_tramitavel integer NOT NULL,
    id_objeto_tramitavel integer NOT NULL,
    id_acao integer NOT NULL,
    id_condicao_inicial integer NOT NULL,
    id_condicao_final integer NOT NULL,
    id_usuario integer,
    id_usuario_destino integer,
    dt_cadastro timestamp without time zone,
    tx_observacao character varying(1000),
    tx_acao character varying(2000) DEFAULT '-'::character varying NOT NULL,
    tx_condicao_inicial character varying(2000) DEFAULT '-'::character varying NOT NULL,
    tx_condicao_final character varying(2000) DEFAULT '-'::character varying NOT NULL,
    tx_usuario character varying(2000),
    tx_usuario_destino character varying(2000),
    id_etapa_inicial integer,
    tx_etapa_inicial character varying(2000),
    id_etapa_final integer,
    tx_etapa_final character varying(2000)
);

COMMENT ON TABLE historico_objeto_tramitavel IS 'Entidade responsável por armazenar o histórico de todas as transições do objeto tramitável. Desta forma, sempre que uma transição for atravessada por um objeto tramitável, algumas informações serão armazenadas.';

COMMENT ON COLUMN historico_objeto_tramitavel.id_historico_objeto_tramitavel IS 'Identificador único da Tabela.';

COMMENT ON COLUMN historico_objeto_tramitavel.id_objeto_tramitavel IS 'Identificador da entidade objeto_tramitavel que realizará o relacionamento entre as entidades objeto_tramitavel e histórico_objeto_tramitavel. ';

COMMENT ON COLUMN historico_objeto_tramitavel.id_acao IS 'Identificador da entidade acao que realizará o relacionamento entre as entidades acao e histórico_objeto_tramitavel.';

COMMENT ON COLUMN historico_objeto_tramitavel.id_condicao_inicial IS 'Identificador da entidade condicao que realizará o relacionamento entre as entidades condicao e histórico_objeto_tramitavel. Este campo indica a condição inicial do objeto tramitável.';

COMMENT ON COLUMN historico_objeto_tramitavel.id_condicao_final IS 'Identificador da entidade condicao que realizará o relacionamento entre as entidades condicao e histórico_objeto_tramitavel. Este campo indica a condição final do objeto tramitável.';

COMMENT ON COLUMN historico_objeto_tramitavel.id_usuario IS 'Identificador da entidade usr_geocar_aplicacao.usuario que realizará o relacionamento entre as entidades usuario e histórico_objeto_tramitavel. Identifica qual usuário executou a ação da tramitação.';

COMMENT ON COLUMN historico_objeto_tramitavel.id_usuario_destino IS 'Identificador da entidade usr_geocar_aplicacao.usuario que realizará o relacionamento entre as entidades usuario e histórico_objeto_tramitavel. Identifica qual será o usuário responsável pelo objeto tramitável após a execução da ação da tramitação. Caso a ação não resulte na alteração do responsável pelo objeto tramitável, este campo deverá ser preenchido com o mesmo identificador do responsável atual.';

COMMENT ON COLUMN historico_objeto_tramitavel.dt_cadastro IS 'Data em que o objeto tramitável sofreu a tramitação.';

COMMENT ON COLUMN historico_objeto_tramitavel.tx_observacao IS 'Campo utilizado para descrever o motivo ou justificativa de uma tramitação.';

COMMENT ON COLUMN historico_objeto_tramitavel.tx_acao IS 'Descrição da ação executada.';

COMMENT ON COLUMN historico_objeto_tramitavel.tx_condicao_inicial IS 'Nome da condição inicial.';

COMMENT ON COLUMN historico_objeto_tramitavel.tx_condicao_final IS 'Nome da condição final.';

COMMENT ON COLUMN historico_objeto_tramitavel.tx_usuario IS 'Descrição do usuário que executou a ação da tramitação.';

COMMENT ON COLUMN historico_objeto_tramitavel.tx_usuario_destino IS 'Descrição do usuário responsável pelo objeto tramitável após a execução da ação de tramitação.';

COMMENT ON COLUMN historico_objeto_tramitavel.id_etapa_inicial IS 'Identificador único da entidade etapa que realiza o relacionamento entre as entidades etapa e histórico_objeto_tramitavel. Identifica a etapa inicial.';

COMMENT ON COLUMN historico_objeto_tramitavel.tx_etapa_inicial IS 'Descrição da etapa inicial.';

COMMENT ON COLUMN historico_objeto_tramitavel.id_etapa_final IS 'Identificador único da entidade etapa que realiza o relacionamento entre as entidades etapa e histórico_objeto_tramitavel. Identifica a etapa final.';

COMMENT ON COLUMN historico_objeto_tramitavel.tx_etapa_final IS 'Descrição da etapa final.';

CREATE SEQUENCE historico_objeto_tramitavel_id_historico_objeto_tramitavel_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE historico_objeto_tramitavel_id_historico_objeto_tramitavel_seq OWNED BY historico_objeto_tramitavel.id_historico_objeto_tramitavel;

CREATE TABLE impedimento_transicao (
    id_impedimento_transicao integer NOT NULL,
    id_situacao integer NOT NULL,
    id_transicao integer NOT NULL,
    tp_impedimento numeric(1,0)
);

COMMENT ON TABLE impedimento_transicao IS 'Esta entidade é utilizada para armazenar as informações de impedimento, para uma ação dentro do fluxo de acordo com a situação atual do objeto tramitável. Ou seja, os impedimentos são quais situações que impedem que determinada transição no fluxo ocorra. ';

COMMENT ON COLUMN impedimento_transicao.id_impedimento_transicao IS 'Identificador único da entidade impedimento_transicao.';

COMMENT ON COLUMN impedimento_transicao.id_situacao IS 'Idenficador da tabela situação que realizará o relacionamento entre as tabelas impedimento da transação e a tabela situação. Obs.: este campo serve para identificar em quais situações o objeto tramitavel não pode realizar a ação especifica na transição';

COMMENT ON COLUMN impedimento_transicao.id_transicao IS 'Idenficador da tabela transação que realizará o relacionamento entre as tabelas impedimento da transação e a tabela transação. Obs.: este campo serve para identificar qual ação devera ser impedida de ser executada por um eventual impedimento do objeto tramitavel';

COMMENT ON COLUMN impedimento_transicao.tp_impedimento IS 'Indica o tipo de impedimento: 0 - Situação restritiva   1 - Situação Obrigatória.';

CREATE SEQUENCE impedimento_transicao_id_impedimento_transicao_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE impedimento_transicao_id_impedimento_transicao_seq OWNED BY impedimento_transicao.id_impedimento_transicao;

CREATE TABLE objeto_tramitavel (
    id_objeto_tramitavel integer NOT NULL,
    id_condicao integer NOT NULL,
    id_etapa integer,
    id_fluxo integer,
    id_tipo_objeto_tramitavel integer,
    dt_criacao_objeto_tramitavel timestamp without time zone NOT NULL,
    id_usuario integer,
    id_pai integer,
    id_responsavel_anterior integer,
    id_condicao_fluxo_anterior integer,
    id_responsavel_fluxo_anterior integer
);

COMMENT ON TABLE objeto_tramitavel IS 'Esta entidade é utilizada para identificar o processo, documento, ou outra entidade, que deverá ser tramitado da sua origem até seu destino, dentro dos variados fluxos dos sistemas. Através desse objeto tramitável, deverá ser possível identificar onde o processo/documento se encontra no fluxo que está percorrendo, sua condição, etapa, situações e qual o seu usuário (CAR) responsável naquele momento. Um objeto tramitável pode estar relacionado a outro objeto tramitável pai, podendo um interferir no outro através de situações. ';

COMMENT ON COLUMN objeto_tramitavel.id_objeto_tramitavel IS 'Identificador único da entidade objeto_tramitavel.';

COMMENT ON COLUMN objeto_tramitavel.id_condicao IS 'Identificador da entidade condicao que realizará o relacionamento entre as entidades objeto_tramitavel e condicao. Identifica onde o objeto se encontra no fluxo que está percorrendo. A condição implica nas possíveis ações a serem executadas sobre o objeto. ';

COMMENT ON COLUMN objeto_tramitavel.id_etapa IS 'Identificador da entidade etapa que realizará o relacionamento entre as entidades objeto_tramitavel e etapa. Este campo tem o objetivo de indicar a etapa atual do objeto tramitável que, assim como a condição, identifica a posição do objeto no fluxo, porém, de forma mais ampla que a condição (pois uma condição está contida em uma etapa).';

COMMENT ON COLUMN objeto_tramitavel.id_fluxo IS 'Identificador da entidade fluxo que realizará o relacionamento entre as entidades objeto tramitável e fluxo. Este campo tem o objetivo de determinar qual fluxo o objeto tramitável esta percorrendo. Este fluxo está sempre relacionado à condição e etapa do objeto tramitável, pois a condição e a etapa devem sempre pertencer a este fluxo.';

COMMENT ON COLUMN objeto_tramitavel.id_tipo_objeto_tramitavel IS 'Identificador da entidade tipo_objeto_tramitavel que realizará o relacionamento entre as entidades tipo_objeto_tramitavel e objeto_tramitavel. Este campo tem o objetivo de mostrar se o objeto tramitavel é do tipo processo ou documento, entre outros.';

COMMENT ON COLUMN objeto_tramitavel.dt_criacao_objeto_tramitavel IS 'Data de criação do objeto tramitável, ou seja, a data em que o objeto iniciou percorrimento de seu fluxo. ';

COMMENT ON COLUMN objeto_tramitavel.id_usuario IS 'Identificador da entidade usuario que realizará o relacionamento entre as entidades objeto_tramitavel e usuario. Este campo tem o objetivo de armazenar o usuário que esta de posse do objeto tramitável. Isto é, identifica quem é o responsável atual pelo objeto tramitável.';

COMMENT ON COLUMN objeto_tramitavel.id_pai IS 'Identifica o objeto tramitável pai. Este objeto tramitável pai pode percorrer outro fluxo totalmente diferente daquele percorrido por este objeto, porém, os objetos estão relacionados e podem interferir um no outro através de situações. ';

COMMENT ON COLUMN objeto_tramitavel.id_responsavel_anterior IS 'Identificador da entidade usuario que realizará o relacionamento entre as entidades objeto_tramitavel e usuario. Utilizado em casos de recusa, para retornar para o mesmo usuário.';

COMMENT ON COLUMN objeto_tramitavel.id_condicao_fluxo_anterior IS 'Identificador da entidade condicacao que realizará o relacionamento entre as entidades objeto_tramitavel e condicao. Utilizado para retornar ao fluxo antertior na mesma condicao em que se encontrava.';

COMMENT ON COLUMN objeto_tramitavel.id_responsavel_fluxo_anterior IS 'Responsável pelo objeto tramitável do fluxo anterior.';

CREATE SEQUENCE objeto_tramitavel_id_objeto_tramitavel_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE objeto_tramitavel_id_objeto_tramitavel_seq OWNED BY objeto_tramitavel.id_objeto_tramitavel;

CREATE TABLE rel_evento_transicao (
    id_rel_evento_transicao integer NOT NULL,
    id_transicao integer,
    id_evento integer
);

COMMENT ON TABLE rel_evento_transicao IS 'Esta entidade é utilizada para identificar todos os possíveis relacionamentos do evento com as transições. Obs.: Uma transição poderá ter varias eventos e um evento por sua vez, poderá ter varias transições associados a ele.  

';

COMMENT ON COLUMN rel_evento_transicao.id_rel_evento_transicao IS 'Identificador único do relacionamento.';

COMMENT ON COLUMN rel_evento_transicao.id_transicao IS 'Identificador da entidade transicao que realizará o relacionamento entre as entidades transicao com rel_evento_transicao. Identifica a transição cuja ação terá como consequência a ocorrência de um evento.';

COMMENT ON COLUMN rel_evento_transicao.id_evento IS 'Identificador da entidade evento que realizará o relacionamento entre as entidades evento com rel_evento_transicao. Identifica qual evento deverá ocorrer.';

CREATE SEQUENCE rel_evento_transicao_id_rel_evento_transicao_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE rel_evento_transicao_id_rel_evento_transicao_seq OWNED BY rel_evento_transicao.id_rel_evento_transicao;

CREATE TABLE rel_objeto_tramitavel_situacao (
    id_rel_obj_tramitavel_situacao integer NOT NULL,
    id_situacao integer,
    id_objeto_tramitavel integer
);

COMMENT ON TABLE rel_objeto_tramitavel_situacao IS 'Esta entidade é utilizada para identificar todos os possíveis relacionamentos do objeto tramitável com a situação. Obs.: O objeto tramitável poderá ter varias situações e uma situação por sua vez, poderá ter vários objetos associados a ela.';

COMMENT ON COLUMN rel_objeto_tramitavel_situacao.id_rel_obj_tramitavel_situacao IS 'Identificador único do relacionamento.';

COMMENT ON COLUMN rel_objeto_tramitavel_situacao.id_situacao IS 'Identificador da entidade situacao que realizará o relacionamento entre as entidades situacao e  rel_objeto_tramitavel_situacao.';

COMMENT ON COLUMN rel_objeto_tramitavel_situacao.id_objeto_tramitavel IS 'Identificador da entidade objeto_tramitavel que realizará o relacionamento entre as entidades objeto_tramitavel e  rel_objeto_tramitavel_situacao.';

CREATE SEQUENCE rel_objeto_tramitavel_situaca_id_rel_obj_tramitavel_situaca_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE rel_objeto_tramitavel_situaca_id_rel_obj_tramitavel_situaca_seq OWNED BY rel_objeto_tramitavel_situacao.id_rel_obj_tramitavel_situacao;

CREATE TABLE rel_situacao_historico_transicao (
    id_rel_situacao_historico_transicao integer NOT NULL,
    id_situacao integer,
    dt_final timestamp without time zone,
    tx_observacao character varying(1000),
    id_historico_objeto_tramitavel integer NOT NULL
);

COMMENT ON TABLE rel_situacao_historico_transicao IS 'Entidade utilizada para representar o relacionamento da situação e do histórico de transição. Sendo este relacionamento de fundamental importância para identificar se a tramitação/transição do objeto tramitável foi interrompida, por quanto tempo e a justificativa dessa interrupção.';

COMMENT ON COLUMN rel_situacao_historico_transicao.id_rel_situacao_historico_transicao IS 'Identificador único do relacionamento.';

COMMENT ON COLUMN rel_situacao_historico_transicao.id_situacao IS 'Identificador da entidade situacao que realizará o relacionamento com a entidade REL_SITUACAO_HISTORICO_TRANSICAO.';

COMMENT ON COLUMN rel_situacao_historico_transicao.dt_final IS 'Data final da interrupção da tramitação do objeto tramitável.';

COMMENT ON COLUMN rel_situacao_historico_transicao.tx_observacao IS 'Campo utilizado para descrever o motivo ou justificativa da interrupção da sequência do fluxo. ';

COMMENT ON COLUMN rel_situacao_historico_transicao.id_historico_objeto_tramitavel IS 'Identificador da entidade historico_objeto_tramitavel que realizará o relacionamento com a entidade REL_SITUACAO_HISTORICO_TRANSICAO.';

CREATE SEQUENCE rel_situacao_historico_transicao_id_rel_situacao_historico_tran
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE rel_situacao_historico_transicao_id_rel_situacao_historico_tran OWNED BY rel_situacao_historico_transicao.id_rel_situacao_historico_transicao;

CREATE TABLE situacao (
    id_situacao integer NOT NULL,
    tx_descricao character varying(100) NOT NULL,
    fl_ativo numeric(1,0) NOT NULL
);

COMMENT ON TABLE situacao IS 'Esta entidade é utilizada para identificar a situação atual do objeto tramitável. Esta situação poderá servir como parâmetro para impedir que um objeto tramitável sofra uma ação dentro do fluxo, ou apenas para representar uma informação adicional do estado do objeto tramitável. A situação difere da condição, pois a condição indica onde o objeto se encontra no fluxo e ele é único ao longo de todo o fluxo, uma situação pode estar associada ao objeto independente de sua posição no fluxo, e o objeto ainda pode ter varias situações, porém apenas uma condição. 
';

COMMENT ON COLUMN situacao.id_situacao IS 'Identificador único da tabela situacao.';

COMMENT ON COLUMN situacao.tx_descricao IS 'Descrição da situação.';

COMMENT ON COLUMN situacao.fl_ativo IS 'Flag para exclusão lógica da situação. 0 - Inativo  1 - Ativo.';

CREATE SEQUENCE situacao_id_situacao_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE situacao_id_situacao_seq OWNED BY situacao.id_situacao;

CREATE TABLE tipo_objeto_tramitavel (
    id_tipo_objeto_tramitavel integer NOT NULL,
    tx_descricao character varying(200) NOT NULL,
    fl_ativo numeric(1,0) NOT NULL
);

COMMENT ON TABLE tipo_objeto_tramitavel IS 'Esta entidade serve para tipificar o objeto tramitável, por exemplo: um objeto tramitável pode ser um processo, um documento, uma solicitação de informação complementar, entre outros. Tal distinção poderá facilitar na geração de relatórios.';

COMMENT ON COLUMN tipo_objeto_tramitavel.id_tipo_objeto_tramitavel IS 'Identificador único da entidade tipo_objeto_tramitavel.';

COMMENT ON COLUMN tipo_objeto_tramitavel.tx_descricao IS 'Nome do tipo objeto tramitavel. Ex: documento, processo, etc.';

COMMENT ON COLUMN tipo_objeto_tramitavel.fl_ativo IS 'Flag identificador da exclusão lógica do tipo do objeto tramitavel. 0 - Inativo  1 - Ativo';

CREATE SEQUENCE tipo_objeto_tramitavel_id_tipo_objeto_tramitavel_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE tipo_objeto_tramitavel_id_tipo_objeto_tramitavel_seq OWNED BY tipo_objeto_tramitavel.id_tipo_objeto_tramitavel;

CREATE TABLE transicao (
    id_transicao integer NOT NULL,
    id_acao integer NOT NULL,
    id_condicao_inicial integer,
    id_condicao_final integer,
    dt_prazo integer,
    fl_retornar_fluxo_anterior numeric(1,0)
);

COMMENT ON TABLE transicao IS 'Esta entidade é utilizada para descrever o fluxo que o objeto tramitável deverá percorrer, isto é, um conjunto de transições representa o fluxo. Cada transição executada dentro do fluxo pode acarretar em mudanças da condicao do objeto tramitável dentro do fluxo. Uma transição possui uma ação a ser executada, uma condição inicial, que representa a condição onde a ação pode ser executada e uma condição final, que indica a condição destino do objeto tramitável após a execução da ação. Desta forma, a partir desta entidade é possível descobrir quais ações podem ser realizadas em um objeto tramitável em um determinado ponto do fluxo. A modelagem desta entidade segue os conceitos de autômatos.
Uma transição pode possuir um ou mais impedimentos. Um impedimento indica que a transição não pode ser executada se o objeto possuir determinada situação. Assim, antes da execução da transição deve ser garantido que não haja nenhum impedimento para o objeto tramitável. 
';

COMMENT ON COLUMN transicao.id_transicao IS 'Identificador único da entidade transicao.';

COMMENT ON COLUMN transicao.id_acao IS 'Idenficador da tabela ação que realizará o relacionamento entre as tabelas transição e a tabela ação. Obs.: este campo identifica qual ação devera ser realizada pela transição do status inicial para o status final dentro do registro';

COMMENT ON COLUMN transicao.id_condicao_inicial IS 'Identificador da entidade condicao que realizará o relacionamento entre as entidades transicao e condicao. Identifica a condição inicial da transição, isto é, a condição no qual o objeto tramitável deve estar antes de sofrer a execução da ação pertinente a esta transição.';

COMMENT ON COLUMN transicao.id_condicao_final IS 'Identificador da entidade condicao que realizará o relacionamento entre as entidades transicao e condicao. Identifica a condição final da transição, isto é, a condição que será atribuída ao objeto tramitável após a sofrer a execução da ação pertinente a esta transição.';

COMMENT ON COLUMN transicao.dt_prazo IS 'Prazo máximo em dias para que a transição ocorra.';

COMMENT ON COLUMN transicao.fl_retornar_fluxo_anterior IS 'Flag que indica se a transição pode retornar para o fluxo anterior (0- Não e 1- Sim. Caso 1 (sim) a transição deverá ter o campo condicao_final null).';

CREATE SEQUENCE transicao_id_transicao_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE transicao_id_transicao_seq OWNED BY transicao.id_transicao;

CREATE VIEW vw_acao_disponivel_objeto AS
    SELECT DISTINCT a.id_acao, a.tx_descricao, a.fl_tramitavel, ot.id_objeto_tramitavel FROM acao a, transicao t, objeto_tramitavel ot WHERE ((((ot.id_condicao = t.id_condicao_inicial) AND (a.id_acao = t.id_acao)) AND (NOT (t.id_transicao IN (SELECT t.id_transicao FROM (((objeto_tramitavel o JOIN transicao t ON ((o.id_condicao = t.id_condicao_inicial))) JOIN impedimento_transicao it ON ((t.id_transicao = it.id_transicao))) LEFT JOIN rel_objeto_tramitavel_situacao ots ON (((it.id_situacao = ots.id_situacao) AND (ots.id_objeto_tramitavel = o.id_objeto_tramitavel)))) WHERE (((it.tp_impedimento = (1)::numeric) AND (o.id_objeto_tramitavel = ot.id_objeto_tramitavel)) AND (ots.id_situacao IS NULL)))))) AND (NOT (t.id_transicao IN (SELECT it.id_transicao FROM (impedimento_transicao it JOIN rel_objeto_tramitavel_situacao ots ON ((ots.id_situacao = it.id_situacao))) WHERE (((it.tp_impedimento = (0)::numeric) AND (ots.id_objeto_tramitavel = ot.id_objeto_tramitavel)) AND (it.id_transicao = t.id_transicao))))));

CREATE VIEW vw_condicao AS
    SELECT etapa.id_fluxo, etapa.id_etapa, etapa.tx_etapa, condicao.id_condicao, condicao.nm_condicao FROM condicao, etapa WHERE ((etapa.id_etapa = condicao.id_etapa) AND (condicao.fl_ativo = (1)::numeric));

CREATE VIEW vw_historico_objeto_tramitavel AS
    SELECT historico_objeto_tramitavel.id_historico_objeto_tramitavel, historico_objeto_tramitavel.id_objeto_tramitavel, historico_objeto_tramitavel.id_acao, 
    historico_objeto_tramitavel.tx_acao AS nm_acao, 
    historico_objeto_tramitavel.id_condicao_inicial, historico_objeto_tramitavel.tx_condicao_inicial 
    AS nm_condicao_inicial, historico_objeto_tramitavel.id_condicao_final, historico_objeto_tramitavel.tx_condicao_final 
    AS nm_condicao_final, historico_objeto_tramitavel.id_etapa_inicial, historico_objeto_tramitavel.tx_etapa_inicial
     AS nm_etapa_inicial, historico_objeto_tramitavel.id_etapa_final, historico_objeto_tramitavel.tx_etapa_final 
     AS nm_etapa_final, historico_objeto_tramitavel.tx_observacao, historico_objeto_tramitavel.id_usuario 
     AS id_usuario_executor, historico_objeto_tramitavel.tx_usuario AS nm_usuario_executor, historico_objeto_tramitavel.id_usuario_destino, 
     historico_objeto_tramitavel.tx_usuario_destino AS nm_usuario_destino, 
     historico_objeto_tramitavel.dt_cadastro FROM historico_objeto_tramitavel;

-- CREATE VIEW vw_objeto_tramitavel AS
--     SELECT o.id_objeto_tramitavel, s.id_condicao, s.nm_condicao, e.id_etapa, e.tx_etapa AS nm_etapa, o.id_usuario AS id_usuario_responsavel, 
--     p.nome AS nm_usuario_responsavel, o.id_pai, o.id_fluxo, o.id_responsavel_anterior, p_ant.nome AS nm_usuario_anterior, 
--     o.id_responsavel_fluxo_anterior, p_fx_ant.nome AS nm_usuario_fluxo_anterior, 
--     string_agg(DISTINCT (si.tx_descricao)::text, ', '::text) AS string_agg 
--     FROM ((((((((((objeto_tramitavel o JOIN condicao s ON ((o.id_condicao = s.id_condicao))) 
--     JOIN etapa e ON ((o.id_etapa = e.id_etapa))) 
--     LEFT JOIN portal_seguranca.usuario ui ON ((o.id_usuario = ui.id))) 
--     LEFT JOIN licenciamento.pessoa_fisica p ON ((ui.id_pessoa = p.id_pessoa))) 
--     LEFT JOIN portal_seguranca.usuario u_ant ON ((o.id_responsavel_anterior = u_ant.id))) 
--     LEFT JOIN licenciamento.pessoa_fisica p_ant ON ((u_ant.id_pessoa = p_ant.id_pessoa))) 
--     LEFT JOIN portal_seguranca.usuario u_fx_ant ON ((o.id_responsavel_fluxo_anterior = u_fx_ant.id))) 
--     LEFT JOIN licenciamento.pessoa_fisica p_fx_ant ON ((u_fx_ant.id_pessoa = p_fx_ant.id_pessoa))) 
--     LEFT JOIN rel_objeto_tramitavel_situacao rots ON ((rots.id_objeto_tramitavel = o.id_objeto_tramitavel))) 
--     LEFT JOIN situacao si ON ((si.id_situacao = rots.id_situacao))) 
--     GROUP BY o.id_objeto_tramitavel, s.id_condicao, s.nm_condicao, e.id_etapa, e.tx_etapa, o.id_usuario, p.nome, o.id_pai, o.id_fluxo, 
--     o.id_responsavel_anterior, p_ant.nome, o.id_responsavel_fluxo_anterior, p_fx_ant.nome;

ALTER TABLE ONLY acao ALTER COLUMN id_acao SET DEFAULT nextval('acao_id_acao_seq'::regclass);

ALTER TABLE ONLY condicao ALTER COLUMN id_condicao SET DEFAULT nextval('condicao_id_condicao_seq'::regclass);

ALTER TABLE ONLY config_situacao ALTER COLUMN id_config_situacao SET DEFAULT nextval('config_situacao_id_config_situacao_seq'::regclass);

ALTER TABLE ONLY etapa ALTER COLUMN id_etapa SET DEFAULT nextval('etapa_id_etapa_seq'::regclass);

ALTER TABLE ONLY evento ALTER COLUMN id_evento SET DEFAULT nextval('evento_id_evento_seq'::regclass);

ALTER TABLE ONLY fluxo ALTER COLUMN id_fluxo SET DEFAULT nextval('fluxo_id_fluxo_seq'::regclass);

ALTER TABLE ONLY historico_objeto_tramitavel ALTER COLUMN id_historico_objeto_tramitavel SET DEFAULT nextval('historico_objeto_tramitavel_id_historico_objeto_tramitavel_seq'::regclass);

ALTER TABLE ONLY impedimento_transicao ALTER COLUMN id_impedimento_transicao SET DEFAULT nextval('impedimento_transicao_id_impedimento_transicao_seq'::regclass);

ALTER TABLE ONLY objeto_tramitavel ALTER COLUMN id_objeto_tramitavel SET DEFAULT nextval('objeto_tramitavel_id_objeto_tramitavel_seq'::regclass);

ALTER TABLE ONLY rel_evento_transicao ALTER COLUMN id_rel_evento_transicao SET DEFAULT nextval('rel_evento_transicao_id_rel_evento_transicao_seq'::regclass);

ALTER TABLE ONLY rel_objeto_tramitavel_situacao ALTER COLUMN id_rel_obj_tramitavel_situacao SET DEFAULT nextval('rel_objeto_tramitavel_situaca_id_rel_obj_tramitavel_situaca_seq'::regclass);

ALTER TABLE ONLY rel_situacao_historico_transicao ALTER COLUMN id_rel_situacao_historico_transicao SET DEFAULT nextval('rel_situacao_historico_transicao_id_rel_situacao_historico_tran'::regclass);

ALTER TABLE ONLY situacao ALTER COLUMN id_situacao SET DEFAULT nextval('situacao_id_situacao_seq'::regclass);

ALTER TABLE ONLY tipo_objeto_tramitavel ALTER COLUMN id_tipo_objeto_tramitavel SET DEFAULT nextval('tipo_objeto_tramitavel_id_tipo_objeto_tramitavel_seq'::regclass);

ALTER TABLE ONLY transicao ALTER COLUMN id_transicao SET DEFAULT nextval('transicao_id_transicao_seq'::regclass);

ALTER TABLE ONLY acao ADD CONSTRAINT pk_acao PRIMARY KEY (id_acao);

ALTER TABLE ONLY condicao ADD CONSTRAINT pk_condicao PRIMARY KEY (id_condicao);

ALTER TABLE ONLY config_situacao ADD CONSTRAINT pk_config_situacao PRIMARY KEY (id_config_situacao);

ALTER TABLE ONLY etapa ADD CONSTRAINT pk_etapa PRIMARY KEY (id_etapa);

ALTER TABLE ONLY evento ADD CONSTRAINT pk_evento PRIMARY KEY (id_evento);

ALTER TABLE ONLY fluxo ADD CONSTRAINT pk_fluxo PRIMARY KEY (id_fluxo);

ALTER TABLE ONLY historico_objeto_tramitavel ADD CONSTRAINT pk_historico_objeto_tramitavel PRIMARY KEY (id_historico_objeto_tramitavel);

ALTER TABLE ONLY impedimento_transicao ADD CONSTRAINT pk_impedimento_transicao PRIMARY KEY (id_impedimento_transicao);

ALTER TABLE ONLY objeto_tramitavel ADD CONSTRAINT pk_objeto_tramitavel PRIMARY KEY (id_objeto_tramitavel);

ALTER TABLE ONLY rel_evento_transicao ADD CONSTRAINT pk_rel_evento_transicao PRIMARY KEY (id_rel_evento_transicao);

ALTER TABLE ONLY rel_objeto_tramitavel_situacao ADD CONSTRAINT pk_rel_objeto_tramitavel_situa PRIMARY KEY (id_rel_obj_tramitavel_situacao);

ALTER TABLE ONLY rel_situacao_historico_transicao ADD CONSTRAINT pk_rel_situacao_historico_tran PRIMARY KEY (id_rel_situacao_historico_transicao);

ALTER TABLE ONLY situacao ADD CONSTRAINT pk_situacao PRIMARY KEY (id_situacao);

ALTER TABLE ONLY tipo_objeto_tramitavel ADD CONSTRAINT pk_tipo_objeto_tramitavel PRIMARY KEY (id_tipo_objeto_tramitavel);

ALTER TABLE ONLY transicao ADD CONSTRAINT pk_transicao PRIMARY KEY (id_transicao);

CREATE UNIQUE INDEX un_impedimento_transicao ON impedimento_transicao USING btree (id_situacao, id_transicao);

CREATE UNIQUE INDEX un_transicao_evento ON rel_evento_transicao USING btree (id_transicao, id_evento);

ALTER TABLE ONLY config_situacao ADD CONSTRAINT fk_ci_id_situacao FOREIGN KEY (id_situacao) REFERENCES situacao(id_situacao);

ALTER TABLE ONLY config_situacao ADD CONSTRAINT fk_ci_id_transicao FOREIGN KEY (id_transicao) REFERENCES transicao(id_transicao);

ALTER TABLE ONLY etapa ADD CONSTRAINT fk_e_id_fluxo FOREIGN KEY (id_fluxo) REFERENCES fluxo(id_fluxo);

ALTER TABLE ONLY fluxo ADD CONSTRAINT fk_f_id_condicao FOREIGN KEY (id_condicao_inicial) REFERENCES condicao(id_condicao);

ALTER TABLE ONLY historico_objeto_tramitavel ADD CONSTRAINT fk_hot_id_acao FOREIGN KEY (id_acao) REFERENCES acao(id_acao);

ALTER TABLE ONLY historico_objeto_tramitavel ADD CONSTRAINT fk_hot_id_condicacao_inicial FOREIGN KEY (id_condicao_inicial) REFERENCES condicao(id_condicao);

ALTER TABLE ONLY historico_objeto_tramitavel ADD CONSTRAINT fk_hot_id_condicao_final FOREIGN KEY (id_condicao_final) REFERENCES condicao(id_condicao);

ALTER TABLE ONLY historico_objeto_tramitavel ADD CONSTRAINT fk_hot_id_etapa_final FOREIGN KEY (id_etapa_final) REFERENCES etapa(id_etapa);

ALTER TABLE ONLY historico_objeto_tramitavel ADD CONSTRAINT fk_hot_id_etapa_inicial FOREIGN KEY (id_etapa_inicial) REFERENCES etapa(id_etapa);

ALTER TABLE ONLY historico_objeto_tramitavel ADD CONSTRAINT fk_hot_id_objeto_tramitavel FOREIGN KEY (id_objeto_tramitavel) REFERENCES objeto_tramitavel(id_objeto_tramitavel);

ALTER TABLE ONLY impedimento_transicao ADD CONSTRAINT fk_it_id_situacao FOREIGN KEY (id_situacao) REFERENCES situacao(id_situacao);

ALTER TABLE ONLY impedimento_transicao ADD CONSTRAINT fk_it_id_transicao FOREIGN KEY (id_transicao) REFERENCES transicao(id_transicao);

ALTER TABLE ONLY objeto_tramitavel ADD CONSTRAINT fk_ot_id_condicao FOREIGN KEY (id_condicao) REFERENCES condicao(id_condicao);

ALTER TABLE ONLY objeto_tramitavel ADD CONSTRAINT fk_ot_id_condicao_fluxo_anterior FOREIGN KEY (id_condicao_fluxo_anterior) REFERENCES condicao(id_condicao);

ALTER TABLE ONLY objeto_tramitavel ADD CONSTRAINT fk_ot_id_etapa FOREIGN KEY (id_etapa) REFERENCES etapa(id_etapa);

ALTER TABLE ONLY objeto_tramitavel ADD CONSTRAINT fk_ot_id_fluxo FOREIGN KEY (id_fluxo) REFERENCES fluxo(id_fluxo);

ALTER TABLE ONLY objeto_tramitavel ADD CONSTRAINT fk_ot_id_obj_tram_pai FOREIGN KEY (id_pai) REFERENCES objeto_tramitavel(id_objeto_tramitavel);

ALTER TABLE ONLY objeto_tramitavel ADD CONSTRAINT fk_ot_id_tipo_obj_tramitavel FOREIGN KEY (id_tipo_objeto_tramitavel) REFERENCES tipo_objeto_tramitavel(id_tipo_objeto_tramitavel);

ALTER TABLE ONLY rel_evento_transicao ADD CONSTRAINT fk_ret_id_evento FOREIGN KEY (id_evento) REFERENCES evento(id_evento);

ALTER TABLE ONLY rel_evento_transicao ADD CONSTRAINT fk_ret_id_transicao FOREIGN KEY (id_transicao) REFERENCES transicao(id_transicao);

ALTER TABLE ONLY rel_objeto_tramitavel_situacao ADD CONSTRAINT fk_rots_id_obj_tramitavel FOREIGN KEY (id_objeto_tramitavel) REFERENCES objeto_tramitavel(id_objeto_tramitavel);

ALTER TABLE ONLY rel_objeto_tramitavel_situacao ADD CONSTRAINT fk_rots_id_situacao FOREIGN KEY (id_situacao) REFERENCES situacao(id_situacao);

ALTER TABLE ONLY rel_situacao_historico_transicao ADD CONSTRAINT fk_rsht_id_historico_objeto_tramitavel FOREIGN KEY (id_historico_objeto_tramitavel) REFERENCES historico_objeto_tramitavel(id_historico_objeto_tramitavel) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY rel_situacao_historico_transicao ADD CONSTRAINT fk_rsht_id_situacao FOREIGN KEY (id_situacao) REFERENCES situacao(id_situacao);

ALTER TABLE ONLY condicao ADD CONSTRAINT fk_s_id_etapa FOREIGN KEY (id_etapa) REFERENCES etapa(id_etapa);

ALTER TABLE ONLY transicao ADD CONSTRAINT fk_t_id_acao FOREIGN KEY (id_acao) REFERENCES acao(id_acao);

ALTER TABLE ONLY transicao ADD CONSTRAINT fk_t_id_condicao_fim FOREIGN KEY (id_condicao_final) REFERENCES condicao(id_condicao);

ALTER TABLE ONLY transicao ADD CONSTRAINT fk_t_id_condicao_inicial FOREIGN KEY (id_condicao_inicial) REFERENCES condicao(id_condicao);

SET search_path = analise, pg_catalog, public;

# --- !Downs

DROP SCHEMA tramitacao CASCADE;