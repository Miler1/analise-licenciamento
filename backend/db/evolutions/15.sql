# --- !Ups

ALTER TABLE analise.licenca_analise ADD COLUMN emitir BOOLEAN;
COMMENT ON COLUMN analise.licenca_analise.emitir IS 'Flag que indica se irá emitir a licença(True: emite, False: não emite e Null: aguardando ação do usuário).';

# --- !Downs

ALTER TABLE analise.licenca_analise DROP COLUMN emitir;