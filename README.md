# licensing_analysis
Brazilian Environmental Licensing Analysis Module - system derived from environmental licensing - used by members of the federal government to approve or disapprove of environmental licenses issued by private companies.

[« Home](home) / Tecnologias Utilizadas

# Tecnologias Utilizadas

### Seguem abaixo as tecnologias utilizadas no sistema:

- Tecnologia Backend: 
    - Java **`1.8`**
    - [Play Framework](http://www.playframework.com) versão **`1.5.0`**
    
- Tecnologia Frontend: 
    - HTML, CSS, Javascript
    - [Less](http://lesscss.org/)
    - [AngularJS](http://angularjs.org/) versão **`1.6.2`**
    - [Pug](https://pugjs.org)
    - [Gulp](http://gulpjs.com/)
    - [Bower](http://bower.io/)
    
- Banco de dados
  - [PostgreSQL](http://www.postgresql.org/) (banco de dados) - versão **`9.5`** ou maior
  - [PostGis](http://postgis.net/) (extensão gis para o postgreSQL) - versão **`2.2`** ou maior
  
  [« Home](home) / Configurações dos ambientes

# Configurações dos ambientes

### 1 - Pré-requisitos

Instalar as dependências das [tecnologias utilizadas](tecnologias utilizadas).

#### 1.1 - Instalando a JDK da Oracle

    sudo add-apt-repository ppa:webupd8team/java
    sudo apt-get update
    sudo apt-get install oracle-jdk8-installer

#### 1.2 - Instalando o PostgreSQL 

Instale o PostgreSQL pelo apt-get :

    sudo apt-get install postgresql

Inicialize o PostgreSQL com o comando:

    sudo service postgresql start

Caso houver alguma dependencia faltando, instale-a utilizando o apt-get install e re-execute o comando acima.

#### 1.3 - Instalando o PostGIS

Instale o PostGIS pelo apt-get :

    sudo apt-get install postgis

Pronto. Se tudo ocorreu bem, seu PostGIS foi instalado com sucesso.

#### 1.4 - Instalando o Node.js

Execute o seguinte comando no terminal para instalar o Curl:

    sudo apt-get install curl

Em seguida execute o comando a baixo: 

    curl --silent --location https://deb.nodesource.com/setup_0.12 | sudo bash -

Assim que terminar o comando anterior execute o seguinte para instalar o Node.js:

    sudo apt-get install nodejs

#### 1.5 - Instalando o Play Framework

Baixe o Play 1.5.0 no site do Play Framework:

    https://www.playframework.com/download#older-versions

Em seguida extraia o play na pasta onde você fez o download.

Execute o comando a baixo para criar a pasta do play na pasta `opt`:

    sudo mkdir /opt/playframework

Agora devemos mover a pasta do play para a pasta que acabamos de criar, para isso execute o comando (lembre-se de substituir `pasta_onde_extraiu_o_play` pela pasta onde você extraiu o play):

    sudo mv `pasta_onde_extraiu_o_play`/play-1.5.0 /opt/playframework/

Falta, agora, informar para o sistema onde o play está localizado, para isso executamos o comando (lembre-se de substituir X pela versão do play):

    sudo update-alternatives --install /usr/bin/play play /opt/playframework/play-1.5.0/play 1

Pronto! Agora para verificar se está tudo certo, é só digitar o comando:

    play -version

Se aparecer a versão que você baixou tudo está certo.

#### 1.6 - Instalando o PGAdmin

Instale o PGAdmin pelo apt-get :

    sudo apt-get install pgadmin3

#### 1.7 - Instalando o Tomcat

Execute os comandos abaixo no terminal:

    sudo apt-get install tomcat7 tomcat7-admin

### 2- Configuração do Banco de Dados ###

#### 2.1 - Criar banco de dados :

Executar os comandos abaixo no terminal

LINUX: 
    
Execute o seguinte comando no terminal para mudar o usuário para postgres:

    sudo su postgres

Execute os seguintes comandos para criar os bancos e configurar para que os mesmos utilizem o PostGIS:

    createdb -E UTF8 --lc-collate='pt_BR.UTF-8' --lc-ctype='pt_BR.UTF-8' -T template0 licenciamento_ms

    psql -d licenciamento_ms -f /usr/share/postgresql/[postgre version number]/contrib/postgis-[postgis version number]/postgis.sql     
    psql -d licenciamento_ms -f /usr/share/postgresql/[postgre version number]/contrib/postgis-[postgis version number]/spatial_ref_sys.sql
    psql -d licenciamento_ms -f /usr/share/postgresql/[postgre version number]/contrib/postgis-[postgis version number]/postgis_comments.sql
    psql -d licenciamento_ms -f /usr/share/postgresql/[postgre version number]/contrib/postgis-[postgis version number]/legacy.sql
    
    
    createdb -E UTF8 --lc-collate='pt_BR.UTF-8' --lc-ctype='pt_BR.UTF-8' -T template0 licenciamento_ms_test

    psql -d licenciamento_ms_test -f /usr/share/postgresql/[postgre version number]/contrib/postgis-[postgis version number]/postgis.sql     
    psql -d licenciamento_ms_test -f /usr/share/postgresql/[postgre version number]/contrib/postgis-[postgis version number]/spatial_ref_sys.sql
    psql -d licenciamento_ms_test -f /usr/share/postgresql/[postgre version number]/contrib/postgis-[postgis version number]/postgis_comments.sql
    psql -d licenciamento_ms_test -f /usr/share/postgresql/[postgre version number]/contrib/postgis-[postgis version number]/legacy.sql
    
-----

### 3 - Configurando a aplicação

Baixe o projeto:

    git@gitlab.ti.lemaf.ufla.br:Amazonas/analise.git


Entre na pasta do backend, baixe as dependências, e execute as evolutions para o banco de desenvolvimento e teste:

    cd  analise/backend
    play deps
    play evolutions:apply
    play evolutions:apply --%test

Volte à raiz do projeto e entre na pasta frontend. Depois execute os comandos abaixo para baixar dependências e configurar o frontend:

    npm install
    bower install (// para windows: /frontend, utilizando o gitbash)
    gulp

---------

#### 4 - Instalando a Tramitação

##### 4.1 - Configuração do Banco de Dados

Execute as externals para criar a estrutura da tramitação (na pasta db/external/)

##### 4.3 - Deploy da aplicação

Add WAR file: [Tramitacao.war]()

Execute os comandos abaixo no terminal:

    sudo rm /var/lib/tomcat7/webapps/Tramitacao.war
    sudo cp [Diretório onde foi feito o download]/Tramitacao.war /var/lib/tomcat7/webapps/
    sudo service tomcat7 restart

-----

### 5 - Testando a aplicação

Para testar o sistema, execute o comando `play run` na pasta backend e acesse o endereço abaixo:

    http://localhost:9011

---------
