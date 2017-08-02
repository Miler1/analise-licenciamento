package controllers;

import java.util.List;

import models.Processo;
import models.licenciamento.Caracterizacao;
import models.portalSeguranca.Perfil;
import models.portalSeguranca.Usuario;
import security.Acao;
import security.UsuarioSessao;
import serializers.UsuarioSerializer;
import utils.Mensagem;

public class Analistas extends InternalController {

	public static  void vincularAnaliseAnalistaTecnico(Long idUsuario, String justificativaCoordenador, Long... idsProcesso) {
		
		verificarPermissao(Acao.VINCULAR_PROCESSO_TECNICO);
		
		Usuario analista = Usuario.findById(idUsuario);				
		UsuarioSessao usuarioSessao = getUsuarioSessao();
		Usuario usuarioExecultor = Usuario.findById(usuarioSessao.id);
		
		for(Long idProcesso : idsProcesso) {
			
			Processo processo = Processo.findById(idProcesso);
			
			processo.vincularAnalista(analista, usuarioExecultor, justificativaCoordenador);
			
		}
		
		renderMensagem(Mensagem.ANALISTA_VINCULADO_SUCESSO);
		
	}
	
	public static void getAnalistaTecnico(Long idProcesso) {
		
		verificarPermissao(Acao.VINCULAR_PROCESSO_TECNICO);
		
		Processo processo = Processo.findById(idProcesso);
		
		List<Usuario> consultores = Usuario.getUsuariosBySetor(processo.caracterizacoes.get(0).atividadeCaracterizacao.atividadeCnae.setor.id);
		
		renderJSON(consultores, UsuarioSerializer.getConsultoresAnalistasGerentes);
	}
	
}
