package controllers;

import java.util.ArrayList;
import java.util.List;

import exceptions.AppException;
import models.portalSeguranca.Setor;
import models.portalSeguranca.Usuario;
import play.db.jpa.JPABase;
import security.Acao;
import security.UsuarioSessao;
import utils.Mensagem;

public class Setores extends InternalController {
	
	public void getSetoresFilhos(){
		
		verificarPermissao(Acao.LISTAR_PROCESSO_JURIDICO);
		
		UsuarioSessao usuarioSessao = getUsuarioSessao();
		Usuario usuarioExecutor = Usuario.findById(usuarioSessao.id);		
		
		Setor setorPai = Setor.findById(usuarioExecutor.setorSelecionado.id);
		
		if (setorPai == null) {
			throw new AppException(Mensagem.SETOR_NAO_ENCONTRADO);
		}
		
		List<Setor> setoresFilhos = setorPai.getSetoresFilhos();
		
		renderJSON(setoresFilhos);
	}
}
