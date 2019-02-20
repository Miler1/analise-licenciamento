package controllers;

import models.EntradaUnica.CodigoPerfil;
import models.Processo;
import models.licenciamento.AtividadeCaracterizacao;
import models.licenciamento.TipoCaracterizacaoAtividade;
import security.Acao;

import java.util.List;

public class Coordenadores extends InternalController {
	
	public static void getCoordenadoresAprovacao(String codigoPerfil, Long idProcesso) {
		
		verificarPermissao(Acao.VALIDAR_PARECERES_JURIDICO_TECNICO);
		
		if(codigoPerfil.equals(CodigoPerfil.COORDENADOR_TECNICO)) {
			
			Processo processo = Processo.findById(idProcesso);
			
			List<AtividadeCaracterizacao> atividadesCaracterizacao = processo.caracterizacoes.get(0).atividadesCaracterizacao;
			
			TipoCaracterizacaoAtividade tipoAtividadeCaracterizacao = 
					TipoCaracterizacaoAtividade.findTipoCaracterizacaoAtividadeByAtividadesCaracterizacao(atividadesCaracterizacao);		
			
			// sobe na hierarquia até encontrar o coordenador técnico
			//TODO REFACTOR
//			Setor setor = tipoAtividadeCaracterizacao.setor;
//			while(setor.setorPai != null && !setor.tipoSetor.equals(TipoSetor.COORDENADORIA)) {
//				setor = setor.setorPai;
//			}
//
//
//			renderJSON(UsuarioLicenciamento.getUsuariosByPerfilSetor(idPerfil, setor.id),
//					UsuarioSerializer.getConsultoresAnalistasGerentes);
		}
		
//		renderJSON(UsuarioLicenciamento.getUsuariosByPerfil(idPerfil),
//				UsuarioSerializer.getConsultoresAnalistasGerentes);
	}

}
