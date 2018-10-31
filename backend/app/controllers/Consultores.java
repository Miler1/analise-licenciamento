package controllers;

import java.util.List;

import models.Processo;
import models.portalSeguranca.Perfil;
import models.portalSeguranca.UsuarioLicenciamento;
import security.Acao;
import serializers.UsuarioSerializer;
import utils.Mensagem;

public class Consultores extends InternalController {

	public static void vincularAnaliseConsultorJuridico(Long idUsuario, Long... idsProcesso) {
		
		verificarPermissao(Acao.VINCULAR_PROCESSO_JURIDICO);
		
		UsuarioLicenciamento consultor = UsuarioLicenciamento.findById(idUsuario);
		UsuarioLicenciamento usuarioSessao = getUsuarioSessao();
		UsuarioLicenciamento usuarioExecutor = UsuarioLicenciamento.findById(usuarioSessao.id, usuarioSessao.perfilSelecionado, usuarioSessao.setorSelecionado);
		
		for(Long idProcesso : idsProcesso) {
			
			Processo processo = Processo.findById(idProcesso);
			
				processo.vincularConsultor(consultor, usuarioExecutor);
			
		}
		
		renderMensagem(Mensagem.CONSULTOR_VINCULADO_SUCESSO);
		
	}
	
	public static void getConsultoresJuridicos() {
		
		verificarPermissao(Acao.VINCULAR_PROCESSO_JURIDICO, Acao.VALIDAR_PARECERES_JURIDICO_TECNICO);
		
		List<UsuarioLicenciamento> consultores = UsuarioLicenciamento.getUsuariosByPerfil(Perfil.CONSULTOR_JURIDICO);
		
		renderJSON(consultores, UsuarioSerializer.getConsultoresAnalistasGerentes);
	}
	
}
