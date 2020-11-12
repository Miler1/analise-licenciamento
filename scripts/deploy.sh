#!/bin/bash

source "play-shell-utils/play-shell-utils.sh"

### Configurações

config_project_name="analise-licenciamento-ap"
config_play_version='1.5.0'

config_backend_app_conf_include_path="ambientes/<env>.conf"


dp_execute_deploy_procedure "$1"  
