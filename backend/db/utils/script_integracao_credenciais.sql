UPDATE portal_seguranca.cliente_oauth SET id_cliente = 'eb6f4f10a403c38b3225eca778fba0f528c40eaba0ba9d8be967e6533dfa7ab69c1ee2cbb059c634c8c0c735f0714de6b53072493b3328e5d02781470351e2ea',
 segredo = 'ed083340bf2bab15d6da92856a03d80f150cb3460969e337f63f6fc162dee75f83636a28adaad9b3092235b21d55ec9e978cde5de5b51373a53f5368308c42bd' WHERE id = (SELECT id_cliente_oauth
 FROM portal_seguranca.modulo WHERE sigla = 'ALA');