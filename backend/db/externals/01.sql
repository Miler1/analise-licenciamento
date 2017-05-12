# --- !Ups

INSERT INTO portal_seguranca.modulo(alvo, chave, data_cadastro, descricao, logotipo, nome, sigla, ativo, url, fixo) VALUES (0, 'apRWmiizr4Hq6aL672wwjwIjAE9n7Rm56RlkmqkYJdsCDmkLsSWGnGzXnkgQw8L6', now(), 'Análise das solicitações de licença', '3.png', 'Análise', 'ANL', true, 'http://localhost:9011/authenticate', true);

INSERT INTO portal_seguranca.permissao(codigo, data_cadastro, nome, id_modulo) VALUES ('acessarAnaliseLicenciamento', now(), 'Acessar Analise Licenciamento', (SELECT id from portal_seguranca.modulo where sigla = 'ANL'));

INSERT INTO portal_seguranca.permissao_perfil(id_perfil, id_permissao) VALUES (2, (SELECT id FROM portal_seguranca.permissao WHERE codigo = 'acessarAnaliseLicenciamento'));

# --- !Downs

DELETE FROM portal_seguranca.permissao_perfil WHERE id_perfil=2 AND id_permissao=(SELECT id FROM portal_seguranca.permissao WHERE codigo = 'acessarAnaliseLicenciamento');

DELETE FROM portal_seguranca.permissao WHERE codigo='acessarAnaliseLicenciamento';

DELETE FROM portal_seguranca.modulo WHERE nome ='Análise';
