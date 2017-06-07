# --- !Ups

-- External Analise
update portal_seguranca.modulo set logotipo = 'modulo-analise.png' where sigla = 'ANL';
update portal_seguranca.perfil set avatar = 'perfil-coordenador-juridico.png' where id = 3;
update portal_seguranca.perfil set avatar = 'perfil-administrativo-juridico.png' where id = 4;
update portal_seguranca.perfil set avatar = 'perfil-consultor-juridico.png' where id = 5;
update portal_seguranca.perfil set avatar = 'perfil-coordenador-tecnico.png' where id = 6;
update portal_seguranca.perfil set avatar = 'perfil-gerente-tecnico.png' where id = 7;
update portal_seguranca.perfil set avatar = 'perfil-analista-tecnico.png' where id = 8;
update portal_seguranca.perfil set avatar = 'perfil-diretor.png' where id = 9;

# --- !Downs

update portal_seguranca.perfil set avatar = '3.png' where id = 3;
update portal_seguranca.perfil set avatar = '4.png' where id = 4;
update portal_seguranca.perfil set avatar = '5.png' where id = 5;
update portal_seguranca.perfil set avatar = '6.png' where id = 6;
update portal_seguranca.perfil set avatar = '7.png' where id = 7;
update portal_seguranca.perfil set avatar = '8.png' where id = 8;
update portal_seguranca.perfil set avatar = '9.png' where id = 9;
update portal_seguranca.modulo set logotipo = '3.png' where sigla = 'ANL';