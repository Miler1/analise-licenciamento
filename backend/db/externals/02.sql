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

CREATE VIEW vw_objeto_tramitavel AS
    SELECT o.id_objeto_tramitavel, s.id_condicao, s.nm_condicao, e.id_etapa, e.tx_etapa AS nm_etapa, o.id_usuario AS id_usuario_responsavel, 
    p.nome AS nm_usuario_responsavel, o.id_pai, o.id_fluxo, o.id_responsavel_anterior, p_ant.nome AS nm_usuario_anterior, 
    o.id_responsavel_fluxo_anterior, p_fx_ant.nome AS nm_usuario_fluxo_anterior, 
    string_agg(DISTINCT (si.tx_descricao)::text, ', '::text) AS string_agg 
    FROM ((((((((((objeto_tramitavel o JOIN condicao s ON ((o.id_condicao = s.id_condicao))) 
    JOIN etapa e ON ((o.id_etapa = e.id_etapa))) 
    LEFT JOIN portal_seguranca.usuario ui ON ((o.id_usuario = ui.id))) 
    LEFT JOIN licenciamento.pessoa_fisica p ON ((ui.id_pessoa = p.id_pessoa))) 
    LEFT JOIN portal_seguranca.usuario u_ant ON ((o.id_responsavel_anterior = u_ant.id))) 
    LEFT JOIN licenciamento.pessoa_fisica p_ant ON ((u_ant.id_pessoa = p_ant.id_pessoa))) 
    LEFT JOIN portal_seguranca.usuario u_fx_ant ON ((o.id_responsavel_fluxo_anterior = u_fx_ant.id))) 
    LEFT JOIN licenciamento.pessoa_fisica p_fx_ant ON ((u_fx_ant.id_pessoa = p_fx_ant.id_pessoa))) 
    LEFT JOIN rel_objeto_tramitavel_situacao rots ON ((rots.id_objeto_tramitavel = o.id_objeto_tramitavel))) 
    LEFT JOIN situacao si ON ((si.id_situacao = rots.id_situacao))) 
    GROUP BY o.id_objeto_tramitavel, s.id_condicao, s.nm_condicao, e.id_etapa, e.tx_etapa, o.id_usuario, p.nome, o.id_pai, o.id_fluxo, 
    o.id_responsavel_anterior, p_ant.nome, o.id_responsavel_fluxo_anterior, p_fx_ant.nome;

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

--3

CREATE ROLE tramitacao LOGIN
ENCRYPTED PASSWORD 'tramitacao'
SUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION;

GRANT USAGE ON SCHEMA public TO tramitacao;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO tramitacao;

GRANT SELECT ON TABLE analise.usuario_analise TO tramitacao;
GRANT SELECT ON TABLE analise.inconsistencia TO tramitacao;
GRANT SELECT ON TABLE analise.rel_documento_inconsistencia TO tramitacao;
GRANT SELECT ON TABLE analise.inconsistencia_tecnica TO tramitacao;
GRANT SELECT ON TABLE analise.rel_documento_inconsistencia_tecnica TO tramitacao;
GRANT SELECT ON TABLE analise.desvinculo_analise_tecnica TO tramitacao;


GRANT USAGE ON SCHEMA tramitacao TO tramitacao;
GRANT SELECT, UPDATE, INSERT, DELETE ON ALL TABLES IN SCHEMA tramitacao TO tramitacao;
GRANT SELECT, USAGE ON ALL SEQUENCES IN SCHEMA tramitacao TO tramitacao;

GRANT CONNECT ON DATABASE licenciamento_ap TO tramitacao;

GRANT USAGE ON SCHEMA analise, tramitacao TO tramitacao;

GRANT SELECT ON ALL TABLES IN SCHEMA analise TO tramitacao;
GRANT SELECT ON ALL SEQUENCES IN SCHEMA analise TO tramitacao;


SET search_path = tramitacao, pg_catalog;

INSERT INTO tramitacao.acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (1, 'Vincular', 1, 1);
INSERT INTO tramitacao.acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (2, 'Iniciar análise', 1, 1);
INSERT INTO tramitacao.acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (3, 'Notificar', 1, 1);
INSERT INTO tramitacao.acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (4, 'Analisar', 1, 1);
INSERT INTO tramitacao.acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (5, 'Recusar análise', 1, 1);
INSERT INTO tramitacao.acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (6, 'Deferir análise', 1, 1);
INSERT INTO tramitacao.acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (7, 'Indeferir análise', 1, 1);

SELECT pg_catalog.setval('acao_id_acao_seq', 7, true);

INSERT INTO tramitacao.fluxo (id_fluxo, id_condicao_inicial, tx_descricao, dt_prazo) VALUES (1, null, 'Processo de Análise do Licenciamento Ambiental', NULL);

--SELECT pg_catalog.setval('fluxo_id_fluxo_seq', 1, true);

INSERT INTO tramitacao.etapa (id_etapa, id_fluxo, tx_etapa, dt_prazo) VALUES (1, 1, 'Análise jurídica', NULL);
INSERT INTO tramitacao.etapa (id_etapa, id_fluxo, tx_etapa, dt_prazo) VALUES (2, 1, 'Análise técnica', NULL);

--SELECT pg_catalog.setval('etapa_id_etapa_seq', 2, true);

INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (1, 1, 'Aguardando vinculação jurídica', 1);
INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (2, 1, 'Aguardando análise jurídica', 1);
INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (3, 1, 'Em análise jurídica', 1);
INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (4, NULL, 'Notificado', 1);
INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (5, 1, 'Aguardando validação jurídica', 1);
INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (6, NULL, 'Arquivado', 1);
INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (7, 2, 'Aguardando vinculação técnica', 1);

