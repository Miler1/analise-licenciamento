#!/bin/bash

source "play-shell-utils/play-shell-utils.sh"

### Configurações

config_project_name="analise-licenciamento-am"
config_play_version='1.5.0'

### Testes antes do deploy

# dist_conf="$ROOT_FOLDER/backend/"
# cd $dist_conf
# play autotest

# if [ $? -eq 0 ]
# then
#     ### Deploy
#     dp_execute_deploy_procedure "$1"    
# else
#     echo "Erro ao tentar executar os testes unitários/funcionais."
# fi    



dp_execute_deploy_procedure "$1"  
