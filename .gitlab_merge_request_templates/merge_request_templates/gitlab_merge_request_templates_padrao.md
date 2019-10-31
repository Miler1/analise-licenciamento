## Padrão de Code Review - Análise Licenciamento - AM
#### Padrões de formatação de código

* [ ] Indentação do código usando tabs
* [ ] Indentação do `dependencies.yml` usando espaços
* [ ] Linha em branco no início e final de blocos
* [ ] Não mais que uma linha em branco no início e final de blocos
* [ ] Métodos devem ser separados por uma linha em branco

#### Padrões de nomeclatura

* [ ] Nomes de pacotes devem ser com letras minúsculas
* [ ] Nomes de classe devem ser substantivos, em maiúsculas e minúsculas com a primeira letra de cada palavra interna em maiúscula.
* [ ] Métodos devem ser verbos, com a letra minúscula em primeiro lugar, com a primeira letra de cada palavra interna em maiúscula.
* [ ] Nomes de variáveis devem ser intuitivos e não usar underscore
* [ ] Nomes de constantes devem ser todo em letras maiúsculas com palavras separadas por underscore

#### Padrões de organização

* [ ] Classes e métodos não devem ser grandes
* [ ] Verificar se classes com contextos diferentes estão em pacotes separados
* [ ] Verificar se a lógica e regras de negócio estã nos pacotes corretos


#### Padrões de banco de dados

* [ ] Verificar se evolutions e externals foram validadas pelo DBA e registrada na planilha de controle
* [ ] Verificar se existe alguma consulta desnecessária ou que pode ser otimizada

#### Padrões do projeto 

* [ ] Verificar se a atividade foi incluída no arquivo `changelog.md`
* [ ] Verificar se as ferramentas e tenologias utilizadas estão na versão estável mais recente
* [ ] Verificar se existe métodos com a mesma finalidade
* [ ] Verificar se a lógica utilizada no desenvolvimento faz sentido
* [ ] Verificar se existe excesso de estruturas condicionais e loops
* [ ] Verificar se as credencias foram comentadas no código
