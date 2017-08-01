package controllers;

import java.util.List;

import models.Processo;
import models.portalSeguranca.Perfil;
import models.portalSeguranca.Usuario;
import security.Acao;
import security.UsuarioSessao;
import serializers.UsuarioSerializer;
import utils.Mensagem;

public class GerentesTecnicos extends InternalController {

	public static void vincularAnaliseGerenteTecnico(Long idUsuario, Long... idsProcesso) {
		
		verificarPermissao(Acao.VINCULAR_PROCESSO_TECNICO);
		
		Usuario gerente = Usuario.findById(idUsuario);				
		UsuarioSessao usuarioSessao = getUsuarioSessao();
		Usuario usuarioExecultor = Usuario.findById(usuarioSessao.id);
		
		for(Long idProcesso : idsProcesso) {
			
			Processo processo = Processo.findById(idProcesso);
			
			processo.vincularGerenteTecnico(gerente, usuarioExecultor);
			
		}
		
		renderMensagem(Mensagem.GERENTE_VINCULADO_SUCESSO);		
	}
	
	public static void getGerenteTecnico() {
		
		verificarPermissao(Acao.VINCULAR_PROCESSO_TECNICO);
		
		List<Usuario> gerentes = Usuario.getUsuariosByPerfil(Perfil.GERENTE_TECNICO);
		
		renderJSON(gerentes, UsuarioSerializer.getConsultoresAnalistasGerentes);
	}
	
}
