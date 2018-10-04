# --- !Ups

-- Criação da tabela base_vetorial

CREATE TABLE analise.base_vetorial (
 id SERIAL NOT NULL,
 nome VARCHAR(200) NOT NULL,
 fonte VARCHAR(200) NOT NULL,
 ultima_atualizacao TIMESTAMP NOT NULL,
 escala VARCHAR(200) NOT NULL,
 observacao TEXT NOT NULL,
 CONSTRAINT pk_base_vetorial PRIMARY KEY(id)
);

COMMENT ON TABLE analise.base_vetorial IS 'Entidade responsável por armazenas os metadados da base vetorial oficial utilizada na análise.';
COMMENT ON COLUMN analise.base_vetorial.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.base_vetorial.nome IS 'Nome da base vetorial.';
COMMENT ON COLUMN analise.base_vetorial.fonte IS 'Fonte da base vetorial.';
COMMENT ON COLUMN analise.base_vetorial.ultima_atualizacao IS 'Data da ultima atualização da base.';
COMMENT ON COLUMN analise.base_vetorial.escala IS 'Escala da base vetorial.';
COMMENT ON COLUMN analise.base_vetorial.observacao IS 'Observação da base vetorial.';

ALTER TABLE analise.base_vetorial OWNER TO postgres;
GRANT ALL ON TABLE analise.base_vetorial TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.base_vetorial TO licenciamento_pa;
GRANT SELECT, USAGE ON SEQUENCE analise.base_vetorial_id_seq TO licenciamento_pa;


-- Criação da tabela imovel_manejo

CREATE TABLE analise.imovel_manejo (
 id SERIAL NOT NULL,
 registro_car VARCHAR(500) NOT NULL,
 area_total_imovel_documentado DOUBLE PRECISION NOT NULL,
 area_liquida_imovel DOUBLE PRECISION NOT NULL,
 area_reserva_legal DOUBLE PRECISION,
 area_preservacao_permanente DOUBLE PRECISION,
 area_remanescente_vegetacao_nativa DOUBLE PRECISION,
 area_corpos_agua DOUBLE PRECISION,
 area_uso_consolidado DOUBLE PRECISION,
 CONSTRAINT pk_imovel_manejo PRIMARY KEY(id)
);

COMMENT ON TABLE analise.imovel_manejo IS 'Entidade responsável por armazenas o imóvel de uma análise de manejo.';
COMMENT ON COLUMN analise.imovel_manejo.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.imovel_manejo.registro_car IS 'Registro do imóvel no CAR/PA.';
COMMENT ON COLUMN analise.imovel_manejo.area_total_imovel_documentado IS 'Área total documentada do imóvel.';
COMMENT ON COLUMN analise.imovel_manejo.area_liquida_imovel IS 'Área liquida do imóvel.';
COMMENT ON COLUMN analise.imovel_manejo.area_reserva_legal IS 'Área de reserva legal do imóvel.';
COMMENT ON COLUMN analise.imovel_manejo.area_preservacao_permanente IS 'Área de preservação permanente do imóvel.';
COMMENT ON COLUMN analise.imovel_manejo.area_remanescente_vegetacao_nativa IS 'Área de remanescente de vegetação nativa do imóvel.';
COMMENT ON COLUMN analise.imovel_manejo.area_corpos_agua IS 'Área de corpos de agua do imóvel.';
COMMENT ON COLUMN analise.imovel_manejo.area_uso_consolidado IS 'Área de uso consolidado do imóvel.';

ALTER TABLE analise.imovel_manejo OWNER TO postgres;
GRANT ALL ON TABLE analise.imovel_manejo TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.imovel_manejo TO licenciamento_pa;
GRANT SELECT, USAGE ON SEQUENCE analise.imovel_manejo_id_seq TO licenciamento_pa;


-- Criação da tabela analise_manejo

