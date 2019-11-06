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
			
			List<AtividadeCaracterizacao> atividadesCaracterizacao = processo.caracterizacao.atividadesCaracterizacao;
			
			TipoCaracterizacaoAtividade tipoAtividadeCaracterizacao = 
					TipoCaracterizacaoAtividade.findTipoCaracterizacaoAtividadeByAtividadesCaracterizacao(atividadesCaracterizacao);

			br.ufla.lemaf.beans.pessoa.Setor setor = getUsuarioSessao().usuarioEntradaUnica.setorSelecionado;

			renderJSON(UsuarioAnalise.findUsuariosByPerfilAndSetor(codigoPerfil, setor.sigla),
					UsuarioSerializer.getConsultoresAnalistasGerentes);
		}
		
		renderJSON(UsuarioAnalise.getUsuariosByPerfil(codigoPerfil),
				UsuarioSerializer.getConsultoresAnalistasGerentes);
	}

}
