package controllers;

import models.EntradaUnica.CodigoPerfil;
import models.EntradaUnica.Setor;
import models.Processo;
import models.licenciamento.AtividadeCaracterizacao;
import models.licenciamento.TipoCaracterizacaoAtividade;
import enums.TipoSetor;
import models.UsuarioAnalise;
import security.Acao;
import serializers.UsuarioSerializer;

import java.util.List;

public class Coordenadores extends InternalController {
	
	public static void getCoordenadoresAprovacao(String codigoPerfil, Long idProcesso) {
		
		verificarPermissao(Acao.VALIDAR_PARECERES_JURIDICO_TECNICO);
		
		if(codigoPerfil.equals(CodigoPerfil.COORDENADOR_TECNICO)) {
			
			Processo processo = Processo.findById(idProcesso);
			
			List<AtividadeCaracterizacao> atividadesCaracterizacao = processo.caracterizacoes.get(0).atividadesCaracterizacao;
			
			TipoCaracterizacaoAtividade tipoAtividadeCaracterizacao = 
					TipoCaracterizacaoAtividade.findTipoCaracterizacaoAtividadeByAtividadesCaracterizacao(atividadesCaracterizacao);

			Setor setor = getUsuarioSessao().usuarioEntradaUnica.setorSelecionado;
			while(setor.setorPai != null && !setor.tipo.equals(TipoSetor.COORDENADORIA)) {
				setor = setor.setorPai;
			}

			renderJSON(UsuarioAnalise.getUsuariosByPerfilSetor(codigoPerfil, setor.sigla),
					UsuarioSerializer.getConsultoresAnalistasGerentes);
		}
		
		renderJSON(UsuarioAnalise.getUsuariosByPerfil(codigoPerfil),
				UsuarioSerializer.getConsultoresAnalistasGerentes);
	}

}