CREATE TABLE analise.analise_manejo (
 id SERIAL NOT NULL,
 data TIMESTAMP NOT NULL,
 dias_analise INTEGER NOT NULL,
 path_arquivo_shape VARCHAR(500) NOT NULL,
 path_anexo VARCHAR(500),
 analise_temporal TEXT NOT NULL,
 area_manejo_florestal_solicitada DOUBLE PRECISION NOT NULL,
 area_preservacao_permanente DOUBLE PRECISION,
 area_servidao DOUBLE PRECISION,
 area_antropizada_nao_consolidada DOUBLE PRECISION,
 area_consolidada DOUBLE PRECISION,
 area_uso_restrito DOUBLE PRECISION,
 area_sem_potencial DOUBLE PRECISION,
 area_corpos_agua DOUBLE PRECISION,
 area_embargada_ibama DOUBLE PRECISION,
 area_embargada_ldi DOUBLE PRECISION,
 area_seletiva_ndfi DOUBLE PRECISION,
 area_efetivo_manejo DOUBLE PRECISION,
 area_com_exploraca_ndfi_baixo DOUBLE PRECISION,
 area_com_exploraca_ndfi_medio DOUBLE PRECISION,
 area_sem_previa_exploracao DOUBLE PRECISION,
 consideracoes TEXT NOT NULL,
 conclusao TEXT NOT NULL,
 id_usuario INTEGER NOT NULL,
 CONSTRAINT pk_analise_manejo PRIMARY KEY(id),
 CONSTRAINT fk_am_u FOREIGN KEY (id_usuario) REFERENCES portal_seguranca.usuario (id)
);

COMMENT ON TABLE analise.analise_manejo IS 'Entidade responsável por armazenas uma análise de manejo.';
COMMENT ON COLUMN analise.analise_manejo.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.analise_manejo.dias_analise IS 'Quantidade de dias corridos desde o inicio da análise.';
COMMENT ON COLUMN analise.analise_manejo.data IS 'Data do inicio da análise de manejo.';
COMMENT ON COLUMN analise.analise_manejo.path_arquivo_shape IS 'Caminho onde está armazenado o arquivo shape da análise.';
COMMENT ON COLUMN analise.analise_manejo.path_anexo IS 'Caminho onde está armazenado o anéxo da análise.';
COMMENT ON COLUMN analise.analise_manejo.analise_temporal IS 'Dados da análise temporal.';
COMMENT ON COLUMN analise.analise_manejo.area_manejo_florestal_solicitada IS 'Área de manejo florestal solicitada em hectares.';
COMMENT ON COLUMN analise.analise_manejo.area_preservacao_permanente IS 'Área de preservação permanente em hectares definida pela análise.';
COMMENT ON COLUMN analise.analise_manejo.area_servidao IS 'Área de servidão em hectares definida pela análise.';
COMMENT ON COLUMN analise.analise_manejo.area_antropizada_nao_consolidada IS 'Área antropizada não consolidada em hectares definida pela análise.';
COMMENT ON COLUMN analise.analise_manejo.area_consolidada IS 'Área consolidada em hectares definida pela análise.';
COMMENT ON COLUMN analise.analise_manejo.area_uso_restrito IS 'Área de uso restrito em hectares definida pela análise.';
COMMENT ON COLUMN analise.analise_manejo.area_sem_potencial IS 'Área sem potencial em hectares definida pela análise.';
COMMENT ON COLUMN analise.analise_manejo.area_corpos_agua IS 'Área dos corpos de agua em hectares defindos pela análise.';
COMMENT ON COLUMN analise.analise_manejo.area_embargada_ibama IS 'Área embargada pelo IBAMA em hectares.';
COMMENT ON COLUMN analise.analise_manejo.area_embargada_ldi IS 'Área embargada pelo LDI em hectares.';
COMMENT ON COLUMN analise.analise_manejo.area_efetivo_manejo IS 'Área de efetivo manejo em hectares definida pela análise.';
COMMENT ON COLUMN analise.analise_manejo.area_com_exploraca_ndfi_baixo IS 'Área com exploração NDFI (Baixo) em hectares.';
COMMENT ON COLUMN analise.analise_manejo.area_com_exploraca_ndfi_medio IS 'Área com exploração NDFI (Médio) em hectares.';
COMMENT ON COLUMN analise.analise_manejo.area_sem_previa_exploracao IS 'Área sem previa exploração em hectares.';
COMMENT ON COLUMN analise.analise_manejo.consideracoes IS 'Considerações da análise.';
COMMENT ON COLUMN analise.analise_manejo.conclusao IS 'Notas de conclusão da análise.';
COMMENT ON COLUMN analise.analise_manejo.id_usuario IS 'Identificador da entidade usuário que denota o usuário responsável por fazer a análise.';

