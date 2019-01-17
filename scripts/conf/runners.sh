config_play_version='1.5.0'
config_update_server_app=true
config_server_ssh="deploy@runners.ti.lemaf.ufla.br"
config_server_release_folder_path="/home/deploy"
config_server_app_path="/var/play/amazonas/licenciamento_ambiental/analise/"
config_server_app_stop_cmd="systemctl stop analise-licenciamento.service"
config_server_app_start_cmd="systemctl start analise-licenciamento.service"
config_require_version_name=false
config_use_branch_as_version_name=true
config_backend_static_folders=("conf" "lib" "public" "modules")
