package controllers;

import models.EntradaUnica.CodigoPerfil;
import models.Processo;
import models.licenciamento.AtividadeCaracterizacao;
import models.licenciamento.TipoCaracterizacaoAtividade;
import models.UsuarioAnalise;
import security.Acao;
import serializers.UsuarioSerializer;
import utils.Mensagem;

import java.util.List;

public class GerentesTecnicos extends InternalController {

	public static void vincularAnaliseGerenteTecnico(Long idUsuario, Long... idsProcesso) {
		
		verificarPermissao(Acao.VINCULAR_PROCESSO_TECNICO);

		UsuarioAnalise usuarioAnalise = UsuarioAnalise.findById(idUsuario);

		UsuarioAnalise gerente = UsuarioAnalise.getUsuarioByLogin(usuarioAnalise.login);

		UsuarioAnalise usuarioExecutor = getUsuarioSessao();
		
		for(Long idProcesso : idsProcesso) {
			
			Processo processo = Processo.findById(idProcesso);
			
			processo.vincularGerenteTecnico(gerente, usuarioExecutor);
			
		}
		
		renderMensagem(Mensagem.GERENTE_VINCULADO_SUCESSO);		
	}
	
	public static void getGerentesTecnicosByIdProcesso(Long idProcesso) {
		
		verificarPermissao(Acao.VINCULAR_PROCESSO_TECNICO);
		
		Processo processo = Processo.findById(idProcesso);
		
		List<AtividadeCaracterizacao> atividadesCaracterizacao = processo.caracterizacoes.get(0).atividadesCaracterizacao;
		
		TipoCaracterizacaoAtividade tipoAtividadeCaracterizacao = 
				TipoCaracterizacaoAtividade.findTipoCaracterizacaoAtividadeByAtividadesCaracterizacao(atividadesCaracterizacao);
		
		List<UsuarioAnalise> consultores = UsuarioAnalise.getUsuariosByPerfilSetor(CodigoPerfil.GERENTE_TECNICO, tipoAtividadeCaracterizacao.atividade.siglaSetor);
		
		renderJSON(consultores, UsuarioSerializer.getConsultoresAnalistasGerentes);
	}	
	
	public static void getGerentesTecnicos() {
		
		verificarPermissao(Acao.VINCULAR_PROCESSO_TECNICO);
		
		List<UsuarioAnalise> gerentes = UsuarioAnalise.getUsuariosByPerfil(CodigoPerfil.GERENTE_TECNICO);
		
		renderJSON(gerentes, UsuarioSerializer.getConsultoresAnalistasGerentes);
	}
	
}