ALTER TABLE analise.analise_manejo OWNER TO postgres;
GRANT ALL ON TABLE analise.analise_manejo TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.analise_manejo TO licenciamento_pa;
GRANT SELECT, USAGE ON SEQUENCE analise.analise_manejo_id_seq TO licenciamento_pa;


-- Criação da tabela rel_base_vetorial_analise_manejo

CREATE TABLE analise.rel_base_vetorial_analise_manejo (
 id SERIAL NOT NULL,
 id_base_vetorial INTEGER NOT NULL,
 id_analise_manejo INTEGER NOT NULL,
 CONSTRAINT pk_rel_base_vetorial_analise_manejo PRIMARY KEY(id),
 CONSTRAINT fk_rbvam_bv FOREIGN KEY (id_base_vetorial) REFERENCES analise.base_vetorial (id),
 CONSTRAINT fk_rbvam_am FOREIGN KEY (id_analise_manejo) REFERENCES analise.analise_manejo (id)
);

COMMENT ON TABLE analise.rel_base_vetorial_analise_manejo IS 'Entidade responsável por armazenas o relacionamento entre análise manejo e a base vetorial.';
COMMENT ON COLUMN analise.rel_base_vetorial_analise_manejo.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.rel_base_vetorial_analise_manejo.id_base_vetorial IS 'Identificador da base vetorial.';
COMMENT ON COLUMN analise.rel_base_vetorial_analise_manejo.id_analise_manejo IS 'Identificador da análise manejo.';

ALTER TABLE analise.rel_base_vetorial_analise_manejo OWNER TO postgres;
GRANT ALL ON TABLE analise.rel_base_vetorial_analise_manejo TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.rel_base_vetorial_analise_manejo TO licenciamento_pa;
GRANT SELECT, USAGE ON SEQUENCE analise.rel_base_vetorial_analise_manejo_id_seq TO licenciamento_pa;


-- Criação da tabela analise_vetorial

CREATE TABLE analise.analise_vetorial (
 id SERIAL NOT NULL,
 tipo VARCHAR(200) NOT NULL,
 nome VARCHAR(200) NOT NULL,
 distancia_propriedade DOUBLE PRECISION NOT NULL,
 sobreposicao_propriedade DOUBLE PRECISION NOT NULL,
 distancia_amf DOUBLE PRECISION NOT NULL,
 sobreposicao_amf DOUBLE PRECISION NOT NULL,
 observacao TEXT NOT NULL,
 id_analise_manejo INTEGER NOT NULL,
 CONSTRAINT pk_analise_vetorial PRIMARY KEY(id),
 CONSTRAINT fk_av_am FOREIGN KEY (id_analise_manejo) REFERENCES analise.analise_manejo (id)
);

