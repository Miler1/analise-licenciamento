package controllers;

import java.util.List;

import models.Processo;
import models.licenciamento.AtividadeCaracterizacao;
import models.licenciamento.Caracterizacao;
import models.licenciamento.TipoCaracterizacaoAtividade;
import models.portalSeguranca.Perfil;
import models.portalSeguranca.Usuario;
import play.db.jpa.JPABase;
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
		
		AtividadeCaracterizacao atividadeCaracterizacao = processo.caracterizacoes.get(0).atividadeCaracterizacao;
		
		TipoCaracterizacaoAtividade tipoAtividadeCaracterizacao = 
				TipoCaracterizacaoAtividade.find("atividade.id = :idAtividade and atividadeCnae.id :idAtividadeCnane")
					.setParameter("idAtividade",atividadeCaracterizacao.atividade.id)
					.setParameter("idAtividadeCnane", atividadeCaracterizacao.atividadeCnae.id)
					.first();
		
		List<Usuario> consultores = Usuario.getUsuariosByPerfilSetor(Perfil.ANALISTA_TECNICO,tipoAtividadeCaracterizacao.setor.id);
		
		renderJSON(consultores, UsuarioSerializer.getConsultoresAnalistasGerentes);
	}
	
	public static void getAnalistaTecnicoPerfil() {
		
		verificarPermissao(Acao.VINCULAR_PROCESSO_TECNICO);
		
		List<Usuario> consultores = Usuario.getUsuariosByPerfil(Perfil.ANALISTA_TECNICO);
		
		renderJSON(consultores, UsuarioSerializer.getConsultoresAnalistasGerentes);		
		
	}
	
	public static void getAnalistaTecnicoPerfilSetores() {
		
		verificarPermissao(Acao.VINCULAR_PROCESSO_TECNICO);
		
		UsuarioSessao usuarioSessao = getUsuarioSessao();

		List<Integer> idsSetoresFilhos = usuarioSessao.setorSelecionado.getIdsSetoresFilhos();
		
		List<Usuario> consultores = Usuario.getUsuariosByPerfilSetores(Perfil.ANALISTA_TECNICO, idsSetoresFilhos);
		
		renderJSON(consultores, UsuarioSerializer.getConsultoresAnalistasGerentes);		
		
	}
	
	
}
