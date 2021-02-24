package controllers;

import models.EntradaUnica.CodigoPerfil;
import models.Processo;
import models.UsuarioAnalise;
import security.Acao;
import serializers.UsuarioSerializer;
import utils.Mensagem;

import java.util.List;

public class Consultores extends InternalController {

	public static void vincularAnaliseConsultorJuridico(Long idUsuario, Long... idsProcesso) {
		
		verificarPermissao(Acao.VINCULAR_PROCESSO_JURIDICO);

		UsuarioAnalise usuarioAnalise = UsuarioAnalise.findById(idUsuario);

		UsuarioAnalise consultor = UsuarioAnalise.getUsuarioEntradaUnicaByLogin(usuarioAnalise.login);
		UsuarioAnalise usuarioExecutor = getUsuarioSessao();
		
		for(Long idProcesso : idsProcesso) {
			
			Processo processo = Processo.findById(idProcesso);
			
				processo.vincularConsultor(consultor, usuarioExecutor);
			
		}
		
		renderMensagem(Mensagem.CONSULTOR_VINCULADO_SUCESSO);
		
	}
	
	public static void getConsultoresJuridicos() {
		
		verificarPermissao(Acao.VINCULAR_PROCESSO_JURIDICO, Acao.VALIDAR_PARECERES_JURIDICO_TECNICO, Acao.VINCULAR_PROCESSO);
		
		List<UsuarioAnalise> consultores = UsuarioAnalise.getUsuariosByPerfil(CodigoPerfil.CONSULTOR_JURIDICO);
		
		renderJSON(consultores, UsuarioSerializer.getConsultoresAnalistasCoordenadores);
	}
	
}