COMMENT ON TABLE analise.analise_vetorial IS 'Entidade responsável por armazenas os dados de uma análise vetorial.';
COMMENT ON COLUMN analise.analise_vetorial.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.analise_vetorial.tipo IS 'Tipo da análise vetorial.';
COMMENT ON COLUMN analise.analise_vetorial.nome IS 'Nome da análise vetorial.';
COMMENT ON COLUMN analise.analise_vetorial.distancia_propriedade IS 'Distância da propriedade em kilometros.';
COMMENT ON COLUMN analise.analise_vetorial.sobreposicao_propriedade IS 'Sobreposição da propriedade em hectares.';
COMMENT ON COLUMN analise.analise_vetorial.distancia_amf IS 'Distância da AMF em kilometros.';
COMMENT ON COLUMN analise.analise_vetorial.sobreposicao_amf IS 'Sobreposição da AMF em hectares.';
COMMENT ON COLUMN analise.analise_vetorial.observacao IS 'Observação da análise vetorial.';
COMMENT ON COLUMN analise.analise_vetorial.id_analise_manejo IS 'Identificador da análise manejo.';

ALTER TABLE analise.analise_vetorial OWNER TO postgres;
GRANT ALL ON TABLE analise.analise_vetorial TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.analise_vetorial TO licenciamento_pa;
GRANT SELECT, USAGE ON SEQUENCE analise.analise_vetorial_id_seq TO licenciamento_pa;


-- Criação da tabela analise_ndfi

CREATE TABLE analise.analise_ndfi (
 id SERIAL NOT NULL,
 data TIMESTAMP NOT NULL,
 orbita INTEGER NOT NULL,
 ponto INTEGER NOT NULL,
 satelite VARCHAR(200) NOT NULL,
 nivel_exploracao VARCHAR(200) NOT NULL,
 valor_ndfi DOUBLE PRECISION NOT NULL,
 area DOUBLE PRECISION NOT NULL,
 id_analise_manejo INTEGER NOT NULL,
 CONSTRAINT pk_analise_ndfi PRIMARY KEY(id),
 CONSTRAINT fk_an_am FOREIGN KEY (id_analise_manejo) REFERENCES analise.analise_manejo (id)
);

COMMENT ON TABLE analise.analise_ndfi IS 'Entidade responsável por armazenas os dados de uma análise NDFI.';
COMMENT ON COLUMN analise.analise_ndfi.id IS 'Data da análise NDFI.';
COMMENT ON COLUMN analise.analise_ndfi.orbita IS 'Orbita da análise NDFI.';
COMMENT ON COLUMN analise.analise_ndfi.ponto IS 'Ponto da análise NDFI.';
COMMENT ON COLUMN analise.analise_ndfi.satelite IS 'Satélite usado na análise.';
COMMENT ON COLUMN analise.analise_ndfi.nivel_exploracao IS 'Nível de exploração da análise.';
COMMENT ON COLUMN analise.analise_ndfi.valor_ndfi IS 'Valor do NDFI.';
COMMENT ON COLUMN analise.analise_ndfi.area IS 'Área da análise NDFI em hectares.';
COMMENT ON COLUMN analise.analise_ndfi.id_analise_manejo IS 'Identificador da análise manejo.';

ALTER TABLE analise.analise_ndfi OWNER TO postgres;
GRANT ALL ON TABLE analise.analise_ndfi TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.analise_ndfi TO licenciamento_pa;
GRANT SELECT, USAGE ON SEQUENCE analise.analise_ndfi_id_seq TO licenciamento_pa;


-- Criação da tabela observacao

CREATE TABLE analise.observacao (
 id SERIAL NOT NULL,
 texto TEXT NOT NULL,
 id_analise_manejo INTEGER NOT NULL,
 num_passo SMALLINT NOT NULL,
 CONSTRAINT pk_observacao PRIMARY KEY(id),
 CONSTRAINT fk_obs_am FOREIGN KEY (id_analise_manejo) REFERENCES analise.analise_manejo (id)
);

COMMENT ON TABLE analise.observacao IS 'Entidade responsável por armazenas as observações de uma análise de manejo.';
COMMENT ON COLUMN analise.observacao.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.observacao.texto IS 'Conteúdo da observação.';
COMMENT ON COLUMN analise.observacao.id_analise_manejo IS 'Identificador da entidade analise_manejo que faz o relacionamento entre as duas entidades.';
COMMENT ON COLUMN analise.observacao.num_passo IS 'Número do passo em que a observação foi inserida.';

