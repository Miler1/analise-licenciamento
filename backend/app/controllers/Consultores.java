package controllers;

import models.Processo;
import models.portalSeguranca.Usuario;
import security.Acao;
import security.UsuarioSessao;
import utils.Mensagem;

public class Consultores extends InternalController {

	public static void vincularAnalise(Long idUsuario, Boolean isJuridica, Long...idsProcesso) {
		
		verificarPermissao(Acao.VINCULAR_PROCESSO);
		
		Usuario consultor = Usuario.findById(idUsuario);
		UsuarioSessao usuarioSessao = getUsuarioSessao();
		Usuario usuarioExecultor = Usuario.findById(usuarioSessao.id);
		
		for(Long idProcesso : idsProcesso) {
			
			Processo processo = Processo.findById(idProcesso);
			
			if(isJuridica)
				processo.vincularConsultorJuridico(consultor, usuarioExecultor);
			
		}
		
		renderJSON(Mensagem.CONSULTOR_VINCULADO_SUCESSO);
		
	}
	
}
