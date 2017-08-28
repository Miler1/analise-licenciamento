package controllers;

import java.util.List;

import models.portalSeguranca.Perfil;
import models.portalSeguranca.Usuario;
import security.Acao;
import serializers.UsuarioSerializer;

public class Coordenadores extends InternalController {
	
	public static void getCoordenadores(Integer idPerfil) {
		
		verificarPermissao(Acao.APROVAR_ANALISE);
		
		List<Usuario> gerentes = Usuario.getUsuariosByPerfil(idPerfil);
		
		renderJSON(gerentes, UsuarioSerializer.getConsultoresAnalistasGerentes);
	}

}
