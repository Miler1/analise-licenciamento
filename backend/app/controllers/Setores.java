package controllers;

import java.util.ArrayList;
import java.util.List;

import exceptions.AppException;
import models.portalSeguranca.Setor;
import models.portalSeguranca.TipoSetor;
import models.portalSeguranca.Usuario;
import play.db.jpa.JPABase;
import security.Acao;
import security.UsuarioSessao;
import serializers.SetoresSerializer;
import utils.Mensagem;

public class Setores extends InternalController {
	
	public void getSetoresFilhos(){
		
		verificarPermissao(Acao.LISTAR_PROCESSO_JURIDICO);
		
		UsuarioSessao usuarioSessao = getUsuarioSessao();
		
		if (usuarioSessao.setorSelecionado == null) {
			throw new AppException(Mensagem.SETOR_OBRIGATORIO_ANALISE);
		}
		
		Setor setorPai = Setor.findById(usuarioSessao.setorSelecionado.id);
		
		if (setorPai == null) {
			throw new AppException(Mensagem.SETOR_NAO_ENCONTRADO);
		}
		
		List<Setor> setoresFilhos = setorPai.getSetoresFilhos();
		
		renderJSON(setoresFilhos, SetoresSerializer.getSetoresFilhos);
	}
	
	public void getSetoresByTipo(TipoSetor tipoSetor) {
		
		verificarPermissao(Acao.LISTAR_PROCESSO_JURIDICO);
		
		List<Setor> setores = Setor.find("tipoSetor = :tipoSetor")
					.setParameter("tipoSetor", tipoSetor)
					.fetch();
		
		renderJSON(setores, SetoresSerializer.getSetoresFilhos);		
	}
}
