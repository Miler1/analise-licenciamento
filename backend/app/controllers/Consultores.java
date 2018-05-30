package controllers;

import java.util.List;

import models.Processo;
import models.portalSeguranca.Perfil;
import models.portalSeguranca.Usuario;
import security.Acao;
import security.UsuarioSessao;
import serializers.UsuarioSerializer;
import utils.Mensagem;

public class Consultores extends InternalController {

	public static void vincularAnaliseConsultorJuridico(Long idUsuario, Long... idsProcesso) {
		
		verificarPermissao(Acao.VINCULAR_PROCESSO_JURIDICO);
		
		Usuario consultor = Usuario.findById(idUsuario);				
		UsuarioSessao usuarioSessao = getUsuarioSessao();
		Usuario usuarioExecultor = Usuario.findById(usuarioSessao.id);
		usuarioExecultor.perfilSelecionado = usuarioSessao.perfilSelecionado;
		usuarioExecultor.setorSelecionado = usuarioSessao.setorSelecionado;
		
		for(Long idProcesso : idsProcesso) {
			
			Processo processo = Processo.findById(idProcesso);
			
				processo.vincularConsultor(consultor, usuarioExecultor);
			
		}
		
		renderMensagem(Mensagem.CONSULTOR_VINCULADO_SUCESSO);
		
	}
	
	public static void getConsultoresJuridicos() {
		
		verificarPermissao(Acao.VINCULAR_PROCESSO_JURIDICO, Acao.VALIDAR_PARECERES_JURIDICO_TECNICO);
		
		List<Usuario> consultores = Usuario.getUsuariosByPerfil(Perfil.CONSULTOR_JURIDICO);
		
		renderJSON(consultores, UsuarioSerializer.getConsultoresAnalistasGerentes);
	}
	
}