--SELECT pg_catalog.setval('condicao_id_condicao_seq', 7, true);

UPDATE tramitacao.fluxo SET id_condicao_inicial = 1 WHERE id_fluxo = 1;

INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (1, 1, 1, 2, NULL, NULL);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (2, 2, 2, 3, NULL, NULL);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (3, 3, 3, 4, NULL, NULL);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (4, 4, 3, 5, NULL, NULL);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (5, 5, 5, 2, NULL, NULL);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (6, 6, 5, 7, NULL, NULL);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (7, 7, 5, 6, NULL, NULL);

--SELECT pg_catalog.setval('transicao_id_transicao_seq', 7, true);

INSERT INTO tramitacao.tipo_objeto_tramitavel (id_tipo_objeto_tramitavel, tx_descricao, fl_ativo) VALUES (1, 'Licenciamento Ambiental', 1);

--SELECT pg_catalog.setval('tipo_objeto_tramitavel_id_tipo_objeto_tramitavel_seq', 1, true);

SET search_path = licenciamento, pg_catalog, public;

--4
-- Grant select do schema licenciamento para a role tramitacao
GRANT USAGE ON SCHEMA licenciamento TO tramitacao;
GRANT SELECT ON ALL TABLES IN SCHEMA licenciamento TO tramitacao;

--7


SET search_path = tramitacao, pg_catalog;

UPDATE acao SET tx_descricao = 'Vincular consultor' WHERE id_acao = 1;
UPDATE acao SET tx_descricao = 'Iniciar análise jurídica' WHERE id_acao = 2;
UPDATE acao SET tx_descricao = 'Deferir análise jurídica' WHERE id_acao = 4;
UPDATE acao SET tx_descricao = 'Invalidar parecer jurídico' WHERE id_acao = 5;
UPDATE acao SET tx_descricao = 'Validar deferimento jurídico' WHERE id_acao = 6;
UPDATE acao SET tx_descricao = 'Validar indeferimento jurídico' WHERE id_acao = 7;

INSERT INTO acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (8, 'Indeferir análise jurídica', 1, 1);
INSERT INTO acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (9, 'Solicitar ajustes parecer jurídico', 1, 1);
INSERT INTO acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (10, 'Vincular analista', 1, 1);
INSERT INTO acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (11, 'Iniciar análise técnica', 1, 1);
INSERT INTO acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (12, 'Deferir análise técnica', 1, 1);
INSERT INTO acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (13, 'Indeferir análise técnica', 1, 1);
INSERT INTO acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (14, 'Invalidar parecer técnico', 1, 1);
INSERT INTO acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (15, 'Solicitar ajustes parecer técnico', 1, 1);
INSERT INTO acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (16, 'Validar deferimento técnico', 1, 1);
INSERT INTO acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (17, 'Validar indeferimento técnico', 1, 1);

SELECT pg_catalog.setval('acao_id_acao_seq', 17, TRUE);

INSERT INTO tramitacao.etapa (id_etapa, id_fluxo, tx_etapa, dt_prazo) VALUES (3, 1, 'Liberação da licença', NULL);

--SELECT pg_catalog.setval('etapa_id_etapa_seq', 3, TRUE);

INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (8, 2, 'Aguardando análise técnica', 1);
INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (9, 2, 'Em análise técnica', 1);
INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (10, 2, 'Aguardando validação técnica', 1);
INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (11, 3, 'Aguardando assinatura diretor', 1);

--SELECT pg_catalog.setval('condicao_id_condicao_seq', 11, TRUE);

INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (8, 8, 3, 5, NULL, NULL);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (9, 9, 5, 2, NULL, NULL);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (10, 10, 7, 8, NULL, NULL);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (11, 11, 8, 9, NULL, NULL);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (12, 12, 9, 10, NULL, NULL);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (13, 13, 9, 10, NULL, NULL);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (14, 14, 10, 8, NULL, NULL);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (15, 15, 10, 8, NULL, NULL);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (16, 16, 10, 11, NULL, NULL);
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (17, 17, 10, 6, NULL, NULL);

--SELECT pg_catalog.setval('transicao_id_transicao_seq', 17, TRUE);

INSERT INTO tramitacao.situacao (id_situacao, fl_ativo, tx_descricao) VALUES (1,1,'Deferido');
INSERT INTO tramitacao.situacao (id_situacao, fl_ativo, tx_descricao) VALUES (2,1,'Indeferido');

SELECT pg_catalog.setval('situacao_id_situacao_seq', 2, TRUE);

INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (1, 1, 4, 1);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (2, 2, 8, 1);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (3, 1, 3, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (4, 2, 3, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (5, 1, 5, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (6, 2, 5, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (7, 1, 6, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (8, 2, 6, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (9, 1, 7, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (10, 2, 7, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (11, 1, 9, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (12, 2, 9, 0);

INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (13, 1, 12, 1);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (14, 2, 13, 1);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (15, 1, 3, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (16, 2, 3, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (17, 1, 14, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (18, 2, 14, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (19, 1, 15, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (20, 2, 15, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (21, 1, 16, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (22, 2, 16, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (23, 1, 17, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (24, 2, 17, 0);

-- SELECT pg_catalog.setval('config_situacao_id_config_situacao_seq', 24, TRUE);

-- INSERT INTO impedimento_transicao (id_impedimento_transicao, id_situacao, id_transicao, tp_impedimento) VALUES (1, 1, 6, 1);
-- INSERT INTO impedimento_transicao (id_impedimento_transicao, id_situacao, id_transicao, tp_impedimento) VALUES (2, 2, 7, 1);

-- SELECT pg_catalog.setval('impedimento_transicao_id_impedimento_transicao_seq', 2, TRUE);
--8
INSERT INTO tramitacao.acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (18, 'Iniciar processo', 1, 1);

SELECT pg_catalog.setval('tramitacao.acao_id_acao_seq', 18, true);

INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (0, null, 'Estado inicial', 1);

INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (18, 18, 0, 1, NULL, NULL);

SELECT pg_catalog.setval('tramitacao.transicao_id_transicao_seq', 18, true);

--12
GRANT USAGE ON SCHEMA tramitacao TO licenciamento_ap;

GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA tramitacao TO licenciamento_ap;
GRANT SELECT, USAGE ON ALL SEQUENCES IN SCHEMA tramitacao TO licenciamento_ap;

GRANT USAGE ON SCHEMA analise TO licenciamento_ap;

GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA analise TO licenciamento_ap;
GRANT SELECT, USAGE ON ALL SEQUENCES IN SCHEMA analise TO licenciamento_ap;

--14
INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) 
VALUES (19, 3, 9, 4, NULL, NULL);

SELECT pg_catalog.setval('tramitacao.transicao_id_transicao_seq', 19, true);


--18


UPDATE tramitacao.condicao SET nm_condicao='Aguardando vinculação técnica pelo gerente' WHERE id_condicao=7;
UPDATE tramitacao.condicao SET nm_condicao='Aguardando validação técnica pelo gerente' WHERE id_condicao=10;

INSERT INTO tramitacao.condicao(id_condicao,id_etapa,nm_condicao,fl_ativo) VALUES
(12,2,'Aguardando vinculação técnica pelo coordenador',1),
(13,2,'Aguardando validação técnica pelo coordenador',1);



UPDATE tramitacao.acao SET tx_descricao='Deferir análise técnica via gerente' WHERE id_acao=12;
UPDATE tramitacao.acao SET tx_descricao='Indeferir análise técnica via gerente' WHERE id_acao=13;
UPDATE tramitacao.acao SET tx_descricao='Validar deferimento técnico pelo gerente' WHERE id_acao=16;
UPDATE tramitacao.acao SET tx_descricao='Invalidar parecer técnico pelo gerente' WHERE id_acao=14;
UPDATE tramitacao.acao SET tx_descricao='Solicitar ajustes parecer técnico pelo gerente' WHERE id_acao=15;
UPDATE tramitacao.acao SET tx_descricao='Validar indeferimento técnico pelo gerente' WHERE id_acao=17;

INSERT INTO tramitacao.acao(id_acao,tx_descricao,fl_ativo,fl_tramitavel) VALUES
(19,'Vincular gerente',1,1),
(20,'Indeferir análise técnica via coordenador',1,1),
(21,'Deferir análise técnica via coordenador',1,1),
(22,'Validar deferimento técnico pelo coordenador',1,1),
(23,'Validar indeferimento técnico pelo coordenador',1,1),
(24,'Invalidar parecer técnico pelo coordenador encaminhando para outro gerente',1,1),
(25,'Invalidar parecer técnico encaminhando para outro técnico',1,1),
(26,'Solicitar ajuste do parecer técnico pelo coordenador',1,1);



UPDATE tramitacao.transicao SET id_condicao_final=12 WHERE id_condicao_inicial=5 AND id_condicao_final=7 AND id_acao=6;

UPDATE tramitacao.transicao SET id_condicao_final=13 WHERE id_condicao_inicial=10 AND id_acao=16;


UPDATE tramitacao.transicao SET id_condicao_final = 13 WHERE id_transicao = 17;

INSERT INTO tramitacao.transicao(id_transicao,id_condicao_inicial,id_condicao_final,id_acao) VALUES
(20,12,7,19),
(21,13,11,22),
(22,13,6,23),
(23,13,8,25),
(24,13,7,24),
(25,13,8,26),
(26,12,8,10),
(27,9,13,21),
(28,9,13,20);

-- INICIO Configurar adição e remoção de situações

--Ação 21 (Deferir análise técnica via coordenador) - Adicionar situação "Deferido" - (Nº da transição: 27)
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (25, 1, 27, 1);
--Ação 20 (Indeferir análise técnica via coordenador) - Adicionar situação "Indeferido" - (Nº da transição: 28)
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (26, 2, 28, 1);

--Ação 22 (Validar deferimento técnico pelo coordenador) - Remover situações "Deferido" e "Indeferido" - (Nº da transição: 21)
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (27, 1, 21, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (28, 2, 21, 0);

--Ação 23 (Validar indeferimento técnico pelo coordenador) - Remover situações "Deferido" e "Indeferido" - (Nº da transição: 22)
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (29, 1, 22, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (30, 2, 22, 0);

--Ação 24 (Invalidar parecer técnico pelo coordenador encaminhando para outro gerente) - Remover situações "Deferido" e "Indeferido" - (Nº da transição: 24)
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (31, 1, 24, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (32, 2, 24, 0);

--Ação 25 (Invalidar parecer técnico encaminhando para outro técnico) - Remover situações "Deferido" e "Indeferido" - (Nº da transição: 23)
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (33, 1, 23, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (34, 2, 23, 0);

--Ação 26 (Solicitar ajuste do parecer técnico pelo coordenador) - Remover situações "Deferido" e "Indeferido" - (Nº da transição: 25)
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (35, 1, 25, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (36, 2, 25, 0);


-- FIM Configurar adição e remoção de situações

-- INICIO Configuração dos impedimentos

--Ação 16 (Validar deferimento técnico pelo gerente) - Situação "Deferido" obrigatória - (Nº da transição: 16)
INSERT INTO tramitacao.impedimento_transicao (id_impedimento_transicao, id_situacao, id_transicao, tp_impedimento) VALUES (3, 1, 16, 1);
--Ação 17 (Validar indeferimento técnico pelo gerente) - Situação "Indeferido" obrigatória - (Nº da transição: 17)
INSERT INTO tramitacao.impedimento_transicao (id_impedimento_transicao, id_situacao, id_transicao, tp_impedimento) VALUES (4, 2, 17, 1);

--Ação 22 (Validar deferimento técnico pelo coordenador) - Situação "Deferido" obrigatória - (Nº da transição: 21)
INSERT INTO tramitacao.impedimento_transicao (id_impedimento_transicao, id_situacao, id_transicao, tp_impedimento) VALUES (5, 1, 21, 1);
--Ação 23 (Validar indeferimento pelo coordenador) - Situação "Indeferido" obrigatória - (Nº da transição: 22)
INSERT INTO tramitacao.impedimento_transicao (id_impedimento_transicao, id_situacao, id_transicao, tp_impedimento) VALUES (6, 2, 22, 1);

-- fim Configuração dos impedimentos
--19
UPDATE tramitacao.acao SET tx_descricao = 'Solicitar ajustes do parecer técnico pelo coordenador para o analista' WHERE id_acao = 26;

INSERT INTO tramitacao.acao(id_acao,tx_descricao,fl_ativo,fl_tramitavel) VALUES
(27,'Solicitar ajustes do parecer técnico pelo coordenador para o gerente',1,1);

INSERT INTO tramitacao.transicao(id_transicao,id_condicao_inicial,id_condicao_final,id_acao) VALUES (29,13,10,27);

--Ação 27 (Solicitar ajuste do parecer técnico pelo coordenador) - Remover situações "Deferido" e "Indeferido" - (Nº da transição: 29)
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (37, 1, 29, 0);
INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES (38, 2, 29, 0);

--20
DELETE FROM tramitacao.config_situacao WHERE id_transicao in (16,17,29);

--21
INSERT INTO tramitacao.acao(id_acao,tx_descricao,fl_ativo,fl_tramitavel) VALUES
(28, 'Solicitar ajuste da análise jurídica pelo aprovador',1,1),
(29, 'Deferir análise juridica pelo coordenador para o aprovador',1,1),
(30, 'Solicitar ajuste da análise técnica pelo aprovador',1,1);

SELECT pg_catalog.setval('tramitacao.acao_id_acao_seq', 30, TRUE);

INSERT INTO tramitacao.transicao(id_transicao,id_condicao_inicial,id_condicao_final,id_acao) VALUES
(30,11,5,28),
(31,5,11,29),
(32,11,13,30);

SELECT pg_catalog.setval('tramitacao.transicao_id_transicao_seq', 32, TRUE);

INSERT INTO tramitacao.situacao(id_situacao,tx_descricao,fl_ativo) VALUES
(3, 'Revisão solicitada aprovador',1);

SELECT pg_catalog.setval('tramitacao.situacao_id_situacao_seq', 3, TRUE);

INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES 
(37, 3, 30, 1),
(38, 3, 31, 0);

SELECT pg_catalog.setval('tramitacao.config_situacao_id_config_situacao_seq', 38, TRUE);

INSERT INTO tramitacao.impedimento_transicao (id_impedimento_transicao, id_situacao, id_transicao, tp_impedimento) VALUES 
(7, 3, 6, 0),
(8, 3, 31, 1);

SELECT pg_catalog.setval('tramitacao.impedimento_transicao_id_impedimento_transicao_seq', 8, TRUE);

--24
UPDATE tramitacao.condicao set nm_condicao = 'Aguardando assinatura aprovador' where id_condicao = 11;

--25
DELETE FROM tramitacao.config_situacao WHERE id_transicao = 21;

--27
INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES
(14, 3, 'Licenca emitida', 1);
SELECT pg_catalog.setval('tramitacao.condicao_id_condicao_seq', 14, TRUE);

INSERT INTO tramitacao.acao(id_acao,tx_descricao,fl_ativo,fl_tramitavel) VALUES
(31, 'Emitir licença', 1, 1);
SELECT pg_catalog.setval('tramitacao.acao_id_acao_seq', 31, TRUE);

INSERT INTO tramitacao.transicao(id_transicao,id_condicao_inicial,id_condicao_final,id_acao) VALUES
(33,11,14,31);
SELECT pg_catalog.setval('tramitacao.transicao_id_transicao_seq', 33, TRUE);

--29
INSERT INTO tramitacao.acao(id_acao,tx_descricao,fl_ativo,fl_tramitavel) VALUES
(32, 'Suspender processo',1,1),
(33, 'Reemitir licença',1,1),
(34, 'Cancelar processo',1,1);

SELECT pg_catalog.setval('tramitacao.acao_id_acao_seq', 34, TRUE);

INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (15, 3, 'Suspenso', 1);
INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (16, 3, 'Cancelado', 1);

SELECT pg_catalog.setval('tramitacao.condicao_id_condicao_seq', 16, TRUE);

INSERT INTO tramitacao.transicao(id_transicao,id_condicao_inicial,id_condicao_final,id_acao) VALUES
(34,14,15,32),
(35,15,14,33),
(36,14,16,34);

SELECT pg_catalog.setval('tramitacao.transicao_id_transicao_seq', 36, TRUE);

UPDATE tramitacao.condicao SET nm_condicao = 'Licença emitida' WHERE id_condicao = 14;


--31

INSERT INTO tramitacao.acao(id_acao,tx_descricao,fl_ativo,fl_tramitavel) VALUES
(35, 'Resolver notificação jurídica',1,1),
(36, 'Resolver notificação técnica',1,1),
(37, 'Arquivar processo',1,1);

SELECT pg_catalog.setval('tramitacao.acao_id_acao_seq', 37, TRUE);

INSERT INTO tramitacao.transicao(id_transicao,id_condicao_inicial,id_condicao_final,id_acao) VALUES
(37,4,2,35),
(38,4,8,36),
(39,4,6,37);

SELECT pg_catalog.setval('tramitacao.transicao_id_transicao_seq', 39, TRUE);

INSERT INTO tramitacao.situacao(id_situacao,tx_descricao,fl_ativo) VALUES
(4, 'Notificado via jurídico',1),
(5, 'Notificado via técnico',1);

SELECT pg_catalog.setval('tramitacao.situacao_id_situacao_seq', 5, TRUE);

INSERT INTO tramitacao.config_situacao (id_config_situacao, id_situacao, id_transicao, fl_adicionar) VALUES 
(39, 4, 3, 1),
(40, 4, 37, 0),
(41, 4, 39, 0),
(42, 5, 19, 1),
(43, 5, 38, 0),
(44, 5, 39, 0);

SELECT pg_catalog.setval('tramitacao.config_situacao_id_config_situacao_seq', 44, TRUE);

INSERT INTO tramitacao.impedimento_transicao (id_impedimento_transicao, id_situacao, id_transicao, tp_impedimento) VALUES 
(9, 4, 37, 1),
(10, 5, 38, 1);

SELECT pg_catalog.setval('tramitacao.impedimento_transicao_id_impedimento_transicao_seq', 10, TRUE);

--32
-- Adicionar ações para a renovação de licencas simples

INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (38, 'Arquivar por renovação', 1, 1);
INSERT INTO tramitacao.transicao(id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (40, 38, 14, 6, NULL, NULL);

INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (39, 'Renovar licença sem alterações', 1, 1);
INSERT INTO tramitacao.transicao(id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (41, 39, 1, 12, NULL, NULL);

SELECT pg_catalog.setval('tramitacao.acao_id_acao_seq', 39, TRUE);

--33

-- Adicionar condições do manejo digital

INSERT INTO tramitacao.condicao(id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (17, NULL, 'Manejo digital aguardando análise técnica', 1);
INSERT INTO tramitacao.condicao(id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (18, NULL, 'Manejo digital em análise técnica', 1);
INSERT INTO tramitacao.condicao(id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (19, NULL, 'Manejo digital deferido', 1);
INSERT INTO tramitacao.condicao(id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (20, NULL, 'Manejo digital indeferido', 1);

SELECT setval('tramitacao.condicao_id_condicao_seq', max(id_condicao)) FROM tramitacao.condicao;

-- Adicionar fluxo do manejo digital

INSERT INTO tramitacao.fluxo(id_fluxo, id_condicao_inicial, tx_descricao, dt_prazo) VALUES (2, 17, 'Processo de Análise do Manejo Digital', NULL);

SELECT setval('tramitacao.fluxo_id_fluxo_seq', max(id_fluxo)) FROM tramitacao.fluxo;

-- Adicionar condições do manejo digital

INSERT INTO tramitacao.tipo_objeto_tramitavel(id_tipo_objeto_tramitavel, tx_descricao, fl_ativo) VALUES (2,'Manejo digital', 2);

SELECT setval('tramitacao.tipo_objeto_tramitavel_id_tipo_objeto_tramitavel_seq', max(id_tipo_objeto_tramitavel)) FROM tramitacao.tipo_objeto_tramitavel;

-- Adicionar etapa do manejo digital e definir a etapa nas condições criadas

INSERT INTO tramitacao.etapa(id_etapa, id_fluxo, tx_etapa, dt_prazo) VALUES (4, 2, 'Análise técnica do manejo digital', NULL);
UPDATE tramitacao.condicao SET id_etapa = 4 WHERE id_condicao in (17, 18, 19, 20);

SELECT setval('tramitacao.etapa_id_etapa_seq', max(id_etapa)) FROM tramitacao.etapa;

-- Adicionar ação do manejo digital

INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (40, 'Iniciar análise técnica do manejo florestal', 1, 1);
INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (41, 'Deferir análise técnica do manejo florestal', 1, 1);
INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (42, 'Indeferir análise técnica do manejo florestal', 1, 1);

SELECT setval('tramitacao.acao_id_acao_seq', max(id_acao)) FROM tramitacao.acao;

-- Adicionar tramitação do manejo digital

INSERT INTO tramitacao.transicao(id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (44, 40, 17, 18, NULL, NULL);
INSERT INTO tramitacao.transicao(id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (45, 41, 18, 19, NULL, NULL);
INSERT INTO tramitacao.transicao(id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (46, 42, 18, 20, NULL, NULL);


SELECT setval('tramitacao.transicao_id_transicao_seq', max(id_transicao)) FROM tramitacao.transicao;

--34

-- Adicionando nova condição do licenciamento

INSERT INTO tramitacao.condicao(id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (21, 3, 'Em prorrogação', 1);

SELECT setval('tramitacao.condicao_id_condicao_seq', max(id_condicao)) FROM tramitacao.condicao;

-- Adicionando novas ações do licenciamento

INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (43, 'Prorrogar licença', 1, 1);
INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (44, 'Arquivar prorrogação por renovação', 1, 1);

SELECT setval('tramitacao.acao_id_acao_seq', max(id_acao)) FROM tramitacao.acao;

-- Adicionanando novas transições do licenciamento

INSERT INTO tramitacao.transicao(id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (47, 43, 14, 21, NULL, NULL);
INSERT INTO tramitacao.transicao(id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (48, 44, 21, 6, NULL, NULL);


SELECT setval('tramitacao.transicao_id_transicao_seq', max(id_transicao)) FROM tramitacao.transicao;

--35

-- Adicionar novas condições do manejo digital

INSERT INTO tramitacao.condicao(id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (22, 4, 'Manejo digital Aguardando Análise de Shape', 1);
INSERT INTO tramitacao.condicao(id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (23, 4, 'Manejo digital em análise de Shape', 1);

-- Adicionar novas ações do manejo digital

INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (45, 'Iniciar análise de Shape do manejo florestal', 1, 1);
INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (46, 'Finalizar análise de Shape do manejo florestal', 1, 1);

-- Adicionar novas tramitações do manejo digital

INSERT INTO tramitacao.transicao(id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (49, 45, 22, 23, NULL, NULL);
INSERT INTO tramitacao.transicao(id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (50, 46, 23, 17, NULL, NULL);

-- Alterando fluxo de manejo digital

UPDATE tramitacao.fluxo SET id_condicao_inicial = 22 WHERE id_fluxo = 2;

--37

-- Alterar condições do manejo digital

UPDATE tramitacao.condicao SET nm_condicao = 'Manejo digital apto' WHERE id_condicao = 19;
UPDATE tramitacao.condicao SET nm_condicao = 'Manejo digital inapto' WHERE id_condicao = 20;

--38

-- Adicionar/Alterar ações do manejo digital

UPDATE tramitacao.acao SET tx_descricao = 'Indeferir análise do shape do manejo digital' WHERE id_acao = 42;
INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (48, 'Indeferir análise técnica do manejo digital', 1, 1);

-- Adicionar novas transições do manejo digital

INSERT INTO tramitacao.transicao(id_transicao, id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (52, 48, 18, 20, NULL, NULL);


--40
SELECT setval('tramitacao.acao_id_acao_seq', max(id_acao)) FROM tramitacao.acao;

INSERT INTO tramitacao.acao(tx_descricao, fl_ativo, fl_tramitavel) VALUES
('Deferir análise GEO via gerente', 1, 1),
('Indeferir análise GEO via gerente', 1, 1),
('Iniciar análise GEO', 1, 1),
('Invalidar parecer GEO pelo gerente', 1, 1),
('Validar deferimento GEO pelo gerente', 1, 1),
('Validar indeferimento GEO pelo gerente', 1, 1),
('Solicitar ajustes parecer GEO pelo gerente', 1, 1),
('Solicitar ajuste da análise GEO pelo presidente', 1, 1),
('Resolver notificação GEO', 1, 1),
('Invalidar parecer GEO encaminhando para outro GEO', 1, 1);

SELECT setval('tramitacao.etapa_id_etapa_seq', max(id_etapa)) FROM tramitacao.etapa;

INSERT INTO tramitacao.etapa(id_fluxo, tx_etapa, dt_prazo) VALUES
(1, 'Análise GEO', null);

SELECT setval('tramitacao.condicao_id_condicao_seq', max(id_condicao)) FROM tramitacao.condicao;

INSERT INTO tramitacao.condicao(id_etapa, nm_condicao, fl_ativo) VALUES
((SELECT id_etapa FROM tramitacao.etapa where tx_etapa ilike 'Análise GEO'), 'Aguardando vinculação GEO pelo gerente', 1),
((SELECT id_etapa FROM tramitacao.etapa where tx_etapa ilike 'Análise GEO'), 'Aguardando análise GEO', 1),
((SELECT id_etapa FROM tramitacao.etapa where tx_etapa ilike 'Análise GEO'), 'Em análise GEO', 1),
((SELECT id_etapa FROM tramitacao.etapa where tx_etapa ilike 'Análise GEO'), 'Aguardando validação GEO pelo gerente', 1);

SELECT setval('tramitacao.situacao_id_situacao_seq', max(id_situacao)) FROM tramitacao.situacao;

INSERT INTO tramitacao.situacao(tx_descricao, fl_ativo)
VALUES ('Notificado via GEO', 1);

--41

INSERT INTO tramitacao.condicao(id_etapa, nm_condicao, fl_ativo) VALUES
((SELECT id_etapa FROM tramitacao.etapa where tx_etapa = 'Análise GEO'), 'Aguardando validação gerente', 1),
((SELECT id_etapa FROM tramitacao.etapa where tx_etapa = 'Análise GEO'), 'Aguardando validação pela diretoria', 1);

--42
DELETE FROM tramitacao.config_situacao WHERE id_transicao IS NOT NULL;
DELETE FROM tramitacao.impedimento_transicao WHERE id_transicao IS NOT NULL;
DELETE FROM tramitacao.transicao;

ALTER SEQUENCE tramitacao.transicao_id_transicao_seq RESTART WITH 1;
ALTER SEQUENCE tramitacao.impedimento_transicao_id_impedimento_transicao_seq RESTART WITH 1;
ALTER SEQUENCE tramitacao.config_situacao_id_config_situacao_seq RESTART WITH 1;

INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final) VALUES (18, 0, 25);
INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final) VALUES (51, 25, 26);
INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final) VALUES (49, 26, 27);
INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final) VALUES (50, 26, 27);
INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final) VALUES (52, 27, 25);
INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final) VALUES (55, 27, 25);
INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final) VALUES (53, 27, 29);
INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final) VALUES (54, 27, 29);
INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final) VALUES (3, 26, 4);
INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final) VALUES (58, 29, 25);
INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final) VALUES (58, 29, 25);
INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final) VALUES (10, 27, 8);

UPDATE tramitacao.fluxo SET id_condicao_inicial = 25 WHERE id_fluxo = 1;

--43
ALTER TABLE licenciamento.empreendimento ADD COLUMN possui_anexo BOOLEAN;

COMMENT ON COLUMN licenciamento.empreendimento.possui_anexo IS 'Boooleano que indica se o empreendimento posui ou não anexos';

UPDATE licenciamento.empreendimento SET possui_anexo = false WHERE possui_anexo is null;

ALTER TABLE licenciamento.empreendimento ALTER COLUMN possui_anexo SET NOT NULL;
ALTER TABLE licenciamento.empreendimento ALTER COLUMN possui_anexo SET DEFAULT FALSE;


--44
ALTER TABLE licenciamento.empreendimento ALTER COLUMN possui_anexo DROP NOT NULL;
ALTER TABLE licenciamento.empreendimento ALTER COLUMN possui_anexo SET DEFAULT NULL;
ALTER TABLE licenciamento.empreendimento RENAME COLUMN possui_anexo TO possui_shape;
COMMENT ON COLUMN licenciamento.empreendimento.possui_shape IS 'Boooleano que indica se o empreendimento posui ou não upload de shapes, se é nulo o empreendimento nunca cadastrou shapes';

--45
INSERT INTO tramitacao.condicao(id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES
(30, (SELECT id_etapa FROM tramitacao.etapa where tx_etapa = 'Análise GEO'), 'Solicitação de desvínculo pendente', 1);

INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES
(59, 'Solicitar desvínculo', 1, 1),
(60, 'Aprovar solicitação de desvínculo', 1, 1),
(61, 'Negar solicitação de desvínculo', 1, 1);

INSERT INTO tramitacao.transicao(id_transicao, id_acao, id_condicao_inicial, id_condicao_final) VALUES (13, 59, 25, 30);
INSERT INTO tramitacao.transicao(id_transicao, id_acao, id_condicao_inicial, id_condicao_final) VALUES (14, 60, 30, 25);
INSERT INTO tramitacao.transicao(id_transicao, id_acao, id_condicao_inicial, id_condicao_final) VALUES (15, 61, 30, 25);

SELECT setval('tramitacao.condicao_id_condicao_seq', max(id_condicao)) FROM tramitacao.condicao;
SELECT setval('tramitacao.acao_id_acao_seq', max(id_acao)) FROM tramitacao.acao;
SELECT setval('tramitacao.transicao_id_transicao_seq', max(id_transicao)) FROM tramitacao.transicao;



--50
INSERT INTO tramitacao.etapa(id_etapa, id_fluxo, tx_etapa) VALUES (6, 1, 'Análise gerente');

INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (31, 6, 'Em análise pelo gerente', 1);
INSERT INTO tramitacao.acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (62, 'Iniciar análise gerente', 1, 1);

INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (62, 27, 31, NULL, NULL);
INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (62, 10, 31, NULL, NULL);

--51
INSERT INTO tramitacao.acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (63, 'Validar parecer geo pelo gerente', 1, 1);

INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (55, 31, 26, NULL, NULL);
INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (63, 31, 8, NULL, NULL);
INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (58, 31, 26, NULL, NULL);


--52
UPDATE tramitacao.acao SET tx_descricao = REPLACE(tx_descricao, 'processo', 'protocolo') WHERE tx_descricao LIKE '%processo%';

--53
INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final) VALUES (39, 25, 25);

--55
UPDATE tramitacao.acao SET tx_descricao = REPLACE(tx_descricao, 'Deferir análise GEO via gerente', 'Deferir análise GEO') WHERE id_acao = 49;
UPDATE tramitacao.acao SET tx_descricao = REPLACE(tx_descricao, 'Indeferir análise GEO via gerente', 'Indeferir análise GEO') WHERE id_acao = 50;

--56
INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final) VALUES (11, 8, 9);

--57
INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (64, 'Resolver comunicado', 1, 1);
INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (65, 'Aguardar resposta comunicado', 1, 1);

INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (32, 5, 'Aguardando resposta comunicado', 1);

INSERT INTO tramitacao.transicao(id_transicao, id_acao, id_condicao_inicial, id_condicao_final) VALUES (30, 64, 32, 27);
INSERT INTO tramitacao.transicao(id_transicao, id_acao, id_condicao_inicial, id_condicao_final) VALUES (31, 65, 26, 32);

--59

UPDATE tramitacao.condicao SET nm_condicao = 'Solicitação de desvínculo pendente análise GEO' WHERE nm_condicao = 'Solicitação de desvínculo pendente' ;

INSERT INTO tramitacao.condicao(id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES
(33, (SELECT id_etapa FROM tramitacao.etapa where tx_etapa = 'Análise técnica'), 'Solicitação de desvínculo pendente análise técnica', 1);

INSERT INTO tramitacao.transicao (id_transicao, id_acao, id_condicao_inicial, id_condicao_final) VALUES (32, 59, 8, 33);


--62

INSERT INTO tramitacao.etapa (id_etapa, id_fluxo, tx_etapa, dt_prazo) VALUES (7, 1, 'Análise finalizada', NULL);

INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES (34, 7, 'Análise finalizada', 1);

SELECT setval('tramitacao.condicao_id_condicao_seq', coalesce(max(id_condicao), 1)) FROM tramitacao.condicao;

--69
UPDATE tramitacao.transicao SET id_condicao_final = 25 WHERE id_acao = 58 AND id_condicao_inicial = 31;

--71
INSERT INTO tramitacao.transicao(id_condicao_inicial,id_condicao_final,id_acao) 
    VALUES (9,10,13);
SELECT setval('tramitacao.transicao_id_transicao_seq', coalesce(max(id_transicao), 1)) FROM tramitacao.transicao;

--72
INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final) 
    VALUES (12, 9, 10);
SELECT setval('tramitacao.transicao_id_transicao_seq', coalesce(max(id_transicao), 1)) FROM tramitacao.transicao;

--73
UPDATE tramitacao.acao SET tx_descricao='Deferir análise GEO com comunicado' WHERE id_acao = 65;

--74
UPDATE tramitacao.condicao SET id_etapa = 7 WHERE id_condicao = 6;

--75
UPDATE tramitacao.acao SET tx_descricao='Notificar pelo Analista GEO' WHERE id_acao = 3;

INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES 
    (66, 'Notificar pelo Analista técnico', 1, 1);

UPDATE tramitacao.condicao SET id_etapa = 5, nm_condicao = 'Notificado pelo Analista GEO' WHERE id_condicao = 4;

INSERT INTO tramitacao.condicao(id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES 
    (35, 2, 'Notificado pelo Analista técnico', 1);

INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final) VALUES 
    (66, 9, 35);

--77
INSERT INTO tramitacao.acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES 
    (67, 'Iniciar análise técnica gerente', 1, 1);

INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES 
    (36, 6, 'Em análise técnica pelo gerente', 1);

INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES 
    (67, 10, 36, NULL, NULL);

--78

INSERT INTO tramitacao.acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES (68, 'Validar parecer técnico gerente', 1, 1);

INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (15, 36, 9, NULL, NULL);
INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (68, 36, 29, NULL, NULL);
INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES (25, 36, 8, NULL, NULL);

-- 81

UPDATE tramitacao.acao SET tx_descricao = 'Solicitar desvínculo análise Geo' WHERE tx_descricao = 'Solicitar desvínculo';

INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES
    (69, 'Aprovar solicitação de desvínculo do Analista técnico', 1, 1);
INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES
    (70, 'Negar solicitação de desvínculo do Analista técnico', 1, 1);
INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES
    (71, 'Solicitar desvínculo análise técnica', 1, 1);

DELETE FROM tramitacao.transicao WHERE id_acao = 59 AND id_condicao_inicial = 8 AND id_condicao_final = 33;

INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final) VALUES
    (69, 33, 8);
INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final) VALUES
    (70, 33, 8);
INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final) VALUES
    (71, 8, 33);

--83
INSERT INTO licenciamento.rel_tipo_sobreposicao_orgao(id_tipo_sobreposicao, id_orgao) VALUES (19, 2);

--85
INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior)
VALUES (37, 35, 6, NULL, NULL);

INSERT INTO tramitacao.acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel)
VALUES (73, 'Iniciar analise Técnica por volta de notificação', 1, 1);

INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior)
VALUES (73, 25, 8, NULL, NULL);

--87

INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES 
    (72, 'Iniciar análise pelo diretor', 1, 1);

INSERT INTO tramitacao.condicao(id_condicao,id_etapa,nm_condicao,fl_ativo) VALUES
(37,2,'Em análise pelo diretor',1);

INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final) VALUES 
    (72, 29, 37);

--89
INSERT INTO tramitacao.acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES 
    (74, 'Validar análise pelo diretor', 1, 1);
INSERT INTO tramitacao.acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES 
    (75, 'Invalidar análise pelo diretor', 1, 1);

INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES 
    (74, 37, 11, NULL, NULL); 

INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES 
    (75, 37, 11, NULL, NULL);

--90
UPDATE tramitacao.condicao SET nm_condicao='Aguardando assinatura do presidente' WHERE id_condicao=11;

INSERT INTO tramitacao.acao(id_acao,tx_descricao,fl_ativo,fl_tramitavel) VALUES
    (76,'Iniciar análise do presidente',1,1);

INSERT INTO tramitacao.condicao(id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES 
    (38, 7, 'Em análise pelo presidente', 1);

INSERT INTO tramitacao.transicao(id_acao, id_condicao_inicial, id_condicao_final) VALUES 
    (76, 11, 38);

--92
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

--93
INSERT INTO tramitacao.acao(id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES 
    (78, 'Aprovar solicitação de licença', 1, 1),
    (79, 'Negar solicitação de licença', 1, 1);

INSERT INTO tramitacao.condicao(id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES 
    (40, 7, 'Solicitação da licença aprovada', 1),
    (41, 7, 'Solicitação da licença negada', 1);

INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES 
    (78, 38, 40, NULL, NULL),
    (79, 38, 41, NULL, NULL);

--96
INSERT INTO tramitacao.condicao (id_condicao, id_etapa, nm_condicao, fl_ativo) VALUES 
    (39, 1, 'Aguardando resposta jurídica', 1);

INSERT INTO tramitacao.acao (id_acao, tx_descricao, fl_ativo, fl_tramitavel) VALUES 
    (77, 'Resolver análise jurídica', 1, 1);

UPDATE tramitacao.transicao SET id_condicao_final = 39 WHERE id_acao = 63; 

INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) VALUES 
    (77, 39, 8, NULL, NULL);

--97
INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final) 
    VALUES (32, 40, 15);
SELECT setval('tramitacao.transicao_id_transicao_seq', coalesce(max(id_transicao), 1)) FROM tramitacao.transicao;

INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final) 
    VALUES (34, 40, 16);
SELECT setval('tramitacao.transicao_id_transicao_seq', coalesce(max(id_transicao), 1)) FROM tramitacao.transicao;


--98
DELETE FROM tramitacao.transicao WHERE id_acao = 37 and id_condicao_final=6;

INSERT INTO tramitacao.transicao (id_acao, id_condicao_inicial, id_condicao_final, dt_prazo, fl_retornar_fluxo_anterior) 
    SELECT 37, id_condicao, 6, null, null FROM tramitacao.condicao WHERE id_condicao NOT IN (6, 14, 15, 16, 34);



# --- !Downs

DROP SCHEMA tramitacao CASCADE;