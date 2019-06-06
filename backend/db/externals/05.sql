# --- !Ups

-- External Analise
update portal_seguranca.modulo set logotipo = 'modulo-analise.png' where sigla = 'ANL';


# --- !Downs


update portal_seguranca.modulo set logotipo = '3.png' where sigla = 'ANL';