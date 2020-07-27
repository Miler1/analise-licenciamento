# --- !Ups

--- Alterando texto das considerações

UPDATE analise.consideracao
 SET texto = 'Toda a análise da GEOTEC/DIGEO foi realizada com base em dados apresentados pelo próprio técnico responsável, passível de sanções administrativas conforme art. 66 da Lei n° 9605 de 12/02/1998 e art. 82 do Decreto n° 6514 de 22/07/2008.'
 WHERE texto = 'Toda a análise da GEOTEC foi realizada com base em dados apresentados pelo próprio Técnico Responsável, passível de sanções administrativas conforme art. 66 da Lei n° 9605 de 12/02/1998 e art. 82 do Decreto n° 6514 de 22/07/2008.';

UPDATE analise.consideracao
 SET texto = 'As informações utilizadas, até a presente data, para análise do processo da GEOTEC/DIGEO, foram disponibilizados do banco de dados de Raster pela GTDI, DIGEO  e Vetorial GEOSIG/DIGEO.'
 WHERE texto = 'As informações utilizadas, até a presente data, para analise do processo da GEOTEC, foram disponibilizadas do Banco de Dados de Raster e Vetores pela gerencia GTDI.';

UPDATE analise.consideracao
 SET texto = 'Consta no processo memorial e planta gerada automaticamente pelo SIGEF - Sistema de Gestão Fundiária, com base nas informações transmitidas e assinadas digitalmente pelo responsável técnico credenciado.'
 WHERE texto = 'De acordo com a INSTRUÇÃO NORMATIVA Nº 001, de 14 de janeiro de 2014, a APAT não permite o início das atividades de manejo, não autoriza a exploração florestal e nem se constitui em prova de posse ou propriedade para fins de regularização fundiários, de autorização de desmatamento ou de obtenção de financiamento junto às instituições de crédito públicas ou privados;';

# --- !Downs

UPDATE analise.consideracao
 SET texto = 'De acordo com a INSTRUÇÃO NORMATIVA Nº 001, de 14 de janeiro de 2014, a APAT não permite o início das atividades de manejo, não autoriza a exploração florestal e nem se constitui em prova de posse ou propriedade para fins de regularização fundiários, de autorização de desmatamento ou de obtenção de financiamento junto às instituições de crédito públicas ou privados;'
 WHERE texto = 'Consta no processo memorial e planta gerada automaticamente pelo SIGEF - Sistema de Gestão Fundiária, com base nas informações transmitidas e assinadas digitalmente pelo responsável técnico credenciado.';

UPDATE analise.consideracao
 SET texto = 'As informações utilizadas, até a presente data, para analise do processo da GEOTEC, foram disponibilizadas do Banco de Dados de Raster e Vetores pela gerencia GTDI.'
 WHERE texto = 'As informações utilizadas, até a presente data, para análise do processo da GEOTEC/DIGEO, foram disponibilizados do banco de dados de Raster pela GTDI, DIGEO  e Vetorial GEOSIG/DIGEO.';

UPDATE analise.consideracao
 SET texto = 'Toda a análise da GEOTEC foi realizada com base em dados apresentados pelo próprio Técnico Responsável, passível de sanções administrativas conforme art. 66 da Lei n° 9605 de 12/02/1998 e art. 82 do Decreto n° 6514 de 22/07/2008.'
 WHERE texto = 'Toda a análise da GEOTEC/DIGEO foi realizada com base em dados apresentados pelo próprio técnico responsável, passível de sanções administrativas conforme art. 66 da Lei n° 9605 de 12/02/1998 e art. 82 do Decreto n° 6514 de 22/07/2008.';