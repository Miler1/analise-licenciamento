#!/bin/bash

### Inicialização

SELF_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
LOG_FILE="$SELF_DIR/logs/deploy.log"

source "$SELF_DIR/utils/utils.sh"
source "$SELF_DIR/utils/play.sh"

HELP_TEXT="
	Uso correto: `basename $0` <ambiente> <numero_versão> <opções>
	
	Obs: os ambientes estão configurados em arquivos de nome conf/nome_do_ambiente.sh
	
	Opções disponíveis:
	      -nocode    Não incluir o código fonte
	      -deploy    Executa o deploy no servidor
	      -help      Ajuda"


show_title "Deploy"

validate_files_exist "$ROOT_FOLDER/backend" "$ROOT_FOLDER/frontend"

DIST_FOLDER="$ROOT_FOLDER/dist"

### PARÂMETROS

ambiente=$1
version=$2
remove_code=false
execute_deploy=false
current_date=$(date +'%d/%m/%Y %H:%M')

if [ -z "$ambiente" -o -z "$version" ]; then
	show_help "Argumentos inválidos."
fi


### AMBIENTE: Verificando ambiente e arquivos de configuração

path_config_ambiente="$SELF_DIR/conf/$ambiente.sh"
application_conf="ambientes/$ambiente.conf"

if [ ! -f $path_config_ambiente ]; then
	show_help "Ambiente inválido. Não existe o arquivo de configuração '$path_config_ambiente'."
fi

validate_files_exist "$ROOT_FOLDER/backend/conf/$application_conf"

source "$path_config_ambiente"


### PARÂMETROS EXTRAS

for var in "$@"
do
	case $var in
		-nocode) remove_code=true;;
		-deploy) execute_deploy=true;;
		-help) ajuda;;
	esac
done


### LIMPANDO PASTAS

show_msg "Limpando pastas ..."

if [ -d $DIST_FOLDER ]; then 
	rm -Rf $DIST_FOLDER
fi

mkdir $DIST_FOLDER
mkdir $DIST_FOLDER/backend
rm -Rf $ROOT_FOLDER/backend/precompiled


### FRONTEND

show_msg "Configurando frontend ..."
cd "$ROOT_FOLDER/frontend"
gulp dist


### BACKEND

show_msg "Configurando backend ..."
cd "$ROOT_FOLDER/backend"

# Copiando demais pastas necessárias
cp -Rf $ROOT_FOLDER/backend/conf $DIST_FOLDER/backend/
cp -Rf $ROOT_FOLDER/backend/lib $DIST_FOLDER/backend/
cp -Rf $ROOT_FOLDER/backend/public $DIST_FOLDER/backend/
cp -Rf $ROOT_FOLDER/backend/app $DIST_FOLDER/backend/
cp -Rf $ROOT_FOLDER/backend/modules $DIST_FOLDER/backend/
cp -Rf $ROOT_FOLDER/backend/templates $DIST_FOLDER/backend/

if $remove_code; then
	error "Ainda não implementada a remoção do código fonte."
	# rm -Rf $DIST_FOLDER/backend/app
fi

# Adicionando include das configurações de backend
echo "" >> $DIST_FOLDER/backend/conf/application.conf
echo "@include.production = $application_conf" >> $DIST_FOLDER/backend/conf/application.conf

# Adicionando informações de versionamento nas configurações
echo "" >> $DIST_FOLDER/backend/conf/application.conf
echo "server.version=$version" >> $DIST_FOLDER/backend/conf/application.conf
echo "server.update=$current_date" >> $DIST_FOLDER/backend/conf/application.conf

# Zipando pasta para deploy
echo "-- Gerando arquivo ZIP ($zip_dist_file):"
cd $DIST_FOLDER
zip_sulfix=$(date +'%Y-%m-%d_%H-%M')
zip_file="(licenciamento_pa)_analise_$zip_sulfix.zip"
zip -r $zip_file backend


echo ""
echo "-- Versão para deploy gerada com sucesso na pasta:"
echo "      $DIST_FOLDER "


### DEPLOY

if $execute_deploy; then

	date=`date +%Y_%m_%d:%H:%M:%S`

	show_msg "${server_ssh}"

	if [ ! "${server_ssh}" ];then
		error "Configuração 'server_ssh' não definida para este ambiente."
	fi

	# Copiando arquivo backend.zip para o servidor

 	show_msg "Executando deploy no servidor: ${ambiente}"
 	show_msg "Copiando arquivo $zip_dist_file para o servidor de testes"

	scp "${DIST_FOLDER}/${zip_file}" "$server_ssh:$server_home_folder"

	# Acessando o servidor de deploy

	echo -n "Digite a senha do servidor > "
	read server_password

	ssh -T $server_ssh << ATUALIZACAO

		echo -e "\n--> Extraindo arquivo $zip_file"
		cd $server_home_folder

		if [ -d backend ]; then
			rm -Rf backend
		fi

		unzip $zip_file

		$server_password += "\n"

		echo -e "\n--> Parando a aplicação"
		echo -e $server_password | sudo -S service $server_aplicacao_service stop

		echo -e "\n--> Gerando backup da aplicação"
		cd $server_path_aplicacao
		echo -e $server_password | sudo -S mv backend "backend_$date"

		echo -e "\n--> Copiando aplicação para o diretório final"
		echo -e $server_password | sudo -S mv "${server_home_folder}backend" $server_path_aplicacao

		echo -e "\n--> Startando a aplicação"
		echo -e $server_password | sudo -S service $server_aplicacao_service ${service_start}

ATUALIZACAO

fi
