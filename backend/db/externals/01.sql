# --- !Ups

INSERT INTO portal_seguranca.modulo(alvo, chave, data_cadastro, descricao, logotipo, nome, sigla, ativo, url, fixo) VALUES (0, 'apRWmiizr4Hq6aL672wwjwIjAE9n7Rm56RlkmqkYJdsCDmkLsSWGnGzXnkgQw8L6', now(), 'Análise das solicitações de licença', '3.png', 'Análise', 'ANL', true, 'http://localhost:9011/authenticate', true);

# --- !Downs


DELETE FROM portal_seguranca.modulo WHERE nome ='Análise' and sigla =  'ANL' and chave = 'apRWmiizr4Hq6aL672wwjwIjAE9n7Rm56RlkmqkYJdsCDmkLsSWGnGzXnkgQw8L6';
