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

	public static  void vincularAnaliseAnalistaTecnico(Long idUsuario, Long... idsProcesso) {
		
		verificarPermissao(Acao.VINCULAR_PROCESSO_TECNICO);
		
		Usuario analista = Usuario.findById(idUsuario);				
		UsuarioSessao usuarioSessao = getUsuarioSessao();
		Usuario usuarioExecultor = Usuario.findById(usuarioSessao.id);
		
		for(Long idProcesso : idsProcesso) {
			
			Processo processo = Processo.findById(idProcesso);
			
			processo.vincularAnalista(analista, usuarioExecultor);
			
		}
		
		renderMensagem(Mensagem.ANALISTA_VINCULADO_SUCESSO);
		
	}
	
	public static void getAnalistaTecnico(Long idProcesso) {
		
		verificarPermissao(Acao.VINCULAR_PROCESSO_TECNICO);
		
		Processo processo = Processo.findById(idProcesso);
		Caracterizacao caracterizacao = Caracterizacao.findById(processo.caracterizacoes.get(0).id);
		
		
		//List<Usuario> consultores = Usuario.getUsuariosByPerfil(Perfil.ANALISTA_TECNICO);
		List<Usuario> consultores = Usuario.getUsuariosBySetor(caracterizacao.atividadeCaracterizacao.atividadeCnae.setor.id);
		
		renderJSON(consultores, UsuarioSerializer.getConsultoresEAnalistas);
	}
	
}