ALTER TABLE analise.observacao OWNER TO postgres;
GRANT ALL ON TABLE analise.observacao TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.observacao TO licenciamento_pa;
GRANT SELECT, USAGE ON SEQUENCE analise.observacao_id_seq TO licenciamento_pa;


-- Criação da tabela processo_manejo

CREATE TABLE analise.processo_manejo (
 id SERIAL NOT NULL,
 num_processo VARCHAR(200) NOT NULL,
 id_empreendimento_simlam INTEGER NOT NULL,
 cpf_cnpj_empreendimento VARCHAR(20) NOT NULL,
 denominacao_empreendimento_simlam VARCHAR(1000) NOT NULL,
 id_municipio_simlam INTEGER NOT NULL,
 nome_municipio_simlam VARCHAR(1000) NOT NULL,
 id_tipo_licenca INTEGER NOT NULL,
 nome_tipo_licenca VARCHAR(200) NOT NULL,
 id_imovel_manejo INTEGER NOT NULL,
 id_analise_manejo INTEGER,
 CONSTRAINT pk_processo_manejo PRIMARY KEY(id),
 CONSTRAINT fk_pm_im FOREIGN KEY (id_imovel_manejo) REFERENCES analise.imovel_manejo (id),
 CONSTRAINT fk_pm_am FOREIGN KEY (id_analise_manejo) REFERENCES analise.analise_manejo (id)
);

COMMENT ON TABLE analise.processo_manejo IS 'Entidade responsável por armazenas os dados de um processo de manejo do SIMLAM.';
COMMENT ON COLUMN analise.processo_manejo.id IS 'Identificador único da entidade.';
COMMENT ON COLUMN analise.processo_manejo.num_processo IS 'Número do processo.';
COMMENT ON COLUMN analise.processo_manejo.id_empreendimento_simlam IS 'Identificador do empreendimento no SIMLAM.';
COMMENT ON COLUMN analise.processo_manejo.cpf_cnpj_empreendimento IS 'CPF ou CNPJ do empreendimento no SIMLAM.';
COMMENT ON COLUMN analise.processo_manejo.denominacao_empreendimento_simlam IS 'Denominação do empreeendimento no SIMLAM.';
COMMENT ON COLUMN analise.processo_manejo.id_municipio_simlam IS 'Identificador do município no SIMLAM.';
COMMENT ON COLUMN analise.processo_manejo.nome_municipio_simlam IS 'Nome do município no SIMLAM.';
COMMENT ON COLUMN analise.processo_manejo.id_tipo_licenca IS 'Identificador do tipo de licença de manejo do SIMLAM.';
COMMENT ON COLUMN analise.processo_manejo.nome_tipo_licenca IS 'Nome do tipo de licença de manejo do SIMLAM.';
COMMENT ON COLUMN analise.processo_manejo.id_imovel_manejo IS 'Identificador da imóvelo do manejo.';
COMMENT ON COLUMN analise.processo_manejo.id_analise_manejo IS 'Identificador da análise manejo.';

ALTER TABLE analise.processo_manejo OWNER TO postgres;
GRANT ALL ON TABLE analise.processo_manejo TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE analise.processo_manejo TO licenciamento_pa;
GRANT SELECT, USAGE ON SEQUENCE analise.processo_manejo_id_seq TO licenciamento_pa;


# --- !Downs

DROP TABLE analise.processo_manejo;
DROP TABLE analise.observacao;
DROP TABLE analise.analise_ndfi;
DROP TABLE analise.analise_vetorial;
DROP TABLE analise.rel_base_vetorial_analise_manejo;
DROP TABLE analise.analise_manejo;
DROP TABLE analise.imovel_manejo;
DROP TABLE analise.base_vetorial;