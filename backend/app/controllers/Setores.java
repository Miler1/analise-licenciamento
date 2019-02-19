package controllers;

import java.util.List;

import exceptions.AppException;
import models.portalSeguranca.Setor;
import models.portalSeguranca.TipoSetor;
import models.portalSeguranca.UsuarioLicenciamento;
import security.Acao;
import serializers.SetoresSerializer;
import utils.Mensagem;

public class Setores extends InternalController {
	
	public static void getSetoresByNivel(Integer nivel){
		
		verificarPermissao(Acao.LISTAR_PROCESSO);
		
		UsuarioLicenciamento usuarioSessao = getUsuarioSessao();
		
		if (usuarioSessao.setorSelecionado == null) {
			throw new AppException(Mensagem.SETOR_OBRIGATORIO_ANALISE);
		}
		
		Setor setorPai = Setor.findById(usuarioSessao.setorSelecionado.id);
		
		if (setorPai == null) {
			throw new AppException(Mensagem.SETOR_NAO_ENCONTRADO);
		}
		
		List<Setor> setoresFilhos = setorPai.getSetoresByNivel(nivel);
		
		renderJSON(setoresFilhos, SetoresSerializer.getSetoresByNivel);
	}
	
	public static void getSetoresByTipo(TipoSetor tipoSetor) {
		
		verificarPermissao(Acao.LISTAR_PROCESSO);
		
		List<Setor> setores = Setor.find("tipoSetor = :tipoSetor")
					.setParameter("tipoSetor", tipoSetor)
					.fetch();
		
		renderJSON(setores, SetoresSerializer.getSetoresByNivel);		
	}
}
