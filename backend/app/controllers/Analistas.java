package controllers;

import java.util.List;

import models.Processo;
import models.licenciamento.AtividadeCaracterizacao;
import models.licenciamento.Caracterizacao;
import models.licenciamento.TipoCaracterizacaoAtividade;
import models.portalSeguranca.Perfil;
import models.portalSeguranca.Setor;
import models.portalSeguranca.Usuario;
import play.db.jpa.JPABase;
import security.Acao;
import security.UsuarioSessao;
import serializers.UsuarioSerializer;
import utils.Mensagem;

public class Analistas extends InternalController {

	public static void vincularAnaliseAnalistaTecnico(Long idUsuario, String justificativaCoordenador, Long... idsProcesso) {
		
		verificarPermissao(Acao.VINCULAR_PROCESSO_TECNICO);
		
		Usuario analista = Usuario.findById(idUsuario);				
		UsuarioSessao usuarioSessao = getUsuarioSessao();
		Usuario usuarioExecutor = Usuario.findById(usuarioSessao.id, usuarioSessao.perfilSelecionado, usuarioSessao.setorSelecionado);
		
		for(Long idProcesso : idsProcesso) {
			
			Processo processo = Processo.findById(idProcesso);
			
			processo.vincularAnalista(analista, usuarioExecutor, justificativaCoordenador);
			
		}
		
		renderMensagem(Mensagem.ANALISTA_VINCULADO_SUCESSO);
		
	}
	
	public static void getAnalistaTecnico(Long idProcesso) {
		
		verificarPermissao(Acao.VINCULAR_PROCESSO_TECNICO);
		
		Processo processo = Processo.findById(idProcesso);
		
		List<AtividadeCaracterizacao> atividadesCaracterizacao = processo.caracterizacoes.get(0).atividadesCaracterizacao;
		
		TipoCaracterizacaoAtividade tipoAtividadeCaracterizacao = 
				TipoCaracterizacaoAtividade.findTipoCaracterizacaoAtividadeByAtividadesCaracterizacao(atividadesCaracterizacao);
		
		List<Usuario> consultores = Usuario.getUsuariosByPerfilSetor(Perfil.ANALISTA_TECNICO,tipoAtividadeCaracterizacao.setor.id);
		
		renderJSON(consultores, UsuarioSerializer.getConsultoresAnalistasGerentes);
	}
	
	public static void getAnalistaTecnicoPerfil() {
		
		verificarPermissao(Acao.VINCULAR_PROCESSO_TECNICO, Acao.CONSULTAR_PROCESSO);
		
		List<Usuario> consultores = Usuario.getUsuariosByPerfil(Perfil.ANALISTA_TECNICO);
		
		renderJSON(consultores, UsuarioSerializer.getConsultoresAnalistasGerentes);		
		
	}
	
	public static void getAnalistaTecnicoPerfilSetores(boolean isGerente) {
		
		verificarPermissao(Acao.VINCULAR_PROCESSO_TECNICO, Acao.VALIDAR_PARECERES_JURIDICO_TECNICO);
		
		UsuarioSessao usuarioSessao = getUsuarioSessao();
		
		List<Integer> idsSetoresFilhos = null;
		List<Usuario> pessoas = null;
		switch (usuarioSessao.perfilSelecionado.id) {
		/**
		 * Nível 1 corresponde aos filhos e nível 2 aos netos e assim por diante na hieraquia. 
		 * Neste caso, colocamos o nível 2, pois aqui o aprovador está pesquisando e o setor dele
		 * é diretoria, então a hierarquia é Diretoria/Coordenadoria(1)/Gerência(2)
		 */
		case Perfil.APROVADOR:
			idsSetoresFilhos = usuarioSessao.setorSelecionado.getIdsSetoresByNivel(2);
			pessoas = Usuario.getUsuariosByPerfilSetores(Perfil.ANALISTA_TECNICO, idsSetoresFilhos);
			break;
		/**
		 * Nível 1 corresponde aos filhos e nível 2 aos netos e assim por diante na hieraquia. 
		 * Neste caso, colocamos o nível 1, pois aqui o coordenador está pesquisando e o setor dele
		 * é cordenadoria, então a hierarquia é Coordenadoria/Gerência(1)
		 */			
		case Perfil.COORDENADOR_TECNICO:
			idsSetoresFilhos = usuarioSessao.setorSelecionado.getIdsSetoresByNivel(1);
			pessoas = Usuario.getUsuariosByPerfilSetores(Perfil.ANALISTA_TECNICO, idsSetoresFilhos);
			break;
		/**
		 * No caso aqui seria o Gerente ou outros que estão no mesmo setor que os Analistas
		 */
		default:
			pessoas = Usuario.getUsuariosByPerfilSetor(Perfil.ANALISTA_TECNICO, usuarioSessao.setorSelecionado.id);
			break;
		}
		
		renderJSON(pessoas, UsuarioSerializer.getConsultoresAnalistasGerentes);
	}
	
	
}
