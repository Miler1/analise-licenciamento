package controllers;

import java.util.List;

import models.Processo;
import models.licenciamento.AtividadeCaracterizacao;
import models.licenciamento.TipoCaracterizacaoAtividade;
import models.portalSeguranca.Perfil;
import models.portalSeguranca.Setor;
import models.portalSeguranca.TipoSetor;
import models.portalSeguranca.UsuarioLicenciamento;
import security.Acao;
import serializers.UsuarioSerializer;

public class Coordenadores extends InternalController {
	
	public static void getCoordenadoresAprovacao(Integer idPerfil, Long idProcesso) {
		
		verificarPermissao(Acao.VALIDAR_PARECERES_JURIDICO_TECNICO);
		
		if(idPerfil == Perfil.COORDENADOR_TECNICO) {
			
			Processo processo = Processo.findById(idProcesso);
			
			List<AtividadeCaracterizacao> atividadesCaracterizacao = processo.caracterizacoes.get(0).atividadesCaracterizacao;
			
			TipoCaracterizacaoAtividade tipoAtividadeCaracterizacao = 
					TipoCaracterizacaoAtividade.findTipoCaracterizacaoAtividadeByAtividadesCaracterizacao(atividadesCaracterizacao);		
			
			// sobe na hierarquia até encontrar o coordenador técnico
			Setor setor = tipoAtividadeCaracterizacao.setor;
			while(setor.setorPai != null && !setor.tipoSetor.equals(TipoSetor.COORDENADORIA)) {
				setor = setor.setorPai;
			}
			
			
			renderJSON(UsuarioLicenciamento.getUsuariosByPerfilSetor(idPerfil, setor.id),
					UsuarioSerializer.getConsultoresAnalistasGerentes);
		}
		
		renderJSON(UsuarioLicenciamento.getUsuariosByPerfil(idPerfil),
				UsuarioSerializer.getConsultoresAnalistasGerentes);
	}

}
