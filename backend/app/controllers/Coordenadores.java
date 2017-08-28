package controllers;

import java.util.List;

import models.Processo;
import models.licenciamento.TipoCaracterizacaoAtividade;
import models.portalSeguranca.Perfil;
import models.portalSeguranca.Usuario;
import security.Acao;
import serializers.UsuarioSerializer;

public class Coordenadores extends InternalController {
	
	public static void getCoordenadoresAprovacao(Integer idPerfil, Long idProcesso) {
		
		verificarPermissao(Acao.APROVAR_ANALISE);
		
		if(idPerfil == Perfil.COORDENADOR_TECNICO) {
			
			Processo processo = Processo.findById(idProcesso);
			
			TipoCaracterizacaoAtividade tipoAtividadeCaracterizacao = 
					TipoCaracterizacaoAtividade.find("atividade.id = :idAtividade and atividadeCnae.id = :idAtividadeCnae")
						.setParameter("idAtividade", processo.caracterizacoes.get(0).atividadeCaracterizacao.atividade.id)
						.setParameter("idAtividadeCnae", processo.caracterizacoes.get(0).atividadeCaracterizacao.atividadeCnae.id)
						.first();			
			
			renderJSON(Usuario.getUsuariosByPerfilSetor(idPerfil, tipoAtividadeCaracterizacao.setor.id), 
					UsuarioSerializer.getConsultoresAnalistasGerentes);
		}
		
		renderJSON(Usuario.getUsuariosByPerfil(idPerfil), 
				UsuarioSerializer.getConsultoresAnalistasGerentes);
	}

}
