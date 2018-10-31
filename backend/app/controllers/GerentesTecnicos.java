package controllers;

import java.util.List;

import models.Processo;
import models.licenciamento.AtividadeCaracterizacao;
import models.licenciamento.TipoCaracterizacaoAtividade;
import models.portalSeguranca.Perfil;
import models.portalSeguranca.UsuarioLicenciamento;
import security.Acao;
import serializers.UsuarioSerializer;
import utils.Mensagem;

public class GerentesTecnicos extends InternalController {

	public static void vincularAnaliseGerenteTecnico(Long idUsuario, Long... idsProcesso) {
		
		verificarPermissao(Acao.VINCULAR_PROCESSO_TECNICO);
		
		UsuarioLicenciamento gerente = UsuarioLicenciamento.findById(idUsuario);
		UsuarioLicenciamento usuarioSessao = getUsuarioSessao();
		UsuarioLicenciamento usuarioExecutor = UsuarioLicenciamento.findById(usuarioSessao.id, usuarioSessao.perfilSelecionado, usuarioSessao.setorSelecionado);
		
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
		
		List<UsuarioLicenciamento> consultores = UsuarioLicenciamento.getUsuariosByPerfilSetor(Perfil.GERENTE_TECNICO, tipoAtividadeCaracterizacao.setor.id);
		
		renderJSON(consultores, UsuarioSerializer.getConsultoresAnalistasGerentes);
	}	
	
	public static void getGerentesTecnicos() {
		
		verificarPermissao(Acao.VINCULAR_PROCESSO_TECNICO);
		
		List<UsuarioLicenciamento> gerentes = UsuarioLicenciamento.getUsuariosByPerfil(Perfil.GERENTE_TECNICO);
		
		renderJSON(gerentes, UsuarioSerializer.getConsultoresAnalistasGerentes);
	}
	
}
